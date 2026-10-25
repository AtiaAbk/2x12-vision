package bd.july2024.ui;

import bd.july2024.dialogue.DialogueLine;
import bd.july2024.dialogue.DialogueManager;
import bd.july2024.level.MissionObjective;
import bd.july2024.level.MissionTracker;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.ui.Picture;

import java.awt.Color;

/**
 * Cinematic Documentary-style HUD for Project July 2024.
 * Renders compass, objective banner, distance indicator, stamina gauge,
 * interaction prompts, and active bilingual dialogue subtitles.
 */
public class ProjectJulyHUD {

    private final Node guiNode;
    private final BengaliTextRenderer textRenderer;
    private final int screenWidth;
    private final int screenHeight;

    private Picture compassPicture;
    private Picture objectivePicture;
    private Picture promptPicture;
    private Picture dialoguePicture;
    private Picture staminaPicture;

    private String lastObjectiveId = "";
    private String lastPromptText = "";
    private String lastDialogueText = "";
    private int lastCompassDeg = -999;
    private int lastStaminaInt = -1;

    public ProjectJulyHUD(Node guiNode, BengaliTextRenderer textRenderer, int screenWidth, int screenHeight) {
        this.guiNode = guiNode;
        this.textRenderer = textRenderer;
        this.screenWidth = screenWidth > 0 ? screenWidth : 1280;
        this.screenHeight = screenHeight > 0 ? screenHeight : 720;
    }

    public void update(Vector3f playerPos, float cameraYawDegrees, MissionTracker tracker, DialogueManager dialogueManager, String contextPrompt, float stamina) {
        updateCompass(cameraYawDegrees, playerPos, tracker);
        updateObjective(playerPos, tracker);
        updateStamina(stamina);
        updatePrompt(contextPrompt, dialogueManager.isActive());
        updateDialogue(dialogueManager);
    }

    private void updateCompass(float cameraYaw, Vector3f playerPos, MissionTracker tracker) {
        // Camera yaw 0 looks along +Z, which is south in the campus layout (north = -Z), so the
        // geographic bearing shown is the yaw turned by 180 degrees.
        float bearing = cameraYaw + 180f;
        int yawInt = ((int) ((bearing % 360f + 360f) % 360f)) / 5 * 5;
        if (yawInt == lastCompassDeg) return;
        lastCompassDeg = yawInt;

        String cardinal = getCardinal(yawInt);
        float dist = 0f;
        if (tracker != null && playerPos != null) {
            dist = playerPos.distance(tracker.getCurrentObjective().getTargetPosition());
        }

        String compassStr = String.format("◆ %s (%d°)  |  লক্ষ্য: %.1f মি.", cardinal, yawInt, dist);

        if (compassPicture != null) {
            guiNode.detachChild(compassPicture);
        }

        compassPicture = textRenderer.createTextPicture(
            "Compass",
            compassStr,
            15f,
            true,
            new Color(255, 235, 180),
            new Color(15, 20, 26, 200),
            14,
            6
        );
        compassPicture.setPosition(screenWidth - compassPicture.getWidth() - 24f, screenHeight - compassPicture.getHeight() - 20f);
        guiNode.attachChild(compassPicture);
    }

    private String getCardinal(int deg) {
        if (deg >= 338 || deg < 23) return "উ (N)";
        if (deg < 68) return "উ-পূ (NE)";
        if (deg < 113) return "পূ (E)";
        if (deg < 158) return "দ-পূ (SE)";
        if (deg < 203) return "দ (S)";
        if (deg < 248) return "দ-প (SW)";
        if (deg < 293) return "প (W)";
        return "উ-প (NW)";
    }

    private void updateObjective(Vector3f playerPos, MissionTracker tracker) {
        if (tracker == null) return;
        MissionObjective obj = tracker.getCurrentObjective();
        float dist = playerPos != null ? playerPos.distance(obj.getTargetPosition()) : 0f;

        String key = obj.getId() + "_" + (int) dist;
        if (key.equals(lastObjectiveId)) return;
        lastObjectiveId = key;

        if (objectivePicture != null) {
            guiNode.detachChild(objectivePicture);
        }

        String objText = String.format("%s  [%.1fm]\n%s", obj.getTitleBengali(), dist, obj.getTitleEnglish());
        objectivePicture = textRenderer.createTextPicture(
            "Objective",
            objText,
            17f,
            true,
            new Color(255, 225, 90),
            new Color(20, 25, 32, 215),
            16,
            10
        );
        objectivePicture.setPosition(24f, screenHeight - objectivePicture.getHeight() - 20f);
        guiNode.attachChild(objectivePicture);
    }

    private void updateStamina(float stamina) {
        int stInt = (int) stamina;
        if (stInt == lastStaminaInt) return;
        lastStaminaInt = stInt;

        if (staminaPicture != null) {
            guiNode.detachChild(staminaPicture);
        }

        String stStr = String.format("শক্তি (STAMINA): %d%%", stInt);
        Color barColor = stInt > 30 ? new Color(110, 220, 130) : new Color(240, 90, 80);

        staminaPicture = textRenderer.createTextPicture(
            "Stamina",
            stStr,
            14f,
            true,
            barColor,
            new Color(15, 20, 25, 180),
            12,
            5
        );
        staminaPicture.setPosition(24f, 24f);
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

        if (promptPicture != null) {
            guiNode.detachChild(promptPicture);
        }

        promptPicture = textRenderer.createTextPicture(
            "Prompt",
            prompt,
            18f,
            true,
            new Color(255, 235, 110),
            new Color(12, 16, 22, 230),
            20,
            10
        );
        promptPicture.setPosition(screenWidth / 2f - promptPicture.getWidth() / 2f, 110f);
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

        String lineKey = line.getSpeaker() + "_" + line.getTextBengali();
        if (lineKey.equals(lastDialogueText)) return;
        lastDialogueText = lineKey;

        if (dialoguePicture != null) {
            guiNode.detachChild(dialoguePicture);
        }

        String dialogueStr = String.format("▶ %s\n%s\n\"%s\"\n[E / ENTER] পরবর্তী (Advance)",
            line.getSpeaker(), line.getTextBengali(), line.getTextEnglish());

        dialoguePicture = textRenderer.createTextPicture(
            "Dialogue",
            dialogueStr,
            17f,
            false,
            new Color(255, 255, 255),
            new Color(12, 16, 24, 240),
            24,
            14
        );
        dialoguePicture.setPosition(screenWidth / 2f - dialoguePicture.getWidth() / 2f, 75f);
        guiNode.attachChild(dialoguePicture);
    }
}
