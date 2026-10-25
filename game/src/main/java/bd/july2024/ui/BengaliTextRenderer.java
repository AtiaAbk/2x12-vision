package bd.july2024.ui;

import com.jme3.texture.Image;
import com.jme3.texture.Texture2D;
import com.jme3.texture.plugins.AWTLoader;
import com.jme3.ui.Picture;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.AttributedString;
import java.util.HashMap;
import java.util.Map;

/**
 * High-DPI Dynamic Bengali & English Typography System for jMonkeyEngine.
 * Utilizes the official Noto Sans Bengali TrueType font to dynamically rasterize
 * complex Indic conjuncts (যুক্তাক্ষর) with subpixel anti-aliasing into jME 2D GUI textures.
 *
 * Fonts created from a file with {@link Font#createFont} do not fall back to other fonts
 * for glyphs they lack, so any symbol outside Noto Sans Bengali (for example ◆ ▶ ★ •)
 * would render as a missing-glyph box. Each line is therefore split into font runs:
 * Noto Sans Bengali first, then the bundled Liberation Sans, then the platform's
 * logical sans-serif font for anything else.
 */
public class BengaliTextRenderer {

    private final com.jme3.asset.AssetManager assetManager;
    private Font bengaliFontRegular;
    private Font bengaliFontBold;
    private Font latinFontRegular;
    private Font latinFontBold;
    private final Font systemFontRegular = new Font(Font.SANS_SERIF, Font.PLAIN, 24);
    private final Font systemFontBold = new Font(Font.SANS_SERIF, Font.BOLD, 24);
    private final AWTLoader awtLoader = new AWTLoader();
    private final Map<String, Texture2D> textCache = new HashMap<>();

    public BengaliTextRenderer(com.jme3.asset.AssetManager assetManager) {
        this.assetManager = assetManager;
        loadFonts();
    }

    private void loadFonts() {
        try {
            File fontFile = new File("assets/fonts/NotoSansBengali-Variable.ttf");
            if (fontFile.exists()) {
                Font baseFont = Font.createFont(Font.TRUETYPE_FONT, fontFile);
                bengaliFontRegular = baseFont.deriveFont(Font.PLAIN, 24f);
                bengaliFontBold = baseFont.deriveFont(Font.BOLD, 24f);
            } else {
                // Fallback to system sans-serif
                bengaliFontRegular = systemFontRegular;
                bengaliFontBold = systemFontBold;
            }
        } catch (Throwable t) {
            bengaliFontRegular = systemFontRegular;
            bengaliFontBold = systemFontBold;
        }

        try {
            File regular = new File("assets/fonts/LiberationSans-Regular.ttf");
            File bold = new File("assets/fonts/LiberationSans-Bold.ttf");
            latinFontRegular = regular.exists()
                ? Font.createFont(Font.TRUETYPE_FONT, regular).deriveFont(Font.PLAIN, 24f)
                : systemFontRegular;
            latinFontBold = bold.exists()
                ? Font.createFont(Font.TRUETYPE_FONT, bold).deriveFont(Font.BOLD, 24f)
                : systemFontBold;
        } catch (Throwable t) {
            latinFontRegular = systemFontRegular;
            latinFontBold = systemFontBold;
        }
    }

    /**
     * Creates a Picture quad with rendered Bengali/English text.
     */
    public Picture createTextPicture(String key, String text, float fontSize, boolean isBold, Color textColor, Color bgColor, int paddingX, int paddingY) {
        long glContext = org.lwjgl.glfw.GLFW.glfwGetCurrentContext();
        BufferedImage img = renderStringToImage(text, fontSize, isBold, textColor, bgColor, paddingX, paddingY);
        if (glContext != 0L) {
            org.lwjgl.glfw.GLFW.glfwMakeContextCurrent(glContext);
        }
        Image jmeImage = awtLoader.load(img, true);
        Texture2D texture = new Texture2D(jmeImage);
        texture.setMinFilter(com.jme3.texture.Texture.MinFilter.BilinearNoMipMaps);
        texture.setMagFilter(com.jme3.texture.Texture.MagFilter.Bilinear);

        Picture pic = new Picture("Text_" + key);
        if (assetManager != null) {
            pic.setTexture(assetManager, texture, true);
        }
        pic.setWidth(img.getWidth());
        pic.setHeight(img.getHeight());
        return pic;
    }

