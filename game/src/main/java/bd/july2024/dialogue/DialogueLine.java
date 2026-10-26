package bd.july2024.dialogue;

/**
 * A dialogue entry containing authentic spoken Bangla dialogue paired with English subtitles
 * and optional voice acting audio clip path.
 */
public class DialogueLine {

    private final String speaker;
    private final String textBengali;
    private final String textEnglish;
    private final String voiceClip;

    public DialogueLine(String speaker, String textBengali, String textEnglish) {
        this(speaker, textBengali, textEnglish, null);
    }

    public DialogueLine(String speaker, String textBengali, String textEnglish, String voiceClip) {
        this.speaker = speaker;
        this.textBengali = textBengali;
        this.textEnglish = textEnglish;
        this.voiceClip = voiceClip;
    }

    public String getSpeaker() { return speaker; }
    public String getSpeakerEnglish() {
        if (speaker == null) return "";
        if (speaker.contains("(") && speaker.contains(")")) {
            int start = speaker.indexOf('(') + 1;
            int end = speaker.indexOf(')');
            if (end > start) return speaker.substring(start, end).trim();
        }
        return speaker;
    }
    public String getTextBengali() { return textBengali; }
    public String getTextEnglish() { return textEnglish; }
    public String getVoiceClip() { return voiceClip; }
    public boolean hasVoiceClip() { return voiceClip != null && !voiceClip.trim().isEmpty(); }
}
