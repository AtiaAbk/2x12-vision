package bd.july2024.world;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Disposable;

/**
 * Procedural PBR texture generator for Project July 2024.
 * Generates weathered architectural materials without requiring copyrighted binary images.
 */
public class TextureFactory implements Disposable {

    public Texture concretePbr;
    public Texture brickTerracotta;
    public Texture asphaltRoad;
    public Texture weatheredStone;
    public Texture foliageLeaf;
    public Texture studentCotton;
    public Texture denimTrousers;
    public Texture skinTone;
    public Texture darkHair;

    public TextureFactory() {
        generateTextures();
    }

    private void generateTextures() {
        concretePbr = createNoiseTexture(256, 256, new Color(0.78f, 0.77f, 0.75f, 1f), 0.08f);
        brickTerracotta = createBrickTexture(256, 256, new Color(0.68f, 0.28f, 0.22f, 1f));
        asphaltRoad = createNoiseTexture(256, 256, new Color(0.24f, 0.24f, 0.25f, 1f), 0.06f);
        weatheredStone = createNoiseTexture(256, 256, new Color(0.62f, 0.60f, 0.58f, 1f), 0.12f);
        foliageLeaf = createFoliageTexture(128, 128, new Color(0.22f, 0.52f, 0.24f, 1f));

        studentCotton = createNoiseTexture(128, 128, new Color(0.92f, 0.94f, 0.96f, 1f), 0.04f);
        denimTrousers = createNoiseTexture(128, 128, new Color(0.18f, 0.22f, 0.32f, 1f), 0.05f);
        skinTone = createSolidTexture(64, 64, new Color(0.68f, 0.48f, 0.38f, 1f));
        darkHair = createSolidTexture(64, 64, new Color(0.10f, 0.09f, 0.08f, 1f));
    }

    private Texture createSolidTexture(int w, int h, Color color) {
        Pixmap p = new Pixmap(w, h, Format.RGBA8888);
        p.setColor(color);
        p.fill();
        Texture tex = new Texture(p);
        p.dispose();
        return tex;
    }

    private Texture createNoiseTexture(int w, int h, Color base, float noiseStrength) {
        Pixmap p = new Pixmap(w, h, Format.RGBA8888);
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                float n = (MathUtils.random() - 0.5f) * noiseStrength;
                float r = MathUtils.clamp(base.r + n, 0f, 1f);
                float g = MathUtils.clamp(base.g + n, 0f, 1f);
                float b = MathUtils.clamp(base.b + n, 0f, 1f);
                p.setColor(r, g, b, 1f);
                p.drawPixel(x, y);
            }
        }
        Texture tex = new Texture(p);
        tex.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
        tex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        p.dispose();
        return tex;
    }

    private Texture createBrickTexture(int w, int h, Color mortar) {
        Pixmap p = new Pixmap(w, h, Format.RGBA8888);
        p.setColor(0.35f, 0.32f, 0.30f, 1f); // Mortar line
        p.fill();

        Color brick = new Color(0.72f, 0.26f, 0.20f, 1f);
        int rows = 16;
        int cols = 8;
        int rowH = h / rows;
        int colW = w / cols;

        for (int r = 0; r < rows; r++) {
            int y0 = r * rowH;
            int xOffset = (r % 2 == 0) ? 0 : colW / 2;
            for (int c = -1; c <= cols; c++) {
                int x0 = c * colW + xOffset;
                float var = (MathUtils.random() - 0.5f) * 0.08f;
                p.setColor(
                    MathUtils.clamp(brick.r + var, 0f, 1f),
                    MathUtils.clamp(brick.g + var, 0f, 1f),
                    MathUtils.clamp(brick.b + var, 0f, 1f),
                    1f
                );
                p.fillRectangle(x0 + 1, y0 + 1, colW - 2, rowH - 2);
            }
        }

        Texture tex = new Texture(p);
        tex.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
        tex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        p.dispose();
        return tex;
    }

    private Texture createFoliageTexture(int w, int h, Color leafBase) {
        Pixmap p = new Pixmap(w, h, Format.RGBA8888);
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                float d = MathUtils.clamp(MathUtils.random(), 0.8f, 1.2f);
                p.setColor(leafBase.r * d, leafBase.g * d, leafBase.b * d, 1f);
                p.drawPixel(x, y);
            }
        }
        Texture tex = new Texture(p);
        p.dispose();
        return tex;
    }

    @Override
    public void dispose() {
        if (concretePbr != null) { concretePbr.dispose(); concretePbr = null; }
        if (brickTerracotta != null) { brickTerracotta.dispose(); brickTerracotta = null; }
        if (asphaltRoad != null) { asphaltRoad.dispose(); asphaltRoad = null; }
        if (weatheredStone != null) { weatheredStone.dispose(); weatheredStone = null; }
        if (foliageLeaf != null) { foliageLeaf.dispose(); foliageLeaf = null; }
        if (studentCotton != null) { studentCotton.dispose(); studentCotton = null; }
        if (denimTrousers != null) { denimTrousers.dispose(); denimTrousers = null; }
        if (skinTone != null) { skinTone.dispose(); skinTone = null; }
        if (darkHair != null) { darkHair.dispose(); darkHair = null; }
    }
}
