package bd.july2024;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Player-adjustable options, persisted as a properties file next to the checkpoint save
 * ({@code save/settings.properties} when run from the repository, the per-user folder when packaged).
 * Window size and fullscreen are read before the window is created and need a restart to change.
 */
public final class GameSettings {

    public static final int[][] WINDOW_SIZES = {{1280, 720}, {1600, 900}, {1920, 1080}};
    public static final String[] SUBTITLE_MODES = {"both", "bn", "en"};
    public static final String[] PRESET_NAMES = {"Low", "Medium", "High", "Performance", "Balanced", "Custom"};
    public static final int PRESET_LOW = 0;
    public static final int PRESET_MEDIUM = 1;
    public static final int PRESET_HIGH = 2;
    public static final int PRESET_PERFORMANCE = 3;
    public static final int PRESET_BALANCED = 4;
    public static final int PRESET_CUSTOM = 5;

    public static final String[] SHADOW_MODES = {"Off", "Low", "Medium", "High"};
    public static final String[] VIEW_DISTANCES = {"Near (200m)", "Medium (300m)", "Far (400m)", "Ultra (550m)"};
    public static final float[] VIEW_DISTANCE_METRES = {200f, 300f, 400f, 550f};
    public static final String[] VEGETATION_LODS = {"Performance", "Balanced", "High"};
    public static final int[] TARGET_FPS_OPTIONS = {30, 60, 120, 0};

    private static GameSettings current;

    public float mouseSensitivity = 1.0f;   // 0.3 .. 3.0
    public boolean invertMouseY = false;
    public float masterVolume = 0.8f;       // 0 .. 1
    public String subtitles = "both";       // both | bn | en
    public int windowSizeIndex = 0;
    public boolean fullscreen = false;

    // Hardware-Aware Graphics Settings
    public int graphicsPresetIndex = PRESET_BALANCED;
    public boolean adaptivePerformance = true;
    public int targetFps = 60;
    public int shadowQuality = 2;           // 0=Off, 1=Low, 2=Medium, 3=High
    public boolean ssao = true;
    public boolean fxaa = true;
    public int viewDistanceIndex = 1;       // 1 = Medium (300m)
    public int vegetationLodIndex = 1;      // 1 = Balanced
    public boolean vsync = true;

    public void applyPreset(int presetIdx) {
        if (presetIdx < 0 || presetIdx >= PRESET_NAMES.length) return;
        this.graphicsPresetIndex = presetIdx;
        switch (presetIdx) {
            case PRESET_LOW:
                shadowQuality = 0;
                ssao = false;
                fxaa = false;
                viewDistanceIndex = 0;
                vegetationLodIndex = 0;
                targetFps = 60;
                vsync = true;
                break;
            case PRESET_PERFORMANCE:
                shadowQuality = 1;
                ssao = false;
                fxaa = true;
                viewDistanceIndex = 0;
                vegetationLodIndex = 0;
                targetFps = 60;
                vsync = true;
                break;
            case PRESET_MEDIUM:
                shadowQuality = 1;
                ssao = true;
                fxaa = true;
                viewDistanceIndex = 1;
                vegetationLodIndex = 1;
                targetFps = 60;
                vsync = true;
                break;
            case PRESET_BALANCED:
                shadowQuality = 2;
                ssao = true;
                fxaa = true;
                viewDistanceIndex = 1;
                vegetationLodIndex = 1;
                targetFps = 60;
                vsync = true;
                break;
            case PRESET_HIGH:
                shadowQuality = 3;
                ssao = true;
                fxaa = true;
                viewDistanceIndex = 2;
                vegetationLodIndex = 2;
                targetFps = 60;
                vsync = true;
                break;
            case PRESET_CUSTOM:
            default:
                break;
        }
    }

    public void restoreDefaults() {
        mouseSensitivity = 1.0f;
        invertMouseY = false;
        masterVolume = 0.8f;
        subtitles = "both";
        windowSizeIndex = 0;
        fullscreen = false;
        adaptivePerformance = true;
        applyPreset(PRESET_BALANCED);
    }

    public static synchronized GameSettings get() {
        if (current == null) {
            current = load(settingsFile());
        }
        return current;
    }

    public static File settingsFile() {
        return new File(AppPaths.saveDir(), "settings.properties");
    }

