package bd.spark36.interaction;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.badlogic.gdx.math.Vector3;
import bd.spark36.character.NpcBehaviorState;
import bd.spark36.character.StudentNPC;
import bd.spark36.dialogue.DialogueCallback;
import bd.spark36.dialogue.DialogueManager;
import bd.spark36.dialogue.DialogueSequence;
import bd.spark36.level.DialogueRepository;
import bd.spark36.level.Level1PeacefulCampus;
import bd.spark36.mission.MissionObjective;
import bd.spark36.mission.MissionTracker;

/**
 * Dedicated automated test suite verifying Level 01 Vertical Slice:
 * - Spawn position and orientation (facing south toward Raju Sculpture).
 * - Fictional companions (Rafi, Tanvir, Nusrat) dialogue repository integration.
 * - Dialogue sequence bilingual content and structure.
 * - Sequential 6-objective progression from Raju Roundabout to Central Library to TSC.
 */
public class Level01VerticalSliceTest {

    private Level1PeacefulCampus level1;

    @BeforeEach
    public void setUp() {
        level1 = new Level1PeacefulCampus();
    }

    @Test
    @DisplayName("Level 01: Spawn position and heading align with Raju Sculpture approach")
    public void testSpawnPositionAndHeading() {
        Vector3 spawn = level1.getSpawnPosition();
        assertEquals(44.0f, spawn.x, 0.001f, "Spawn X should be 44m (Raju North approach)");
        assertEquals(0.0f, spawn.y, 0.001f, "Spawn Y should be ground level");
        assertEquals(24.0f, spawn.z, 0.001f, "Spawn Z should be 24m");
        assertEquals(0.0f, level1.getSpawnHeading(), 0.001f, "Spawn heading should be 0 deg (facing south toward Raju)");
        assertTrue(level1.getLocationName().contains("Raju"), "Location name should feature Raju Sculpture");
    }

    @Test
    @DisplayName("Level 01: Checklist contains exactly the 6 canonical objectives")
    public void testChecklistStructure() {
        String[] checklist = level1.getChecklistItems();
        assertNotNull(checklist);
        assertEquals(6, checklist.length, "Level 1 should have exactly 6 sequential objectives");
        assertEquals(6, level1.getTotalObjectiveCount());
        assertEquals(0, level1.getCompletedObjectiveCount());
        assertFalse(level1.isLevelComplete());
    }

    @Test
    @DisplayName("DialogueRepository: Rafi dialogue sequence contains authentic student solidarity lines")
    public void testRafiDialogueSequence() {
        DialogueSequence seq = DialogueRepository.createRafiDialogueSequence();
        assertNotNull(seq);
        assertEquals("seq_friend_rafi", seq.getId());
        assertEquals(3, seq.getLineCount());

        assertEquals("RAFI", seq.getLine(0).getSpeaker());
        assertTrue(seq.getLine(0).getTextBengali().contains("রাজু ভাস্কর্য"));
        assertTrue(seq.getLine(0).getTextEnglish().contains("Raju Sculpture"));

        assertEquals("ARIF", seq.getLine(1).getSpeaker());
        assertTrue(seq.getLine(1).getTextBengali().contains("১৯৯২") || seq.getLine(1).getTextBengali().contains("রাজু"));
        assertTrue(seq.getLine(1).getTextEnglish().contains("peaceful") || seq.getLine(1).getTextEnglish().contains("constitutional"));

        assertEquals("RAFI", seq.getLine(2).getSpeaker());
        assertTrue(seq.getLine(2).getTextEnglish().contains("Central Library"));
    }

    @Test
    @DisplayName("DialogueRepository: Tanvir dialogue sequence contains constitutional equality discussion")
    public void testTanvirDialogueSequence() {
        DialogueSequence seq = DialogueRepository.createTanvirDialogueSequence();
        assertNotNull(seq);
        assertEquals("seq_friend_tanvir", seq.getId());
        assertEquals(3, seq.getLineCount());

        assertEquals("TANVIR", seq.getLine(0).getSpeaker());
        assertTrue(seq.getLine(0).getTextBengali().contains("হাকিম চত্বর") || seq.getLine(0).getTextBengali().contains("লাইব্রেরি"));
        assertTrue(seq.getLine(0).getTextEnglish().contains("Central Library"));

        assertEquals("ARIF", seq.getLine(1).getSpeaker());
        assertTrue(seq.getLine(1).getTextEnglish().contains("Muzharul Islam"));

        assertEquals("TANVIR", seq.getLine(2).getSpeaker());
        assertTrue(seq.getLine(2).getTextEnglish().contains("TSC"));
    }

