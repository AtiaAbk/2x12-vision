package bd.spark36.level;

/**
 * Data-driven dialogue repository containing authentic Bengali and English subtitles.
 * Replaces hardcoded strings and phonetic Banglish with verified cultural phrasing.
 */
public class DialogueRepository {

    public static class DialogueLine {
        private final String speaker;
        private final String speakerBengali;
        private final String textBengali;
        private final String textEnglish;

        public DialogueLine(String speaker, String speakerBengali, String textBengali, String textEnglish) {
            this.speaker = speaker;
            this.speakerBengali = speakerBengali;
            this.textBengali = textBengali;
            this.textEnglish = textEnglish;
        }

        public String getSpeaker() {
            return speaker;
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

    /**
     * Arena Combat Encounter Dialogue: Helmeted Aggressor's challenge.
     * Bengali: "ক্যাম্পাস খালি করো! এখনই এখান থেকে চলে যাও!"
     * English: "Leave this campus immediately, or face the consequences!"
     */
    public static final DialogueLine AGGRESSOR_CHALLENGE = new DialogueLine(
        "HELMETED AGGRESSOR",
        "হেলমেট পরা আক্রমণকারী",
        "ক্যাম্পাস খালি করো! এখনই এখান থেকে চলে যাও!",
        "Leave this campus immediately, or face the consequences!"
    );

    /**
     * Peaceful Level 1 Encounter Dialogue: Campus Security / Warden's friendly greeting.
     * Bengali: "কার্জন হলে স্বাগতম। ক্যাম্পাসের শান্তিশৃঙ্খলা বজায় রাখুন।"
     * English: "Welcome to Curzon Hall. Please maintain peace on campus."
     */
    public static final DialogueLine CAMPUS_SECURITY_PEACEFUL = new DialogueLine(
        "CAMPUS SECURITY",
        "ক্যাম্পাস নিরাপত্তা কর্মী",
        "কার্জন হলে স্বাগতম। ক্যাম্পাসের শান্তিশৃঙ্খলা বজায় রাখুন।",
        "Welcome to Curzon Hall. Please maintain peace on campus."
    );

    // =========================================================================
    // LEVEL-AWARE STUDENT CROWD DIALOGUE (Historical Bengali & English Phrasing)
    // =========================================================================

    // Level 1: July 1 – 14 (Academic Life & Early Quota Reform Awareness)
    public static final DialogueLine[] LEVEL_1_STUDENT_LINES = {
        new DialogueLine("DU PHYSICS STUDENT", "পদার্থবিজ্ঞান বিভাগের শিক্ষার্থী",
            "দাবি আমাদের একটাই — মেধার যথাযথ মূল্যায়ন চাই, বৈষম্যহীন ভবিষ্যৎ চাই।",
            "Our demand is simple: merit-based evaluation and an equitable future."),
        new DialogueLine("LIBRARY SCHOLAR", "কেন্দ্রীয় লাইব্রেরির গবেষক",
            "লাইব্রেরিতে আজ খুব ভিড়। সাধারণ শিক্ষার্থীরা সবাই ন্যায্য অধিকার নিয়ে সচেতন হচ্ছে।",
            "The library is crowded today. General students are waking up to their rights."),
        new DialogueLine("CURZON RESIDENT", "কার্জন হল আবাসিক শিক্ষার্থী",
            "কার্জন হলের লাল ইটের প্রাঙ্গণে শতাব্দীর ঐতিহ্য। আমরা শান্তিপূর্ণ শিক্ষাঙ্গন চাই।",
            "Centuries of heritage live in Curzon's red bricks. We stand for a peaceful campus.")
    };

    // Level 2: July 15 – 16 (Student Resistance & Solidarity)
    public static final DialogueLine[] LEVEL_2_STUDENT_LINES = {
        new DialogueLine("STUDENT ACTIVIST", "আন্দোলনরত সাধারণ শিক্ষার্থী",
            "হলগুলো থেকে সব সাধারণ শিক্ষার্থীরা রাজপথে নেমে আসছে! কোনো হুমকি আমাদের থামাতে পারবে না।",
            "Students from every residential hall are pouring into the streets! No threats will stop us."),
        new DialogueLine("ARTS FACULTY STUDENT", "কলা অনুষদের শিক্ষার্থী",
            "রাজু ভাস্কর্যের সামনে চলো! সেখানে সবাই হাতে হাত রেখে মানবপ্রাচীর গড়ে তুলছে।",
            "Head to the Raju Memorial Sculpture! Everyone is forming a united human chain there.")
    };

    // Level 3: July 18 (Escalation, Tear Gas & Mutual Aid)
    public static final DialogueLine[] LEVEL_3_STUDENT_LINES = {
        new DialogueLine("FIRST AID VOLUNTEER", "প্রাথমিক চিকিৎসা দলের স্বেচ্ছাসেবক",
            "টিয়ারগ্যাসের ধোঁয়ায় চোখ জ্বলছে! বোতলের পানি নাও, মুখ ধুয়ে পাশে নিরাপদ ছায়ায় চলো।",
            "Tear gas in the air! Take fresh water, rinse your eyes, and move to safe cover."),
        new DialogueLine("FRONT-LINE STUDENT", "সংগ্রামী শিক্ষার্থী",
            "আমাদের ভ্রাতৃত্ব কেউ ভাঙতে পারবে না। আহত ভাই-বোনদের নিরাপদে সরিয়ে নিতে সাহায্য করো!",
            "Our brotherhood cannot be broken. Help move the injured to safety!")
    };

    // Level 4: July 19 – 20 (Internet Blackout & Curfew Vigilance)
    public static final DialogueLine[] LEVEL_4_STUDENT_LINES = {
        new DialogueLine("CAMPUS VIGIL STUDENT", "নিশাচর পাহারাদার শিক্ষার্থী",
            "ইন্টারনেট বন্ধ করে আমাদের স্তব্ধ করতে পারবে না। প্রতিটি গলিতে আমরা ঐক্যবদ্ধ আছি।",
            "They cut the internet but cannot silence the truth. We remain united across every corridor."),
        new DialogueLine("RESISTANCE COURIER", "যোগাযোগ সমন্বয়ক শিক্ষার্থী",
            "সাবধানে ছায়ায় ছায়ায় চলো। সার্চলাইটের আলো থেকে দূরে থেকে সহপাঠীদের খবর নাও।",
            "Stay in the shadows. Keep clear of the searchlights and check on your peers.")
    };

    // Level 5: August 3 – 4 (Mass Mobilization — 1-Point Demand)
    public static final DialogueLine[] LEVEL_5_STUDENT_LINES = {
        new DialogueLine("STUDENT CO-ORDINATOR", "কেন্দ্রীয় সমন্বয়ক শিক্ষার্থী",
            "এক দফা এক দাবি! পুরো জাতি আজ ঢাকার রাজপথে আমাদের পাশে দাঁড়িয়েছে!",
            "One point, one demand! The entire nation now marches alongside us on Dhaka's streets!"),
        new DialogueLine("MARCHING STUDENT", "মিছিলের সম্মুখ সারির শিক্ষার্থী",
            "শহীদ মিনার থেকে গণভবন — জনতার এই বাঁধভাঙা জোয়ার কেউ রুখতে পারবে না!",
            "From Shaheed Minar onward — nothing can hold back this unstoppable wave of the people!")
    };

    // Level 6: August 5 (Victory & Historic Epilogue — 36 July)
    public static final DialogueLine[] LEVEL_6_STUDENT_LINES = {
        new DialogueLine("CELEBRATING STUDENT", "বিজয়ী শিক্ষার্থী",
            "৩৬ জুলাইয়ের নতুন সূর্য উদিত হয়েছে! এবার দেশকে নতুন করে ন্যায় আর সাম্যের ভিত্তিতে গড়তে হবে।",
            "The new sun of 36 July has risen! Now we rebuild this nation upon justice and equity."),
        new DialogueLine("MEMORIAL GUARDIAN", "শহীদ স্মৃতি সংরক্ষক",
            "আমাদের শহীদ ভাই-বোনদের রক্ত আমরা বৃথা যেতে দেব না। তাদের আত্মত্যাগ চিরকাল বেঁচে থাকবে।",
            "We will never let our fallen peers' sacrifices be forgotten. Their memory lives forever.")
    };

    /**
     * Retrieves an authentic student dialogue line matching the active level and seed.
     */
    public static DialogueLine getStudentDialogue(int levelNumber, int seed) {
        DialogueLine[] pool;
        switch (levelNumber) {
            case 2:  pool = LEVEL_2_STUDENT_LINES; break;
            case 3:  pool = LEVEL_3_STUDENT_LINES; break;
            case 4:  pool = LEVEL_4_STUDENT_LINES; break;
            case 5:  pool = LEVEL_5_STUDENT_LINES; break;
            case 6:  pool = LEVEL_6_STUDENT_LINES; break;
            case 1:
            default: pool = LEVEL_1_STUDENT_LINES; break;
        }
        int index = Math.abs(seed) % pool.length;
        return pool[index];
    }

    /**
     * Constructs a rich multi-line DialogueSequence for active campus dialogue interactions.
     */
    public static bd.spark36.dialogue.DialogueSequence getStudentDialogueSequence(int levelNumber, int seed) {
        String seqId = "student_dialogue_lvl" + levelNumber + "_s" + seed;
        switch (levelNumber) {
            case 2:
                return new bd.spark36.dialogue.DialogueSequence(seqId, "CAMPUS RESISTANCE DIALOGUE",
                    LEVEL_2_STUDENT_LINES[0],
                    LEVEL_2_STUDENT_LINES[1],
                    new DialogueLine("ARTS FACULTY STUDENT", "কলা অনুষদের শিক্ষার্থী",
                        "হাতে হাত রাখো, কোনো উসকানির মুখে পিছিয়ে যাওয়া যাবে না!",
                        "Hold hands firmly; we must not retreat in the face of provocation!")
                );
            case 3:
                return new bd.spark36.dialogue.DialogueSequence(seqId, "MEDICAL MUTUAL AID",
                    LEVEL_3_STUDENT_LINES[0],
                    LEVEL_3_STUDENT_LINES[1],
                    new DialogueLine("MEDICAL VOLUNTEER", "চিকিৎসা দলের সমন্বয়ক",
                        "মেডিক্যাল ক্যাম্প আর্টস গেটে প্রস্তুত আছে, দ্রুত সবাইকে পথ দেখাও।",
                        "The emergency medical station is set up near Arts Gate, lead the way.")
                );
            case 4:
                return new bd.spark36.dialogue.DialogueSequence(seqId, "CURFEW BRIEFING",
                    LEVEL_4_STUDENT_LINES[0],
                    LEVEL_4_STUDENT_LINES[1],
                    new DialogueLine("RESISTANCE COURIER", "যোগাযোগ সমন্বয়ক শিক্ষার্থী",
                        "দেয়ালগুলোতে প্রতিবাদী গ্রাফিতি আঁকা হচ্ছে — সত্যকে থামানো যাবে না।",
                        "Protest graffiti is rising on every wall — the truth cannot be suppressed.")
                );
            case 5:
                return new bd.spark36.dialogue.DialogueSequence(seqId, "MASS MOBILIZATION RALLY",
                    LEVEL_5_STUDENT_LINES[0],
                    LEVEL_5_STUDENT_LINES[1],
                    new DialogueLine("MARCHING STUDENT", "মিছিলের সম্মুখ সারির শিক্ষার্থী",
                        "লাখো ছাত্র-জনতার স্লোগানে আজ প্রকম্পিত সারা বাংলাদেশ!",
                        "Bangladesh resounds with the united chants of millions of students and citizens!")
                );
            case 6:
                return new bd.spark36.dialogue.DialogueSequence(seqId, "HISTORIC VICTORY EPILOGUE",
                    LEVEL_6_STUDENT_LINES[0],
                    LEVEL_6_STUDENT_LINES[1],
                    new DialogueLine("MEMORIAL GUARDIAN", "শহীদ স্মৃতি সংরক্ষক",
                        "এই ক্যাম্পাস থেকেই শুরু হয়েছিল নবজাগরণ। বিজয় আমাদের সবার।",
                        "From this very campus the awakening began. This victory belongs to all of us.")
                );
            case 1:
            default:
                return new bd.spark36.dialogue.DialogueSequence(seqId, "STUDENT ASSEMBLY DISCUSSION",
                    LEVEL_1_STUDENT_LINES[0],
                    new DialogueLine("DU PHYSICS STUDENT", "পদার্থবিজ্ঞান বিভাগের শিক্ষার্থী",
                        "আমরা কোনো বিশৃঙ্খলা চাই না, কেবল আমাদের সাংবিধানিক অধিকার চাই।",
                        "We desire no chaos, only our constitutional right to equal opportunity."),
                    LEVEL_1_STUDENT_LINES[2]
                );
        }
    }

    // =========================================================================
    // LEVEL 1 CORE CHARACTERS: ARIF'S CLOSE FRIENDS (RAFI, TANVIR, NUSRAT)
    // =========================================================================

    public static bd.spark36.dialogue.DialogueSequence createRafiDialogueSequence() {
        return new bd.spark36.dialogue.DialogueSequence("seq_friend_rafi", "MEET RAFI AT RAJU SCULPTURE",
            new DialogueLine("RAFI", "রাফি",
                "আরিফ, এখানে এসো! রাজু ভাস্কর্যের চারপাশে সবাই হাত ধরে দাঁড়াচ্ছে। মেধার সমতা আর ন্যায়ের দাবিতে সাধারণ শিক্ষার্থীরা আজ এক।",
                "Arif, over here! Everyone is holding hands around the Raju Sculpture. General students are uniting today for merit and justice."),
            new DialogueLine("ARIF", "আরিফ",
                "আমিও বন্ধুদের সঙ্গে একাত্মতা জানাতে এসেছি। ১৯৯২ সালে সন্ত্রাসের বিরুদ্ধে শহীদ রাজু ভাইয়ের আত্মত্যাগের প্রতীক এই ভাস্কর্য — আমাদের আন্দোলনও সম্পূর্ণ অহিংস ও সাংবিধানিক।",
                "I came to stand with our peers. This sculpture commemorates martyr Raju's sacrifice against violence in 1992 — our movement is completely peaceful and constitutional."),
            new DialogueLine("RAFI", "রাফি",
                "তানভীর কেন্দ্রীয় লাইব্রেরি আর হাকিম চত্বরে অধ্যয়ন দলের সমন্বয় করছে। তুমি একবার লাইব্রেরি চত্বর ঘুরে আসো, তারপর টিএসসিতে নুসরাতের সঙ্গে দেখা করব।",
                "Tanvir is coordinating with study circles at the Central Library and Hakim Chattar. Go check the library plaza, then we will regroup with Nusrat at TSC.")
        );
    }

    public static bd.spark36.dialogue.DialogueSequence createTanvirDialogueSequence() {
        return new bd.spark36.dialogue.DialogueSequence("seq_friend_tanvir", "DISCUSS REFORM WITH TANVIR AT LIBRARY",
            new DialogueLine("TANVIR", "তানভীর",
                "আরিফ! কেন্দ্রীয় লাইব্রেরির সামনে আমরা সংবিধানের সমসুযোগ সংক্রান্ত ধারাগুলো নিয়ে শিক্ষার্থীদের সাথে আলোচনা করছি।",
                "Arif! Right here in front of the Central Library, we are discussing constitutional equality and quota reform principles with fellow students."),
            new DialogueLine("ARIF", "আরিফ",
                "স্থপতি মাজহারুল ইসলামের ১৯৫৩ সালের এই আধুনিক স্থাপত্য মুক্তচিন্তার প্রতীক। লাইব্রেরির প্রতিটি সিঁড়ি আজ ছাত্রদের জাগ্রত বিবেকের সাক্ষী।",
                "Architect Muzharul Islam's 1953 modernist design is a symbol of free thought. Every step of this library bears witness to student conscience today."),
            new DialogueLine("TANVIR", "তানভীর",
                "ঠিক তাই। নুসরাত টিএসসির ক্যাফেটেরিয়া চত্বরে আমাদের পরবর্তী শান্তিপূর্ণ কর্মসূচির প্রস্তুতি নিচ্ছে। তুমি টিএসসিতে গিয়ে ওর সাথে দেখা করো।",
                "Exactly. Nusrat is at the TSC cafeteria terrace preparing our next peaceful assembly statement. Head over to TSC to regroup with her.")
        );
    }

    public static bd.spark36.dialogue.DialogueSequence createNusratDialogueSequence() {
        return new bd.spark36.dialogue.DialogueSequence("seq_friend_nusrat", "REGROUP WITH NUSRAT AT TSC",
            new DialogueLine("NUSRAT", "নুসরাত",
                "আরিফ, স্বাগতম টিএসসিতে! ১৯৬১ সালে স্থপতি ডক্সিয়াডিসের তৈরি এই প্রাঙ্গণ সবসময়ই দেশের সংকটময় মুহূর্তে ছাত্রদের নিরাপদ মিলনমেলা।",
                "Arif, welcome to TSC! Designed by architect Doxiadis in 1961, this center has always been the safe haven for students in national turning points."),
            new DialogueLine("ARIF", "আরিফ",
                "আমরা কোনো বিশৃঙ্খলা বা সহিংসতা চাই না। মেধার মূল্যায়ন ও সমতার ভিত্তিতে দেশের ভবিষ্যত গড়াই আমাদের একমাত্র লক্ষ্য।",
                "We seek no chaos or violence. Building a future grounded in merit, equality, and justice is our sole collective goal."),
            new DialogueLine("NUSRAT", "নুসরাত",
                "আমাদের শান্তিপূর্ণ ঐক্যের বার্তা পুরো ক্যাম্পাসে ছড়িয়ে পড়েছে। চলো, টিএসসির ঐতিহাসিক ঘোষণাপত্র পরিদর্শন করে আজকের সংহতি কর্মসূচি সমাপ্ত করি।",
                "Our message of peaceful unity has spread across the entire campus. Let us inspect the historic solidarity declaration and conclude today's assembly.")
        );
    }
}
