package bd.july2024;

import bd.july2024.audio.AudioController;
import bd.july2024.camera.CinematicOrbitCamera;
import bd.july2024.character.CompanionNPC;
import bd.july2024.character.PlayerControl;
import bd.july2024.character.PlayerMesh;
import bd.july2024.dialogue.DialogueManager;
import bd.july2024.dialogue.HistoricalDialogues;
import bd.july2024.level.MissionTracker;
import bd.july2024.level.SaveManager;
import bd.july2024.ui.BengaliTextRenderer;
import bd.july2024.ui.HistoricalDocumentModal;
import bd.july2024.ui.PauseMenu;
import bd.july2024.ui.ProjectJulyHUD;
import bd.july2024.world.CampusWorld;
import bd.july2024.world.LightingManager;
import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.post.SceneProcessor;
import com.jme3.profile.AppProfiler;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Node;
import com.jme3.system.AppSettings;
import com.jme3.texture.FrameBuffer;
import com.jme3.ui.Picture;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * PROJECT JULY 2024 — Primary Game Application (jMonkeyEngine Architecture).
 * Authentic, historically grounded 3D action-adventure inspired by the July–August 2024 Bangladesh student movement.
 */
public class ProjectJulyApp extends SimpleApplication implements ActionListener, AnalogListener {

    public enum GameState {
        MAIN_MENU,
        CAMPAIGN,
        ARCHIVES_CODEX
    }

    private GameState state = GameState.CAMPAIGN; // Defaults directly into campaign or menu

    // Systems
    private AudioController audio;
    private BengaliTextRenderer textRenderer;
    private ProjectJulyHUD hud;
    private LightingManager lighting;
    private CampusWorld campusWorld;
    private CinematicOrbitCamera orbitCam;
    private PlayerControl playerControl;
    private MissionTracker missionTracker;
    private DialogueManager dialogueManager;
    private SaveManager saveManager;
    private PauseMenu pauseMenu;
    private HistoricalDocumentModal docModal;
    private boolean capturePending = false;
    private String pendingScreenshotPath = null;

    // Entities
    private Node playerNode;
    private PlayerMesh playerMesh;
    private final List<CompanionNPC> companions = new ArrayList<>();

    // Input state
    private boolean moveForward = false;
    private boolean moveBackward = false;
    private boolean moveLeft = false;
    private boolean moveRight = false;
    private boolean isSprint = false;
    private boolean isJump = false;
    private boolean isCrouch = false;

    // Profiling & automated testing
    private static Integer autoExitFrames = null;
    private int frameCounter = 0;
    private float totalDeltaTime = 0f;
    private float minDelta = Float.MAX_VALUE;
    private float maxDelta = 0f;

    public static void main(String[] args) {
        // Enforce headless AWT to avoid macOS Cocoa NSApp / GLFW main-thread run loop conflicts
        System.setProperty("java.awt.headless", "true");
        if (args != null && args.length > 0) {
            for (String arg : args) {
                if (arg.startsWith("--exit-frames=")) {
                    try {
                        autoExitFrames = Integer.parseInt(arg.substring("--exit-frames=".length()).trim());
                    } catch (Exception ignored) {}
                }
            }
        }
        String sysProp = System.getProperty("bd.july2024.testExitAfterFrames");
        if (sysProp != null && !sysProp.trim().isEmpty()) {
            try { autoExitFrames = Integer.parseInt(sysProp.trim()); } catch (Exception ignored) {}
        }
        String env = System.getenv("TEST_EXIT_FRAMES");
        if (env != null && !env.trim().isEmpty()) {
            try { autoExitFrames = Integer.parseInt(env.trim()); } catch (Exception ignored) {}
        }
        File exitFile = new File(".test_exit_frames");
        if (exitFile.exists()) {
            try {
                String content = java.nio.file.Files.readString(exitFile.toPath()).trim();
                autoExitFrames = Integer.parseInt(content);
                System.out.println("[ProjectJulyApp] Automated exit target set from .test_exit_frames: " + autoExitFrames + " frames.");
            } catch (Exception e) {
                System.err.println("[ProjectJulyApp] Error reading .test_exit_frames: " + e.getMessage());
            }
        }
        System.out.println("[ProjectJulyApp] Initialized with autoExitFrames: " + autoExitFrames);

        ProjectJulyApp app = new ProjectJulyApp();
        AppSettings settings = new AppSettings(true);
        settings.setTitle("Project July 2024");
        settings.setWidth(1280);
        settings.setHeight(720);
        settings.setResolution(1280, 720);
        settings.setFullscreen(false);
        settings.setVSync(false);
        settings.setFrameRate(120);
        settings.setSamples(1); // Anti-aliasing handled via FXAA in post-processing

        app.setSettings(settings);
        app.setShowSettings(false);
        app.setPauseOnLostFocus(false); // Disable pause-on-lost-focus before window creation
        app.start();
    }

