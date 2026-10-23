package bd.spark36.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Array;
import bd.spark36.camera.CinematicCamera;
import bd.spark36.character.CampusCrowd;
import bd.spark36.character.HelmetGoon;
import bd.spark36.character.NpcBehaviorState;
import bd.spark36.character.PlayerController;
import bd.spark36.character.StudentMesh;
import bd.spark36.character.StudentNPC;
import bd.spark36.hud.WhereWindsMeetHUD;
import bd.spark36.world.DhakaCampusWorld;
import bd.spark36.world.JulyMemorials;
import bd.spark36.world.JulyMemorials.MemorialEntry;
import bd.spark36.world.TextureFactory;

import bd.spark36.mission.MissionObjective;
import bd.spark36.mission.MissionTracker;
import bd.spark36.mission.ObjectiveType;

/**
 * LEVEL 1: "Where It All Began" (যেখান থেকে সব শুরু)
 * Period: July 1 – 14, 2024 (Pre-crackdown peaceful student solidarity).
 * Environment: Dhaka University Central Hub: Raju Memorial Sculpture, Central Library & Hakim Chattar, and TSC.
 * Focus: Movement, camera, historical setup, dialogues with student companions (Rafi, Tanvir, Nusrat),
 * and archival discovery at key campus landmarks.
 * Combat: Strictly disabled (peaceful assembly).
 */
public class Level1PeacefulCampus implements CampaignLevel {

    private DhakaCampusWorld world;
    private JulyMemorials memorials;
    private CampusCrowd crowd;
    private HelmetGoon campusSecurity;
    private TextureFactory textures;
    private StudentMesh friendMesh;

    // Dedicated Fictional Companions (representing student conscience)
    private StudentNPC friendRafi;
    private StudentNPC friendTanvir;
    private StudentNPC friendNusrat;

    private final MissionTracker missionTracker = new MissionTracker(true);

    private static final String[] CHECKLIST = {
        "1. Meet Rafi at Raju Sculpture",
        "2. Inspect Raju Memorial Plaque",
        "3. Discuss Reform with Tanvir",
        "4. Inspect Central Library Archives",
        "5. Regroup with Nusrat at TSC Terrace",
        "6. Inspect TSC Solidarity Declaration"
    };

    public Level1PeacefulCampus() {
        initDefaultObjectives();
    }

    private void initDefaultObjectives() {
        missionTracker.reset();
        missionTracker.addObjective(new MissionObjective(
            "m_rafi",
            "MEET RAFI AT RAJU SCULPTURE",
            "Target: Raju Sculpture Roundabout",
            CHECKLIST[0],
            ObjectiveType.INTERACT,
            new Vector3(44f, 0f, 36f),
            3.8f,
            true
        ));
        missionTracker.addObjective(new MissionObjective(
            "m_raju",
            "INSPECT RAJU MEMORIAL SCULPTURE",
            "Target: Anti-Terrorism Raju Memorial Plaque",
            CHECKLIST[1],
            ObjectiveType.INTERACT,
            new Vector3(44f, 0f, 42f),
            3.8f,
            false
        ));
        missionTracker.addObjective(new MissionObjective(
            "m_tanvir",
            "DISCUSS REFORM WITH TANVIR",
            "Target: Central Library Steps / Hakim Chattar",
            CHECKLIST[2],
            ObjectiveType.INTERACT,
            new Vector3(-54f, 0f, 40f),
            3.8f,
            false
        ));
        missionTracker.addObjective(new MissionObjective(
            "m_library",
            "INSPECT CENTRAL LIBRARY ARCHIVES",
            "Target: Central Library Pilotis & Louvers",
            CHECKLIST[3],
            ObjectiveType.INTERACT,
            new Vector3(-58f, 0f, 36f),
            3.8f,
            false
        ));
        missionTracker.addObjective(new MissionObjective(
            "m_nusrat",
            "REGROUP WITH NUSRAT AT TSC",
            "Target: TSC Cafeteria Terrace",
            CHECKLIST[4],
            ObjectiveType.INTERACT,
            new Vector3(46f, 0f, 20f),
            3.8f,
            false
        ));
        missionTracker.addObjective(new MissionObjective(
            "m_tsc",
            "INSPECT TSC SOLIDARITY DECLARATION",
            "Target: TSC Modernist Central Hall",
            CHECKLIST[5],
            ObjectiveType.INTERACT,
            new Vector3(66f, 0f, 20f),
            3.8f,
            false
        ));
    }

