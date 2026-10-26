package bd.july2024.performance;

/**
 * Cross-platform hardware profile detector for Project July 2024.
 * Safely inspects system architecture, CPU cores, JVM memory limits, and GPU info.
 */
public final class HardwareProfile {

    private final String osName;
    private final String osArch;
    private final int cpuCores;
    private final long maxHeapMb;
    private final boolean appleSilicon;
    private String gpuRenderer = "Unknown";
    private String glVersion = "Unknown";

    private static HardwareProfile instance;

    public static synchronized HardwareProfile get() {
        if (instance == null) {
            instance = new HardwareProfile();
        }
        return instance;
    }

    private HardwareProfile() {
        this.osName = System.getProperty("os.name", "Unknown");
        this.osArch = System.getProperty("os.arch", "Unknown");
        this.cpuCores = Runtime.getRuntime().availableProcessors();
        this.maxHeapMb = Runtime.getRuntime().maxMemory() / (1024L * 1024L);
        this.appleSilicon = osName.toLowerCase().contains("mac") && "aarch64".equalsIgnoreCase(osArch);
    }

    public void setGpuInfo(String renderer, String version) {
        if (renderer != null && !renderer.isEmpty()) this.gpuRenderer = renderer;
        if (version != null && !version.isEmpty()) this.glVersion = version;
    }

    public String getOsName() { return osName; }
    public String getOsArch() { return osArch; }
    public int getCpuCores() { return cpuCores; }
    public long getMaxHeapMb() { return maxHeapMb; }
    public boolean isAppleSilicon() { return appleSilicon; }
    public String getGpuRenderer() { return gpuRenderer; }
    public String getGlVersion() { return glVersion; }

    /** Returns the recommended baseline preset based on verified hardware constraints. */
    public String getRecommendedPresetName() {
        if (appleSilicon && cpuCores >= 8 && maxHeapMb >= 1024L) {
            return "BALANCED";
        }
        if (cpuCores <= 4 || maxHeapMb < 1024L) {
            return "PERFORMANCE";
        }
        return "BALANCED";
    }

    public String getSummary() {
        return String.format(
            "HardwareProfile[OS: %s (%s), CPU: %d cores, Max Heap: %d MB, GPU: %s, GL: %s, AppleSilicon: %b]",
            osName, osArch, cpuCores, maxHeapMb, gpuRenderer, glVersion, appleSilicon
        );
    }
}
