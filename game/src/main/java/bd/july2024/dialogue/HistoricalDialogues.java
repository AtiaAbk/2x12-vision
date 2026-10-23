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

    public static bd.july2024.ui.HistoricalDocumentModal.DocumentRecord getRajuMemorialDocument() {
        return new bd.july2024.ui.HistoricalDocumentModal.DocumentRecord(
            "সন্ত্রাসবিরোধী রাজু স্মারক ভাস্কর্য",
            "ANTI-TERRORISM RAJU MEMORIAL",
            "July 1 – 14, 2024",
            "TSC Raju Sculpture Roundabout, Dhaka University",
            "১৯৯২ সালের ১৩ মার্চ ক্যাম্পাসে সন্ত্রাসবিরোধী গণতান্ত্রিক মিছিলে শহীদ মঈন হোসেন রাজুর স্মৃতিতে এই ভাস্কর্য নির্মিত হয়। ২০২৪ সালের জুলাইয়ে কোটা সংস্কার ও মেধার মূল্যায়নের দাবিতে শিক্ষার্থীরা এই ভাস্কর্যের চারপাশেই শান্তিপূর্ণ ঐক্যের প্রাচীর গড়ে তোলে।",
            "Erected in memory of martyr Moin Hossain Raju, killed during an anti-terrorism democratic march on March 13, 1992. In July 2024, students locked arms here to form a peaceful wall of solidarity demanding equality and merit-based governance."
        );
    }

    public static bd.july2024.ui.HistoricalDocumentModal.DocumentRecord getLibraryDocument() {
        return new bd.july2024.ui.HistoricalDocumentModal.DocumentRecord(
            "ঢাকা বিশ্ববিদ্যালয় কেন্দ্রীয় লাইব্রেরি ও হাকিম চত্বর",
            "CENTRAL LIBRARY & HAKIM CHATTAR",
            "July 7 – 18, 2024",
            "Central Library Steps & Pilotis Colonnade",
            "স্থপতি মাজহারুল ইসলামের ১৯৫৩ সালের আধুনিক স্থাপত্যে নির্মিত এই লাইব্রেরি মুক্তচিন্তা ও বুদ্ধিবৃত্তিক প্রতিরোধের কেন্দ্রবিন্দু। আন্দোলনের সময় হাকিম চত্বর ও লাইব্রেরির উন্মুক্ত চত্বরে শিক্ষার্থীরা চিকিৎসাসেবা, খাদ্য বিতরণ ও ব্ল্যাকআউট চলাকালে যোগাযোগ কেন্দ্র পরিচালনা করে।",
            "Designed by pioneer architect Muzharul Islam in 1953, this regional modernist landmark became the nexus of intellectual solidarity. Students established study circles, emergency aid, and dispatch centers beneath its iconic open pilotis."
        );
    }

    public static bd.july2024.ui.HistoricalDocumentModal.DocumentRecord getTscDocument() {
        return new bd.july2024.ui.HistoricalDocumentModal.DocumentRecord(
            "টিএসসি ও ঐতিহাসিক জুলাই সংহতি ঘোষণা",
            "TEACHER-STUDENT CENTRE (TSC)",
            "August 5, 2024 (36 July)",
            "TSC Main Plaza & Modernist Pavilion",
            "১৯৬১ সালে বিশ্বখ্যাত স্থপতি কনস্টান্টিনোস ডক্সিয়াডিসের মাস্টারপ্ল্যানে নির্মিত ছাত্র-শিক্ষক কেন্দ্র (টিএসসি) বাঙালির প্রতিটি জাতীয় গণতান্ত্রিক উত্তরণের সূতিকাগার। ২০২৪ সালের ৫ আগস্ট এই প্রাঙ্গণেই ঘোষিত হয় ঐতিহাসিক বিজয়, যাকে ছাত্র-জনতা নাম দেয় '৩৬ জুলাই'।",
            "Constructed under the master plan of architect Constantinos Doxiadis in 1961, the Teacher-Student Centre has sheltered student conscience through every turning point. Here, the historic culmination was proclaimed as '36 July' — rewriting time for justice."
        );
    }
}
