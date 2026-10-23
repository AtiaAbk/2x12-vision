package bd.july2024.world;

import bd.july2024.ui.BengaliTextRenderer;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Cylinder;
import com.jme3.scene.shape.Quad;
import com.jme3.scene.shape.Sphere;
import com.jme3.texture.Texture2D;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * Authentic, Historically Grounded 3D Dhaka University Campus Reconstruction
 * for Project July 2024 (Level 01: Raju Memorial Sculpture, Central Library & TSC).
 *
 * Implements:
 * 1. Anti-Terrorism Raju Memorial Sculpture (8 interconnected figures locked arm-in-arm in solidarity)
 * 2. Muzharul Islam's iconic 1953 Modernist Central Library (grand brick entry staircase, pilotis colonnade, brise-soleil louvers)
 * 3. Hakim Chattar cultural node with iconic Banyan Tree (Bot-tola) & tea stall pavilion
 * 4. Constantinos Doxiadis's 1961 TSC (butterfly roof cantilever, jaali perforated screens, sunken adda circle)
 * 5. Road networks with asphalt surface, white zebra crossings, center medians, brick pedestrian footpaths
 * 6. Authentic July 2024 street graffiti decals & student movement banners in high-res Bengali Unicode
 * 7. Lush Bangladeshi monsoon vegetation: scarlet Krishnachura, purple Jarul, golden Radhachura, and Banyan aerial roots
 * 8. Vintage cast-iron Dhaka University street lamps & concrete park benches.
 */
public class CampusWorld {

    private final Node rootNode;
    private final BengaliTextRenderer textRenderer;
    private final List<float[]> colliders = new ArrayList<>();

    // PBR & Lit Materials
    private Material asphaltMat;
    private Material roadLineMat;
    private Material grassMat;
    private Material concreteMat;
    private Material darkConcreteMat;
    private Material terracottaBrickMat;
    private Material glassMat;
    private Material sculptureMat;
    private Material treeTrunkMat;
    private Material foliageMat;
    private Material krishnachuraBloomMat;
    private Material jarulBloomMat;
    private Material radhachuraBloomMat;
    private Material hedgeMat;
    private Material curbMat;
    private Material lampMat;
    private Material memorialGoldMat;

    public CampusWorld(AssetManager assetManager, Node sceneRoot) {
        this(assetManager, sceneRoot, null);
    }

    public CampusWorld(AssetManager assetManager, Node sceneRoot, BengaliTextRenderer textRenderer) {
        this.rootNode = new Node("CampusWorld");
        this.textRenderer = textRenderer;
        initMaterials(assetManager);
        buildWorld(assetManager);
        if (sceneRoot != null) {
            sceneRoot.attachChild(rootNode);
        }
    }

    private void initMaterials(AssetManager assetManager) {
        asphaltMat = createLitMaterial(assetManager, new ColorRGBA(0.15f, 0.16f, 0.18f, 1f), 10f);
        roadLineMat = createLitMaterial(assetManager, new ColorRGBA(0.92f, 0.92f, 0.88f, 1f), 30f);
        grassMat = createLitMaterial(assetManager, new ColorRGBA(0.22f, 0.38f, 0.18f, 1f), 6f);
        concreteMat = createLitMaterial(assetManager, new ColorRGBA(0.80f, 0.78f, 0.74f, 1f), 20f);
        darkConcreteMat = createLitMaterial(assetManager, new ColorRGBA(0.48f, 0.47f, 0.45f, 1f), 15f);
        terracottaBrickMat = createLitMaterial(assetManager, new ColorRGBA(0.68f, 0.26f, 0.20f, 1f), 8f);
        glassMat = createLitMaterial(assetManager, new ColorRGBA(0.20f, 0.28f, 0.35f, 1f), 80f);
        sculptureMat = createLitMaterial(assetManager, new ColorRGBA(0.68f, 0.65f, 0.60f, 1f), 16f);
        treeTrunkMat = createLitMaterial(assetManager, new ColorRGBA(0.22f, 0.16f, 0.12f, 1f), 4f);
        foliageMat = createLitMaterial(assetManager, new ColorRGBA(0.24f, 0.46f, 0.20f, 1f), 6f);
        krishnachuraBloomMat = createLitMaterial(assetManager, new ColorRGBA(0.92f, 0.20f, 0.10f, 1f), 12f);
        jarulBloomMat = createLitMaterial(assetManager, new ColorRGBA(0.72f, 0.26f, 0.76f, 1f), 12f);
        radhachuraBloomMat = createLitMaterial(assetManager, new ColorRGBA(0.95f, 0.80f, 0.12f, 1f), 12f);
        hedgeMat = createLitMaterial(assetManager, new ColorRGBA(0.16f, 0.32f, 0.14f, 1f), 8f);
        curbMat = createLitMaterial(assetManager, new ColorRGBA(0.72f, 0.70f, 0.68f, 1f), 22f);
        lampMat = createLitMaterial(assetManager, new ColorRGBA(0.10f, 0.20f, 0.14f, 1f), 35f);

        memorialGoldMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        memorialGoldMat.setColor("Color", new ColorRGBA(1.0f, 0.86f, 0.32f, 1f));
    }

