package bd.july2024.character;

import com.badlogic.gdx.math.MathUtils;

/**
 * Kinematic character pose representation for student locomotion and gestures in Project July 2024.
 * Generates natural arm swings, leg strides, breathing idle, and head pitch/yaw.
 */
public class CharacterPose {

    public float spinePitch = 0f;
    public float spineYaw = 0f;
    public float headPitch = 0f;
    public float headYaw = 0f;

    public float leftArmPitch = 0f;
    public float rightArmPitch = 0f;
    public float leftElbowPitch = 0f;
    public float rightElbowPitch = 0f;

    public float leftLegPitch = 0f;
    public float rightLegPitch = 0f;
    public float leftKneePitch = 0f;
    public float rightKneePitch = 0f;

    public float rootOffsetY = 0f;

    /**
     * Evaluates procedural kinematics based on movement speed, walk phase, and idle breathing.
     */
    public void evaluate(float speed, float stridePhase, float idleTime, boolean isCrouching) {
        float breath = MathUtils.sin(idleTime * 2.2f) * 1.5f;

        if (speed < 0.2f) {
            // Idle state with gentle breathing and relaxed posture
            spinePitch = isCrouching ? 22f : (3.0f + breath);
            spineYaw = 0f;
            headPitch = isCrouching ? -12f : -breath * 0.5f;
            headYaw = 0f;

            leftArmPitch = isCrouching ? 25f : 6f + breath * 0.8f;
            rightArmPitch = isCrouching ? 25f : 6f + breath * 0.8f;
            leftElbowPitch = isCrouching ? 45f : 12f;
            rightElbowPitch = isCrouching ? 45f : 12f;

            leftLegPitch = isCrouching ? 35f : 0f;
            rightLegPitch = isCrouching ? 35f : 0f;
            leftKneePitch = isCrouching ? 55f : 0f;
            rightKneePitch = isCrouching ? 55f : 0f;

            rootOffsetY = isCrouching ? -0.42f : (MathUtils.sin(idleTime * 2.2f) * 0.015f);
        } else {
            // Locomotion state: alternating stride cycle
            float swingAmp = Math.min(45f, speed * 7.5f);
            float armAmp = swingAmp * 0.75f;

            float legCycle = MathUtils.sin(stridePhase);
            float legCycleCos = MathUtils.cos(stridePhase);

            spinePitch = (speed > 5.5f) ? 14f : 6f;
            spineYaw = -legCycle * 3.5f;
            headPitch = (speed > 5.5f) ? -6f : -2f;
            headYaw = 0f;

            // Opposite arm to leg swing
            leftArmPitch = -legCycle * armAmp;
            rightArmPitch = legCycle * armAmp;
            leftElbowPitch = Math.max(10f, Math.abs(leftArmPitch) * 0.6f);
            rightElbowPitch = Math.max(10f, Math.abs(rightArmPitch) * 0.6f);

            leftLegPitch = legCycle * swingAmp;
            rightLegPitch = -legCycle * swingAmp;

            // Knee bend on recovery phase
            leftKneePitch = (leftLegPitch < 0f) ? Math.abs(leftLegPitch) * 1.3f : 0f;
            rightKneePitch = (rightLegPitch < 0f) ? Math.abs(rightLegPitch) * 1.3f : 0f;

            // Vertical bobbing during gait
            rootOffsetY = Math.abs(legCycleCos) * 0.06f;
            if (isCrouching) rootOffsetY -= 0.38f;
        }
    }
}
