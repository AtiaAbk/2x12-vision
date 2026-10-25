package bd.july2024.character;

import com.jme3.anim.AnimComposer;
import com.jme3.anim.SkinningControl;
import com.jme3.anim.tween.action.Action;
import com.jme3.asset.AssetManager;
import com.jme3.bounding.BoundingBox;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Cylinder;

import java.util.HashMap;
import java.util.Map;

/**
 * Rigged, skinned humanoid character for Arif and the companions.
 *
 * Model: Quaternius "Universal Animation Library" (Standard, free tier), CC0 1.0.
 * Source and licence are recorded in assets/Models/Characters/UAL/ and docs/ASSET_REGISTER.md.
 * The mesh is a neutral mannequin: human proportions and a full locomotion animation set,
 * but no face, hair or clothing. It replaces the earlier box-and-sphere placeholder and is
 * itself an interim character until a dressed student model on the same rig is sourced.
 *
 * Public surface is unchanged from the procedural version so PlayerControl, CompanionNPC and
 * ProjectJulyApp keep working: constructor(assetManager, shirtColor, pantsColor),
 * animate(speed, stridePhase, idleTime, isCrouching), getRootNode().
 */
public class PlayerMesh {

    public static final String MODEL_PATH = "Models/Characters/UAL/AnimationLibrary_Godot_Standard.gltf";

    /** Standing height the model is scaled to, in metres. */
    public static final float TARGET_HEIGHT = 1.72f;

    // Clip names as exported in the glTF
    public static final String CLIP_IDLE = "Idle_Loop";
    public static final String CLIP_IDLE_TALK = "Idle_Talking_Loop";
    public static final String CLIP_WALK = "Walk_Loop";
    public static final String CLIP_JOG = "Jog_Fwd_Loop";
    public static final String CLIP_SPRINT = "Sprint_Loop";
    public static final String CLIP_CROUCH_IDLE = "Crouch_Idle_Loop";
    public static final String CLIP_CROUCH_MOVE = "Crouch_Fwd_Loop";
    public static final String CLIP_JUMP = "Jump_Loop";

    /**
     * Approximate forward speed (m/s) each locomotion clip was authored for. Used to scale clip
     * playback so feet do not visibly slide at the game's movement speeds. Estimated by eye from
     * the clips; not a value published by the author.
     */
    private static final float WALK_CLIP_SPEED = 1.6f;
    private static final float JOG_CLIP_SPEED = 3.4f;
    private static final float SPRINT_CLIP_SPEED = 6.5f;
    private static final float CROUCH_CLIP_SPEED = 1.3f;

    private final Node rootNode;
    private AnimComposer composer;
    private final Map<String, Action> actions = new HashMap<>();
    private String currentClip = null;
    private boolean airborne = false;
    private boolean talking = false;
    private boolean modelLoaded = false;

    public PlayerMesh(AssetManager assetManager, ColorRGBA shirtColor, ColorRGBA pantsColor) {
        rootNode = new Node("StudentMeshRoot");
        ColorRGBA shirt = shirtColor != null ? shirtColor : new ColorRGBA(0.18f, 0.42f, 0.68f, 1f);
        ColorRGBA pants = pantsColor != null ? pantsColor : new ColorRGBA(0.12f, 0.18f, 0.28f, 1f);

        Spatial model = null;
        if (assetManager != null) {
            // The glTF loader logs one warning per cubic-spline sampler ("JME only supports linear
            // interpolation"), which is 3,400 lines for this file. The clips still load; silence it.
            java.util.logging.Logger gltfLog = java.util.logging.Logger.getLogger("com.jme3.scene.plugins.gltf");
            java.util.logging.Level previous = gltfLog.getLevel();
            gltfLog.setLevel(java.util.logging.Level.SEVERE);
            try {
                model = assetManager.loadModel(MODEL_PATH);
            } catch (Throwable t) {
                System.err.println("[PlayerMesh] Could not load " + MODEL_PATH + ": " + t);
            } finally {
                gltfLog.setLevel(previous);
            }
        }

        if (model != null) {
            setupModel(model, assetManager, shirt, pants);
            modelLoaded = true;
        } else {
            buildFallback(assetManager, shirt);
            System.err.println("[PlayerMesh] Using fallback capsule; the character model is missing.");
        }
    }

