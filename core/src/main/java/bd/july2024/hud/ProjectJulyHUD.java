package bd.july2024.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import bd.july2024.dialogue.DialogueLine;
import bd.july2024.dialogue.DialogueManager;
import bd.july2024.level.MissionTracker;
import bd.july2024.world.JulyMemorials.MemorialEntry;

/**
 * Minimalist documentary-style HUD for Project July 2024.
 * Renders bilingual dialogues, compass waypoints, mission objectives, and pause/victory states.
 */
public class ProjectJulyHUD implements Disposable {

    private final ShapeRenderer shapeRenderer;
    private final SpriteBatch spriteBatch;
    private final BengaliFontRenderer fonts;
    private final DialogueManager dialogueManager = new DialogueManager();
    private final HistoricalDocumentModal documentModal = new HistoricalDocumentModal();

    private boolean isPaused = false;
    private boolean isVictory = false;

    // Toast notification
    private String toastTitle = null;
    private float toastTimer = 0f;

    public ProjectJulyHUD(BengaliFontRenderer fonts) {
        this.fonts = fonts;
        this.shapeRenderer = new ShapeRenderer();
        this.spriteBatch = new SpriteBatch();
    }

    public void update(float delta) {
        if (toastTimer > 0f) {
            toastTimer -= delta;
            if (toastTimer <= 0f) {
                toastTitle = null;
            }
        }
    }

    public void showToast(String title) {
        this.toastTitle = title;
        this.toastTimer = 4.0f;
    }

    public void render(float screenW, float screenH, Vector3 playerPos, float cameraYaw,
                       MissionTracker tracker, String contextPrompt, float stamina) {
        // 1. In-game Compass & Active Mission Header
        renderHeader(screenW, screenH, playerPos, tracker);

        // 2. Mission Checklist (Top-Left)
        renderChecklist(screenW, screenH, tracker);

        // 3. Stamina Indicator (Bottom-Right)
        renderStaminaBar(screenW, screenH, stamina);

        // 4. Context Prompt ([E] Talk / Inspect)
        renderContextPrompt(screenW, screenH, contextPrompt);

        // 5. Active Dialogue Subtitles
        renderDialogueSubtitles(screenW, screenH);

        // 6. Toast Notification Banner
        renderToast(screenW, screenH);

        // 7. Historical Document Reader Modal
        documentModal.render(shapeRenderer, spriteBatch, fonts, screenW, screenH);

        // 8. Pause Menu
        if (isPaused) {
            renderPauseMenu(screenW, screenH);
        }

        // 9. Victory Summary
        if (isVictory) {
            renderVictoryScreen(screenW, screenH);
        }
    }

    private void renderHeader(float screenW, float screenH, Vector3 playerPos, MissionTracker tracker) {
        float barH = 54f;
        shapeRenderer.begin(ShapeType.Filled);
        shapeRenderer.setColor(0.08f, 0.09f, 0.11f, 0.85f);
        shapeRenderer.rect(0, screenH - barH, screenW, barH);
        shapeRenderer.end();

        spriteBatch.begin();
        BitmapFont titleFont = fonts.getTitleFont();
        BitmapFont subFont = fonts.getSubtitleFont();

        titleFont.setColor(0.95f, 0.95f, 0.95f, 1f);
        titleFont.draw(spriteBatch, "PROJECT JULY 2024", 24f, screenH - 14f);

        subFont.setColor(0.75f, 0.78f, 0.80f, 1f);
        subFont.draw(spriteBatch, "অধ্যায় ০১: যেখানে সব শুরু — ঢাকা বিশ্ববিদ্যালয় (July 1 – 14, 2024)", 24f, screenH - 36f);

        // Waypoint Distance
        if (tracker != null && tracker.getActiveWaypoint() != null && playerPos != null) {
            float dist = playerPos.dst(tracker.getActiveWaypoint());
            String distText = String.format("গন্তব্য: %.1f মিটার | %s", dist, tracker.getActiveObjective().getTargetDescription());
            subFont.setColor(0.95f, 0.82f, 0.35f, 1f);
            subFont.draw(spriteBatch, distText, screenW - 480f, screenH - 24f);
        }
        spriteBatch.end();
    }

