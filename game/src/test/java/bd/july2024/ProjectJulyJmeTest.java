package bd.july2024;

import bd.july2024.character.PlayerControl;
import bd.july2024.dialogue.DialogueLine;
import bd.july2024.dialogue.DialogueManager;
import bd.july2024.dialogue.HistoricalDialogues;
import bd.july2024.level.MissionObjective;
import bd.july2024.level.MissionTracker;
import bd.july2024.level.SaveManager;
import bd.july2024.ui.BengaliTextRenderer;
import bd.july2024.ui.HistoricalDocumentModal;
import com.jme3.math.Vector3f;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Path;
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
        // The plaque belongs to the Raju sculpture, which stood there in July 2024; no commemorative
        // "July Movement" plaque existed in the first days of the movement.
        assertTrue(tracker.getCurrentObjective().getTitleEnglish().contains("Raju"));
        assertFalse(tracker.getCurrentObjective().getTitleEnglish().contains("July Movement"));

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
    public void testMissionTrackerResetAndSetIndex() {
        MissionTracker tracker = new MissionTracker();
        tracker.setCurrentObjectiveIndex(3);
        assertEquals(3, tracker.getCurrentObjectiveIndex());
        assertTrue(tracker.getAllObjectives().get(0).isCompleted());
        assertTrue(tracker.getAllObjectives().get(1).isCompleted());
        assertTrue(tracker.getAllObjectives().get(2).isCompleted());
        assertFalse(tracker.getAllObjectives().get(3).isCompleted());

        tracker.reset();
        assertEquals(0, tracker.getCurrentObjectiveIndex());
        for (MissionObjective obj : tracker.getAllObjectives()) {
            assertFalse(obj.isCompleted());
        }
    }

    @Test
    public void testSaveManagerPersistence(@TempDir Path tempDir) {
        File saveDir = tempDir.resolve("save").toFile();
        SaveManager saveManager = new SaveManager(saveDir);

        assertFalse(saveManager.hasCheckpoint());
        assertNull(saveManager.loadCheckpoint());

        Vector3f pos = new Vector3f(12.5f, 0.5f, -8.2f);
        float heading = 45f;
        float stamina = 85.5f;
        int objectiveIndex = 2;

        assertTrue(saveManager.saveCheckpoint(objectiveIndex, pos, heading, stamina));
        assertTrue(saveManager.hasCheckpoint());

        SaveManager.SaveData data = saveManager.loadCheckpoint();
        assertNotNull(data);
        assertEquals(2, data.objectiveIndex);
        assertEquals(12.5f, data.posX, 0.01f);
        assertEquals(0.5f, data.posY, 0.01f);
        assertEquals(-8.2f, data.posZ, 0.01f);
        assertEquals(45f, data.heading, 0.01f);
        assertEquals(85.5f, data.stamina, 0.01f);

        // Clear checkpoint
        saveManager.clearCheckpoint();
        assertFalse(saveManager.hasCheckpoint());
        assertNull(saveManager.loadCheckpoint());
    }

    @Test
    public void testHistoricalArchivalDocuments() {
        HistoricalDocumentModal.DocumentRecord rajuDoc = HistoricalDialogues.getRajuMemorialDocument();
        assertNotNull(rajuDoc);
        assertTrue(rajuDoc.titleBengali.contains("রাজু"));
        assertTrue(rajuDoc.titleEnglish.contains("RAJU"));
        assertTrue(rajuDoc.textBengali.contains("১৯৯২"));
        assertTrue(rajuDoc.textEnglish.contains("1992"));

        HistoricalDocumentModal.DocumentRecord libDoc = HistoricalDialogues.getLibraryDocument();
        assertNotNull(libDoc);
        assertTrue(libDoc.titleBengali.contains("লাইব্রেরি"));
        assertTrue(libDoc.textBengali.contains("মাজহারুল ইসলাম"));
        assertTrue(libDoc.textEnglish.contains("Muzharul Islam"));
        assertTrue(libDoc.textEnglish.contains("1953"));
        // Level 01 is set in early July: no later-July events or unsourced aid/blackout claims.
        assertTrue(libDoc.date.startsWith("July 1"));
        assertFalse(libDoc.date.contains("18"));
        assertFalse(libDoc.textEnglish.contains("dispatch"));

        HistoricalDocumentModal.DocumentRecord tscDoc = HistoricalDialogues.getTscDocument();
        assertNotNull(tscDoc);
        assertTrue(tscDoc.titleBengali.contains("টিএসসি"));
        assertTrue(tscDoc.textEnglish.contains("Doxiadis"));
        // Level 01 is set in early July: the card must not jump ahead to 5 August.
        assertFalse(tscDoc.date.contains("August"));
        assertFalse(tscDoc.textEnglish.contains("36 July"));
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
        String testText = "বাঙালি ছাত্র-জনতার ঐতিহাসিক জুলাই অভ্যুত্থান\nদ্বিতীয় লাইন (Second Line)";
        BufferedImage img = renderer.renderStringToImage(testText, 20f, true, Color.WHITE, new Color(0, 0, 0, 180), 10, 5);

        assertNotNull(img);
        assertTrue(img.getWidth() > 50, "Rendered text width should be greater than 50px");
        assertTrue(img.getHeight() > 15, "Rendered text height should be greater than 15px");
    }

    @Test
    public void testLevel01MissionCompletionFlow() {
        MissionTracker tracker = new MissionTracker();
        assertEquals(0, tracker.getCurrentObjectiveIndex());
        assertFalse(tracker.isAllCompleted());

        // Step 1: Meet Rafi
        assertEquals("OBJ_01_RAFI", tracker.getCurrentObjective().getId());
        tracker.advanceObjective();

        // Step 2: Collect leaflets from Tanvir
        assertEquals("OBJ_02_LIBRARY", tracker.getCurrentObjective().getId());
        tracker.advanceObjective();

        // Step 3: Hand over leaflets to Nusrat
        assertEquals("OBJ_03_TSC", tracker.getCurrentObjective().getId());
        tracker.advanceObjective();

        // Step 4: Read Raju plaque
        assertEquals("OBJ_04_PLAQUE", tracker.getCurrentObjective().getId());
        tracker.advanceObjective();

        // Step 5: Assembly at Roundabout
        assertEquals("OBJ_05_RALLY", tracker.getCurrentObjective().getId());
        tracker.advanceObjective();

        // Step 6: Chapter complete
        assertEquals("OBJ_06_COMPLETE", tracker.getCurrentObjective().getId());
        tracker.advanceObjective();

        assertTrue(tracker.isAllCompleted(), "Chapter 1 should be fully completed after final objective");
    }

    @Test
    public void testPlayerPerimeterClamping() {
        com.jme3.scene.Node testNode = new com.jme3.scene.Node("TestNode");
        PlayerControl pc = new PlayerControl(testNode, null, null);
        pc.setPosition(300f, 0f, 300f);

        // Run update tick
        pc.update(0.016f, 0f, false, false, false, false, false, false, false);

        assertTrue(pc.getPosition().x <= 85.0f, "Player X should be clamped inside eastern campus perimeter");
        assertTrue(pc.getPosition().z <= 135.0f, "Player Z should be clamped inside southern campus perimeter");

        pc.setPosition(-300f, 0f, -300f);
        pc.update(0.016f, 0f, false, false, false, false, false, false, false);

        assertTrue(pc.getPosition().x >= -125.0f, "Player X should be clamped inside western campus perimeter");
        assertTrue(pc.getPosition().z >= -175.0f, "Player Z should be clamped inside northern campus perimeter");
    }

    @Test
    public void testCampusMapOverlayLifecycle() {
        com.jme3.scene.Node guiNode = new com.jme3.scene.Node("GuiNode");
        bd.july2024.ui.BengaliTextRenderer textRenderer = new bd.july2024.ui.BengaliTextRenderer(null);
        bd.july2024.ui.ProjectJulyHUD hud = new bd.july2024.ui.ProjectJulyHUD(guiNode, textRenderer, 1280, 720);

        assertFalse(hud.isMapOpen(), "Map should initially be closed");

        hud.toggleMap();
        assertTrue(hud.isMapOpen(), "Map should be open after toggle");

        hud.toggleMap();
        assertFalse(hud.isMapOpen(), "Map should be closed after second toggle");

        hud.showMap();
        assertTrue(hud.isMapOpen(), "Map should be open after showMap()");

        hud.clear();
        assertFalse(hud.isMapOpen(), "Map should close when HUD is cleared");
    }
}
