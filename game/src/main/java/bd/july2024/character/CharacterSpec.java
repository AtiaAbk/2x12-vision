package bd.july2024.character;

/**
 * Describes a loadable character model: its glTF path, the clip names used for each locomotion
 * state, the approximate forward speed each locomotion clip was authored for (so playback can be
 * scaled to the game's movement speed), and which material names represent the shirt and the
 * trousers so a per-character colour can be applied.
 *
 * All presets are CC0 assets by Quaternius; see docs/ASSET_REGISTER.md.
 * A null clip name means the model has no clip for that state; PlayerMesh then falls back.
 */
public final class CharacterSpec {

    public final String id;
    public final String modelPath;
    public final String clipIdle;
    public final String clipIdleTalk;
    public final String clipWalk;
    public final String clipJog;
    public final String clipSprint;
    public final String clipCrouchIdle;
    public final String clipCrouchMove;
    public final String clipJump;
    public final float walkClipSpeed;
    public final float jogClipSpeed;
    public final float sprintClipSpeed;
    public final float crouchClipSpeed;
    /** Material name (as exported in the glTF) whose colour is the shirt, or null to keep authored colours. */
    public final String shirtMaterial;
    /** Material name whose colour is the trousers, or null to keep authored colours. */
    public final String pantsMaterial;

    private CharacterSpec(String id, String modelPath,
                          String clipIdle, String clipIdleTalk, String clipWalk, String clipJog, String clipSprint,
                          String clipCrouchIdle, String clipCrouchMove, String clipJump,
                          float walkClipSpeed, float jogClipSpeed, float sprintClipSpeed, float crouchClipSpeed,
                          String shirtMaterial, String pantsMaterial) {
        this.id = id;
        this.modelPath = modelPath;
        this.clipIdle = clipIdle;
        this.clipIdleTalk = clipIdleTalk;
        this.clipWalk = clipWalk;
        this.clipJog = clipJog;
        this.clipSprint = clipSprint;
        this.clipCrouchIdle = clipCrouchIdle;
        this.clipCrouchMove = clipCrouchMove;
        this.clipJump = clipJump;
        this.walkClipSpeed = walkClipSpeed;
        this.jogClipSpeed = jogClipSpeed;
        this.sprintClipSpeed = sprintClipSpeed;
        this.crouchClipSpeed = crouchClipSpeed;
        this.shirtMaterial = shirtMaterial;
        this.pantsMaterial = pantsMaterial;
    }

    /**
     * Quaternius Universal Animation Library mannequin (CC0). Full locomotion set including crouch,
     * jump and talking idle, but a faceless, unclothed body. Kept as the animation reference model.
     */
    public static final CharacterSpec MANNEQUIN = new CharacterSpec(
        "MANNEQUIN",
        "Models/Characters/UAL/AnimationLibrary_Godot_Standard.gltf",
        "Idle_Loop", "Idle_Talking_Loop", "Walk_Loop", "Jog_Fwd_Loop", "Sprint_Loop",
        "Crouch_Idle_Loop", "Crouch_Fwd_Loop", "Jump_Loop",
        1.6f, 3.4f, 6.5f, 1.3f,
        "M_Main", "M_Joints");

    /**
     * Quaternius Ultimate Modular Men / Women packs (CC0). Clothed low-poly students with faces and
     * hair and 24 clips each: Idle, Idle_Neutral, Walk, Run and one-shots. No crouch, jump or
     * sprint clip exists, so crouch uses the neutral idle / walk and sprint reuses Run at a higher rate.
     */
    private static CharacterSpec modular(String id, String path, String shirtMaterial, String pantsMaterial) {
        return new CharacterSpec(
            id, path,
            "Idle", "Idle", "Walk", "Run", "Run",
            "Idle_Neutral", "Walk", null,
            1.5f, 4.0f, 4.0f, 1.2f,
            shirtMaterial, pantsMaterial);
    }

    // Material names below were read from each glTF and confirmed against the rendered model on
    // 2026-10-25 (docs/screenshots/character_2026-10-25_clothed_v1.png).

    /** Arif: men's casual short-sleeve shirt ("LightBrown") and jeans ("LightBlue"). */
    public static final CharacterSpec ARIF = modular(
        "ARIF", "Models/Characters/QuaterniusModular/Men/Casual_2.gltf", "LightBrown", "LightBlue");

    /** Rafi: men's hoodie ("Purple") and jeans ("LightBlue"). */
    public static final CharacterSpec RAFI = modular(
        "RAFI", "Models/Characters/QuaterniusModular/Men/Casual_Hoodie.gltf", "Purple", "LightBlue");

    /** Tanvir: men's shirt with jacket; "Suit" covers jacket and trousers, so only one tint applies. */
    public static final CharacterSpec TANVIR = modular(
        "TANVIR", "Models/Characters/QuaterniusModular/Men/Suit.gltf", "Suit", null);

    /** Nusrat: women's top ("White") and trousers ("Orange"). */
    public static final CharacterSpec NUSRAT = modular(
        "NUSRAT", "Models/Characters/QuaterniusModular/Women/Casual.gltf", "White", "Orange");

    @Override
    public String toString() {
        return id + " (" + modelPath + ")";
    }
}
