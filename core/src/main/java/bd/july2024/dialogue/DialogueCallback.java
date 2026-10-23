package bd.july2024.dialogue;

/**
 * Callback invoked when a dialogue sequence finishes execution.
 */
public interface DialogueCallback {
    void onDialogueComplete(String sequenceId);
}
