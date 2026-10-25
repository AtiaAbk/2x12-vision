package bd.july2024.ui;

import com.jme3.scene.Node;
import com.jme3.ui.Picture;

import java.awt.Color;

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

        String content = String.format(
            "%s\n%s  |  তারিখ: %s  |  স্থান: %s\n\n%s\n\n%s\n\n[E / SPACE / ENTER] নথিটি বন্ধ করুন (Close Document)",
            doc.titleBengali,
            doc.titleEnglish,
            doc.date,
            doc.location,
            doc.textBengali,
            doc.textEnglish
        );

        modalPicture = textRenderer.createTextPicture(
            "HistDocModal",
            content,
            16f,
            false,
            new Color(250, 240, 215),
            new Color(18, 22, 28, 235),
            28,
            20
        );

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
