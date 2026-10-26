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
        lines.add(new DialogueLine("ঐতিহাসিক তথ্যফলক (Memorial Plaque)", "সন্ত্রাসবিরোধী রাজু স্মারক ভাস্কর্য। ভাস্কর শ্যামল চৌধুরী ও গোপাল পাল; উদ্বোধন ১৭ সেপ্টেম্বর ১৯৯৭।", "Anti-Terrorism Raju Memorial Sculpture. Sculptors Shaymol Chowdhury and Gopal Paul; inaugurated 17 September 1997."));
        lines.add(new DialogueLine("ঐতিহাসিক তথ্যফলক (Memorial Plaque)", "১৯৯২ সালের ১৩ মার্চ ক্যাম্পাসে সন্ত্রাসবিরোধী মিছিলে নিহত মঈন হোসেন রাজুর স্মরণে।", "In memory of Moin Hossain Raju, killed in the anti-terrorism march on campus on 13 March 1992."));
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
            "July 1 – 14, 2024",
            "Central Library open ground floor and Hakim Chattar",
            "স্থপতি মাজহারুল ইসলামের নকশায় ১৯৫৩–৫৫ সালে নির্মিত কেন্দ্রীয় লাইব্রেরি: স্তম্ভের ওপর তোলা দুটি আয়তাকার ব্লক, দোতলায় ওঠার ঢালু পথ, খোলা নিচতলা আর খিলানছাদের একটি ব্লক। লাইব্রেরি ও সড়কের মাঝের হাকিম চত্বর শিক্ষার্থীদের চায়ের আড্ডার জায়গা। ২০২৪ সালের জুলাইয়ের প্রথম সপ্তাহে কোটা সংস্কারের দাবিতে এই লাইব্রেরির সামনে শান্তিপূর্ণ সমাবেশ হয়; বৈষম্যবিরোধী ছাত্র আন্দোলন নামটি এই দিনগুলোতেই সামনে আসে।",
            "Designed by Muzharul Islam and built in 1953–1955: two rectangular blocks raised on pilotis, a ramp to the first floor, an open ground floor and a third block under a vaulted roof. Hakim Chattar, between the library and the road, is where students gather over tea. In the first week of July 2024 peaceful rallies for quota reform assembled in front of this library, the days in which the name Students Against Discrimination came into use."
        );
    }

    public static bd.july2024.ui.HistoricalDocumentModal.DocumentRecord getTscDocument() {
        return new bd.july2024.ui.HistoricalDocumentModal.DocumentRecord(
            "ছাত্র-শিক্ষক কেন্দ্র (টিএসসি)",
            "TEACHER-STUDENT CENTRE (TSC)",
            "July 1 – 14, 2024",
            "TSC union block and the TSC Mor roundabout",
            "ষাটের দশকের গোড়ায় গ্রিক স্থপতি কনস্টান্টিনোস ডক্সিয়াডিসের নকশায় নির্মিত টিএসসি: ইটের জালি, দোতলা ছাদ ও ঢাকা হাঁটাপথ ঘেরা ছাত্র সংসদ ভবন, মিলনায়তন ও ক্যাফেটেরিয়া। ২০২৪ সালের জুলাইয়ের প্রথম দিনগুলোতে কোটা সংস্কারের দাবিতে শিক্ষার্থীরা পাশের রাজু ভাস্কর্যের চত্বরে জড়ো হয়ে শাহবাগের দিকে মিছিল করে।",
            "Designed by the Greek architect Constantinos Doxiadis in the early 1960s: a student union block with brick jali screens and a double roof, an auditorium and cafeteria wings linked by covered walkways. In the first days of July 2024, students demanding quota reform assembled on the Raju sculpture roundabout beside TSC before marching towards Shahbagh."
        );
    }

    public static bd.july2024.ui.HistoricalDocumentModal.DocumentRecord getCurzonHallDocument() {
        return new bd.july2024.ui.HistoricalDocumentModal.DocumentRecord(
            "কার্জন হল ও ঐতিহাসিক ঐতিহ্য",
            "CURZON HALL & HISTORICAL LEGACY",
            "July 1 – 14, 2024 / Historical",
            "Curzon Hall, Faculty of Science, Dhaka University",
            "১৯০৪ সালে ভিত্তিপ্রস্তর স্থাপিত এবং ১৯২১ সালে ঢাকা বিশ্ববিদ্যালয়ের জন্মলগ্ন থেকে বিজ্ঞান অনুষদ হিসেবে ব্যবহৃত এই লাল ইটের ভবনটি ইন্দো-ইউরোপীয় স্থাপত্যের এক অনন্য নিদর্শন। ১৯৪৮ সালে এই ভবনের সমাবর্তন কক্ষেই ঢাকা বিশ্ববিদ্যালয়ের ছাত্ররা প্রথম অন্যায়ের বিরুদ্ধে গর্জে উঠেছিল, যা যুগ যুগ ধরে শিক্ষার্থী আন্দোলনের ঐতিহাসিক অনুপ্রেরণা।",
            "Founded in 1904 with distinctive Indo-Saracenic red-brick architecture, Curzon Hall has housed the DU Faculty of Science since 1921. In 1948, Dhaka University students in this hall first raised their defiant voices against injustice, forging a timeless legacy of student resistance."
        );
    }
}
