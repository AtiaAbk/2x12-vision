package bd.july2024.world;

import bd.july2024.level.Slogans;
import bd.july2024.ui.BengaliTextRenderer;
import com.jme3.asset.AssetManager;
import com.jme3.asset.TextureKey;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.VertexBuffer;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Cylinder;
import com.jme3.scene.shape.Quad;
import com.jme3.scene.shape.Sphere;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture2D;
import com.jme3.util.TangentBinormalGenerator;
import jme3tools.optimize.GeometryBatchFactory;

import java.awt.Color;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Level 01: the TSC Mor area of the University of Dhaka at true scale.
 *
 * Coordinate convention: origin = Anti-Terrorism Raju Memorial Sculpture, +X east, +Z south, metres.
 * Landmark positions and footprints come from OpenStreetMap; building programmes from published
 * descriptions. Every dimension used here is listed with its source and status in
 * docs/REFERENCE_LEDGER.md, including which parts are modelling choices rather than documented fact.
 *
 * Built from primitives and two custom meshes (parabolic vault, flat disc); surfaces use CC0 ambientCG
 * textures (docs/ASSET_REGISTER.md). Static geometry is batched per material after construction.
 */
public class CampusWorld {

    // --- Layout constants (metres). See docs/REFERENCE_LEDGER.md for each value's source. ---
    /** Memorial island around the sculpture: OSM way 225234308 is ~20.5 m across. */
    private static final float ISLAND_RADIUS = 10.0f;
    private static final float ISLAND_HEIGHT = 0.32f;
    /** Roundabout carriageway: OSM centre-line radius ~18 m; four lanes assumed, outer kerb at 25 m. */
    private static final float RING_OUTER_RADIUS = 25.0f;
    private static final float ROAD_TOP = 0.015f;
    private static final float FOOTPATH_HEIGHT = 0.12f;
    private static final float PLAZA_HEIGHT = 0.10f;
    /** TSC Student Union block centre (OSM way 655714604) and the 9 degree yaw shared by the TSC buildings. */
    private static final float TSC_X = 9.9f;
    private static final float TSC_Z = 66.8f;
    private static final float TSC_YAW = 9f * FastMath.DEG_TO_RAD;
    /** Hakim Chattar (OSM node 11824686638). */
    private static final float HAKIM_X = -83f;
    private static final float HAKIM_Z = -46f;
    /** Curzon Hall centre along the south-east avenue. */
    private static final float CURZON_X = 64f;
    private static final float CURZON_Z = 64f;
    private static final float CURZON_YAW = -2.356f; // -135 degrees, facing avenue entrance

    private static final boolean BATCH_STATIC_GEOMETRY = true;

    private final Node rootNode;
    /** Everything that is opaque and never moves; batched per material at the end of construction. */
    private final Node staticNode;
    /** Tree placements; kept out of the static batch so their level of detail can switch. */
    private final Node treeNode;
    private final TreeLibrary trees;
    private int treeCount = 0;
    private int fallbackTreeCount = 0;
    private final BengaliTextRenderer textRenderer;
    private final List<float[]> colliders = new ArrayList<>();
    private final List<Surface> surfaces = new ArrayList<>();
    /** Road arms as {dx, dz, px, pz, start, length, halfWidth, lateral} for the footstep surface lookup. */
    private final List<float[]> roadArms = new ArrayList<>();
    private final Map<Material, Float> tileOf = new HashMap<>();
    private final Frame tscFrame = new Frame(TSC_X, TSC_Z, TSC_YAW);
    private final Frame curzonFrame = new Frame(CURZON_X, CURZON_Z, CURZON_YAW);
    private Node tscNode;
    private Node tscDecalNode;
    private Node curzonNode;
    private int geometryCount = 0;

    // Textured (ambientCG CC0) materials
    private Material grassMat;
    private Material asphaltMat;
    private Material pavingMat;
    private Material brickMat;
    private Material brickDarkMat;
    private Material concreteMat;
    private Material concreteDarkMat;
    private Material plasterMat;
    private Material plasterWarmMat;
    // Plain lit materials
    private Material roadLineMat;
    private Material glassMat;
    private Material bronzeMat;
    private Material steelMat;
    private Material treeTrunkMat;
    private Material foliageMat;
    private Material krishnachuraBloomMat;
    private Material jarulBloomMat;
    private Material radhachuraBloomMat;
    private Material hedgeMat;
    private Material lampMat;
    private Material memorialGoldMat;
    private Material woodMat;
    private Vector3f libraryBoardPos;
    private Vector3f tscBoardPos;
    private Vector3f curzonBoardPos;

    public CampusWorld(AssetManager assetManager, Node sceneRoot) {
        this(assetManager, sceneRoot, null);
    }

    public CampusWorld(AssetManager assetManager, Node sceneRoot, BengaliTextRenderer textRenderer) {
        this.rootNode = new Node("CampusWorld");
        this.staticNode = new Node("CampusStatic");
        this.treeNode = new Node("CampusTrees");
        this.trees = new TreeLibrary(assetManager);
        this.textRenderer = textRenderer;
        rootNode.attachChild(staticNode);
        rootNode.attachChild(treeNode);
        rootNode.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        initMaterials(assetManager);
        // Tangent generation logs one warning per degenerate cap triangle on cylinders; silence it.
        java.util.logging.Logger tbLog = java.util.logging.Logger.getLogger("com.jme3.util.TangentBinormalGenerator");
        java.util.logging.Level previous = tbLog.getLevel();
        tbLog.setLevel(java.util.logging.Level.SEVERE);
        try {
            buildWorld(assetManager);
        } finally {
            tbLog.setLevel(previous);
        }
        finishStaticGeometry();
        if (sceneRoot != null) {
            sceneRoot.attachChild(rootNode);
        }
    }

    // ------------------------------------------------------------------------------------------
    // Materials
    // ------------------------------------------------------------------------------------------

    private void initMaterials(AssetManager assetManager) {
        grassMat = texturedMaterial(assetManager, "Grass004", new ColorRGBA(0.60f, 0.68f, 0.50f, 1f), 2f, 3.0f);
        grassMat.setColor("Specular", ColorRGBA.Black);
        asphaltMat = texturedMaterial(assetManager, "Asphalt010", new ColorRGBA(0.92f, 0.92f, 0.94f, 1f), 2f, 6.0f);
        asphaltMat.setColor("Specular", ColorRGBA.Black);
        pavingMat = texturedMaterial(assetManager, "PavingStones070", new ColorRGBA(0.92f, 0.88f, 0.84f, 1f), 6f, 3.0f);
        brickMat = texturedMaterial(assetManager, "Bricks059", new ColorRGBA(1.0f, 0.92f, 0.86f, 1f), 6f, 3.0f);
        brickDarkMat = texturedMaterial(assetManager, "Bricks059", new ColorRGBA(0.72f, 0.60f, 0.54f, 1f), 6f, 1.5f);
        concreteMat = texturedMaterial(assetManager, "Concrete030", new ColorRGBA(0.95f, 0.94f, 0.92f, 1f), 10f, 4.0f);
        concreteDarkMat = texturedMaterial(assetManager, "Concrete030", new ColorRGBA(0.62f, 0.62f, 0.62f, 1f), 10f, 2.0f);
        plasterMat = texturedMaterial(assetManager, "PaintedPlaster017", new ColorRGBA(0.96f, 0.95f, 0.90f, 1f), 12f, 4.0f);
        plasterWarmMat = texturedMaterial(assetManager, "PaintedPlaster017", new ColorRGBA(0.96f, 0.88f, 0.72f, 1f), 12f, 4.0f);

        roadLineMat = createLitMaterial(assetManager, new ColorRGBA(0.92f, 0.92f, 0.88f, 1f), 30f);
        glassMat = createLitMaterial(assetManager, new ColorRGBA(0.16f, 0.22f, 0.28f, 1f), 90f);
        glassMat.setColor("Specular", ColorRGBA.White.mult(0.6f));
        bronzeMat = createLitMaterial(assetManager, new ColorRGBA(0.20f, 0.14f, 0.09f, 1f), 40f);
        bronzeMat.setColor("Specular", new ColorRGBA(0.35f, 0.28f, 0.18f, 1f));
        steelMat = createLitMaterial(assetManager, new ColorRGBA(0.30f, 0.31f, 0.33f, 1f), 40f);
        treeTrunkMat = createLitMaterial(assetManager, new ColorRGBA(0.22f, 0.16f, 0.12f, 1f), 4f);
        foliageMat = createLitMaterial(assetManager, new ColorRGBA(0.24f, 0.46f, 0.20f, 1f), 6f);
        krishnachuraBloomMat = createLitMaterial(assetManager, new ColorRGBA(0.92f, 0.20f, 0.10f, 1f), 12f);
        jarulBloomMat = createLitMaterial(assetManager, new ColorRGBA(0.72f, 0.26f, 0.76f, 1f), 12f);
        radhachuraBloomMat = createLitMaterial(assetManager, new ColorRGBA(0.95f, 0.80f, 0.12f, 1f), 12f);
        hedgeMat = createLitMaterial(assetManager, new ColorRGBA(0.16f, 0.32f, 0.14f, 1f), 8f);
        lampMat = createLitMaterial(assetManager, new ColorRGBA(0.10f, 0.20f, 0.14f, 1f), 35f);
        woodMat = createLitMaterial(assetManager, new ColorRGBA(0.45f, 0.30f, 0.18f, 1f), 10f);

        memorialGoldMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        memorialGoldMat.setColor("Color", new ColorRGBA(1.0f, 0.86f, 0.32f, 1f));
    }

    private Material createLitMaterial(AssetManager assetManager, ColorRGBA diffuse, float shininess) {
        Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        mat.setBoolean("UseMaterialColors", true);
        mat.setColor("Ambient", diffuse.mult(0.65f));
        mat.setColor("Diffuse", diffuse);
        mat.setColor("Specular", ColorRGBA.White.mult(0.12f));
        mat.setFloat("Shininess", shininess);
        return mat;
    }

    /**
     * Lit material with an ambientCG colour and normal map, tiled every {@code tileMetres} in world space.
     * Falls back to a plain tinted material if the texture files are missing.
     */
    private Material texturedMaterial(AssetManager assetManager, String id, ColorRGBA tint, float shininess, float tileMetres) {
        Material mat = createLitMaterial(assetManager, tint, shininess);
        mat.setColor("Ambient", tint.mult(0.75f));
        mat.setColor("Specular", ColorRGBA.White.mult(0.08f));
        mat.setName(id);
        Texture color = loadTiledTexture(assetManager, "Textures/" + id + "/" + id + "_Color.jpg");
        Texture normal = loadTiledTexture(assetManager, "Textures/" + id + "/" + id + "_NormalGL.jpg");
        if (color != null) {
            mat.setTexture("DiffuseMap", color);
        }
        if (normal != null) {
            mat.setTexture("NormalMap", normal);
        }
        if (color == null && normal == null) {
            System.err.println("[CampusWorld] Texture set " + id + " missing; using flat colour.");
        }
        tileOf.put(mat, tileMetres);
        return mat;
    }