    private void renderChecklist(float screenW, float screenH, MissionTracker tracker) {
        if (tracker == null) return;
        float x = 24f;
        float y = screenH - 74f;

        spriteBatch.begin();
        BitmapFont subFont = fonts.getSubtitleFont();
        subFont.setColor(0.85f, 0.72f, 0.35f, 1f);
        subFont.draw(spriteBatch, "কার্যক্রম (OBJECTIVES):", x, y);
        y -= 22f;

        String[] items = tracker.getChecklistItems();
        for (int i = 0; i < items.length; i++) {
            boolean done = tracker.isChecklistItemDone(i);
            boolean active = (tracker.getObjective(i) != null && tracker.getObjective(i).isActive());

            if (done) {
                subFont.setColor(0.35f, 0.82f, 0.40f, 1f);
                subFont.draw(spriteBatch, "[✔] " + items[i], x, y);
            } else if (active) {
                subFont.setColor(1.0f, 0.90f, 0.35f, 1f);
                subFont.draw(spriteBatch, "[▶] " + items[i], x, y);
            } else {
                subFont.setColor(0.55f, 0.55f, 0.55f, 0.75f);
                subFont.draw(spriteBatch, "[ ] " + items[i], x, y);
            }
            y -= 20f;
        }
        spriteBatch.end();
    }

    private void renderStaminaBar(float screenW, float screenH, float stamina) {
        float barW = 180f;
        float barH = 10f;
        float barX = screenW - barW - 24f;
        float barY = 24f;

        shapeRenderer.begin(ShapeType.Filled);
        shapeRenderer.setColor(0.12f, 0.12f, 0.12f, 0.75f);
        shapeRenderer.rect(barX, barY, barW, barH);

        shapeRenderer.setColor(0.25f, 0.65f, 0.85f, 0.90f);
        shapeRenderer.rect(barX, barY, barW * (stamina / 100f), barH);
        shapeRenderer.end();

        spriteBatch.begin();
        BitmapFont subFont = fonts.getSubtitleFont();
        subFont.setColor(0.75f, 0.75f, 0.75f, 1f);
        subFont.draw(spriteBatch, "শক্তি (STAMINA)", barX, barY + 22f);
        spriteBatch.end();
    }

    private void renderContextPrompt(float screenW, float screenH, String prompt) {
        if (prompt == null || prompt.isEmpty() || dialogueManager.isDialogueActive() || documentModal.isOpen()) {
            return;
        }

        float boxW = 460f;
        float boxH = 42f;
        float boxX = (screenW - boxW) / 2f;
        float boxY = 80f;

        shapeRenderer.begin(ShapeType.Filled);
        shapeRenderer.setColor(0.10f, 0.11f, 0.13f, 0.90f);
        shapeRenderer.rect(boxX, boxY, boxW, boxH);
        shapeRenderer.end();

        shapeRenderer.begin(ShapeType.Line);
        shapeRenderer.setColor(0.95f, 0.82f, 0.35f, 1f);
        shapeRenderer.rect(boxX, boxY, boxW, boxH);
        shapeRenderer.end();

        spriteBatch.begin();
        BitmapFont bodyFont = fonts.getBodyFont();
        bodyFont.setColor(0.95f, 0.88f, 0.40f, 1f);
        bodyFont.draw(spriteBatch, prompt, boxX + 16f, boxY + 28f);
        spriteBatch.end();
    }

