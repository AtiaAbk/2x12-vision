package bd.july2024.character;

import com.jme3.anim.AnimComposer;
import com.jme3.anim.SkinningControl;
import com.jme3.anim.tween.action.Action;
import com.jme3.asset.AssetManager;
import com.jme3.bounding.BoundingBox;
import com.jme3.material.MatParam;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.VertexBuffer;
import com.jme3.scene.shape.Cylinder;

import java.util.HashMap;
import java.util.Map;

/**
 * Rigged, skinned humanoid character for Arif and the companions, loaded from a glTF described by a
 * {@link CharacterSpec}. All current presets are CC0 models by Quaternius (see docs/ASSET_REGISTER.md).
 *
 * The glTF PBR materials are replaced with the scene's Phong materials, keeping each material's
 * authored base colour (and vertex colours where present) so the character is lit like the rest of
 * the campus. Optionally the spec's shirt and trouser materials are tinted per character.
 *
 * Public surface kept from the earlier procedural version: constructor(assetManager, shirtColor,
 * pantsColor), animate(speed, stridePhase, idleTime, isCrouching), getRootNode().
 */
public class PlayerMesh {

    /** Standing height the model is scaled to, in metres. */
    public static final float TARGET_HEIGHT = 1.72f;

    private final Node rootNode;
    private final CharacterSpec spec;
    private AnimComposer composer;
    private final Map<String, Action> actions = new HashMap<>();
    private String currentClip = null;
    private boolean airborne = false;
    private boolean talking = false;
    private boolean modelLoaded = false;

    /** Loads the default character (Arif) tinted with the given colours. */
    public PlayerMesh(AssetManager assetManager, ColorRGBA shirtColor, ColorRGBA pantsColor) {
        this(assetManager, CharacterSpec.ARIF, shirtColor, pantsColor);
    }

    /**
     * @param shirtColor tint for the spec's shirt material, or null to keep the authored colour
     * @param pantsColor tint for the spec's trouser material, or null to keep the authored colour
     */
    public PlayerMesh(AssetManager assetManager, CharacterSpec spec, ColorRGBA shirtColor, ColorRGBA pantsColor) {
        this.rootNode = new Node("CharacterRoot_" + spec.id);
        this.spec = spec;

        Spatial model = null;
        if (assetManager != null) {
            // The glTF loader logs one warning per cubic-spline sampler ("JME only supports linear
            // interpolation"), thousands of lines for some files. The clips still load; silence it.
            java.util.logging.Logger gltfLog = java.util.logging.Logger.getLogger("com.jme3.scene.plugins.gltf");
            java.util.logging.Level previous = gltfLog.getLevel();
            gltfLog.setLevel(java.util.logging.Level.SEVERE);
            try {
                model = assetManager.loadModel(spec.modelPath);
            } catch (Throwable t) {
                System.err.println("[PlayerMesh] Could not load " + spec.modelPath + ": " + t);
            } finally {
                gltfLog.setLevel(previous);
            }
        }

        if (model != null) {
            setupModel(model, assetManager, shirtColor, pantsColor);
            modelLoaded = true;
        } else {
            buildFallback(assetManager, shirtColor != null ? shirtColor : new ColorRGBA(0.18f, 0.42f, 0.68f, 1f));
            System.err.println("[PlayerMesh] Using fallback capsule; the character model is missing.");
        }
    }

    private void setupModel(Spatial model, AssetManager assetManager, ColorRGBA shirtColor, ColorRGBA pantsColor) {
        model.setName("Character_" + spec.id);

        // Locate the animation composer and skinning control anywhere in the loaded tree
        model.depthFirstTraversal(sp -> {
            AnimComposer c = sp.getControl(AnimComposer.class);
            if (c != null) composer = c;
            SkinningControl sc = sp.getControl(SkinningControl.class);
            if (sc != null) sc.setHardwareSkinningPreferred(true);
        });

        // Replace PBR materials with Phong, one Phong material per source material so shared
        // materials stay shared.
        Map<Material, Material> converted = new HashMap<>();
        model.depthFirstTraversal(sp -> {
            if (!(sp instanceof Geometry)) return;
            Geometry g = (Geometry) sp;
            Material old = g.getMaterial();
            Material phong = converted.get(old);
            if (phong == null) {
                String name = old != null && old.getName() != null ? old.getName() : "";
                ColorRGBA base = readBaseColor(old);
                if (spec.shirtMaterial != null && shirtColor != null && name.equals(spec.shirtMaterial)) {
                    base = shirtColor;
                } else if (spec.pantsMaterial != null && pantsColor != null && name.equals(spec.pantsMaterial)) {
                    base = pantsColor;
                }
                boolean vertexColors = g.getMesh().getBuffer(VertexBuffer.Type.Color) != null;
                phong = createLitMaterial(assetManager, base, 10f, vertexColors);
                phong.setName(name);
                converted.put(old, phong);
            }
            g.setMaterial(phong);
            g.setShadowMode(com.jme3.renderer.queue.RenderQueue.ShadowMode.CastAndReceive);
        });

        // Normalise height to TARGET_HEIGHT and put the feet on y = 0
        model.updateModelBound();
        model.updateGeometricState();
        if (model.getWorldBound() instanceof BoundingBox) {
            BoundingBox bb = (BoundingBox) model.getWorldBound();
            float height = bb.getYExtent() * 2f;
            if (height > 0.01f) {
                float scale = TARGET_HEIGHT / height;
                model.setLocalScale(scale);
                float minY = (bb.getCenter().y - bb.getYExtent()) * scale;
                model.setLocalTranslation(0f, -minY, 0f);
                System.out.println("[PlayerMesh] " + spec.id + ": model height " + String.format("%.3f", height)
                    + " units, scale " + String.format("%.3f", scale) + ", foot offset " + String.format("%.3f", -minY)
                    + ", materials " + converted.size());
            }
        }

        rootNode.attachChild(model);

        if (composer != null) {
            prepareAction(spec.clipIdle);
            prepareAction(spec.clipIdleTalk);
            prepareAction(spec.clipWalk);
            prepareAction(spec.clipJog);
            prepareAction(spec.clipSprint);
            prepareAction(spec.clipCrouchIdle);
            prepareAction(spec.clipCrouchMove);
            prepareAction(spec.clipJump);
            System.out.println("[PlayerMesh] " + spec.id + ": clips available " + composer.getAnimClipsNames().size()
                + ", mapped " + actions.keySet());
            play(spec.clipIdle);
        } else {
            System.err.println("[PlayerMesh] No AnimComposer found in " + spec.modelPath + "; character will not animate.");
        }
    }

