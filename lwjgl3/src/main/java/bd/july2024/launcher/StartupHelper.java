package bd.july2024.launcher;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;

/**
 * Ensures JVM compatibility and handles macOS -XstartOnFirstThread launch if needed.
 */
public class StartupHelper {

    private static final String JVM_RESTARTED_ARG = "jvmIsRestarted";

    public static boolean startNewJvmIfRequired() {
        String osName = System.getProperty("os.name", "").toLowerCase();
        if (!osName.contains("mac")) {
            return false;
        }

        if ("true".equals(System.getProperty(JVM_RESTARTED_ARG))) {
            return false;
        }

        long pid = getProcessId();
        List<String> jvmArgs = ManagementFactory.getRuntimeMXBean().getInputArguments();
        boolean hasStartOnFirstThread = false;
        for (String arg : jvmArgs) {
            if (arg.contains("-XstartOnFirstThread")) {
                hasStartOnFirstThread = true;
                break;
            }
        }

        if (hasStartOnFirstThread) {
            return false;
        }

        // Restart with -XstartOnFirstThread
        String javaHome = System.getProperty("java.home");
        String javaBin = javaHome + File.separator + "bin" + File.separator + "java";
        List<String> command = new ArrayList<>();
        command.add(javaBin);
        command.add("-XstartOnFirstThread");
        command.add("-D" + JVM_RESTARTED_ARG + "=true");
        command.addAll(jvmArgs);
        command.add("-cp");
        command.add(System.getProperty("java.class.path"));
        command.add(DesktopLauncher.class.getName());

        try {
            ProcessBuilder builder = new ProcessBuilder(command);
            builder.inheritIO();
            Process process = builder.start();
            process.waitFor();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static long getProcessId() {
        try {
            String processName = ManagementFactory.getRuntimeMXBean().getName();
            return Long.parseLong(processName.split("@")[0]);
        } catch (Exception e) {
            return -1;
        }
    }
}