    private static Texture loadTiledTexture(AssetManager assetManager, String path) {
        if (assetManager == null) return null;
        try {
            TextureKey key = new TextureKey(path, true);
            key.setGenerateMips(true);
            Texture t = assetManager.loadTexture(key);
            t.setWrap(Texture.WrapMode.Repeat);
            t.setMinFilter(Texture.MinFilter.Trilinear);
            t.setAnisotropicFilter(4);
            return t;
        } catch (Throwable t) {
            System.err.println("[CampusWorld] Could not load texture " + path + ": " + t);
            return null;
        }
    }

    // ------------------------------------------------------------------------------------------
    // Geometry helpers
    // ------------------------------------------------------------------------------------------

    /** A horizontal frame: origin and yaw (jME convention, rotation about +Y). */
    private static final class Frame {
        final float ox, oz, yaw, cos, sin;

        Frame(float ox, float oz, float yaw) {
            this.ox = ox;
            this.oz = oz;
            this.yaw = yaw;
            this.cos = FastMath.cos(yaw);
            this.sin = FastMath.sin(yaw);
        }

        float worldX(float lx, float lz) { return ox + lx * cos + lz * sin; }
        float worldZ(float lx, float lz) { return oz - lx * sin + lz * cos; }
        float localX(float wx, float wz) { float dx = wx - ox, dz = wz - oz; return dx * cos - dz * sin; }
        float localZ(float wx, float wz) { float dx = wx - ox, dz = wz - oz; return dx * sin + dz * cos; }
    }

    /** A raised walkable surface: a rectangle in a frame (radius 0) or a disc centred on the frame origin. */
    private static final class Surface {
        final Frame frame;
        final float hx, hz, radius, height;

        Surface(Frame frame, float hx, float hz, float radius, float height) {
            this.frame = frame;
            this.hx = hx;
            this.hz = hz;
            this.radius = radius;
            this.height = height;
        }

        boolean contains(float x, float z) {
            if (radius > 0f) {
                float dx = x - frame.ox, dz = z - frame.oz;
                return dx * dx + dz * dz <= radius * radius;
            }
            float lx = frame.localX(x, z), lz = frame.localZ(x, z);
            return Math.abs(lx) <= hx && Math.abs(lz) <= hz;
        }
    }

    /** Camera used to pick each tree's level of detail. */
    public void setLodCamera(Camera camera) {
        trees.setCamera(camera);
    }

    public int getTreeCount() {
        return treeCount;
    }

    /** What the ground is made of at a horizontal position: paving on footpaths and plazas, asphalt on roads, else grass. */
    public GroundType groundTypeAt(float x, float z) {
        for (Surface s : surfaces) {
            if (s.contains(x, z)) return GroundType.PAVING;
        }
        float r2 = x * x + z * z;
        if (r2 <= RING_OUTER_RADIUS * RING_OUTER_RADIUS && r2 > ISLAND_RADIUS * ISLAND_RADIUS) return GroundType.ASPHALT;
        for (float[] arm : roadArms) {
            float along = x * arm[0] + z * arm[1];
            float across = x * arm[2] + z * arm[3] - arm[7];
            if (along >= arm[4] && along <= arm[4] + arm[5] && Math.abs(across) <= arm[6]) return GroundType.ASPHALT;
        }
        return GroundType.GRASS;
    }

    /** Walkable ground height at a horizontal position (0 on the base ground). */
    public float groundHeightAt(float x, float z) {
        float h = 0f;
        for (Surface s : surfaces) {
            if (s.height > h && s.contains(x, z)) h = s.height;
        }
        return h;
    }

    private void addSurfaceRect(Frame frame, float lcx, float lcz, float hx, float hz, float height) {
        Frame f = new Frame(frame.worldX(lcx, lcz), frame.worldZ(lcx, lcz), frame.yaw);
        surfaces.add(new Surface(f, hx, hz, 0f, height));
    }

    private void addSurfaceDisc(float cx, float cz, float radius, float height) {
        surfaces.add(new Surface(new Frame(cx, cz, 0f), 0f, 0f, radius, height));
    }

    /** Attaches a geometry, applies world-space tiling for boxes and generates tangents for normal maps. */
    private Geometry finish(Geometry g, Material mat, Node parent) {
        g.setMaterial(mat);
        Float tile = tileOf.get(mat);
        if (tile != null) {
            if (g.getMesh() instanceof Box) {
                Vector3f t = g.getLocalTranslation();
                applyWorldUV(g.getMesh(), tile, t.x, t.y, t.z);
            }
            if (mat.getTextureParam("NormalMap") != null) {
                TangentBinormalGenerator.generate(g.getMesh());
            }
        }
        (parent != null ? parent : staticNode).attachChild(g);
        geometryCount++;
        return g;
    }

    /** Planar projection of texture coordinates per face, so tiles are continuous across adjacent boxes. */
    private static void applyWorldUV(Mesh mesh, float tile, float offX, float offY, float offZ) {
        FloatBuffer pos = mesh.getFloatBuffer(VertexBuffer.Type.Position);
        FloatBuffer nrm = mesh.getFloatBuffer(VertexBuffer.Type.Normal);
        if (pos == null || nrm == null) return;
        int n = mesh.getVertexCount();
        float[] uv = new float[n * 2];
        for (int i = 0; i < n; i++) {
            float px = pos.get(i * 3) + offX, py = pos.get(i * 3 + 1) + offY, pz = pos.get(i * 3 + 2) + offZ;
            float nx = Math.abs(nrm.get(i * 3)), ny = Math.abs(nrm.get(i * 3 + 1)), nz = Math.abs(nrm.get(i * 3 + 2));
            float u, v;
            if (ny >= nx && ny >= nz) { u = px; v = pz; }
            else if (nx >= nz) { u = pz; v = py; }
            else { u = px; v = py; }
            uv[i * 2] = u / tile;
            uv[i * 2 + 1] = v / tile;
        }
        mesh.clearBuffer(VertexBuffer.Type.TexCoord);
        mesh.setBuffer(VertexBuffer.Type.TexCoord, 2, uv);
    }

    private Geometry addBox(String name, Material mat, float cx, float cy, float cz, float hx, float hy, float hz) {
        return addBox(name, mat, cx, cy, cz, hx, hy, hz, null);
    }

    private Geometry addBox(String name, Material mat, float cx, float cy, float cz, float hx, float hy, float hz, Node parent) {
        Geometry g = new Geometry(name, new Box(hx, hy, hz));
        g.setLocalTranslation(cx, cy, cz);
        return finish(g, mat, parent);
    }

    /** Box rotated about +Y by {@code yaw} radians, placed in world space. */
    private Geometry addYawedBox(String name, Material mat, float cx, float cy, float cz, float hx, float hy, float hz, float yaw) {
        Geometry g = new Geometry(name, new Box(hx, hy, hz));
        g.setLocalTranslation(cx, cy, cz);
        g.setLocalRotation(new Quaternion().fromAngleAxis(yaw, Vector3f.UNIT_Y));
        return finish(g, mat, null);
    }

    /** Vertical cylinder standing on {@code baseY}. */
    private Geometry addCylinderY(String name, Material mat, float cx, float baseY, float cz, float radius, float height, Node parent) {
        Cylinder cyl = new Cylinder(2, 32, radius, height, true);
        Float tile = tileOf.get(mat);
        if (tile != null) {
            cyl.scaleTextureCoordinates(new Vector2f(FastMath.TWO_PI * radius / tile, Math.max(1f, height / tile)));
        }
        Geometry g = new Geometry(name, cyl);
        g.rotate(FastMath.HALF_PI, 0f, 0f);
        g.setLocalTranslation(cx, baseY + height * 0.5f, cz);
        return finish(g, mat, parent);
    }

    /** Thin cylinder between two points (limbs, branches). */
    private Geometry addLimb(String name, Material mat, Vector3f a, Vector3f b, float radius, Node parent) {
        float len = a.distance(b);
        Cylinder cyl = new Cylinder(2, 10, radius, len, true);
        Geometry g = new Geometry(name, cyl);
        Vector3f dir = b.subtract(a).normalizeLocal();
        Vector3f up = Math.abs(dir.y) > 0.9f ? Vector3f.UNIT_X : Vector3f.UNIT_Y;
        g.setLocalRotation(new Quaternion().lookAt(dir, up));
        g.setLocalTranslation(a.add(b).multLocal(0.5f));
        return finish(g, mat, parent);
    }

    /** Flat disc facing up, texture coordinates in world space. */
    private Geometry addDisc(String name, Material mat, float cx, float y, float cz, float radius, int segments) {
        Float tile = tileOf.get(mat);
        float t = tile != null ? tile : 1f;
        float[] p = new float[(segments + 2) * 3];
        float[] n = new float[(segments + 2) * 3];
        float[] uv = new float[(segments + 2) * 2];
        int[] idx = new int[segments * 3];
        p[0] = 0; p[1] = 0; p[2] = 0;
        n[1] = 1f;
        uv[0] = cx / t; uv[1] = cz / t;
        for (int i = 0; i <= segments; i++) {
            float a = i * FastMath.TWO_PI / segments;
            float x = FastMath.cos(a) * radius, z = FastMath.sin(a) * radius;
            int v = i + 1;
            p[v * 3] = x; p[v * 3 + 1] = 0; p[v * 3 + 2] = z;
            n[v * 3 + 1] = 1f;
            uv[v * 2] = (cx + x) / t; uv[v * 2 + 1] = (cz + z) / t;
        }
        for (int i = 0; i < segments; i++) {
            // counter-clockwise seen from above (+Y): centre, next, current
            idx[i * 3] = 0; idx[i * 3 + 1] = i + 2; idx[i * 3 + 2] = i + 1;
        }
        Mesh m = new Mesh();
        m.setBuffer(VertexBuffer.Type.Position, 3, p);
        m.setBuffer(VertexBuffer.Type.Normal, 3, n);
        m.setBuffer(VertexBuffer.Type.TexCoord, 2, uv);
        m.setBuffer(VertexBuffer.Type.Index, 3, idx);
        m.updateBound();
        Geometry g = new Geometry(name, m);
        g.setLocalTranslation(cx, y, cz);
        return finish(g, mat, null);
    }

