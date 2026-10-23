package bd.july2024.character;

import bd.july2024.audio.AudioController;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * Third-person Kinematic Locomotion Controller for Arif.
 * Supports smooth walk, sprint with stamina drain/recovery, jump, crouch,
 * smooth heading turns, footstep audio synchronization, and AABB world collision.
 */
public class PlayerControl {

    private final Node characterNode;
    private final PlayerMesh mesh;
    private final AudioController audio;

    private final Vector3f position = new Vector3f(0f, 0f, 8f);
    private final Vector3f velocity = new Vector3f();
    private float headingDegrees = 180f; // Facing north toward Raju sculpture
    private float targetHeadingDegrees = 180f;

    // Kinematic locomotion parameters
    public static final float WALK_SPEED = 4.2f;   // m/s
    public static final float SPRINT_SPEED = 7.6f; // m/s
    public static final float JUMP_FORCE = 5.8f;   // m/s
    public static final float GRAVITY = -16.0f;    // m/s^2

    // Stamina system
    private float stamina = 100f;
    public static final float STAMINA_DRAIN_RATE = 28f;
    public static final float STAMINA_RECOVERY_RATE = 20f;

    // State flags
    private boolean isGrounded = true;
    private boolean isSprinting = false;
    private boolean isCrouching = false;
    private float verticalVelocity = 0f;
    private float stridePhase = 0f;
    private float idleTime = 0f;

    // Campus obstacle colliders
    private final List<float[]> colliders = new ArrayList<>();

    public PlayerControl(Node characterNode, PlayerMesh mesh, AudioController audio) {
        this.characterNode = characterNode;
        this.mesh = mesh;
        this.audio = audio;
        characterNode.setLocalTranslation(position);
    }

    public void update(float delta, float cameraYawDegrees, boolean forward, boolean backward, boolean left, boolean right, boolean sprint, boolean jump, boolean crouch) {
        if (delta > 0.1f) delta = 0.1f;
        idleTime += delta;

        // Process sprint and stamina
        boolean wantsSprint = sprint && (forward || backward || left || right) && !crouch;
        if (wantsSprint && stamina > 5f) {
            isSprinting = true;
            stamina = Math.max(0f, stamina - STAMINA_DRAIN_RATE * delta);
        } else {
            isSprinting = false;
            stamina = Math.min(100f, stamina + STAMINA_RECOVERY_RATE * delta);
        }
        isCrouching = crouch && isGrounded;

        // Jump
        if (jump && isGrounded && !isCrouching) {
            verticalVelocity = JUMP_FORCE;
            isGrounded = false;
        }

        // Compute input vector
        float moveX = 0f;
        float moveZ = 0f;
        if (forward) moveZ -= 1f;
        if (backward) moveZ += 1f;
        if (left) moveX -= 1f;
        if (right) moveX += 1f;

        float currentSpeed = 0f;
        if (moveX != 0f || moveZ != 0f) {
            float len = (float) Math.sqrt(moveX * moveX + moveZ * moveZ);
            moveX /= len;
            moveZ /= len;

            float speed = isSprinting ? SPRINT_SPEED : (isCrouching ? WALK_SPEED * 0.55f : WALK_SPEED);
            currentSpeed = speed;

            // Rotate movement relative to camera horizontal yaw
            float rad = cameraYawDegrees * FastMath.DEG_TO_RAD;
            float cos = FastMath.cos(rad);
            float sin = FastMath.sin(rad);

            float worldDirX = moveX * cos - moveZ * sin;
            float worldDirZ = moveX * sin + moveZ * cos;

            velocity.x = worldDirX * speed;
            velocity.z = worldDirZ * speed;

            // Target facing heading
            targetHeadingDegrees = FastMath.atan2(worldDirX, worldDirZ) * FastMath.RAD_TO_DEG;

            // Advance stride cycle & play footsteps on footfall
            float prevPhase = stridePhase;
            stridePhase += speed * delta * 2.8f;
            if (isGrounded && (int)(prevPhase / FastMath.PI) != (int)(stridePhase / FastMath.PI)) {
                if (audio != null) audio.playFootstep();
            }
        } else {
            velocity.x *= Math.max(0f, 1f - 12f * delta);
            velocity.z *= Math.max(0f, 1f - 12f * delta);
        }

        // Smooth heading turn
        float diff = (targetHeadingDegrees - headingDegrees) % 360f;
        if (diff > 180f) diff -= 360f;
        if (diff < -180f) diff += 360f;
        headingDegrees += diff * Math.min(1f, 14f * delta);

        // Apply gravity & vertical velocity
        if (!isGrounded) {
            verticalVelocity += GRAVITY * delta;
        }
        position.y += verticalVelocity * delta;
        if (position.y <= 0f) {
            position.y = 0f;
            verticalVelocity = 0f;
            isGrounded = true;
        }

        // Horizontal displacement with collision check
        float nextX = position.x + velocity.x * delta;
        float nextZ = position.z + velocity.z * delta;

        if (!checkCollision(nextX, position.y, position.z)) {
            position.x = nextX;
        } else {
            velocity.x = 0f;
        }

        if (!checkCollision(position.x, position.y, nextZ)) {
            position.z = nextZ;
        } else {
            velocity.z = 0f;
        }

        // Apply transform to jME Node
        characterNode.setLocalTranslation(position);
        characterNode.setLocalRotation(new com.jme3.math.Quaternion().fromAngleAxis(headingDegrees * FastMath.DEG_TO_RAD, Vector3f.UNIT_Y));

        // Animate procedural mesh limbs
        if (mesh != null) {
            mesh.animate(currentSpeed, stridePhase, idleTime, isCrouching);
        }
    }

    private boolean checkCollision(float px, float py, float pz) {
        float r = 0.35f;
        for (float[] c : colliders) {
            // Box: {minX, minY, minZ, maxX, maxY, maxZ}
            if (px + r > c[0] && px - r < c[3] &&
                pz + r > c[2] && pz - r < c[5] &&
                py + 1.8f > c[1] && py < c[4]) {
                return true;
            }
        }
        return false;
    }

    public void setPosition(float x, float y, float z) {
        position.set(x, y, z);
        characterNode.setLocalTranslation(position);
    }

    public void setHeadingDegrees(float deg) {
        headingDegrees = deg;
        targetHeadingDegrees = deg;
    }

    public void setColliders(List<float[]> colliders) {
        this.colliders.clear();
        if (colliders != null) {
            this.colliders.addAll(colliders);
        }
    }

    public Vector3f getPosition() { return position; }
    public float getHeadingDegrees() { return headingDegrees; }
    public float getStamina() { return stamina; }
    public boolean isSprinting() { return isSprinting; }
    public boolean isCrouching() { return isCrouching; }
}
