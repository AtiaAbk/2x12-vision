package bd.july2024.ui;

import com.jme3.texture.Image;
import com.jme3.texture.Texture2D;
import com.jme3.texture.plugins.AWTLoader;
import com.jme3.ui.Picture;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Dynamic Bengali and English typography for the jME GUI, rasterized with Java2D from the bundled
 * Noto Sans Bengali font so complex conjuncts (যুক্তাক্ষর) shape correctly.
 *
 * Fonts created from a file with {@link Font#createFont} do not fall back to other fonts for glyphs
 * they lack, so each line is split into font runs: Noto Sans Bengali first, then the bundled
 * Liberation Sans, then the platform's logical sans-serif font for anything else.
 *
 * {@link #renderPanel} draws the shared HUD panel style: a translucent dark rounded card with a thin
 * border, an optional accent bar, lines in individual sizes and colours, optional word wrapping and an
 * optional custom overlay (used for the stamina bar).
 */
public class BengaliTextRenderer {

    /** One line of a panel with its own size, weight and colour. */
    public static final class Line {
        public final String text;
        public final float size;
        public final boolean bold;
        public final Color color;
        public final int gapBefore;

        public Line(String text, float size, boolean bold, Color color) {
            this(text, size, bold, color, 0);
        }

        public Line(String text, float size, boolean bold, Color color, int gapBefore) {
            this.text = text == null ? "" : text;
            this.size = size;
            this.bold = bold;
            this.color = color;
            this.gapBefore = gapBefore;
        }
    }

    /** Panel background, border, accent and sizing. */
    public static final class PanelStyle {
        public Color background = new Color(10, 12, 16, 178);
        public Color border = new Color(255, 255, 255, 36);
        /** Vertical bar along the left edge, or null for none. */
        public Color accent = null;
        public int radius = 12;
        public int paddingX = 18;
        public int paddingY = 12;
        public int minWidth = 0;
        /** Wrap lines longer than this panel width (0 = no wrapping). */
        public int maxWidth = 0;
        /** Extra space below the text for an overlay. */
        public int extraHeight = 0;
    }

    /** Draws custom content into the space reserved by {@link PanelStyle#extraHeight}. */
    public interface Overlay {
        void draw(Graphics2D g, int x, int y, int width, int height);
    }

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
            File fontFile = bd.july2024.AppPaths.asset("fonts/NotoSansBengali-Variable.ttf");
            if (fontFile.exists()) {
                Font baseFont = Font.createFont(Font.TRUETYPE_FONT, fontFile);
                bengaliFontRegular = baseFont.deriveFont(Font.PLAIN, 24f);
                bengaliFontBold = baseFont.deriveFont(Font.BOLD, 24f);
            } else {
                bengaliFontRegular = systemFontRegular;
                bengaliFontBold = systemFontBold;
            }
        } catch (Throwable t) {
            bengaliFontRegular = systemFontRegular;
            bengaliFontBold = systemFontBold;
        }

        try {
            File regular = bd.july2024.AppPaths.asset("fonts/LiberationSans-Regular.ttf");
            File bold = bd.july2024.AppPaths.asset("fonts/LiberationSans-Bold.ttf");
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

    /** Wraps a rendered image in a GUI Picture sized 1:1 to the image. */
    public Picture createPicture(String key, BufferedImage img) {
        long glContext = org.lwjgl.glfw.GLFW.glfwGetCurrentContext();
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

    /** Styled panel picture: see {@link #renderPanel}. */
    public Picture createPanelPicture(String key, List<Line> lines, PanelStyle style, Overlay overlay) {
        return createPicture(key, renderPanel(lines, style, overlay));
    }

    /**
     * Creates a Picture quad with rendered Bengali/English text (single size and colour, plain
     * rounded background). Kept for simple callers.
     */
    public Picture createTextPicture(String key, String text, float fontSize, boolean isBold, Color textColor, Color bgColor, int paddingX, int paddingY) {
        return createPicture(key, renderStringToImage(text, fontSize, isBold, textColor, bgColor, paddingX, paddingY));
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
        List<Line> lines = new ArrayList<>();
        for (String l : (text != null ? text : "").split("\n")) {
            lines.add(new Line(l, fontSize, isBold, textColor != null ? textColor : Color.WHITE));
        }
        PanelStyle style = new PanelStyle();
        style.background = bgColor;
        style.border = null;
        style.radius = 8;
        style.paddingX = paddingX;
        style.paddingY = paddingY;
        return renderPanel(lines, style, null);
    }

    /** One laid-out row of text (a Line may wrap into several rows). */
    private static final class Row {
        final TextLayout layout;
        final Color color;
        final float height;
        final float ascent;
        final int gapBefore;

        Row(TextLayout layout, Color color, float height, float ascent, int gapBefore) {
            this.layout = layout;
            this.color = color;
            this.height = height;
            this.ascent = ascent;
            this.gapBefore = gapBefore;
        }
    }

    /**
     * Renders a panel: translucent rounded background, optional border and accent bar, text lines
     * with per-line size, weight and colour, word wrapping when {@code style.maxWidth} is set, and an
     * optional overlay drawn in {@code style.extraHeight} pixels below the text.
     */
    public BufferedImage renderPanel(List<Line> lines, PanelStyle style, Overlay overlay) {
        if (style == null) style = new PanelStyle();
        FontRenderContext frc = new FontRenderContext(null, true, true);
        int accentWidth = style.accent != null ? 10 : 0;
        int wrapWidth = style.maxWidth > 0 ? Math.max(40, style.maxWidth - style.paddingX * 2 - accentWidth) : 0;

        List<Row> rows = new ArrayList<>();
        float maxLineWidth = 0f;
        for (Line line : lines) {
            Font primary = (line.bold ? bengaliFontBold : bengaliFontRegular).deriveFont(line.size);
            Font latin = (line.bold ? latinFontBold : latinFontRegular).deriveFont(line.size);
            Font system = (line.bold ? systemFontBold : systemFontRegular).deriveFont(line.size);
            if (line.text.isEmpty()) {
                TextLayout blank = new TextLayout(" ", primary, frc);
                rows.add(new Row(null, line.color, blank.getAscent() + blank.getDescent() + blank.getLeading(), blank.getAscent(), line.gapBefore));
                continue;
            }
            AttributedString attributed = buildFontRuns(line.text, primary, latin, system);
            boolean first = true;
            if (wrapWidth > 0) {
                LineBreakMeasurer measurer = new LineBreakMeasurer(attributed.getIterator(), frc);
                while (measurer.getPosition() < line.text.length()) {
                    TextLayout layout = measurer.nextLayout(wrapWidth);
                    if (layout == null) break;
                    rows.add(new Row(layout, line.color, layout.getAscent() + layout.getDescent() + layout.getLeading(),
                        layout.getAscent(), first ? line.gapBefore : 0));
                    maxLineWidth = Math.max(maxLineWidth, layout.getAdvance());
                    first = false;
                }
            } else {
                TextLayout layout = new TextLayout(attributed.getIterator(), frc);
                rows.add(new Row(layout, line.color, layout.getAscent() + layout.getDescent() + layout.getLeading(),
                    layout.getAscent(), line.gapBefore));
                maxLineWidth = Math.max(maxLineWidth, layout.getAdvance());
            }
        }

        float textHeight = 0f;
        for (Row r : rows) textHeight += r.height + r.gapBefore;
        int contentWidth = (int) Math.ceil(maxLineWidth);
        int totalWidth = Math.max(1, Math.max(style.minWidth, contentWidth + style.paddingX * 2 + accentWidth));
        int totalHeight = Math.max(1, (int) Math.ceil(textHeight) + style.paddingY * 2 + style.extraHeight);
        int innerWidth = totalWidth - style.paddingX * 2 - accentWidth;

        BufferedImage img = new BufferedImage(totalWidth, totalHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        RoundRectangle2D shape = new RoundRectangle2D.Float(0, 0, totalWidth, totalHeight, style.radius, style.radius);
        if (style.background != null && style.background.getAlpha() > 0) {
            g.setColor(style.background);
            g.fill(shape);
        }
        if (style.accent != null) {
            Shape oldClip = g.getClip();
            g.setClip(shape);
            g.setColor(style.accent);
            g.fillRect(0, 0, 4, totalHeight);
            g.setClip(oldClip);
        }
        if (style.border != null && style.border.getAlpha() > 0) {
            g.setColor(style.border);
            g.setStroke(new BasicStroke(1f));
            g.draw(new RoundRectangle2D.Float(0.5f, 0.5f, totalWidth - 1f, totalHeight - 1f, style.radius, style.radius));
        }

        float x = style.paddingX + accentWidth;
        float y = style.paddingY;
        for (Row r : rows) {
            y += r.gapBefore;
            if (r.layout != null) {
                float baseline = y + r.ascent;
                g.setColor(new Color(0, 0, 0, 110));
                r.layout.draw(g, x + 1f, baseline + 1f);
                g.setColor(r.color != null ? r.color : Color.WHITE);
                r.layout.draw(g, x, baseline);
            }
            y += r.height;
        }

        if (overlay != null && style.extraHeight > 0) {
            overlay.draw(g, (int) x, (int) Math.ceil(y), innerWidth, style.extraHeight);
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