    /**
     * Parabolic barrel vault along local X: shell plus two end caps. The curve spans {@code width}
     * across Z, starts at {@code baseY} at both edges and rises by {@code rise} at the crown.
     */
    private Mesh createVaultMesh(float length, float width, float baseY, float rise, int segments, float tile) {
        float hl = length * 0.5f, hw = width * 0.5f;
        List<Float> p = new ArrayList<>(), n = new ArrayList<>(), t = new ArrayList<>();
        List<Integer> idx = new ArrayList<>();

        float arc = 0f, prevZ = -hw, prevY = baseY;
        for (int i = 0; i <= segments; i++) {
            float z = -hw + width * i / segments;
            float y = baseY + rise * (1f - (z / hw) * (z / hw));
            float slope = -2f * rise * z / (hw * hw);
            Vector3f nn = new Vector3f(0f, 1f, -slope).normalizeLocal();
            if (i > 0) arc += FastMath.sqrt((z - prevZ) * (z - prevZ) + (y - prevY) * (y - prevY));
            prevZ = z;
            prevY = y;
            pushVertex(p, n, t, -hl, y, z, nn, 0f, arc / tile);
            pushVertex(p, n, t, hl, y, z, nn, length / tile, arc / tile);
        }
        for (int i = 0; i < segments; i++) {
            int a = i * 2, b = a + 1, c = a + 3, d = a + 2;
            pushTriangle(idx, p, n, a, d, c);
            pushTriangle(idx, p, n, a, c, b);
        }
        for (int side = -1; side <= 1; side += 2) {
            float x = side * hl;
            Vector3f nn = new Vector3f(side, 0f, 0f);
            int centre = p.size() / 3;
            pushVertex(p, n, t, x, baseY + rise * 0.4f, 0f, nn, hw / tile, rise * 0.4f / tile);
            int first = p.size() / 3;
            for (int i = 0; i <= segments; i++) {
                float z = -hw + width * i / segments;
                float y = baseY + rise * (1f - (z / hw) * (z / hw));
                pushVertex(p, n, t, x, y, z, nn, (z + hw) / tile, (y - baseY) / tile);
            }
            for (int i = 0; i < segments; i++) {
                pushTriangle(idx, p, n, centre, first + i, first + i + 1);
            }
            pushTriangle(idx, p, n, centre, first + segments, first);
        }

        Mesh m = new Mesh();
        m.setBuffer(VertexBuffer.Type.Position, 3, toArray(p));
        m.setBuffer(VertexBuffer.Type.Normal, 3, toArray(n));
        m.setBuffer(VertexBuffer.Type.TexCoord, 2, toArray(t));
        int[] ind = new int[idx.size()];
        for (int i = 0; i < ind.length; i++) ind[i] = idx.get(i);
        m.setBuffer(VertexBuffer.Type.Index, 3, ind);
        m.updateBound();
        return m;
    }

    private static void pushVertex(List<Float> p, List<Float> n, List<Float> t, float x, float y, float z, Vector3f nn, float u, float v) {
        p.add(x); p.add(y); p.add(z);
        n.add(nn.x); n.add(nn.y); n.add(nn.z);
        t.add(u); t.add(v);
    }

    /** Adds a triangle, flipping the winding if its face normal opposes the first vertex's normal. */
    private static void pushTriangle(List<Integer> idx, List<Float> p, List<Float> n, int a, int b, int c) {
        Vector3f pa = new Vector3f(p.get(a * 3), p.get(a * 3 + 1), p.get(a * 3 + 2));
        Vector3f pb = new Vector3f(p.get(b * 3), p.get(b * 3 + 1), p.get(b * 3 + 2));
        Vector3f pc = new Vector3f(p.get(c * 3), p.get(c * 3 + 1), p.get(c * 3 + 2));
        Vector3f na = new Vector3f(n.get(a * 3), n.get(a * 3 + 1), n.get(a * 3 + 2));
        Vector3f face = pb.subtract(pa).cross(pc.subtract(pa));
        idx.add(a);
        if (face.dot(na) >= 0f) { idx.add(b); idx.add(c); } else { idx.add(c); idx.add(b); }
    }

    private static float[] toArray(List<Float> l) {
        float[] a = new float[l.size()];
        for (int i = 0; i < a.length; i++) a[i] = l.get(i);
        return a;
    }

    private void addCollider(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
        colliders.add(new float[]{minX, minY, minZ, maxX, maxY, maxZ});
    }

    /** Collider for a yawed rectangle: covered by a grid of axis-aligned boxes no larger than 4 m. */
    private void addFrameCollider(Frame f, float lcx, float lcz, float hx, float hz, float minY, float maxY) {
        int nx = Math.max(1, (int) Math.ceil(hx * 2f / 4f));
        int nz = Math.max(1, (int) Math.ceil(hz * 2f / 4f));
        float sx = hx * 2f / nx, sz = hz * 2f / nz;
        for (int i = 0; i < nx; i++) {
            for (int j = 0; j < nz; j++) {
                float x0 = lcx - hx + i * sx, x1 = x0 + sx;
                float z0 = lcz - hz + j * sz, z1 = z0 + sz;
                float[] xs = {f.worldX(x0, z0), f.worldX(x1, z0), f.worldX(x1, z1), f.worldX(x0, z1)};
                float[] zs = {f.worldZ(x0, z0), f.worldZ(x1, z0), f.worldZ(x1, z1), f.worldZ(x0, z1)};
                float minX = Float.MAX_VALUE, maxX = -Float.MAX_VALUE, minZ = Float.MAX_VALUE, maxZ = -Float.MAX_VALUE;
                for (int k = 0; k < 4; k++) {
                    minX = Math.min(minX, xs[k]); maxX = Math.max(maxX, xs[k]);
                    minZ = Math.min(minZ, zs[k]); maxZ = Math.max(maxZ, zs[k]);
                }
                addCollider(minX, minY, minZ, maxX, maxY, maxZ);
            }
        }
    }

    // ------------------------------------------------------------------------------------------
    // World
    // ------------------------------------------------------------------------------------------

    private void buildWorld(AssetManager assetManager) {
        buildGroundAndRoads();
        buildRajuMemorial();
        buildMemorialPlaque();
        buildTSC();
        buildCentralLibrary();
        buildHakimChattar();
        buildCurzonHall();
        buildNoticeBoards();
        buildVegetation();
        buildCampusProps();
        buildJulyMovementBannersAndMurals(assetManager);
    }

    private void finishStaticGeometry() {
        int before = geometryCount;
        if (BATCH_STATIC_GEOMETRY) {
            try {
                rootNode.updateGeometricState();
                GeometryBatchFactory.optimize(staticNode);
                int after = countGeometries(staticNode);
                System.out.println("[CampusWorld] Static geometry batched: " + before + " -> " + after
                    + " geometries, " + colliders.size() + " colliders, " + surfaces.size() + " walkable surfaces, "
                    + treeCount + " model trees (" + fallbackTreeCount + " procedural fallbacks).");
            } catch (Throwable t) {
                System.err.println("[CampusWorld] Batching failed, rendering unbatched: " + t);
            }
        } else {
            System.out.println("[CampusWorld] " + before + " geometries (unbatched), " + colliders.size() + " colliders.");
        }
    }

    private static int countGeometries(Node n) {
        int[] c = {0};
        n.depthFirstTraversal(sp -> { if (sp instanceof Geometry) c[0]++; });
        return c[0];
    }

    // ---- Ground, roundabout and road arms --------------------------------------------------------

    private void buildGroundAndRoads() {
        // Lawn base, 600 m square, top at y = 0
        addBox("GrassBase", grassMat, 0f, -0.05f, 0f, 300f, 0.05f, 300f);

        // Roundabout carriageway (OSM: four lanes, asphalt) as a disc; the island sits on top.
        addDisc("RingRoad", asphaltMat, 0f, 0.02f, 0f, RING_OUTER_RADIUS, 72);

        // Road arms. Only the roundabout footprint and the south-west bearing of Shahid Minar Sarak are
        // from OSM; the other bearings are approximate (docs/REFERENCE_LEDGER.md, section 4).
        addRoadArm("RoadWest_NilkhetSarak", 180f, 6f, 150f, 8f);              // west, centre line 8 m north
        addRoadArm("RoadSouthWest_ShahidMinarSarak", 120f, 6f, 150f, 0f);     // south-west
        addRoadArm("RoadSouthEast_KaziNazrulIslamAve", 45f, 5f, 170f, 0f);    // south-east
        addRoadArm("RoadNorthEast_Shahbagh", -50f, 6f, 150f, 0f);             // north-east

        // Zebra crossings near the roundabout
        for (int i = -3; i <= 3; i++) {
            addBox("ZebraW_" + i, roadLineMat, -31f, ROAD_TOP + 0.004f, -8f + i * 1.7f, 2.6f, 0.004f, 0.55f);
        }
        float a = 120f * FastMath.DEG_TO_RAD;
        for (int i = -3; i <= 3; i++) {
            float along = 31f, across = i * 1.7f;
            float cx = FastMath.cos(a) * along - FastMath.sin(a) * across;
            float cz = FastMath.sin(a) * along + FastMath.cos(a) * across;
            addYawedBox("ZebraSW_" + i, roadLineMat, cx, ROAD_TOP + 0.004f, cz, 2.6f, 0.004f, 0.55f, -a);
        }

        // Footpath from the roundabout to the TSC plaza (axis-aligned, approximate)
        addBox("PathToTSC", pavingMat, 6f, FOOTPATH_HEIGHT * 0.5f, 36.5f, 2.2f, FOOTPATH_HEIGHT * 0.5f, 11f);
        addSurfaceRect(new Frame(0f, 0f, 0f), 6f, 36.5f, 2.2f, 11f, FOOTPATH_HEIGHT);
    }

    /**
     * Straight road arm leaving the roundabout. {@code bearingDeg} is measured from +X towards +Z (south);
     * {@code lateral} shifts the centre line sideways (positive = to the right when looking outward).
     */
    private void addRoadArm(String name, float bearingDeg, float halfWidth, float length, float lateral) {
        float a = bearingDeg * FastMath.DEG_TO_RAD;
        float dx = FastMath.cos(a), dz = FastMath.sin(a);
        float px = -dz, pz = dx; // left-hand perpendicular
        float start = RING_OUTER_RADIUS - 1.5f;
        float mid = start + length * 0.5f;
        float cx = dx * mid + px * lateral, cz = dz * mid + pz * lateral;
        // Box local +X runs along the arm: yaw such that +X maps to (dx, dz); jME maps +X to (cos, -sin).
        float yaw = -a;
        roadArms.add(new float[]{dx, dz, px, pz, start, length, halfWidth, lateral});
        addYawedBox(name, asphaltMat, cx, ROAD_TOP * 0.5f, cz, length * 0.5f, ROAD_TOP * 0.5f, halfWidth, yaw);
        // Paved footpaths on both sides
        float fpHalf = 2.5f;
        for (int side = -1; side <= 1; side += 2) {
            float off = lateral + side * (halfWidth + fpHalf);
            float fx = dx * (mid + 2f) + px * off, fz = dz * (mid + 2f) + pz * off;
            addYawedBox(name + "_Footpath" + (side < 0 ? "L" : "R"), pavingMat, fx, FOOTPATH_HEIGHT * 0.5f, fz,
                length * 0.5f - 2f, FOOTPATH_HEIGHT * 0.5f, fpHalf, yaw);
            surfaces.add(new Surface(new Frame(fx, fz, yaw), length * 0.5f - 2f, fpHalf, 0f, FOOTPATH_HEIGHT));
        }
    }

    // ---- Anti-Terrorism Raju Memorial Sculpture ---------------------------------------------------