    @Override
    public void loseFocus() {
        // Project July 2024: Never pause or throttle rendering in background/unfocused windows
        this.paused = false;
        this.speed = 1f;
        if (context != null) {
            context.setAutoFlushFrames(true);
        }
    }

    @Override
    public void simpleInitApp() {
        flyCam.setEnabled(false); // Use custom cinematic third person orbit camera
        setPauseOnLostFocus(false); // Keep running and rendering even when window loses OS focus
        this.paused = false;
        this.speed = 1f;
        if (context != null) {
            context.setAutoFlushFrames(true);
        }

        setDisplayFps(false);
        setDisplayStatView(false);
        viewPort.setBackgroundColor(new ColorRGBA(0.60f, 0.74f, 0.88f, 1.0f)); // Warm, realistic Bangladesh summer sky

        System.out.println("[ProjectJulyApp] Renderer: " + renderManager.getRenderer().getClass().getName());
        System.out.println("[ProjectJulyApp] Caps: " + renderManager.getRenderer().getCaps());

        // Ensure modern single-pass lighting for macOS Core Profile (OpenGL 4.1 Metal)
        renderManager.setPreferredLightMode(com.jme3.material.TechniqueDef.LightMode.SinglePass);
        renderManager.setSinglePassLightBatchSize(4);

        // 1. Initialize Subsystems
        audio = new AudioController(assetManager, rootNode);
        textRenderer = new BengaliTextRenderer(assetManager);
        missionTracker = new MissionTracker();
        dialogueManager = new DialogueManager();
        saveManager = new SaveManager();
        hud = new ProjectJulyHUD(guiNode, textRenderer, settings.getWidth(), settings.getHeight());
        pauseMenu = new PauseMenu(guiNode, textRenderer, settings.getWidth(), settings.getHeight());
        docModal = new HistoricalDocumentModal(guiNode, textRenderer, settings.getWidth(), settings.getHeight());

        // 2. Build 3D Campus Environment & Realistic Summer Lighting
        campusWorld = new CampusWorld(assetManager, rootNode, textRenderer);
        lighting = new LightingManager(assetManager, rootNode, viewPort);

        // 3. Setup Screenshot Processor on GUI viewport (captures post-processed scene + HUD)
        setupScreenshotProcessor();


        // 3. Spawn Student Protagonist (Arif)
        playerNode = new Node("Arif_Player");
        playerMesh = new PlayerMesh(assetManager, new ColorRGBA(0.18f, 0.42f, 0.68f, 1f), new ColorRGBA(0.14f, 0.18f, 0.28f, 1f));
        playerNode.attachChild(playerMesh.getRootNode());
        rootNode.attachChild(playerNode);

        playerControl = new PlayerControl(playerNode, playerMesh, audio);
        playerControl.setPosition(0f, 0f, 8f);
        playerControl.setHeadingDegrees(0f);
        playerControl.setColliders(campusWorld.getColliders());

        // Restore checkpoint if saved
        SaveManager.SaveData saved = saveManager.loadCheckpoint();
        if (saved != null) {
            missionTracker.setCurrentObjectiveIndex(saved.objectiveIndex);
            playerControl.setPosition(saved.posX, saved.posY, saved.posZ);
            playerControl.setHeadingDegrees(saved.heading);
            playerControl.setStamina(saved.stamina);
            System.out.println("[ProjectJulyApp] Loaded saved checkpoint (Objective " + saved.objectiveIndex + ")");
        }

        // 4. Setup Cinematic Orbit Camera
        orbitCam = new CinematicOrbitCamera(cam);
        orbitCam.setAngles(180f, -8f);
        orbitCam.setColliders(campusWorld.getColliders());

        // 5. Spawn Companion NPCs
        spawnCompanions();

        // 6. Setup Controls & Input Handlers
        initInputs();

        // Audio intro click
        audio.playClick();
        System.out.println("[ProjectJulyApp] Level 01 (Dhaka University) initialized cleanly in jMonkeyEngine.");
    }

