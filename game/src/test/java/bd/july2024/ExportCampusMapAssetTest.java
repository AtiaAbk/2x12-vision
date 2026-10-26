package bd.july2024;

import bd.july2024.level.MissionTracker;
import bd.july2024.ui.CampusMapRenderer;
import com.jme3.math.Vector3f;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ExportCampusMapAssetTest {

    @Test
    public void exportRealGeographyMapAsset() throws IOException {
        int width = 1264;
        int height = 795;
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        java.awt.Graphics2D g = img.createGraphics();

        MissionTracker tracker = new MissionTracker();
        Vector3f playerPos = new Vector3f(0f, 0f, 17f);
        float playerHeading = 180f;

        CampusMapRenderer.renderMap(g, 0, 0, width, height, playerPos, playerHeading, tracker);
        g.dispose();

        File outFile = AppPaths.asset("maps/campus_map_geography.png");
        if (outFile == null) outFile = new File("assets/maps/campus_map_geography.png");
        if (outFile.getParentFile() != null && !outFile.getParentFile().exists()) {
            outFile.getParentFile().mkdirs();
        }
        boolean ok = ImageIO.write(img, "png", outFile);
        assertTrue(ok);
        assertTrue(outFile.exists());
        assertTrue(outFile.length() > 1000);
    }
}
