package bd.july2024.dialogue;

/**
 * Single spoken dialogue line with bilingual subtitles and speaker identification.
 */
public class DialogueLine {

    private final String speakerId;
    private final String speakerBengali;
    private final String textBengali;
    private final String textEnglish;

    public DialogueLine(String speakerId, String speakerBengali, String textBengali, String textEnglish) {
        this.speakerId = (speakerId != null) ? speakerId : "SPEAKER";
        this.speakerBengali = (speakerBengali != null) ? speakerBengali : speakerId;
        this.textBengali = (textBengali != null) ? textBengali : "";
        this.textEnglish = (textEnglish != null) ? textEnglish : "";
    }

    public String getSpeakerId() {
        return speakerId;
    }

    public String getSpeakerBengali() {
        return speakerBengali;
    }

    public String getTextBengali() {
        return textBengali;
    }

    public String getTextEnglish() {
        return textEnglish;
    }
}