    private void spawnCompanions() {
        // 1. Rafi at Raju Memorial Sculpture
        CompanionNPC rafi = new CompanionNPC(
            assetManager, "RAFI", "রাফি (Rafi)", "Classmate & Organizer",
            new Vector3f(2.4f, 0.35f, 3.2f),
            new ColorRGBA(0.72f, 0.25f, 0.22f, 1f), // Red cotton shirt
            new ColorRGBA(0.15f, 0.18f, 0.22f, 1f)
        );
        companions.add(rafi);
        rootNode.attachChild(rafi.getNode());

        // 2. Tanvir at Hakim Chattar / Central Library
        CompanionNPC tanvir = new CompanionNPC(
            assetManager, "TANVIR", "তানভীর (Tanvir)", "Leaflet Distribution",
            new Vector3f(-12f, 0.15f, -22f),
            new ColorRGBA(0.22f, 0.52f, 0.32f, 1f), // Green kurti
            new ColorRGBA(0.18f, 0.18f, 0.20f, 1f)
        );
        companions.add(tanvir);
        rootNode.attachChild(tanvir.getNode());

        // 3. Nusrat at TSC Auditorium Entrance
        CompanionNPC nusrat = new CompanionNPC(
            assetManager, "NUSRAT", "নুসরাত (Nusrat)", "Student Liaison",
            new Vector3f(18f, 0f, 15f),
            new ColorRGBA(0.85f, 0.62f, 0.18f, 1f), // Yellow/gold salwar
            new ColorRGBA(0.68f, 0.22f, 0.25f, 1f)
        );
        companions.add(nusrat);
        rootNode.attachChild(nusrat.getNode());
    }

