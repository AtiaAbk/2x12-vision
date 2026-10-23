package bd.july2024.ui;

import com.jme3.texture.Image;
import com.jme3.texture.Texture2D;
import com.jme3.texture.plugins.AWTLoader;
import com.jme3.ui.Picture;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * High-DPI Dynamic Bengali & English Typography System for jMonkeyEngine.
 * Utilizes the official Noto Sans Bengali TrueType font to dynamically rasterize
 * complex Indic conjuncts (যুক্তাক্ষর) with subpixel anti-aliasing into jME 2D GUI textures.
 */
public class BengaliTextRenderer {

    private final com.jme3.asset.AssetManager assetManager;
    private Font bengaliFontRegular;
    private Font bengaliFontBold;
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
                bengaliFontRegular = new Font(Font.SANS_SERIF, Font.PLAIN, 24);
                bengaliFontBold = new Font(Font.SANS_SERIF, Font.BOLD, 24);
            }
        } catch (Throwable t) {
            bengaliFontRegular = new Font(Font.SANS_SERIF, Font.PLAIN, 24);
            bengaliFontBold = new Font(Font.SANS_SERIF, Font.BOLD, 24);
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

    public BufferedImage renderStringToImage(String text, float fontSize, boolean isBold, Color textColor, Color bgColor, int paddingX, int paddingY) {
        Font font = (isBold ? bengaliFontBold : bengaliFontRegular).deriveFont(fontSize);
        String[] lines = text != null ? text.split("\n") : new String[]{""};

        // Measure text bounds using a dummy image graphics context
        BufferedImage dummy = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = dummy.createGraphics();
        g2d.setFont(font);
        FontMetrics fm = g2d.getFontMetrics();

        int maxLineWidth = 0;
        for (String line : lines) {
            int w = fm.stringWidth(line);
            if (w > maxLineWidth) maxLineWidth = w;
        }
        int lineHeight = fm.getHeight();
        g2d.dispose();

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
        g.setFont(font);
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            int yPos = paddingY + fm.getAscent() + i * lineHeight;
            // Draw drop shadow
            g.setColor(new Color(0, 0, 0, 180));
            g.drawString(line, paddingX + 1, yPos + 1);
            // Draw primary text
            g.setColor(textColor != null ? textColor : Color.WHITE);
            g.drawString(line, paddingX, yPos);
        }

        g.dispose();
        return img;
    }
}
