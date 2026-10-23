package bd.july2024.world;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Cylinder;
import com.jme3.scene.shape.Sphere;

import java.util.ArrayList;
import java.util.List;

/**
 * Authentic 3D Dhaka University Campus Reconstruction for Project July 2024 (Level 01 Vertical Slice).
 * Reconstructs the central corridor:
 * 1. Anti-Terrorism Raju Memorial Sculpture and its circular roundabout
 * 2. Dhaka University Central Library & Hakim Chattar
 * 3. Teacher-Student Centre (TSC) with butterfly roof silhouette
 * 4. July Movement Commemorative Memorial Plaque
 * 5. Tropical Krishnachura shade trees, brick curbs, asphalt roads, and pedestrian pathways.
 */
public class CampusWorld {

    private final Node rootNode;
    private final List<float[]> colliders = new ArrayList<>();

    // PBR Materials
    private Material asphaltMat;
    private Material grassMat;
    private Material concreteMat;
    private Material terracottaBrickMat;
    private Material sculptureMat;
    private Material treeTrunkMat;
    private Material treeFoliageMat;
    private Material bloomMat;
    private Material memorialMat;
    private Material curbMat;

    public CampusWorld(AssetManager assetManager, Node sceneRoot) {
        this.rootNode = new Node("CampusWorld");
        initMaterials(assetManager);
        buildWorld();
        if (sceneRoot != null) {
            sceneRoot.attachChild(rootNode);
        }
    }

    private void initMaterials(AssetManager assetManager) {
        asphaltMat = createLitMaterial(assetManager, new ColorRGBA(0.18f, 0.19f, 0.20f, 1f), 12f);
        grassMat = createLitMaterial(assetManager, new ColorRGBA(0.24f, 0.38f, 0.20f, 1f), 8f);
        concreteMat = createLitMaterial(assetManager, new ColorRGBA(0.82f, 0.80f, 0.77f, 1f), 24f);
        terracottaBrickMat = createLitMaterial(assetManager, new ColorRGBA(0.72f, 0.28f, 0.22f, 1f), 10f);
        sculptureMat = createLitMaterial(assetManager, new ColorRGBA(0.66f, 0.63f, 0.58f, 1f), 18f);
        treeTrunkMat = createLitMaterial(assetManager, new ColorRGBA(0.24f, 0.18f, 0.14f, 1f), 6f);
        treeFoliageMat = createLitMaterial(assetManager, new ColorRGBA(0.28f, 0.52f, 0.22f, 1f), 8f);
        bloomMat = createLitMaterial(assetManager, new ColorRGBA(0.88f, 0.22f, 0.14f, 1f), 10f);
        curbMat = createLitMaterial(assetManager, new ColorRGBA(0.75f, 0.74f, 0.72f, 1f), 20f);

        memorialMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        memorialMat.setColor("Color", new ColorRGBA(1.0f, 0.88f, 0.35f, 1f));
    }

    private Material createLitMaterial(AssetManager assetManager, ColorRGBA diffuse, float shininess) {
        Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        mat.setBoolean("UseMaterialColors", true);
        mat.setColor("Diffuse", diffuse);
        mat.setColor("Specular", ColorRGBA.White.mult(0.15f));
        mat.setFloat("Shininess", shininess);
        return mat;
    }

    private void buildWorld() {
        // 1. Base Campus Ground Plane (Asphalt Road Surface)
        Box groundBox = new Box(75f, 0.1f, 75f);
        Geometry ground = new Geometry("Ground", groundBox);
        ground.setMaterial(asphaltMat);
        ground.setLocalTranslation(0, -0.1f, 0);
        rootNode.attachChild(ground);

        // 2. Anti-Terrorism Raju Memorial Sculpture & Roundabout Island
        buildRajuMemorial();

        // 3. Dhaka University Central Library & Hakim Chattar Colonnade
        buildCentralLibrary();

        // 4. Teacher-Student Centre (TSC)
        buildTSC();

        // 5. July Movement Commemorative Memorial Plaque
        buildMemorialPlaque();

        // 6. Tropical Krishnachura Shade Trees
        buildVegetation();
    }

