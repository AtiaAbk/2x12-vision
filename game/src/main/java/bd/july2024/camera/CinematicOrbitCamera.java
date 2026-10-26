package bd.july2024.camera;

import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;

import java.util.ArrayList;
import java.util.List;

/**
 * Cinematic Spring-Arm Third-Person Orbit Camera for Project July 2024.
 * Tracks Arif smoothly with mouse look, pitch/yaw damping, ground avoidance,
 * and ray-box obstacle clipping to avoid penetrating campus structures.
 */
public class CinematicOrbitCamera {

    private final Camera cam;
    private float distance = 4.2f;
    private float targetDistance = 4.2f;
    private float yaw = 0f;    // Degrees
    private float pitch = -8f; // Degrees (looking slightly down)

    private final Vector3f targetPos = new Vector3f();
    private final Vector3f currentLookAt = new Vector3f();
    private final Vector3f camPos = new Vector3f();

    private final List<float[]> colliders = new ArrayList<>();

    public CinematicOrbitCamera(Camera cam) {
        this.cam = cam;
        cam.setFrustumPerspective(65f, (float) cam.getWidth() / (float) cam.getHeight(), 0.15f, 350f);
    }

    public void update(float delta, Vector3f playerPos) {
        if (delta > 0.1f) delta = 0.1f;

        if (playerPos != null) {
            targetPos.set(playerPos.x, playerPos.y + 1.45f, playerPos.z);
        }

        // Smooth look-at tracking
        currentLookAt.interpolateLocal(targetPos, Math.min(1f, 15f * delta));

        // Smooth zoom
        distance += (targetDistance - distance) * Math.min(1f, 8f * delta);

        // Spherical offset
        float pitchRad = pitch * FastMath.DEG_TO_RAD;
        float yawRad = yaw * FastMath.DEG_TO_RAD;

        float cosPitch = FastMath.cos(pitchRad);
        float sinPitch = FastMath.sin(pitchRad);
        float cosYaw = FastMath.cos(yawRad);
        float sinYaw = FastMath.sin(yawRad);

        float offsetX = distance * cosPitch * sinYaw;
        float offsetY = -distance * sinPitch;
        float offsetZ = -distance * cosPitch * cosYaw;

        // Obstacle collision avoidance against campus colliders
        float actualDistance = distance;
        if (!colliders.isEmpty()) {
            float dirX = offsetX / distance;
            float dirY = offsetY / distance;
            float dirZ = offsetZ / distance;

            for (float[] c : colliders) {
                float hit = rayBoxIntersect(currentLookAt.x, currentLookAt.y, currentLookAt.z, dirX, dirY, dirZ, c);
                if (hit > 0f && hit < actualDistance) {
                    actualDistance = Math.max(1.2f, hit - 0.25f);
                }
            }
        }

        camPos.set(
            currentLookAt.x + (offsetX / distance) * actualDistance,
            currentLookAt.y + (offsetY / distance) * actualDistance,
            currentLookAt.z + (offsetZ / distance) * actualDistance
        );

        // Ground avoidance clamp
        if (camPos.y < 0.35f) {
            camPos.y = 0.35f;
        }

        cam.setLocation(camPos);
        cam.lookAt(currentLookAt, Vector3f.UNIT_Y);
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

    public void rotate(float deltaYaw, float deltaPitch) {
        yaw = (yaw + deltaYaw) % 360f;
        pitch = FastMath.clamp(pitch + deltaPitch, -55f, 25f);
    }

    public void setColliders(List<float[]> colliders) {
        this.colliders.clear();
        if (colliders != null) {
            this.colliders.addAll(colliders);
        }
    }

    /** Default follow distance in metres. */
    public static final float DEFAULT_DISTANCE = 4.2f;

    /** Sets the distance the camera eases towards (front end uses a wide orbit, play the default). */
    public void setDistance(float d) { this.targetDistance = d; }

    public float getYaw() { return yaw; }
    public float getPitch() { return pitch; }
    public void setAngles(float yaw, float pitch) {
        this.yaw = yaw;
        this.pitch = FastMath.clamp(pitch, -55f, 25f);
    }
}
