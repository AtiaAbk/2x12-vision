package bd.july2024.ui;

import bd.july2024.AppPaths;
import bd.july2024.dialogue.DialogueLine;
import bd.july2024.dialogue.DialogueManager;
import bd.july2024.level.MissionObjective;
import bd.july2024.level.MissionTracker;
import bd.july2024.ui.BengaliTextRenderer.Line;
import bd.july2024.ui.BengaliTextRenderer.PanelStyle;
import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.ui.Picture;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * In-game HUD: objective card (top left), bearing and target distance (top right), stamina bar
 * (bottom left), interaction prompt and dialogue subtitles (bottom centre).
 *
 * All panels share one visual language from {@link BengaliTextRenderer#renderPanel}: translucent
 * dark cards, white primary text, muted secondary text, a single amber highlight and a red accent
 * bar on the panels that need attention.
 */
public class ProjectJulyHUD {

    public static final Color TEXT = new Color(246, 246, 242);
    public static final Color MUTED = new Color(188, 194, 200);
    public static final Color AMBER = new Color(255, 200, 96);
    public static final Color ACCENT = new Color(214, 58, 48);
    public static final Color PANEL = new Color(8, 10, 14, 198);
    public static final int MARGIN = 24;

    private final Node guiNode;
    private final BengaliTextRenderer textRenderer;
    private final int screenWidth;
    private final int screenHeight;
    private final AssetManager assetManager;

    private Picture compassPicture;
    private Picture objectivePicture;
    private Picture promptPicture;
    private Picture dialoguePicture;
    private Picture staminaPicture;

    private Picture mapPicture;
    private boolean mapOpen = false;
    private BufferedImage cachedMapImage;

    private String lastObjectiveKey = "";
    private String lastCompassKey = "";
    private String lastPromptText = "";
    private String lastDialogueText = "";
    private int lastStaminaInt = -1;

    public ProjectJulyHUD(Node guiNode, BengaliTextRenderer textRenderer, int screenWidth, int screenHeight) {
        this(guiNode, textRenderer, screenWidth, screenHeight, null);
    }

    public ProjectJulyHUD(Node guiNode, BengaliTextRenderer textRenderer, int screenWidth, int screenHeight, AssetManager assetManager) {
        this.guiNode = guiNode;
        this.textRenderer = textRenderer;
        this.screenWidth = screenWidth > 0 ? screenWidth : 1280;
        this.screenHeight = screenHeight > 0 ? screenHeight : 720;
        this.assetManager = assetManager;
    }

    public boolean isMapOpen() {
        return mapOpen;
    }

    public void toggleMap() {
        if (mapOpen) {
            hideMap();
        } else {
            showMap();
        }
    }

    public void showMap() {
        if (mapOpen) return;
        mapOpen = true;

        if (mapPicture != null) {
            guiNode.detachChild(mapPicture);
            mapPicture = null;
        }

        BufferedImage img = getMapImage();
        int mapW = Math.min(680, Math.max(340, screenWidth - 100));
        int mapH = (int) (mapW * (795f / 1264f));

        List<Line> lines = new ArrayList<>();
        lines.add(new Line("ক্যাম্পাস মানচিত্র  ·  DHAKA UNIVERSITY CAMPUS MAP", 14.5f, true, AMBER));
        lines.add(new Line("১. রাজু ভাস্কর্য  ·  ২. হাকিম চত্বর  ·  ৩. কেন্দ্রীয় লাইব্রেরি  ·  ৪. টিএসসি  ·  ৫. কার্জন হল", 12f, false, TEXT, 3));
        lines.add(new Line("M / Esc / E / Space  ·  মানচিত্র বন্ধ করুন (Close Map)", 11.5f, false, MUTED, 2));

        PanelStyle style = new PanelStyle();
        style.background = new Color(10, 12, 16, 235);
        style.accent = AMBER;
        style.radius = 14;
        style.paddingX = 22;
        style.paddingY = 14;
        style.minWidth = mapW + style.paddingX * 2 + 10;
        style.extraHeight = mapH + 8;

        mapPicture = textRenderer.createPanelPicture("CampusMapOverlay", lines, style, (Graphics2D g, int x, int y, int w, int h) -> {
            if (img != null) {
                g.setColor(new Color(255, 255, 255, 36));
                g.drawRoundRect(x - 1, y + 2, mapW + 1, mapH + 1, 6, 6);
                g.drawImage(img, x, y + 3, mapW, mapH, null);
            }
        });

        float x = (screenWidth - mapPicture.getWidth()) / 2f;
        float y = (screenHeight - mapPicture.getHeight()) / 2f;
        mapPicture.setPosition(Math.max(10f, x), Math.max(10f, y));
        guiNode.attachChild(mapPicture);
    }

    public void hideMap() {
        if (mapPicture != null) {
            guiNode.detachChild(mapPicture);
            mapPicture = null;
        }
        mapOpen = false;
    }

    private BufferedImage getMapImage() {
        if (cachedMapImage != null) return cachedMapImage;
        try {
            File f = AppPaths.asset("maps/campus_map.png");
            if (f != null && f.exists()) {
                cachedMapImage = ImageIO.read(f);
            }
        } catch (Throwable t) {
            System.err.println("[ProjectJulyHUD] Could not load campus map asset: " + t.getMessage());
        }
        return cachedMapImage;
    }

    private String lastStaminaKey = "";

    public void update(Vector3f playerPos, float cameraYawDegrees, MissionTracker tracker, DialogueManager dialogueManager, String contextPrompt, float stamina) {
        update(playerPos, cameraYawDegrees, tracker, dialogueManager, contextPrompt, stamina, null);
    }

    public void update(Vector3f playerPos, float cameraYawDegrees, MissionTracker tracker, DialogueManager dialogueManager, String contextPrompt, float stamina, bd.july2024.level.PlayerInventory inventory) {
        float dist = 0f;
        if (tracker != null && playerPos != null) {
            dist = playerPos.distance(tracker.getCurrentObjective().getTargetPosition());
        }
        updateCompass(cameraYawDegrees, dist);
        updateObjective(tracker);
        updateStamina(stamina, inventory);
        updatePrompt(contextPrompt, dialogueManager != null && dialogueManager.isActive());
        updateDialogue(dialogueManager);
    }

    /** Removes every HUD panel (front end) and forgets the cached keys so the next update redraws. */
    public void clear() {
        hideMap();
        for (Picture p : new Picture[]{compassPicture, objectivePicture, promptPicture, dialoguePicture, staminaPicture}) {
            if (p != null) guiNode.detachChild(p);
        }
        compassPicture = objectivePicture = promptPicture = dialoguePicture = staminaPicture = null;
        lastObjectiveKey = lastCompassKey = lastPromptText = lastDialogueText = "";
        lastStaminaInt = -1;
    }

    /** Forces the dialogue card to be redrawn (after a subtitle setting change). */
    public void invalidateDialogue() {
        lastDialogueText = "";
    }

    private static PanelStyle panelStyle(Color accent) {
        PanelStyle s = new PanelStyle();
        s.background = PANEL;
        s.accent = accent;
        s.paddingX = 16;
        s.paddingY = 10;
        return s;
    }

    private void updateCompass(float cameraYaw, float dist) {
        // Camera yaw 0 looks along +Z, which is south in the campus layout (north = -Z), so the
        // geographic bearing shown is the yaw turned by 180 degrees.
        float bearing = cameraYaw + 180f;
        int deg = ((int) ((bearing % 360f + 360f) % 360f)) / 5 * 5;
        int distInt = (int) dist;
        String key = deg + "_" + distInt;
        if (key.equals(lastCompassKey)) return;
        lastCompassKey = key;

        if (compassPicture != null) guiNode.detachChild(compassPicture);

        List<Line> lines = new ArrayList<>();
        lines.add(new Line(String.format("%s   %d°", getCardinal(deg), deg), 16f, true, TEXT));
        lines.add(new Line(String.format("লক্ষ্য  %d মি   ·   target  %d m", distInt, distInt), 12.5f, false, AMBER, 2));
        PanelStyle style = panelStyle(null);
        style.minWidth = 200;
        compassPicture = textRenderer.createPanelPicture("Compass", lines, style, null);
        compassPicture.setPosition(screenWidth - compassPicture.getWidth() - MARGIN, screenHeight - compassPicture.getHeight() - MARGIN);
        guiNode.attachChild(compassPicture);
    }

    private String getCardinal(int deg) {
        if (deg >= 338 || deg < 23) return "উত্তর  N";
        if (deg < 68) return "উত্তর-পূর্ব  NE";
        if (deg < 113) return "পূর্ব  E";
        if (deg < 158) return "দক্ষিণ-পূর্ব  SE";
        if (deg < 203) return "দক্ষিণ  S";
        if (deg < 248) return "দক্ষিণ-পশ্চিম  SW";
        if (deg < 293) return "পশ্চিম  W";
        return "উত্তর-পশ্চিম  NW";
    }

    private void updateObjective(MissionTracker tracker) {
        if (tracker == null) return;
        MissionObjective obj = tracker.getCurrentObjective();
        String key = obj.getId();
        if (key.equals(lastObjectiveKey)) return;
        lastObjectiveKey = key;

        if (objectivePicture != null) guiNode.detachChild(objectivePicture);

        int index = tracker.getCurrentObjectiveIndex() + 1;
        int total = tracker.getObjectiveCount();
        List<Line> lines = new ArrayList<>();
        lines.add(new Line(String.format("লক্ষ্য %d / %d   ·   OBJECTIVE", index, total), 11.5f, true, MUTED));
        lines.add(new Line(stripNumber(obj.getTitleBengali()), 19f, true, TEXT, 4));
        lines.add(new Line(stripNumber(obj.getTitleEnglish()), 14f, false, MUTED, 2));
        PanelStyle style = panelStyle(ACCENT);
        style.maxWidth = 560;
        objectivePicture = textRenderer.createPanelPicture("Objective", lines, style, null);
        objectivePicture.setPosition(MARGIN, screenHeight - objectivePicture.getHeight() - MARGIN);
        guiNode.attachChild(objectivePicture);
    }

    /** Objective titles start with "১. " / "1. "; the card shows the number separately. */
    private static String stripNumber(String title) {
        if (title == null) return "";
        return title.replaceFirst("^[0-9০-৯]+\\.\\s*", "");
    }

    private void updateStamina(float stamina, bd.july2024.level.PlayerInventory inventory) {
        int stInt = (int) stamina;
        int waterCount = inventory != null ? inventory.getWaterCount() : 0;
        int foodCount = inventory != null ? inventory.getFoodCount() : 0;
        int leafletCount = inventory != null ? inventory.getLeafletCount() : 0;
        String key = stInt + "_" + waterCount + "_" + foodCount + "_" + leafletCount;
        if (key.equals(lastStaminaKey)) return;
        lastStaminaKey = key;

        if (staminaPicture != null) guiNode.detachChild(staminaPicture);

        final float pct = Math.max(0f, Math.min(1f, stamina / 100f));
        final Color fill = pct > 0.5f ? new Color(116, 214, 128) : (pct > 0.25f ? AMBER : new Color(236, 92, 78));
        List<Line> lines = new ArrayList<>();
        lines.add(new Line("শক্তি  ·  STAMINA", 11.5f, true, MUTED));
        if (inventory != null) {
            String invText = String.format("[1] পানি: %d  ·  [2] খাবার: %d", waterCount, foodCount) + (leafletCount > 0 ? "  ·  লিফলেট: " + leafletCount : "");
            lines.add(new Line(invText, 11f, false, AMBER, 14));
        }
        PanelStyle style = panelStyle(null);
        style.minWidth = 270;
        style.extraHeight = inventory != null ? 8 : 14;
        staminaPicture = textRenderer.createPanelPicture("Stamina", lines, style, (Graphics2D g, int x, int y, int w, int h) -> {
            int barH = 6;
            int barY = y + 4;
            g.setColor(new Color(255, 255, 255, 42));
            g.fill(new RoundRectangle2D.Float(x, barY, w, barH, barH, barH));
            g.setColor(fill);
            g.fill(new RoundRectangle2D.Float(x, barY, Math.max(barH, w * pct), barH, barH, barH));
        });
        staminaPicture.setPosition(MARGIN, MARGIN);
        guiNode.attachChild(staminaPicture);
    }

    private void updatePrompt(String prompt, boolean dialogueActive) {
        if (dialogueActive || prompt == null || prompt.isEmpty()) {
            if (promptPicture != null) {
                guiNode.detachChild(promptPicture);
                promptPicture = null;
            }
            lastPromptText = "";
            return;
        }
        if (prompt.equals(lastPromptText)) return;
        lastPromptText = prompt;

        if (promptPicture != null) guiNode.detachChild(promptPicture);

        String text = prompt.startsWith("[E] ") ? prompt.substring(4) : prompt;
        List<Line> lines = new ArrayList<>();
        lines.add(new Line("E", 12f, true, AMBER));
        lines.add(new Line(text, 16f, true, TEXT, 1));
        PanelStyle style = panelStyle(AMBER);
        style.paddingX = 18;
        promptPicture = textRenderer.createPanelPicture("Prompt", lines, style, null);
        promptPicture.setPosition(screenWidth / 2f - promptPicture.getWidth() / 2f, 96f);
        guiNode.attachChild(promptPicture);
    }

    private void updateDialogue(DialogueManager dialogueManager) {
        if (dialogueManager == null || !dialogueManager.isActive()) {
            if (dialoguePicture != null) {
                guiNode.detachChild(dialoguePicture);
                dialoguePicture = null;
            }
            lastDialogueText = "";
            return;
        }
        DialogueLine line = dialogueManager.getCurrentLine();
        if (line == null) return;
        bd.july2024.GameSettings gs = bd.july2024.GameSettings.get();
        String lineKey = line.getSpeaker() + "_" + line.getTextBengali() + "_" + gs.subtitles;
        if (lineKey.equals(lastDialogueText)) return;
        lastDialogueText = lineKey;

        if (dialoguePicture != null) guiNode.detachChild(dialoguePicture);

        List<Line> lines = new ArrayList<>();
        lines.add(new Line(line.getSpeaker(), 13f, true, AMBER));
        boolean bn = gs.showBengaliSubtitles();
        if (bn) lines.add(new Line(line.getTextBengali(), 18f, true, TEXT, 4));
        if (gs.showEnglishSubtitles()) lines.add(new Line(line.getTextEnglish(), bn ? 15f : 17f, !bn, bn ? MUTED : TEXT, bn ? 3 : 4));
        lines.add(new Line("E / Enter  ·  পরবর্তী (next)", 11.5f, false, MUTED, 10));
        PanelStyle style = panelStyle(ACCENT);
        style.paddingX = 22;
        style.paddingY = 14;
        style.minWidth = 720;
        style.maxWidth = 920;
        dialoguePicture = textRenderer.createPanelPicture("Dialogue", lines, style, null);
        dialoguePicture.setPosition(screenWidth / 2f - dialoguePicture.getWidth() / 2f, 48f);
        guiNode.attachChild(dialoguePicture);
    }
}