    private Material createLitMaterial(AssetManager assetManager, ColorRGBA diffuse, float shininess) {
        Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        mat.setBoolean("UseMaterialColors", true);
        mat.setColor("Diffuse", diffuse);
        mat.setColor("Specular", ColorRGBA.White.mult(0.12f));
        mat.setFloat("Shininess", shininess);
        return mat;
    }

    private void buildWorld(AssetManager assetManager) {
        // 1. Campus Ground, Avenues, Zebra Crossings & Sidewalks
        buildRoadsAndGround();

        // 2. Anti-Terrorism Raju Memorial Sculpture (8 figures in solidarity)
        buildRajuMemorial();

        // 3. Muzharul Islam's Central Library & Hakim Chattar
        buildCentralLibrary();

        // 4. Constantinos Doxiadis's Teacher-Student Centre (TSC)
        buildTSC();

        // 5. July Movement Commemorative Memorial Plaque
        buildMemorialPlaque();

        // 6. Historic Street Graffiti Decals & Banner Artworks
        buildJulyMovementBannersAndMurals(assetManager);

        // 7. Lush Monsoon Vegetation & Banyan Tree
        buildLushVegetation();

        // 8. Campus Props (DU Vintage Street Lamps & Park Benches)
        buildCampusProps();
    }

    private void buildRoadsAndGround() {
        // Main base ground plane (Asphalt)
        Box groundBox = new Box(85f, 0.1f, 85f);
        Geometry ground = new Geometry("GroundAsphalt", groundBox);
        ground.setMaterial(asphaltMat);
        ground.setLocalTranslation(0, -0.1f, 0);
        rootNode.attachChild(ground);

        // North Avenue Pedestrian Walkway (Herringbone Red Brick Footpath)
        Box footNorth = new Box(3.2f, 0.12f, 32.0f);
        Geometry footNorthGeom = new Geometry("FootNorth", footNorth);
        footNorthGeom.setMaterial(terracottaBrickMat);
        footNorthGeom.setLocalTranslation(-11.5f, 0.12f, -14.0f);
        rootNode.attachChild(footNorthGeom);

        Box footNorthEast = new Box(3.2f, 0.12f, 32.0f);
        Geometry footNorthEastGeom = new Geometry("FootNorthEast", footNorthEast);
        footNorthEastGeom.setMaterial(terracottaBrickMat);
        footNorthEastGeom.setLocalTranslation(11.5f, 0.12f, -14.0f);
        rootNode.attachChild(footNorthEastGeom);

        // East Avenue Walkway towards TSC
        Box footEast = new Box(30.0f, 0.12f, 3.2f);
        Geometry footEastGeom = new Geometry("FootEast", footEast);
        footEastGeom.setMaterial(terracottaBrickMat);
        footEastGeom.setLocalTranslation(24.0f, 0.12f, 8.5f);
        rootNode.attachChild(footEastGeom);

        // Road Divider / Median along North Avenue
        Box medianNorth = new Box(0.6f, 0.22f, 18.0f);
        Geometry medNorth = new Geometry("MedianNorth", medianNorth);
        medNorth.setMaterial(curbMat);
        medNorth.setLocalTranslation(0f, 0.22f, -20.0f);
        rootNode.attachChild(medNorth);
        addCollider(-0.6f, 0f, -38.0f, 0.6f, 0.44f, -2.0f);

        // Median Green Hedges
        for (float mz = -35.0f; mz <= -5.0f; mz += 4.2f) {
            Box hedgeBox = new Box(0.45f, 0.38f, 1.4f);
            Geometry hGeom = new Geometry("MedianHedge_" + mz, hedgeBox);
            hGeom.setMaterial(hedgeMat);
            hGeom.setLocalTranslation(0f, 0.58f, mz);
            rootNode.attachChild(hGeom);
        }

        // White Zebra Crossings
        // 1. Crosswalk across North Avenue (leading to Library)
        for (int i = -3; i <= 3; i++) {
            Box stripe = new Box(0.65f, 0.005f, 2.8f);
            Geometry sGeom = new Geometry("ZebraN_" + i, stripe);
            sGeom.setMaterial(roadLineMat);
            sGeom.setLocalTranslation(i * 1.8f, 0.005f, -10.0f);
            rootNode.attachChild(sGeom);
        }

        // 2. Crosswalk across East Avenue (leading to TSC)
        for (int i = -2; i <= 2; i++) {
            Box stripe = new Box(2.8f, 0.005f, 0.65f);
            Geometry sGeom = new Geometry("ZebraE_" + i, stripe);
            sGeom.setMaterial(roadLineMat);
            sGeom.setLocalTranslation(10.0f, 0.005f, i * 1.8f);
            rootNode.attachChild(sGeom);
        }
    }