    private void buildRajuMemorial() {
        Node raju = new Node("RajuMemorial");
        staticNode.attachChild(raju);

        // Island: concrete kerb cylinder with a lawn disc on top (OSM way 225234308, ~20.5 m across)
        addCylinderY("IslandKerb", concreteDarkMat, 0f, 0f, 0f, ISLAND_RADIUS + 0.3f, ISLAND_HEIGHT, raju);
        addDisc("IslandLawn", grassMat, 0f, ISLAND_HEIGHT + 0.015f, 0f, ISLAND_RADIUS, 48);
        addSurfaceDisc(0f, 0f, ISLAND_RADIUS + 0.3f, ISLAND_HEIGHT);

        // Flower border
        for (int b = 0; b < 36; b++) {
            float angle = b * (FastMath.TWO_PI / 36f);
            float bx = FastMath.cos(angle) * (ISLAND_RADIUS - 0.7f);
            float bz = FastMath.sin(angle) * (ISLAND_RADIUS - 0.7f);
            Geometry fGeom = new Geometry("IslandShrub_" + b, new Sphere(8, 8, 0.26f));
            fGeom.setLocalScale(1.1f, 0.6f, 1.1f);
            fGeom.setLocalTranslation(bx, ISLAND_HEIGHT + 0.16f, bz);
            finish(fGeom, (b % 3 == 0) ? radhachuraBloomMat : ((b % 3 == 1) ? krishnachuraBloomMat : hedgeMat), raju);
        }

        // Two-step plinth. Size and height are modelling choices (no measured source).
        float y0 = ISLAND_HEIGHT;
        addCylinderY("RajuPlinthLower", concreteMat, 0f, y0, 0f, 3.6f, 0.5f, raju);
        addCylinderY("RajuPlinthUpper", concreteDarkMat, 0f, y0 + 0.5f, 0f, 2.7f, 0.6f, raju);
        addCollider(-3.6f, 0f, -3.6f, 3.6f, y0 + 0.5f, 3.6f);
        addCollider(-2.7f, 0f, -2.7f, 2.7f, y0 + 1.1f, 2.7f);
        float top = y0 + 1.1f;

        // Eight figures in a ring, facing outward, arms raised and linked (composition per Wikipedia;
        // figure height 2.3 m is an estimate). Each figure: two legs, torso, head, two arms to the
        // shared raised hand points between neighbours.
        final int count = 8;
        final float ringR = 1.9f;
        final float figureH = 2.3f;
        Vector3f[] centres = new Vector3f[count];
        Vector3f[] tangents = new Vector3f[count];
        Vector3f[] hands = new Vector3f[count];
        for (int i = 0; i < count; i++) {
            float a = i * FastMath.TWO_PI / count;
            centres[i] = new Vector3f(FastMath.cos(a) * ringR, top, FastMath.sin(a) * ringR);
            tangents[i] = new Vector3f(-FastMath.sin(a), 0f, FastMath.cos(a));
            float am = a + FastMath.PI / count;
            hands[i] = new Vector3f(FastMath.cos(am) * (ringR + 0.1f), top + figureH * 0.98f, FastMath.sin(am) * (ringR + 0.1f));
        }
        for (int i = 0; i < count; i++) {
            Vector3f c = centres[i];
            Vector3f tg = tangents[i];
            float hipY = top + figureH * 0.46f;
            float shoulderY = top + figureH * 0.78f;
            float a = i * FastMath.TWO_PI / count;

            // Legs
            for (int s = -1; s <= 1; s += 2) {
                Vector3f hip = c.add(tg.mult(s * 0.10f)).setY(hipY);
                Vector3f foot = c.add(tg.mult(s * 0.15f)).setY(top + 0.02f);
                addLimb("RajuLeg_" + i + "_" + s, bronzeMat, hip, foot, 0.085f, raju);
            }
            // Torso (wider across the tangent), facing outward
            Geometry torso = new Geometry("RajuTorso_" + i, new Box(0.22f, figureH * 0.17f, 0.12f));
            torso.setLocalTranslation(c.x, (hipY + shoulderY) * 0.5f, c.z);
            torso.setLocalRotation(new Quaternion().fromAngleAxis(-a + FastMath.HALF_PI, Vector3f.UNIT_Y));
            finish(torso, bronzeMat, raju);
            // Neck and head
            Geometry head = new Geometry("RajuHead_" + i, new Sphere(12, 12, 0.14f));
            head.setLocalTranslation(c.x, shoulderY + 0.24f, c.z);
            finish(head, bronzeMat, raju);
            addLimb("RajuNeck_" + i, bronzeMat, new Vector3f(c.x, shoulderY, c.z), new Vector3f(c.x, shoulderY + 0.14f, c.z), 0.05f, raju);
            // Arms: right arm to the hand point ahead, left arm to the one behind
            Vector3f shoulderR = c.add(tg.mult(0.24f)).setY(shoulderY);
            Vector3f shoulderL = c.add(tg.mult(-0.24f)).setY(shoulderY);
            addLimb("RajuArmR_" + i, bronzeMat, shoulderR, hands[i], 0.06f, raju);
            addLimb("RajuArmL_" + i, bronzeMat, shoulderL, hands[(i + count - 1) % count], 0.06f, raju);
        }
        addCollider(-ringR - 0.4f, top, -ringR - 0.4f, ringR + 0.4f, top + figureH, ringR + 0.4f);
    }

    private void buildMemorialPlaque() {
        // Information board for the Raju Memorial Sculpture on the island lawn; opens the Raju document
        // card. The stand itself is a modelling choice (the real inscription was not photographed).
        float y0 = ISLAND_HEIGHT;
        addBox("PlaqueBase", concreteMat, 2f, y0 + 0.85f, -6f, 0.7f, 0.85f, 0.4f);
        addCollider(1.3f, 0f, -6.4f, 2.7f, y0 + 1.7f, -5.6f);
        Geometry face = new Geometry("PlaqueFace", new Box(0.55f, 0.65f, 0.05f));
        face.setLocalTranslation(2f, y0 + 0.95f, -5.58f);
        finish(face, memorialGoldMat, null);
        Geometry beacon = new Geometry("PlaqueBeacon", new Sphere(16, 16, 0.18f));
        beacon.setLocalTranslation(2f, y0 + 1.95f, -6f);
        finish(beacon, memorialGoldMat, rootNode);
    }

    // ---- Notice boards ---------------------------------------------------------------------------

    /**
     * Two freestanding notice boards that open the Central Library and TSC document cards in play: one
     * under the library banner in front of the open ground floor of Building II, one on the TSC entrance
     * plaza east of the verandah. The boards are a modelling choice (no reference for boards at either
     * spot); they stand well clear of the companions so the interaction prompt is unambiguous.
     */
    private void buildNoticeBoards() {
        float lx = -48.75f, lz = -61.5f;
        float ly = groundHeightAt(lx, lz);
        buildBoardStand("LibBoard", lx, ly, lz, null);
        addCollider(lx - 0.95f, 0f, lz - 0.2f, lx + 0.95f, ly + 2.1f, lz + 0.2f);
        libraryBoardPos = new Vector3f(lx, ly + 0.8f, lz);

        float tlx = 6f, tlz = -7f;
        Vector3f tw = tscLocalToWorld(tlx, tlz);
        float ty = groundHeightAt(tw.x, tw.z);
        buildBoardStand("TscBoard", tlx, ty, tlz, tscNode);
        addCollider(tw.x - 1.05f, 0f, tw.z - 0.4f, tw.x + 1.05f, ty + 2.1f, tw.z + 0.4f);
        tscBoardPos = new Vector3f(tw.x, ty + 0.8f, tw.z);

        float cx = 46f, cz = 46f;
        float cy = groundHeightAt(cx, cz);
        buildBoardStand("CurzonBoard", cx, cy, cz, null);
        addCollider(cx - 0.95f, 0f, cz - 0.2f, cx + 0.95f, cy + 2.1f, cz + 0.2f);
        curzonBoardPos = new Vector3f(cx, cy + 0.8f, cz);
    }

    private void buildBoardStand(String name, float cx, float y0, float cz, Node parent) {
        addBox(name + "_PostL", woodMat, cx - 0.75f, y0 + 0.95f, cz, 0.05f, 0.95f, 0.05f, parent);
        addBox(name + "_PostR", woodMat, cx + 0.75f, y0 + 0.95f, cz, 0.05f, 0.95f, 0.05f, parent);
        addBox(name + "_Board", concreteDarkMat, cx, y0 + 1.45f, cz, 0.85f, 0.55f, 0.04f, parent);
        addBox(name + "_Roof", woodMat, cx, y0 + 2.04f, cz, 0.95f, 0.04f, 0.14f, parent);
    }

    /** World position (at prompt height) of the Central Library notice board, or null before the world is built. */
    public Vector3f getLibraryBoardPosition() {
        return libraryBoardPos;
    }

    /** World position (at prompt height) of the TSC notice board, or null before the world is built. */
    public Vector3f getTscBoardPosition() {
        return tscBoardPos;
    }

    /** World position (at prompt height) of the Curzon Hall notice board, or null before the world is built. */
    public Vector3f getCurzonBoardPosition() {
        return curzonBoardPos;
    }

    // ---- Teacher-Student Centre -------------------------------------------------------------------

    private void buildTSC() {
        tscNode = new Node("TSC");
        tscNode.setLocalTranslation(TSC_X, 0f, TSC_Z);
        tscNode.setLocalRotation(new Quaternion().fromAngleAxis(TSC_YAW, Vector3f.UNIT_Y));
        staticNode.attachChild(tscNode);
        tscDecalNode = new Node("TSC_Decals");
        tscDecalNode.setLocalTranslation(TSC_X, 0f, TSC_Z);
        tscDecalNode.setLocalRotation(new Quaternion().fromAngleAxis(TSC_YAW, Vector3f.UNIT_Y));
        rootNode.attachChild(tscDecalNode);

        buildTSCUnionBlock();
        buildTSCAuditorium();
        buildTSCCafeteria();
    }