    private void renderDialogueSubtitles(float screenW, float screenH) {
        if (!dialogueManager.isDialogueActive()) return;

        DialogueLine line = dialogueManager.getCurrentLine();
        if (line == null) return;

        float boxW = Math.min(920f, screenW - 60f);
        float boxH = 135f;
        float boxX = (screenW - boxW) / 2f;
        float boxY = 24f;

        // Subtitle backdrop
        shapeRenderer.begin(ShapeType.Filled);
        shapeRenderer.setColor(0.06f, 0.07f, 0.09f, 0.92f);
        shapeRenderer.rect(boxX, boxY, boxW, boxH);
        shapeRenderer.end();

        shapeRenderer.begin(ShapeType.Line);
        shapeRenderer.setColor(0.85f, 0.72f, 0.35f, 0.7f);
        shapeRenderer.rect(boxX, boxY, boxW, boxH);
        shapeRenderer.end();

        spriteBatch.begin();
        BitmapFont titleFont = fonts.getTitleFont();
        BitmapFont bodyFont = fonts.getBodyFont();
        BitmapFont subFont = fonts.getSubtitleFont();

        // Speaker tag
        titleFont.setColor(0.95f, 0.82f, 0.35f, 1f);
        titleFont.draw(spriteBatch, line.getSpeakerBengali() + " (" + line.getSpeakerId() + ")", boxX + 22f, boxY + boxH - 14f);

        // Bengali dialogue text
        bodyFont.setColor(1.0f, 1.0f, 1.0f, 1f);
        bodyFont.draw(spriteBatch, line.getTextBengali(), boxX + 22f, boxY + boxH - 46f, boxW - 44f, -1, true);

        // English subtitle translation
        subFont.setColor(0.80f, 0.82f, 0.85f, 1f);
        subFont.draw(spriteBatch, line.getTextEnglish(), boxX + 22f, boxY + 36f, boxW - 44f, -1, true);

        // Advance prompt
        subFont.setColor(0.70f, 0.70f, 0.70f, 1f);
        subFont.draw(spriteBatch, "[E / ENTER] এগিয়ে যান (Continue)", boxX + boxW - 220f, boxY + boxH - 16f);

        spriteBatch.end();
    }

    private void renderToast(float screenW, float screenH) {
        if (toastTitle == null) return;

        float toastW = 480f;
        float toastH = 44f;
        float toastX = (screenW - toastW) / 2f;
        float toastY = screenH - 120f;

        shapeRenderer.begin(ShapeType.Filled);
        shapeRenderer.setColor(0.12f, 0.45f, 0.22f, 0.92f);
        shapeRenderer.rect(toastX, toastY, toastW, toastH);
        shapeRenderer.end();

        spriteBatch.begin();
        BitmapFont bodyFont = fonts.getBodyFont();
        bodyFont.setColor(1.0f, 1.0f, 1.0f, 1f);
        bodyFont.draw(spriteBatch, "✔ " + toastTitle, toastX + 18f, toastY + 30f);
        spriteBatch.end();
    }

    private void renderPauseMenu(float screenW, float screenH) {
        shapeRenderer.begin(ShapeType.Filled);
        shapeRenderer.setColor(0f, 0f, 0f, 0.70f);
        shapeRenderer.rect(0, 0, screenW, screenH);
        shapeRenderer.end();

        float boxW = 420f;
        float boxH = 260f;
        float boxX = (screenW - boxW) / 2f;
        float boxY = (screenH - boxH) / 2f;

        shapeRenderer.begin(ShapeType.Filled);
        shapeRenderer.setColor(0.10f, 0.11f, 0.13f, 0.95f);
        shapeRenderer.rect(boxX, boxY, boxW, boxH);
        shapeRenderer.end();

        shapeRenderer.begin(ShapeType.Line);
        shapeRenderer.setColor(0.85f, 0.72f, 0.35f, 1f);
        shapeRenderer.rect(boxX, boxY, boxW, boxH);
        shapeRenderer.end();

        spriteBatch.begin();
        BitmapFont titleFont = fonts.getTitleFont();
        BitmapFont bodyFont = fonts.getBodyFont();
        BitmapFont subFont = fonts.getSubtitleFont();

        titleFont.setColor(0.95f, 0.82f, 0.35f, 1f);
        titleFont.draw(spriteBatch, "খেলা স্থগিত (PAUSED)", boxX + 80f, boxY + boxH - 28f);

        bodyFont.setColor(1f, 1f, 1f, 1f);
        bodyFont.draw(spriteBatch, "[ESC] খেলা চালিয়ে যান (Resume)", boxX + 40f, boxY + boxH - 85f);
        bodyFont.draw(spriteBatch, "[R] লেভেল পুনরায় শুরু (Restart Level)", boxX + 40f, boxY + boxH - 130f);
        bodyFont.draw(spriteBatch, "[M] প্রধান মেনু (Main Menu)", boxX + 40f, boxY + boxH - 175f);

        subFont.setColor(0.70f, 0.70f, 0.70f, 1f);
        subFont.draw(spriteBatch, "Project July 2024 — Verified Campus Build", boxX + 40f, boxY + 32f);
        spriteBatch.end();
    }

