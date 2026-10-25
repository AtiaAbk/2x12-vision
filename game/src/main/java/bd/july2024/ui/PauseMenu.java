package bd.july2024.ui;

import com.jme3.scene.Node;
import com.jme3.ui.Picture;

import bd.july2024.ui.BengaliTextRenderer.Line;
import bd.july2024.ui.BengaliTextRenderer.PanelStyle;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

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

        List<Line> lines = new ArrayList<>();
        lines.add(new Line("খেলা স্থগিত  ·  PAUSED", 12f, true, ProjectJulyHUD.MUTED));
        lines.add(new Line("প্রজেক্ট জুলাই ২০২৪", 22f, true, ProjectJulyHUD.TEXT, 4));
        lines.add(new Line("লেভেল ০১  ·  ঢাকা বিশ্ববিদ্যালয়: রাজু ভাস্কর্য, কেন্দ্রীয় লাইব্রেরি ও টিএসসি", 14f, false, ProjectJulyHUD.MUTED, 2));
        if (statusMessage != null && !statusMessage.isEmpty()) {
            lines.add(new Line(statusMessage, 14f, true, ProjectJulyHUD.AMBER, 10));
        }

        lines.add(new Line("নিয়ন্ত্রণ  ·  CONTROLS", 12f, true, ProjectJulyHUD.AMBER, 16));
        String[][] controls = {
            {"W A S D", "চলাফেরা  ·  move"},
            {"Shift", "দৌড়  ·  sprint (uses stamina)"},
            {"Space", "লাফ  ·  jump"},
            {"C", "নিচু হওয়া  ·  crouch"},
            {"E / Enter", "কথা বলুন, পড়ুন  ·  interact"},
            {"Mouse", "ক্যামেরা  ·  look around"},
            {"F12", "স্ক্রিনশট  ·  screenshot"},
        };
        for (String[] c : controls) {
            lines.add(new Line(String.format("%-10s  %s", c[0], c[1]), 14f, false, ProjectJulyHUD.TEXT, 3));
        }

        lines.add(new Line("মেনু  ·  MENU", 12f, true, ProjectJulyHUD.AMBER, 16));
        String[][] menu = {
            {"Esc", "খেলা চালিয়ে যান  ·  resume"},
            {"S", "চেকপয়েন্ট সংরক্ষণ  ·  save checkpoint"},
            {"R", "লেভেল ০১ পুনরায় শুরু  ·  restart level"},
            {"Q", "প্রস্থান  ·  quit"},
        };
        for (String[] m : menu) {
            lines.add(new Line(String.format("%-10s  %s", m[0], m[1]), 14f, false, ProjectJulyHUD.TEXT, 3));
        }

        PanelStyle style = new PanelStyle();
        style.background = new Color(10, 12, 16, 228);
        style.accent = ProjectJulyHUD.ACCENT;
        style.paddingX = 30;
        style.paddingY = 22;
        style.minWidth = 520;
        style.maxWidth = 760;
        menuPicture = textRenderer.createPanelPicture("PauseMenuModal", lines, style, null);

        float x = (screenWidth - menuPicture.getWidth()) / 2f;
        float y = (screenHeight - menuPicture.getHeight()) / 2f;
        menuPicture.setPosition(Math.max(10f, x), Math.max(10f, y));
        guiNode.attachChild(menuPicture);
    }
}