    private void buildRajuMemorial() {
        // Outer Curb Ring
        Cylinder curbCyl = new Cylinder(32, 32, 8.2f, 0.28f, true);
        Geometry curbGeom = new Geometry("RajuCurb", curbCyl);
        curbGeom.rotate(FastMath.HALF_PI, 0, 0);
        curbGeom.setLocalTranslation(0, 0.14f, 0);
        curbGeom.setMaterial(curbMat);
        rootNode.attachChild(curbGeom);

        // Circular Lawn Island
        Cylinder islandCyl = new Cylinder(32, 32, 8.0f, 0.32f, true);
        Geometry islandGeom = new Geometry("RajuIsland", islandCyl);
        islandGeom.rotate(FastMath.HALF_PI, 0, 0);
        islandGeom.setLocalTranslation(0, 0.16f, 0);
        islandGeom.setMaterial(grassMat);
        rootNode.attachChild(islandGeom);

        // Stepped Concrete Memorial Base Pedestal
        Box baseBox1 = new Box(3.2f, 0.4f, 3.2f);
        Geometry base1 = new Geometry("RajuBase1", baseBox1);
        base1.setMaterial(concreteMat);
        base1.setLocalTranslation(0, 0.4f, 0);
        rootNode.attachChild(base1);
        addCollider(-3.2f, 0, -3.2f, 3.2f, 0.8f, 3.2f);

        Box baseBox2 = new Box(2.4f, 0.5f, 2.4f);
        Geometry base2 = new Geometry("RajuBase2", baseBox2);
        base2.setMaterial(concreteMat);
        base2.setLocalTranslation(0, 0.9f, 0);
        rootNode.attachChild(base2);
        addCollider(-2.4f, 0.8f, -2.4f, 2.4f, 1.4f, 2.4f);

        // 7 Interconnected Stylized Figures Representing Solidarity
        float[] figureHeights = {2.6f, 3.1f, 3.6f, 4.2f, 3.8f, 3.3f, 2.8f};
        for (int i = 0; i < 7; i++) {
            float angle = i * (FastMath.TWO_PI / 7f);
            float radius = 1.4f;
            float h = figureHeights[i];

            Box figBox = new Box(0.32f, h * 0.5f, 0.32f);
            Geometry figGeom = new Geometry("RajuFigure_" + i, figBox);
            figGeom.setMaterial(sculptureMat);
            float fx = FastMath.cos(angle) * radius;
            float fz = FastMath.sin(angle) * radius;
            figGeom.setLocalTranslation(fx, 1.4f + h * 0.5f, fz);
            rootNode.attachChild(figGeom);
            addCollider(fx - 0.35f, 1.4f, fz - 0.35f, fx + 0.35f, 1.4f + h, fz + 0.35f);
        }
    }

    private void buildCentralLibrary() {
        // North Side Campus Block: Central Library & Hakim Chattar
        float libZ = -26.0f;

        // Brick Colonnade Base Walkway
        Box walkBox = new Box(24f, 0.15f, 6.0f);
        Geometry walkGeom = new Geometry("LibWalk", walkBox);
        walkGeom.setMaterial(terracottaBrickMat);
        walkGeom.setLocalTranslation(0f, 0.15f, libZ + 6f);
        rootNode.attachChild(walkGeom);

        // Colonnade Columns (Ground Floor Pilotis)
        for (int c = -5; c <= 5; c++) {
            Cylinder colCyl = new Cylinder(16, 16, 0.42f, 4.8f, true);
            Geometry colGeom = new Geometry("LibCol_" + c, colCyl);
            colGeom.rotate(FastMath.HALF_PI, 0, 0);
            float cx = c * 4.2f;
            float cz = libZ + 4.0f;
            colGeom.setLocalTranslation(cx, 2.4f, cz);
            colGeom.setMaterial(concreteMat);
            rootNode.attachChild(colGeom);
            addCollider(cx - 0.5f, 0f, cz - 0.5f, cx + 0.5f, 4.8f, cz + 0.5f);
        }

        // Library Main Upper Reading Hall (Terracotta Red Brick Block)
        Box upperBox = new Box(24f, 4.5f, 9.0f);
        Geometry upperGeom = new Geometry("LibUpper", upperBox);
        upperGeom.setMaterial(terracottaBrickMat);
        upperGeom.setLocalTranslation(0f, 9.3f, libZ);
        rootNode.attachChild(upperGeom);
        addCollider(-24f, 0f, libZ - 9f, 24f, 13.8f, libZ + 3f);

        // Hakim Chattar Pavilion (Tea & Discussion Corner)
        Box hakimPavilion = new Box(4.5f, 1.8f, 3.5f);
        Geometry hakimGeom = new Geometry("HakimPavilion", hakimPavilion);
        hakimGeom.setMaterial(concreteMat);
        hakimGeom.setLocalTranslation(-14f, 1.8f, libZ + 8f);
        rootNode.attachChild(hakimGeom);
        addCollider(-18.5f, 0f, libZ + 4.5f, -9.5f, 3.6f, libZ + 11.5f);
    }

