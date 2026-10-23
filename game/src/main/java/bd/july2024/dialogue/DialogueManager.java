package bd.july2024.dialogue;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages active sequential dialogues in Project July 2024.
 */
public class DialogueManager {

    public interface DialogueCallback {
        void onDialogueFinished();
    }

    private final List<DialogueLine> currentLines = new ArrayList<>();
    private int currentLineIndex = 0;
    private boolean active = false;
    private DialogueCallback onFinishedCallback;

    public void startDialogue(List<DialogueLine> lines, DialogueCallback callback) {
        if (lines == null || lines.isEmpty()) return;
        currentLines.clear();
        currentLines.addAll(lines);
        currentLineIndex = 0;
        active = true;
        this.onFinishedCallback = callback;
    }

    public boolean advanceDialogue() {
        if (!active) return false;
        currentLineIndex++;
        if (currentLineIndex >= currentLines.size()) {
            active = false;
            if (onFinishedCallback != null) {
                onFinishedCallback.onDialogueFinished();
            }
            return false;
        }
        return true;
    }

    public boolean isActive() {
        return active;
    }

    public DialogueLine getCurrentLine() {
        if (active && currentLineIndex < currentLines.size()) {
            return currentLines.get(currentLineIndex);
        }
        return null;
    }

    public void dismiss() {
        active = false;
        currentLines.clear();
        currentLineIndex = 0;
    }

    public void endDialogue() {
        dismiss();
    }
}
