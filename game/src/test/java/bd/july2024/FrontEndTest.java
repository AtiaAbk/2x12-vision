package bd.july2024;

import bd.july2024.level.MissionTracker;
import bd.july2024.level.PlayerInventory;
import bd.july2024.level.SaveManager;
import bd.july2024.level.Slogans;
import com.jme3.math.Vector3f;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class FrontEndTest {

    @Test
    public void settingsRoundTripThroughPropertiesFile(@TempDir Path dir) {
        File f = dir.resolve("settings.properties").toFile();
        GameSettings a = new GameSettings();
        a.mouseSensitivity = 1.7f;
        a.invertMouseY = true;
        a.masterVolume = 0.35f;
        a.subtitles = "en";
        a.windowSizeIndex = 2;
        a.fullscreen = true;
        assertTrue(a.save(f));

        GameSettings b = GameSettings.load(f);
        assertEquals(1.7f, b.mouseSensitivity, 0.001f);
        assertTrue(b.invertMouseY);
        assertEquals(0.35f, b.masterVolume, 0.001f);
        assertEquals("en", b.subtitles);
        assertEquals(1920, b.windowWidth());
        assertEquals(1080, b.windowHeight());
        assertTrue(b.fullscreen);
        assertFalse(b.showBengaliSubtitles());
        assertTrue(b.showEnglishSubtitles());
    }

    @Test
    public void settingsDefaultsWhenFileMissingOrCorrupt(@TempDir Path dir) throws Exception {
        GameSettings d = GameSettings.load(dir.resolve("none.properties").toFile());
        assertEquals(1280, d.windowWidth());
        assertEquals("both", d.subtitles);
        File bad = dir.resolve("bad.properties").toFile();
        java.nio.file.Files.writeString(bad.toPath(), "mouseSensitivity=abc\nwindowSizeIndex=99\nsubtitles=klingon\n");
        GameSettings c = GameSettings.load(bad);
        assertEquals(1.0f, c.mouseSensitivity, 0.001f);
        assertEquals(2, c.windowSizeIndex); // clamped to the last size
        assertEquals("both", c.subtitles);
    }

    @Test
    public void level01UsesOnlyQuotaReformSlogans() {
        assertEquals(22, Slogans.all().size());
        assertFalse(Slogans.forLevel01().isEmpty());
        for (Slogans.Slogan s : Slogans.forLevel01()) {
            assertEquals(Slogans.Phase.QUOTA_REFORM, s.phase);
        }
        // The 5 August chant must never be on a Level 01 wall.
        for (Slogans.Slogan s : Slogans.forLevel01()) {
            assertFalse(s.bengali.contains("পালাইছে"));
        }
    }

    @Test
    public void audioFilesForEveryCueExist() {
        File root = new File("..", "assets/audio");
        if (!root.isDirectory()) root = new File("assets/audio");
        String[] names = {"ui_click", "ui_confirm", "ui_back", "ui_notify", "ui_warning", "objective", "bell",
            "footstep_grass_1", "footstep_grass_2", "footstep_grass_3", "footstep_paving_1", "footstep_paving_2", "footstep_paving_3",
            "footstep_asphalt_1", "footstep_asphalt_2", "footstep_asphalt_3", "ambience_campus", "music_title"};
        for (String n : names) {
            File f = new File(root, n + ".wav");
            assertTrue(f.isFile() && f.length() > 1000, f + " missing");
        }
    }

    @Test
    public void menuMouseClickNavigation() {
        com.jme3.scene.Node gui = new com.jme3.scene.Node("gui");
        bd.july2024.ui.BengaliTextRenderer text = new bd.july2024.ui.BengaliTextRenderer(null);
        GameSettings s = new GameSettings();
        boolean[] flags = new boolean[1];
        bd.july2024.ui.MenuSystem.Actions act = new bd.july2024.ui.MenuSystem.Actions() {
            public void newGame() { flags[0] = true; }
            public void continueGame() {}
            public void resume() {}
            public void saveCheckpoint() {}
            public void restartLevel() {}
            public void toMainMenu() {}
            public void quit() {}
            public void settingsChanged() {}
            public boolean hasCheckpoint() { return false; }
            public void playClick() {}
            public void playConfirm() {}
            public void playBack() {}
        };
        bd.july2024.ui.MenuSystem menu = new bd.july2024.ui.MenuSystem(gui, text, 1280, 720, s, act);
        menu.showTitle();
        assertTrue(menu.isOpen());
        // Clicking on Title Screen transitions to Main Menu
        menu.onClick(640, 360);
        assertEquals(bd.july2024.ui.MenuSystem.Screen.MAIN, menu.getScreen());
    }

    @Test
    public void testGraphicsPresetsAndCustomOverrides() {
        GameSettings s = new GameSettings();
        s.applyPreset(GameSettings.PRESET_LOW);
        assertEquals(0, s.shadowQuality, "Low preset disables shadows");
        assertFalse(s.ssao, "Low preset disables SSAO");
        assertFalse(s.fxaa, "Low preset disables FXAA");

        s.applyPreset(GameSettings.PRESET_BALANCED);
        assertEquals(2, s.shadowQuality, "Balanced preset uses medium shadows");
        assertTrue(s.ssao, "Balanced preset enables SSAO");
        assertTrue(s.fxaa, "Balanced preset enables FXAA");

        s.applyPreset(GameSettings.PRESET_PERFORMANCE);
        assertEquals(1, s.shadowQuality);
        assertFalse(s.ssao, "Performance preset disables SSAO");
        assertTrue(s.fxaa, "Performance preset keeps FXAA");

        s.applyPreset(GameSettings.PRESET_HIGH);
        assertEquals(3, s.shadowQuality, "High preset uses maximum shadows");
        assertTrue(s.ssao);
        assertTrue(s.fxaa);
        assertEquals(2, s.viewDistanceIndex);
    }

    @Test
    public void testHardwareProfileDetection() {
        bd.july2024.performance.HardwareProfile hp = bd.july2024.performance.HardwareProfile.get();
        assertNotNull(hp.getOsName());
        assertNotNull(hp.getOsArch());
        assertTrue(hp.getCpuCores() >= 1);
        assertTrue(hp.getMaxHeapMb() > 0);
        assertNotNull(hp.getRecommendedPresetName());
        assertTrue(hp.getSummary().contains("HardwareProfile"));
    }

    @Test
    public void testAdaptivePerformanceControllerStepDownAndRecovery() {
        int[] levels = new int[1];
        bd.july2024.performance.AdaptivePerformanceController controller =
            new bd.july2024.performance.AdaptivePerformanceController((level, desc) -> levels[0] = level);

        controller.setTargetFps(60f);
        assertEquals(0, controller.getCurrentAdaptationLevel());

        // Simulate sustained underperformance: 35ms frames (> 28 FPS drops) for 5 seconds
        for (int i = 0; i < 150; i++) {
            controller.recordFrame(0.035f, true);
        }

        assertTrue(controller.getCurrentAdaptationLevel() > 0, "Adaptive controller should step down under sustained load");
        assertTrue(levels[0] > 0);
        int steppedDownLevel = controller.getCurrentAdaptationLevel();

        // Simulate sustained high headroom: 8ms frames (125 FPS) for 8 seconds (1000 frames)
        for (int i = 0; i < 1000; i++) {
            controller.recordFrame(0.008f, true);
        }

        assertTrue(controller.getCurrentAdaptationLevel() < steppedDownLevel, "Adaptive controller should step back up when headroom is available");
    }

    @Test
    public void testPlayerInventoryConsumptionAndSaveRestoration() {
        bd.july2024.level.PlayerInventory inv = new bd.july2024.level.PlayerInventory();
        assertEquals(2, inv.getWaterCount());
        assertEquals(1, inv.getFoodCount());
        assertEquals(0, inv.getLeafletCount());
        assertFalse(inv.hasLeaflets());

        assertTrue(inv.useWater());
        assertEquals(1, inv.getWaterCount());

        assertTrue(inv.useFood());
        assertEquals(0, inv.getFoodCount());
        assertFalse(inv.useFood()); // empty

        inv.setLeafletCount(50);
        assertTrue(inv.hasLeaflets());
        assertEquals(50, inv.getLeafletCount());

        SaveManager sm = new SaveManager();
        SaveManager.SaveData d = new SaveManager.SaveData(2, new Vector3f(12f, 0f, 34f), 90f, 75f, inv);
        String json = sm.toJson(d);
        SaveManager.SaveData loaded = sm.parseJson(json);

        assertEquals(2, loaded.objectiveIndex);
        assertEquals(1, loaded.water);
        assertEquals(0, loaded.food);
        assertEquals(50, loaded.leaflets);
        assertEquals(1, loaded.firstAid);
        assertEquals(75f, loaded.stamina, 0.01f);
    }

    @Test
    public void testMissionTrackerSequentialObjectives() {
        MissionTracker tracker = new MissionTracker();
        assertEquals(6, tracker.getObjectiveCount());
        assertEquals(0, tracker.getCurrentObjectiveIndex());
        assertFalse(tracker.isAllCompleted());

        assertEquals("OBJ_01_RAFI", tracker.getCurrentObjective().getId());
        assertTrue(tracker.advanceObjective());
        assertEquals(1, tracker.getCurrentObjectiveIndex());
        assertEquals("OBJ_02_LIBRARY", tracker.getCurrentObjective().getId());

        assertTrue(tracker.advanceObjective());
        assertEquals(2, tracker.getCurrentObjectiveIndex());
        assertEquals("OBJ_03_TSC", tracker.getCurrentObjective().getId());

        assertTrue(tracker.advanceObjective());
        assertEquals(3, tracker.getCurrentObjectiveIndex());
        assertEquals("OBJ_04_PLAQUE", tracker.getCurrentObjective().getId());

        assertTrue(tracker.advanceObjective());
        assertEquals(4, tracker.getCurrentObjectiveIndex());
        assertEquals("OBJ_05_RALLY", tracker.getCurrentObjective().getId());

        assertTrue(tracker.advanceObjective());
        assertEquals(5, tracker.getCurrentObjectiveIndex());
        assertEquals("OBJ_06_COMPLETE", tracker.getCurrentObjective().getId());

        assertFalse(tracker.advanceObjective()); // already at end
        assertTrue(tracker.isAllCompleted());
    }
}