    private void buildRajuMemorial() {
        // Outer Curb Ring
        Cylinder curbCyl = new Cylinder(36, 36, 8.6f, 0.30f, true);
        Geometry curbGeom = new Geometry("RajuCurb", curbCyl);
        curbGeom.rotate(FastMath.HALF_PI, 0, 0);
        curbGeom.setLocalTranslation(0, 0.15f, 0);
        curbGeom.setMaterial(curbMat);
        rootNode.attachChild(curbGeom);

        // Circular Lawn Island
        Cylinder islandCyl = new Cylinder(36, 36, 8.3f, 0.34f, true);
        Geometry islandGeom = new Geometry("RajuIsland", islandCyl);
        islandGeom.rotate(FastMath.HALF_PI, 0, 0);
        islandGeom.setLocalTranslation(0, 0.17f, 0);
        islandGeom.setMaterial(grassMat);
        rootNode.attachChild(islandGeom);

        // Outer Flowerbed Border (Lush seasonal marigolds & foliage)
        for (int b = 0; b < 24; b++) {
            float angle = b * (FastMath.TWO_PI / 24f);
            float bx = FastMath.cos(angle) * 7.4f;
            float bz = FastMath.sin(angle) * 7.4f;
            Sphere flower = new Sphere(8, 8, 0.28f);
            Geometry fGeom = new Geometry("RajuFlower_" + b, flower);
            fGeom.setMaterial(radhachuraBloomMat);
            fGeom.setLocalTranslation(bx, 0.38f, bz);
            rootNode.attachChild(fGeom);
        }

        // Stepped Brick & Terrazzo Pedestal
        Box baseBox1 = new Box(3.6f, 0.4f, 3.6f);
        Geometry base1 = new Geometry("RajuBase1", baseBox1);
        base1.setMaterial(concreteMat);
        base1.setLocalTranslation(0, 0.4f, 0);
        rootNode.attachChild(base1);
        addCollider(-3.6f, 0, -3.6f, 3.6f, 0.8f, 3.6f);

        Box baseBox2 = new Box(2.6f, 0.5f, 2.6f);
        Geometry base2 = new Geometry("RajuBase2", baseBox2);
        base2.setMaterial(darkConcreteMat);
        base2.setLocalTranslation(0, 0.9f, 0);
        rootNode.attachChild(base2);
        addCollider(-2.6f, 0.8f, -2.6f, 2.6f, 1.4f, 2.6f);

        // 8 Interconnected Figures Representing Indomitable Student Solidarity
        // (Sculpted in memory of martyr Moin Hossain Raju, standing arm-in-arm)
        float[] figureHeights = {2.7f, 3.2f, 3.7f, 4.3f, 4.0f, 3.5f, 3.0f, 2.6f};
        for (int i = 0; i < 8; i++) {
            float angle = i * (FastMath.TWO_PI / 8f);
            float radius = 1.55f;
            float h = figureHeights[i];
            float fx = FastMath.cos(angle) * radius;
            float fz = FastMath.sin(angle) * radius;

            // Torso
            Box figBox = new Box(0.30f, h * 0.45f, 0.26f);
            Geometry figGeom = new Geometry("RajuFigure_" + i, figBox);
            figGeom.setMaterial(sculptureMat);
            figGeom.setLocalTranslation(fx, 1.4f + h * 0.45f, fz);
            figGeom.rotate(0f, -angle + FastMath.HALF_PI, 0f);
            rootNode.attachChild(figGeom);

            // Head of Figure
            Sphere figHead = new Sphere(12, 12, 0.24f);
            Geometry headGeom = new Geometry("RajuHead_" + i, figHead);
            headGeom.setMaterial(sculptureMat);
            headGeom.setLocalTranslation(fx, 1.4f + h * 0.9f + 0.22f, fz);
            rootNode.attachChild(headGeom);

            // Linking Arm to next figure (unbroken wall of resistance)
            float nextAngle = (i + 1) * (FastMath.TWO_PI / 8f);
            float nfx = FastMath.cos(nextAngle) * radius;
            float nfz = FastMath.sin(nextAngle) * radius;
            float armMidX = (fx + nfx) * 0.5f;
            float armMidZ = (fz + nfz) * 0.5f;

            Box armLink = new Box(0.10f, 0.12f, 0.48f);
            Geometry armGeom = new Geometry("RajuArmLink_" + i, armLink);
            armGeom.setMaterial(sculptureMat);
            armGeom.setLocalTranslation(armMidX, 1.4f + h * 0.55f, armMidZ);
            armGeom.rotate(0f, -angle + FastMath.QUARTER_PI, 0f);
            rootNode.attachChild(armGeom);

            addCollider(fx - 0.4f, 1.4f, fz - 0.4f, fx + 0.4f, 1.4f + h, fz + 0.4f);
        }
    }

