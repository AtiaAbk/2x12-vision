package bd.july2024.dialogue;

/**
 * Historical dialogue repository for Project July 2024.
 * Contains authentic student dialogues in Bengali Unicode with English translations.
 */
public class HistoricalDialogues {

    /**
     * Objective 1: Rafi at Anti-Terrorism Raju Memorial Sculpture roundabout.
     */
    public static DialogueSequence createRafiDialogue() {
        return new DialogueSequence("seq_rafi_raju", "MEET RAFI AT RAJU SCULPTURE",
            new DialogueLine("RAFI", "রাফি",
                "আরিফ, এখানে এসো! রাজু ভাস্কর্যের চারপাশে সাধারণ শিক্ষার্থীরা হাত ধরে দাঁড়াচ্ছে। মেধার সমতা আর ন্যায়ের দাবিতে আমরা সবাই আজ এক।",
                "Arif, over here! General students are linking hands around Raju Sculpture. We stand united today demanding merit and justice."),
            new DialogueLine("ARIF", "আরিফ",
                "আমিও বন্ধুদের সঙ্গে একাত্মতা জানাতে এসেছি। ১৯৯২ সালে ক্যাম্পাসে সন্ত্রাসের বিরুদ্ধে শহীদ রাজু ভাইয়ের আত্মত্যাগের প্রতীক এই ভাস্কর্য — আমাদের আন্দোলনও সম্পূর্ণ অহিংস ও সাংবিধানিক।",
                "I came to stand with our peers. This sculpture commemorates martyr Raju's sacrifice against campus violence in 1992 — our movement is completely peaceful and constitutional."),
            new DialogueLine("RAFI", "রাফি",
                "তানভীর কেন্দ্রীয় লাইব্রেরি আর হাকিম চত্বরে অধ্যয়ন দলের সমন্বয় করছে। রাজুর পাদদেশে শ্রদ্ধা জানিয়ে তুমি একবার লাইব্রেরির দিকে যাও, তারপর টিএসসিতে নুসরাতের সঙ্গে দেখা করব।",
                "Tanvir is coordinating with study circles at the Central Library and Hakim Chattar. Pay respects at Raju's pedestal and visit the library, then we will regroup with Nusrat at TSC.")
        );
    }

    /**
     * Objective 3: Tanvir at Dhaka University Central Library steps / Hakim Chattar.
     */
    public static DialogueSequence createTanvirDialogue() {
        return new DialogueSequence("seq_tanvir_library", "DISCUSS REFORM WITH TANVIR",
            new DialogueLine("TANVIR", "তানভীর",
                "আরিফ! কেন্দ্রীয় লাইব্রেরির সামনে আমরা সংবিধানের সমসুযোগ ও কোটা সংস্কার সংক্রান্ত মূল ধারাগুলো নিয়ে শিক্ষার্থীদের সাথে আলোচনা করছি।",
                "Arif! Right here before the Central Library, we are discussing constitutional equality and quota reform principles with fellow students."),
            new DialogueLine("ARIF", "আরিফ",
                "স্থপতি মাজহারুল ইসলামের ১৯৫৩ সালের এই আধুনিক স্থাপত্য মুক্তচিন্তার প্রতীক। লাইব্রেরির প্রতিটি সিঁড়ি আজ ছাত্রদের জাগ্রত বিবেকের সাক্ষী।",
                "Architect Muzharul Islam's 1953 modernist design is a symbol of free thought. Every step of this library bears witness to student conscience today."),
            new DialogueLine("TANVIR", "তানভীর",
                "ঠিক তাই। নুসরাত টিএসসির ক্যাফেটেরিয়া চত্বরে আমাদের পরবর্তী শান্তিপূর্ণ সমাবেশের প্রস্তুতি নিচ্ছে। তুমি লাইব্রেরির নথি দেখে টিএসসিতে গিয়ে ওর সাথে দেখা করো।",
                "Exactly. Nusrat is at the TSC cafeteria terrace preparing our next peaceful assembly statement. Inspect the library archives and head to TSC to regroup with her.")
        );
    }

    /**
     * Objective 5: Nusrat at Teacher-Student Centre (TSC) cafeteria terrace.
     */
    public static DialogueSequence createNusratDialogue() {
        return new DialogueSequence("seq_nusrat_tsc", "REGROUP WITH NUSRAT AT TSC",
            new DialogueLine("NUSRAT", "নুসরাত",
                "আরিফ, স্বাগতম টিএসসিতে! ১৯৬১ সালে স্থপতি ডক্সিয়াডিসের তৈরি এই প্রাঙ্গণ সবসময়ই দেশের সংকটময় মুহূর্তে ছাত্রদের নিরাপদ মিলনমেলা।",
                "Arif, welcome to TSC! Designed by architect Doxiadis in 1961, this center has always been the safe haven for students in national turning points."),
            new DialogueLine("ARIF", "আরিফ",
                "আমরা কোনো বিশৃঙ্খলা বা সহিংসতা চাই না। মেধার মূল্যায়ন ও সমতার ভিত্তিতে দেশের ভবিষ্যৎ গড়াই আমাদের একমাত্র লক্ষ্য।",
                "We seek no chaos or violence. Building a future grounded in merit, equality, and justice is our sole collective goal."),
            new DialogueLine("NUSRAT", "নুসরাত",
                "আমাদের শান্তিপূর্ণ ঐক্যের বার্তা পুরো ক্যাম্পাসে ছড়িয়ে পড়েছে। চলো, টিএসসির ঐতিহাসিক ঘোষণাপত্র পরিদর্শন করে আজকের সংহতি কর্মসূচি সম্পন্ন করি।",
                "Our message of peaceful unity has spread across the entire campus. Let us inspect the historic solidarity declaration and conclude today's assembly.")
        );
    }
}
