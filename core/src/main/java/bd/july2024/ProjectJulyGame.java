package bd.july2024;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import bd.july2024.audio.SoundManager;
import bd.july2024.camera.ThirdPersonCamera;
import bd.july2024.character.PlayerController;
import bd.july2024.hud.BengaliFontRenderer;
import bd.july2024.hud.ProjectJulyHUD;
import bd.july2024.level.Level01DhakaUniversity;
import bd.july2024.render.PostProcessor;
import bd.july2024.world.JulyMemorials;
import bd.july2024.world.JulyMemorials.MemorialEntry;
import bd.july2024.world.TextureFactory;

/**
 * Main Game Application for Project July 2024.
 * Authentic historical 3D action-adventure inspired by the July–August 2024 Bangladesh student movement.
 */
public class ProjectJulyGame implements ApplicationListener {

    public enum GameState {
        MAIN_MENU,
        CAMPAIGN,
        ARCHIVES_CODEX
    }

    private GameState state = GameState.MAIN_MENU;

    // Rendering subsystems
    private ModelBatch modelBatch;
    private SpriteBatch spriteBatch;
    private ShapeRenderer shapeRenderer;
    private PostProcessor postProcessor;

    private BengaliFontRenderer fonts;
    private TextureFactory textures;
    private SoundManager soundManager;

    // Gameplay subsystems
    private Level01DhakaUniversity level01;
    private PlayerController player;
    private ThirdPersonCamera camera;
    private ProjectJulyHUD hud;

    // Menu state
    private int menuSelection = 0;
    private int frameCounter = 0;
    private final String[] MENU_ITEMS = {
        "START CAMPAIGN (ক্যাম্পেইন শুরু করুন — লেভেল ০১)",
        "HISTORICAL ARCHIVES (ঐতিহাসিক দলিল ও কোডেক্স)",
        "QUIT (প্রস্থান)"
    };
    private float menuAnimTime = 0f;

    @Override
    public void create() {
        modelBatch = new ModelBatch();
        spriteBatch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();

        fonts = new BengaliFontRenderer();
        textures = new TextureFactory();
        soundManager = SoundManager.get();

        player = new PlayerController();
        int screenW = Gdx.graphics.getWidth();
        int screenH = Gdx.graphics.getHeight();
        camera = new ThirdPersonCamera(screenW, screenH);
        postProcessor = new PostProcessor(screenW, screenH);

        level01 = new Level01DhakaUniversity(textures);
        hud = new ProjectJulyHUD(fonts);

        // Position player and camera at spawn
        resetPlayerToSpawn();
        player.setColliders(level01.getWorld().getColliders());
        camera.setColliders(level01.getWorld().getColliders());

        // Fast-start flag for automated headless/test execution
        if ("true".equalsIgnoreCase(System.getProperty("bd.july2024.autoStart"))) {
            startCampaign();
        }
    }

    public void startCampaign() {
        state = GameState.CAMPAIGN;
        resetPlayerToSpawn();
        hud.setPaused(false);
        hud.setVictory(false);
        soundManager.playClick();
        if (Gdx.input != null) {
            Gdx.input.setCursorCatched(true);
        }
    }

    public void returnToMainMenu() {
        state = GameState.MAIN_MENU;
        menuSelection = 0;
        hud.setPaused(false);
        soundManager.playClick();
        if (Gdx.input != null) {
            Gdx.input.setCursorCatched(false);
        }
    }