    private static ColorRGBA readBaseColor(Material m) {
        if (m != null) {
            MatParam p = m.getParam("BaseColor");
            if (p != null && p.getValue() instanceof ColorRGBA) {
                return ((ColorRGBA) p.getValue()).clone();
            }
            p = m.getParam("Diffuse");
            if (p != null && p.getValue() instanceof ColorRGBA) {
                return ((ColorRGBA) p.getValue()).clone();
            }
        }
        return new ColorRGBA(0.7f, 0.7f, 0.7f, 1f);
    }

    private void prepareAction(String clip) {
        if (clip == null || composer == null || !composer.hasAnimClip(clip) || actions.containsKey(clip)) return;
        Action a = composer.action(clip);
        a.setSpeed(1f);
        actions.put(clip, a);
    }

    private void play(String clip) {
        if (composer == null) return;
        if (clip == null || !actions.containsKey(clip)) {
            clip = spec.clipIdle;
            if (clip == null || !actions.containsKey(clip)) return;
        }
        if (clip.equals(currentClip)) return;
        composer.setCurrentAction(clip);
        currentClip = clip;
    }

    private void setClipSpeed(String clip, float speed) {
        Action a = clip != null ? actions.get(clip) : null;
        if (a != null) a.setSpeed(Math.max(0.6f, Math.min(1.9f, speed)));
    }

    private void buildFallback(AssetManager assetManager, ColorRGBA color) {
        Cylinder body = new Cylinder(12, 16, 0.22f, 1.6f, true);
        Geometry g = new Geometry("FallbackBody", body);
        g.setMaterial(createLitMaterial(assetManager, color, 10f, false));
        g.rotate(com.jme3.math.FastMath.HALF_PI, 0f, 0f);
        g.setLocalTranslation(0f, 0.8f, 0f);
        rootNode.attachChild(g);
    }

    private Material createLitMaterial(AssetManager assetManager, ColorRGBA color, float shininess, boolean vertexColors) {
        Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        mat.setBoolean("UseMaterialColors", true);
        mat.setColor("Ambient", color.mult(0.9f));
        mat.setColor("Diffuse", color);
        mat.setColor("Specular", ColorRGBA.White.mult(0.12f));
        mat.setFloat("Shininess", shininess);
        if (vertexColors) {
            mat.setBoolean("UseVertexColor", true);
        }
        return mat;
    }

    /**
     * Selects the locomotion clip from the controller state.
     *
     * @param speed        horizontal speed in m/s (0 when standing)
     * @param stridePhase  procedural stride phase, kept for API compatibility (unused with skinned clips)
     * @param idleTime     seconds since spawn, kept for API compatibility
     * @param isCrouching  crouch state
     */
    public void animate(float speed, float stridePhase, float idleTime, boolean isCrouching) {
        if (composer == null) return;

        boolean moving = speed > 0.2f;
        String clip;
        if (airborne && spec.clipJump != null && actions.containsKey(spec.clipJump)) {
            clip = spec.clipJump;
        } else if (airborne && currentClip != null) {
            return; // no jump clip for this model: keep the clip that was playing at take-off
        } else if (isCrouching) {
            clip = moving ? spec.clipCrouchMove : spec.clipCrouchIdle;
            if (moving) setClipSpeed(spec.clipCrouchMove, speed / spec.crouchClipSpeed);
        } else if (speed > 6f) {
            clip = spec.clipSprint;
            setClipSpeed(spec.clipSprint, speed / spec.sprintClipSpeed);
        } else if (speed > 2.6f) {
            clip = spec.clipJog;
            setClipSpeed(spec.clipJog, speed / spec.jogClipSpeed);
        } else if (moving) {
            clip = spec.clipWalk;
            setClipSpeed(spec.clipWalk, speed / spec.walkClipSpeed);
        } else {
            clip = talking ? spec.clipIdleTalk : spec.clipIdle;
        }
        play(clip);
    }

    /** Set by the controller while the character is off the ground. */
    public void setAirborne(boolean airborne) {
        this.airborne = airborne;
    }

    /** When true and standing still, the talking idle is used instead of the plain idle. */
    public void setTalking(boolean talking) {
        this.talking = talking;
    }

    public boolean isModelLoaded() {
        return modelLoaded;
    }

    public CharacterSpec getSpec() {
        return spec;
    }

    public String getCurrentClip() {
        return currentClip;
    }

    /** Current playback time of the active clip in seconds, or -1 when no composer is present. */
    public double getAnimationTime() {
        return composer != null ? composer.getTime() : -1.0;
    }

    public Node getRootNode() {
        return rootNode;
    }
}