    /**
     * Student Union Building: three storeys, 158 x 37 ft (48.2 x 11.3 m), RC frame with brick walls,
     * double wing-like roof, brick jali, access from the north (Daily Star); 15 m high (OSM).
     * Local frame: X along the length, north face at local z = -5.65.
     */
    private void buildTSCUnionBlock() {
        Node n = tscNode;
        float hl = 24.1f, hw = 5.65f;
        float g0 = 0f, g1 = 4.2f;      // ground storey
        float f1 = 4.5f, f1t = 8.0f;   // first floor
        float f2 = 8.3f, f2t = 11.8f;  // second floor

        // Entrance plaza on the north side
        addBox("TSCPlaza", pavingMat, 0f, PLAZA_HEIGHT * 0.5f, -13f, 26f, PLAZA_HEIGHT * 0.5f, 7.2f, n);
        addSurfaceRect(tscFrame, 0f, -13f, 26f, 7.2f, PLAZA_HEIGHT);

        // Ground storey: verandah along the north face (2 m deep) in front of a plaster back wall
        addBox("TSC_GroundWall", plasterWarmMat, 0f, (g0 + g1) * 0.5f, 1.0f, hl, (g1 - g0) * 0.5f, hw - 2.0f, n);
        addFrameCollider(tscFrame, 0f, 1.0f, hl, hw - 2.0f, 0f, 12f);
        for (int i = 0; i <= 12; i++) {
            float x = -24f + i * 4f;
            addBox("TSC_Col_" + i, concreteMat, x, g1 * 0.5f, -hw + 0.25f, 0.22f, g1 * 0.5f, 0.22f, n);
            float wx = tscFrame.worldX(x, -hw + 0.25f), wz = tscFrame.worldZ(x, -hw + 0.25f);
            addCollider(wx - 0.35f, 0f, wz - 0.35f, wx + 0.35f, g1, wz + 0.35f);
        }
        // Two entrances (OSM: building:entrances=2)
        for (int s = -1; s <= 1; s += 2) {
            addBox("TSC_Door_" + s, glassMat, s * 8f, 1.5f, -3.60f, 1.3f, 1.5f, 0.05f, n);
            addBox("TSC_DoorFrame_" + s, concreteDarkMat, s * 8f, 3.3f, -3.58f, 1.6f, 0.3f, 0.08f, n);
        }
        // Brick jali panels on the verandah back wall
        float[] jaliX = {-20f, -15f, 15f, 20f};
        for (int j = 0; j < jaliX.length; j++) {
            addJaliPanel("TSC_Jali_" + j, jaliX[j], 0.6f, -3.62f, 1.3f, 1.4f, n);
        }

        // Floor slabs
        addBox("TSC_Slab1", concreteMat, 0f, (g1 + f1) * 0.5f, 0f, hl + 0.4f, (f1 - g1) * 0.5f, hw + 0.4f, n);
        addBox("TSC_Slab2", concreteMat, 0f, (f1t + f2) * 0.5f, 0f, hl + 0.4f, (f2 - f1t) * 0.5f, hw + 0.4f, n);
        // Upper floors: brick walls with a window per 4 m bay on both long faces
        addBox("TSC_Floor1", brickMat, 0f, (f1 + f1t) * 0.5f, 0f, hl, (f1t - f1) * 0.5f, hw, n);
        addBox("TSC_Floor2", brickMat, 0f, (f2 + f2t) * 0.5f, 0f, hl, (f2t - f2) * 0.5f, hw, n);
        for (int i = 0; i < 12; i++) {
            float x = -22f + i * 4f;
            for (int s = -1; s <= 1; s += 2) {
                addBox("TSC_Win1_" + i + "_" + s, glassMat, x, f1 + 2.0f, s * (hw + 0.03f), 1.4f, 0.8f, 0.04f, n);
                addBox("TSC_Win2_" + i + "_" + s, glassMat, x, f2 + 2.0f, s * (hw + 0.03f), 1.4f, 0.8f, 0.04f, n);
            }
        }
        // Roof: flat slab with the double "butterfly" roof above it, valley on the centre line, edges at 15 m
        addBox("TSC_RoofSlab", concreteMat, 0f, f2t + 0.2f, 0f, hl + 0.4f, 0.2f, hw + 0.2f, n);
        float valleyY = 12.5f, edgeY = 15.0f, wingHalf = 3.15f;
        float tilt = FastMath.atan((edgeY - valleyY) / (wingHalf * 2f));
        for (int s = -1; s <= 1; s += 2) {
            Geometry wing = new Geometry("TSC_RoofWing_" + s, new Box(hl + 0.6f, 0.12f, wingHalf));
            wing.setLocalTranslation(0f, (valleyY + edgeY) * 0.5f, s * wingHalf);
            wing.setLocalRotation(new Quaternion().fromAngleAxis(-s * tilt, Vector3f.UNIT_X));
            finish(wing, concreteMat, n);
        }
        addBox("TSC_RoofValley", concreteDarkMat, 0f, valleyY, 0f, hl + 0.6f, 0.18f, 0.35f, n);
        // Clerestory glazing closes the gap between the flat slab and the raised wing edges (a choice)
        float cleH = (edgeY - 0.3f - (f2t + 0.4f)) * 0.5f, cleY = f2t + 0.4f + cleH;
        for (int s = -1; s <= 1; s += 2) {
            addBox("TSC_Clerestory_" + s, glassMat, 0f, cleY, s * (hw - 0.05f), hl, cleH, 0.04f, n);
            addBox("TSC_ClerestoryEnd_" + s, glassMat, s * (hl - 0.05f), cleY, 0f, 0.04f, cleH, hw - 0.1f, n);
        }

        // Covered walkways on steel columns (Daily Star): 10 m beyond each end, and a link to the auditorium
        for (int s = -1; s <= 1; s += 2) {
            float cx = s * (hl + 5f);
            addBox("TSC_WalkCanopy_" + s, concreteMat, cx, 3.3f, -4.4f, 5f, 0.08f, 1.2f, n);
            for (int i = 0; i < 3; i++) {
                float x = s * (hl + 1.5f + i * 4f);
                for (int z = 0; z < 2; z++) {
                    float lz = -5.3f + z * 1.8f;
                    addBox("TSC_WalkCol_" + s + "_" + i + "_" + z, steelMat, x, 1.65f, lz, 0.05f, 1.65f, 0.05f, n);
                    float wx = tscFrame.worldX(x, lz), wz = tscFrame.worldZ(x, lz);
                    addCollider(wx - 0.15f, 0f, wz - 0.15f, wx + 0.15f, 3.3f, wz + 0.15f);
                }
            }
        }
        addBox("TSC_LinkCanopy", concreteMat, -14f, 3.3f, 12.5f, 1.2f, 0.08f, 6.9f, n);
        for (int i = 0; i < 3; i++) {
            float lz = 8f + i * 4f;
            for (int s = -1; s <= 1; s += 2) {
                float lx = -14f + s * 0.9f;
                addBox("TSC_LinkCol_" + i + "_" + s, steelMat, lx, 1.65f, lz, 0.05f, 1.65f, 0.05f, n);
                float wx = tscFrame.worldX(lx, lz), wz = tscFrame.worldZ(lx, lz);
                addCollider(wx - 0.15f, 0f, wz - 0.15f, wx + 0.15f, 3.3f, wz + 0.15f);
            }
        }
    }

