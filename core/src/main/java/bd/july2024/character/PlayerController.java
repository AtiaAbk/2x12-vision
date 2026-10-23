package bd.july2024.character;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

/**
 * Grounded third-person character locomotion controller for Arif in Project July 2024.
 * Features realistic walking, sprinting with stamina management, jumping, and collision damping.
 */
public class PlayerController {

    private final Vector3 position = new Vector3(44f, 0f, 24f);
    private final Vector3 velocity = new Vector3();
    private float verticalVelocity = 0f;
    private boolean isGrounded = true;

    private float headingDegrees = 0f; // 0 deg = South (+Z)
    private float targetHeadingDegrees = 0f;

    // Speeds in m/s
    public static final float WALK_SPEED = 4.2f;
    public static final float SPRINT_SPEED = 7.6f;
    public static final float JUMP_FORCE = 5.8f;
    public static final float GRAVITY = -18.0f;

    // Stamina system (100 max)
    private float stamina = 100f;
    private static final float STAMINA_DRAIN_RATE = 20f;
    private static final float STAMINA_RECOVERY_RATE = 15f;

    private boolean isSprinting = false;
    private boolean isCrouching = false;
    private float stridePhase = 0f;
    private float idleTime = 0f;

    private final CharacterPose pose = new CharacterPose();
    private java.util.List<float[]> colliders = new java.util.ArrayList<>();

    public void setPosition(float x, float y, float z) {
        position.set(x, y, z);
    }

    public void setHeadingDegrees(float deg) {
        this.headingDegrees = deg;
        this.targetHeadingDegrees = deg;
    }

    public void setColliders(java.util.List<float[]> colliders) {
        this.colliders = (colliders != null) ? colliders : new java.util.ArrayList<float[]>();
    }

    public void update(float delta, float cameraYawDegrees, boolean inputEnabled) {
        if (delta > 0.1f) delta = 0.1f;
        idleTime += delta;

        float moveX = 0f;
        float moveZ = 0f;

        if (inputEnabled && Gdx.input != null) {
            if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) moveZ += 1f;
            if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) moveZ -= 1f;
            if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) moveX -= 1f;
            if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) moveX += 1f;

            isCrouching = Gdx.input.isKeyPressed(Input.Keys.C) || Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT);

            // Sprinting logic with stamina
            boolean sprintKey = (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT));
            if (sprintKey && stamina > 5f && (moveX != 0f || moveZ != 0f) && !isCrouching) {
                isSprinting = true;
                stamina = Math.max(0f, stamina - STAMINA_DRAIN_RATE * delta);
            } else {
                isSprinting = false;
                stamina = Math.min(100f, stamina + STAMINA_RECOVERY_RATE * delta);
            }

            // Jump
            if ((Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.isKeyJustPressed(Input.Keys.J)) && isGrounded && !isCrouching) {
                verticalVelocity = JUMP_FORCE;
                isGrounded = false;
            }
        } else {
            stamina = Math.min(100f, stamina + STAMINA_RECOVERY_RATE * delta);
            isSprinting = false;
            isCrouching = false;
        }

        // Compute movement vector relative to camera yaw
        float currentSpeed = 0f;
        if (moveX != 0f || moveZ != 0f) {
            float len = (float) Math.sqrt(moveX * moveX + moveZ * moveZ);
            moveX /= len;
            moveZ /= len;

            float speed = isSprinting ? SPRINT_SPEED : (isCrouching ? WALK_SPEED * 0.55f : WALK_SPEED);
            currentSpeed = speed;

            // Rotate input direction by camera yaw
            float rad = cameraYawDegrees * MathUtils.degreesToRadians;
            float cos = MathUtils.cos(rad);
            float sin = MathUtils.sin(rad);

            float worldDirX = moveX * cos - moveZ * sin;
            float worldDirZ = moveX * sin + moveZ * cos;

            velocity.x = worldDirX * speed;
            velocity.z = worldDirZ * speed;

            // Update target heading
            targetHeadingDegrees = MathUtils.atan2(worldDirX, worldDirZ) * MathUtils.radiansToDegrees;

            // Advance stride cycle & trigger footstep audio on contact
            float prevPhase = stridePhase;
            stridePhase += speed * delta * 2.8f;
            if (isGrounded && (int)(prevPhase / MathUtils.PI) != (int)(stridePhase / MathUtils.PI)) {
                bd.july2024.audio.SoundManager.get().playFootstep();
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

        // Apply gravity & vertical movement
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

        // Evaluate kinematic character pose
        pose.evaluate(currentSpeed, stridePhase, idleTime, isCrouching);
    }

    private boolean checkCollision(float px, float py, float pz) {
        float r = 0.32f; // Character radius
        for (float[] c : colliders) {
            // Collider box format: {minX, minY, minZ, maxX, maxY, maxZ}
            if (px + r > c[0] && px - r < c[3] &&
                pz + r > c[2] && pz - r < c[5] &&
                py + 1.8f > c[1] && py < c[4]) {
                return true;
            }
        }
        return false;
    }

    public Vector3 getPosition() {
        return position;
    }

    public Vector3 getVelocity() {
        return velocity;
    }

    public float getHeadingDegrees() {
        return headingDegrees;
    }

    public CharacterPose getPose() {
        return pose;
    }

    public float getStamina() {
        return stamina;
    }

    public boolean isSprinting() {
        return isSprinting;
    }

    public boolean isCrouching() {
        return isCrouching;
    }
}
