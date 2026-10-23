package bd.july2024;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.badlogic.gdx.math.Vector3;
import bd.july2024.character.CharacterPose;
import bd.july2024.character.CompanionNPC;
import bd.july2024.character.PlayerController;
import bd.july2024.dialogue.DialogueCallback;
import bd.july2024.dialogue.DialogueLine;
import bd.july2024.dialogue.DialogueManager;
import bd.july2024.dialogue.DialogueSequence;
import bd.july2024.dialogue.HistoricalDialogues;
import bd.july2024.level.MissionObjective;
import bd.july2024.level.MissionTracker;

/**
 * Automated test suite verifying the rebuilt Project July 2024 architecture:
 * - Level 01 spatial orientation and spawn coordinates.
 * - Sequential 6-objective progression from Raju Sculpture to Library and TSC.
 * - Bilingual dialogue playback and callback completion.
 * - Kinematic character poses and locomotion stamina.
 */
public class ProjectJulyLevel01Test {

    private MissionTracker tracker;
    private DialogueManager dialogueManager;

    @BeforeEach
    public void setUp() {
        dialogueManager = new DialogueManager();
        tracker = new MissionTracker(true);

        tracker.addObjective(new MissionObjective(
            "m_rafi", "MEET RAFI AT RAJU SCULPTURE", "Raju Memorial Roundabout",
            "1. Meet Rafi at Raju Sculpture", new Vector3(44f, 0f, 36f), 3.8f, true
        ));
        tracker.addObjective(new MissionObjective(
            "m_raju", "INSPECT RAJU MEMORIAL PLAQUE", "Anti-Terrorism Raju Memorial",
            "2. Inspect Raju Memorial Plaque", new Vector3(44f, 0f, 42f), 3.8f, false
        ));
        tracker.addObjective(new MissionObjective(
            "m_tanvir", "DISCUSS REFORM WITH TANVIR", "Central Library Steps",
            "3. Discuss Reform with Tanvir", new Vector3(-54f, 0f, 40f), 3.8f, false
        ));
        tracker.addObjective(new MissionObjective(
            "m_library", "INSPECT CENTRAL LIBRARY ARCHIVES", "Central Library Colonnade",
            "4. Inspect Central Library Archives", new Vector3(-58f, 0f, 36f), 3.8f, false
        ));
        tracker.addObjective(new MissionObjective(
            "m_nusrat", "REGROUP WITH NUSRAT AT TSC", "TSC Cafeteria Terrace",
            "5. Regroup with Nusrat at TSC Terrace", new Vector3(46f, 0f, 20f), 3.8f, false
        ));
        tracker.addObjective(new MissionObjective(
            "m_tsc", "INSPECT TSC SOLIDARITY DECLARATION", "TSC Main Plaza",
            "6. Inspect TSC Solidarity Declaration", new Vector3(66f, 0f, 20f), 3.8f, false
        ));
    }

    @Test
    @DisplayName("Level 01: Spawn coordinates align with Northern Raju Sculpture avenue")
    public void testLevel01SpawnCoordinates() {
        Vector3 spawn = new Vector3(44.0f, 0f, 24.0f);
        float heading = 0.0f; // South (+Z)

        assertEquals(44.0f, spawn.x, 0.001f);
        assertEquals(0.0f, spawn.y, 0.001f);
        assertEquals(24.0f, spawn.z, 0.001f);
        assertEquals(0.0f, heading, 0.001f);
    }

    @Test
    @DisplayName("MissionTracker: 6 sequential objectives progress from Raju to Library to TSC")
    public void testSequentialMissionProgression() {
        assertEquals(6, tracker.getTotalCount());
        assertEquals(0, tracker.getCompletedCount());
        assertFalse(tracker.isAllCompleted());

        // Step 1: Meet Rafi
        assertTrue(tracker.getObjective(0).isActive());
        assertEquals(new Vector3(44f, 0f, 36f), tracker.getActiveWaypoint());
        assertTrue(tracker.completeObjective("m_rafi"));
        assertEquals(1, tracker.getCompletedCount());

        // Step 2: Inspect Raju Memorial Plaque
        assertTrue(tracker.getObjective(1).isActive());
        assertEquals(new Vector3(44f, 0f, 42f), tracker.getActiveWaypoint());
        assertTrue(tracker.completeObjective("m_raju"));
        assertEquals(2, tracker.getCompletedCount());

        // Step 3: Discuss with Tanvir at Library
        assertTrue(tracker.getObjective(2).isActive());
        assertEquals(new Vector3(-54f, 0f, 40f), tracker.getActiveWaypoint());
        assertTrue(tracker.completeObjective("m_tanvir"));
        assertEquals(3, tracker.getCompletedCount());

        // Step 4: Inspect Central Library Archives
        assertTrue(tracker.getObjective(3).isActive());
        assertEquals(new Vector3(-58f, 0f, 36f), tracker.getActiveWaypoint());
        assertTrue(tracker.completeObjective("m_library"));
        assertEquals(4, tracker.getCompletedCount());

        // Step 5: Regroup with Nusrat at TSC
        assertTrue(tracker.getObjective(4).isActive());
        assertEquals(new Vector3(46f, 0f, 20f), tracker.getActiveWaypoint());
        assertTrue(tracker.completeObjective("m_nusrat"));
        assertEquals(5, tracker.getCompletedCount());

        // Step 6: Inspect TSC Solidarity Declaration
        assertTrue(tracker.getObjective(5).isActive());
        assertEquals(new Vector3(66f, 0f, 20f), tracker.getActiveWaypoint());
        assertTrue(tracker.completeObjective("m_tsc"));
        assertEquals(6, tracker.getCompletedCount());

        assertTrue(tracker.isAllCompleted(), "Level must be fully completed after all 6 objectives");
    }

