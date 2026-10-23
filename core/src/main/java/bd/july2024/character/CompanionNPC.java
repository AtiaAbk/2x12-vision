package bd.july2024.character;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import bd.july2024.dialogue.DialogueSequence;

/**
 * Historical student companion NPC for Project July 2024.
 * Represents student peers (Rafi, Tanvir, Nusrat) positioned at key landmarks.
 */
public class CompanionNPC {

    private final String id;
    private final String name;
    private final String roleTitle;
    private final Vector3 position = new Vector3();
    private float headingDegrees = 0f;
    private float targetHeadingDegrees = 0f;

    private DialogueSequence dialogueSequence;
    private final CharacterPose pose = new CharacterPose();
    private float animTime = 0f;

    private boolean isTalking = false;

    public CompanionNPC(String id, String name, String roleTitle, Vector3 pos, float heading) {
        this.id = id;
        this.name = name;
        this.roleTitle = roleTitle;
        if (pos != null) {
            this.position.set(pos);
        }
        this.headingDegrees = heading;
        this.targetHeadingDegrees = heading;
    }

    public void setDialogueSequence(DialogueSequence seq) {
        this.dialogueSequence = seq;
    }

    public DialogueSequence getDialogueSequence() {
        return dialogueSequence;
    }

    public void update(float delta, Vector3 playerPos) {
        animTime += delta;

        // Turn smoothly toward player if player is nearby or during active conversation
        if (playerPos != null && playerPos.dst(position) <= 4.0f) {
            float dx = playerPos.x - position.x;
            float dz = playerPos.z - position.z;
            targetHeadingDegrees = MathUtils.atan2(dx, dz) * MathUtils.radiansToDegrees;
        }

        // Smooth angle interpolation
        float diff = (targetHeadingDegrees - headingDegrees) % 360f;
        if (diff > 180f) diff -= 360f;
        if (diff < -180f) diff += 360f;
        headingDegrees += diff * Math.min(1f, 6.0f * delta);

        // Procedural idle and speaking animation
        pose.evaluate(0f, 0f, animTime, false);
        if (isTalking) {
            pose.headPitch += MathUtils.sin(animTime * 6f) * 4f;
            pose.rightArmPitch += MathUtils.sin(animTime * 4f) * 12f;
        }
    }

    public void render(ModelBatch batch, Environment env, CharacterModel model) {
        if (model != null) {
            model.render(batch, env, position, headingDegrees, pose);
        }
    }

    public void setTalking(boolean talking) {
        this.isTalking = talking;
    }

    public boolean isTalking() {
        return isTalking;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRoleTitle() {
        return roleTitle;
    }

    public Vector3 getPosition() {
        return position;
    }

    public float getHeadingDegrees() {
        return headingDegrees;
    }
}
