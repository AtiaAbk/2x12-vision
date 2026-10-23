package bd.july2024.world;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import net.mgsx.gltf.scene3d.attributes.PBRColorAttribute;
import net.mgsx.gltf.scene3d.attributes.PBRFloatAttribute;
import net.mgsx.gltf.scene3d.attributes.PBRTextureAttribute;

/**
 * Authentic 3D Dhaka University campus environment for Project July 2024.
 * Reconstructs the central vertical slice: Raju Memorial Sculpture, Central Library & Hakim Chattar, and TSC.
 */
public class CampusWorld implements Disposable {

    private final Array<Model> models = new Array<>();
    private final Array<ModelInstance> instances = new Array<>();
    private final java.util.List<float[]> colliders = new java.util.ArrayList<>();
    private final Environment environment = new Environment();

    public CampusWorld(TextureFactory textures) {
        setupLighting();
        buildCampus(textures);
    }

    private void setupLighting() {
        // Natural Bangladesh summer daylight with warm direct sun and soft atmospheric fill
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.45f, 0.47f, 0.52f, 1f));
        DirectionalLight sun = new DirectionalLight();
        sun.set(new Color(1.0f, 0.94f, 0.86f, 1f), new Vector3(-0.45f, -0.75f, -0.48f).nor());
        environment.add(sun);
    }

    private void buildCampus(TextureFactory textures) {
        ModelBuilder mb = new ModelBuilder();
        long attr = Usage.Position | Usage.Normal;

        // PBR Materials
        Material groundMat = new Material(
            PBRTextureAttribute.createBaseColorTexture(textures.concretePbr),
            ColorAttribute.createDiffuse(new Color(0.82f, 0.82f, 0.80f, 1f)),
            PBRFloatAttribute.createMetallic(0.0f),
            PBRFloatAttribute.createRoughness(0.85f)
        );

        Material asphaltMat = new Material(
            PBRTextureAttribute.createBaseColorTexture(textures.asphaltRoad),
            ColorAttribute.createDiffuse(new Color(0.28f, 0.28f, 0.30f, 1f)),
            PBRFloatAttribute.createMetallic(0.0f),
            PBRFloatAttribute.createRoughness(0.88f)
        );

        Material curbMat = new Material(
            PBRTextureAttribute.createBaseColorTexture(textures.weatheredStone),
            ColorAttribute.createDiffuse(new Color(0.70f, 0.68f, 0.66f, 1f)),
            PBRFloatAttribute.createMetallic(0.0f),
            PBRFloatAttribute.createRoughness(0.82f)
        );

        Material rajuConcreteMat = new Material(
            PBRTextureAttribute.createBaseColorTexture(textures.concretePbr),
            ColorAttribute.createDiffuse(new Color(0.88f, 0.87f, 0.84f, 1f)),
            PBRFloatAttribute.createMetallic(0.0f),
            PBRFloatAttribute.createRoughness(0.78f)
        );

        Material brickMat = new Material(
            PBRTextureAttribute.createBaseColorTexture(textures.brickTerracotta),
            ColorAttribute.createDiffuse(new Color(0.72f, 0.30f, 0.24f, 1f)),
            PBRFloatAttribute.createMetallic(0.0f),
            PBRFloatAttribute.createRoughness(0.80f)
        );

        Material treeTrunkMat = new Material(
            ColorAttribute.createDiffuse(new Color(0.28f, 0.20f, 0.15f, 1f)),
            PBRColorAttribute.createBaseColorFactor(new Color(0.28f, 0.20f, 0.15f, 1f)),
            PBRFloatAttribute.createMetallic(0.0f),
            PBRFloatAttribute.createRoughness(0.90f)
        );

        Material foliageMat = new Material(
            PBRTextureAttribute.createBaseColorTexture(textures.foliageLeaf),
            ColorAttribute.createDiffuse(new Color(0.22f, 0.55f, 0.25f, 1f)),
            PBRFloatAttribute.createMetallic(0.0f),
            PBRFloatAttribute.createRoughness(0.75f)
        );

        // 1. Base Campus Ground Plane (260m x 260m)
        Model groundModel = mb.createBox(260f, 0.1f, 260f, groundMat, attr);
        models.add(groundModel);
        ModelInstance groundInst = new ModelInstance(groundModel);
        groundInst.transform.setTranslation(0f, -0.05f, 0f);
        instances.add(groundInst);

        // 2. Main Connecting Avenue along Z = 30.0m (from X = -75m to X = +75m, width 8m)
        Model roadModel = mb.createBox(150f, 0.08f, 8.0f, asphaltMat, attr);
        models.add(roadModel);
        ModelInstance roadInst = new ModelInstance(roadModel);
        roadInst.transform.setTranslation(0f, 0.04f, 30.0f);
        instances.add(roadInst);

        // North-South Avenue branch to Raju Sculpture (X = 44.0m, Z = 30m to 42m)
        Model branchModel = mb.createBox(8.0f, 0.08f, 16.0f, asphaltMat, attr);
        models.add(branchModel);
        ModelInstance branchInst = new ModelInstance(branchModel);
        branchInst.transform.setTranslation(44.0f, 0.04f, 36.0f);
        instances.add(branchInst);

        // ==========================================
        // 3. ANTI-TERRORISM RAJU MEMORIAL SCULPTURE (X = 44.0, Z = 42.0)
        // ==========================================
        float rx = 44.0f, rz = 42.0f;
        Model rRoundabout = mb.createCylinder(16.0f, 0.35f, 16.0f, 24, curbMat, attr);
        Model rStep1 = mb.createBox(5.2f, 0.40f, 5.2f, curbMat, attr);
        Model rStep2 = mb.createBox(3.8f, 0.60f, 3.8f, rajuConcreteMat, attr);
        Model rSpire = mb.createBox(0.90f, 4.8f, 0.90f, rajuConcreteMat, attr);
        Model rFigure = mb.createBox(0.55f, 1.9f, 0.45f, rajuConcreteMat, attr);

        models.addAll(rRoundabout, rStep1, rStep2, rSpire, rFigure);

        ModelInstance rrbInst = new ModelInstance(rRoundabout);
        rrbInst.transform.setTranslation(rx, 0.17f, rz);
        instances.add(rrbInst);

        ModelInstance rs1Inst = new ModelInstance(rStep1);
        rs1Inst.transform.setTranslation(rx, 0.55f, rz);
        instances.add(rs1Inst);

        ModelInstance rs2Inst = new ModelInstance(rStep2);
        rs2Inst.transform.setTranslation(rx, 1.05f, rz);
        instances.add(rs2Inst);

        ModelInstance rspInst = new ModelInstance(rSpire);
        rspInst.transform.setTranslation(rx, 3.75f, rz);
        instances.add(rspInst);
        addCollider(rx - 0.5f, 0f, rz - 0.5f, rx + 0.5f, 6.2f, rz + 0.5f);

        // 8 Interlocking student figures in circular formation
        for (int i = 0; i < 8; i++) {
            float ang = (float) i / 8f * MathUtils.PI2;
            float fx = rx + 1.25f * MathUtils.cos(ang);
            float fz = rz + 1.25f * MathUtils.sin(ang);
            ModelInstance fig = new ModelInstance(rFigure);
            fig.transform.setTranslation(fx, 2.3f, fz);
            fig.transform.rotate(Vector3.Y, -ang * MathUtils.radiansToDegrees + 90f);
            instances.add(fig);
        }

        // ==========================================
        // 4. DHAKA UNIVERSITY CENTRAL LIBRARY (X = -58.0, Z = 36.0)
        // ==========================================
        float lx = -58.0f, lz = 36.0f;
        // Upper cantilevered modernist library mass
        Model libUpper = mb.createBox(36.0f, 9.0f, 22.0f, brickMat, attr);
        Model libRoof = mb.createBox(38.0f, 0.6f, 24.0f, curbMat, attr);
        Model libPillar = mb.createCylinder(0.9f, 4.2f, 0.9f, 10, rajuConcreteMat, attr);
        Model libSteps = mb.createBox(14.0f, 0.4f, 4.0f, curbMat, attr);

        models.addAll(libUpper, libRoof, libPillar, libSteps);

        ModelInstance luInst = new ModelInstance(libUpper);
        luInst.transform.setTranslation(lx, 8.7f, lz);
        instances.add(luInst);
        addCollider(lx - 18f, 4.2f, lz - 11f, lx + 18f, 14f, lz + 11f);

        ModelInstance lrInst = new ModelInstance(libRoof);
        lrInst.transform.setTranslation(lx, 13.5f, lz);
        instances.add(lrInst);

        // Open pilotis colonnade (Muzharul Islam architecture)
        float[] px = {-15f, -5f, 5f, 15f};
        float[] pz = {-8f, 8f};
        for (float colX : px) {
            for (float colZ : pz) {
                ModelInstance pInst = new ModelInstance(libPillar);
                pInst.transform.setTranslation(lx + colX, 2.1f, lz + colZ);
                instances.add(pInst);
                addCollider(lx + colX - 0.5f, 0f, lz + colZ - 0.5f, lx + colX + 0.5f, 4.2f, lz + colZ + 0.5f);
            }
        }

        // Steps at library forecourt
        ModelInstance stepInst = new ModelInstance(libSteps);
        stepInst.transform.setTranslation(lx + 4f, 0.2f, lz + 12f);
        instances.add(stepInst);

        // Hakim Chattar Pavilion (X = -58.0, Z = 54.0)
        float hx = -58.0f, hz = 54.0f;
        Model hakPlaza = mb.createCylinder(14.0f, 0.25f, 14.0f, 16, curbMat, attr);
        Model hakRoof = mb.createCone(10.0f, 3.2f, 10.0f, 8, brickMat, attr);
        models.addAll(hakPlaza, hakRoof);

        ModelInstance hpInst = new ModelInstance(hakPlaza);
        hpInst.transform.setTranslation(hx, 0.12f, hz);
        instances.add(hpInst);

        ModelInstance hrInst = new ModelInstance(hakRoof);
        hrInst.transform.setTranslation(hx, 5.2f, hz);
        instances.add(hrInst);

        // ==========================================
        // 5. TEACHER-STUDENT CENTRE (TSC) (X = 68.0, Z = 20.0)
        // ==========================================
        float tx = 68.0f, tz = 20.0f;
        Model tscHall = mb.createBox(32.0f, 9.5f, 26.0f, brickMat, attr);
        Model tscButterflyRoof = mb.createBox(34.0f, 0.8f, 28.0f, rajuConcreteMat, attr);
        Model tscTerrace = mb.createBox(16.0f, 0.4f, 12.0f, curbMat, attr);

        models.addAll(tscHall, tscButterflyRoof, tscTerrace);

        ModelInstance thInst = new ModelInstance(tscHall);
        thInst.transform.setTranslation(tx, 4.75f, tz);
        instances.add(thInst);
        addCollider(tx - 16f, 0f, tz - 13f, tx + 16f, 10f, tz + 13f);

        ModelInstance tbInst = new ModelInstance(tscButterflyRoof);
        tbInst.transform.setTranslation(tx, 9.9f, tz);
        instances.add(tbInst);

        // Cafeteria terrace entrance at (X = 46.0, Z = 20.0)
        ModelInstance ttInst = new ModelInstance(tscTerrace);
        ttInst.transform.setTranslation(46.0f, 0.2f, tz);
        instances.add(ttInst);

        // ==========================================
        // 6. NATIVE TROPICAL TREES ALONG AVENUE
        // ==========================================
        Model trunkModel = mb.createCylinder(0.45f, 4.2f, 0.45f, 8, treeTrunkMat, attr);
        Model canopyModel = mb.createSphere(5.2f, 4.2f, 5.2f, 10, 8, foliageMat, attr);
        models.addAll(trunkModel, canopyModel);

        float[] treeX = {-45f, -25f, -5f, 15f, 32f, 58f};
        for (float treePosX : treeX) {
            // North side trees
            addTree(treePosX, 36.5f, trunkModel, canopyModel);
            // South side trees
            addTree(treePosX, 23.5f, trunkModel, canopyModel);
        }
    }

    private void addTree(float x, float z, Model trunk, Model canopy) {
        ModelInstance tInst = new ModelInstance(trunk);
        tInst.transform.setTranslation(x, 2.1f, z);
        instances.add(tInst);

        ModelInstance cInst = new ModelInstance(canopy);
        cInst.transform.setTranslation(x, 5.5f, z);
        instances.add(cInst);

        addCollider(x - 0.3f, 0f, z - 0.3f, x + 0.3f, 4.5f, z + 0.3f);
    }

    private void addCollider(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
        colliders.add(new float[]{minX, minY, minZ, maxX, maxY, maxZ});
    }

    public void render(ModelBatch batch) {
        for (ModelInstance mi : instances) {
            batch.render(mi, environment);
        }
    }

    public java.util.List<float[]> getColliders() {
        return colliders;
    }

    public Environment getEnvironment() {
        return environment;
    }

    @Override
    public void dispose() {
        for (Model m : models) {
            m.dispose();
        }
        models.clear();
        instances.clear();
        colliders.clear();
    }
}
