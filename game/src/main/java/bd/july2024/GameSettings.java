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

    private static GameSettings current;

    public float mouseSensitivity = 1.0f;   // 0.3 .. 3.0
    public boolean invertMouseY = false;
    public float masterVolume = 0.8f;       // 0 .. 1
    public String subtitles = "both";       // both | bn | en
    public int windowSizeIndex = 0;
    public boolean fullscreen = false;

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
