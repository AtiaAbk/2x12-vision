package bd.july2024.hud;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import bd.july2024.world.JulyMemorials.MemorialEntry;

/**
 * Historical archival reader modal for Project July 2024.
 * Renders authentic historical plaques and documentation with bilingual text.
 */
public class HistoricalDocumentModal {

    private boolean isOpen = false;
    private MemorialEntry currentEntry = null;

    public void open(MemorialEntry entry) {
        this.currentEntry = entry;
        this.isOpen = true;
    }

    public void close() {
        this.isOpen = false;
        this.currentEntry = null;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public MemorialEntry getCurrentEntry() {
        return currentEntry;
    }

    public void render(ShapeRenderer shapeRenderer, SpriteBatch spriteBatch, BengaliFontRenderer fonts,
                       float screenW, float screenH) {
        if (!isOpen || currentEntry == null) return;

        float boxW = Math.min(840f, screenW - 80f);
        float boxH = Math.min(520f, screenH - 80f);
        float boxX = (screenW - boxW) / 2f;
        float boxY = (screenH - boxH) / 2f;

        // Dark modal backdrop
        shapeRenderer.begin(ShapeType.Filled);
        shapeRenderer.setColor(0f, 0f, 0f, 0.75f);
        shapeRenderer.rect(0, 0, screenW, screenH);

        // Archival paper plaque background (warm dark parchment)
        shapeRenderer.setColor(0.12f, 0.13f, 0.15f, 0.95f);
        shapeRenderer.rect(boxX, boxY, boxW, boxH);

        // Gold border
        shapeRenderer.end();
        shapeRenderer.begin(ShapeType.Line);
        shapeRenderer.setColor(0.85f, 0.72f, 0.35f, 1f);
        shapeRenderer.rect(boxX + 2, boxY + 2, boxW - 4, boxH - 4);
        shapeRenderer.rect(boxX + 6, boxY + 6, boxW - 12, boxH - 12);
        shapeRenderer.end();

        // Render bilingual archival text
        spriteBatch.begin();

        BitmapFont titleFont = fonts.getTitleFont();
        BitmapFont bodyFont = fonts.getBodyFont();
        BitmapFont subFont = fonts.getSubtitleFont();

        float textX = boxX + 28f;
        float curY = boxY + boxH - 36f;

        // Title
        titleFont.setColor(0.95f, 0.82f, 0.35f, 1f);
        titleFont.draw(spriteBatch, currentEntry.titleBengali, textX, curY);
        curY -= 32f;

        subFont.setColor(0.80f, 0.80f, 0.80f, 1f);
        subFont.draw(spriteBatch, currentEntry.titleEnglish + " | " + currentEntry.date, textX, curY);
        curY -= 20f;

        subFont.setColor(0.65f, 0.65f, 0.65f, 1f);
        subFont.draw(spriteBatch, "স্থান: " + currentEntry.location, textX, curY);
        curY -= 36f;

        // Bengali Description
        bodyFont.setColor(0.95f, 0.95f, 0.95f, 1f);
        bodyFont.draw(spriteBatch, currentEntry.descriptionBengali, textX, curY, boxW - 56f, -1, true);
        curY -= 140f;

        // English Description
        subFont.setColor(0.82f, 0.85f, 0.88f, 1f);
        subFont.draw(spriteBatch, currentEntry.descriptionEnglish, textX, curY, boxW - 56f, -1, true);

        // Dismiss instruction
        subFont.setColor(0.85f, 0.72f, 0.35f, 1f);
        subFont.draw(spriteBatch, "[E / ENTER / SPACE] নথি বন্ধ করুন (Close Document)", textX, boxY + 34f);

        spriteBatch.end();
    }
}