    private void setupModel(Spatial model, AssetManager assetManager, ColorRGBA shirt, ColorRGBA pants) {
        model.setName("UAL_Mannequin");

        // Locate the animation composer and skinning control anywhere in the loaded tree
        model.depthFirstTraversal(sp -> {
            AnimComposer c = sp.getControl(AnimComposer.class);
            if (c != null) composer = c;
            SkinningControl sc = sp.getControl(SkinningControl.class);
            if (sc != null) sc.setHardwareSkinningPreferred(true);
        });

        // Replace the glTF PBR materials with the scene's Phong materials so the character is lit
        // like the rest of the campus (no light probe exists yet). M_Joints is the darker accent
        // material on the mannequin; everything else is the main body.
        Material bodyMat = createLitMaterial(assetManager, shirt, 12f);
        Material jointMat = createLitMaterial(assetManager, pants, 10f);
        model.depthFirstTraversal(sp -> {
            if (sp instanceof Geometry) {
                Geometry g = (Geometry) sp;
                Material old = g.getMaterial();
                String name = old != null && old.getName() != null ? old.getName() : "";
                g.setMaterial(name.contains("Joint") ? jointMat : bodyMat);
                g.setShadowMode(com.jme3.renderer.queue.RenderQueue.ShadowMode.CastAndReceive);
            }
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
                System.out.println("[PlayerMesh] Model height " + String.format("%.3f", height)
                    + " units, scale " + String.format("%.3f", scale) + ", foot offset " + String.format("%.3f", -minY));
            }
        }

        rootNode.attachChild(model);

        if (composer != null) {
            System.out.println("[PlayerMesh] Animation clips: " + composer.getAnimClipsNames());
            prepareAction(CLIP_IDLE, 1f);
            prepareAction(CLIP_IDLE_TALK, 1f);
            prepareAction(CLIP_WALK, 1f);
            prepareAction(CLIP_JOG, 1f);
            prepareAction(CLIP_SPRINT, 1f);
            prepareAction(CLIP_CROUCH_IDLE, 1f);
            prepareAction(CLIP_CROUCH_MOVE, 1f);
            prepareAction(CLIP_JUMP, 1f);
            play(CLIP_IDLE);
        } else {
            System.err.println("[PlayerMesh] No AnimComposer found in " + MODEL_PATH + "; character will not animate.");
        }
    }

    private void prepareAction(String clip, float speed) {
        if (composer == null || !composer.hasAnimClip(clip)) return;
        Action a = composer.action(clip);
        a.setSpeed(speed);
        actions.put(clip, a);
    }

    private void play(String clip) {
        if (composer == null) return;
        if (!actions.containsKey(clip)) {
            clip = CLIP_IDLE;
            if (!actions.containsKey(clip)) return;
        }
        if (clip.equals(currentClip)) return;
        composer.setCurrentAction(clip);
        currentClip = clip;
    }

    private void setClipSpeed(String clip, float speed) {
        Action a = actions.get(clip);
        if (a != null) a.setSpeed(Math.max(0.6f, Math.min(1.6f, speed)));
    }

    private void buildFallback(AssetManager assetManager, ColorRGBA color) {
        Cylinder body = new Cylinder(12, 16, 0.22f, 1.6f, true);
        Geometry g = new Geometry("FallbackBody", body);
        g.setMaterial(createLitMaterial(assetManager, color, 10f));
        g.rotate(com.jme3.math.FastMath.HALF_PI, 0f, 0f);
        g.setLocalTranslation(0f, 0.8f, 0f);
        rootNode.attachChild(g);
    }

    private Material createLitMaterial(AssetManager assetManager, ColorRGBA color, float shininess) {
        Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        mat.setBoolean("UseMaterialColors", true);
        mat.setColor("Ambient", color.mult(0.9f));
        mat.setColor("Diffuse", color);
        mat.setColor("Specular", ColorRGBA.White.mult(0.15f));
        mat.setFloat("Shininess", shininess);
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
        if (airborne) {
            clip = CLIP_JUMP;
        } else if (isCrouching) {
            clip = moving ? CLIP_CROUCH_MOVE : CLIP_CROUCH_IDLE;
            if (moving) setClipSpeed(CLIP_CROUCH_MOVE, speed / CROUCH_CLIP_SPEED);
        } else if (speed > 6f) {
            clip = CLIP_SPRINT;
            setClipSpeed(CLIP_SPRINT, speed / SPRINT_CLIP_SPEED);
        } else if (speed > 2.6f) {
            clip = CLIP_JOG;
            setClipSpeed(CLIP_JOG, speed / JOG_CLIP_SPEED);
        } else if (moving) {
            clip = CLIP_WALK;
            setClipSpeed(CLIP_WALK, speed / WALK_CLIP_SPEED);
        } else {
            clip = talking ? CLIP_IDLE_TALK : CLIP_IDLE;
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
