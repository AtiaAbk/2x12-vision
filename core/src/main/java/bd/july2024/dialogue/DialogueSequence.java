package bd.july2024.dialogue;

import com.badlogic.gdx.utils.Array;

/**
 * Encapsulates a multi-line conversation sequence with speaker tags,
 * bilingual subtitles, and an optional completion callback.
 */
public class DialogueSequence {

    private final String id;
    private final String title;
    private final Array<DialogueLine> lines;
    private DialogueCallback callback;

    public DialogueSequence(String id, String title, DialogueCallback callback, DialogueLine... linesArray) {
        this.id = (id != null) ? id : "sequence_default";
        this.title = (title != null) ? title : "";
        this.callback = callback;
        this.lines = new Array<DialogueLine>(false, linesArray != null ? linesArray.length : 4);
        if (linesArray != null) {
            for (DialogueLine line : linesArray) {
                if (line != null) this.lines.add(line);
            }
        }
    }

    public DialogueSequence(String id, String title, DialogueLine... linesArray) {
        this(id, title, null, linesArray);
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getLineCount() {
        return lines.size;
    }

    public DialogueLine getLine(int index) {
        if (index >= 0 && index < lines.size) {
            return lines.get(index);
        }
        return null;
    }

    public Array<DialogueLine> getLines() {
        return lines;
    }

    public DialogueCallback getCallback() {
        return callback;
    }

    public DialogueSequence setCallback(DialogueCallback callback) {
        this.callback = callback;
        return this;
    }
}
