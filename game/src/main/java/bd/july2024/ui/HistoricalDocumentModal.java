package bd.july2024.ui;

import com.jme3.scene.Node;
import com.jme3.ui.Picture;

import bd.july2024.ui.BengaliTextRenderer.Line;
import bd.july2024.ui.BengaliTextRenderer.PanelStyle;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * Historical Archival Document Modal for Project July 2024.
 * Renders verified historical plaques and documents with authentic bilingual typography.
 */
public class HistoricalDocumentModal {

    public static class DocumentRecord {
        public final String titleBengali;
        public final String titleEnglish;
        public final String date;
        public final String location;
        public final String textBengali;
        public final String textEnglish;

        public DocumentRecord(String titleBengali, String titleEnglish, String date, String location, String textBengali, String textEnglish) {
            this.titleBengali = titleBengali;
            this.titleEnglish = titleEnglish;
            this.date = date;
            this.location = location;
            this.textBengali = textBengali;
            this.textEnglish = textEnglish;
        }
    }

    private final Node guiNode;
    private final BengaliTextRenderer textRenderer;
    private final int screenWidth;
    private final int screenHeight;

    private Picture modalPicture;
    private boolean open = false;
    private DocumentRecord currentDocument;

    public HistoricalDocumentModal(Node guiNode, BengaliTextRenderer textRenderer, int screenWidth, int screenHeight) {
        this.guiNode = guiNode;
        this.textRenderer = textRenderer;
        this.screenWidth = screenWidth > 0 ? screenWidth : 1280;
        this.screenHeight = screenHeight > 0 ? screenHeight : 720;
    }

    public void open(DocumentRecord doc) {
        if (doc == null) return;
        this.currentDocument = doc;
        this.open = true;

        if (modalPicture != null) {
            guiNode.detachChild(modalPicture);
        }

        List<Line> lines = new ArrayList<>();
        lines.add(new Line("ঐতিহাসিক নথি  ·  HISTORICAL DOCUMENT", 12f, true, ProjectJulyHUD.MUTED));
        lines.add(new Line(doc.titleBengali, 22f, true, ProjectJulyHUD.TEXT, 4));
        lines.add(new Line(doc.titleEnglish, 15f, false, ProjectJulyHUD.MUTED, 2));
        lines.add(new Line(String.format("তারিখ  %s   ·   স্থান  %s", doc.date, doc.location), 13f, false, ProjectJulyHUD.AMBER, 6));
        lines.add(new Line(doc.textBengali, 16f, false, ProjectJulyHUD.TEXT, 16));
        lines.add(new Line(doc.textEnglish, 14.5f, false, ProjectJulyHUD.MUTED, 10));
        lines.add(new Line("E / Space / Enter  ·  নথিটি বন্ধ করুন (close)", 12f, false, ProjectJulyHUD.MUTED, 16));

        PanelStyle style = new PanelStyle();
        style.background = new Color(10, 12, 16, 232);
        style.accent = ProjectJulyHUD.AMBER;
        style.paddingX = 30;
        style.paddingY = 22;
        style.minWidth = 640;
        style.maxWidth = 860;
        modalPicture = textRenderer.createPanelPicture("HistDocModal", lines, style, null);

        float x = (screenWidth - modalPicture.getWidth()) / 2f;
        float y = (screenHeight - modalPicture.getHeight()) / 2f;
        modalPicture.setPosition(Math.max(10f, x), Math.max(10f, y));
        guiNode.attachChild(modalPicture);
    }

    public void close() {
        this.open = false;
        this.currentDocument = null;
        if (modalPicture != null) {
            guiNode.detachChild(modalPicture);
            modalPicture = null;
        }
    }

    public boolean isOpen() {
        return open;
    }

    public DocumentRecord getCurrentDocument() {
        return currentDocument;
    }
}
