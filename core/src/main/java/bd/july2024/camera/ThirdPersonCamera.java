package bd.july2024.camera;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

/**
 * Cinematic third-person spring-arm orbit camera for Project July 2024.
 * Smoothly follows Arif across the Dhaka University campus with mouse orbit damping.
 */
public class ThirdPersonCamera {

    private final PerspectiveCamera camera;

    private float distance = 4.2f;
    private float targetDistance = 4.2f;
    private float yaw = 0f;    // Horizontal orbit angle (deg)
    private float pitch = -8f; // Vertical orbit angle (deg, looking slightly down)

    private final Vector3 targetPos = new Vector3();
    private final Vector3 currentLookAt = new Vector3();
    private final Vector3 camPos = new Vector3();
    private java.util.List<float[]> colliders;

    public ThirdPersonCamera(int width, int height) {
        camera = new PerspectiveCamera(67, width > 0 ? width : 1280, height > 0 ? height : 720);
        camera.near = 0.15f;
        camera.far = 350.0f;
    }

    public void update(float delta, Vector3 playerPos) {
        if (delta > 0.1f) delta = 0.1f;

        if (playerPos != null) {
            targetPos.set(playerPos.x, playerPos.y + 1.45f, playerPos.z);
        }

        // Smooth look-at tracking
        currentLookAt.lerp(targetPos, Math.min(1f, 15f * delta));

        // Smooth distance zoom
        distance += (targetDistance - distance) * Math.min(1f, 8f * delta);

        // Calculate spherical camera position
        float pitchRad = pitch * MathUtils.degreesToRadians;
        float yawRad = yaw * MathUtils.degreesToRadians;

        float cosPitch = MathUtils.cos(pitchRad);
        float sinPitch = MathUtils.sin(pitchRad);
        float cosYaw = MathUtils.cos(yawRad);
        float sinYaw = MathUtils.sin(yawRad);

        // Camera offset behind target
        float offsetX = distance * cosPitch * sinYaw;
        float offsetY = -distance * sinPitch;
        float offsetZ = -distance * cosPitch * cosYaw;

        float actualDistance = distance;
        if (colliders != null && !colliders.isEmpty()) {
            float dirX = offsetX / distance;
            float dirY = offsetY / distance;
            float dirZ = offsetZ / distance;

            for (float[] c : colliders) {
                float hitDist = rayBoxIntersect(currentLookAt.x, currentLookAt.y, currentLookAt.z, dirX, dirY, dirZ, c);
                if (hitDist > 0f && hitDist < actualDistance) {
                    actualDistance = Math.max(1.2f, hitDist - 0.25f);
                }
            }
        }

        camPos.set(currentLookAt.x + (offsetX / distance) * actualDistance,
                   currentLookAt.y + (offsetY / distance) * actualDistance,
                   currentLookAt.z + (offsetZ / distance) * actualDistance);

        // Ground avoidance clamp
        if (camPos.y < 0.35f) {
            camPos.y = 0.35f;
        }

        camera.position.set(camPos);
        camera.lookAt(currentLookAt);
        camera.up.set(Vector3.Y);
        camera.update();
    }

    private float rayBoxIntersect(float ox, float oy, float oz, float dx, float dy, float dz, float[] box) {
        float minX = box[0], minY = box[1], minZ = box[2];
        float maxX = box[3], maxY = box[4], maxZ = box[5];

        float tmin = (minX - ox) / (dx != 0f ? dx : 0.00001f);
        float tmax = (maxX - ox) / (dx != 0f ? dx : 0.00001f);
        if (tmin > tmax) { float temp = tmin; tmin = tmax; tmax = temp; }

        float tymin = (minY - oy) / (dy != 0f ? dy : 0.00001f);
        float tymax = (maxY - oy) / (dy != 0f ? dy : 0.00001f);
        if (tymin > tymax) { float temp = tymin; tymin = tymax; tymax = temp; }

        if (tmin > tymax || tymin > tmax) return -1f;
        if (tymin > tmin) tmin = tymin;
        if (tymax < tmax) tmax = tymax;

        float tzmin = (minZ - oz) / (dz != 0f ? dz : 0.00001f);
        float tzmax = (maxZ - oz) / (dz != 0f ? dz : 0.00001f);
        if (tzmin > tzmax) { float temp = tzmin; tzmin = tzmax; tzmax = temp; }

        if (tmin > tzmax || tzmin > tmax) return -1f;
        if (tzmin > tmin) tmin = tzmin;
        if (tzmax < tmax) tmax = tzmax;

        return (tmin > 0f) ? tmin : -1f;
    }

    public void setColliders(java.util.List<float[]> colliders) {
        this.colliders = colliders;
    }

    public void rotate(float deltaYaw, float deltaPitch) {
        yaw = (yaw + deltaYaw) % 360f;
        pitch = MathUtils.clamp(pitch + deltaPitch, -55f, 25f);
    }

    public void resize(int width, int height) {
        if (width > 0 && height > 0) {
            camera.viewportWidth = width;
            camera.viewportHeight = height;
            camera.update();
        }
    }

    public PerspectiveCamera getCamera() {
        return camera;
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public void setAngles(float yaw, float pitch) {
        this.yaw = yaw;
        this.pitch = MathUtils.clamp(pitch, -55f, 25f);
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.targetDistance = MathUtils.clamp(distance, 2.0f, 9.0f);
    }
}
