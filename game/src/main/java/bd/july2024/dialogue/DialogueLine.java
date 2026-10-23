package bd.july2024.dialogue;

/**
 * A bilingual dialogue entry with speaker name and Bengali/English text.
 */
public class DialogueLine {

    private final String speaker;
    private final String textBengali;
    private final String textEnglish;

    public DialogueLine(String speaker, String textBengali, String textEnglish) {
        this.speaker = speaker;
        this.textBengali = textBengali;
        this.textEnglish = textEnglish;
    }

    public String getSpeaker() { return speaker; }
    public String getTextBengali() { return textBengali; }
    public String getTextEnglish() { return textEnglish; }
}
