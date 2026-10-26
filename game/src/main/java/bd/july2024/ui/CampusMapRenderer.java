package bd.july2024.ui;

import bd.july2024.level.MissionTracker;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;

/**
 * Geographically grounded campus map renderer for Dhaka University (Level 01).
 * Translates in-engine 3D Cartesian coordinates (Origin at Raju Memorial, +X East, +Z South)
 * into a high-contrast, authentic cartographic layout with dynamic live player orientation,
 * active objective tracking beacon, authentic architectural footprints, roads, and bilingual labels.
 */
public class CampusMapRenderer {

    // Visual Palette: Authentic Cartographic South Asian Campus
    private static final Color BG_DARK = new Color(11, 14, 20);
    private static final Color GRID_LINE = new Color(25, 32, 44, 140);
    private static final Color LAWN_GREEN = new Color(28, 52, 34);
    private static final Color POND_BLUE = new Color(24, 64, 88);
    private static final Color POND_SHORE = new Color(38, 92, 120, 160);
    private static final Color ROAD_ASPHALT = new Color(42, 47, 56);
    private static final Color ROAD_KERB = new Color(80, 88, 100);
    private static final Color PATH_PAVING = new Color(92, 86, 78);
    private static final Color BLDG_FILL = new Color(130, 48, 42); // Brick red
    private static final Color BLDG_BORDER = new Color(210, 160, 120);
    private static final Color MODERNIST_FILL = new Color(54, 70, 84); // Tropical modernist grey-slate
    private static final Color MODERNIST_BORDER = new Color(150, 180, 200);
    private static final Color GOLD = new Color(236, 178, 46);
    private static final Color CYAN = new Color(64, 204, 224);
    private static final Color TEXT_WHITE = new Color(240, 244, 248);
    private static final Color TEXT_MUTED = new Color(160, 168, 178);

    // Map geographic projection scale
    public static final float PIXELS_PER_METER = 2.45f;
    public static final float CENTER_OFFSET_X = 0f;
    public static final float CENTER_OFFSET_Z = 12f;

    public static float worldToMapX(float wx, int mapWidth) {
        return (mapWidth * 0.5f) + (wx - CENTER_OFFSET_X) * PIXELS_PER_METER;
    }

    public static float worldToMapY(float wz, int mapHeight) {
        return (mapHeight * 0.48f) + (wz - CENTER_OFFSET_Z) * PIXELS_PER_METER;
    }

    /**
     * Renders the complete vector-driven geographic campus map onto the provided Graphics2D context.
     */
    public static void renderMap(Graphics2D g, int x, int y, int width, int height,
                                 Vector3f playerPos, float playerHeadingDegrees,
                                 MissionTracker tracker) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // 1. Base Map Canvas & Frame
        g.setColor(BG_DARK);
        g.fillRoundRect(x, y, width, height, 10, 10);
        g.setColor(new Color(255, 255, 255, 40));
        g.drawRoundRect(x, y, width - 1, height - 1, 10, 10);

        Shape clipBackup = g.getClip();
        g.clip(new RoundRectangle2D.Float(x, y, width, height, 10, 10));

        // 2. Coordinate Grid Lines
        g.setColor(GRID_LINE);
        g.setStroke(new BasicStroke(1.0f));
        int gridStep = 50;
        for (int gx = x; gx < x + width; gx += gridStep) {
            g.drawLine(gx, y, gx, y + height);
        }
        for (int gy = y; gy < y + height; gy += gridStep) {
            g.drawLine(x, gy, x + width, gy);
        }

        // 3. Campus Green Lawns and Water Bodies
        drawLawnsAndPonds(g, x, y, width, height);

        // 4. Roads, Roundabout & Walkways
        drawRoadsAndPaths(g, x, y, width, height);

        // 5. Authentic Architectural Building Footprints
        drawBuildingFootprints(g, x, y, width, height);

        // 6. Landmark Badges and Bilingual Labels
        drawLandmarkLabels(g, x, y, width, height);

