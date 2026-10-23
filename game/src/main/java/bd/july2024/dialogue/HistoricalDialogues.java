package bd.july2024.dialogue;

import java.util.ArrayList;
import java.util.List;

/**
 * Historical dialogue repository with authentic Bengali Unicode and English subtitles.
 */
public class HistoricalDialogues {

    public static List<DialogueLine> getRafiDialogue() {
        List<DialogueLine> lines = new ArrayList<>();
        lines.add(new DialogueLine("আরিফ (Arif)", "রাফি! ক্যাম্পাসের অবস্থা কেমন? সবাই কি জড়ো হচ্ছে?", "Rafi! How is the campus right now? Is everyone assembling?"));
        lines.add(new DialogueLine("রাফি (Rafi)", "হ্যাঁ আরিফ, লাইব্রেরি চত্বর আর হাকিম চত্বরে ছাত্রছাত্রীরা জড়ো হয়েছে। সবাই শান্তিপূর্ণ সমাবেশ চায়।", "Yes Arif, students are gathering around the Library and Hakim Chattar. Everyone wants a peaceful assembly."));
        lines.add(new DialogueLine("রাফি (Rafi)", "তানভীরের কাছে সচেতনতামূলক লিফলেটগুলো আছে। তুমি হাকিম চত্বর থেকে লিফলেটগুলো নিয়ে এসো।", "Tanvir has the informational leaflets. Go collect them from Hakim Chattar."));
        return lines;
    }

    public static List<DialogueLine> getTanvirDialogue() {
        List<DialogueLine> lines = new ArrayList<>();
        lines.add(new DialogueLine("তানভীর (Tanvir)", "আরিফ, এই নাও লিফলেটগুলো। এতে কোটা সংস্কার আন্দোলনের যৌক্তিক দাবিগুলো সুন্দর করে লেখা আছে।", "Arif, here are the leaflets. They clearly outline the justified demands for quota reform."));
        lines.add(new DialogueLine("তানভীর (Tanvir)", "টিএসসিতে নুসরাত আর অন্যরা অপেক্ষা করছে। এই লিফলেটগুলো দ্রুত ওদের কাছে পৌঁছে দাও।", "Nusrat and others are waiting at TSC. Deliver these leaflets to them quickly."));
        lines.add(new DialogueLine("আরিফ (Arif)", "আমি এখনই যাচ্ছি। সবাই ঐক্যবদ্ধ থাকলে কোনো অন্যায় আমাদের দমাতে পারবে না।", "I am on my way. If we stand united, no injustice can stop us."));
        return lines;
    }

    public static List<DialogueLine> getNusratDialogue() {
        List<DialogueLine> lines = new ArrayList<>();
        lines.add(new DialogueLine("নুসরাত (Nusrat)", "ধন্যবাদ আরিফ! এই লিফলেটগুলো সাধারণ শিক্ষার্থীদের মাঝে সঠিক তথ্য পৌঁছে দেবে।", "Thank you Arif! These leaflets will convey the truth to general students."));
        lines.add(new DialogueLine("নুসরাত (Nusrat)", "আমাদের শক্তি হলো সত্য আর অহিংস ঐক্য। রাজু ভাস্কর্যের সামনের তথ্যফলকটি একবার দেখে এসো।", "Our strength is truth and non-violent unity. Go inspect the memorial plaque near Raju sculpture."));
        lines.add(new DialogueLine("নুসরাত (Nusrat)", "ঐতিহাসিক সংকল্প মনে রাখা আমাদের প্রত্যেকের দায়িত্ব।", "Remembering our historical resolve is the duty of every one of us."));
        return lines;
    }

    public static List<DialogueLine> getMemorialPlaqueDialogue() {
        List<DialogueLine> lines = new ArrayList<>();
        lines.add(new DialogueLine("ঐতিহাসিক তথ্যফলক (Memorial Plaque)", "জুলাই ২০২৪: বৈষম্যবিরোধী ছাত্র আন্দোলনের ঐতিহাসিক সূচনা।", "July 2024: The historic beginning of the Anti-Discrimination Student Movement."));
        lines.add(new DialogueLine("ঐতিহাসিক তথ্যফলক (Memorial Plaque)", "শহীদদের আত্মত্যাগ ও সাধারণ ছাত্র-জনতার ঐতিহাসিক একতা অন্যায়ের বিরুদ্ধে চিরন্তন প্রেরণা।", "The sacrifice of the martyrs and the unity of students remains an eternal inspiration against oppression."));
        return lines;
    }

    public static List<DialogueLine> getRallyDialogue() {
        List<DialogueLine> lines = new ArrayList<>();
        lines.add(new DialogueLine("ছাত্র-জনতা (Students Collective)", "আমাদের দাবি এক ও অভিন্ন — মেধার মূল্যায়ন ও বৈষম্যের অবসান!", "Our demand is united — evaluation by merit and an end to all discrimination!"));
        lines.add(new DialogueLine("আরিফ (Arif)", "আমরা অন্যায়ের কাছে মাথা নত করব না। ঐক্যই আমাদের বিজয়।", "We will not bow to injustice. Unity is our victory."));
        return lines;
    }
}