    private void resetPlayerToSpawn() {
        player.setPosition(
            level01.getSpawnPosition().x,
            level01.getSpawnPosition().y,
            level01.getSpawnPosition().z
        );
        player.setHeadingDegrees(level01.getSpawnHeading());
        camera.setAngles(0f, -8f);
        camera.setDistance(4.5f);
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();
        if (delta > 0.1f) delta = 0.1f;

        frameCounter++;
        String exitFrames = System.getProperty("bd.july2024.testExitAfterFrames");
        if (exitFrames != null) {
            try {
                int target = Integer.parseInt(exitFrames.trim());
                if (frameCounter == target - 1) {
                    String screenshotName = System.getProperty("bd.july2024.screenshot");
                    if (screenshotName != null) {
                        try {
                            com.badlogic.gdx.graphics.Pixmap pixmap = com.badlogic.gdx.utils.ScreenUtils.getFrameBufferPixmap(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
                            com.badlogic.gdx.graphics.PixmapIO.writePNG(Gdx.files.local(screenshotName), pixmap);
                            pixmap.dispose();
                            Gdx.app.log("ProjectJulyGame", "Saved verification screenshot to: " + screenshotName);
                        } catch (Throwable t) {
                            Gdx.app.error("ProjectJulyGame", "Failed to capture screenshot", t);
                        }
                    }
                }
                if (frameCounter >= target) {
                    Gdx.app.log("ProjectJulyGame", "Automated launch test verified! Rendered " + frameCounter + " frames cleanly.");
                    Gdx.app.exit();
                    return;
                }
            } catch (Exception ignored) {}
        }

        Gdx.gl.glClearColor(0.08f, 0.09f, 0.11f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        switch (state) {
            case MAIN_MENU:
                renderMainMenu(delta);
                break;
            case CAMPAIGN:
                renderCampaign(delta);
                break;
            case ARCHIVES_CODEX:
                renderCodex(delta);
                break;
        }
    }

    private void renderMainMenu(float delta) {
        menuAnimTime += delta;
        float screenW = Gdx.graphics.getWidth();
        float screenH = Gdx.graphics.getHeight();

        handleMenuInput();

        // Dark atmospheric background with green and red ambient gradient
        shapeRenderer.begin(ShapeType.Filled);
        shapeRenderer.setColor(0.06f, 0.08f, 0.10f, 1f);
        shapeRenderer.rect(0, 0, screenW, screenH);

        // Decorative subtle top and bottom accent bands
        shapeRenderer.setColor(0.12f, 0.35f, 0.20f, 0.85f);
        shapeRenderer.rect(0, screenH - 8f, screenW, 8f);
        shapeRenderer.setColor(0.72f, 0.22f, 0.18f, 0.85f);
        shapeRenderer.rect(0, 0, screenW, 6f);
        shapeRenderer.end();

        spriteBatch.begin();
        BitmapFont titleFont = fonts.getTitleFont();
        BitmapFont bodyFont = fonts.getBodyFont();
        BitmapFont subFont = fonts.getSubtitleFont();

        float centerX = screenW / 2f;
        float startY = screenH * 0.76f;

        // Game Title
        titleFont.setColor(1.0f, 0.95f, 0.85f, 1f);
        titleFont.draw(spriteBatch, "PROJECT JULY 2024", centerX - 180f, startY);

        bodyFont.setColor(0.85f, 0.72f, 0.35f, 1f);
        bodyFont.draw(spriteBatch, "বাঙালি ছাত্র-জনতার ঐতিহাসিক জুলাই অভ্যুত্থান", centerX - 230f, startY - 38f);

        subFont.setColor(0.65f, 0.68f, 0.72f, 1f);
        subFont.draw(spriteBatch, "An authentic, historically grounded 3D action-adventure", centerX - 210f, startY - 68f);

        // Menu items
        float menuY = screenH * 0.44f;
        for (int i = 0; i < MENU_ITEMS.length; i++) {
            boolean selected = (i == menuSelection);
            if (selected) {
                bodyFont.setColor(1.0f, 0.88f, 0.35f, 1f);
                bodyFont.draw(spriteBatch, "▶  " + MENU_ITEMS[i], centerX - 220f, menuY);
            } else {
                bodyFont.setColor(0.75f, 0.78f, 0.82f, 0.85f);
                bodyFont.draw(spriteBatch, "   " + MENU_ITEMS[i], centerX - 220f, menuY);
            }
            menuY -= 48f;
        }

        // Footer instructions
        subFont.setColor(0.50f, 0.52f, 0.55f, 1f);
        subFont.draw(spriteBatch, "[W / S / ARROWS] নির্বাচন (Navigate)   [ENTER / SPACE] প্রবেশ (Confirm)", centerX - 240f, 40f);
        spriteBatch.end();
    }

    private void handleMenuInput() {
        if (Gdx.input == null) return;

        if (Gdx.input.isKeyJustPressed(Input.Keys.W) || Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            menuSelection = (menuSelection - 1 + MENU_ITEMS.length) % MENU_ITEMS.length;
            soundManager.playClick();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.S) || Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            menuSelection = (menuSelection + 1) % MENU_ITEMS.length;
            soundManager.playClick();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) || Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            if (menuSelection == 0) {
                startCampaign();
            } else if (menuSelection == 1) {
                state = GameState.ARCHIVES_CODEX;
                soundManager.playClick();
            } else if (menuSelection == 2) {
                Gdx.app.exit();
            }
        }
    }

