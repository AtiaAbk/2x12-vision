package bd.july2024.world;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import net.mgsx.gltf.scene3d.attributes.PBRColorAttribute;
import net.mgsx.gltf.scene3d.attributes.PBRFloatAttribute;

/**
 * Historical archival monuments and commemorative beacons for Project July 2024.
 * Places 3D archival beacons with detailed historical descriptions.
 */
public class JulyMemorials implements Disposable {

    public static class MemorialEntry {
        public final int id;
        public final String titleBengali;
        public final String titleEnglish;
        public final String date;
        public final String location;
        public final String descriptionBengali;
        public final String descriptionEnglish;
        public final Vector3 position;
        public boolean inspected = false;

        public MemorialEntry(int id, String titleBengali, String titleEnglish, String date,
                             String location, String descriptionBengali, String descriptionEnglish, Vector3 position) {
            this.id = id;
            this.titleBengali = titleBengali;
            this.titleEnglish = titleEnglish;
            this.date = date;
            this.location = location;
            this.descriptionBengali = descriptionBengali;
            this.descriptionEnglish = descriptionEnglish;
            this.position = position;
        }
    }

    private final Array<MemorialEntry> entries = new Array<>();
    private final Array<Model> models = new Array<>();
    private final Array<ModelInstance> instances = new Array<>();
    private final Array<ModelInstance> beaconInstances = new Array<>();

    private float pulseTime = 0f;

    public JulyMemorials() {
        initEntries();
        createMeshes();
    }

    private void initEntries() {
        entries.add(new MemorialEntry(
            1,
            "সন্ত্রাসবিরোধী রাজু স্মারক ভাস্কর্য",
            "ANTI-TERRORISM RAJU MEMORIAL",
            "July 1 – 14, 2024",
            "TSC Raju Sculpture Roundabout",
            "১৯৯২ সালের ১৩ মার্চ ক্যাম্পাসে সন্ত্রাসবিরোধী গণতান্ত্রিক মিছিলে শহীদ মঈন হোসেন রাজুর স্মৃতিতে এই ভাস্কর্য নির্মিত হয়। ২০২৪ সালের জুলাইয়ে কোটা সংস্কার ও মেধার মূল্যায়নের দাবিতে শিক্ষার্থীরা এই ভাস্কর্যের চারপাশেই শান্তিপূর্ণ ঐক্যের প্রাচীর গড়ে তোলে।",
            "Erected in memory of martyr Moin Hossain Raju, killed during an anti-terrorism democratic march on March 13, 1992. In July 2024, students locked arms here to form a peaceful wall of solidarity demanding equality and merit-based governance.",
            new Vector3(44f, 0f, 42f)
        ));

        entries.add(new MemorialEntry(
            2,
            "ঢাকা বিশ্ববিদ্যালয় কেন্দ্রীয় লাইব্রেরি ও হাকিম চত্বর",
            "CENTRAL LIBRARY & HAKIM CHATTAR",
            "July 7 – 18, 2024",
            "Central Library Steps & Pilotis Colonnade",
            "স্থপতি মাজহারুল ইসলামের ১৯৫৩ সালের আধুনিক স্থাপত্যে নির্মিত এই লাইব্রেরি মুক্তচিন্তা ও বুদ্ধিবৃত্তিক প্রতিরোধের কেন্দ্রবিন্দু। আন্দোলনের সময় হাকিম চত্বর ও লাইব্রেরির উন্মুক্ত চত্বরে শিক্ষার্থীরা চিকিৎসাসেবা, খাদ্য বিতরণ ও ব্ল্যাকআউট চলাকালে যোগাযোগ কেন্দ্র পরিচালনা করে।",
            "Designed by pioneer architect Muzharul Islam in 1953, this regional modernist landmark became the nexus of intellectual solidarity. Students established study circles, emergency aid, and dispatch centers beneath its iconic open pilotis.",
            new Vector3(-58f, 0f, 36f)
        ));

        entries.add(new MemorialEntry(
            3,
            "টিএসসি ও ঐতিহাসিক জুলাই সংহতি ঘোষণা",
            "TEACHER-STUDENT CENTRE (TSC)",
            "August 5, 2024 (36 July)",
            "TSC Main Plaza & Modernist Pavilion",
            "১৯৬১ সালে বিশ্বখ্যাত স্থপতি কনস্টান্টিনোস ডক্সিয়াডিসের মাস্টারপ্ল্যানে নির্মিত ছাত্র-শিক্ষক কেন্দ্র (টিএসসি) বাঙালির প্রতিটি জাতীয় গণতান্ত্রিক উত্তরণের সূতিকাগার। ২০২৪ সালের ৫ আগস্ট এই প্রাঙ্গণেই ঘোষিত হয় ঐতিহাসিক বিজয়, যাকে ছাত্র-জনতা নাম দেয় '৩৬ জুলাই'।",
            "Constructed under the master plan of architect Constantinos Doxiadis in 1961, the Teacher-Student Centre has sheltered student conscience through every turning point. Here, the historic culmination was proclaimed as '36 July' — rewriting time for justice.",
            new Vector3(66f, 0f, 20f)
        ));
    }

