package bd.july2024;

import bd.july2024.world.TreeLibrary;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class TreeLibraryTest {

    @Test
    public void lodIndexFollowsDistanceBands() {
        assertEquals(0, TreeLibrary.lodIndexFor(0f));
        assertEquals(0, TreeLibrary.lodIndexFor(TreeLibrary.LOD1_DISTANCE - 0.01f));
        assertEquals(1, TreeLibrary.lodIndexFor(TreeLibrary.LOD1_DISTANCE));
        assertEquals(1, TreeLibrary.lodIndexFor(TreeLibrary.LOD2_DISTANCE - 0.01f));
        assertEquals(2, TreeLibrary.lodIndexFor(TreeLibrary.LOD2_DISTANCE));
        assertEquals(2, TreeLibrary.lodIndexFor(10_000f));
    }

    /** Every variant CampusWorld places must ship all three LOD files with their buffers. */
    @Test
    public void treeModelFilesArePresent() {
        File root = new File("..", "assets/Models/Trees");
        if (!root.isDirectory()) root = new File("assets/Models/Trees");
        assertTrue(root.isDirectory(), "assets/Models/Trees missing (cwd " + new File(".").getAbsolutePath() + ")");
        String[][] variants = {{"island_tree_02", "krishnachura"}, {"island_tree_02", "radhachura"},
            {"island_tree_02", "shade"}, {"jacaranda_tree", "jarul"}};
        for (String[] v : variants) {
            for (int lod = 0; lod < TreeLibrary.LOD_COUNT; lod++) {
                File gltf = new File(root, v[0] + "/" + v[1] + "_lod" + lod + ".gltf");
                File bin = new File(root, v[0] + "/" + v[1] + "_lod" + lod + ".bin");
                assertTrue(gltf.isFile(), gltf + " missing");
                assertTrue(bin.isFile() && bin.length() > 1000, bin + " missing or empty");
            }
        }
        assertTrue(new File(root, "LICENSE.txt").isFile());
    }
}