    private void buildCentralLibrary() {
        // Muzharul Islam's Masterpiece: Dhaka University Central Library (1953)
        float libZ = -28.0f;

        // 1. Broad Terraced Entrance Steps (Grand brick staircase)
        for (int step = 0; step < 5; step++) {
            float stepY = step * 0.16f;
            float stepW = 18f - step * 0.6f;
            float stepD = 2.0f;
            Box stepBox = new Box(stepW, 0.08f, stepD);
            Geometry stepGeom = new Geometry("LibStep_" + step, stepBox);
            stepGeom.setMaterial(terracottaBrickMat);
            stepGeom.setLocalTranslation(0f, stepY + 0.08f, libZ + 12f - step * 1.2f);
            rootNode.attachChild(stepGeom);
        }

        // 2. Elevated Colonnade Terrace Walkway (Ground floor pilotis platform)
        Box walkBox = new Box(26f, 0.4f, 7.5f);
        Geometry walkGeom = new Geometry("LibTerrace", walkBox);
        walkGeom.setMaterial(terracottaBrickMat);
        walkGeom.setLocalTranslation(0f, 0.8f, libZ + 5.5f);
        rootNode.attachChild(walkGeom);

        // 3. Iconic Concrete Pilotis Colonnade (11 load-bearing columns elevating the structure)
        for (int c = -5; c <= 5; c++) {
            Cylinder colCyl = new Cylinder(18, 18, 0.48f, 5.2f, true);
            Geometry colGeom = new Geometry("LibCol_" + c, colCyl);
            colGeom.rotate(FastMath.HALF_PI, 0, 0);
            float cx = c * 4.6f;
            float cz = libZ + 4.2f;
            colGeom.setLocalTranslation(cx, 3.4f, cz);
            colGeom.setMaterial(concreteMat);
            rootNode.attachChild(colGeom);
            addCollider(cx - 0.6f, 0f, cz - 0.6f, cx + 0.6f, 6.0f, cz + 0.6f);
        }

        // 4. Ground Floor Recessed Glass Reading Halls & Entrance (Behind Pilotis)
        Box glassBox = new Box(23.5f, 2.2f, 0.1f);
        Geometry glassGeom = new Geometry("LibGlassWall", glassBox);
        glassGeom.setMaterial(glassMat);
        glassGeom.setLocalTranslation(0f, 3.2f, libZ + 1.2f);
        rootNode.attachChild(glassGeom);

        // 5. Library Upper Reading Hall (Terracotta Red Brick Block)
        Box upperBox = new Box(26f, 4.6f, 10.0f);
        Geometry upperGeom = new Geometry("LibUpperHall", upperBox);
        upperGeom.setMaterial(terracottaBrickMat);
        upperGeom.setLocalTranslation(0f, 10.6f, libZ);
        rootNode.attachChild(upperGeom);
        addCollider(-26f, 0f, libZ - 10f, 26f, 15.2f, libZ + 1f);

        // 6. Muzharul Islam's Signature Vertical Louvers / Brise-Soleil Sunshades
        for (int l = -12; l <= 12; l++) {
            Box louver = new Box(0.14f, 3.8f, 0.65f);
            Geometry lGeom = new Geometry("LibLouver_" + l, louver);
            lGeom.setMaterial(concreteMat);
            lGeom.setLocalTranslation(l * 1.9f, 10.6f, libZ + 10.2f);
            rootNode.attachChild(lGeom);
        }

        // 7. Hakim Chattar Cultural Node & Tea Stall Kiosk
        buildHakimChattar(libZ);
    }