    private void createMeshes() {
        ModelBuilder mb = new ModelBuilder();
        long attr = Usage.Position | Usage.Normal;

        Color pedestalCol = new Color(0.24f, 0.25f, 0.28f, 1f);
        Color beaconCol = new Color(0.95f, 0.78f, 0.22f, 1f);

        Material pedMat = new Material(
            ColorAttribute.createDiffuse(pedestalCol),
            PBRColorAttribute.createBaseColorFactor(pedestalCol),
            PBRFloatAttribute.createMetallic(0.1f),
            PBRFloatAttribute.createRoughness(0.80f)
        );

        Material beaconMat = new Material(
            ColorAttribute.createDiffuse(beaconCol),
            PBRColorAttribute.createBaseColorFactor(beaconCol),
            PBRFloatAttribute.createMetallic(0.0f),
            PBRFloatAttribute.createRoughness(0.30f)
        );

        Model pedModel = mb.createBox(1.2f, 0.9f, 1.2f, pedMat, attr);
        Model beaconModel = mb.createCylinder(0.25f, 2.8f, 0.25f, 12, beaconMat, attr);
        Model plaqueModel = mb.createBox(0.8f, 0.5f, 0.04f, beaconMat, attr);

        models.addAll(pedModel, beaconModel, plaqueModel);

        for (MemorialEntry entry : entries) {
            ModelInstance pedInst = new ModelInstance(pedModel);
            pedInst.transform.setTranslation(entry.position.x, 0.45f, entry.position.z);
            instances.add(pedInst);

            ModelInstance bInst = new ModelInstance(beaconModel);
            bInst.transform.setTranslation(entry.position.x, 2.3f, entry.position.z);
            beaconInstances.add(bInst);

            ModelInstance plInst = new ModelInstance(plaqueModel);
            plInst.transform.setTranslation(entry.position.x, 0.95f, entry.position.z - 0.58f);
            instances.add(plInst);
        }
    }

    public void update(float delta) {
        pulseTime += delta * 3.0f;
        float pulse = 0.85f + 0.15f * MathUtils.sin(pulseTime);

        for (int i = 0; i < beaconInstances.size; i++) {
            ModelInstance b = beaconInstances.get(i);
            MemorialEntry entry = entries.get(i);
            if (!entry.inspected) {
                b.transform.setToTranslation(entry.position.x, 2.2f + 0.15f * MathUtils.sin(pulseTime * 0.7f), entry.position.z);
                b.transform.scl(pulse, 1.0f + 0.1f * pulse, pulse);
            }
        }
    }

    public void render(ModelBatch batch, Environment env) {
        for (ModelInstance mi : instances) {
            batch.render(mi, env);
        }
        for (int i = 0; i < beaconInstances.size; i++) {
            if (!entries.get(i).inspected) {
                batch.render(beaconInstances.get(i), env);
            }
        }
    }

    public Array<MemorialEntry> getEntries() {
        return entries;
    }

    public MemorialEntry getEntryById(int id) {
        for (MemorialEntry e : entries) {
            if (e.id == id) return e;
        }
        return null;
    }

    public boolean isAllInspected() {
        for (MemorialEntry e : entries) {
            if (!e.inspected) return false;
        }
        return true;
    }

    public int getInspectedCount() {
        int count = 0;
        for (MemorialEntry e : entries) {
            if (e.inspected) count++;
        }
        return count;
    }

    @Override
    public void dispose() {
        for (Model m : models) {
            m.dispose();
        }
        models.clear();
        instances.clear();
        beaconInstances.clear();
    }
}
