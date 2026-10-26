package bd.july2024.level;

import com.jme3.math.Vector3f;

/**
 * Historical Mission Objective for Project July 2024.
 */
public class MissionObjective {

    private final String id;
    private final String titleBengali;
    private final String titleEnglish;
    private final String hintBengali;
    private final String hintEnglish;
    private final Vector3f targetPosition;
    private final float interactionRadius;
    private boolean completed = false;

    public MissionObjective(String id, String titleBengali, String titleEnglish, String hintBengali, Vector3f targetPosition, float interactionRadius) {
        this(id, titleBengali, titleEnglish, hintBengali, defaultEnglishHint(id), targetPosition, interactionRadius);
    }

    public MissionObjective(String id, String titleBengali, String titleEnglish, String hintBengali, String hintEnglish, Vector3f targetPosition, float interactionRadius) {
        this.id = id;
        this.titleBengali = titleBengali;
        this.titleEnglish = titleEnglish;
        this.hintBengali = hintBengali;
        this.hintEnglish = hintEnglish;
        this.targetPosition = targetPosition;
        this.interactionRadius = interactionRadius;
    }

    private static String defaultEnglishHint(String id) {
        if ("OBJ_01_RAFI".equals(id)) return "Rafi is waiting at the base of the sculpture";
        if ("OBJ_02_LIBRARY".equals(id)) return "Find Tanvir in front of the Central Library colonnade";
        if ("OBJ_03_TSC".equals(id)) return "Meet Nusrat at the Teacher-Student Centre entrance";
        if ("OBJ_04_PLAQUE".equals(id)) return "Inspect the historic bronze plaque beside the sculpture";
        if ("OBJ_05_RALLY".equals(id)) return "Assemble with students at the roundabout intersection";
        if ("OBJ_06_COMPLETE".equals(id)) return "Chapter complete: Solidarity and Historical Resolve";
        return "";
    }

    public String getId() { return id; }
    public String getTitleBengali() { return titleBengali; }
    public String getTitleEnglish() { return titleEnglish; }
    public String getHintBengali() { return hintBengali; }
    public String getHintEnglish() { return hintEnglish != null ? hintEnglish : ""; }
    public Vector3f getTargetPosition() { return targetPosition; }
    public float getInteractionRadius() { return interactionRadius; }
    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }

    public boolean isNear(Vector3f playerPos) {
        if (playerPos == null || targetPosition == null) return false;
        return playerPos.distance(targetPosition) <= interactionRadius;
    }
}