    private void initInputs() {
        inputManager.addMapping("Forward", new KeyTrigger(KeyInput.KEY_W), new KeyTrigger(KeyInput.KEY_UP));
        inputManager.addMapping("Backward", new KeyTrigger(KeyInput.KEY_S), new KeyTrigger(KeyInput.KEY_DOWN));
        inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_A), new KeyTrigger(KeyInput.KEY_LEFT));
        inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D), new KeyTrigger(KeyInput.KEY_RIGHT));
        inputManager.addMapping("Sprint", new KeyTrigger(KeyInput.KEY_LSHIFT), new KeyTrigger(KeyInput.KEY_RSHIFT));
        inputManager.addMapping("Jump", new KeyTrigger(KeyInput.KEY_SPACE), new KeyTrigger(KeyInput.KEY_J));
        inputManager.addMapping("Crouch", new KeyTrigger(KeyInput.KEY_C));
        inputManager.addMapping("Interact", new KeyTrigger(KeyInput.KEY_E), new KeyTrigger(KeyInput.KEY_RETURN));
        inputManager.addMapping("Screenshot", new KeyTrigger(KeyInput.KEY_F12), new KeyTrigger(KeyInput.KEY_SYSRQ));
        inputManager.addMapping("Pause", new KeyTrigger(KeyInput.KEY_ESCAPE), new KeyTrigger(KeyInput.KEY_P));
        inputManager.addMapping("MenuRestart", new KeyTrigger(KeyInput.KEY_R));
        inputManager.addMapping("MenuQuit", new KeyTrigger(KeyInput.KEY_Q));

        inputManager.addMapping("CamLeft", new MouseAxisTrigger(MouseInput.AXIS_X, true));
        inputManager.addMapping("CamRight", new MouseAxisTrigger(MouseInput.AXIS_X, false));
        inputManager.addMapping("CamUp", new MouseAxisTrigger(MouseInput.AXIS_Y, true));
        inputManager.addMapping("CamDown", new MouseAxisTrigger(MouseInput.AXIS_Y, false));

        inputManager.addListener(this, "Forward", "Backward", "Left", "Right", "Sprint", "Jump", "Crouch", "Interact", "Screenshot", "Pause", "MenuRestart", "MenuQuit");
        inputManager.addListener(this, "CamLeft", "CamRight", "CamUp", "CamDown");
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if ("Pause".equals(name) && isPressed) {
            if (docModal.isOpen()) {
                docModal.close();
                audio.playClick();
                return;
            }
            audio.playClick();
            pauseMenu.toggle();
            return;
        }

        if (pauseMenu.isOpen()) {
            if (isPressed) {
                if ("Backward".equals(name)) { // S key triggers checkpoint save in pause menu
                    saveGameCheckpoint();
                } else if ("MenuRestart".equals(name)) {
                    restartLevel();
                } else if ("MenuQuit".equals(name)) {
                    System.out.println("[ProjectJulyApp] Player quit from pause menu.");
                    stop(true);
                    new Thread(() -> {
                        try { Thread.sleep(200); } catch (Exception ignored) {}
                        System.exit(0);
                    }).start();
                }
            }
            return;
        }

        if (docModal.isOpen()) {
            if (isPressed && ("Interact".equals(name) || "Jump".equals(name))) {
                docModal.close();
                audio.playClick();
            }
            return;
        }

        if ("Forward".equals(name)) moveForward = isPressed;
        if ("Backward".equals(name)) moveBackward = isPressed;
        if ("Left".equals(name)) moveLeft = isPressed;
        if ("Right".equals(name)) moveRight = isPressed;
        if ("Sprint".equals(name)) isSprint = isPressed;
        if ("Jump".equals(name)) isJump = isPressed;
        if ("Crouch".equals(name)) isCrouch = isPressed;

        if ("Interact".equals(name) && isPressed) {
            handleInteraction();
        }

        if ("Screenshot".equals(name) && isPressed) {
            triggerScreenshot("docs/screenshots/project_july_jme_gameplay.png");
        }
    }

    @Override
    public void onAnalog(String name, float value, float tpf) {
        if (pauseMenu.isOpen() || docModal.isOpen()) {
            return; // Freeze camera rotation during pause or document modal
        }
        float sens = 45.0f;
        if ("CamLeft".equals(name)) orbitCam.rotate(value * sens, 0f);
        if ("CamRight".equals(name)) orbitCam.rotate(-value * sens, 0f);
        if ("CamUp".equals(name)) orbitCam.rotate(0f, -value * sens);
        if ("CamDown".equals(name)) orbitCam.rotate(0f, value * sens);
    }

    private void handleInteraction() {
        if (docModal.isOpen()) {
            docModal.close();
            audio.playClick();
            return;
        }

        // Priority 1: Advance active dialogue line
        if (dialogueManager.isActive()) {
            boolean hasMore = dialogueManager.advanceDialogue();
            audio.playClick();
            if (!hasMore) {
                audio.playObjective();
            }
            return;
        }

        // Priority 2: Check nearby companions
        Vector3f pPos = playerControl.getPosition();
        for (CompanionNPC c : companions) {
            if (pPos.distance(c.getPosition()) <= 3.8f) {
                audio.playClick();
                if ("RAFI".equals(c.getId())) {
                    dialogueManager.startDialogue(HistoricalDialogues.getRafiDialogue(), () -> {
                        if (missionTracker.getCurrentObjectiveIndex() == 0) {
                            missionTracker.advanceObjective();
                            audio.playBell();
                        }
                    });
                } else if ("TANVIR".equals(c.getId())) {
                    dialogueManager.startDialogue(HistoricalDialogues.getTanvirDialogue(), () -> {
                        if (missionTracker.getCurrentObjectiveIndex() == 1) {
                            missionTracker.advanceObjective();
                            audio.playBell();
                        }
                    });
                } else if ("NUSRAT".equals(c.getId())) {
                    dialogueManager.startDialogue(HistoricalDialogues.getNusratDialogue(), () -> {
                        if (missionTracker.getCurrentObjectiveIndex() == 2) {
                            missionTracker.advanceObjective();
                            audio.playBell();
                        }
                    });
                }
                return;
            }
        }

        // Priority 3: Check Memorial Plaque (opens archival document modal)
        if (pPos.distance(new Vector3f(2f, 0.8f, -6f)) <= 3.2f) {
            audio.playClick();
            docModal.open(HistoricalDialogues.getRajuMemorialDocument());
            if (missionTracker.getCurrentObjectiveIndex() == 3) {
                missionTracker.advanceObjective();
                audio.playBell();
            }
            return;
        }

        // Priority 4: Rally assembly at roundabout
        if (missionTracker.getCurrentObjectiveIndex() == 4 && pPos.distance(new Vector3f(0f, 0f, 0f)) <= 4.5f) {
            audio.playClick();
            dialogueManager.startDialogue(HistoricalDialogues.getRallyDialogue(), () -> {
                missionTracker.advanceObjective();
                audio.playBell();
            });
        }
    }

    public void saveGameCheckpoint() {
        boolean ok = saveManager.saveCheckpoint(
            missionTracker.getCurrentObjectiveIndex(),
            playerControl.getPosition(),
            playerControl.getHeadingDegrees(),
            playerControl.getStamina()
        );
        if (ok) {
            audio.playObjective();
            pauseMenu.setStatusMessage("চেকপয়েন্ট সংরক্ষিত হয়েছে! (Checkpoint Saved)");
            System.out.println("[ProjectJulyApp] Checkpoint saved successfully at objective " + missionTracker.getCurrentObjectiveIndex());
        } else {
            pauseMenu.setStatusMessage("সংরক্ষণ ব্যর্থ হয়েছে! (Save Failed)");
        }
    }

    public void restartLevel() {
        saveManager.clearCheckpoint();
        missionTracker.reset();
        playerControl.setPosition(0f, 0f, 8f);
        playerControl.setHeadingDegrees(0f);
        playerControl.setStamina(100f);
        if (dialogueManager.isActive()) dialogueManager.endDialogue();
        if (docModal.isOpen()) docModal.close();
        if (pauseMenu.isOpen()) pauseMenu.close();
        audio.playObjective();
        System.out.println("[ProjectJulyApp] Level 01 restarted to initial state.");
    }

    private String getContextPrompt() {
        if (dialogueManager.isActive() || pauseMenu.isOpen() || docModal.isOpen()) return "";
        Vector3f pPos = playerControl.getPosition();

        for (CompanionNPC c : companions) {
            if (pPos.distance(c.getPosition()) <= 3.8f) {
                return "[E] " + c.getNameBengali() + "-র সাথে কথা বলুন (Talk)";
            }
        }

        if (pPos.distance(new Vector3f(2f, 0.8f, -6f)) <= 3.2f) {
            return "[E] জুলাই স্মারক তথ্যফলক পাঠ করুন (Read Plaque)";
        }

        if (missionTracker.getCurrentObjectiveIndex() == 4 && pPos.distance(new Vector3f(0f, 0f, 0f)) <= 4.5f) {
            return "[E] ছাত্র-জনতার ঐতিহাসিক সমাবেশে বক্তব্য দিন (Address Rally)";
        }

        return "";
    }

    @Override
    public void simpleUpdate(float tpf) {
        frameCounter++;
        totalDeltaTime += tpf;
        if (tpf < minDelta) minDelta = tpf;
        if (tpf > maxDelta) maxDelta = tpf;
        if (frameCounter <= 5 || frameCounter % 15 == 0) {
            System.out.println("[ProjectJulyApp] Frame " + frameCounter + " rendered (tpf=" + String.format("%.4f", tpf) + ", autoExit=" + autoExitFrames + ")");
        }

        // 1. Update Player Locomotion (Pause movement during active dialogue, pause menu, or document modal)
        boolean canMove = !dialogueManager.isActive() && !pauseMenu.isOpen() && !docModal.isOpen();
        if (canMove) {
            playerControl.update(
                tpf, orbitCam.getYaw(),
                moveForward, moveBackward, moveLeft, moveRight,
                isSprint, isJump, isCrouch
            );
        }

        // 2. Update Orbit Camera
        orbitCam.update(tpf, playerControl.getPosition());

        // 3. Update Companions
        for (CompanionNPC c : companions) {
            c.update(tpf, playerControl.getPosition());
        }

        // 4. Update HUD
        hud.update(
            playerControl.getPosition(),
            orbitCam.getYaw(),
            missionTracker,
            dialogueManager,
            getContextPrompt(),
            playerControl.getStamina()
        );

        // 5. Automated Execution & Profiling Gate
        if (autoExitFrames != null && autoExitFrames > 0) {
            int target = autoExitFrames;
            if (frameCounter == target - 10) {
                triggerScreenshot("docs/screenshots/project_july_jme_gameplay.png");
            }

            if (frameCounter >= target) {
                long usedMem = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024 * 1024);
                long totalMem = Runtime.getRuntime().totalMemory() / (1024 * 1024);
                long maxMem = Runtime.getRuntime().maxMemory() / (1024 * 1024);
                float avgFrameMs = (totalDeltaTime / frameCounter) * 1000f;
                float avgFps = frameCounter / (totalDeltaTime > 0 ? totalDeltaTime : 0.0001f);

                System.out.println("=== PROJECT JULY 2024 — JMONKEYENGINE PERFORMANCE PROFILE ===");
                System.out.println("Frames Rendered: " + frameCounter);
                System.out.println("Average Frame Time: " + String.format("%.2f ms", avgFrameMs) + " (~" + String.format("%.1f", avgFps) + " FPS)");
                System.out.println("Min Frame Time: " + String.format("%.2f ms", minDelta * 1000f) + ", Max Frame Time: " + String.format("%.2f ms", maxDelta * 1000f));
                System.out.println("Heap Memory: Used " + usedMem + " MB / Total " + totalMem + " MB (Max: " + maxMem + " MB)");
                System.out.println("Resolution: " + settings.getWidth() + "x" + settings.getHeight());
                System.out.println("Renderer: " + renderer.getClass().getSimpleName());
                System.out.println("Note: automated static run with no input; not a gameplay benchmark and not a 60 FPS claim. Spec target 1920x1080 @ 60 FPS on MacBook Air M4 remains unmeasured.");
                System.out.println("=============================================================");

                stop(true);
                new Thread(() -> {
                    try { Thread.sleep(600); } catch (Exception ignored) {}
                    System.exit(0);
                }).start();
            }
        }
    }

    private void setupScreenshotProcessor() {
        guiViewPort.addProcessor(new SceneProcessor() {
            @Override
            public void initialize(RenderManager rm, ViewPort vp) {}

            @Override
            public void reshape(ViewPort vp, int w, int h) {}

            @Override
            public boolean isInitialized() {
                return true;
            }

            @Override
            public void preFrame(float tpf) {}

            @Override
            public void postQueue(RenderQueue rq) {}

            @Override
            public void postFrame(FrameBuffer out) {
                if (capturePending && pendingScreenshotPath != null) {
                    capturePending = false;
                    String path = pendingScreenshotPath;
                    pendingScreenshotPath = null;
                    captureDirectScreenshot(path);
                }
            }

            @Override
            public void cleanup() {}

            @Override
            public void setProfiler(AppProfiler profiler) {}
        });
    }

    public void triggerScreenshot(String path) {
        this.pendingScreenshotPath = path;
        this.capturePending = true;
    }

    private void captureDirectScreenshot(String filePath) {
        try {
            int w = settings.getWidth();
            int h = settings.getHeight();
            ByteBuffer buffer = BufferUtils.createByteBuffer(w * h * 4);
            GL11.glReadPixels(0, 0, w, h, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);

            File file = new File(filePath);
            if (file.getParentFile() != null) {
                file.getParentFile().mkdirs();
            }

            BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
            for (int y = 0; y < h; y++) {
                for (int x = 0; x < w; x++) {
                    int i = (x + (h - y - 1) * w) * 4;
                    int r = buffer.get(i) & 0xFF;
                    int g = buffer.get(i + 1) & 0xFF;
                    int b = buffer.get(i + 2) & 0xFF;
                    int a = buffer.get(i + 3) & 0xFF;
                    image.setRGB(x, y, (a << 24) | (r << 16) | (g << 8) | b);
                }
            }
            ImageIO.write(image, "PNG", file);
            System.out.println("[ProjectJulyApp] Verification screenshot saved to: " + file.getAbsolutePath());
        } catch (Throwable t) {
            System.err.println("[ProjectJulyApp] Failed to save screenshot: " + t.getMessage());
            t.printStackTrace();
        }
    }

    public PlayerControl getPlayerControl() { return playerControl; }
    public MissionTracker getMissionTracker() { return missionTracker; }
    public DialogueManager getDialogueManager() { return dialogueManager; }
    public List<CompanionNPC> getCompanions() { return companions; }
    public SaveManager getSaveManager() { return saveManager; }
    public PauseMenu getPauseMenu() { return pauseMenu; }
    public HistoricalDocumentModal getDocModal() { return docModal; }
}
