package bd.july2024.level;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import bd.july2024.audio.SoundManager;
import bd.july2024.character.CharacterModel;
import bd.july2024.character.CompanionNPC;
import bd.july2024.character.PlayerController;
import bd.july2024.dialogue.DialogueCallback;
import bd.july2024.dialogue.HistoricalDialogues;
import bd.july2024.hud.ProjectJulyHUD;
import bd.july2024.world.CampusWorld;
import bd.july2024.world.JulyMemorials;
import bd.july2024.world.JulyMemorials.MemorialEntry;
import bd.july2024.world.TextureFactory;

/**
 * Level 01: "Where It All Began" (যেখানে সব শুরু)
 * Period: July 1 – 14, 2024.
 * Setting: Dhaka University Central Hub (Raju Memorial Sculpture, Central Library & Hakim Chattar, TSC).
 * Narrative: Peaceful student assembly demanding quota reform and merit-based equality.
 */
public class Level01DhakaUniversity implements Disposable {

    private final CampusWorld world;
    private final JulyMemorials memorials;
    private final CharacterModel characterModel;

    private CompanionNPC friendRafi;
    private CompanionNPC friendTanvir;
    private CompanionNPC friendNusrat;

    private final MissionTracker missionTracker = new MissionTracker(true);

    private static final String[] CHECKLIST = {
        "1. Meet Rafi at Raju Sculpture (রাজু ভাস্কর্যে রাফির সাথে সাক্ষাৎ)",
        "2. Inspect Raju Memorial Plaque (রাজু স্মারক স্তম্ভ পরিদর্শন)",
        "3. Discuss Reform with Tanvir (লাইব্রেরির সামনে তানভীরের সাথে আলোচনা)",
        "4. Inspect Central Library Archives (কেন্দ্রীয় লাইব্রেরির আর্কাইভ পরিদর্শন)",
        "5. Regroup with Nusrat at TSC Terrace (টিএসসি চত্বরে নুসরাতের সাথে বৈঠক)",
        "6. Inspect TSC Solidarity Declaration (টিএসসির সংহতি ঘোষণাপত্র পাঠ)"
    };

    public Level01DhakaUniversity(TextureFactory textures) {
        this.world = new CampusWorld(textures);
        this.memorials = new JulyMemorials();
        this.characterModel = new CharacterModel(textures);

        initCompanions();
        initObjectives();
    }

    private void initCompanions() {
        // 1. Rafi at Raju Memorial roundabout approach (X = 44.0, Z = 36.0, facing North: 180 deg)
        friendRafi = new CompanionNPC("npc_rafi", "Rafi (রাফি)", "Classmate & Friend",
            new Vector3(44.0f, 0f, 36.0f), 180.0f);
        friendRafi.setDialogueSequence(HistoricalDialogues.createRafiDialogue().setCallback(new DialogueCallback() {
            @Override
            public void onDialogueComplete(String sequenceId) {
                missionTracker.completeObjective("m_rafi");
            }
        }));

        // 2. Tanvir at Central Library Steps (X = -54.0, Z = 40.0, facing East: 90 deg)
        friendTanvir = new CompanionNPC("npc_tanvir", "Tanvir (তানভীর)", "Study Partner & Friend",
            new Vector3(-54.0f, 0f, 40.0f), 90.0f);
        friendTanvir.setDialogueSequence(HistoricalDialogues.createTanvirDialogue().setCallback(new DialogueCallback() {
            @Override
            public void onDialogueComplete(String sequenceId) {
                missionTracker.completeObjective("m_tanvir");
            }
        }));

        // 3. Nusrat at TSC Cafeteria Terrace (X = 46.0, Z = 20.0, facing West: 270 deg)
        friendNusrat = new CompanionNPC("npc_nusrat", "Nusrat (নুসরাত)", "Student Representative",
            new Vector3(46.0f, 0f, 20.0f), 270.0f);
        friendNusrat.setDialogueSequence(HistoricalDialogues.createNusratDialogue().setCallback(new DialogueCallback() {
            @Override
            public void onDialogueComplete(String sequenceId) {
                missionTracker.completeObjective("m_nusrat");
            }
        }));
    }

    private void initObjectives() {
        missionTracker.reset();
        missionTracker.addObjective(new MissionObjective(
            "m_rafi", "MEET RAFI AT RAJU SCULPTURE", "Raju Memorial Roundabout", CHECKLIST[0],
            new Vector3(44f, 0f, 36f), 3.8f, true
        ));
        missionTracker.addObjective(new MissionObjective(
            "m_raju", "INSPECT RAJU MEMORIAL PLAQUE", "Anti-Terrorism Raju Memorial", CHECKLIST[1],
            new Vector3(44f, 0f, 42f), 3.8f, false
        ));
        missionTracker.addObjective(new MissionObjective(
            "m_tanvir", "DISCUSS REFORM WITH TANVIR", "Central Library Forecourt Steps", CHECKLIST[2],
            new Vector3(-54f, 0f, 40f), 3.8f, false
        ));
        missionTracker.addObjective(new MissionObjective(
            "m_library", "INSPECT CENTRAL LIBRARY ARCHIVES", "Central Library Colonnade", CHECKLIST[3],
            new Vector3(-58f, 0f, 36f), 3.8f, false
        ));
        missionTracker.addObjective(new MissionObjective(
            "m_nusrat", "REGROUP WITH NUSRAT AT TSC", "TSC Cafeteria Terrace", CHECKLIST[4],
            new Vector3(46f, 0f, 20f), 3.8f, false
        ));
        missionTracker.addObjective(new MissionObjective(
            "m_tsc", "INSPECT TSC SOLIDARITY DECLARATION", "TSC Main Plaza Pavilion", CHECKLIST[5],
            new Vector3(66f, 0f, 20f), 3.8f, false
        ));
    }

