package bd.july2024;

import bd.july2024.level.Slogans;
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
}