    @Override
    public int getLevelNumber() {
        return 1;
    }

    @Override
    public String getTitle() {
        return "WHERE IT ALL BEGAN";
    }

    @Override
    public String getSubtitle() {
        return "যেখান থেকে সব শুরু";
    }

    @Override
    public String getHistoricalDate() {
        return "July 1 – 14, 2024";
    }

    @Override
    public String getLocationName() {
        return "Dhaka University — TSC, Raju Sculpture & Central Library";
    }

    @Override
    public String getMissionTag() {
        int comp = missionTracker.getCompletedCount();
        int tot = missionTracker.getTotalCount();
        return String.format("ACTIVE MISSION: %02d / %02d", Math.min(comp + 1, tot), tot);
    }

    @Override
    public String getMissionTitle() {
        MissionObjective next = missionTracker.getActiveObjective();
        return (next != null) ? next.getTitle() : "CAMPUS ARCHIVES UNLOCKED";
    }

    @Override
    public String getMissionTarget() {
        MissionObjective next = missionTracker.getActiveObjective();
        return (next != null) ? next.getTargetDescription() : "All objectives completed";
    }

    @Override
    public Vector3 getActiveWaypoint() {
        Vector3 wp = missionTracker.getActiveWaypoint();
        return (wp != null) ? wp : new Vector3(44f, 0f, 36f);
    }

    @Override
    public float getWaypointDistance(Vector3 playerPos) {
        return playerPos.dst(getActiveWaypoint());
    }

    @Override
    public String[] getChecklistItems() {
        return CHECKLIST;
    }

    @Override
    public boolean isChecklistItemDone(int index) {
        return missionTracker.isChecklistItemDone(index);
    }

    @Override
    public int getCompletedObjectiveCount() {
        return missionTracker.getCompletedCount();
    }

    @Override
    public int getTotalObjectiveCount() {
        return missionTracker.getTotalCount();
    }

    @Override
    public String getContextPrompt(Vector3 playerPos) {
        if (friendRafi != null && playerPos.dst(friendRafi.getPosition()) <= 3.5f) {
            return "[E] TALK TO RAFI (CLASSMATE & FRIEND)";
        }
        if (friendTanvir != null && playerPos.dst(friendTanvir.getPosition()) <= 3.5f) {
            return "[E] TALK TO TANVIR (STUDY PARTNER & FRIEND)";
        }
        if (friendNusrat != null && playerPos.dst(friendNusrat.getPosition()) <= 3.5f) {
            return "[E] TALK TO NUSRAT (STUDENT REPRESENTATIVE)";
        }
        if (memorials != null) {
            for (MemorialEntry entry : memorials.getEntries()) {
                if (!entry.inspected && playerPos.dst(entry.position) <= 3.8f) {
                    return "[E] INSPECT HISTORICAL ARCHIVE — 36 JULY MOVEMENT";
                }
            }
        }
        if (playerPos.dst(DhakaCampusWorld.BOUNDARY_STONE_POS) <= 3.2f) {
            return "[E] INSPECT 1921 HERITAGE BOUNDARY MARKER";
        }
        if (world != null && world.isNearPondGhat(playerPos)) {
            return "[E] SIT BY CURZON PUKUR GHAT";
        }
        if (crowd != null && crowd.getClosestInteractable(playerPos, 2.6f) != null) {
            return "[E] TALK TO DU STUDENT";
        }
        return null;
    }

