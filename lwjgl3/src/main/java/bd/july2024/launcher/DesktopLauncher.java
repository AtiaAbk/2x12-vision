package bd.july2024.launcher;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import bd.july2024.ProjectJulyGame;

/**
 * Desktop (LWJGL3) Launcher for Project July 2024.
 * Configured for high-fidelity rendering on macOS Apple Silicon and PC displays.
 */
public class DesktopLauncher {

    public static void main(String[] args) {
        if (StartupHelper.startNewJvmIfRequired()) {
            return;
        }

        createApplication();
    }

    private static Lwjgl3Application createApplication() {
        return new Lwjgl3Application(new ProjectJulyGame(), getDefaultConfiguration());
    }

    private static Lwjgl3ApplicationConfiguration getDefaultConfiguration() {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();

        config.setTitle("Project July 2024");
        config.setHdpiMode(com.badlogic.gdx.graphics.glutils.HdpiMode.Pixels);

        // Window mode
        int customW = Integer.getInteger("bd.july2024.width", 0);
        int customH = Integer.getInteger("bd.july2024.height", 0);

        if (customW > 0 && customH > 0) {
            config.setWindowedMode(customW, customH);
        } else if ("true".equalsIgnoreCase(System.getProperty("bd.july2024.fullscreen"))) {
            try {
                config.setFullscreenMode(Lwjgl3ApplicationConfiguration.getDisplayMode());
            } catch (Throwable t) {
                config.setWindowedMode(1440, 900);
            }
        } else {
            // High-resolution default windowed mode
            config.setWindowedMode(1440, 900);
        }

        config.setResizable(true);
        config.useVsync(true);

        // 24-bit depth buffer and anti-aliasing (MSAA)
        config.setBackBufferConfig(8, 8, 8, 8, 24, 0, 4);

        return config;
    }
}