    public void update(float delta, Vector3 playerPos) {
        if (world != null) {}
        if (memorials != null) memorials.update(delta);
        if (friendRafi != null) friendRafi.update(delta, playerPos);
        if (friendTanvir != null) friendTanvir.update(delta, playerPos);
        if (friendNusrat != null) friendNusrat.update(delta, playerPos);
    }

    public void render3D(ModelBatch batch) {
        if (world != null) world.render(batch);
        if (memorials != null) memorials.render(batch, world.getEnvironment());

        if (characterModel != null && world != null) {
            Environment env = world.getEnvironment();
            if (friendRafi != null) friendRafi.render(batch, env, characterModel);
            if (friendTanvir != null) friendTanvir.render(batch, env, characterModel);
            if (friendNusrat != null) friendNusrat.render(batch, env, characterModel);
        }
    }

    public String getContextPrompt(Vector3 playerPos) {
        if (playerPos == null) return null;

        if (friendRafi != null && playerPos.dst(friendRafi.getPosition()) <= 3.5f) {
            return "[E] TALK TO RAFI (রাফির সাথে কথা বলুন)";
        }
        if (friendTanvir != null && playerPos.dst(friendTanvir.getPosition()) <= 3.5f) {
            return "[E] TALK TO TANVIR (তানভীরের সাথে কথা বলুন)";
        }
        if (friendNusrat != null && playerPos.dst(friendNusrat.getPosition()) <= 3.5f) {
            return "[E] TALK TO NUSRAT (নুসরাতের সাথে কথা বলুন)";
        }

        if (memorials != null) {
            for (MemorialEntry entry : memorials.getEntries()) {
                if (!entry.inspected && playerPos.dst(entry.position) <= 3.8f) {
                    return "[E] INSPECT HISTORICAL ARCHIVE (ঐতিহাসিক নথি পাঠ করুন)";
                }
            }
        }
        return null;
    }

    public boolean handleInteract(Vector3 playerPos, PlayerController player, ProjectJulyHUD hud) {
        if (playerPos == null || hud == null) return false;

        // 1. Friend Rafi
        if (friendRafi != null && playerPos.dst(friendRafi.getPosition()) <= 3.5f) {
            hud.getDialogueManager().startDialogue(friendRafi.getDialogueSequence());
            missionTracker.completeObjective("m_rafi");
            hud.showToast("Rafi: Anti-Terrorism Solidarity");
            SoundManager.get().playObjective();
            return true;
        }

        // 2. Friend Tanvir
        if (friendTanvir != null && playerPos.dst(friendTanvir.getPosition()) <= 3.5f) {
            hud.getDialogueManager().startDialogue(friendTanvir.getDialogueSequence());
            missionTracker.completeObjective("m_tanvir");
            hud.showToast("Tanvir: Constitutional Equality");
            SoundManager.get().playObjective();
            return true;
        }

        // 3. Friend Nusrat
        if (friendNusrat != null && playerPos.dst(friendNusrat.getPosition()) <= 3.5f) {
            hud.getDialogueManager().startDialogue(friendNusrat.getDialogueSequence());
            missionTracker.completeObjective("m_nusrat");
            hud.showToast("Nusrat: Student Assembly");
            SoundManager.get().playObjective();
            return true;
        }

        // 4. Historical Memorials
        if (memorials != null) {
            for (MemorialEntry entry : memorials.getEntries()) {
                if (!entry.inspected && playerPos.dst(entry.position) <= 3.8f) {
                    entry.inspected = true;
                    if (entry.id == 1) {
                        missionTracker.completeObjective("m_raju");
                    } else if (entry.id == 2) {
                        missionTracker.completeObjective("m_library");
                    } else if (entry.id == 3) {
                        missionTracker.completeObjective("m_tsc");
                    }

                    hud.getDocumentModal().open(entry);
                    hud.showToast(entry.titleEnglish);
                    SoundManager.get().playBell();

                    if (missionTracker.isAllCompleted()) {
                        hud.setVictory(true);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public Vector3 getSpawnPosition() {
        return new Vector3(44.0f, 0f, 24.0f); // Northern paved avenue approach
    }

    public float getSpawnHeading() {
        return 0.0f; // Facing south directly toward Raju Memorial Sculpture
    }

    public MissionTracker getMissionTracker() {
        return missionTracker;
    }

    public CampusWorld getWorld() {
        return world;
    }

    public JulyMemorials getMemorials() {
        return memorials;
    }

    public CharacterModel getCharacterModel() {
        return characterModel;
    }

    public CompanionNPC getFriendRafi() {
        return friendRafi;
    }

    public CompanionNPC getFriendTanvir() {
        return friendTanvir;
    }

    public CompanionNPC getFriendNusrat() {
        return friendNusrat;
    }

    public boolean isLevelComplete() {
        return missionTracker.isAllCompleted();
    }

    public void restart() {
        initCompanions();
        initObjectives();
        if (memorials != null) {
            for (MemorialEntry e : memorials.getEntries()) {
                e.inspected = false;
            }
        }
    }

    @Override
    public void dispose() {
        if (world != null) world.dispose();
        if (memorials != null) memorials.dispose();
        if (characterModel != null) characterModel.dispose();
    }
}