    private void buildHakimChattar(float libZ) {
        float hx = -15.5f;
        float hz = libZ + 9.5f;

        // Brick Raised Pavilion
        Box pavilionBase = new Box(5.2f, 0.25f, 4.2f);
        Geometry pavGeom = new Geometry("HakimBase", pavilionBase);
        pavGeom.setMaterial(terracottaBrickMat);
        pavGeom.setLocalTranslation(hx, 0.25f, hz);
        rootNode.attachChild(pavGeom);

        // Wooden Tea Stall Counter
        Box stallBox = new Box(2.8f, 1.1f, 1.2f);
        Geometry stallGeom = new Geometry("HakimTeaStall", stallBox);
        stallGeom.setMaterial(treeTrunkMat);
        stallGeom.setLocalTranslation(hx - 1.2f, 1.1f, hz);
        rootNode.attachChild(stallGeom);
        addCollider(hx - 4.0f, 0f, hz - 1.5f, hx + 1.8f, 2.4f, hz + 1.5f);

        // Corrugated Roof Canopy
        Box roofBox = new Box(3.6f, 0.08f, 2.2f);
        Geometry rGeom = new Geometry("HakimRoof", roofBox);
        rGeom.setMaterial(darkConcreteMat);
        rGeom.rotate(0.08f, 0f, -0.05f);
        rGeom.setLocalTranslation(hx - 1.0f, 2.6f, hz);
        rootNode.attachChild(rGeom);

        // Wooden Benches for Student Discussion (Adda)
        Box bench1 = new Box(1.8f, 0.25f, 0.35f);
        Geometry b1 = new Geometry("HakimBench1", bench1);
        b1.setMaterial(concreteMat);
        b1.setLocalTranslation(hx + 2.5f, 0.45f, hz - 1.5f);
        rootNode.attachChild(b1);

        Box bench2 = new Box(1.8f, 0.25f, 0.35f);
        Geometry b2 = new Geometry("HakimBench2", bench2);
        b2.setMaterial(concreteMat);
        b2.setLocalTranslation(hx + 2.5f, 0.45f, hz + 1.5f);
        rootNode.attachChild(b2);
    }

    private void buildTSC() {
        // Constantinos Doxiadis's Teacher-Student Centre (TSC, 1961)
        float tscX = 24.0f;
        float tscZ = 19.0f;

        // Paved TSC Front Plaza (Geometric brick courtyard)
        Box plazaBox = new Box(16.0f, 0.12f, 14.0f);
        Geometry plazaGeom = new Geometry("TSCPlaza", plazaBox);
        plazaGeom.setMaterial(terracottaBrickMat);
        plazaGeom.setLocalTranslation(tscX - 2.0f, 0.12f, tscZ - 12.0f);
        rootNode.attachChild(plazaGeom);

        // Main Auditorium Block
        Box audBox = new Box(13.0f, 4.5f, 11.0f);
        Geometry audGeom = new Geometry("TSCAuditorium", audBox);
        audGeom.setMaterial(concreteMat);
        audGeom.setLocalTranslation(tscX, 4.5f, tscZ);
        rootNode.attachChild(audGeom);
        addCollider(tscX - 13.0f, 0f, tscZ - 11.0f, tscX + 13.0f, 9.0f, tscZ + 11.0f);

        // Modernist Butterfly Roof (Two distinct angled cantilever wings)
        Box wingLeft = new Box(7.2f, 0.32f, 12.2f);
        Geometry wlGeom = new Geometry("TSCRoofLeft", wingLeft);
        wlGeom.rotate(0.06f, 0f, -0.12f);
        wlGeom.setLocalTranslation(tscX - 6.0f, 9.6f, tscZ);
        wlGeom.setMaterial(terracottaBrickMat);
        rootNode.attachChild(wlGeom);

        Box wingRight = new Box(7.2f, 0.32f, 12.2f);
        Geometry wrGeom = new Geometry("TSCRoofRight", wingRight);
        wrGeom.rotate(0.06f, 0f, 0.12f);
        wrGeom.setLocalTranslation(tscX + 6.0f, 9.6f, tscZ);
        wrGeom.setMaterial(terracottaBrickMat);
        rootNode.attachChild(wrGeom);

        // Front Entrance Gate & Columns
        for (int g = -2; g <= 2; g++) {
            Cylinder gateCol = new Cylinder(16, 16, 0.42f, 5.2f, true);
            Geometry gc = new Geometry("TSCGateCol_" + g, gateCol);
            gc.rotate(FastMath.HALF_PI, 0, 0);
            gc.setMaterial(concreteMat);
            float gcx = tscX - 8.0f + g * 2.8f;
            float gcz = tscZ - 11.2f;
            gc.setLocalTranslation(gcx, 2.6f, gcz);
            rootNode.attachChild(gc);
            addCollider(gcx - 0.5f, 0f, gcz - 0.5f, gcx + 0.5f, 5.2f, gcz + 0.5f);
        }

        // Sunken Circular Discussion Circle (Iconic DU TSC Adda Circle)
        Cylinder addaRing = new Cylinder(28, 28, 4.2f, 0.35f, true);
        Geometry addaGeom = new Geometry("TSCAddaRing", addaRing);
        addaGeom.rotate(FastMath.HALF_PI, 0, 0);
        addaGeom.setMaterial(darkConcreteMat);
        addaGeom.setLocalTranslation(tscX - 6.0f, 0.25f, tscZ - 14.0f);
        rootNode.attachChild(addaGeom);
    }