    private void renderCampaign(float delta) {
        float screenW = Gdx.graphics.getWidth();
        float screenH = Gdx.graphics.getHeight();

        handleCampaignInput();

        // 1. Update Game Physics & Simulation (frozen during pause or modal)
        boolean canMove = !hud.isPaused() && !hud.isVictory() &&
                          !hud.getDocumentModal().isOpen() &&
                          !hud.getDialogueManager().isDialogueActive();

        if (canMove) {
            handleMouseLook();
            player.update(delta, camera.getYaw(), true);
            camera.update(delta, player.getPosition());
            level01.update(delta, player.getPosition());
        }
        hud.update(delta);

        // 2. Render 3D World Pass with Filmic Post-Processing
        camera.resize((int) screenW, (int) screenH);
        postProcessor.resize((int) screenW, (int) screenH);
        postProcessor.begin();

        modelBatch.begin(camera.getCamera());

        level01.render3D(modelBatch);
        level01.getCharacterModel().render(
            modelBatch,
            level01.getWorld().getEnvironment(),
            player.getPosition(),
            player.getHeadingDegrees(),
            player.getPose()
        );

        modelBatch.end();

        postProcessor.endAndRender(delta);

        // 3. Render Documentary HUD Pass
        String prompt = level01.getContextPrompt(player.getPosition());
        hud.render(
            screenW, screenH,
            player.getPosition(),
            camera.getYaw(),
            level01.getMissionTracker(),
            prompt,
            player.getStamina()
        );
    }

    private void handleMouseLook() {
        if (Gdx.input == null || !Gdx.input.isCursorCatched()) return;

        float deltaX = Gdx.input.getDeltaX();
        float deltaY = Gdx.input.getDeltaY();

        float sens = 0.22f;
        camera.rotate(-deltaX * sens, deltaY * sens);
    }

    private void handleCampaignInput() {
        if (Gdx.input == null) return;

        // Interaction key [E] or [ENTER]
        if (Gdx.input.isKeyJustPressed(Input.Keys.E) || Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            // Priority 1: Dismiss open document modal
            if (hud.getDocumentModal().isOpen()) {
                hud.getDocumentModal().close();
                soundManager.playClick();
                return;
            }

            // Priority 2: Advance active dialogue line
            if (hud.getDialogueManager().isDialogueActive()) {
                hud.getDialogueManager().advanceDialogue();
                soundManager.playClick();
                return;
            }

            // Priority 3: Interact with nearby companion or historical memorial
            level01.handleInteract(player.getPosition(), player, hud);
        }

        // Pause menu toggle [ESCAPE]
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            if (hud.getDocumentModal().isOpen()) {
                hud.getDocumentModal().close();
                return;
            }
            if (hud.getDialogueManager().isDialogueActive()) {
                hud.getDialogueManager().cancelDialogue();
                return;
            }

            hud.setPaused(!hud.isPaused());
            Gdx.input.setCursorCatched(!hud.isPaused());
            soundManager.playClick();
        }

