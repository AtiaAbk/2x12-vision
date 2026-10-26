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
        public final Phase phase;

        Slogan(String bengali, Phase phase) {
            this.bengali = bengali;
            this.phase = phase;
        }
    }

    private static final List<Slogan> ALL;

    static {
        List<Slogan> l = new ArrayList<>();
        // Quota-reform phase (Level 01)
        l.add(new Slogan("কোটা না মেধা? মেধা, মেধা!", Phase.QUOTA_REFORM));
        l.add(new Slogan("আমার সোনার বাংলায়, বৈষম্যের ঠাঁই নাই!", Phase.QUOTA_REFORM));
        l.add(new Slogan("সারা বাংলায় খবর দে, কোটাপ্রথা কবর দে!", Phase.QUOTA_REFORM));
        l.add(new Slogan("কোটাপ্রথা নিপাত যাক, মেধাবীরা মুক্তি পাক!", Phase.QUOTA_REFORM));
        l.add(new Slogan("জেগেছে রে জেগেছে, ছাত্রসমাজ জেগেছে!", Phase.QUOTA_REFORM));
        // 14 July night onwards
        l.add(new Slogan("তুমি কে? আমি কে? রাজাকার, রাজাকার! কে বলেছে? কে বলেছে? স্বৈরাচার, স্বৈরাচার!", Phase.AFTER_14_JULY));
        l.add(new Slogan("চাইলাম অধিকার, হয়ে গেলাম রাজাকার!", Phase.AFTER_14_JULY));
        l.add(new Slogan("লেগেছে রে লেগেছে, রক্তে আগুন লেগেছে!", Phase.AFTER_14_JULY));
        // After the first deaths (16 July onwards)
        l.add(new Slogan("তোর কোটা তুই নে, আমার ভাই ফিরিয়ে দে!", Phase.AFTER_FIRST_DEATHS));
        l.add(new Slogan("আমার ভাইয়ের রক্ত, বৃথা যেতে দেব না!", Phase.AFTER_FIRST_DEATHS));
        l.add(new Slogan("বুকের ভেতর অনেক ঝড়, বুক পেতেছি গুলি কর!", Phase.AFTER_FIRST_DEATHS));
        l.add(new Slogan("আমার ভাই কবরে, খুনি কেন বাইরে?", Phase.AFTER_FIRST_DEATHS));
        l.add(new Slogan("লাঠি, বোমা, টিয়ারগ্যাস, জবাব দিবে বাংলাদেশ!", Phase.AFTER_FIRST_DEATHS));
        l.add(new Slogan("লড়াই লড়াই লড়াই চাই, লড়াই করে বাঁচতে চাই!", Phase.AFTER_FIRST_DEATHS));
        // One-point demand and non-cooperation (late July to 4 August)
        l.add(new Slogan("এক দফা, এক দাবি, স্বৈরাচার তুই কবে যাবি!", Phase.ONE_POINT_DEMAND));
        l.add(new Slogan("এক দুই তিন চার, শেখ হাসিনা গদি ছাড়!", Phase.ONE_POINT_DEMAND));
        l.add(new Slogan("ঢাকায় আসো জনতা, ছাড়তে হবে ক্ষমতা!", Phase.ONE_POINT_DEMAND));
        l.add(new Slogan("গোলামি না আজাদি? আজাদি, আজাদি!", Phase.ONE_POINT_DEMAND));
        l.add(new Slogan("গোলামি না ইনকিলাব? ইনকিলাব, ইনকিলাব!", Phase.ONE_POINT_DEMAND));
        l.add(new Slogan("আপস না সংগ্রাম? সংগ্রাম, সংগ্রাম!", Phase.ONE_POINT_DEMAND));
        l.add(new Slogan("লাখো শহীদের দামে কেনা, দেশটা কারও বাপের না!", Phase.ONE_POINT_DEMAND));
        // 5 August and after
        l.add(new Slogan("পালাইছে রে পালাইছে, শেখ হাসিনা পালাইছে!", Phase.AFTER_5_AUGUST));
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
}