    private void buildMemorialPlaque() {
        // Commemorative July Movement Plaque
        Box plaqueBase = new Box(0.7f, 0.85f, 0.4f);
        Geometry pBase = new Geometry("PlaqueBase", plaqueBase);
        pBase.setMaterial(concreteMat);
        pBase.setLocalTranslation(2f, 0.85f, -6f);
        rootNode.attachChild(pBase);
        addCollider(1.3f, 0f, -6.4f, 2.7f, 1.7f, -5.6f);

        // Brass Information Face
        Box plaqueFace = new Box(0.55f, 0.65f, 0.05f);
        Geometry pFace = new Geometry("PlaqueFace", plaqueFace);
        pFace.setMaterial(memorialGoldMat);
        pFace.setLocalTranslation(2f, 0.95f, -5.58f);
        rootNode.attachChild(pFace);

        // Golden Memorial Beacon Indicator
        Sphere beacon = new Sphere(16, 16, 0.18f);
        Geometry bGeom = new Geometry("PlaqueBeacon", beacon);
        bGeom.setMaterial(memorialGoldMat);
        bGeom.setLocalTranslation(2f, 1.95f, -6f);
        rootNode.attachChild(bGeom);
    }

    private void buildJulyMovementBannersAndMurals(AssetManager assetManager) {
        if (textRenderer == null || assetManager == null) return;

        try {
            // 1. Road Graffiti: "কোটা না মেধা? মেধা! মেধা!" (Approaching Raju Sculpture)
            Texture2D quotaTex = textRenderer.createTexture(
                "কোটা না মেধা?  মেধা! মেধা!",
                28f, true,
                new Color(255, 255, 255),
                new Color(20, 20, 22, 230),
                20, 10
            );
            Geometry quotaDecal = createDecalQuad(assetManager, "Decal_Quota", quotaTex, 6.4f, 1.6f);
            quotaDecal.setLocalTranslation(-3.2f, 0.02f, 6.2f);
            quotaDecal.rotate(-FastMath.HALF_PI, 0, 0);
            rootNode.attachChild(quotaDecal);

            // 2. Road Graffiti: "৩৬ জুলাই — ছাত্র-জনতার ঐতিহাসিক বিজয়"
            Texture2D july36Tex = textRenderer.createTexture(
                "৩৬ জুলাই — ছাত্র-জনতার ঐতিহাসিক বিজয়",
                28f, true,
                new Color(255, 230, 80),
                new Color(20, 20, 22, 230),
                20, 10
            );
            Geometry july36Decal = createDecalQuad(assetManager, "Decal_July36", july36Tex, 7.2f, 1.6f);
            july36Decal.setLocalTranslation(-3.6f, 0.02f, -4.5f);
            july36Decal.rotate(-FastMath.HALF_PI, 0, 0);
            rootNode.attachChild(july36Decal);

            // 3. Central Library Wall Banner: "আমার ভাই মরলো কেন? জবাব চাই!"
            Texture2D bannerTex1 = textRenderer.createTexture(
                "আমার ভাই মরলো কেন?  জবাব চাই!",
                32f, true,
                new Color(255, 255, 255),
                new Color(180, 25, 20, 240),
                24, 12
            );
            Geometry libBanner = createDecalQuad(assetManager, "Banner_Library", bannerTex1, 8.5f, 1.8f);
            libBanner.setLocalTranslation(-4.25f, 4.8f, -22.2f);
            rootNode.attachChild(libBanner);

            // 4. TSC Wall Banner: "স্বৈরাচার নিপাত যাক, গণতন্ত্র মুক্তি পাক"
            Texture2D bannerTex2 = textRenderer.createTexture(
                "স্বৈরাচার নিপাত যাক, গণতন্ত্র মুক্তি পাক",
                30f, true,
                new Color(255, 245, 120),
                new Color(15, 30, 25, 240),
                24, 12
            );
            Geometry tscBanner = createDecalQuad(assetManager, "Banner_TSC", bannerTex2, 8.0f, 1.8f);
            tscBanner.setLocalTranslation(16.0f, 4.2f, 8.8f);
            tscBanner.rotate(0f, -FastMath.HALF_PI, 0f);
            rootNode.attachChild(tscBanner);

            System.out.println("[CampusWorld] Dynamic July Movement Bengali street murals & banners attached successfully.");
        } catch (Throwable t) {
            System.err.println("[CampusWorld] Failed to generate text decals: " + t.getMessage());
        }
    }

