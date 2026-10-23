package bd.july2024.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.utils.Disposable;

/**
 * High-fidelity FreeType font renderer supporting Bengali Unicode script
 * and crisp English typography for Project July 2024.
 */
public class BengaliFontRenderer implements Disposable {

    private BitmapFont titleFont;
    private BitmapFont bodyFont;
    private BitmapFont subtitleFont;
    private BitmapFont monoFont;

    private static final String BENGALI_CHARS =
        "অআইঈউঊঋএঐওঔকখগঘঙচছজঝঞটঠডঢণতথদধনপফবভমযরলশষসহড়ঢ়য়ৎংঃঁ" +
        "ািীুূৃেৈোৌ্ৗড়ঢ়য়০১২৩৪৫৬৭৮৯" +
        "—–‘’“”•…।॥";

    public BengaliFontRenderer() {
        initFonts();
    }

    private void initFonts() {
        try {
            if (Gdx.files != null && Gdx.files.internal("fonts/NotoSansBengali-Variable.ttf").exists()) {
                FreeTypeFontGenerator gen = new FreeTypeFontGenerator(Gdx.files.internal("fonts/NotoSansBengali-Variable.ttf"));

                FreeTypeFontParameter titleParam = new FreeTypeFontParameter();
                titleParam.size = 28;
                titleParam.color = Color.WHITE;
                titleParam.characters = FreeTypeFontGenerator.DEFAULT_CHARS + BENGALI_CHARS;
                titleFont = gen.generateFont(titleParam);

                FreeTypeFontParameter bodyParam = new FreeTypeFontParameter();
                bodyParam.size = 20;
                bodyParam.color = Color.WHITE;
                bodyParam.characters = FreeTypeFontGenerator.DEFAULT_CHARS + BENGALI_CHARS;
                bodyFont = gen.generateFont(bodyParam);

                FreeTypeFontParameter subParam = new FreeTypeFontParameter();
                subParam.size = 16;
                subParam.color = new Color(0.9f, 0.9f, 0.9f, 1f);
                subParam.characters = FreeTypeFontGenerator.DEFAULT_CHARS + BENGALI_CHARS;
                subtitleFont = gen.generateFont(subParam);

                gen.dispose();
            }
        } catch (Throwable t) {
            // Fallback font initialized if FreeType encounters issues
        }

        // Safe fallbacks if font files were missing or during headless testing
        if (titleFont == null) titleFont = new BitmapFont();
        if (bodyFont == null) bodyFont = new BitmapFont();
        if (subtitleFont == null) subtitleFont = new BitmapFont();
        if (monoFont == null) monoFont = new BitmapFont();
    }

    public BitmapFont getTitleFont() {
        return titleFont;
    }

    public BitmapFont getBodyFont() {
        return bodyFont;
    }

    public BitmapFont getSubtitleFont() {
        return subtitleFont;
    }

    public BitmapFont getMonoFont() {
        return monoFont;
    }

    @Override
    public void dispose() {
        if (titleFont != null) { titleFont.dispose(); titleFont = null; }
        if (bodyFont != null) { bodyFont.dispose(); bodyFont = null; }
        if (subtitleFont != null) { subtitleFont.dispose(); subtitleFont = null; }
        if (monoFont != null) { monoFont.dispose(); monoFont = null; }
    }
}