    private void buildTSC() {
        // South-East Campus Block: Teacher-Student Centre (TSC)
        float tscX = 22.0f;
        float tscZ = 18.0f;

        // Main Auditorium Block
        Box audBox = new Box(12.0f, 4.2f, 10.0f);
        Geometry audGeom = new Geometry("TSCAuditorium", audBox);
        audGeom.setMaterial(concreteMat);
        audGeom.setLocalTranslation(tscX, 4.2f, tscZ);
        rootNode.attachChild(audGeom);
        addCollider(tscX - 12.0f, 0f, tscZ - 10.0f, tscX + 12.0f, 8.4f, tscZ + 10.0f);

        // Iconic Butterfly Roof Cantilever
        Box roofWing1 = new Box(13.5f, 0.35f, 11.5f);
        Geometry roofGeom1 = new Geometry("TSCRoof1", roofWing1);
        roofGeom1.rotate(0.08f, 0f, 0.06f);
        roofGeom1.setLocalTranslation(tscX, 8.9f, tscZ);
        roofGeom1.setMaterial(terracottaBrickMat);
        rootNode.attachChild(roofGeom1);

        // Front Entrance Gate & Columns
        Cylinder gateCol1 = new Cylinder(16, 16, 0.45f, 5.0f, true);
        Geometry g1 = new Geometry("TSCGate1", gateCol1);
        g1.rotate(FastMath.HALF_PI, 0, 0);
        g1.setMaterial(concreteMat);
        g1.setLocalTranslation(tscX - 10f, 2.5f, tscZ - 11f);
        rootNode.attachChild(g1);
        addCollider(tscX - 10.5f, 0f, tscZ - 11.5f, tscX - 9.5f, 5.0f, tscZ - 10.5f);

        Cylinder gateCol2 = new Cylinder(16, 16, 0.45f, 5.0f, true);
        Geometry g2 = new Geometry("TSCGate2", gateCol2);
        g2.rotate(FastMath.HALF_PI, 0, 0);
        g2.setMaterial(concreteMat);
        g2.setLocalTranslation(tscX - 6f, 2.5f, tscZ - 11f);
        rootNode.attachChild(g2);
        addCollider(tscX - 6.5f, 0f, tscZ - 11.5f, tscX - 5.5f, 5.0f, tscZ - 10.5f);
    }

    private void buildMemorialPlaque() {
        // Commemorative July Movement Plaque
        Box plaqueBase = new Box(0.6f, 0.8f, 0.35f);
        Geometry pBase = new Geometry("PlaqueBase", plaqueBase);
        pBase.setMaterial(concreteMat);
        pBase.setLocalTranslation(2f, 0.8f, -6f);
        rootNode.attachChild(pBase);
        addCollider(1.4f, 0f, -6.35f, 2.6f, 1.6f, -5.65f);

        // Golden Memorial Indicator
        Sphere beacon = new Sphere(16, 16, 0.18f);
        Geometry bGeom = new Geometry("PlaqueBeacon", beacon);
        bGeom.setMaterial(memorialMat);
        bGeom.setLocalTranslation(2f, 1.9f, -6f);
        rootNode.attachChild(bGeom);
    }

    private void buildVegetation() {
        // Krishnachura Shade Trees along Roads and Plazas
        float[][] treeLocations = {
            {-14f, 12f}, {-22f, 6f}, {-18f, -8f},
            {16f, -14f}, {24f, -6f}, {12f, -22f},
            {-6f, -18f}, {6f, 20f}, {-12f, 24f}
        };

        for (int i = 0; i < treeLocations.length; i++) {
            float tx = treeLocations[i][0];
            float tz = treeLocations[i][1];

            // Trunk
            Cylinder trunkCyl = new Cylinder(16, 16, 0.32f, 4.2f, true);
            Geometry trunk = new Geometry("TreeTrunk_" + i, trunkCyl);
            trunk.rotate(FastMath.HALF_PI, 0, 0);
            trunk.setMaterial(treeTrunkMat);
            trunk.setLocalTranslation(tx, 2.1f, tz);
            rootNode.attachChild(trunk);
            addCollider(tx - 0.4f, 0f, tz - 0.4f, tx + 0.4f, 4.2f, tz + 0.4f);

            // Spreading Canopy (Deep Green Foliage)
            Sphere canopySphere = new Sphere(16, 16, 2.6f);
            Geometry canopy = new Geometry("TreeCanopy_" + i, canopySphere);
            canopy.setMaterial(treeFoliageMat);
            canopy.setLocalTranslation(tx, 4.8f, tz);
            canopy.setLocalScale(1.3f, 0.65f, 1.3f);
            rootNode.attachChild(canopy);

            // Scarlet Blooming Blossoms (Authentic Bangladesh Krishnachura Summer Bloom)
            Sphere bloomSphere = new Sphere(12, 12, 1.8f);
            Geometry bloomGeom = new Geometry("TreeBloom_" + i, bloomSphere);
            bloomGeom.setMaterial(bloomMat);
            bloomGeom.setLocalTranslation(tx + 0.25f, 5.25f, tz + 0.2f);
            bloomGeom.setLocalScale(1.05f, 0.42f, 1.05f);
            rootNode.attachChild(bloomGeom);
        }
    }

    private void addCollider(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
        colliders.add(new float[]{minX, minY, minZ, maxX, maxY, maxZ});
    }

    public List<float[]> getColliders() {
        return colliders;
    }

    public Node getRootNode() {
        return rootNode;
    }
}