    @Test
    @DisplayName("DialogueRepository: Nusrat dialogue sequence contains peaceful assembly coordination")
    public void testNusratDialogueSequence() {
        DialogueSequence seq = DialogueRepository.createNusratDialogueSequence();
        assertNotNull(seq);
        assertEquals("seq_friend_nusrat", seq.getId());
        assertEquals(3, seq.getLineCount());

        assertEquals("NUSRAT", seq.getLine(0).getSpeaker());
        assertTrue(seq.getLine(0).getTextBengali().contains("টিএসসি") || seq.getLine(0).getTextEnglish().contains("TSC"));

        assertEquals("ARIF", seq.getLine(1).getSpeaker());
        assertTrue(seq.getLine(1).getTextEnglish().contains("merit") || seq.getLine(1).getTextEnglish().contains("equality"));

        assertEquals("NUSRAT", seq.getLine(2).getSpeaker());
        assertTrue(seq.getLine(2).getTextEnglish().contains("peaceful"));
    }

    @Test
    @DisplayName("Level 01: Sequential progression from Raju to Library to TSC unlocks in exact order")
    public void testSequentialObjectiveProgression() {
        MissionTracker tracker = level1.getMissionTracker();
        assertNotNull(tracker);

        // 1. Initial State: m_rafi active
        MissionObjective obj0 = tracker.getObjective(0);
        assertTrue(obj0.isActive(), "m_rafi must be active initially");
        assertEquals("m_rafi", obj0.getId());
        assertEquals(new Vector3(44f, 0f, 36f), level1.getActiveWaypoint());
        assertEquals("MEET RAFI AT RAJU SCULPTURE", level1.getMissionTitle());

        // 2. Complete Rafi -> m_raju activates
        boolean comp0 = tracker.completeObjective("m_rafi");
        assertTrue(comp0);
        assertEquals(1, level1.getCompletedObjectiveCount());
        assertTrue(tracker.getObjective(1).isActive(), "m_raju must activate after Rafi");
        assertEquals(new Vector3(44f, 0f, 42f), level1.getActiveWaypoint());
        assertEquals("INSPECT RAJU MEMORIAL SCULPTURE", level1.getMissionTitle());

        // 3. Complete Raju Memorial -> m_tanvir activates
        boolean comp1 = tracker.completeObjective("m_raju");
        assertTrue(comp1);
        assertEquals(2, level1.getCompletedObjectiveCount());
        assertTrue(tracker.getObjective(2).isActive(), "m_tanvir must activate after Raju Memorial");
        assertEquals(new Vector3(-54f, 0f, 40f), level1.getActiveWaypoint());
        assertEquals("DISCUSS REFORM WITH TANVIR", level1.getMissionTitle());

        // 4. Complete Tanvir -> m_library activates
        boolean comp2 = tracker.completeObjective("m_tanvir");
        assertTrue(comp2);
        assertEquals(3, level1.getCompletedObjectiveCount());
        assertTrue(tracker.getObjective(3).isActive(), "m_library must activate after Tanvir");
        assertEquals(new Vector3(-58f, 0f, 36f), level1.getActiveWaypoint());
        assertEquals("INSPECT CENTRAL LIBRARY ARCHIVES", level1.getMissionTitle());

        // 5. Complete Central Library -> m_nusrat activates
        boolean comp3 = tracker.completeObjective("m_library");
        assertTrue(comp3);
        assertEquals(4, level1.getCompletedObjectiveCount());
        assertTrue(tracker.getObjective(4).isActive(), "m_nusrat must activate after Library");
        assertEquals(new Vector3(46f, 0f, 20f), level1.getActiveWaypoint());
        assertEquals("REGROUP WITH NUSRAT AT TSC", level1.getMissionTitle());

        // 6. Complete Nusrat -> m_tsc activates
        boolean comp4 = tracker.completeObjective("m_nusrat");
        assertTrue(comp4);
        assertEquals(5, level1.getCompletedObjectiveCount());
        assertTrue(tracker.getObjective(5).isActive(), "m_tsc must activate after Nusrat");
        assertEquals(new Vector3(66f, 0f, 20f), level1.getActiveWaypoint());
        assertEquals("INSPECT TSC SOLIDARITY DECLARATION", level1.getMissionTitle());

        // 7. Complete TSC -> Level Complete!
        boolean comp5 = tracker.completeObjective("m_tsc");
        assertTrue(comp5);
        assertEquals(6, level1.getCompletedObjectiveCount());
        assertTrue(level1.isLevelComplete(), "Level 1 must be complete once all 6 objectives are done");
    }
}