    @Override
    public boolean onInteract(Vector3 playerPos, PlayerController player, WhereWindsMeetHUD hud) {
        // 1. Friend Rafi at Raju Sculpture
        if (friendRafi != null && playerPos.dst(friendRafi.getPosition()) <= 3.5f) {
            boolean spoke = friendRafi.onInteract(playerPos, player, hud);
            missionTracker.completeObjective("m_rafi");
            if (hud != null) {
                hud.triggerMissionToast("Rafi: Anti-Terrorism Solidarity", getCompletedObjectiveCount());
            }
            return spoke;
        }

        // 2. Friend Tanvir at Central Library Steps
        if (friendTanvir != null && playerPos.dst(friendTanvir.getPosition()) <= 3.5f) {
            boolean spoke = friendTanvir.onInteract(playerPos, player, hud);
            missionTracker.completeObjective("m_tanvir");
            if (hud != null) {
                hud.triggerMissionToast("Tanvir: Quota Reform Discussion", getCompletedObjectiveCount());
            }
            return spoke;
        }

        // 3. Friend Nusrat at TSC Cafeteria Terrace
        if (friendNusrat != null && playerPos.dst(friendNusrat.getPosition()) <= 3.5f) {
            boolean spoke = friendNusrat.onInteract(playerPos, player, hud);
            missionTracker.completeObjective("m_nusrat");
            if (hud != null) {
                hud.triggerMissionToast("Nusrat: Student Assembly", getCompletedObjectiveCount());
            }
            return spoke;
        }

        // 4. Inspect Memorials
        if (memorials != null) {
            for (MemorialEntry entry : memorials.getEntries()) {
                if (!entry.inspected && playerPos.dst(entry.position) <= 3.8f) {
                    entry.inspected = true;
                    if (entry.id == 3) {
                        missionTracker.completeObjective("m_raju");
                    } else if (entry.id == 4) {
                        missionTracker.completeObjective("m_library");
                    } else if (entry.id == 5) {
                        missionTracker.completeObjective("m_tsc");
                    } else {
                        missionTracker.completeObjective("m" + entry.id);
                    }
                    if (hud != null) {
                        hud.openMemorialModal(entry);
                        hud.triggerMissionToast(entry.title, getCompletedObjectiveCount());
                        if (isLevelComplete()) {
                            hud.triggerVictory();
                        }
                    }
                    return true;
                }
            }
        }

        // 5. Inspect 1921 Boundary Stone
        if (playerPos.dst(DhakaCampusWorld.BOUNDARY_STONE_POS) <= 3.2f) {
            if (hud != null) hud.openMemorialModal(hud.getBoundaryStoneEntry());
            return true;
        }

        // 6. Water sitting at Pond Ghat
        if (world != null && world.isNearPondGhat(playerPos)) {
            boolean nextSit = !player.isSittingWater();
            player.setSittingWater(nextSit);
            if (nextSit) {
                player.setPosition(0f, 0.18f, 17.2f);
            }
            return true;
        }

        // 7. Talk to nearby DU Student in crowd
        if (crowd != null && crowd.interactWithClosest(playerPos, 2.6f, getLevelNumber(), hud)) {
            return true;
        }

        return false;
    }

    @Override
    public boolean isLevelComplete() {
        return missionTracker.isAllCompleted();
    }

    @Override
    public boolean isCombatEnabled() {
        return false;
    }

    @Override
    public Vector3 getSpawnPosition() {
        return new Vector3(44.0f, 0f, 24.0f); // Paved campus avenue facing south toward Raju Memorial Sculpture
    }

    @Override
    public float getSpawnHeading() {
        return 0.0f; // Facing south toward Raju Memorial Sculpture with TSC to the left
    }

    @Override
    public void init(TextureFactory textures, PlayerController player, CinematicCamera camera) {
        this.textures = textures;
        this.world = new DhakaCampusWorld(textures);
        this.memorials = new JulyMemorials();
        this.friendMesh = new StudentMesh(textures);

        if (this.missionTracker.getTotalCount() == 0) {
            initDefaultObjectives();
        } else {
            this.missionTracker.reset();
        }

        player.setExtraColliders(world.getColliders());
        this.crowd = new CampusCrowd(16, player.getColliders());
        this.crowd.setLevelProfile(1);

        // Dedicated Student NPCs for Arif's 3 fictional companions
        this.friendRafi = new StudentNPC(101, 0, 1, 0f, "Rafi (Classmate & Friend)");
        this.friendRafi.setPosition(44.0f, 0f, 36.0f);
        this.friendRafi.setHeading(180.0f); // Facing north towards approaching player
        this.friendRafi.transitionTo(NpcBehaviorState.IDLE, 999999f);
        this.friendRafi.setCustomDialogue(DialogueRepository.createRafiDialogueSequence());

        this.friendTanvir = new StudentNPC(102, 0, 2, 0f, "Tanvir (Study Partner & Friend)");
        this.friendTanvir.setPosition(-54.0f, 0f, 40.0f);
        this.friendTanvir.setHeading(90.0f); // Facing east towards central promenade
        this.friendTanvir.transitionTo(NpcBehaviorState.IDLE, 999999f);
        this.friendTanvir.setCustomDialogue(DialogueRepository.createTanvirDialogueSequence());

        this.friendNusrat = new StudentNPC(103, 1, 3, 0f, "Nusrat (Student Representative)");
        this.friendNusrat.setPosition(46.0f, 0f, 20.0f);
        this.friendNusrat.setHeading(270.0f); // Facing west towards cafeteria terrace walkway
        this.friendNusrat.transitionTo(NpcBehaviorState.IDLE, 999999f);
        this.friendNusrat.setCustomDialogue(DialogueRepository.createNusratDialogueSequence());

        this.campusSecurity = new HelmetGoon(textures);
        this.campusSecurity.setRole(HelmetGoon.Role.CAMPUS_SECURITY);
        this.campusSecurity.reset(0f, 36.0f);

        player.setPosition(getSpawnPosition().x, getSpawnPosition().y, getSpawnPosition().z);
        player.setHeadingDegrees(getSpawnHeading());

        camera.setColliders(player.getColliders());
        camera.setAngles(0f, -4f);
        camera.setDistance(4.5f);
    }