        // 7. Dynamic Active Mission Target Marker
        if (tracker != null && !tracker.isAllCompleted()) {
            Vector3f target = tracker.getCurrentObjective().getTargetPosition();
            float tx = x + worldToMapX(target.x, width);
            float ty = y + worldToMapY(target.z, height);
            drawTargetMarker(g, tx, ty, tracker);
        }

        // 8. Dynamic Player Location Marker & Heading Arrow
        if (playerPos != null) {
            float px = x + worldToMapX(playerPos.x, width);
            float py = y + worldToMapY(playerPos.z, height);
            drawPlayerMarker(g, px, py, playerHeadingDegrees);
        }

        // 9. North Compass Rose & Scale Bar
        drawCompassAndScale(g, x, y, width, height);

        // 10. Map Legend & Controls Discovery Bar
        drawLegendAndFooter(g, x, y, width, height);

        g.setClip(clipBackup);
    }

    private static void drawLawnsAndPonds(Graphics2D g, int mx, int my, int w, int h) {
        // Curzon Hall front lawn and garden
        float curzonX = mx + worldToMapX(64f, w);
        float curzonY = my + worldToMapY(64f, h);
        g.setColor(LAWN_GREEN);
        g.fillRoundRect((int) (curzonX - 55), (int) (curzonY - 45), 110, 90, 16, 16);

        // Curzon Pukur / Historic Pond
        float pondX = mx + worldToMapX(64f, w) - 10;
        float pondY = my + worldToMapY(64f, h) + 26;
        g.setColor(POND_SHORE);
        g.fillRoundRect((int) (pondX - 28), (int) (pondY - 18), 56, 36, 12, 12);
        g.setColor(POND_BLUE);
        g.fillRoundRect((int) (pondX - 25), (int) (pondY - 15), 50, 30, 8, 8);
        g.setColor(new Color(255, 255, 255, 80));
        g.drawString("পুকুর (Pukur)", pondX - 18, pondY + 4);

        // TSC Green Court / Field
        float tscX = mx + worldToMapX(10f, w);
        float tscY = my + worldToMapY(67f, h);
        g.setColor(LAWN_GREEN);
        g.fillRoundRect((int) (tscX - 45), (int) (tscY - 20), 80, 75, 14, 14);

        // Central Library & Hakim Chattar Green Lawn
        float libX = mx + worldToMapX(-60f, w);
        float libY = my + worldToMapY(-70f, h);
        g.setColor(LAWN_GREEN);
        g.fillRoundRect((int) (libX - 55), (int) (libY - 45), 110, 95, 14, 14);

        // Raju Roundabout Center Lawn
        float rajuX = mx + worldToMapX(0f, w);
        float rajuY = my + worldToMapY(0f, h);
        float rR = 10f * PIXELS_PER_METER;
        g.setColor(LAWN_GREEN);
        g.fill(new Ellipse2D.Float(rajuX - rR, rajuY - rR, rR * 2, rR * 2));
    }

    private static void drawRoadsAndPaths(Graphics2D g, int mx, int my, int w, int h) {
        float cx = mx + worldToMapX(0f, w);
        float cy = my + worldToMapY(0f, h);

        // Roundabout Road Ring
        float ringR = 25f * PIXELS_PER_METER;
        g.setStroke(new BasicStroke(14f));
        g.setColor(ROAD_KERB);
        g.draw(new Ellipse2D.Float(cx - ringR, cy - ringR, ringR * 2, ringR * 2));
        g.setStroke(new BasicStroke(11f));
        g.setColor(ROAD_ASPHALT);
        g.draw(new Ellipse2D.Float(cx - ringR, cy - ringR, ringR * 2, ringR * 2));

        // Four Main Road Arteries
        g.setStroke(new BasicStroke(12f));
        g.setColor(ROAD_ASPHALT);

        // Road South: towards TSC & Curzon Avenue
        g.drawLine((int) cx, (int) cy + 30, (int) cx + 25, my + h - 40);
        // Road West: Fuller Road towards Central Library & Hakim Chattar
        g.drawLine((int) cx - 30, (int) cy, mx + 30, (int) (cy - 120));
        // Road North: Towards Shahbagh
        g.drawLine((int) cx, (int) cy - 30, (int) cx, my + 30);
        // Road East: Towards Suhrawardy Udyan
        g.drawLine((int) cx + 30, (int) cy, mx + w - 30, (int) cy);

        // Curzon Avenue connector branch
        float curzonX = mx + worldToMapX(64f, w);
        float curzonY = my + worldToMapY(64f, h);
        g.setStroke(new BasicStroke(9f));
        g.setColor(PATH_PAVING);
        g.drawLine((int) (cx + 15), (int) (cy + 60), (int) curzonX, (int) curzonY);
    }

    private static void drawBuildingFootprints(Graphics2D g, int mx, int my, int w, int h) {
        g.setStroke(new BasicStroke(1.5f));

        // 1. Curzon Hall (Indo-Saracenic Complex Footprint)
        float curzonX = mx + worldToMapX(64f, w);
        float curzonY = my + worldToMapY(64f, h);
        // Center block
        g.setColor(BLDG_FILL);
        g.fillRoundRect((int) (curzonX - 16), (int) (curzonY - 14), 32, 28, 4, 4);
        // West and East wings
        g.fillRoundRect((int) (curzonX - 42), (int) (curzonY - 8), 26, 16, 4, 4);
        g.fillRoundRect((int) (curzonX + 16), (int) (curzonY - 8), 26, 16, 4, 4);
        g.setColor(BLDG_BORDER);
        g.drawRoundRect((int) (curzonX - 16), (int) (curzonY - 14), 32, 28, 4, 4);
        g.drawRoundRect((int) (curzonX - 42), (int) (curzonY - 8), 26, 16, 4, 4);
        g.drawRoundRect((int) (curzonX + 16), (int) (curzonY - 8), 26, 16, 4, 4);

        // 2. TSC Complex (Student Union & Parabolic Auditorium)
        float tscX = mx + worldToMapX(10f, w);
        float tscY = my + worldToMapY(67f, h);
        // Student Union Block
        g.setColor(MODERNIST_FILL);
        g.fillRoundRect((int) (tscX - 35), (int) (tscY - 10), 70, 20, 3, 3);
        g.setColor(MODERNIST_BORDER);
        g.drawRoundRect((int) (tscX - 35), (int) (tscY - 10), 70, 20, 3, 3);
        // Auditorium with curved shell
        float audX = tscX - 12;
        float audY = tscY + 28;
        g.setColor(MODERNIST_FILL);
        g.fillRoundRect((int) (audX - 25), (int) (audY - 15), 50, 30, 8, 8);
        g.setColor(MODERNIST_BORDER);
        g.drawRoundRect((int) (audX - 25), (int) (audY - 15), 50, 30, 8, 8);

        // 3. Central Library (Muzharul Islam's Pavilion Blocks)
        float libX = mx + worldToMapX(-45f, w);
        float libY = my + worldToMapY(-85f, h);
        g.setColor(MODERNIST_FILL);
        g.fillRoundRect((int) (libX - 24), (int) (libY - 32), 36, 60, 4, 4);
        g.fillRoundRect((int) (libX - 48), (int) (libY - 15), 24, 40, 4, 4);
        g.setColor(MODERNIST_BORDER);
        g.drawRoundRect((int) (libX - 24), (int) (libY - 32), 36, 60, 4, 4);
        g.drawRoundRect((int) (libX - 48), (int) (libY - 15), 24, 40, 4, 4);

        // 4. Hakim Chattar Pavilion Platform
        float hakimX = mx + worldToMapX(-83f, w);
        float hakimY = my + worldToMapY(-46f, h);
        g.setColor(new Color(90, 60, 50));
        g.fillRoundRect((int) (hakimX - 12), (int) (hakimY - 10), 24, 20, 4, 4);
        g.setColor(GOLD);
        g.drawRoundRect((int) (hakimX - 12), (int) (hakimY - 10), 24, 20, 4, 4);
    }

    private static void drawLandmarkLabels(Graphics2D g, int mx, int my, int w, int h) {
        g.setFont(new Font("SansSerif", Font.BOLD, 11));

        drawBadge(g, mx + worldToMapX(0f, w), my + worldToMapY(0f, h) - 24, "১. রাজু স্মারক ভাস্কর্য", "Raju Memorial");
        drawBadge(g, mx + worldToMapX(10f, w), my + worldToMapY(67f, h) - 18, "২. টিএসসি প্রাঙ্গণ", "TSC Centre");
        drawBadge(g, mx + worldToMapX(-62f, w), my + worldToMapY(-55f, h), "৩. হাকিম চত্বর ও লাইব্রেরি", "Hakim Chattar & Library");
        drawBadge(g, mx + worldToMapX(64f, w), my + worldToMapY(64f, h) - 24, "৪. কার্জন হল", "Curzon Hall");
    }

    private static void drawBadge(Graphics2D g, float bx, float by, String bn, String en) {
        int pad = 5;
        FontMetrics fm = g.getFontMetrics();
        int bw = Math.max(fm.stringWidth(bn), fm.stringWidth(en)) + pad * 2;
        int bh = 28;

        g.setColor(new Color(12, 16, 22, 210));
        g.fillRoundRect((int) (bx - bw * 0.5f), (int) by, bw, bh, 6, 6);
        g.setColor(new Color(255, 255, 255, 60));
        g.drawRoundRect((int) (bx - bw * 0.5f), (int) by, bw, bh, 6, 6);

        g.setColor(GOLD);
        g.drawString(bn, bx - bw * 0.5f + pad, by + 12);
        g.setColor(TEXT_MUTED);
        g.drawString(en, bx - bw * 0.5f + pad, by + 24);
    }

    private static void drawPlayerMarker(Graphics2D g, float px, float py, float headingDeg) {
        // Concentric radar ring
        g.setColor(new Color(236, 178, 46, 70));
        g.fill(new Ellipse2D.Float(px - 14, py - 14, 28, 28));
        g.setColor(new Color(236, 178, 46, 180));
        g.draw(new Ellipse2D.Float(px - 14, py - 14, 28, 28));

        // Player Center Dot
        g.setColor(GOLD);
        g.fill(new Ellipse2D.Float(px - 6, py - 6, 12, 12));
        g.setColor(Color.WHITE);
        g.fill(new Ellipse2D.Float(px - 2.5f, py - 2.5f, 5, 5));

        // Direction Arrow pointing along player heading
        // In jME: Heading 0° is North (-Z = -Y on map), 90° East (+X), 180° South (+Z = +Y on map)
        float rad = (headingDeg - 90f) * FastMath.DEG_TO_RAD;
        float arrowLen = 18f;
        float tipX = px + FastMath.cos(rad) * arrowLen;
        float tipY = py + FastMath.sin(rad) * arrowLen;

        float leftRad = rad + 2.5f;
        float rightRad = rad - 2.5f;
        float bLen = 9f;
        float b1X = px + FastMath.cos(leftRad) * bLen;
        float b1Y = py + FastMath.sin(leftRad) * bLen;
        float b2X = px + FastMath.cos(rightRad) * bLen;
        float b2Y = py + FastMath.sin(rightRad) * bLen;

        Polygon arrow = new Polygon();
        arrow.addPoint((int) tipX, (int) tipY);
        arrow.addPoint((int) b1X, (int) b1Y);
        arrow.addPoint((int) b2X, (int) b2Y);

        g.setColor(GOLD);
        g.fill(arrow);
        g.setColor(Color.WHITE);
        g.draw(arrow);
    }

    private static void drawTargetMarker(Graphics2D g, float tx, float ty, MissionTracker tracker) {
        g.setColor(new Color(64, 204, 224, 80));
        g.fill(new Ellipse2D.Float(tx - 16, ty - 16, 32, 32));
        g.setColor(CYAN);
        g.setStroke(new BasicStroke(2.0f));
        g.draw(new Ellipse2D.Float(tx - 16, ty - 16, 32, 32));
        g.fill(new Ellipse2D.Float(tx - 4, ty - 4, 8, 8));

        // Active Target Banner Tag
        String targetText = "★ সক্রিয় লক্ষ্য (TARGET)";
        g.setFont(new Font("SansSerif", Font.BOLD, 10));
        FontMetrics fm = g.getFontMetrics();
        int tw = fm.stringWidth(targetText) + 8;
        g.setColor(new Color(10, 30, 40, 220));
        g.fillRoundRect((int) (tx - tw * 0.5f), (int) (ty + 14), tw, 18, 4, 4);
        g.setColor(CYAN);
        g.drawRoundRect((int) (tx - tw * 0.5f), (int) (ty + 14), tw, 18, 4, 4);
        g.drawString(targetText, tx - tw * 0.5f + 4, ty + 27);
    }

    private static void drawCompassAndScale(Graphics2D g, int mx, int my, int w, int h) {
        // Cartographic North Arrow at Top-Right
        int nx = mx + w - 42;
        int ny = my + 38;
        g.setColor(new Color(15, 20, 28, 220));
        g.fillRoundRect(nx - 18, ny - 24, 36, 48, 6, 6);
        g.setColor(new Color(255, 255, 255, 60));
        g.drawRoundRect(nx - 18, ny - 24, 36, 48, 6, 6);

        Polygon northArrow = new Polygon();
        northArrow.addPoint(nx, ny - 16);
        northArrow.addPoint(nx - 7, ny + 2);
        northArrow.addPoint(nx + 7, ny + 2);
        g.setColor(GOLD);
        g.fill(northArrow);

        g.setColor(TEXT_WHITE);
        g.setFont(new Font("SansSerif", Font.BOLD, 10));
        g.drawString("N", nx - 4, ny + 17);

        // Metric Scale Bar (50 meters)
        int barW = (int) (50f * PIXELS_PER_METER);
        int sx = mx + 20;
        int sy = my + h - 42;
        g.setColor(new Color(15, 20, 28, 220));
        g.fillRoundRect(sx - 8, sy - 14, barW + 16, 28, 4, 4);
        g.setColor(new Color(255, 255, 255, 60));
        g.drawRoundRect(sx - 8, sy - 14, barW + 16, 28, 4, 4);

        g.setColor(TEXT_WHITE);
        g.setStroke(new BasicStroke(2.5f));
        g.drawLine(sx, sy, sx + barW, sy);
        g.drawLine(sx, sy - 4, sx, sy + 4);
        g.drawLine(sx + barW / 2, sy - 2, sx + barW / 2, sy + 2);
        g.drawLine(sx + barW, sy - 4, sx + barW, sy + 4);
        g.drawString("0", sx - 3, sy + 10);
        g.drawString("25", sx + barW / 2 - 6, sy + 10);
        g.drawString("50 m", sx + barW - 12, sy + 10);
    }

    private static void drawLegendAndFooter(Graphics2D g, int mx, int my, int w, int h) {
        // Bottom Legend Banner
        int lw = 440;
        int lh = 22;
        int lx = mx + (w - lw) / 2;
        int ly = my + h - 28;

        g.setColor(new Color(12, 16, 24, 235));
        g.fillRoundRect(lx, ly, lw, lh, 6, 6);
        g.setColor(new Color(255, 255, 255, 45));
        g.drawRoundRect(lx, ly, lw, lh, 6, 6);

        g.setFont(new Font("SansSerif", Font.PLAIN, 10));
        int tx = lx + 12;

        // Player dot
        g.setColor(GOLD);
        g.fillOval(tx, ly + 6, 8, 8);
        g.setColor(TEXT_WHITE);
        g.drawString("আপনি (You)", tx + 12, ly + 14);

        // Target dot
        tx += 90;
        g.setColor(CYAN);
        g.fillOval(tx, ly + 6, 8, 8);
        g.setColor(TEXT_WHITE);
        g.drawString("লক্ষ্য (Target)", tx + 12, ly + 14);

        // Historic Landmark
        tx += 95;
        g.setColor(BLDG_FILL);
        g.fillRect(tx, ly + 6, 8, 8);
        g.setColor(BLDG_BORDER);
        g.drawRect(tx, ly + 6, 8, 8);
        g.setColor(TEXT_WHITE);
        g.drawString("স্থাপনা (Landmark)", tx + 12, ly + 14);

        // Controls
        tx += 115;
        g.setColor(TEXT_MUTED);
        g.drawString("M / Esc: বন্ধ (Close)", tx, ly + 14);
    }
}
