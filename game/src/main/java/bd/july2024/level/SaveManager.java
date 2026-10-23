package bd.july2024.level;

import com.jme3.math.Vector3f;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

/**
 * Checkpoint and Progress Persistence Manager for Project July 2024.
 * Saves and restores campaign progress, current objective index, and player coordinates.
 */
public class SaveManager {

    private final File saveDir;
    private final File saveFile;

    public static class SaveData {
        public int objectiveIndex = 0;
        public float posX = 0f;
        public float posY = 0f;
        public float posZ = 8f;
        public float heading = 0f;
        public float stamina = 100f;
        public long timestamp = System.currentTimeMillis();

        public SaveData() {}

        public SaveData(int objectiveIndex, Vector3f pos, float heading, float stamina) {
            this.objectiveIndex = objectiveIndex;
            if (pos != null) {
                this.posX = pos.x;
                this.posY = pos.y;
                this.posZ = pos.z;
            }
            this.heading = heading;
            this.stamina = stamina;
            this.timestamp = System.currentTimeMillis();
        }
    }

    public SaveManager() {
        this(new File("save"));
    }

    public SaveManager(File saveDirectory) {
        this.saveDir = saveDirectory;
        this.saveFile = new File(saveDir, "checkpoint.json");
    }

    public boolean saveCheckpoint(int objectiveIndex, Vector3f pos, float heading, float stamina) {
        try {
            if (!saveDir.exists()) {
                saveDir.mkdirs();
            }
            SaveData data = new SaveData(objectiveIndex, pos, heading, stamina);
            String json = toJson(data);
            Files.writeString(saveFile.toPath(), json, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            return true;
        } catch (IOException e) {
            System.err.println("[SaveManager] Failed to save checkpoint: " + e.getMessage());
            return false;
        }
    }

    public SaveData loadCheckpoint() {
        if (!saveFile.exists()) {
            return null;
        }
        try {
            String json = Files.readString(saveFile.toPath());
            return parseJson(json);
        } catch (Exception e) {
            System.err.println("[SaveManager] Failed to load checkpoint: " + e.getMessage());
            return null;
        }
    }

    public boolean hasCheckpoint() {
        return saveFile.exists() && saveFile.length() > 0;
    }

    public void clearCheckpoint() {
        if (saveFile.exists()) {
            saveFile.delete();
        }
    }

    public String toJson(SaveData d) {
        return String.format(
            "{\n  \"objectiveIndex\": %d,\n  \"posX\": %.3f,\n  \"posY\": %.3f,\n  \"posZ\": %.3f,\n  \"heading\": %.2f,\n  \"stamina\": %.1f,\n  \"timestamp\": %d\n}",
            d.objectiveIndex, d.posX, d.posY, d.posZ, d.heading, d.stamina, d.timestamp
        );
    }

    public SaveData parseJson(String json) {
        SaveData d = new SaveData();
        for (String line : json.split("[\n,}]")) {
            String trimmed = line.trim();
            if (trimmed.startsWith("\"objectiveIndex\"")) {
                d.objectiveIndex = extractInt(trimmed);
            } else if (trimmed.startsWith("\"posX\"")) {
                d.posX = extractFloat(trimmed);
            } else if (trimmed.startsWith("\"posY\"")) {
                d.posY = extractFloat(trimmed);
            } else if (trimmed.startsWith("\"posZ\"")) {
                d.posZ = extractFloat(trimmed);
            } else if (trimmed.startsWith("\"heading\"")) {
                d.heading = extractFloat(trimmed);
            } else if (trimmed.startsWith("\"stamina\"")) {
                d.stamina = extractFloat(trimmed);
            } else if (trimmed.startsWith("\"timestamp\"")) {
                d.timestamp = extractLong(trimmed);
            }
        }
        return d;
    }

    private int extractInt(String s) {
        return Integer.parseInt(s.split(":")[1].trim());
    }

    private float extractFloat(String s) {
        return Float.parseFloat(s.split(":")[1].trim());
    }

    private long extractLong(String s) {
        return Long.parseLong(s.split(":")[1].trim());
    }

    public File getSaveFile() {
        return saveFile;
    }
}