    public Texture2D createTexture(String text, float fontSize, boolean isBold, Color textColor, Color bgColor, int paddingX, int paddingY) {
        long glContext = org.lwjgl.glfw.GLFW.glfwGetCurrentContext();
        BufferedImage img = renderStringToImage(text, fontSize, isBold, textColor, bgColor, paddingX, paddingY);
        if (glContext != 0L) {
            org.lwjgl.glfw.GLFW.glfwMakeContextCurrent(glContext);
        }
        Image jmeImage = awtLoader.load(img, true);
        Texture2D texture = new Texture2D(jmeImage);
        texture.setMinFilter(com.jme3.texture.Texture.MinFilter.BilinearNoMipMaps);
        texture.setMagFilter(com.jme3.texture.Texture.MagFilter.Bilinear);
        return texture;
    }

    public BufferedImage renderStringToImage(String text, float fontSize, boolean isBold, Color textColor, Color bgColor, int paddingX, int paddingY) {
        Font primary = (isBold ? bengaliFontBold : bengaliFontRegular).deriveFont(fontSize);
        Font latin = (isBold ? latinFontBold : latinFontRegular).deriveFont(fontSize);
        Font system = (isBold ? systemFontBold : systemFontRegular).deriveFont(fontSize);
        String[] lines = text != null ? text.split("\n") : new String[]{""};

        FontRenderContext frc = new FontRenderContext(null, true, true);

        // Measure using the primary font's metrics for consistent line spacing
        BufferedImage dummy = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = dummy.createGraphics();
        g2d.setFont(primary);
        FontMetrics fm = g2d.getFontMetrics();
        int lineHeight = fm.getHeight();
        int ascent = fm.getAscent();
        g2d.dispose();

        TextLayout[] layouts = new TextLayout[lines.length];
        int maxLineWidth = 0;
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            if (line.isEmpty()) {
                layouts[i] = null;
                continue;
            }
            AttributedString attributed = buildFontRuns(line, primary, latin, system);
            TextLayout layout = new TextLayout(attributed.getIterator(), frc);
            layouts[i] = layout;
            int w = (int) Math.ceil(layout.getAdvance());
            if (w > maxLineWidth) maxLineWidth = w;
        }

        int totalWidth = Math.max(1, maxLineWidth + paddingX * 2);
        int totalHeight = Math.max(1, lineHeight * lines.length + paddingY * 2);

        BufferedImage img = new BufferedImage(totalWidth, totalHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();

        // High quality text rendering hints
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);

        // Optional background fill
        if (bgColor != null && bgColor.getAlpha() > 0) {
            g.setColor(bgColor);
            g.fillRoundRect(0, 0, totalWidth, totalHeight, 8, 8);
        }

        // Draw text lines
        for (int i = 0; i < lines.length; i++) {
            TextLayout layout = layouts[i];
            if (layout == null) continue;
            float yPos = paddingY + ascent + i * lineHeight;
            // Draw drop shadow
            g.setColor(new Color(0, 0, 0, 180));
            layout.draw(g, paddingX + 1f, yPos + 1f);
            // Draw primary text
            g.setColor(textColor != null ? textColor : Color.WHITE);
            layout.draw(g, paddingX, yPos);
        }

        g.dispose();
        return img;
    }

    /**
     * Assigns a font to every code point of the line: the Bengali font wherever it has a glyph,
     * otherwise Liberation Sans, otherwise the platform sans-serif font.
     */
    private AttributedString buildFontRuns(String line, Font primary, Font latin, Font system) {
        AttributedString attributed = new AttributedString(line);
        attributed.addAttribute(TextAttribute.FONT, primary, 0, line.length());
        int i = 0;
        while (i < line.length()) {
            int cp = line.codePointAt(i);
            int len = Character.charCount(cp);
            if (!primary.canDisplay(cp)) {
                Font fallback = latin.canDisplay(cp) ? latin : system;
                attributed.addAttribute(TextAttribute.FONT, fallback, i, i + len);
            }
            i += len;
        }
        return attributed;
    }
}
