package bd.july2024.level;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Slogans of the 2024 movement as supplied by the project owner (2026-10-26), tagged with the phase
 * of the movement in which they were chanted so that each level shows only the slogans of its own
 * days. The phase attribution is the developer's reading of the documented chronology
 * (docs/historical_timeline.md); only the 14 July chant is individually sourced there. The texts
 * are reproduced as supplied, with no alteration. See docs/REFERENCE_LEDGER.md, section 7.
 */
public final class Slogans {

    public enum Phase {
        /** 1 to 14 July: quota-reform demand, "Bangla Blockade", peaceful campus (Level 01). */
        QUOTA_REFORM,
        /** Night of 14 July onwards: reaction to the "Razakar" remark. */
        AFTER_14_JULY,
        /** 16 July onwards: after the first killings. */
        AFTER_FIRST_DEATHS,
        /** Late July to 4 August: one-point demand and non-cooperation. */
        ONE_POINT_DEMAND,
        /** 5 August and after. */
        AFTER_5_AUGUST
    }

    public static final class Slogan {
        public final String bengali;
        public final String english;
        public final Phase phase;

        Slogan(String bengali, String english, Phase phase) {
            this.bengali = bengali;
            this.english = english;
            this.phase = phase;
        }

        Slogan(String bengali, Phase phase) {
            this(bengali, bengali, phase);
        }
    }

    private static final List<Slogan> ALL;

    static {
        List<Slogan> l = new ArrayList<>();
        // Quota-reform phase (Level 01)
        l.add(new Slogan("কোটা না মেধা? মেধা, মেধা!", "Quota or Merit? Merit! Merit!", Phase.QUOTA_REFORM));
        l.add(new Slogan("আমার সোনার বাংলায়, বৈষম্যের ঠাঁই নাই!", "In our Golden Bengal, there is no place for discrimination!", Phase.QUOTA_REFORM));
        l.add(new Slogan("সারা বাংলায় খবর দে, কোটাপ্রথা কবর দে!", "Spread the word across Bengal, bury the quota system!", Phase.QUOTA_REFORM));
        l.add(new Slogan("কোটাপ্রথা নিপাত যাক, মেধাবীরা মুক্তি পাক!", "Dismantle the quota system, let merit prevail!", Phase.QUOTA_REFORM));
        l.add(new Slogan("জেগেছে রে জেগেছে, ছাত্রসমাজ জেগেছে!", "Awoken, awoken, the student body has awoken!", Phase.QUOTA_REFORM));
        // 14 July night onwards
        l.add(new Slogan("তুমি কে? আমি কে? রাজাকার, রাজাকার! কে বলেছে? কে বলেছে? স্বৈরাচার, স্বৈরাচার!", "Who are you? Who am I? Who dared label us? The autocrat!", Phase.AFTER_14_JULY));
        l.add(new Slogan("চাইলাম অধিকার, হয়ে গেলাম রাজাকার!", "We asked for our rights, and you labeled us traitors!", Phase.AFTER_14_JULY));
        l.add(new Slogan("লেগেছে রে লেগেছে, রক্তে আগুন লেগেছে!", "Ignited, ignited, fire ignites in our blood!", Phase.AFTER_14_JULY));
        // After the first deaths (16 July onwards)
        l.add(new Slogan("তোর কোটা তুই নে, আমার ভাই ফিরিয়ে দে!", "Keep your quotas, give us our brothers back!", Phase.AFTER_FIRST_DEATHS));
        l.add(new Slogan("আমার ভাইয়ের রক্ত, বৃথা যেতে দেব না!", "The blood of our brothers shall not be shed in vain!", Phase.AFTER_FIRST_DEATHS));
        l.add(new Slogan("বুকের ভেতর অনেক ঝড়, বুক পেতেছি গুলি কর!", "A storm rages within our chests; shoot if you dare!", Phase.AFTER_FIRST_DEATHS));
        l.add(new Slogan("আমার ভাই কবরে, খুনি কেন বাইরে?", "Our brothers lie in graves; why do the killers roam free?", Phase.AFTER_FIRST_DEATHS));
        l.add(new Slogan("লাঠি, বোমা, টিয়ারগ্যাস, জবাব দিবে বাংলাদেশ!", "Batons, tear gas, and bullets—Bangladesh will answer!", Phase.AFTER_FIRST_DEATHS));
        l.add(new Slogan("লড়াই লড়াই লড়াই চাই, লড়াই করে বাঁচতে চাই!", "Resistance, resistance, we fight to live free!", Phase.AFTER_FIRST_DEATHS));
        // One-point demand and non-cooperation (late July to 4 August)
        l.add(new Slogan("এক দফা, এক দাবি, স্বৈরাচার তুই কবে যাবি!", "One point, one demand: when will the autocrat step down!", Phase.ONE_POINT_DEMAND));
        l.add(new Slogan("এক দুই তিন চার, শেখ হাসিনা গদি ছাড়!", "One, two, three, four: step down from power!", Phase.ONE_POINT_DEMAND));
        l.add(new Slogan("ঢাকায় আসো জনতা, ছাড়তে হবে ক্ষমতা!", "March to Dhaka, citizens: surrender your power!", Phase.ONE_POINT_DEMAND));
        l.add(new Slogan("গোলামি না আজাদি? আজাদি, আজাদি!", "Subjugation or liberty? Liberty! Liberty!", Phase.ONE_POINT_DEMAND));
        l.add(new Slogan("গোলামি না ইনকিলাব? ইনকিলাব, ইনকিলাব!", "Subjugation or revolution? Revolution! Revolution!", Phase.ONE_POINT_DEMAND));
        l.add(new Slogan("আপস না সংগ্রাম? সংগ্রাম, সংগ্রাম!", "Compromise or struggle? Struggle! Struggle!", Phase.ONE_POINT_DEMAND));
        l.add(new Slogan("লাখো শহীদের দামে কেনা, দেশটা কারও বাপের না!", "Bought with the blood of martyrs, this nation belongs to the people!", Phase.ONE_POINT_DEMAND));
        // 5 August and after
        l.add(new Slogan("পালাইছে রে পালাইছে, শেখ হাসিনা পালাইছে!", "Fled, fled, the autocrat has fled!", Phase.AFTER_5_AUGUST));
        ALL = Collections.unmodifiableList(l);
    }

    private Slogans() {
    }

    public static List<Slogan> all() {
        return ALL;
    }

    public static List<Slogan> forPhase(Phase phase) {
        List<Slogan> out = new ArrayList<>();
        for (Slogan s : ALL) {
            if (s.phase == phase) out.add(s);
        }
        return out;
    }

    /** Level 01 is set in the first days of July; only quota-reform slogans belong on its walls. */
    public static List<Slogan> forLevel01() {
        return forPhase(Phase.QUOTA_REFORM);
    }

    public static String text(Phase phase, int index) {
        List<Slogan> list = forPhase(phase);
        return list.get(index % list.size()).bengali;
    }

    public static String textEnglish(Phase phase, int index) {
        List<Slogan> list = forPhase(phase);
        return list.get(index % list.size()).english;
    }
}