    private void renderVictoryScreen(float screenW, float screenH) {
        shapeRenderer.begin(ShapeType.Filled);
        shapeRenderer.setColor(0f, 0f, 0f, 0.85f);
        shapeRenderer.rect(0, 0, screenW, screenH);
        shapeRenderer.end();

        float boxW = 680f;
        float boxH = 340f;
        float boxX = (screenW - boxW) / 2f;
        float boxY = (screenH - boxH) / 2f;

        shapeRenderer.begin(ShapeType.Filled);
        shapeRenderer.setColor(0.10f, 0.12f, 0.14f, 0.98f);
        shapeRenderer.rect(boxX, boxY, boxW, boxH);
        shapeRenderer.end();

        shapeRenderer.begin(ShapeType.Line);
        shapeRenderer.setColor(0.35f, 0.85f, 0.45f, 1f);
        shapeRenderer.rect(boxX, boxY, boxW, boxH);
        shapeRenderer.end();

        spriteBatch.begin();
        BitmapFont titleFont = fonts.getTitleFont();
        BitmapFont bodyFont = fonts.getBodyFont();
        BitmapFont subFont = fonts.getSubtitleFont();

        titleFont.setColor(0.35f, 0.92f, 0.45f, 1f);
        titleFont.draw(spriteBatch, "লেভেল ০১ সম্পন্ন! (LEVEL 01 COMPLETED)", boxX + 50f, boxY + boxH - 36f);

        bodyFont.setColor(1f, 1f, 1f, 1f);
        bodyFont.draw(spriteBatch, "ঢাকা বিশ্ববিদ্যালয় প্রাঙ্গণে ছাত্রদের শান্তিপূর্ণ সংহতি কর্মসূচি সফলভাবে পরিদর্শিত হয়েছে।", boxX + 40f, boxY + boxH - 85f, boxW - 80f, -1, true);

        subFont.setColor(0.85f, 0.88f, 0.90f, 1f);
        subFont.draw(spriteBatch, "The peaceful solidarity phase across Raju Sculpture, Central Library, and TSC is fully documented.", boxX + 40f, boxY + boxH - 160f, boxW - 80f, -1, true);

        bodyFont.setColor(0.95f, 0.82f, 0.35f, 1f);
        bodyFont.draw(spriteBatch, "[ENTER / SPACE] প্রধান মেনুতে ফিরে যান (Return to Main Menu)", boxX + 40f, boxY + 48f);

        spriteBatch.end();
    }

    public DialogueManager getDialogueManager() {
        return dialogueManager;
    }

    public HistoricalDocumentModal getDocumentModal() {
        return documentModal;
    }

    public void setPaused(boolean paused) {
        this.isPaused = paused;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public void setVictory(boolean victory) {
        this.isVictory = victory;
    }

    public boolean isVictory() {
        return isVictory;
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
        spriteBatch.dispose();
        documentModal.close();
    }
}