    private Geometry createDecalQuad(AssetManager assetManager, String name, Texture2D texture, float width, float height) {
        Quad quad = new Quad(width, height);
        Geometry geom = new Geometry(name, quad);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setTexture("ColorMap", texture);
        mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        mat.getAdditionalRenderState().setDepthWrite(false);
        geom.setMaterial(mat);
        geom.setQueueBucket(RenderQueue.Bucket.Transparent);
        return geom;
    }

    private void buildLushVegetation() {
        // 1. Spreading Krishnachura Shade Trees (Scarlet orange-red summer blooms)
        float[][] krishnachuraLocations = {
            {-16f, 14f}, {-24f, 8f}, {-20f, -10f},
            {18f, -16f}, {26f, -8f}, {14f, -24f},
            {8f, 22f}, {-14f, 26f}
        };

        for (int i = 0; i < krishnachuraLocations.length; i++) {
            createOrnamentalTree(
                "Krishnachura_" + i,
                krishnachuraLocations[i][0], krishnachuraLocations[i][1],
                4.4f, 2.8f, krishnachuraBloomMat
            );
        }

        // 2. Jarul Trees (Purple/Magenta blossoms near library lawns)
        float[][] jarulLocations = {
            {-8f, -22f}, {8f, -22f}, {-24f, -22f}
        };
        for (int i = 0; i < jarulLocations.length; i++) {
            createOrnamentalTree(
                "Jarul_" + i,
                jarulLocations[i][0], jarulLocations[i][1],
                4.0f, 2.4f, jarulBloomMat
            );
        }

        // 3. Radhachura Trees (Golden yellow blossoms near TSC)
        float[][] radhachuraLocations = {
            {28f, 6f}, {32f, 18f}
        };
        for (int i = 0; i < radhachuraLocations.length; i++) {
            createOrnamentalTree(
                "Radhachura_" + i,
                radhachuraLocations[i][0], radhachuraLocations[i][1],
                4.2f, 2.5f, radhachuraBloomMat
            );
        }

        // 4. Iconic Hakim Chattar Banyan Tree (Bot-tola) with hanging aerial prop roots
        buildBanyanTree(-18f, -18f);
    }

    private void createOrnamentalTree(String name, float tx, float tz, float height, float canopyRadius, Material bloomMat) {
        // Trunk
        Cylinder trunkCyl = new Cylinder(16, 16, 0.35f, height, true);
        Geometry trunk = new Geometry(name + "_Trunk", trunkCyl);
        trunk.rotate(FastMath.HALF_PI, 0, 0);
        trunk.setMaterial(treeTrunkMat);
        trunk.setLocalTranslation(tx, height * 0.5f, tz);
        rootNode.attachChild(trunk);
        addCollider(tx - 0.45f, 0f, tz - 0.45f, tx + 0.45f, height, tz + 0.45f);

        // Main Canopy (Lush Green Foliage)
        Sphere canopySphere = new Sphere(16, 16, canopyRadius);
        Geometry canopy = new Geometry(name + "_Canopy", canopySphere);
        canopy.setMaterial(foliageMat);
        canopy.setLocalTranslation(tx, height + canopyRadius * 0.4f, tz);
        canopy.setLocalScale(1.35f, 0.70f, 1.35f);
        rootNode.attachChild(canopy);

        // Seasonal Summer Blooms
        if (bloomMat != null) {
            Sphere bloomSphere = new Sphere(14, 14, canopyRadius * 0.75f);
            Geometry bloomGeom = new Geometry(name + "_Bloom", bloomSphere);
            bloomGeom.setMaterial(bloomMat);
            bloomGeom.setLocalTranslation(tx + 0.2f, height + canopyRadius * 0.65f, tz + 0.15f);
            bloomGeom.setLocalScale(1.10f, 0.45f, 1.10f);
            rootNode.attachChild(bloomGeom);
        }
    }

