package bd.july2024.performance;

/**
 * Adaptive Hardware-Aware Performance Controller for Project July 2024.
 *
 * Monitors real-time rolling frame times and dynamically adjusts rendering load
 * when sustained drops occur below target FPS, with cooldown hysteresis to prevent oscillation.
 */
public class AdaptivePerformanceController {

    public interface AdaptationListener {
        void onAdaptationChanged(int newLevel, String stepDescription);
    }

    private static final int ROLLING_SAMPLE_COUNT = 90;
    private final float[] frameTimeSamples = new float[ROLLING_SAMPLE_COUNT];
    private int sampleIndex = 0;
    private int samplesCollected = 0;

    private int currentAdaptationLevel = 0; // 0 = original preset quality, 5 = maximum reduction
    private float sustainedUnderperfTimer = 0f;
    private float sustainedHeadroomTimer = 0f;

    private float targetFps = 60f;
    private float underperfThresholdMs = 20.0f; // < 50 FPS triggers downscale check
    private float headroomThresholdMs = 13.5f;  // > 74 FPS triggers recovery check
    private float cooldownSeconds = 4.0f;       // minimum seconds between downward adjustments
    private float recoverySeconds = 6.0f;       // minimum seconds of sustained headroom before restoration

    private AdaptationListener listener;

    public AdaptivePerformanceController(AdaptationListener listener) {
        this.listener = listener;
    }

    public void setTargetFps(float fps) {
        if (fps > 0f) {
            this.targetFps = fps;
            float targetMs = 1000f / fps;
            this.underperfThresholdMs = targetMs * 1.22f; // e.g. 20.3ms for 60fps
            this.headroomThresholdMs = targetMs * 0.82f;  // e.g. 13.6ms for 60fps
        }
    }

    public void recordFrame(float tpf, boolean adaptiveEnabled) {
        float frameTimeMs = tpf * 1000f;
        frameTimeSamples[sampleIndex] = frameTimeMs;
        sampleIndex = (sampleIndex + 1) % ROLLING_SAMPLE_COUNT;
        if (samplesCollected < ROLLING_SAMPLE_COUNT) samplesCollected++;

        if (!adaptiveEnabled || samplesCollected < 30) {
            sustainedUnderperfTimer = 0f;
            sustainedHeadroomTimer = 0f;
            return;
        }

        float avgMs = getRollingAverageFrameTimeMs();

        // 1. Check for sustained underperformance
        if (avgMs > underperfThresholdMs) {
            sustainedUnderperfTimer += tpf;
            sustainedHeadroomTimer = 0f;

            if (sustainedUnderperfTimer >= cooldownSeconds) {
                sustainedUnderperfTimer = 0f;
                stepDown(avgMs);
            }
        }
        // 2. Check for sustained headroom to restore quality
        else if (avgMs < headroomThresholdMs) {
            sustainedHeadroomTimer += tpf;
            sustainedUnderperfTimer = 0f;

            if (sustainedHeadroomTimer >= recoverySeconds) {
                sustainedHeadroomTimer = 0f;
                stepUp(avgMs);
            }
        } else {
            // Stable performance within acceptable range
            sustainedUnderperfTimer = Math.max(0f, sustainedUnderperfTimer - tpf * 0.5f);
            sustainedHeadroomTimer = Math.max(0f, sustainedHeadroomTimer - tpf * 0.5f);
        }
    }

    private void stepDown(float currentAvgMs) {
        if (currentAdaptationLevel < 5) {
            currentAdaptationLevel++;
            String desc = describeLevel(currentAdaptationLevel);
            System.out.printf("[AdaptivePerformance] Sustained frame time %.2f ms (> %.1f ms target). Stepping DOWN to Level %d: %s%n",
                currentAvgMs, underperfThresholdMs, currentAdaptationLevel, desc);
            if (listener != null) listener.onAdaptationChanged(currentAdaptationLevel, desc);
        }
    }

    private void stepUp(float currentAvgMs) {
        if (currentAdaptationLevel > 0) {
            currentAdaptationLevel--;
            String desc = describeLevel(currentAdaptationLevel);
            System.out.printf("[AdaptivePerformance] Sustained headroom %.2f ms (< %.1f ms target). Stepping UP to Level %d: %s%n",
                currentAvgMs, headroomThresholdMs, currentAdaptationLevel, desc);
            if (listener != null) listener.onAdaptationChanged(currentAdaptationLevel, desc);
        }
    }

    public void reset() {
        currentAdaptationLevel = 0;
        sustainedUnderperfTimer = 0f;
        sustainedHeadroomTimer = 0f;
        sampleIndex = 0;
        samplesCollected = 0;
    }

    public int getCurrentAdaptationLevel() {
        return currentAdaptationLevel;
    }

    public float getRollingAverageFrameTimeMs() {
        if (samplesCollected == 0) return 16.6f;
        float sum = 0f;
        for (int i = 0; i < samplesCollected; i++) {
            sum += frameTimeSamples[i];
        }
        return sum / samplesCollected;
    }

    public float getRollingAverageFps() {
        float avgMs = getRollingAverageFrameTimeMs();
        return avgMs > 0.001f ? (1000f / avgMs) : 60f;
    }

    public String describeLevel(int level) {
        switch (level) {
            case 0: return "Full Preset Quality (No Adaptation)";
            case 1: return "Reduced Shadow Distance";
            case 2: return "Ambient Occlusion (SSAO) Disabled";
            case 3: return "Aggressive Foliage LOD Distances";
            case 4: return "Reduced View Distance (Frustum Far)";
            case 5: return "Directional Shadows Disabled";
            default: return "Custom Adaptation";
        }
    }
}
