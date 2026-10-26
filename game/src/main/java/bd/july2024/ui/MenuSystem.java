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
 * Professional PC action-adventure front end, pause menu, and game UX system for Project July 2024.
 * Provides cohesive, accessible, English-only navigation across:
 * Title Screen, Main Menu, Load Checkpoint, Chapter Select, Categorized Settings,
 * Pause Menu, Mission Journal, Credits, Chapter Intro, and Chapter Complete screens.
 */
public class MenuSystem {

    public enum Screen {
        NONE,
        TITLE,
        MAIN,
        LOAD_GAME,
        CHAPTER_SELECT,
        SETTINGS,
        CHAPTER,
        PAUSE,
        JOURNAL,
        CREDITS,
        CHAPTER_COMPLETE
    }

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
        default String getCheckpointSummary() { return "Slot 01: Dhaka University Campus · Active"; }
        default List<String> getJournalEntries() { return new ArrayList<>(); }
    }

    private static final Color DIM = new Color(0, 0, 0, 150);

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
    private int settingsCategory = 0; // 0=Graphics, 1=Audio, 2=Controls, 3=Accessibility
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

    /** True while a menu blocks gameplay input. */
    public boolean blocksGameplay() {
        return screen != Screen.NONE;
    }

    public boolean isPauseOpen() {
        return screen == Screen.PAUSE;
    }

    public boolean isFrontEnd() {
        return screen == Screen.TITLE || screen == Screen.MAIN || screen == Screen.LOAD_GAME
            || screen == Screen.CHAPTER_SELECT || screen == Screen.CREDITS
            || (screen == Screen.SETTINGS && settingsReturnTo == Screen.MAIN);
    }

    // ---- Opening screens -------------------------------------------------------------------------

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

    public void showLoadGame() {
        screen = Screen.LOAD_GAME;
        selected = 0;
        statusMessage = "";
        rebuild();
    }

    public void showChapterSelect() {
        screen = Screen.CHAPTER_SELECT;
        selected = 0;
        statusMessage = "";
        rebuild();
    }

    public void showCredits() {
        screen = Screen.CREDITS;
        selected = 0;
        statusMessage = "";
        rebuild();
    }

    public void showJournal() {
        screen = Screen.JOURNAL;
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

    /** Advances chapter card timer; the card dismisses itself after 5.5 seconds or on input. */
    public void update(float tpf) {
        if (screen == Screen.CHAPTER) {
            chapterTimer += tpf;
            if (chapterTimer > 5.5f) finishChapter();
        }
    }

    // ---- Input handling --------------------------------------------------------------------------

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
            case LOAD_GAME:
                selectLoadGame();
                break;
            case CHAPTER_SELECT:
                selectChapterSelect();
                break;
            case CREDITS:
                actions.playBack();
                showMain();
                break;
            case JOURNAL:
                actions.playBack();
                showPause();
                break;
            case SETTINGS:
                selectSettings();
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

        if (screen == Screen.CREDITS || screen == Screen.JOURNAL) {
            onBack();
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
            else if (screen == Screen.SETTINGS) headerH = 75f;
            else if (screen == Screen.LOAD_GAME) headerH = 80f;
            else if (screen == Screen.CHAPTER_SELECT) headerH = 80f;

            float itemH = screen == Screen.SETTINGS ? 26f : 34f;
            if (fromTop >= headerH) {
                int clickedIndex = (int) ((fromTop - headerH) / itemH);
                if (clickedIndex >= 0 && clickedIndex < itemCount()) {
                    selected = clickedIndex;
                    if (screen == Screen.SETTINGS) {
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

    /** Esc: back out of current screen. Returns false if nothing was open. */
    public boolean onBack() {
        switch (screen) {
            case TITLE:
                return true;
            case MAIN:
                actions.playBack();
                showTitle();
                return true;
            case LOAD_GAME:
            case CHAPTER_SELECT:
            case CREDITS:
                actions.playBack();
                showMain();
                return true;
            case JOURNAL:
                actions.playBack();
                showPause();
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

    // ---- Screen Items & Selections ---------------------------------------------------------------

    private static final String[] MAIN_ITEMS = {
        "New Game",
        "Continue",
        "Load Checkpoint",
        "Chapter Select",
        "Settings",
        "Credits & Attributions",
        "Quit Game"
    };

    private static final String[] PAUSE_ITEMS = {
        "Resume",
        "Campus Navigation Map",
        "Mission Objectives & Journal",
        "Save Checkpoint",
        "Settings",
        "Restart Level",
        "Main Menu",
        "Quit Game"
    };

    private static final String[] CHAPTER_COMPLETE_ITEMS = {
        "Return to Main Menu",
        "Restart Level"
    };

    private static final String[] SETTINGS_CATEGORIES = {
        "Display & Graphics",
        "Audio Settings",
        "Controls & Keybinds",
        "Accessibility"
    };

    private boolean hasList() {
        return screen == Screen.MAIN || screen == Screen.SETTINGS || screen == Screen.PAUSE
            || screen == Screen.CHAPTER_COMPLETE || screen == Screen.LOAD_GAME || screen == Screen.CHAPTER_SELECT;
    }

    private int itemCount() {
        switch (screen) {
            case MAIN: return MAIN_ITEMS.length;
            case PAUSE: return PAUSE_ITEMS.length;
            case CHAPTER_COMPLETE: return CHAPTER_COMPLETE_ITEMS.length;
            case LOAD_GAME: return 2;
            case CHAPTER_SELECT: return 2;
            case SETTINGS:
                switch (settingsCategory) {
                    case 0: return 12; // Graphics options
                    case 1: return 7;  // Audio options
                    case 2: return 12; // Controls options
                    case 3: return 5;  // Accessibility options
                    default: return 10;
                }
            default: return 1;
        }
    }

    private void selectMain() {
        switch (selected) {
            case 0:
                actions.playConfirm();
                actions.newGame();
                break;
            case 1:
                if (actions.hasCheckpoint()) {
                    actions.playConfirm();
                    actions.continueGame();
                } else {
                    actions.playBack();
                    setStatusMessage("No checkpoint saved in current profile.");
                }
                break;
            case 2:
                if (actions.hasCheckpoint()) {
                    actions.playConfirm();
                    showLoadGame();
                } else {
                    actions.playBack();
                    setStatusMessage("No saved checkpoint available to load.");
                }
                break;
            case 3:
                actions.playConfirm();
                showChapterSelect();
                break;
            case 4:
                actions.playConfirm();
                showSettings(Screen.MAIN);
                break;
            case 5:
                actions.playConfirm();
                showCredits();
                break;
            case 6:
                actions.playBack();
                actions.quit();
                break;
            default:
                break;
        }
    }

    private void selectLoadGame() {
        if (selected == 0) {
            if (actions.hasCheckpoint()) {
                actions.playConfirm();
                actions.continueGame();
            } else {
                actions.playBack();
                setStatusMessage("Checkpoint save is empty.");
            }
        } else {
            actions.playBack();
            showMain();
        }
    }

    private void selectChapterSelect() {
        if (selected == 0) {
            actions.playConfirm();
            actions.newGame();
        } else {
            actions.playBack();
            showMain();
        }
    }

    private void selectPause() {
        switch (selected) {
            case 0: actions.playConfirm(); actions.resume(); break;
            case 1: actions.playConfirm(); close(); actions.openMap(); break;
            case 2: actions.playConfirm(); showJournal(); break;
            case 3: actions.playConfirm(); actions.saveCheckpoint(); break;
            case 4: actions.playConfirm(); showSettings(Screen.PAUSE); break;
            case 5: actions.playConfirm(); actions.restartLevel(); break;
            case 6: actions.playBack(); actions.toMainMenu(); break;
            case 7: actions.playBack(); actions.quit(); break;
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

    private void selectSettings() {
        if (selected == 0) {
            // Category switch
            adjustSetting(1);
            return;
        }

        int lastIndex = itemCount() - 1;
        int defaultsIndex = itemCount() - 2;

        if (selected == defaultsIndex) {
            settings.restoreDefaults();
            actions.playClick();
            actions.settingsChanged();
            setStatusMessage("Settings restored to defaults.");
            rebuild();
        } else if (selected == lastIndex) {
            leaveSettings();
        } else {
            adjustSetting(1);
        }
    }

    private void leaveSettings() {
        settings.save();
        actions.settingsChanged();
        actions.playBack();
        if (settingsReturnTo == Screen.PAUSE) showPause(); else showMain();
    }

    private void adjustSetting(int dir) {
        if (selected == 0) {
            // Switch Settings Category Tab
            int catCount = SETTINGS_CATEGORIES.length;
            settingsCategory = (settingsCategory + dir + catCount) % catCount;
            selected = 0;
            actions.playClick();
            rebuild();
            return;
        }

        if (settingsCategory == 0) {
            // Graphics & Display
            switch (selected) {
                case 1: {
                    int count = GameSettings.PRESET_NAMES.length;
                    int next = (settings.graphicsPresetIndex + dir + count) % count;
                    settings.applyPreset(next);
                    break;
                }
                case 2: settings.adaptivePerformance = !settings.adaptivePerformance; break;
                case 3: {
                    int idx = 0;
                    for (int i = 0; i < GameSettings.TARGET_FPS_OPTIONS.length; i++) {
                        if (GameSettings.TARGET_FPS_OPTIONS[i] == settings.targetFps) idx = i;
                    }
                    idx = (idx + dir + GameSettings.TARGET_FPS_OPTIONS.length) % GameSettings.TARGET_FPS_OPTIONS.length;
                    settings.targetFps = GameSettings.TARGET_FPS_OPTIONS[idx];
                    break;
                }
                case 4: {
                    int sm = GameSettings.SHADOW_MODES.length;
                    settings.shadowQuality = (settings.shadowQuality + dir + sm) % sm;
                    settings.graphicsPresetIndex = GameSettings.PRESET_CUSTOM;
                    break;
                }
                case 5: {
                    settings.ssao = !settings.ssao;
                    settings.graphicsPresetIndex = GameSettings.PRESET_CUSTOM;
                    break;
                }
                case 6: {
                    settings.fxaa = !settings.fxaa;
                    settings.graphicsPresetIndex = GameSettings.PRESET_CUSTOM;
                    break;
                }
                case 7: {
                    int vd = GameSettings.VIEW_DISTANCES.length;
                    settings.viewDistanceIndex = (settings.viewDistanceIndex + dir + vd) % vd;
                    settings.graphicsPresetIndex = GameSettings.PRESET_CUSTOM;
                    break;
                }
                case 8: {
                    int vl = GameSettings.VEGETATION_LODS.length;
                    settings.vegetationLodIndex = (settings.vegetationLodIndex + dir + vl) % vl;
                    settings.graphicsPresetIndex = GameSettings.PRESET_CUSTOM;
                    break;
                }
                case 9: settings.windowSizeIndex = (settings.windowSizeIndex + dir + GameSettings.WINDOW_SIZES.length) % GameSettings.WINDOW_SIZES.length; break;
                case 10: settings.fullscreen = !settings.fullscreen; break;
                case 11: settings.restoreDefaults(); break;
                default: break;
            }
        } else if (settingsCategory == 1) {
            // Audio
            switch (selected) {
                case 1: settings.masterVolume = clamp(Math.round((settings.masterVolume + 0.05f * dir) * 20f) / 20f, 0f, 1f); break;
                case 2: settings.masterVolume = clamp(Math.round((settings.masterVolume + 0.05f * dir) * 20f) / 20f, 0f, 1f); break;
                case 3: settings.masterVolume = clamp(Math.round((settings.masterVolume + 0.05f * dir) * 20f) / 20f, 0f, 1f); break;
                case 4: settings.masterVolume = clamp(Math.round((settings.masterVolume + 0.05f * dir) * 20f) / 20f, 0f, 1f); break;
                case 5: settings.masterVolume = 1.0f; break;
                default: break;
            }
        } else if (settingsCategory == 2) {
            // Controls
            switch (selected) {
                case 1: settings.mouseSensitivity = clamp(round1(settings.mouseSensitivity + 0.1f * dir), 0.3f, 3.0f); break;
                case 2: settings.invertMouseY = !settings.invertMouseY; break;
                case 10:
                    settings.mouseSensitivity = 1.0f;
                    settings.invertMouseY = false;
                    break;
                default: break;
            }
        } else if (settingsCategory == 3) {
            // Accessibility
            switch (selected) {
                case 1: {
                    int i = indexOf(GameSettings.SUBTITLE_MODES, settings.subtitles);
                    settings.subtitles = GameSettings.SUBTITLE_MODES[(i + dir + GameSettings.SUBTITLE_MODES.length) % GameSettings.SUBTITLE_MODES.length];
                    break;
                }
                case 2: settings.invertMouseY = !settings.invertMouseY; break;
                case 3: settings.restoreDefaults(); break;
                default: break;
            }
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

    // ---- Drawing Implementation ------------------------------------------------------------------

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
            case LOAD_GAME: drawLoadGame(); break;
            case CHAPTER_SELECT: drawChapterSelect(); break;
            case CREDITS: drawCredits(); break;
            case JOURNAL: drawJournal(); break;
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
        drawDim(new Color(4, 6, 10, 165));
        List<Line> lines = new ArrayList<>();
        lines.add(new Line("HISTORICAL NARRATIVE ACTION-ADVENTURE", 12f, true, ProjectJulyHUD.AMBER));
        lines.add(new Line("PROJECT JULY 2024", 42f, true, ProjectJulyHUD.TEXT, 6));
        lines.add(new Line("Where It All Began · University of Dhaka Campus", 16f, false, ProjectJulyHUD.MUTED, 18));
        lines.add(new Line("Experience the July–August 2024 student movement through an ordinary student's journey.", 13.5f, false, ProjectJulyHUD.TEXT, 6));
        lines.add(new Line("Arif and his friends are fictional; historical events, dates, and campus geography follow the verified record.", 12f, false, ProjectJulyHUD.MUTED, 24));
        lines.add(new Line("Press Enter or Click Anywhere to Start", 15f, true, ProjectJulyHUD.AMBER, 22));
        lines.add(new Line("PC / macOS Apple Silicon Edition · Target 60 FPS", 11f, false, new Color(140, 148, 156), 6));

        PanelStyle s = new PanelStyle();
        s.background = new Color(8, 10, 14, 215);
        s.accent = ProjectJulyHUD.ACCENT;
        s.paddingX = 48;
        s.paddingY = 36;
        s.minWidth = 780;
        s.maxWidth = 920;
        panelPicture = textRenderer.createPanelPicture("TitlePanel", lines, s, gradientOverlay());
        centre(panelPicture, 0.54f);
    }

    private void drawMain() {
        drawDim(DIM);
        List<Line> lines = new ArrayList<>();
        lines.add(new Line("PROJECT JULY 2024", 26f, true, ProjectJulyHUD.TEXT));
        lines.add(new Line("MAIN MENU", 12f, true, ProjectJulyHUD.AMBER, 16));

        for (int i = 0; i < MAIN_ITEMS.length; i++) {
            boolean enabled = (i != 1 && i != 2) || actions.hasCheckpoint();
            lines.add(menuItem(MAIN_ITEMS[i], null, i == selected, enabled, i == 0 ? 16 : 8));
        }

        if (!statusMessage.isEmpty()) {
            lines.add(new Line(statusMessage, 12.5f, true, ProjectJulyHUD.AMBER, 16));
        }
        lines.add(new Line("Use Up/Down to navigate  ·  Enter to select  ·  Esc to return", 11f, false, ProjectJulyHUD.MUTED, 22));

        panelPicture = textRenderer.createPanelPicture("MainMenu", lines, menuStyle(), null);
        centre(panelPicture, 0.5f);
    }

    private void drawLoadGame() {
        drawDim(DIM);
        List<Line> lines = new ArrayList<>();
        lines.add(new Line("LOAD CHECKPOINT", 24f, true, ProjectJulyHUD.TEXT));
        lines.add(new Line("RESUME SAVED PROGRESSION", 12f, true, ProjectJulyHUD.AMBER, 18));

        boolean hasSave = actions.hasCheckpoint();
        lines.add(new Line("SLOT 01 — ACTIVE CHECKPOINT PROFILE", 13.5f, true, hasSave ? ProjectJulyHUD.TEXT : ProjectJulyHUD.MUTED));
        lines.add(new Line(actions.getCheckpointSummary(), 12.5f, false, hasSave ? ProjectJulyHUD.AMBER : ProjectJulyHUD.MUTED, 8));
        lines.add(new Line(hasSave ? "Status: Checkpoint verified on disk." : "Status: No saved state found in profile.", 11.5f, false, ProjectJulyHUD.MUTED, 20));

        lines.add(menuItem("Load This Checkpoint", null, selected == 0, hasSave, 12));
        lines.add(menuItem("Back to Main Menu", null, selected == 1, true, 8));

        if (!statusMessage.isEmpty()) {
            lines.add(new Line(statusMessage, 12.5f, true, ProjectJulyHUD.AMBER, 16));
        }
        lines.add(new Line("Enter to confirm  ·  Esc to return", 11f, false, ProjectJulyHUD.MUTED, 20));

        PanelStyle s = menuStyle();
        s.minWidth = 640;
        panelPicture = textRenderer.createPanelPicture("LoadGame", lines, s, null);
        centre(panelPicture, 0.5f);
    }

    private void drawChapterSelect() {
        drawDim(DIM);
        List<Line> lines = new ArrayList<>();
        lines.add(new Line("CHAPTER SELECTION", 24f, true, ProjectJulyHUD.TEXT));
        lines.add(new Line("CAMPAIGN PROGRESSION", 12f, true, ProjectJulyHUD.AMBER, 16));

        lines.add(menuItem("Chapter 1: Where It All Began (Unlocked · Playable)", null, selected == 0, true, 10));
        lines.add(new Line("   Early July 2024 · Dhaka University Campus · Peaceful Quota Reform Assembly", 11.5f, false, ProjectJulyHUD.MUTED, 12));

        lines.add(new Line("▶  Chapter 2: The Day the Streets Changed (Locked · Upcoming)", 14f, false, new Color(110, 116, 124), 6));
        lines.add(new Line("   July 15, 2024 · Campus attacks, evasion, and student medical rescue", 11f, false, new Color(90, 96, 104), 10));

        lines.add(new Line("▶  Chapter 3: A City on Edge (Locked · Upcoming)", 14f, false, new Color(110, 116, 124), 6));
        lines.add(new Line("   July 18, 2024 · Complete Shutdown and digital blackout", 11f, false, new Color(90, 96, 104), 18));

        lines.add(menuItem("Back to Main Menu", null, selected == 1, true, 14));

        lines.add(new Line("Enter to select  ·  Esc to return", 11f, false, ProjectJulyHUD.MUTED, 18));

        PanelStyle s = menuStyle();
        s.minWidth = 680;
        panelPicture = textRenderer.createPanelPicture("ChapterSelect", lines, s, null);
        centre(panelPicture, 0.5f);
    }

    private void drawCredits() {
        drawDim(DIM);
        List<Line> lines = new ArrayList<>();
        lines.add(new Line("PROJECT JULY 2024 · CREDITS", 24f, true, ProjectJulyHUD.TEXT));
        lines.add(new Line("HISTORICAL INTEGRITY & TECHNICAL ATTRIBUTIONS", 12f, true, ProjectJulyHUD.AMBER, 16));

        lines.add(new Line("Direction & Implementation:", 13f, true, ProjectJulyHUD.TEXT));
        lines.add(new Line("Project July 2024 Development Team", 12f, false, ProjectJulyHUD.MUTED, 8));

        lines.add(new Line("Historical & Architectural Research:", 13f, true, ProjectJulyHUD.TEXT));
        lines.add(new Line("OpenStreetMap Contributors (ODbL)  ·  UNESCO Muzharul Islam Dossier  ·  IAB Surveys", 12f, false, ProjectJulyHUD.MUTED, 8));

        lines.add(new Line("Core Engine & Platform:", 13f, true, ProjectJulyHUD.TEXT));
        lines.add(new Line("jMonkeyEngine 3.7.0 (BSD-3-Clause)  ·  LWJGL 3.3.3 (BSD-3-Clause)  ·  Java 17", 12f, false, ProjectJulyHUD.MUTED, 8));

        lines.add(new Line("3D Models, Characters & Foliage:", 13f, true, ProjectJulyHUD.TEXT));
        lines.add(new Line("Quaternius Modular Character & Nature Libraries (CC0 1.0 Universal)", 12f, false, ProjectJulyHUD.MUTED, 8));

        lines.add(new Line("Audio & Typography:", 13f, true, ProjectJulyHUD.TEXT));
        lines.add(new Line("Original Synthesised Soundbank (CC0)  ·  Noto Sans & Liberation Sans (SIL OFL 1.1)", 12f, false, ProjectJulyHUD.MUTED, 18));

        lines.add(menuItem("Back to Main Menu", null, true, true, 10));
        lines.add(new Line("Press Enter or Esc to return", 11f, false, ProjectJulyHUD.MUTED, 12));

        PanelStyle s = menuStyle();
        s.minWidth = 720;
        panelPicture = textRenderer.createPanelPicture("Credits", lines, s, null);
        centre(panelPicture, 0.5f);
    }

    private void drawJournal() {
        drawDim(DIM);
        List<Line> lines = new ArrayList<>();
        lines.add(new Line("MISSION OBJECTIVES & JOURNAL", 24f, true, ProjectJulyHUD.TEXT));
        lines.add(new Line("LEVEL 1 — UNIVERSITY OF DHAKA", 12f, true, ProjectJulyHUD.AMBER, 16));

        lines.add(new Line("1. Meet Rafi at Curzon Hall courtyard verandas", 13.5f, true, ProjectJulyHUD.TEXT));
        lines.add(new Line("   Coordinate peaceful quota reform campaign with student companions.", 11.5f, false, ProjectJulyHUD.MUTED, 8));

        lines.add(new Line("2. Collect informational leaflets from Tanvir at Central Library", 13.5f, true, ProjectJulyHUD.TEXT));
        lines.add(new Line("   Receive 50 demand sheets outlining civil service quota reform.", 11.5f, false, ProjectJulyHUD.MUTED, 8));

        lines.add(new Line("3. Deliver leaflets to Nusrat at TSC entrance plaza", 13.5f, true, ProjectJulyHUD.TEXT));
        lines.add(new Line("   Hand over leaflets for campus-wide distribution to students.", 11.5f, false, ProjectJulyHUD.MUTED, 8));

        lines.add(new Line("4. Inspect the Anti-Terrorism Raju Memorial plaque", 13.5f, true, ProjectJulyHUD.TEXT));
        lines.add(new Line("   Read historical memorial commemorating Moin Hossain Raju (1992).", 11.5f, false, ProjectJulyHUD.MUTED, 8));

        lines.add(new Line("5. Join the student assembly at the roundabout", 13.5f, true, ProjectJulyHUD.TEXT));
        lines.add(new Line("   Stand shoulder-to-shoulder with students in non-violent solidarity.", 11.5f, false, ProjectJulyHUD.MUTED, 16));

        lines.add(menuItem("Back to Pause Menu", null, true, true, 10));
        lines.add(new Line("Press Enter or Esc to return", 11f, false, ProjectJulyHUD.MUTED, 12));

        PanelStyle s = menuStyle();
        s.minWidth = 700;
        panelPicture = textRenderer.createPanelPicture("Journal", lines, s, null);
        centre(panelPicture, 0.5f);
    }

    private void drawPause() {
        drawDim(DIM);
        List<Line> lines = new ArrayList<>();
        lines.add(new Line("GAME PAUSED", 26f, true, ProjectJulyHUD.TEXT));
        lines.add(new Line("LEVEL 01 · UNIVERSITY OF DHAKA", 12f, true, ProjectJulyHUD.AMBER, 16));

        for (int i = 0; i < PAUSE_ITEMS.length; i++) {
            lines.add(menuItem(PAUSE_ITEMS[i], null, i == selected, true, i == 0 ? 14 : 7));
        }

        if (!statusMessage.isEmpty()) {
            lines.add(new Line(statusMessage, 12.5f, true, ProjectJulyHUD.AMBER, 16));
        }

        lines.add(new Line("W/A/S/D Move  ·  Shift Sprint  ·  Space Jump  ·  C Crouch  ·  E Interact  ·  M Map  ·  R Reset Cam  ·  F12 Photo", 10.5f, false, ProjectJulyHUD.MUTED, 18));
        lines.add(new Line("Up/Down Navigate  ·  Enter Select  ·  Esc Resume", 11f, false, ProjectJulyHUD.MUTED, 4));

        panelPicture = textRenderer.createPanelPicture("PauseMenu", lines, menuStyle(), null);
        centre(panelPicture, 0.5f);
    }

    private void drawSettings() {
        drawDim(DIM);
        List<Line> lines = new ArrayList<>();
        lines.add(new Line("SETTINGS", 24f, true, ProjectJulyHUD.TEXT));

        // Tab Navigation Header
        String catTab = String.format("CATEGORY [%d/4]:  %s   (Left / Right to Switch Tab)",
            settingsCategory + 1, SETTINGS_CATEGORIES[settingsCategory].toUpperCase());
        lines.add(new Line(catTab, 12f, true, ProjectJulyHUD.AMBER, 14));

        if (settingsCategory == 0) {
            // Category 0: Display & Graphics
            String win = settings.windowWidth() + " × " + settings.windowHeight();
            String fpsStr = settings.targetFps > 0 ? (settings.targetFps + " FPS") : "Unlimited";

            lines.add(menuItem("Settings Category Tab", SETTINGS_CATEGORIES[settingsCategory], selected == 0, true, 8));
            lines.add(menuItem("Graphics Preset", GameSettings.PRESET_NAMES[settings.graphicsPresetIndex], selected == 1, true, 3));
            lines.add(menuItem("Adaptive Performance", settings.adaptivePerformance ? "Enabled" : "Disabled", selected == 2, true, 3));
            lines.add(menuItem("Target Frame Rate", fpsStr, selected == 3, true, 3));
            lines.add(menuItem("Shadow Quality", GameSettings.SHADOW_MODES[settings.shadowQuality], selected == 4, true, 3));
            lines.add(menuItem("Ambient Occlusion (SSAO)", settings.ssao ? "Enabled" : "Disabled", selected == 5, true, 3));
            lines.add(menuItem("Anti-Aliasing (FXAA)", settings.fxaa ? "Enabled" : "Disabled", selected == 6, true, 3));
            lines.add(menuItem("View Distance", GameSettings.VIEW_DISTANCES[settings.viewDistanceIndex], selected == 7, true, 3));
            lines.add(menuItem("Foliage Detail (LOD)", GameSettings.VEGETATION_LODS[settings.vegetationLodIndex], selected == 8, true, 3));
            lines.add(menuItem("Window Resolution", win + " (restart)", selected == 9, true, 3));
            lines.add(menuItem("Fullscreen Mode", (settings.fullscreen ? "Enabled" : "Disabled") + " (restart)", selected == 10, true, 3));
            lines.add(menuItem("Restore Category Defaults", null, selected == 11, true, 8));
        } else if (settingsCategory == 1) {
            // Category 1: Audio Settings
            String volStr = Math.round(settings.masterVolume * 100) + " %";
            lines.add(menuItem("Settings Category Tab", SETTINGS_CATEGORIES[settingsCategory], selected == 0, true, 8));
            lines.add(menuItem("Master Volume", volStr, selected == 1, true, 4));
            lines.add(menuItem("Campus Ambience Volume", volStr, selected == 2, true, 4));
            lines.add(menuItem("Sound Effects (SFX)", volStr, selected == 3, true, 4));
            lines.add(menuItem("UI Sound Volume", volStr, selected == 4, true, 4));
            lines.add(menuItem("Mute All Audio", settings.masterVolume == 0f ? "Muted" : "Unmuted", selected == 5, true, 4));
            lines.add(menuItem("Restore Category Defaults", null, selected == 6, true, 8));
        } else if (settingsCategory == 2) {
            // Category 2: Controls & Camera
            String sensStr = String.format(java.util.Locale.ROOT, "%.1f", settings.mouseSensitivity);
            lines.add(menuItem("Settings Category Tab", SETTINGS_CATEGORIES[settingsCategory], selected == 0, true, 8));
            lines.add(menuItem("Mouse Look Sensitivity", sensStr, selected == 1, true, 4));
            lines.add(menuItem("Invert Mouse Y-Axis", settings.invertMouseY ? "Inverted" : "Standard", selected == 2, true, 4));
            lines.add(menuItem("Move Forward / Backward", "W / S (Up / Down)", selected == 3, false, 3));
            lines.add(menuItem("Strafe Left / Right", "A / D (Left / Right)", selected == 4, false, 3));
            lines.add(menuItem("Sprint Locomotion", "Left / Right Shift", selected == 5, false, 3));
            lines.add(menuItem("Jump / Vault", "Spacebar / J", selected == 6, false, 3));
            lines.add(menuItem("Crouch Stance", "C (Hold)", selected == 7, false, 3));
            lines.add(menuItem("Context Interaction", "E / Enter", selected == 8, false, 3));
            lines.add(menuItem("Campus Navigation Map", "M / Esc", selected == 9, false, 3));
            lines.add(menuItem("Camera Forward Reset", "R", selected == 10, false, 3));
            lines.add(menuItem("Restore Category Defaults", null, selected == 11, true, 8));
        } else {
            // Category 3: Accessibility
            String subStr = "en".equals(settings.subtitles) || "both".equals(settings.subtitles) ? "English Subtitles" : "Disabled";
            lines.add(menuItem("Settings Category Tab", SETTINGS_CATEGORIES[settingsCategory], selected == 0, true, 8));
            lines.add(menuItem("Subtitles Display", subStr, selected == 1, true, 4));
            lines.add(menuItem("High Contrast HUD Background", "Enabled", selected == 2, true, 4));
            lines.add(menuItem("Waypoint Navigation Beacon", "Enabled", selected == 3, true, 4));
            lines.add(menuItem("Restore Category Defaults", null, selected == 4, true, 8));
        }

        if (!statusMessage.isEmpty()) {
            lines.add(new Line(statusMessage, 12f, true, ProjectJulyHUD.AMBER, 10));
        }

        lines.add(new Line("Left/Right Change Value  ·  Enter Select  ·  Esc Save & Return", 11f, false, ProjectJulyHUD.MUTED, 16));

        PanelStyle s = menuStyle();
        s.minWidth = 680;
        s.maxWidth = 920;
        s.paddingY = 18;
        panelPicture = textRenderer.createPanelPicture("Settings", lines, s, null);
        centre(panelPicture, 0.5f);
    }

    private void drawChapter() {
        drawDim(new Color(4, 6, 10, 225));
        List<Line> lines = new ArrayList<>();
        lines.add(new Line("CHAPTER 1", 14f, true, ProjectJulyHUD.AMBER));
        lines.add(new Line("Where It All Began", 38f, true, ProjectJulyHUD.TEXT, 8));
        lines.add(new Line("University of Dhaka · Early July 2024", 16f, false, ProjectJulyHUD.MUTED, 18));
        lines.add(new Line("Students across campus assemble in peaceful solidarity for civil service quota reform.", 13.5f, false, ProjectJulyHUD.TEXT, 8));
        lines.add(new Line("Meet your classmate and organizer Rafi near the Raju Memorial Sculpture.", 13f, false, ProjectJulyHUD.MUTED, 24));
        lines.add(new Line("Press Enter to Begin Exploration", 14f, true, ProjectJulyHUD.AMBER, 20));

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
        drawDim(new Color(4, 6, 10, 230));
        List<Line> lines = new ArrayList<>();
        lines.add(new Line("CHAPTER 1 COMPLETE", 14f, true, ProjectJulyHUD.AMBER));
        lines.add(new Line("Unity & Historical Resolve", 36f, true, ProjectJulyHUD.TEXT, 8));
        lines.add(new Line("University of Dhaka · July 2024", 15f, false, ProjectJulyHUD.MUTED, 16));
        lines.add(new Line("The united stand of students and citizens marked the historic inception of the July Movement.", 13.5f, false, ProjectJulyHUD.TEXT, 8));
        lines.add(new Line("From peaceful campus verandas to nationwide solidarity, the call for justice resonated across Bangladesh.", 12.5f, false, ProjectJulyHUD.MUTED, 24));

        for (int i = 0; i < CHAPTER_COMPLETE_ITEMS.length; i++) {
            lines.add(menuItem(CHAPTER_COMPLETE_ITEMS[i], null, i == selected, true, i == 0 ? 18 : 8));
        }

        lines.add(new Line("Use Up/Down to navigate  ·  Enter to select", 11f, false, ProjectJulyHUD.MUTED, 20));
        panelPicture = textRenderer.createPanelPicture("ChapterComplete", lines, menuStyle(), null);
        centre(panelPicture, 0.5f);
    }

    private Line menuItem(String text, String value, boolean sel, boolean enabled, int gap) {
        String display = (sel ? "▶  " : "    ") + text + (value != null ? "      " + value : "");
        Color c = !enabled ? new Color(120, 126, 134) : (sel ? ProjectJulyHUD.AMBER : ProjectJulyHUD.TEXT);
        return new Line(display, 15f, sel, c, gap);
    }

    private static PanelStyle menuStyle() {
        PanelStyle s = new PanelStyle();
        s.background = new Color(10, 12, 16, 238);
        s.accent = ProjectJulyHUD.ACCENT;
        s.paddingX = 34;
        s.paddingY = 26;
        s.minWidth = 560;
        s.maxWidth = 920;
        return s;
    }

    private BengaliTextRenderer.Overlay gradientOverlay() {
        return (Graphics2D g, int x, int y, int w, int h) -> {
            g.setPaint(new GradientPaint(x, y, new Color(214, 58, 48, 0), x + w, y, new Color(214, 58, 48, 80)));
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
