package bd.july2024.ui;

import com.jme3.scene.Node;
import com.jme3.ui.Picture;

import java.awt.Color;

/**
 * Clean Documentary Pause Menu for Project July 2024.
 * Displays level summary, control keybindings, resume, save checkpoint, and exit actions.
 */
public class PauseMenu {

    private final Node guiNode;
    private final BengaliTextRenderer textRenderer;
    private final int screenWidth;
    private final int screenHeight;

    private Picture menuPicture;
    private boolean open = false;
    private String statusMessage = "";

    public PauseMenu(Node guiNode, BengaliTextRenderer textRenderer, int screenWidth, int screenHeight) {
        this.guiNode = guiNode;
        this.textRenderer = textRenderer;
        this.screenWidth = screenWidth > 0 ? screenWidth : 1280;
        this.screenHeight = screenHeight > 0 ? screenHeight : 720;
    }

    public void open() {
        this.open = true;
        rebuildPicture();
    }

    public void setStatusMessage(String msg) {
        this.statusMessage = msg;
        if (open) {
            rebuildPicture();
        }
    }

    public void close() {
        this.open = false;
        this.statusMessage = "";
        if (menuPicture != null) {
            guiNode.detachChild(menuPicture);
            menuPicture = null;
        }
    }

    public boolean isOpen() {
        return open;
    }

    public void toggle() {
        if (open) close();
        else open();
    }

    private void rebuildPicture() {
        if (menuPicture != null) {
            guiNode.detachChild(menuPicture);
        }

        String statusLine = (statusMessage != null && !statusMessage.isEmpty())
            ? "\n• " + statusMessage + "\n"
            : "";

        String content = String.format(
            "★ PROJECT JULY 2024 — খেলা স্থগিত (PAUSED) ★\n" +
            "লেভেল ০১: ঢাকা বিশ্ববিদ্যালয় — রাজু ভাস্কর্য, কেন্দ্রীয় লাইব্রেরি ও টিএসসি\n" +
            "%s\n" +
            "নিয়ন্ত্রণ সহায়িকা (Controls):\n" +
            " • [W / A / S / D] : চরিত্র পরিচালনা (Move Forward / Left / Back / Right)\n" +
            " • [LSHIFT]        : দ্রুত দৌড় ও গতি বৃদ্ধি (Sprint with Stamina)\n" +
            " • [SPACE]         : লাফ দিন (Jump over obstacles)\n" +
            " • [C]             : নিচু হওয়া (Crouch)\n" +
            " • [E / ENTER]     : সহযোদ্ধাদের সাথে কথা বলুন ও তথ্যফলক পাঠ (Interact)\n" +
            " • [মাউস (Mouse)]  : ক্যামেরা ৩৬০° সিনেমাটিক ভিউ (Orbit View)\n" +
            " • [F12]           : দৃশ্যপট ক্যাপচার (Take Screenshot)\n\n" +
            "মেনু অপশনসমূহ (Menu Actions):\n" +
            " • [ESC] : খেলা চালিয়ে যান (Resume Game)\n" +
            " • [S]   : চেকপয়েন্ট সংরক্ষণ করুন (Save Checkpoint)\n" +
            " • [R]   : লেভেল ০১ পুনরায় শুরু করুন (Restart Level 01)\n" +
            " • [Q]   : খেলায় প্রস্থান করুন (Quit to Desktop)",
            statusLine
        );

        menuPicture = textRenderer.createTextPicture(
            "PauseMenuModal",
            content,
            15f,
            false,
            new Color(255, 245, 220),
            new Color(15, 20, 26, 240),
            32,
            24
        );

        float x = (screenWidth - menuPicture.getWidth()) / 2f;
        float y = (screenHeight - menuPicture.getHeight()) / 2f;
        menuPicture.setPosition(Math.max(10f, x), Math.max(10f, y));
        guiNode.attachChild(menuPicture);
    }
}