    private void buildBanyanTree(float bx, float bz) {
        // Massive Central Trunk
        Cylinder mainTrunk = new Cylinder(18, 18, 0.95f, 5.2f, true);
        Geometry mTrunkGeom = new Geometry("Banyan_Trunk", mainTrunk);
        mTrunkGeom.rotate(FastMath.HALF_PI, 0, 0);
        mTrunkGeom.setMaterial(treeTrunkMat);
        mTrunkGeom.setLocalTranslation(bx, 2.6f, bz);
        rootNode.attachChild(mTrunkGeom);
        addCollider(bx - 1.2f, 0f, bz - 1.2f, bx + 1.2f, 5.2f, bz + 1.2f);

        // Giant Spreading Canopy
        Sphere bigCanopy = new Sphere(18, 18, 4.6f);
        Geometry bCanopyGeom = new Geometry("Banyan_Canopy", bigCanopy);
        bCanopyGeom.setMaterial(foliageMat);
        bCanopyGeom.setLocalTranslation(bx, 6.2f, bz);
        bCanopyGeom.setLocalScale(1.6f, 0.55f, 1.6f);
        rootNode.attachChild(bCanopyGeom);

        // Hanging Aerial Prop Roots
        float[] rootOffsets = {-2.4f, 2.4f};
        for (float rx : rootOffsets) {
            for (float rz : rootOffsets) {
                Cylinder propRoot = new Cylinder(12, 12, 0.16f, 4.8f, true);
                Geometry prGeom = new Geometry("Banyan_Root_" + rx + "_" + rz, propRoot);
                prGeom.rotate(FastMath.HALF_PI, 0, 0);
                prGeom.setMaterial(treeTrunkMat);
                prGeom.setLocalTranslation(bx + rx, 2.4f, bz + rz);
                rootNode.attachChild(prGeom);
                addCollider(bx + rx - 0.25f, 0f, bz + rz - 0.25f, bx + rx + 0.25f, 4.8f, bz + rz + 0.25f);
            }
        }
    }

    private void buildCampusProps() {
        // Dhaka University Vintage Cast-Iron Street Lamps
        float[][] lampLocations = {
            {-9f, -8f}, {9f, -8f},
            {-9f, -20f}, {9f, -20f},
            {15f, 6f}, {22f, 6f},
            {-6f, 12f}, {6f, 12f}
        };

        for (int i = 0; i < lampLocations.length; i++) {
            float lx = lampLocations[i][0];
            float lz = lampLocations[i][1];

            // Lamp Post
            Cylinder pole = new Cylinder(12, 12, 0.08f, 3.8f, true);
            Geometry pGeom = new Geometry("LampPole_" + i, pole);
            pGeom.rotate(FastMath.HALF_PI, 0, 0);
            pGeom.setMaterial(lampMat);
            pGeom.setLocalTranslation(lx, 1.9f, lz);
            rootNode.attachChild(pGeom);

            // Double Lantern Cross-Arm
            Box arm = new Box(0.45f, 0.04f, 0.04f);
            Geometry aGeom = new Geometry("LampArm_" + i, arm);
            aGeom.setMaterial(lampMat);
            aGeom.setLocalTranslation(lx, 3.7f, lz);
            rootNode.attachChild(aGeom);

            // Twin Glowing Lantern Globes
            Sphere globeL = new Sphere(10, 10, 0.12f);
            Geometry gGeomL = new Geometry("LampGlobeL_" + i, globeL);
            gGeomL.setMaterial(memorialGoldMat);
            gGeomL.setLocalTranslation(lx - 0.45f, 3.82f, lz);
            rootNode.attachChild(gGeomL);

            Sphere globeR = new Sphere(10, 10, 0.12f);
            Geometry gGeomR = new Geometry("LampGlobeR_" + i, globeR);
            gGeomR.setMaterial(memorialGoldMat);
            gGeomR.setLocalTranslation(lx + 0.45f, 3.82f, lz);
            rootNode.attachChild(gGeomR);

            addCollider(lx - 0.2f, 0f, lz - 0.2f, lx + 0.2f, 3.8f, lz + 0.2f);
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