    public static GameSettings load(File file) {
        GameSettings s = new GameSettings();
        if (file == null || !file.isFile()) return s;
        Properties p = new Properties();
        try (FileInputStream in = new FileInputStream(file)) {
            p.load(in);
        } catch (IOException e) {
            System.err.println("[GameSettings] Could not read " + file + ": " + e.getMessage());
            return s;
        }
        s.mouseSensitivity = clamp(parseFloat(p.getProperty("mouseSensitivity"), s.mouseSensitivity), 0.3f, 3.0f);
        s.invertMouseY = Boolean.parseBoolean(p.getProperty("invertMouseY", "false"));
        s.masterVolume = clamp(parseFloat(p.getProperty("masterVolume"), s.masterVolume), 0f, 1f);
        String sub = p.getProperty("subtitles", "both");
        for (String m : SUBTITLE_MODES) if (m.equals(sub)) s.subtitles = m;
        s.windowSizeIndex = Math.max(0, Math.min(WINDOW_SIZES.length - 1, (int) parseFloat(p.getProperty("windowSizeIndex"), 0)));
        s.fullscreen = Boolean.parseBoolean(p.getProperty("fullscreen", "false"));
        s.graphicsPresetIndex = Math.max(0, Math.min(PRESET_NAMES.length - 1, (int) parseFloat(p.getProperty("graphicsPresetIndex"), s.graphicsPresetIndex)));
        s.adaptivePerformance = Boolean.parseBoolean(p.getProperty("adaptivePerformance", "true"));
        s.targetFps = (int) parseFloat(p.getProperty("targetFps"), s.targetFps);
        s.shadowQuality = Math.max(0, Math.min(SHADOW_MODES.length - 1, (int) parseFloat(p.getProperty("shadowQuality"), s.shadowQuality)));
        s.ssao = Boolean.parseBoolean(p.getProperty("ssao", Boolean.toString(s.ssao)));
        s.fxaa = Boolean.parseBoolean(p.getProperty("fxaa", Boolean.toString(s.fxaa)));
        s.viewDistanceIndex = Math.max(0, Math.min(VIEW_DISTANCES.length - 1, (int) parseFloat(p.getProperty("viewDistanceIndex"), s.viewDistanceIndex)));
        s.vegetationLodIndex = Math.max(0, Math.min(VEGETATION_LODS.length - 1, (int) parseFloat(p.getProperty("vegetationLodIndex"), s.vegetationLodIndex)));
        s.vsync = Boolean.parseBoolean(p.getProperty("vsync", "true"));
        return s;
    }

    public boolean save() {
        return save(settingsFile());
    }

    public boolean save(File file) {
        Properties p = new Properties();
        p.setProperty("mouseSensitivity", String.format(java.util.Locale.ROOT, "%.2f", mouseSensitivity));
        p.setProperty("invertMouseY", Boolean.toString(invertMouseY));
        p.setProperty("masterVolume", String.format(java.util.Locale.ROOT, "%.2f", masterVolume));
        p.setProperty("subtitles", subtitles);
        p.setProperty("windowSizeIndex", Integer.toString(windowSizeIndex));
        p.setProperty("fullscreen", Boolean.toString(fullscreen));
        p.setProperty("graphicsPresetIndex", Integer.toString(graphicsPresetIndex));
        p.setProperty("adaptivePerformance", Boolean.toString(adaptivePerformance));
        p.setProperty("targetFps", Integer.toString(targetFps));
        p.setProperty("shadowQuality", Integer.toString(shadowQuality));
        p.setProperty("ssao", Boolean.toString(ssao));
        p.setProperty("fxaa", Boolean.toString(fxaa));
        p.setProperty("viewDistanceIndex", Integer.toString(viewDistanceIndex));
        p.setProperty("vegetationLodIndex", Integer.toString(vegetationLodIndex));
        p.setProperty("vsync", Boolean.toString(vsync));
        try {
            File dir = file.getParentFile();
            if (dir != null) dir.mkdirs();
            try (FileOutputStream out = new FileOutputStream(file)) {
                p.store(out, "Project July 2024 settings");
            }
            return true;
        } catch (IOException e) {
            System.err.println("[GameSettings] Could not write " + file + ": " + e.getMessage());
            return false;
        }
    }

    public int windowWidth() {
        return WINDOW_SIZES[windowSizeIndex][0];
    }

    public int windowHeight() {
        return WINDOW_SIZES[windowSizeIndex][1];
    }

    public boolean showBengaliSubtitles() {
        return !"en".equals(subtitles);
    }

    public boolean showEnglishSubtitles() {
        return !"bn".equals(subtitles);
    }

    private static float parseFloat(String v, float dflt) {
        if (v == null) return dflt;
        try {
            return Float.parseFloat(v.trim());
        } catch (NumberFormatException e) {
            return dflt;
        }
    }

    private static float clamp(float v, float lo, float hi) {
        return Math.max(lo, Math.min(hi, v));
    }
}
