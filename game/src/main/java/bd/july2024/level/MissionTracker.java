package bd.july2024.level;

import com.jme3.math.Vector3f;
import java.util.ArrayList;
import java.util.List;

/**
 * Tracks the 6 sequential historical objectives for Level 01 (Dhaka University Central Slice).
 */
public class MissionTracker {

    private final List<MissionObjective> objectives = new ArrayList<>();
    private int currentObjectiveIndex = 0;

    public MissionTracker() {
        initLevel01Objectives();
    }

    private void initLevel01Objectives() {
        objectives.add(new MissionObjective(
            "OBJ_01_RAFI",
            "১. রাজু ভাস্কর্যে সহযোদ্ধা রাফির সাথে সাক্ষাৎ করুন",
            "1. Meet your classmate Rafi at Raju Memorial Sculpture",
            "রাজু ভাস্কর্যের পাদদেশে রাফি অপেক্ষা করছে",
            new Vector3f(2.4f, 0f, 3.2f),
            3.5f
        ));

        objectives.add(new MissionObjective(
            "OBJ_02_LIBRARY",
            "২. হাকিম চত্বর ও কেন্দ্রীয় লাইব্রেরি থেকে লিফলেট সংগ্রহ করুন",
            "2. Collect July campaign leaflets from Hakim Chattar",
            "লাইব্রেরির উত্তর দিকের বারান্দায় তানভীরের কাছে যান",
            new Vector3f(-12f, 0f, -22f),
            4.0f
        ));

        objectives.add(new MissionObjective(
            "OBJ_03_TSC",
            "৩. টিএসসি অডিটোরিয়াম প্রাঙ্গণে নুসরাতের কাছে লিফলেট হস্তান্তর করুন",
            "3. Hand over informational leaflets to Nusrat at TSC",
            "টিএসসির প্রজাপতি ছাদের প্রবেশদ্বারে যান",
            new Vector3f(18f, 0f, 15f),
            4.0f
        ));

        objectives.add(new MissionObjective(
            "OBJ_04_PLAQUE",
            "৪. জুলাই অভ্যুত্থানের ঐতিহাসিক তথ্যফলক পরিদর্শন করুন",
            "4. Inspect the July Movement Commemorative Memorial Plaque",
            "চত্বরের স্মারক স্তম্ভের সামনে দাঁড়িয়ে [E] চাপুন",
            new Vector3f(2f, 0f, -6f),
            3.0f
        ));

        objectives.add(new MissionObjective(
            "OBJ_05_RALLY",
            "৫. শান্তিকামী সাধারণ ছাত্র-জনতার জমায়েতে অংশ নিন",
            "5. Join the peaceful student assembly at the roundabout",
            "ভাস্কর্যের সামনের প্রধান সড়কে আসুন",
            new Vector3f(0f, 0f, 0f),
            4.5f
        ));

        objectives.add(new MissionObjective(
            "OBJ_06_COMPLETE",
            "৬. প্রথম অধ্যায় সম্পন্ন: ঐক্য ও ঐতিহাসিক সংকল্প",
            "6. Chapter One Complete: Solidarity and Historical Resolve",
            "ছাত্র-জনতার ঐতিহাসিক অভ্যুত্থানের চেতনা জাগ্রত",
            new Vector3f(0f, 0f, 0f),
            10.0f
        ));
    }

    public MissionObjective getCurrentObjective() {
        if (currentObjectiveIndex < objectives.size()) {
            return objectives.get(currentObjectiveIndex);
        }
        return objectives.get(objectives.size() - 1);
    }

    public boolean advanceObjective() {
        if (currentObjectiveIndex < objectives.size() - 1) {
            objectives.get(currentObjectiveIndex).setCompleted(true);
            currentObjectiveIndex++;
            return true;
        } else if (currentObjectiveIndex == objectives.size() - 1) {
            objectives.get(currentObjectiveIndex).setCompleted(true);
        }
        return false;
    }

    public boolean isAllCompleted() {
        return currentObjectiveIndex >= objectives.size() - 1 &&
               objectives.get(objectives.size() - 1).isCompleted();
    }

    public int getCurrentObjectiveIndex() {
        return currentObjectiveIndex;
    }

    public List<MissionObjective> getAllObjectives() {
        return objectives;
    }
}
