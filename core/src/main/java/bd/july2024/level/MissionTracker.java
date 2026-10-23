package bd.july2024.level;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

/**
 * Sequential historical mission tracker for Project July 2024.
 * Automatically advances waypoints and checklist entries as Arif traverses the campus.
 */
public class MissionTracker {

    private final Array<MissionObjective> objectives = new Array<MissionObjective>(false, 8);
    private boolean isSequential = true;

    public MissionTracker(boolean sequential) {
        this.isSequential = sequential;
    }

    public MissionTracker addObjective(MissionObjective obj) {
        if (obj != null) {
            objectives.add(obj);
        }
        return this;
    }

    public boolean completeObjective(String id) {
        if (id == null) return false;
        for (int i = 0; i < objectives.size; i++) {
            MissionObjective obj = objectives.get(i);
            if (id.equals(obj.getId()) && obj.isActive()) {
                if (obj.complete()) {
                    handleObjectiveCompleted(i);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean completeObjective(int index) {
        if (index >= 0 && index < objectives.size) {
            MissionObjective obj = objectives.get(index);
            if (obj.isActive() && obj.complete()) {
                handleObjectiveCompleted(index);
                return true;
            }
        }
        return false;
    }

    private void handleObjectiveCompleted(int index) {
        if (isSequential) {
            int nextIndex = index + 1;
            if (nextIndex < objectives.size) {
                objectives.get(nextIndex).activate();
            }
        }
    }

    public MissionObjective getActiveObjective() {
        for (int i = 0; i < objectives.size; i++) {
            MissionObjective obj = objectives.get(i);
            if (obj.isActive()) return obj;
        }
        if (objectives.size > 0 && isAllCompleted()) {
            return objectives.get(objectives.size - 1);
        }
        return (objectives.size > 0) ? objectives.get(0) : null;
    }

    public Vector3 getActiveWaypoint() {
        MissionObjective active = getActiveObjective();
        return (active != null) ? active.getTargetPosition() : null;
    }

    public boolean isAllCompleted() {
        if (objectives.size == 0) return false;
        for (int i = 0; i < objectives.size; i++) {
            if (!objectives.get(i).isComplete()) return false;
        }
        return true;
    }

    public int getCompletedCount() {
        int count = 0;
        for (int i = 0; i < objectives.size; i++) {
            if (objectives.get(i).isComplete()) count++;
        }
        return count;
    }

    public int getTotalCount() {
        return objectives.size;
    }

    public MissionObjective getObjective(int index) {
        if (index >= 0 && index < objectives.size) {
            return objectives.get(index);
        }
        return null;
    }

    public String[] getChecklistItems() {
        String[] items = new String[objectives.size];
        for (int i = 0; i < objectives.size; i++) {
            items[i] = objectives.get(i).getChecklistLabel();
        }
        return items;
    }

    public boolean isChecklistItemDone(int index) {
        if (index >= 0 && index < objectives.size) {
            return objectives.get(index).isComplete();
        }
        return false;
    }

    public void reset() {
        for (int i = 0; i < objectives.size; i++) {
            objectives.get(i).reset(i == 0);
        }
    }

    public void completeAllForTest() {
        for (int i = 0; i < objectives.size; i++) {
            objectives.get(i).activate();
            objectives.get(i).complete();
        }
    }
}
