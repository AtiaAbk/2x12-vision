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
    private final Vector3f targetPosition;
    private final float interactionRadius;
    private boolean completed = false;

    public MissionObjective(String id, String titleBengali, String titleEnglish, String hintBengali, Vector3f targetPosition, float interactionRadius) {
        this.id = id;
        this.titleBengali = titleBengali;
        this.titleEnglish = titleEnglish;
        this.hintBengali = hintBengali;
        this.targetPosition = targetPosition;
        this.interactionRadius = interactionRadius;
    }

    public String getId() { return id; }
    public String getTitleBengali() { return titleBengali; }
    public String getTitleEnglish() { return titleEnglish; }
    public String getHintBengali() { return hintBengali; }
    public Vector3f getTargetPosition() { return targetPosition; }
    public float getInteractionRadius() { return interactionRadius; }
    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }

    public boolean isNear(Vector3f playerPos) {
        if (playerPos == null || targetPosition == null) return false;
        return playerPos.distance(targetPosition) <= interactionRadius;
    }
}
