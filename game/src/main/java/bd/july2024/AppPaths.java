package bd.july2024;

import java.io.File;

/**
 * Locates the game's data folders both when run from the repository ({@code ./gradlew :game:run},
 * working directory = repository root) and when launched from a jpackage image or installer, where
 * the launcher sets {@code jpackage.app-path} and the {@code assets/} folder is copied next to the
 * jars under {@code app/}.
 *
 * Resolution order for the assets folder:
 * <ol>
 *   <li>system property {@code bd.july2024.assets}</li>
 *   <li>{@code assets/} under the working directory, if it holds the known sub-folders</li>
 *   <li>{@code app/assets/} or {@code assets/} next to the jpackage launcher</li>
 *   <li>{@code assets/} relative to the working directory (unchanged legacy behaviour)</li>
 * </ol>
 */
public final class AppPaths {

    private static File assetsDir;
    private static Boolean packaged;

    private AppPaths() {
    }

    /** True when started from a jpackage launcher. */
    public static synchronized boolean isPackaged() {
        if (packaged == null) {
            String appPath = System.getProperty("jpackage.app-path");
            packaged = appPath != null && !appPath.isEmpty();
        }
        return packaged;
    }

    /** The assets folder; never null, but it may not exist (the caller reports that). */
    public static synchronized File assetsDir() {
        if (assetsDir != null) return assetsDir;
        String override = System.getProperty("bd.july2024.assets");
        if (override != null && !override.isEmpty()) {
            assetsDir = new File(override);
            return assetsDir;
        }
        File cwdAssets = new File("assets");
        if (looksLikeAssets(cwdAssets)) {
            assetsDir = cwdAssets;
            return assetsDir;
        }
        String appPath = System.getProperty("jpackage.app-path");
        if (appPath != null && !appPath.isEmpty()) {
            File launcherDir = new File(appPath).getAbsoluteFile().getParentFile();
            File[] candidates = {
                new File(launcherDir, "app/assets"),
                new File(launcherDir, "assets"),
                // macOS app bundles: Contents/MacOS/<launcher>, Contents/app/assets
                launcherDir.getParentFile() != null ? new File(launcherDir.getParentFile(), "app/assets") : null
            };
            for (File c : candidates) {
                if (c != null && looksLikeAssets(c)) {
                    assetsDir = c;
                    return assetsDir;
                }
            }
        }
        assetsDir = cwdAssets;
        return assetsDir;
    }

    /** A file inside the assets folder, e.g. {@code asset("fonts/NotoSansBengali-Variable.ttf")}. */
    public static File asset(String relativePath) {
        return new File(assetsDir(), relativePath);
    }

    /**
     * Folder for checkpoint saves: {@code save/} in the working directory when run from the
     * repository, otherwise a per-user folder, because an installed program folder may be read-only.
     */
    public static File saveDir() {
        if (!isPackaged()) return new File("save");
        String base = System.getenv("LOCALAPPDATA");
        if (base == null || base.isEmpty()) {
            base = System.getProperty("user.home");
        }
        return new File(new File(base, "ProjectJuly2024"), "save");
    }

    private static boolean looksLikeAssets(File dir) {
        return dir.isDirectory() && (new File(dir, "Models").isDirectory() || new File(dir, "fonts").isDirectory());
    }
}
