package bd.july2024.world;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.VertexBuffer;
import com.jme3.scene.control.AbstractControl;
import com.jme3.texture.Texture;
import com.jme3.util.TangentBinormalGenerator;

import java.util.HashMap;
import java.util.Map;

/**
 * Loads the reduced Poly Haven tree models under {@code assets/Models/Trees} (see the LICENSE.txt
 * there and docs/ASSET_REGISTER.md) and places clones of them with distance-based level of detail.
 *
 * Each species variant has three glTF files, {@code <variant>_lod0.gltf} .. {@code _lod2.gltf}. A
 * placement is a node holding one clone per level; a {@link TreeLodControl} shows the level that
 * matches the camera distance. Meshes and materials are shared between clones.
 *
 * The glTF loader produces PBR materials; the rest of the campus uses {@code Lighting.j3md}, so the
 * textures are moved to Phong materials here. Leaf materials use alpha discard (no sorting, works in
 * the shadow pre-pass) and double-sided rendering.
 */
public class TreeLibrary {

    /** Camera distance (metres) above which LOD 1 and LOD 2 are shown. */
    public static final float LOD1_DISTANCE = 45f;
    public static final float LOD2_DISTANCE = 130f;
    public static final int LOD_COUNT = 3;

    private final AssetManager assetManager;
    private final Map<String, Spatial[]> cache = new HashMap<>();
    private final Map<String, int[]> triangleCounts = new HashMap<>();
    private Camera camera;

    public TreeLibrary(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    /** The camera used for LOD selection; until it is set every tree shows LOD 0. */
    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public Camera getCamera() {
        return camera;
    }

    /** LOD index for a camera distance. */
    public static int lodIndexFor(float distance) {
        if (distance >= LOD2_DISTANCE) return 2;
        if (distance >= LOD1_DISTANCE) return 1;
        return 0;
    }

    /**
     * Places one tree. {@code folder} is the model folder under Models/Trees, {@code variant} the file
     * prefix. Returns null (and logs) if the model could not be loaded so the caller can fall back.
     */
    public Node place(String name, String folder, String variant, float x, float y, float z, float scale, float yawDegrees) {
        Spatial[] lods = load(folder, variant);
        if (lods == null) return null;
        Node tree = new Node(name);
        Spatial[] clones = new Spatial[LOD_COUNT];
        for (int i = 0; i < LOD_COUNT; i++) {
            clones[i] = lods[i].clone(false);
            clones[i].setName(name + "_lod" + i);
            clones[i].setCullHint(i == 0 ? Spatial.CullHint.Inherit : Spatial.CullHint.Always);
            tree.attachChild(clones[i]);
        }
        tree.setLocalTranslation(x, y, z);
        tree.setLocalScale(scale);
        tree.rotate(0f, yawDegrees * FastMath.DEG_TO_RAD, 0f);
        tree.addControl(new TreeLodControl(this, clones));
        return tree;
    }

    /** Triangle counts of the three levels of a variant, or null if it is not loaded. */
    public int[] getTriangleCounts(String variant) {
        return triangleCounts.get(variant);
    }

    private Spatial[] load(String folder, String variant) {
        if (cache.containsKey(variant)) return cache.get(variant);
        Spatial[] lods = new Spatial[LOD_COUNT];
        int[] tris = new int[LOD_COUNT];
        java.util.logging.Logger gltfLog = java.util.logging.Logger.getLogger("com.jme3.scene.plugins.gltf");
        java.util.logging.Level previous = gltfLog.getLevel();
        gltfLog.setLevel(java.util.logging.Level.SEVERE);
        try {
            for (int i = 0; i < LOD_COUNT; i++) {
                String path = "Models/Trees/" + folder + "/" + variant + "_lod" + i + ".gltf";
                Spatial model = assetManager.loadModel(path);
                tris[i] = convertMaterials(model);
                model.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
                lods[i] = model;
            }
        } catch (Throwable t) {
            System.err.println("[TreeLibrary] Could not load tree variant '" + variant + "': " + t);
            cache.put(variant, null);
            return null;
        } finally {
            gltfLog.setLevel(previous);
        }
        System.out.println("[TreeLibrary] Loaded " + variant + " LOD triangles: " + tris[0] + " / " + tris[1] + " / " + tris[2]);
        cache.put(variant, lods);
        triangleCounts.put(variant, tris);
        return lods;
    }

    /** Replaces the loader's PBR materials with Phong materials; returns the triangle count. */
    private int convertMaterials(Spatial model) {
        int[] tris = {0};
        model.depthFirstTraversal(sp -> {
            if (!(sp instanceof Geometry)) return;
            Geometry g = (Geometry) sp;
            tris[0] += g.getMesh().getTriangleCount();
            Material pbr = g.getMaterial();
            String matName = pbr.getName() != null ? pbr.getName() : "";
            boolean leaves = matName.endsWith("_leaves");
            boolean twigs = matName.endsWith("_twigs");

            Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
            mat.setName(matName);
            mat.setBoolean("UseMaterialColors", true);
            mat.setColor("Ambient", ColorRGBA.White);
            mat.setColor("Diffuse", ColorRGBA.White);
            mat.setColor("Specular", new ColorRGBA(0.08f, 0.08f, 0.08f, 1f));
            mat.setFloat("Shininess", leaves ? 12f : 6f);
            Texture diffuse = textureParam(pbr, "BaseColorMap");
            Texture normal = textureParam(pbr, "NormalMap");
            if (diffuse != null) {
                prepareTexture(diffuse, twigs);
                mat.setTexture("DiffuseMap", diffuse);
            }
            if (normal != null) {
                prepareTexture(normal, twigs);
                mat.setTexture("NormalMap", normal);
                if (g.getMesh().getBuffer(VertexBuffer.Type.Tangent) == null) {
                    TangentBinormalGenerator.generate(g.getMesh());
                }
            }
            if (g.getMesh().getBuffer(VertexBuffer.Type.Color) != null) {
                mat.setBoolean("UseVertexColor", true);
            }
            if (leaves) {
                mat.setFloat("AlphaDiscardThreshold", 0.5f);
                mat.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
            }
            g.setMaterial(mat);
        });
        return tris[0];
    }

    private static Texture textureParam(Material m, String name) {
        return m.getTextureParam(name) != null ? m.getTextureParam(name).getTextureValue() : null;
    }

    private static void prepareTexture(Texture t, boolean repeat) {
        t.setMinFilter(Texture.MinFilter.Trilinear);
        t.setMagFilter(Texture.MagFilter.Bilinear);
        t.setAnisotropicFilter(4);
        if (repeat) {
            t.setWrap(Texture.WrapMode.Repeat);
        }
    }

    /** Shows the LOD child that matches the distance from the library camera. */
    public static class TreeLodControl extends AbstractControl {
        private final TreeLibrary library;
        private final Spatial[] lods;
        private int current = 0;

        TreeLodControl(TreeLibrary library, Spatial[] lods) {
            this.library = library;
            this.lods = lods;
        }

        public int getCurrentLod() {
            return current;
        }

        @Override
        protected void controlUpdate(float tpf) {
            Camera cam = library.getCamera();
            if (cam == null || spatial == null) return;
            int wanted = lodIndexFor(cam.getLocation().distance(spatial.getWorldTranslation()));
            if (wanted != current) {
                lods[current].setCullHint(Spatial.CullHint.Always);
                lods[wanted].setCullHint(Spatial.CullHint.Inherit);
                current = wanted;
            }
        }

        @Override
        protected void controlRender(RenderManager rm, ViewPort vp) {
        }
    }
}
