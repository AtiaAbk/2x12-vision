package bd.july2024.dialogue;

/**
 * Manages active dialogue sequence playback, line progression, and state transitions.
 */
public class DialogueManager {

    private DialogueSequence activeSequence = null;
    private int currentLineIndex = 0;
    private boolean isFinished = false;

    public void startDialogue(DialogueSequence sequence) {
        if (sequence == null || sequence.getLineCount() == 0) return;
        this.activeSequence = sequence;
        this.currentLineIndex = 0;
        this.isFinished = false;
    }

    public boolean advanceDialogue() {
        if (!isDialogueActive()) return false;

        currentLineIndex++;
        if (currentLineIndex >= activeSequence.getLineCount()) {
            finishDialogue();
            return false;
        }
        return true;
    }

    public void cancelDialogue() {
        this.activeSequence = null;
        this.currentLineIndex = 0;
        this.isFinished = false;
    }

    private void finishDialogue() {
        DialogueSequence completed = activeSequence;
        this.activeSequence = null;
        this.currentLineIndex = 0;
        this.isFinished = true;

        if (completed != null && completed.getCallback() != null) {
            completed.getCallback().onDialogueComplete(completed.getId());
        }
    }

    public boolean isDialogueActive() {
        return activeSequence != null && currentLineIndex < activeSequence.getLineCount();
    }

    public DialogueSequence getActiveSequence() {
        return activeSequence;
    }

    public DialogueLine getCurrentLine() {
        if (activeSequence != null && currentLineIndex >= 0 && currentLineIndex < activeSequence.getLineCount()) {
            return activeSequence.getLine(currentLineIndex);
        }
        return null;
    }

    public int getCurrentLineIndex() {
        return currentLineIndex;
    }

    public int getTotalLines() {
        return (activeSequence != null) ? activeSequence.getLineCount() : 0;
    }

    public boolean isLastLine() {
        return activeSequence != null && currentLineIndex == activeSequence.getLineCount() - 1;
    }
}
