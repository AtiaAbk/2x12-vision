package bd.july2024.level;

import com.badlogic.gdx.math.Vector3;

/**
 * Encapsulates a historical campaign objective in Project July 2024.
 */
public class MissionObjective {

    public enum State {
        INACTIVE,
        ACTIVE,
        COMPLETED
    }

    private final String id;
    private final String title;
    private final String targetDescription;
    private final String checklistLabel;
    private final Vector3 targetPosition = new Vector3();
    private final float targetRadius;

    private State state = State.INACTIVE;

    public MissionObjective(String id, String title, String targetDescription, String checklistLabel,
                            Vector3 targetPosition, float targetRadius, boolean startsActive) {
        this.id = id;
        this.title = title;
        this.targetDescription = targetDescription;
        this.checklistLabel = checklistLabel;
        if (targetPosition != null) {
            this.targetPosition.set(targetPosition);
        }
        this.targetRadius = targetRadius;
        this.state = startsActive ? State.ACTIVE : State.INACTIVE;
    }

    public boolean activate() {
        if (state == State.INACTIVE) {
            state = State.ACTIVE;
            return true;
        }
        return false;
    }

    public boolean complete() {
        if (state == State.ACTIVE) {
            state = State.COMPLETED;
            return true;
        }
        return false;
    }

    public void reset(boolean active) {
        state = active ? State.ACTIVE : State.INACTIVE;
    }

    public boolean checkProximity(Vector3 position) {
        if (state != State.ACTIVE || position == null) return false;
        return position.dst(targetPosition) <= targetRadius;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getTargetDescription() {
        return targetDescription;
    }

    public String getChecklistLabel() {
        return checklistLabel;
    }

    public Vector3 getTargetPosition() {
        return targetPosition;
    }

    public float getTargetRadius() {
        return targetRadius;
    }

    public State getState() {
        return state;
    }

    public boolean isActive() {
        return state == State.ACTIVE;
    }

    public boolean isComplete() {
        return state == State.COMPLETED;
    }
}