        // Restart level [R]
        if (hud.isPaused() && Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            level01.restart();
            resetPlayerToSpawn();
            hud.setPaused(false);
            Gdx.input.setCursorCatched(true);
            soundManager.playClick();
        }

        // Return to main menu [M]
        if ((hud.isPaused() || hud.isVictory()) && Gdx.input.isKeyJustPressed(Input.Keys.M)) {
            returnToMainMenu();
        }

        // Dismiss victory screen [ENTER / SPACE]
        if (hud.isVictory() && (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) || Gdx.input.isKeyJustPressed(Input.Keys.SPACE))) {
            returnToMainMenu();
        }
    }

    private void renderCodex(float delta) {
        float screenW = Gdx.graphics.getWidth();
        float screenH = Gdx.graphics.getHeight();

        if (Gdx.input != null && (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) || Gdx.input.isKeyJustPressed(Input.Keys.ENTER))) {
            state = GameState.MAIN_MENU;
            soundManager.playClick();
            return;
        }

        shapeRenderer.begin(ShapeType.Filled);
        shapeRenderer.setColor(0.06f, 0.08f, 0.10f, 1f);
        shapeRenderer.rect(0, 0, screenW, screenH);
        shapeRenderer.end();

        spriteBatch.begin();
        BitmapFont titleFont = fonts.getTitleFont();
        BitmapFont bodyFont = fonts.getBodyFont();
        BitmapFont subFont = fonts.getSubtitleFont();

        titleFont.setColor(0.95f, 0.82f, 0.35f, 1f);
        titleFont.draw(spriteBatch, "ঐতিহাসিক দলিল ও কোডেক্স (HISTORICAL ARCHIVES)", 60f, screenH - 50f);

        float y = screenH - 120f;
        for (JulyMemorials.MemorialEntry e : level01.getMemorials().getEntries()) {
            bodyFont.setColor(1.0f, 0.90f, 0.35f, 1f);
            bodyFont.draw(spriteBatch, "▶ " + e.titleBengali + " (" + e.titleEnglish + ")", 60f, y);
            y -= 26f;

            subFont.setColor(0.85f, 0.88f, 0.90f, 1f);
            subFont.draw(spriteBatch, e.descriptionBengali, 80f, y, screenW - 140f, -1, true);
            y -= 80f;

            subFont.setColor(0.70f, 0.72f, 0.75f, 1f);
            subFont.draw(spriteBatch, e.descriptionEnglish, 80f, y, screenW - 140f, -1, true);
            y -= 80f;
        }

        bodyFont.setColor(0.95f, 0.82f, 0.35f, 1f);
        bodyFont.draw(spriteBatch, "[ESC / ENTER] প্রধান মেনুতে ফিরে যান (Back to Menu)", 60f, 50f);
        spriteBatch.end();
    }

    public Level01DhakaUniversity getLevel01() {
        return level01;
    }

    public PlayerController getPlayer() {
        return player;
    }

    public ThirdPersonCamera getCamera() {
        return camera;
    }

    public ProjectJulyHUD getHud() {
        return hud;
    }

    public GameState getState() {
        return state;
    }

    @Override
    public void resize(int width, int height) {
        if (camera != null) {
            camera.resize(width, height);
        }
        if (postProcessor != null) {
            postProcessor.resize(width, height);
        }
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void dispose() {
        if (modelBatch != null) modelBatch.dispose();
        if (spriteBatch != null) spriteBatch.dispose();
        if (shapeRenderer != null) shapeRenderer.dispose();
        if (postProcessor != null) postProcessor.dispose();
        if (level01 != null) level01.dispose();
        if (hud != null) hud.dispose();
        if (textures != null) textures.dispose();
        if (fonts != null) fonts.dispose();
        if (soundManager != null) soundManager.dispose();
    }
}
