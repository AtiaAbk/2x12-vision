package bd.july2024.ui;

import bd.july2024.GameSettings;
import bd.july2024.ui.BengaliTextRenderer.Line;
import bd.july2024.ui.BengaliTextRenderer.PanelStyle;
import com.jme3.scene.Node;
import com.jme3.ui.Picture;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Keyboard-driven front end and pause menu: title screen, main menu, settings, chapter card and the
 * in-game pause menu. All screens are drawn with {@link BengaliTextRenderer#renderPanel} so they
 * share the HUD's visual language. Navigation: up/down to move, left/right to change a value,
 * Enter/E/Space to select, Esc to go back.
 */
public class MenuSystem {

    public enum Screen { NONE, TITLE, MAIN, SETTINGS, CHAPTER, PAUSE, CHAPTER_COMPLETE }

    /** What the game does when a menu entry is chosen. */
    public interface Actions {
        void newGame();
        void continueGame();
        void resume();
        void saveCheckpoint();
        void restartLevel();
        void toMainMenu();
        void quit();
        void settingsChanged();
        boolean hasCheckpoint();
        void playClick();
        void playConfirm();
        void playBack();
        default void openMap() {}
    }

    private static final Color DIM = new Color(0, 0, 0, 120);

    private final Node guiNode;
    private final BengaliTextRenderer textRenderer;
    private final int screenWidth;
    private final int screenHeight;
    private final Actions actions;
    private final GameSettings settings;

    private Screen screen = Screen.NONE;
    private Screen settingsReturnTo = Screen.MAIN;
    private Picture dimPicture;
    private Picture panelPicture;
    private int selected = 0;
    private String statusMessage = "";
    private float chapterTimer = 0f;
    private Runnable chapterDone;

    public MenuSystem(Node guiNode, BengaliTextRenderer textRenderer, int screenWidth, int screenHeight, GameSettings settings, Actions actions) {
        this.guiNode = guiNode;
        this.textRenderer = textRenderer;
        this.screenWidth = screenWidth > 0 ? screenWidth : 1280;
        this.screenHeight = screenHeight > 0 ? screenHeight : 720;
        this.settings = settings;
        this.actions = actions;
    }

    public Screen getScreen() {
        return screen;
    }

    public boolean isOpen() {
        return screen != Screen.NONE;
    }

    /** True while a menu blocks gameplay input (every screen except the chapter card, which only waits). */
    public boolean blocksGameplay() {
        return screen != Screen.NONE;
    }

    public boolean isPauseOpen() {
        return screen == Screen.PAUSE;
    }

    public boolean isFrontEnd() {
        return screen == Screen.TITLE || screen == Screen.MAIN || (screen == Screen.SETTINGS && settingsReturnTo == Screen.MAIN);
    }

    // ---- opening screens -------------------------------------------------------------------------

    public void showTitle() {
        screen = Screen.TITLE;
        selected = 0;
        rebuild();
    }

    public void showMain() {
        screen = Screen.MAIN;
        selected = 0;
        statusMessage = "";
        rebuild();
    }

    public void showSettings(Screen returnTo) {
        settingsReturnTo = returnTo;
        screen = Screen.SETTINGS;
        selected = 0;
        rebuild();
    }

    public void showChapter(Runnable whenDone) {
        screen = Screen.CHAPTER;
        chapterTimer = 0f;
        chapterDone = whenDone;
        rebuild();
    }

    public void showChapterComplete() {
        screen = Screen.CHAPTER_COMPLETE;
        selected = 0;
        statusMessage = "";
        rebuild();
    }

    public void showPause() {
        screen = Screen.PAUSE;
        selected = 0;
        statusMessage = "";
        rebuild();
    }

    public void close() {
        screen = Screen.NONE;
        statusMessage = "";
        detach();
    }

    public void setStatusMessage(String msg) {
        statusMessage = msg == null ? "" : msg;
        if (isOpen()) rebuild();
    }

    /** Advances the chapter card timer; the card closes itself after a few seconds. */
    public void update(float tpf) {
        if (screen == Screen.CHAPTER) {
            chapterTimer += tpf;
            if (chapterTimer > 5f) finishChapter();
        }
    }

    // ---- input ---------------------------------------------------------------------------------

    public void onUp() {
        if (!hasList()) return;
        selected = (selected - 1 + itemCount()) % itemCount();
        actions.playClick();
        rebuild();
    }

    public void onDown() {
        if (!hasList()) return;
        selected = (selected + 1) % itemCount();
        actions.playClick();
        rebuild();
    }

    public void onLeft() {
        if (screen == Screen.SETTINGS) adjustSetting(-1);
    }

    public void onRight() {
        if (screen == Screen.SETTINGS) adjustSetting(1);
    }

    public void onSelect() {
        switch (screen) {
            case TITLE:
                actions.playConfirm();
                showMain();
                break;
            case MAIN:
                selectMain();
                break;
            case SETTINGS:
                if (selected == 14) {
                    settings.restoreDefaults();
                    actions.playClick();
                    actions.settingsChanged();
                    rebuild();
                } else if (selected == SETTINGS_ITEMS - 1) {
                    leaveSettings();
                } else {
                    adjustSetting(1);
                }
                break;
            case CHAPTER:
                finishChapter();
                break;
            case CHAPTER_COMPLETE:
                selectChapterComplete();
                break;
            case PAUSE:
                selectPause();
                break;
            default:
                break;
        }
    }

    /** Handles mouse clicks on menus and interactive cards. */
    public void onClick(float mouseX, float mouseY) {
        if (!isOpen()) return;

        if (screen == Screen.TITLE) {
            onSelect();
            return;
        }

        if (screen == Screen.CHAPTER) {
            finishChapter();
            return;
        }

        if (panelPicture == null) return;

        float px = panelPicture.getLocalTranslation().x;
        float py = panelPicture.getLocalTranslation().y;
        float pw = panelPicture.getWidth();
        float ph = panelPicture.getHeight();

        if (mouseX >= px && mouseX <= px + pw && mouseY >= py && mouseY <= py + ph) {
            float fromTop = (py + ph) - mouseY;

            float headerH = 75f;
            if (screen == Screen.CHAPTER_COMPLETE) headerH = 175f;
            else if (screen == Screen.MAIN) headerH = 70f;
            else if (screen == Screen.PAUSE) headerH = 75f;
            else if (screen == Screen.SETTINGS) headerH = 65f;

            float itemH = screen == Screen.SETTINGS ? 26f : 34f;
            if (fromTop >= headerH) {
                int clickedIndex = (int) ((fromTop - headerH) / itemH);
                if (clickedIndex >= 0 && clickedIndex < itemCount()) {
                    selected = clickedIndex;
                    if (screen == Screen.SETTINGS && selected < 14) {
                        float relX = (mouseX - px) / pw;
                        if (relX < 0.5f) {
                            onLeft();
                        } else {
                            onRight();
                        }
                    } else {
                        onSelect();
                    }
                }
            }
        }
    }

    /** Esc: back out of the current screen. Returns false if nothing was open. */
    public boolean onBack() {
        switch (screen) {
            case TITLE:
                return true; // stay on the title
            case MAIN:
                actions.playBack();
                showTitle();
                return true;
            case SETTINGS:
                leaveSettings();
                return true;
            case CHAPTER:
                finishChapter();
                return true;
            case CHAPTER_COMPLETE:
                actions.playBack();
                actions.toMainMenu();
                return true;
            case PAUSE:
                actions.playBack();
                actions.resume();
                return true;
            default:
                return false;
        }
    }

    // ---- screen logic ----------------------------------------------------------------------------

    private static final String[][] MAIN_ITEMS = {
        {"নতুন খেলা", "New game"},
        {"চালিয়ে যান", "Continue"},
        {"সেটিংস", "Settings"},
        {"প্রস্থান", "Quit"},
    };
    private static final String[][] PAUSE_ITEMS = {
        {"খেলা চালিয়ে যান", "Resume"},
        {"ক্যাম্পাস মানচিত্র", "Campus map"},
        {"চেকপয়েন্ট সংরক্ষণ", "Save checkpoint"},
        {"সেটিংস", "Settings"},
        {"লেভেল পুনরায় শুরু", "Restart level"},
        {"মূল মেনু", "Main menu"},
        {"প্রস্থান", "Quit"},
    };
    private static final String[][] CHAPTER_COMPLETE_ITEMS = {
        {"মূল মেনুতে ফিরে যান", "Main menu"},
        {"লেভেল পুনরায় শুরু", "Restart level"},
    };
    private static final int SETTINGS_ITEMS = 16;

    private boolean hasList() {
        return screen == Screen.MAIN || screen == Screen.SETTINGS || screen == Screen.PAUSE || screen == Screen.CHAPTER_COMPLETE;
    }

    private int itemCount() {
        switch (screen) {
            case MAIN: return MAIN_ITEMS.length;
            case PAUSE: return PAUSE_ITEMS.length;
            case SETTINGS: return SETTINGS_ITEMS;
            case CHAPTER_COMPLETE: return CHAPTER_COMPLETE_ITEMS.length;
            default: return 1;
        }
    }

    private void selectMain() {
        switch (selected) {
            case 0: actions.playConfirm(); actions.newGame(); break;
            case 1:
                if (actions.hasCheckpoint()) { actions.playConfirm(); actions.continueGame(); }
                else { actions.playBack(); setStatusMessage("কোনো সংরক্ষিত চেকপয়েন্ট নেই  ·  no saved checkpoint"); }
                break;
            case 2: actions.playConfirm(); showSettings(Screen.MAIN); break;
            case 3: actions.playBack(); actions.quit(); break;
            default: break;
        }
    }

    private void selectPause() {
        switch (selected) {
            case 0: actions.playConfirm(); actions.resume(); break;
            case 1: actions.playConfirm(); close(); actions.openMap(); break;
            case 2: actions.playConfirm(); actions.saveCheckpoint(); break;
            case 3: actions.playConfirm(); showSettings(Screen.PAUSE); break;
            case 4: actions.playConfirm(); actions.restartLevel(); break;
            case 5: actions.playBack(); actions.toMainMenu(); break;
            case 6: actions.playBack(); actions.quit(); break;
            default: break;
        }
    }

    private void selectChapterComplete() {
        switch (selected) {
            case 0: actions.playBack(); actions.toMainMenu(); break;
            case 1: actions.playConfirm(); actions.restartLevel(); break;
            default: break;
        }
    }

    private void leaveSettings() {
        settings.save();
        actions.settingsChanged();
        actions.playBack();
        if (settingsReturnTo == Screen.PAUSE) showPause(); else showMain();
    }

    private void adjustSetting(int dir) {
        switch (selected) {
            case 0: {
                int count = GameSettings.PRESET_NAMES.length;
                int next = (settings.graphicsPresetIndex + dir + count) % count;
                settings.applyPreset(next);
                break;
            }
            case 1: settings.adaptivePerformance = !settings.adaptivePerformance; break;
            case 2: {
                int idx = 0;
                for (int i = 0; i < GameSettings.TARGET_FPS_OPTIONS.length; i++) {
                    if (GameSettings.TARGET_FPS_OPTIONS[i] == settings.targetFps) idx = i;
                }
                idx = (idx + dir + GameSettings.TARGET_FPS_OPTIONS.length) % GameSettings.TARGET_FPS_OPTIONS.length;
                settings.targetFps = GameSettings.TARGET_FPS_OPTIONS[idx];
                break;
            }
            case 3: {
                int sm = GameSettings.SHADOW_MODES.length;
                settings.shadowQuality = (settings.shadowQuality + dir + sm) % sm;
                settings.graphicsPresetIndex = GameSettings.PRESET_CUSTOM;
                break;
            }
            case 4: {
                settings.ssao = !settings.ssao;
                settings.graphicsPresetIndex = GameSettings.PRESET_CUSTOM;
                break;
            }
            case 5: {
                settings.fxaa = !settings.fxaa;
                settings.graphicsPresetIndex = GameSettings.PRESET_CUSTOM;
                break;
            }
            case 6: {
                int vd = GameSettings.VIEW_DISTANCES.length;
                settings.viewDistanceIndex = (settings.viewDistanceIndex + dir + vd) % vd;
                settings.graphicsPresetIndex = GameSettings.PRESET_CUSTOM;
                break;
            }
            case 7: {
                int vl = GameSettings.VEGETATION_LODS.length;
                settings.vegetationLodIndex = (settings.vegetationLodIndex + dir + vl) % vl;
                settings.graphicsPresetIndex = GameSettings.PRESET_CUSTOM;
                break;
            }
            case 8: settings.mouseSensitivity = clamp(round1(settings.mouseSensitivity + 0.1f * dir), 0.3f, 3.0f); break;
            case 9: settings.invertMouseY = !settings.invertMouseY; break;
            case 10: settings.masterVolume = clamp(Math.round((settings.masterVolume + 0.05f * dir) * 20f) / 20f, 0f, 1f); break;
            case 11: {
                int i = indexOf(GameSettings.SUBTITLE_MODES, settings.subtitles);
                settings.subtitles = GameSettings.SUBTITLE_MODES[(i + dir + GameSettings.SUBTITLE_MODES.length) % GameSettings.SUBTITLE_MODES.length];
                break;
            }
            case 12: settings.windowSizeIndex = (settings.windowSizeIndex + dir + GameSettings.WINDOW_SIZES.length) % GameSettings.WINDOW_SIZES.length; break;
            case 13: settings.fullscreen = !settings.fullscreen; break;
            case 14:
                settings.restoreDefaults();
                break;
            case 15:
                leaveSettings();
                return;
            default: return;
        }
        actions.playClick();
        actions.settingsChanged();
        rebuild();
    }

    private void finishChapter() {
        Runnable r = chapterDone;
        chapterDone = null;
        close();
        if (r != null) r.run();
    }

    // ---- drawing ---------------------------------------------------------------------------------

    private void detach() {
        if (panelPicture != null) { guiNode.detachChild(panelPicture); panelPicture = null; }
        if (dimPicture != null) { guiNode.detachChild(dimPicture); dimPicture = null; }
    }

    public void rebuild() {
        detach();
        if (screen == Screen.NONE) return;
        switch (screen) {
            case TITLE: drawTitle(); break;
            case MAIN: drawMain(); break;
            case SETTINGS: drawSettings(); break;
            case CHAPTER: drawChapter(); break;
            case CHAPTER_COMPLETE: drawChapterComplete(); break;
            case PAUSE: drawPause(); break;
            default: break;
        }
    }

    private void drawDim(Color color) {
        List<Line> none = new ArrayList<>();
        none.add(new Line(" ", 1f, false, color));
        PanelStyle s = new PanelStyle();
        s.background = color;
        s.border = new Color(0, 0, 0, 0);
        s.radius = 0;
        s.paddingX = 0;
        s.paddingY = 0;
        s.minWidth = screenWidth;
        s.maxWidth = screenWidth;
        s.extraHeight = screenHeight;
        dimPicture = textRenderer.createPanelPicture("MenuDim", none, s, null);
        dimPicture.setPosition(0, screenHeight - dimPicture.getHeight());
        guiNode.attachChild(dimPicture);
    }

    private void drawTitle() {
        drawDim(new Color(4, 6, 10, 150));
        List<Line> lines = new ArrayList<>();
        lines.add(new Line("জুলাই ২০২৪", 13f, true, ProjectJulyHUD.AMBER));
        lines.add(new Line("প্রজেক্ট জুলাই ২০২৪", 44f, true, ProjectJulyHUD.TEXT, 6));
        lines.add(new Line("PROJECT JULY 2024", 18f, false, ProjectJulyHUD.MUTED, 2));
        lines.add(new Line("বৈষম্যবিরোধী ছাত্র আন্দোলনের দিনগুলোতে ঢাকা বিশ্ববিদ্যালয়", 15f, false, ProjectJulyHUD.TEXT, 18));
        lines.add(new Line("Dhaka University in the days of the Anti-Discrimination Student Movement", 13f, false, ProjectJulyHUD.MUTED, 2));
        lines.add(new Line("আরিফ ও তার বন্ধুরা কাল্পনিক চরিত্র; স্থান, তারিখ ও ঘটনাপ্রবাহ নথিভুক্ত ইতিহাস অনুসরণ করে।", 12f, false, ProjectJulyHUD.MUTED, 14));
        lines.add(new Line("Arif and his friends are fictional; places, dates and events follow the documented record.", 11.5f, false, ProjectJulyHUD.MUTED, 1));
        lines.add(new Line("Enter চাপুন  ·  press Enter", 15f, true, ProjectJulyHUD.AMBER, 26));
        lines.add(new Line("vertical slice  ·  2026-10-26", 10.5f, false, new Color(140, 146, 152), 10));
        PanelStyle s = new PanelStyle();
        s.background = new Color(8, 10, 14, 170);
        s.accent = ProjectJulyHUD.ACCENT;
        s.paddingX = 44;
        s.paddingY = 34;
        s.minWidth = 760;
        s.maxWidth = 900;
        panelPicture = textRenderer.createPanelPicture("TitlePanel", lines, s, gradientOverlay());
        centre(panelPicture, 0.56f);
    }

    private void drawMain() {
        drawDim(DIM);
        List<Line> lines = new ArrayList<>();
        lines.add(new Line("প্রজেক্ট জুলাই ২০২৪", 26f, true, ProjectJulyHUD.TEXT));
        lines.add(new Line("মূল মেনু  ·  MAIN MENU", 11.5f, true, ProjectJulyHUD.MUTED, 2));
        for (int i = 0; i < MAIN_ITEMS.length; i++) {
            boolean enabled = i != 1 || actions.hasCheckpoint();
            lines.add(item(MAIN_ITEMS[i][0], MAIN_ITEMS[i][1], null, i == selected, enabled, i == 0 ? 20 : 8));
        }
        if (!statusMessage.isEmpty()) lines.add(new Line(statusMessage, 12.5f, true, ProjectJulyHUD.AMBER, 16));
        lines.add(new Line("Up / Down নির্বাচন  ·  Enter বাছাই  ·  Esc ফিরে যান", 11f, false, ProjectJulyHUD.MUTED, 22));
        panelPicture = textRenderer.createPanelPicture("MainMenu", lines, menuStyle(), null);
        centre(panelPicture, 0.5f);
    }

    private void drawPause() {
        drawDim(DIM);
        List<Line> lines = new ArrayList<>();
        lines.add(new Line("খেলা স্থগিত  ·  PAUSED", 11.5f, true, ProjectJulyHUD.MUTED));
        lines.add(new Line("লেভেল ০১  ·  ঢাকা বিশ্ববিদ্যালয়", 20f, true, ProjectJulyHUD.TEXT, 4));
        for (int i = 0; i < PAUSE_ITEMS.length; i++) {
            lines.add(item(PAUSE_ITEMS[i][0], PAUSE_ITEMS[i][1], null, i == selected, true, i == 0 ? 18 : 8));
        }
        if (!statusMessage.isEmpty()) lines.add(new Line(statusMessage, 12.5f, true, ProjectJulyHUD.AMBER, 16));
        lines.add(new Line("W A S D চলাফেরা  ·  Shift দৌড়  ·  Space লাফ  ·  C নিচু  ·  E কথা/নথি  ·  M মানচিত্র  ·  R ক্যামেরা  ·  F12 ছবি", 10.5f, false, ProjectJulyHUD.MUTED, 22));
        lines.add(new Line("Up / Down নির্বাচন  ·  Enter বাছাই  ·  Esc ফিরে যান", 11f, false, ProjectJulyHUD.MUTED, 4));
        panelPicture = textRenderer.createPanelPicture("PauseMenu", lines, menuStyle(), null);
        centre(panelPicture, 0.5f);
    }

    private void drawSettings() {
        drawDim(DIM);
        List<Line> lines = new ArrayList<>();
        lines.add(new Line("সেটিংস  ·  SETTINGS", 20f, true, ProjectJulyHUD.TEXT));
        lines.add(new Line("Left / Right মান বদলান  ·  Enter / Click বাছাই  ·  Esc ফিরে যান", 11f, false, ProjectJulyHUD.MUTED, 2));

        String[] subLabels = {"বাংলা + English", "শুধু বাংলা", "English only"};
        int sub = indexOf(GameSettings.SUBTITLE_MODES, settings.subtitles);
        String win = settings.windowWidth() + " × " + settings.windowHeight();
        String fpsStr = settings.targetFps > 0 ? (settings.targetFps + " FPS") : "সীমাহীন · Unlimited";

        lines.add(item("গ্রাফিক্স প্রিসেট", "graphics preset", GameSettings.PRESET_NAMES[settings.graphicsPresetIndex], selected == 0, true, 10));
        lines.add(item("অভিযোজিত পারফরম্যান্স", "adaptive performance", settings.adaptivePerformance ? "চালু · on" : "বন্ধ · off", selected == 1, true, 3));
        lines.add(item("ফ্রেমরেট টার্গেট", "target frame rate", fpsStr, selected == 2, true, 3));
        lines.add(item("ছায়া", "shadow quality", GameSettings.SHADOW_MODES[settings.shadowQuality], selected == 3, true, 3));
        lines.add(item("অ্যাম্বিয়েন্ট অকলুশন (SSAO)", "ambient occlusion", settings.ssao ? "চালু · on" : "বন্ধ · off", selected == 4, true, 3));
        lines.add(item("অ্যান্টি-অ্যালাইজিং (FXAA)", "anti-aliasing", settings.fxaa ? "চালু · on" : "বন্ধ · off", selected == 5, true, 3));
        lines.add(item("দৃশ্যমান দূরত্ব", "view distance", GameSettings.VIEW_DISTANCES[settings.viewDistanceIndex], selected == 6, true, 3));
        lines.add(item("গাছপালার ডিটেইল", "foliage LOD", GameSettings.VEGETATION_LODS[settings.vegetationLodIndex], selected == 7, true, 3));
        lines.add(item("মাউস সংবেদনশীলতা", "mouse sensitivity", String.format(java.util.Locale.ROOT, "%.1f", settings.mouseSensitivity), selected == 8, true, 6));
        lines.add(item("মাউস Y উল্টানো", "invert mouse Y", settings.invertMouseY ? "চালু · on" : "বন্ধ · off", selected == 9, true, 3));
        lines.add(item("শব্দের মাত্রা", "master volume", Math.round(settings.masterVolume * 100) + " %", selected == 10, true, 3));
        lines.add(item("সাবটাইটেল", "subtitles", subLabels[Math.max(0, sub)], selected == 11, true, 3));
        lines.add(item("উইন্ডোর আকার", "window size", win + " (restart)", selected == 12, true, 3));
        lines.add(item("ফুলস্ক্রিন", "fullscreen", (settings.fullscreen ? "চালু · on" : "বন্ধ · off") + " (restart)", selected == 13, true, 3));
        lines.add(item("ডিফল্ট সেটিংস ফিরুন", "restore defaults", null, selected == 14, true, 8));
        lines.add(item("ফিরে যান ও সংরক্ষণ", "save & back", null, selected == 15, true, 4));

        PanelStyle s = menuStyle();
        s.minWidth = 660;
        s.maxWidth = 920;
        s.paddingY = 16;
        panelPicture = textRenderer.createPanelPicture("Settings", lines, s, null);
        centre(panelPicture, 0.5f);
    }

    private void drawChapter() {
        drawDim(new Color(4, 6, 10, 215));
        List<Line> lines = new ArrayList<>();
        lines.add(new Line("অধ্যায় ১  ·  CHAPTER 1", 13f, true, ProjectJulyHUD.AMBER));
        lines.add(new Line("যেখানে সব শুরু", 40f, true, ProjectJulyHUD.TEXT, 8));
        lines.add(new Line("Where It All Began", 18f, false, ProjectJulyHUD.MUTED, 2));
        lines.add(new Line("ঢাকা বিশ্ববিদ্যালয়  ·  জুলাই ২০২৪-এর প্রথম দিনগুলো", 15f, false, ProjectJulyHUD.TEXT, 22));
        lines.add(new Line("Dhaka University, the first days of July 2024", 13f, false, ProjectJulyHUD.MUTED, 2));
        lines.add(new Line("কোটা সংস্কারের দাবিতে শান্তিপূর্ণ সমাবেশ। রাজু ভাস্কর্যে রাফির সাথে দেখা করুন।", 13f, false, ProjectJulyHUD.TEXT, 14));
        lines.add(new Line("Peaceful assembly for quota reform. Meet Rafi at the Raju Memorial Sculpture.", 12f, false, ProjectJulyHUD.MUTED, 1));
        lines.add(new Line("Enter  ·  চলুন", 13f, true, ProjectJulyHUD.AMBER, 24));
        PanelStyle s = new PanelStyle();
        s.background = new Color(0, 0, 0, 0);
        s.border = new Color(0, 0, 0, 0);
        s.accent = ProjectJulyHUD.ACCENT;
        s.paddingX = 40;
        s.paddingY = 30;
        s.minWidth = 720;
        s.maxWidth = 880;
        panelPicture = textRenderer.createPanelPicture("ChapterCard", lines, s, null);
        centre(panelPicture, 0.5f);
    }

    private void drawChapterComplete() {
        drawDim(new Color(4, 6, 10, 220));
        List<Line> lines = new ArrayList<>();
        lines.add(new Line("অধ্যায় ১ সম্পন্ন  ·  CHAPTER 1 COMPLETE", 14f, true, ProjectJulyHUD.AMBER));
        lines.add(new Line("ঐক্য ও ঐতিহাসিক সংকল্প", 34f, true, ProjectJulyHUD.TEXT, 8));
        lines.add(new Line("Solidarity and Historic Resolve", 16f, false, ProjectJulyHUD.MUTED, 2));
        lines.add(new Line("ঢাকা বিশ্ববিদ্যালয়  ·  জুলাই ২০২৪", 14f, false, ProjectJulyHUD.TEXT, 18));
        lines.add(new Line("শিক্ষার্থী ও সাধারণ মানুষের ঐক্যবদ্ধ অবস্থান আন্দোলনের প্রথম পর্যায় সফল করেছে।", 13.5f, false, ProjectJulyHUD.TEXT, 8));
        lines.add(new Line("The united stand of students and citizens marked the resolute beginning of the July Movement.", 12f, false, ProjectJulyHUD.MUTED, 2));
        for (int i = 0; i < CHAPTER_COMPLETE_ITEMS.length; i++) {
            lines.add(item(CHAPTER_COMPLETE_ITEMS[i][0], CHAPTER_COMPLETE_ITEMS[i][1], null, i == selected, true, i == 0 ? 22 : 8));
        }
        lines.add(new Line("Up / Down নির্বাচন  ·  Enter বাছাই", 11f, false, ProjectJulyHUD.MUTED, 20));
        panelPicture = textRenderer.createPanelPicture("ChapterComplete", lines, menuStyle(), null);
        centre(panelPicture, 0.5f);
    }

    private Line item(String bn, String en, String value, boolean sel, boolean enabled, int gap) {
        String text = (sel ? "▶  " : "    ") + bn + "   ·   " + en + (value != null ? "      " + value : "");
        Color c = !enabled ? new Color(120, 126, 132) : (sel ? ProjectJulyHUD.AMBER : ProjectJulyHUD.TEXT);
        return new Line(text, 16f, sel, c, gap);
    }

    private static PanelStyle menuStyle() {
        PanelStyle s = new PanelStyle();
        s.background = new Color(10, 12, 16, 232);
        s.accent = ProjectJulyHUD.ACCENT;
        s.paddingX = 34;
        s.paddingY = 26;
        s.minWidth = 560;
        s.maxWidth = 900;
        return s;
    }

    private BengaliTextRenderer.Overlay gradientOverlay() {
        return (Graphics2D g, int x, int y, int w, int h) -> {
            g.setPaint(new GradientPaint(x, y, new Color(214, 58, 48, 0), x + w, y, new Color(214, 58, 48, 70)));
            g.fillRect(x, y + h - 2, w, 2);
        };
    }

    private void centre(Picture p, float verticalCentre) {
        float x = screenWidth / 2f - p.getWidth() / 2f;
        float y = screenHeight * verticalCentre - p.getHeight() / 2f;
        p.setPosition(Math.max(0, x), Math.max(0, y));
        guiNode.attachChild(p);
    }

    private static int indexOf(String[] arr, String v) {
        for (int i = 0; i < arr.length; i++) if (arr[i].equals(v)) return i;
        return 0;
    }

    private static float clamp(float v, float lo, float hi) {
        return Math.max(lo, Math.min(hi, v));
    }

    private static float round1(float v) {
        return Math.round(v * 10f) / 10f;
    }
}