    @Override
    public void update(float delta, PlayerController player, CinematicCamera camera, boolean canMove, WhereWindsMeetHUD hud) {
        if (world != null) world.update(delta);
        if (memorials != null) memorials.update(delta);
        if (crowd != null) crowd.update(delta, player.getPosition());
        if (friendRafi != null) friendRafi.update(delta, (crowd != null) ? crowd.getGraph() : null, player.getPosition(), delta);
        if (friendTanvir != null) friendTanvir.update(delta, (crowd != null) ? crowd.getGraph() : null, player.getPosition(), delta);
        if (friendNusrat != null) friendNusrat.update(delta, (crowd != null) ? crowd.getGraph() : null, player.getPosition(), delta);
        if (campusSecurity != null && canMove) {
            campusSecurity.update(delta, player);
        }
    }

    @Override
    public void renderShadow(ModelBatch shadowBatch) {
        if (world != null) world.renderShadowCasters(shadowBatch);
        if (campusSecurity != null) campusSecurity.render(shadowBatch, null);
    }

    @Override
    public void render3D(ModelBatch modelBatch, Environment environment) {
        if (world != null) world.render(modelBatch);
        if (memorials != null) memorials.render(modelBatch, environment);
        if (campusSecurity != null) campusSecurity.render(modelBatch, environment);
        if (friendMesh != null) {
            if (friendRafi != null) friendRafi.render(modelBatch, environment, friendMesh);
            if (friendTanvir != null) friendTanvir.render(modelBatch, environment, friendMesh);
            if (friendNusrat != null) friendNusrat.render(modelBatch, environment, friendMesh);
        }
    }

    @Override
    public void renderOverlays(CinematicCamera camera, float delta, int screenW, int screenH) {
        // Atmospheric daylight god rays and campus markers handled by atmosphere renderer
    }

    @Override
    public java.util.List<float[]> getColliders() {
        return (world != null) ? world.getColliders() : new java.util.ArrayList<float[]>();
    }

    @Override
    public Environment getEnvironment() {
        return (world != null) ? world.getEnvironment() : null;
    }

    @Override
    public boolean onPlayerAttack(PlayerController player, CinematicCamera camera, WhereWindsMeetHUD hud) {
        // Peaceful Level 1: hitting innocent students triggers peace violation
        if (crowd != null && crowd.checkHitByPlayer(player.getPosition(), player.getHeadingDegrees(), 2.2f)) {
            hud.setDisqualified(true, "VIOLATION: You struck an innocent campus student! Unprovoked violence against unarmed peers is strictly forbidden.");
            return true;
        }
        return false;
    }

    public DhakaCampusWorld getWorld() {
        return world;
    }

    public JulyMemorials getMemorials() {
        return memorials;
    }

    public CampusCrowd getCrowd() {
        return crowd;
    }

    public HelmetGoon getCampusSecurity() {
        return campusSecurity;
    }

    public StudentNPC getFriendRafi() {
        return friendRafi;
    }

    public StudentNPC getFriendTanvir() {
        return friendTanvir;
    }

    public StudentNPC getFriendNusrat() {
        return friendNusrat;
    }

    @Override
    public MissionTracker getMissionTracker() {
        return missionTracker;
    }

    @Override
    public void completeAllObjectivesForTest() {
        if (memorials != null && memorials.getEntries() != null) {
            for (JulyMemorials.MemorialEntry e : memorials.getEntries()) e.inspected = true;
        }
        if (missionTracker != null) {
            missionTracker.completeAllForTest();
        }
    }

    @Override
    public void dispose() {
        if (world != null) { world.dispose(); world = null; }
        if (memorials != null) { memorials.dispose(); memorials = null; }
        if (campusSecurity != null) { campusSecurity.dispose(); campusSecurity = null; }
        if (friendMesh != null) { friendMesh.dispose(); friendMesh = null; }
    }
}
