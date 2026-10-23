package bd.july2024;

import bd.july2024.dialogue.DialogueLine;
import bd.july2024.dialogue.DialogueManager;
import bd.july2024.dialogue.HistoricalDialogues;
import bd.july2024.level.MissionObjective;
import bd.july2024.level.MissionTracker;
import bd.july2024.ui.BengaliTextRenderer;
import org.junit.jupiter.api.Test;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ProjectJulyJmeTest {

    @Test
    public void testMissionTrackerProgression() {
        MissionTracker tracker = new MissionTracker();
        assertEquals(0, tracker.getCurrentObjectiveIndex());
        assertEquals("OBJ_01_RAFI", tracker.getCurrentObjective().getId());

        // Advance 1 -> 2
        assertTrue(tracker.advanceObjective());
        assertEquals(1, tracker.getCurrentObjectiveIndex());
        assertEquals("OBJ_02_LIBRARY", tracker.getCurrentObjective().getId());

        // Advance 2 -> 3
        assertTrue(tracker.advanceObjective());
        assertEquals(2, tracker.getCurrentObjectiveIndex());
        assertEquals("OBJ_03_TSC", tracker.getCurrentObjective().getId());

        // Advance 3 -> 4
        assertTrue(tracker.advanceObjective());
        assertEquals(3, tracker.getCurrentObjectiveIndex());
        assertEquals("OBJ_04_PLAQUE", tracker.getCurrentObjective().getId());

        // Advance 4 -> 5
        assertTrue(tracker.advanceObjective());
        assertEquals(4, tracker.getCurrentObjectiveIndex());
        assertEquals("OBJ_05_RALLY", tracker.getCurrentObjective().getId());

        // Advance 5 -> 6
        assertTrue(tracker.advanceObjective());
        assertEquals(5, tracker.getCurrentObjectiveIndex());
        assertEquals("OBJ_06_COMPLETE", tracker.getCurrentObjective().getId());

        // Complete final objective
        tracker.advanceObjective();
        assertTrue(tracker.isAllCompleted());
    }

    @Test
    public void testHistoricalDialoguesIntegrity() {
        List<DialogueLine> rafiLines = HistoricalDialogues.getRafiDialogue();
        assertNotNull(rafiLines);
        assertFalse(rafiLines.isEmpty());
        assertTrue(rafiLines.get(0).getTextBengali().contains("ক্যাম্পাসের"));

        List<DialogueLine> tanvirLines = HistoricalDialogues.getTanvirDialogue();
        assertNotNull(tanvirLines);
        assertFalse(tanvirLines.isEmpty());
        assertTrue(tanvirLines.get(0).getTextBengali().contains("লিফলেট"));

        List<DialogueLine> nusratLines = HistoricalDialogues.getNusratDialogue();
        assertNotNull(nusratLines);
        assertFalse(nusratLines.isEmpty());
        assertTrue(nusratLines.get(0).getTextBengali().contains("লিফলেট"));
    }

    @Test
    public void testDialogueManagerExecution() {
        DialogueManager dm = new DialogueManager();
        assertFalse(dm.isActive());

        boolean[] finished = new boolean[]{false};
        dm.startDialogue(HistoricalDialogues.getMemorialPlaqueDialogue(), () -> finished[0] = true);
        assertTrue(dm.isActive());
        assertEquals("ঐতিহাসিক তথ্যফলক (Memorial Plaque)", dm.getCurrentLine().getSpeaker());

        // Advance line 1
        assertTrue(dm.advanceDialogue());
        assertTrue(dm.isActive());

        // Advance line 2 -> finishes dialogue
        assertFalse(dm.advanceDialogue());
        assertFalse(dm.isActive());
        assertTrue(finished[0]);
    }

    @Test
    public void testBengaliTypographyRendering() {
        BengaliTextRenderer renderer = new BengaliTextRenderer(null);
        String testText = "বাঙালি ছাত্র-জনতার ঐতিহাসিক জুলাই অভ্যুত্থান";
        BufferedImage img = renderer.renderStringToImage(testText, 20f, true, Color.WHITE, new Color(0, 0, 0, 180), 10, 5);

        assertNotNull(img);
        assertTrue(img.getWidth() > 50, "Rendered text width should be greater than 50px");
        assertTrue(img.getHeight() > 15, "Rendered text height should be greater than 15px");
    }
}