    /** Perforated brick panel: a brick slab with a grid of dark recesses. */
    private void addJaliPanel(String name, float cx, float bottomY, float cz, float hx, float hy, Node parent) {
        addBox(name, brickDarkMat, cx, bottomY + hy, cz, hx, hy, 0.12f, parent);
        int cols = Math.max(1, Math.round(hx * 2f / 0.45f));
        int rows = Math.max(1, Math.round(hy * 2f / 0.45f));
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                float x = cx - hx + (c + 0.5f) * (hx * 2f / cols);
                float y = bottomY + (r + 0.5f) * (hy * 2f / rows);
                addBox(name + "_h" + r + "_" + c, steelMat, x, y, cz - 0.13f, 0.11f, 0.11f, 0.02f, parent);
            }
        }
    }

    /**
     * Auditorium: 170 x 85 ft under a reinforced-concrete parabolic vault (Daily Star); OSM footprint
     * centre about (-8, 101.4), long axis parallel to the union block. Width 24 m is a compromise between
     * the two sources; the 14 m crown height is an estimate.
     */
    private void buildTSCAuditorium() {
        Node n = tscNode;
        float lx = tscFrame.localX(-8.0f, 101.4f), lz = tscFrame.localZ(-8.0f, 101.4f);
        float hl = 25.9f, hw = 12.0f, wallTop = 8.0f, rise = 6.0f;

        addBox("Aud_Walls", plasterMat, lx, wallTop * 0.5f, lz, hl, wallTop * 0.5f, hw, n);
        addBox("Aud_BrickBase", brickMat, lx, 0.6f, lz, hl + 0.05f, 0.6f, hw + 0.05f, n);
        for (int i = 0; i < 11; i++) {
            float x = lx - 23f + i * 4.6f;
            for (int s = -1; s <= 1; s += 2) {
                addBox("Aud_Win_" + i + "_" + s, glassMat, x, 4.8f, lz + s * (hw + 0.03f), 0.5f, 2.0f, 0.04f, n);
            }
        }
        // Entrance canopy on the north face (towards the union block)
        float northZ = lz - hw;
        addBox("Aud_Doors", glassMat, lx, 1.6f, northZ - 0.03f, 4.5f, 1.5f, 0.05f, n);
        addBox("Aud_Canopy", concreteMat, lx, 3.5f, northZ - 2.5f, 6f, 0.12f, 2.5f, n);
        for (int s = -1; s <= 1; s += 2) {
            addBox("Aud_CanopyCol_" + s, concreteMat, lx + s * 5f, 1.75f, northZ - 4.6f, 0.18f, 1.75f, 0.18f, n);
            float wx = tscFrame.worldX(lx + s * 5f, northZ - 4.6f), wz = tscFrame.worldZ(lx + s * 5f, northZ - 4.6f);
            addCollider(wx - 0.3f, 0f, wz - 0.3f, wx + 0.3f, 3.5f, wz + 0.3f);
        }
        // Parabolic vault
        Mesh vault = createVaultMesh(hl * 2f + 0.6f, hw * 2f + 0.3f, wallTop, rise, 24, tileOf.get(concreteMat));
        Geometry vg = new Geometry("Aud_Vault", vault);
        vg.setLocalTranslation(lx, 0f, lz);
        finish(vg, concreteMat, n);
        addFrameCollider(tscFrame, lx, lz, hl, hw, 0f, wallTop + rise);
    }

    /** Cafeteria: two single-storey wings around a green (Daily Star; OSM way 1560050747). */
    private void buildTSCCafeteria() {
        Node n = tscNode;
        float lx = tscFrame.localX(57f, 131f), lz = tscFrame.localZ(57f, 131f);
        for (int s = -1; s <= 1; s += 2) {
            float z = lz + s * 9f;
            addBox("Cafe_Wing_" + s, plasterWarmMat, lx, 1.8f, z, 30f, 1.8f, 4.5f, n);
            addBox("Cafe_Roof_" + s, concreteMat, lx, 3.75f, z, 30.5f, 0.15f, 5.0f, n);
            for (int i = 0; i < 10; i++) {
                addBox("Cafe_Win_" + s + "_" + i, glassMat, lx - 27f + i * 6f, 1.9f, z - s * (4.5f + 0.03f), 1.6f, 0.7f, 0.04f, n);
            }
            addFrameCollider(tscFrame, lx, z, 30f, 4.5f, 0f, 4f);
        }
    }

    // ---- Central Library ----------------------------------------------------------------------------

    /**
     * Muzharul Islam's library: two rectangular blocks raised on pilotis, a ramp to the first floor and a
     * vaulted third block (Banglapedia). Footprints from OSM ways 295394032 and 1152628065; 3 levels.
     * Storey heights, column grid and facade pattern are modelling choices.
     */
    private void buildCentralLibrary() {
        // Building I: east-west block A and north-south wing B
        buildLibraryBar("LibI_A", -79.5f, -158.5f, 18.5f, 10.5f, false, false);
        buildLibraryBar("LibI_B", -87.5f, -120.25f, 9.5f, 27.75f, false, true);
        // Building II: vaulted east-west bar C and north-south bar D (nearest the sculpture)
        buildLibraryBar("LibII_C", -73f, -82.5f, 19.5f, 6.1f, true, false);
        buildLibraryBar("LibII_D", -44.5f, -92f, 9.5f, 29f, false, true);

        // External ramp to the first floor along the east face of bar D, rising northwards
        float rx = -33.4f, zBottom = -66f, zTop = -96f, h = 4.0f;
        float len = zBottom - zTop;
        float slope = FastMath.atan(h / len);
        Geometry ramp = new Geometry("Lib_Ramp", new Box(1.5f, 0.12f, len * 0.5f + 0.2f));
        ramp.setLocalTranslation(rx, h * 0.5f, (zBottom + zTop) * 0.5f);
        ramp.setLocalRotation(new Quaternion().fromAngleAxis(slope, Vector3f.UNIT_X));
        finish(ramp, concreteMat, null);
        Geometry parapet = new Geometry("Lib_RampParapet", new Box(0.08f, 0.5f, len * 0.5f + 0.2f));
        parapet.setLocalTranslation(rx + 1.45f, h * 0.5f + 0.55f, (zBottom + zTop) * 0.5f);
        parapet.setLocalRotation(new Quaternion().fromAngleAxis(slope, Vector3f.UNIT_X));
        finish(parapet, concreteMat, null);
        int slices = 6;
        for (int i = 0; i < slices; i++) {
            float z1 = zBottom - i * len / slices, z0 = z1 - len / slices;
            float topY = h * (i + 1) / slices + 0.6f;
            addCollider(rx - 1.5f, 0f, z0, rx + 1.5f, topY, z1);
        }
        // Landing posts at the ramp top
        addBox("Lib_RampLanding", concreteMat, rx, 4.2f, zTop - 1.5f, 1.6f, 0.15f, 1.5f);
        addBox("Lib_RampPost", concreteMat, rx + 1.2f, 2.1f, zTop - 2.5f, 0.2f, 2.1f, 0.2f);
        addCollider(rx + 1.0f, 0f, zTop - 2.7f, rx + 1.4f, 4.2f, zTop - 2.3f);
    }

    private void buildLibraryBar(String name, float cx, float cz, float hx, float hz, boolean vaulted, boolean finsOnEast) {
        float colTop = 4.0f, slab1 = 4.4f, floor1Top = 7.6f, slab2 = 8.0f, floor2Top = 11.2f;

        // Pilotis: perimeter columns plus a centre row, open ground floor
        int nx = Math.max(2, Math.round(hx * 2f / 6f)) + 1;
        int nz = Math.max(2, Math.round(hz * 2f / 6f)) + 1;
        List<float[]> cols = new ArrayList<>();
        for (int i = 0; i < nx; i++) {
            float x = cx - hx + 0.5f + (hx * 2f - 1f) * i / (nx - 1);
            cols.add(new float[]{x, cz - hz + 0.5f});
            cols.add(new float[]{x, cz + hz - 0.5f});
            if (hz > 5f) cols.add(new float[]{x, cz});
        }
        for (int j = 1; j < nz - 1; j++) {
            float z = cz - hz + 0.5f + (hz * 2f - 1f) * j / (nz - 1);
            cols.add(new float[]{cx - hx + 0.5f, z});
            cols.add(new float[]{cx + hx - 0.5f, z});
        }
        int k = 0;
        for (float[] c : cols) {
            addBox(name + "_Col" + (k++), concreteMat, c[0], colTop * 0.5f, c[1], 0.28f, colTop * 0.5f, 0.28f);
            addCollider(c[0] - 0.4f, 0f, c[1] - 0.4f, c[0] + 0.4f, colTop, c[1] + 0.4f);
        }
        // Enclosed core on the ground floor (stairs, entrance hall), set back from the columns
        float coreHx = Math.max(1.5f, hx - 3f), coreHz = Math.max(1.5f, hz - 3f);
        addBox(name + "_Core", plasterMat, cx, colTop * 0.5f, cz, coreHx, colTop * 0.5f, coreHz);
        addCollider(cx - coreHx, 0f, cz - coreHz, cx + coreHx, colTop, cz + coreHz);
        addBox(name + "_CoreDoor", glassMat, cx, 1.4f, cz + coreHz + 0.03f, 1.2f, 1.4f, 0.04f);

        // Slabs and two brick upper floors with strip windows on all faces
        addBox(name + "_Slab1", concreteMat, cx, (colTop + slab1) * 0.5f, cz, hx + 0.4f, (slab1 - colTop) * 0.5f, hz + 0.4f);
        addBox(name + "_Floor1", brickMat, cx, (slab1 + floor1Top) * 0.5f, cz, hx, (floor1Top - slab1) * 0.5f, hz);
        addBox(name + "_Slab2", concreteMat, cx, (floor1Top + slab2) * 0.5f, cz, hx + 0.4f, (slab2 - floor1Top) * 0.5f, hz + 0.4f);
        addBox(name + "_Floor2", brickMat, cx, (slab2 + floor2Top) * 0.5f, cz, hx, (floor2Top - slab2) * 0.5f, hz);
        float[] floorY = {slab1 + 2.0f, slab2 + 2.0f};
        for (int f = 0; f < 2; f++) {
            for (int s = -1; s <= 1; s += 2) {
                addBox(name + "_WinX" + f + s, glassMat, cx + s * (hx + 0.03f), floorY[f], cz, 0.04f, 0.8f, hz - 1.0f);
                addBox(name + "_WinZ" + f + s, glassMat, cx, floorY[f], cz + s * (hz + 0.03f), hx - 1.0f, 0.8f, 0.04f);
            }
        }
        if (vaulted) {
            Mesh vault = createVaultMesh(hx * 2f + 0.8f, hz * 2f + 0.8f, floor2Top, 2.6f, 16, tileOf.get(concreteMat));
            Geometry vg = new Geometry(name + "_Vault", vault);
            vg.setLocalTranslation(cx, 0f, cz);
            finish(vg, concreteMat, null);
        } else {
            addBox(name + "_Roof", concreteMat, cx, floor2Top + 0.35f, cz, hx + 0.4f, 0.35f, hz + 0.4f);
        }
        // Vertical concrete fins (brise-soleil) on the east face
        if (finsOnEast) {
            int count = (int) ((hz * 2f - 1f) / 1.5f);
            for (int i = 0; i <= count; i++) {
                float z = cz - hz + 0.75f + i * 1.5f;
                addBox(name + "_Fin" + i, concreteMat, cx + hx + 0.5f, (slab1 + floor2Top) * 0.5f, z, 0.45f, (floor2Top - slab1) * 0.5f, 0.07f);
            }
        }
    }

    // ---- Hakim Chattar --------------------------------------------------------------------------

    private void buildHakimChattar() {
        float hx = HAKIM_X + 3f, hz = HAKIM_Z + 2f;
        // Raised brick pavilion with a tea stall, benches and the banyan tree (tree and stall: scene choices)
        addBox("HakimBase", brickMat, hx, 0.15f, hz, 6.0f, 0.15f, 5.0f);
        addSurfaceRect(new Frame(0f, 0f, 0f), hx, hz, 6.0f, 5.0f, 0.30f);
        addBox("HakimTeaStall", woodMat, hx - 2.0f, 0.3f + 1.0f, hz, 1.6f, 1.0f, 1.1f);
        addCollider(hx - 3.6f, 0f, hz - 1.1f, hx - 0.4f, 2.4f, hz + 1.1f);
        Geometry roof = new Geometry("HakimRoof", new Box(2.4f, 0.05f, 1.8f));
        roof.setLocalTranslation(hx - 2.0f, 2.75f, hz);
        roof.rotate(0.06f, 0f, -0.05f);
        finish(roof, steelMat, null);
        for (int s = -1; s <= 1; s += 2) {
            addBox("HakimRoofPost" + s, woodMat, hx - 2.0f + s * 2.1f, 1.5f, hz + 1.5f, 0.05f, 1.2f, 0.05f);
            addBox("HakimBench" + s, woodMat, hx + 2.5f, 0.3f + 0.42f, hz + s * 2.0f, 1.6f, 0.06f, 0.3f);
            addBox("HakimBenchLegs" + s, concreteDarkMat, hx + 2.5f, 0.3f + 0.18f, hz + s * 2.0f, 1.4f, 0.18f, 0.2f);
        }
        buildBanyanTree(HAKIM_X - 4f, HAKIM_Z - 6f);
    }

    // ---- Curzon Hall (Faculty of Science) --------------------------------------------------------

    private void buildCurzonHall() {
        curzonNode = new Node("CurzonHall");
        curzonNode.setLocalTranslation(CURZON_X, 0f, CURZON_Z);
        curzonNode.setLocalRotation(new Quaternion().fromAngleAxis(CURZON_YAW, Vector3f.UNIT_Y));
        staticNode.attachChild(curzonNode);

        // 1. Front entrance garden plaza
        addBox("CurzonPlaza", pavingMat, 0f, PLAZA_HEIGHT * 0.5f, -11f, 13f, PLAZA_HEIGHT * 0.5f, 7f, curzonNode);
        addSurfaceRect(curzonFrame, 0f, -11f, 13f, 7f, PLAZA_HEIGHT);

        // 2. Base plinth
        addBox("CurzonBase", concreteMat, 0f, 0.4f, 0f, 25f, 0.4f, 6.2f, curzonNode);
        addSurfaceRect(curzonFrame, 0f, 0f, 25f, 6.2f, 0.8f);

        // 3. Central Block (Grand Indo-Saracenic Pavilion)
        addBox("CurzonCenterBlock", brickMat, 0f, 5.6f, 0f, 7.2f, 4.8f, 6.0f, curzonNode);
        addBox("CurzonCenterCornice", plasterMat, 0f, 10.55f, 0f, 7.5f, 0.25f, 6.3f, curzonNode);
        addBox("CurzonPortalArch", brickDarkMat, 0f, 3.4f, -6.05f, 2.6f, 2.6f, 0.1f, curzonNode);
        addBox("CurzonPortalTrim", plasterMat, 0f, 3.4f, -6.15f, 2.8f, 2.8f, 0.05f, curzonNode);

        // Mughal Dome on Drum
        addCylinderY("CurzonDomeDrum", plasterMat, 0f, 10.8f, 0f, 3.2f, 1.6f, curzonNode);
        Geometry dome = new Geometry("CurzonMainDome", new Sphere(20, 20, 3.2f));
        dome.setLocalTranslation(0f, 13.6f, 0f);
        finish(dome, plasterWarmMat, curzonNode);
        addBox("CurzonFinial", memorialGoldMat, 0f, 17.2f, 0f, 0.08f, 0.6f, 0.08f, curzonNode);

        // 4. Symmetrical Left & Right Wings
        addBox("CurzonWingL", brickMat, -16.5f, 4.6f, 0f, 9.2f, 3.8f, 5.5f, curzonNode);
        addBox("CurzonWingR", brickMat, 16.5f, 4.6f, 0f, 9.2f, 3.8f, 5.5f, curzonNode);
        addBox("CurzonBandL", plasterMat, -16.5f, 8.5f, 0f, 9.4f, 0.15f, 5.7f, curzonNode);
        addBox("CurzonBandR", plasterMat, 16.5f, 8.5f, 0f, 9.4f, 0.15f, 5.7f, curzonNode);

        // Verandah Arched Colonnade (front pillars)
        for (int p = 0; p < 4; p++) {
            float pxL = -23.5f + p * 4.6f;
            float pxR = 9.5f + p * 4.6f;
            addCylinderY("CurzonPillarL_" + p, plasterMat, pxL, 0.8f, -5.6f, 0.22f, 7.2f, curzonNode);
            addCylinderY("CurzonPillarR_" + p, plasterMat, pxR, 0.8f, -5.6f, 0.22f, 7.2f, curzonNode);
        }

        // 5. Corner Chhatris (towers with domed chhatris)
        float[] chhatriX = {-24.5f, 24.5f};
        float[] chhatriZ = {-5.5f, 5.5f};
        int cIdx = 0;
        for (float cx : chhatriX) {
            for (float cz : chhatriZ) {
                addBox("CurzonTower_" + cIdx, brickMat, cx, 5.0f, cz, 1.2f, 4.2f, 1.2f, curzonNode);
                addBox("CurzonTowerCornice_" + cIdx, plasterMat, cx, 9.3f, cz, 1.4f, 0.15f, 1.4f, curzonNode);
                Geometry chhatriDome = new Geometry("CurzonChhatri_" + cIdx, new Sphere(12, 12, 1.15f));
                chhatriDome.setLocalTranslation(cx, 10.3f, cz);
                finish(chhatriDome, plasterWarmMat, curzonNode);
                addBox("CurzonSpire_" + cIdx, memorialGoldMat, cx, 11.8f, cz, 0.05f, 0.35f, 0.05f, curzonNode);
                cIdx++;
            }
        }

        // Curzon Garden Hedges
        addBox("CurzonHedgeL", hedgeMat, -10f, 0.45f, -17f, 4f, 0.45f, 0.6f, curzonNode);
        addBox("CurzonHedgeR", hedgeMat, 10f, 0.45f, -17f, 4f, 0.45f, 0.6f, curzonNode);

        // Colliders around Curzon Hall footprint
        addCollider(CURZON_X - 22f, 0f, CURZON_Z - 22f, CURZON_X + 22f, 14f, CURZON_Z + 22f);
    }

    // ---- Vegetation -----------------------------------------------------------------------------

    private void buildVegetation() {
        // Species are stand-ins (docs/REFERENCE_LEDGER.md, section 6): the "krishnachura", "radhachura"
        // and "shade" variants are Poly Haven island_tree_02 (a 3.4 m scan) scaled to 9-12 m with a
        // blossom tint on some leaves; "jarul" is Poly Haven jacaranda_tree (19.5 m scan) at about 0.6.
        float[][] krishnachura = {
            {-22f, -22f}, {-38f, -21f}, {-54f, -21f}, {-70f, -22f},
            {-30f, 6f}, {-48f, 6f}, {-66f, 7f},
            {34f, 16f}, {-5f, 32f}, {22f, 40f}, {-48f, 44f}, {50f, 28f}
        };
        for (int i = 0; i < krishnachura.length; i++) {
            placeTree("Krishnachura_" + i, "island_tree_02", "krishnachura", krishnachura[i][0], krishnachura[i][1],
                2.7f + (i % 3) * 0.25f, i * 137f, 0.32f, krishnachuraBloomMat);
        }
        float[][] jarul = {{-40f, -50f}, {-25f, -46f}, {-66f, -56f}, {-100f, -82f}, {-20f, -75f}, {-22f, -105f}};
        for (int i = 0; i < jarul.length; i++) {
            placeTree("Jarul_" + i, "jacaranda_tree", "jarul", jarul[i][0], jarul[i][1],
                0.55f + (i % 2) * 0.08f, 40f + i * 97f, 1.1f, jarulBloomMat);
        }
        float[][] radhachura = {{-10f, 46f}, {20f, 42f}, {-24f, 84f}, {40f, 92f}};
        for (int i = 0; i < radhachura.length; i++) {
            placeTree("Radhachura_" + i, "island_tree_02", "radhachura", radhachura[i][0], radhachura[i][1],
                2.6f + (i % 2) * 0.3f, 20f + i * 151f, 0.32f, radhachuraBloomMat);
        }
        // Plain shade trees further out
        float[][] shade = {{-120f, -30f}, {-140f, 10f}, {70f, -50f}, {80f, -20f}, {-80f, 100f}, {100f, 60f}, {-110f, -120f}, {0f, -70f}};
        for (int i = 0; i < shade.length; i++) {
            placeTree("Shade_" + i, "island_tree_02", "shade", shade[i][0], shade[i][1],
                3.2f + (i % 3) * 0.2f, 70f + i * 113f, 0.32f, null);
        }
    }

    /**
     * Places a tree model (see {@link TreeLibrary}) on the ground at (tx, tz) with a trunk collider;
     * {@code trunkHalf} is the trunk half-width of the unscaled model. Falls back to the procedural
     * sphere tree if the model is missing.
     */
    private void placeTree(String name, String folder, String variant, float tx, float tz, float scale, float yawDeg,
                           float trunkHalf, Material fallbackBloomMat) {
        float ground = groundHeightAt(tx, tz);
        Node tree = trees.place(name, folder, variant, tx, ground, tz, scale, yawDeg);
        if (tree == null) {
            fallbackTreeCount++;
            createOrnamentalTree(name, tx, tz, 4.6f, 3.0f, fallbackBloomMat);
            return;
        }
        treeNode.attachChild(tree);
        treeCount++;
        float h = trunkHalf * scale;
        addCollider(tx - h, ground, tz - h, tx + h, ground + 3f * scale, tz + h);
    }

    /** Procedural tree used only when a tree model fails to load. */
    private void createOrnamentalTree(String name, float tx, float tz, float height, float canopyRadius, Material bloomMat) {
        Geometry trunk = new Geometry(name + "_Trunk", new Cylinder(2, 12, 0.3f, height, true));
        trunk.rotate(FastMath.HALF_PI, 0f, 0f);
        trunk.setLocalTranslation(tx, height * 0.5f, tz);
        finish(trunk, treeTrunkMat, null);
        addCollider(tx - 0.4f, 0f, tz - 0.4f, tx + 0.4f, height, tz + 0.4f);

        Geometry canopy = new Geometry(name + "_Canopy", new Sphere(12, 12, canopyRadius));
        canopy.setLocalTranslation(tx, height + canopyRadius * 0.4f, tz);
        canopy.setLocalScale(1.35f, 0.70f, 1.35f);
        finish(canopy, foliageMat, null);

        if (bloomMat != null) {
            Geometry bloom = new Geometry(name + "_Bloom", new Sphere(10, 10, canopyRadius * 0.75f));
            bloom.setLocalTranslation(tx + 0.2f, height + canopyRadius * 0.65f, tz + 0.15f);
            bloom.setLocalScale(1.10f, 0.45f, 1.10f);
            finish(bloom, bloomMat, null);
        }
    }

    /** The banyan by Hakim Chattar: the large shade variant stands in for it (no banyan scan is available). */
    private void buildBanyanTree(float bx, float bz) {
        float ground = groundHeightAt(bx, bz);
        Node tree = trees.place("Banyan", "island_tree_02", "shade", bx, ground, bz, 4.4f, 215f);
        if (tree != null) {
            treeNode.attachChild(tree);
            treeCount++;
            addCollider(bx - 1.3f, ground, bz - 1.3f, bx + 1.3f, ground + 12f, bz + 1.3f);
            return;
        }
        fallbackTreeCount++;
        Geometry trunk = new Geometry("Banyan_Trunk", new Cylinder(2, 16, 0.95f, 5.2f, true));
        trunk.rotate(FastMath.HALF_PI, 0f, 0f);
        trunk.setLocalTranslation(bx, 2.6f, bz);
        finish(trunk, treeTrunkMat, null);
        addCollider(bx - 1.2f, 0f, bz - 1.2f, bx + 1.2f, 5.2f, bz + 1.2f);
        Geometry canopy = new Geometry("Banyan_Canopy", new Sphere(16, 16, 5.0f));
        canopy.setLocalTranslation(bx, 6.4f, bz);
        canopy.setLocalScale(1.7f, 0.55f, 1.7f);
        finish(canopy, foliageMat, null);
    }

    // ---- Props ----------------------------------------------------------------------------------

    private void buildCampusProps() {
        List<float[]> lamps = new ArrayList<>();
        // Around the roundabout, skipping the angles where road arms leave it
        float[] ringAngles = {22.5f, 67.5f, 157.5f, 247.5f, 292.5f, 337.5f};
        for (float deg : ringAngles) {
            float a = deg * FastMath.DEG_TO_RAD;
            lamps.add(new float[]{FastMath.cos(a) * 27.5f, FastMath.sin(a) * 27.5f});
        }
        float[][] fixed = {{-45f, -17f}, {-70f, -17f}, {-95f, -17f}, {-45f, 1.5f}, {-70f, 1.5f},
            {-22.4f, 21.7f}, {-7.6f, 30.2f}, {-34.9f, 43.4f}, {-20.1f, 51.9f},
            {14f, 30f}, {26f, 48f}, {-3f, 44f}, {36f, 46f}};
        for (float[] f : fixed) lamps.add(f);

        int i = 0;
        for (float[] l : lamps) {
            float lx = l[0], lz = l[1];
            Geometry pole = new Geometry("LampPole_" + i, new Cylinder(2, 10, 0.07f, 4.2f, true));
            pole.rotate(FastMath.HALF_PI, 0f, 0f);
            pole.setLocalTranslation(lx, 2.1f, lz);
            finish(pole, lampMat, null);
            addBox("LampArm_" + i, lampMat, lx, 4.1f, lz, 0.45f, 0.03f, 0.03f);
            for (int s = -1; s <= 1; s += 2) {
                Geometry globe = new Geometry("LampGlobe_" + i + "_" + s, new Sphere(8, 8, 0.12f));
                globe.setLocalTranslation(lx + s * 0.45f, 4.22f, lz);
                finish(globe, memorialGoldMat, null);
            }
            addCollider(lx - 0.2f, 0f, lz - 0.2f, lx + 0.2f, 4.2f, lz + 0.2f);
            i++;
        }

        // Campus seating benches at social congregation nodes
        addBench("Bench_Hakim_1", -78f, groundHeightAt(-78f, -44f), -44f, FastMath.HALF_PI);
        addBench("Bench_Hakim_2", -86f, groundHeightAt(-86f, -50f), -50f, 0f);
        addBench("Bench_Hakim_3", -75f, groundHeightAt(-75f, -52f), -52f, -FastMath.HALF_PI);

        addBench("Bench_Lib_1", -45f, groundHeightAt(-45f, -56f), -56f, 0f);
        addBench("Bench_Lib_2", -54f, groundHeightAt(-54f, -56f), -56f, 0f);

        addBench("Bench_Raju_E", 14f, groundHeightAt(14f, 12f), 12f, -0.6f);
        addBench("Bench_Raju_W", -14f, groundHeightAt(-14f, 12f), 12f, 0.6f);
        addBench("Bench_Raju_NW", -14f, groundHeightAt(-14f, -12f), -12f, 2.5f);
        addBench("Bench_Raju_NE", 14f, groundHeightAt(14f, -12f), -12f, -2.5f);
    }

    private void addBench(String name, float cx, float groundY, float cz, float yaw) {
        addYawedBox(name + "_Seat", woodMat, cx, groundY + 0.44f, cz, 0.85f, 0.03f, 0.22f, yaw);
        float bx = cx - FastMath.sin(yaw) * 0.20f;
        float bz = cz - FastMath.cos(yaw) * 0.20f;
        addYawedBox(name + "_Back", woodMat, bx, groundY + 0.72f, bz, 0.85f, 0.16f, 0.03f, yaw);
        float lx1 = cx + FastMath.cos(yaw) * 0.72f;
        float lz1 = cz - FastMath.sin(yaw) * 0.72f;
        float lx2 = cx - FastMath.cos(yaw) * 0.72f;
        float lz2 = cz + FastMath.sin(yaw) * 0.72f;
        addYawedBox(name + "_LegL", steelMat, lx1, groundY + 0.22f, lz1, 0.04f, 0.22f, 0.20f, yaw);
        addYawedBox(name + "_LegR", steelMat, lx2, groundY + 0.22f, lz2, 0.04f, 0.22f, 0.20f, yaw);
        addCollider(cx - 0.85f, groundY, cz - 0.3f, cx + 0.85f, groundY + 0.85f, cz + 0.3f);
    }

    // ---- Banners and road graffiti ----------------------------------------------------------------

    private void buildJulyMovementBannersAndMurals(AssetManager assetManager) {
        if (textRenderer == null || assetManager == null) return;

        try {
            // Texts come from Slogans.forLevel01(): the level is set in the first days of July 2024,
            // so only quota-reform slogans appear (docs/REFERENCE_LEDGER.md, section 7).
            java.util.List<Slogans.Slogan> slogans = Slogans.forLevel01();

            // 1. Road graffiti on the roundabout carriageway, read when walking towards the sculpture
            Texture2D quotaTex = textRenderer.createTexture(
                slogans.get(0).bengali,
                28f, true,
                new Color(255, 255, 255),
                new Color(20, 20, 22, 230),
                20, 10
            );
            Geometry quotaDecal = createDecalQuad(assetManager, "Decal_Quota", quotaTex, 6.4f, 1.6f);
            quotaDecal.setLocalTranslation(-3.2f, 0.03f, 13.5f);
            quotaDecal.rotate(-FastMath.HALF_PI, 0, 0);
            rootNode.attachChild(quotaDecal);

            // 2. Road graffiti behind the spawn point
            Texture2D july36Tex = textRenderer.createTexture(
                slogans.get(2).bengali,
                28f, true,
                new Color(255, 230, 80),
                new Color(20, 20, 22, 230),
                20, 10
            );
            Geometry july36Decal = createDecalQuad(assetManager, "Decal_July36", july36Tex, 7.2f, 1.6f);
            july36Decal.setLocalTranslation(-3.6f, 0.03f, 23.5f);
            july36Decal.rotate(-FastMath.HALF_PI, 0, 0);
            rootNode.attachChild(july36Decal);

            // 3. Banner on the south face of Central Library Building II (faces the sculpture)
            Texture2D bannerTex1 = textRenderer.createTexture(
                slogans.get(1).bengali,
                32f, true,
                new Color(255, 255, 255),
                new Color(180, 25, 20, 240),
                24, 12
            );
            Geometry libBanner = createDecalQuad(assetManager, "Banner_Library", bannerTex1, 8.5f, 1.8f);
            libBanner.setLocalTranslation(-44.5f - 4.25f, 5.0f, -63f + 0.08f);
            rootNode.attachChild(libBanner);

            // 4. Banner on the north face of the TSC union block, over the verandah
            Texture2D bannerTex2 = textRenderer.createTexture(
                slogans.get(3).bengali,
                30f, true,
                new Color(255, 245, 120),
                new Color(15, 30, 25, 240),
                24, 12
            );
            Geometry tscBanner = createDecalQuad(assetManager, "Banner_TSC", bannerTex2, 8.0f, 1.8f);
            tscBanner.setLocalTranslation(4.0f, 5.2f, -5.65f - 0.45f);
            tscBanner.rotate(0f, FastMath.PI, 0f);
            tscDecalNode.attachChild(tscBanner);

            // 6. Labels on the two notice boards that open the library and TSC document cards
            if (libraryBoardPos != null && tscBoardPos != null) {
                Color boardInk = new Color(245, 238, 220);
                Color boardBg = new Color(38, 30, 26, 240);
                Texture2D libBoardTex = textRenderer.createTexture("তথ্যফলক · কেন্দ্রীয় লাইব্রেরি", 26f, true, boardInk, boardBg, 20, 12);
                Geometry libBoard = createDecalQuad(assetManager, "Label_LibBoard", libBoardTex, 1.6f, 0.9f);
                libBoard.setLocalTranslation(libraryBoardPos.x - 0.8f, libraryBoardPos.y + 0.2f, libraryBoardPos.z + 0.05f);
                rootNode.attachChild(libBoard);

                Texture2D tscBoardTex = textRenderer.createTexture("তথ্যফলক · টিএসসি", 26f, true, boardInk, boardBg, 20, 12);
                Geometry tscBoard = createDecalQuad(assetManager, "Label_TscBoard", tscBoardTex, 1.6f, 0.9f);
                tscBoard.setLocalTranslation(6f + 0.8f, tscBoardPos.y + 0.2f, -7f - 0.05f);
                tscBoard.rotate(0f, FastMath.PI, 0f);
                tscDecalNode.attachChild(tscBoard);

                if (curzonBoardPos != null) {
                    Texture2D curzonBoardTex = textRenderer.createTexture("তথ্যফলক · কার্জন হল", 26f, true, boardInk, boardBg, 20, 12);
                    Geometry curzonBoard = createDecalQuad(assetManager, "Label_CurzonBoard", curzonBoardTex, 1.6f, 0.9f);
                    curzonBoard.setLocalTranslation(curzonBoardPos.x - 0.8f, curzonBoardPos.y + 0.2f, curzonBoardPos.z + 0.05f);
                    rootNode.attachChild(curzonBoard);
                }
            }

            // 5. Road graffiti on the west arm (Nilkhet road), read when walking towards the library
            Texture2D westTex = textRenderer.createTexture(
                slogans.get(4).bengali,
                28f, true,
                new Color(255, 255, 255),
                new Color(20, 20, 22, 230),
                20, 10
            );
            Geometry westDecal = createDecalQuad(assetManager, "Decal_West", westTex, 9.0f, 2.2f);
            westDecal.setLocalTranslation(-33f, ROAD_TOP + 0.01f, -3.5f);
            westDecal.rotate(-FastMath.HALF_PI, 0, 0);
            westDecal.rotate(0, 0, FastMath.HALF_PI);
            rootNode.attachChild(westDecal);

            // 6. Campus road directional signs (connecting Central Library, TSC, and Curzon Hall)
            Color signWhite = new Color(255, 255, 255);
            Color signGreen = new Color(14, 46, 30, 235);
            Color signBlue = new Color(18, 36, 52, 235);

            Texture2D curzonSignTex = textRenderer.createTexture(
                "দোয়েল চত্বর ও কার্জন হল সড়ক  ·  Road to Curzon Hall",
                20f, true, signWhite, signGreen, 16, 8
            );
            Geometry curzonSign = createDecalQuad(assetManager, "Sign_Curzon", curzonSignTex, 3.8f, 0.95f);
            curzonSign.setLocalTranslation(22f, 1.8f, 20f);
            curzonSign.rotate(0f, -FastMath.QUARTER_PI, 0f);
            rootNode.attachChild(curzonSign);
            addBox("SignPost_Curzon", steelMat, 22f, 0.9f, 20f, 0.04f, 0.9f, 0.04f);

            Texture2D nilkhetSignTex = textRenderer.createTexture(
                "নীলক্ষেত ও কেন্দ্রীয় লাইব্রেরি  ·  To Central Library",
                20f, true, signWhite, signBlue, 16, 8
            );
            Geometry nilkhetSign = createDecalQuad(assetManager, "Sign_Nilkhet", nilkhetSignTex, 3.8f, 0.95f);
            nilkhetSign.setLocalTranslation(-26f, 1.8f, 6f);
            nilkhetSign.rotate(0f, FastMath.HALF_PI, 0f);
            rootNode.attachChild(nilkhetSign);
            addBox("SignPost_Nilkhet", steelMat, -26f, 0.9f, 6f, 0.04f, 0.9f, 0.04f);

            Texture2D shahbaghSignTex = textRenderer.createTexture(
                "শাহবাগ ও জাতীয় জাদুঘর  ·  To Shahbagh",
                20f, true, signWhite, signBlue, 16, 8
            );
            Geometry shahbaghSign = createDecalQuad(assetManager, "Sign_Shahbagh", shahbaghSignTex, 3.4f, 0.95f);
            shahbaghSign.setLocalTranslation(16f, 1.8f, -22f);
            shahbaghSign.rotate(0f, -FastMath.HALF_PI, 0f);
            rootNode.attachChild(shahbaghSign);
            addBox("SignPost_Shahbagh", steelMat, 16f, 0.9f, -22f, 0.04f, 0.9f, 0.04f);

            System.out.println("[CampusWorld] July Movement Bengali street murals and banners attached: " + slogans.size() + " Level 01 slogans.");
        } catch (Throwable t) {
            System.err.println("[CampusWorld] Failed to generate text decals: " + t.getMessage());
        }
    }

    private Geometry createDecalQuad(AssetManager assetManager, String name, Texture2D texture, float width, float height) {
        Quad quad = new Quad(width, height);
        Geometry geom = new Geometry(name, quad);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setTexture("ColorMap", texture);
        mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        mat.getAdditionalRenderState().setDepthWrite(false);
        geom.setMaterial(mat);
        geom.setQueueBucket(RenderQueue.Bucket.Transparent);
        geom.setShadowMode(RenderQueue.ShadowMode.Off);
        return geom;
    }

    // ------------------------------------------------------------------------------------------

    public List<float[]> getColliders() {
        return colliders;
    }

    public Node getRootNode() {
        return rootNode;
    }

    /** World position of a point given in the TSC buildings' local frame (for placing NPCs and objectives). */
    public Vector3f tscLocalToWorld(float lx, float lz) {
        return new Vector3f(tscFrame.worldX(lx, lz), 0f, tscFrame.worldZ(lx, lz));
    }
}