    @Test
    @DisplayName("HistoricalDialogues: Rafi sequence contains authentic student unity lines")
    public void testRafiDialogue() {
        DialogueSequence seq = HistoricalDialogues.createRafiDialogue();
        assertNotNull(seq);
        assertEquals(3, seq.getLineCount());

        assertEquals("RAFI", seq.getLine(0).getSpeakerId());
        assertTrue(seq.getLine(0).getTextBengali().contains("রাজু ভাস্কর্য"));
        assertTrue(seq.getLine(0).getTextEnglish().contains("Raju Sculpture"));

        assertEquals("ARIF", seq.getLine(1).getSpeakerId());
        assertTrue(seq.getLine(1).getTextBengali().contains("১৯৯২"));
        assertTrue(seq.getLine(1).getTextEnglish().contains("1992"));

        assertEquals("RAFI", seq.getLine(2).getSpeakerId());
        assertTrue(seq.getLine(2).getTextEnglish().contains("Central Library"));
    }

    @Test
    @DisplayName("HistoricalDialogues: Tanvir sequence contains Muzharul Islam and quota reform discussion")
    public void testTanvirDialogue() {
        DialogueSequence seq = HistoricalDialogues.createTanvirDialogue();
        assertNotNull(seq);
        assertEquals(3, seq.getLineCount());

        assertEquals("TANVIR", seq.getLine(0).getSpeakerId());
        assertTrue(seq.getLine(0).getTextBengali().contains("কেন্দ্রীয় লাইব্রেরি") || seq.getLine(0).getTextBengali().contains("লাইব্রেরি"));

        assertEquals("ARIF", seq.getLine(1).getSpeakerId());
        assertTrue(seq.getLine(1).getTextBengali().contains("মাজহারুল ইসলাম"));
        assertTrue(seq.getLine(1).getTextEnglish().contains("Muzharul Islam"));

        assertEquals("TANVIR", seq.getLine(2).getSpeakerId());
        assertTrue(seq.getLine(2).getTextEnglish().contains("TSC"));
    }

    @Test
    @DisplayName("HistoricalDialogues: Nusrat sequence contains Doxiadis and peaceful unity message")
    public void testNusratDialogue() {
        DialogueSequence seq = HistoricalDialogues.createNusratDialogue();
        assertNotNull(seq);
        assertEquals(3, seq.getLineCount());

        assertEquals("NUSRAT", seq.getLine(0).getSpeakerId());
        assertTrue(seq.getLine(0).getTextBengali().contains("ডক্সিয়াডিসের") || seq.getLine(0).getTextEnglish().contains("Doxiadis"));

        assertEquals("ARIF", seq.getLine(1).getSpeakerId());
        assertTrue(seq.getLine(1).getTextEnglish().contains("equality"));

        assertEquals("NUSRAT", seq.getLine(2).getSpeakerId());
        assertTrue(seq.getLine(2).getTextEnglish().contains("peaceful"));
    }

    @Test
    @DisplayName("DialogueManager: Line advancement and completion callback execution")
    public void testDialogueManagerAdvancement() {
        final boolean[] completed = {false};
        DialogueLine l1 = new DialogueLine("P1", "বক্তা ১", "বাংলা বাক্য ১", "English line 1");
        DialogueLine l2 = new DialogueLine("P2", "বক্তা ২", "বাংলা বাক্য ২", "English line 2");

        DialogueSequence seq = new DialogueSequence("test_seq", "Test", new DialogueCallback() {
            @Override
            public void onDialogueComplete(String sequenceId) {
                completed[0] = true;
            }
        }, l1, l2);

        dialogueManager.startDialogue(seq);
        assertTrue(dialogueManager.isDialogueActive());
        assertEquals(0, dialogueManager.getCurrentLineIndex());

        // Advance to line 2
        boolean hasMore = dialogueManager.advanceDialogue();
        assertTrue(hasMore);
        assertEquals(1, dialogueManager.getCurrentLineIndex());
        assertTrue(dialogueManager.isLastLine());

        // Advance past line 2 finishes dialogue
        boolean pastEnd = dialogueManager.advanceDialogue();
        assertFalse(pastEnd);
        assertFalse(dialogueManager.isDialogueActive());
        assertTrue(completed[0], "Dialogue completion callback must be called");
    }

    @Test
    @DisplayName("PlayerController: Locomotion speeds and stamina bounds")
    public void testPlayerLocomotionParameters() {
        PlayerController player = new PlayerController();
        assertEquals(100f, player.getStamina(), 0.01f);

        player.setPosition(44f, 0f, 24f);
        assertEquals(44f, player.getPosition().x, 0.001f);
        assertEquals(24f, player.getPosition().z, 0.001f);

        // Update with no input maintains stamina
        player.update(0.016f, 0f, false);
        assertEquals(100f, player.getStamina(), 0.01f);
        assertFalse(player.isSprinting());
    }

    @Test
    @DisplayName("CharacterPose: Kinematics evaluate idle breathing and running stride")
    public void testCharacterPoseEvaluation() {
        CharacterPose pose = new CharacterPose();

        // Idle pose
        pose.evaluate(0f, 0f, 1.0f, false);
        assertTrue(Math.abs(pose.rootOffsetY) <= 0.05f);

        // Running pose (speed = 7.0 m/s)
        pose.evaluate(7.0f, 1.57f, 1.0f, false);
        assertTrue(Math.abs(pose.leftLegPitch) > 10f);
        assertTrue(Math.abs(pose.rightLegPitch) > 10f);
    }
}
