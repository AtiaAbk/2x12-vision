package bd.july2024.character;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import bd.july2024.world.TextureFactory;
import net.mgsx.gltf.scene3d.attributes.PBRColorAttribute;
import net.mgsx.gltf.scene3d.attributes.PBRFloatAttribute;
import net.mgsx.gltf.scene3d.attributes.PBRTextureAttribute;

/**
 * Procedural PBR character model for Project July 2024.
 * Renders protagonist Arif and student companions with articulated body parts,
 * authentic student attire (shirt/kurti, trousers, sandals/shoes), and DU ID badge.
 */
public class CharacterModel implements Disposable {

    private final Array<Model> models = new Array<>();

    // Body part meshes
    private final Model headMesh;
    private final Model hairMesh;
    private final Model torsoMesh;
    private final Model pelvisMesh;
    private final Model upperArmMesh;
    private final Model lowerArmMesh;
    private final Model handMesh;
    private final Model upperLegMesh;
    private final Model lowerLegMesh;
    private final Model footMesh;
    private final Model idBadgeMesh;

    // ModelInstances
    private final ModelInstance headInst;
    private final ModelInstance hairInst;
    private final ModelInstance torsoInst;
    private final ModelInstance pelvisInst;
    private final ModelInstance lArmInst, rArmInst;
    private final ModelInstance lForearmInst, rForearmInst;
    private final ModelInstance lHandInst, rHandInst;
    private final ModelInstance lThighInst, rThighInst;
    private final ModelInstance lShinInst, rShinInst;
    private final ModelInstance lFootInst, rFootInst;
    private final ModelInstance idBadgeInst;

    // Temporary math matrices for hierarchy transformation
    private final Matrix4 rootMatrix = new Matrix4();
    private final Matrix4 torsoMatrix = new Matrix4();
    private final Matrix4 headMatrix = new Matrix4();
    private final Matrix4 tempMatrix = new Matrix4();

    public CharacterModel(TextureFactory textures) {
        ModelBuilder mb = new ModelBuilder();
        long attr = Usage.Position | Usage.Normal;

        // PBR Materials
        Material skinMat = new Material(
            PBRTextureAttribute.createBaseColorTexture(textures.skinTone),
            ColorAttribute.createDiffuse(new Color(0.72f, 0.52f, 0.42f, 1f)),
            PBRFloatAttribute.createMetallic(0.0f),
            PBRFloatAttribute.createRoughness(0.65f)
        );

        Material hairMat = new Material(
            PBRTextureAttribute.createBaseColorTexture(textures.darkHair),
            ColorAttribute.createDiffuse(new Color(0.12f, 0.11f, 0.10f, 1f)),
            PBRFloatAttribute.createMetallic(0.05f),
            PBRFloatAttribute.createRoughness(0.85f)
        );

        Material shirtMat = new Material(
            PBRTextureAttribute.createBaseColorTexture(textures.studentCotton),
            ColorAttribute.createDiffuse(new Color(0.92f, 0.94f, 0.96f, 1f)),
            PBRFloatAttribute.createMetallic(0.0f),
            PBRFloatAttribute.createRoughness(0.78f)
        );

        Material trousersMat = new Material(
            PBRTextureAttribute.createBaseColorTexture(textures.denimTrousers),
            ColorAttribute.createDiffuse(new Color(0.18f, 0.22f, 0.32f, 1f)),
            PBRFloatAttribute.createMetallic(0.0f),
            PBRFloatAttribute.createRoughness(0.82f)
        );

        Material shoeMat = new Material(
            ColorAttribute.createDiffuse(new Color(0.15f, 0.12f, 0.10f, 1f)),
            PBRColorAttribute.createBaseColorFactor(new Color(0.15f, 0.12f, 0.10f, 1f)),
            PBRFloatAttribute.createMetallic(0.05f),
            PBRFloatAttribute.createRoughness(0.70f)
        );

        Material badgeMat = new Material(
            ColorAttribute.createDiffuse(new Color(0.85f, 0.20f, 0.18f, 1f)),
            PBRColorAttribute.createBaseColorFactor(new Color(0.85f, 0.20f, 0.18f, 1f)),
            PBRFloatAttribute.createMetallic(0.1f),
            PBRFloatAttribute.createRoughness(0.40f)
        );

        // Build geometric shapes with realistic human proportions (total height ~1.75m)
        headMesh = mb.createSphere(0.24f, 0.28f, 0.24f, 12, 10, skinMat, attr);
        hairMesh = mb.createSphere(0.26f, 0.18f, 0.26f, 12, 8, hairMat, attr);
        torsoMesh = mb.createBox(0.42f, 0.52f, 0.26f, shirtMat, attr);
        pelvisMesh = mb.createBox(0.38f, 0.18f, 0.24f, trousersMat, attr);

        upperArmMesh = mb.createCylinder(0.11f, 0.32f, 0.11f, 10, shirtMat, attr);
        lowerArmMesh = mb.createCylinder(0.09f, 0.30f, 0.09f, 10, skinMat, attr);
        handMesh = mb.createBox(0.08f, 0.12f, 0.08f, skinMat, attr);

        upperLegMesh = mb.createCylinder(0.15f, 0.44f, 0.15f, 10, trousersMat, attr);
        lowerLegMesh = mb.createCylinder(0.12f, 0.42f, 0.12f, 10, trousersMat, attr);
        footMesh = mb.createBox(0.12f, 0.08f, 0.24f, shoeMat, attr);

        idBadgeMesh = mb.createBox(0.09f, 0.12f, 0.01f, badgeMat, attr);

        models.addAll(headMesh, hairMesh, torsoMesh, pelvisMesh,
                      upperArmMesh, lowerArmMesh, handMesh,
                      upperLegMesh, lowerLegMesh, footMesh, idBadgeMesh);

        // Instantiate parts
        headInst = new ModelInstance(headMesh);
        hairInst = new ModelInstance(hairMesh);
        torsoInst = new ModelInstance(torsoMesh);
        pelvisInst = new ModelInstance(pelvisMesh);

        lArmInst = new ModelInstance(upperArmMesh);
        rArmInst = new ModelInstance(upperArmMesh);
        lForearmInst = new ModelInstance(lowerArmMesh);
        rForearmInst = new ModelInstance(lowerArmMesh);
        lHandInst = new ModelInstance(handMesh);
        rHandInst = new ModelInstance(handMesh);

        lThighInst = new ModelInstance(upperLegMesh);
        rThighInst = new ModelInstance(upperLegMesh);
        lShinInst = new ModelInstance(lowerLegMesh);
        rShinInst = new ModelInstance(lowerLegMesh);
        lFootInst = new ModelInstance(footMesh);
        rFootInst = new ModelInstance(footMesh);

        idBadgeInst = new ModelInstance(idBadgeMesh);
    }

    /**
     * Renders character in 3D world with hierarchical joint transforms.
     */
    public void render(ModelBatch batch, Environment env, Vector3 pos, float headingDeg, CharacterPose pose) {
        if (pos == null || pose == null) return;

        // Base root position and orientation
        rootMatrix.idt()
            .translate(pos.x, pos.y + pose.rootOffsetY, pos.z)
            .rotate(Vector3.Y, headingDeg);

        // 1. Pelvis
        pelvisInst.transform.set(rootMatrix).translate(0f, 0.88f, 0f);
        batch.render(pelvisInst, env);

        // 2. Torso (Spine Pitch & Yaw)
        torsoMatrix.set(rootMatrix)
            .translate(0f, 1.20f, 0f)
            .rotate(Vector3.X, pose.spinePitch)
            .rotate(Vector3.Y, pose.spineYaw);
        torsoInst.transform.set(torsoMatrix);
        batch.render(torsoInst, env);

        // ID Badge on chest
        idBadgeInst.transform.set(torsoMatrix).translate(0.10f, 0.08f, 0.14f);
        batch.render(idBadgeInst, env);

        // 3. Head & Hair
        headMatrix.set(torsoMatrix)
            .translate(0f, 0.40f, 0.02f)
            .rotate(Vector3.X, pose.headPitch)
            .rotate(Vector3.Y, pose.headYaw);
        headInst.transform.set(headMatrix);
        batch.render(headInst, env);

        hairInst.transform.set(headMatrix).translate(0f, 0.08f, -0.02f);
        batch.render(hairInst, env);

        // 4. Left Arm & Hand
        tempMatrix.set(torsoMatrix)
            .translate(-0.28f, 0.18f, 0f)
            .rotate(Vector3.X, pose.leftArmPitch);
        lArmInst.transform.set(tempMatrix).translate(0f, -0.16f, 0f);
        batch.render(lArmInst, env);

        tempMatrix.translate(0f, -0.16f, 0f).rotate(Vector3.X, pose.leftElbowPitch);
        lForearmInst.transform.set(tempMatrix).translate(0f, -0.15f, 0f);
        batch.render(lForearmInst, env);

        lHandInst.transform.set(tempMatrix).translate(0f, -0.32f, 0f);
        batch.render(lHandInst, env);

        // 5. Right Arm & Hand
        tempMatrix.set(torsoMatrix)
            .translate(0.28f, 0.18f, 0f)
            .rotate(Vector3.X, pose.rightArmPitch);
        rArmInst.transform.set(tempMatrix).translate(0f, -0.16f, 0f);
        batch.render(rArmInst, env);

        tempMatrix.translate(0f, -0.16f, 0f).rotate(Vector3.X, pose.rightElbowPitch);
        rForearmInst.transform.set(tempMatrix).translate(0f, -0.15f, 0f);
        batch.render(rForearmInst, env);

        rHandInst.transform.set(tempMatrix).translate(0f, -0.32f, 0f);
        batch.render(rHandInst, env);

        // 6. Left Leg & Foot
        tempMatrix.set(rootMatrix)
            .translate(-0.13f, 0.82f, 0f)
            .rotate(Vector3.X, pose.leftLegPitch);
        lThighInst.transform.set(tempMatrix).translate(0f, -0.22f, 0f);
        batch.render(lThighInst, env);

        tempMatrix.translate(0f, -0.22f, 0f).rotate(Vector3.X, pose.leftKneePitch);
        lShinInst.transform.set(tempMatrix).translate(0f, -0.21f, 0f);
        batch.render(lShinInst, env);

        lFootInst.transform.set(tempMatrix).translate(0f, -0.38f, 0.06f);
        batch.render(lFootInst, env);

        // 7. Right Leg & Foot
        tempMatrix.set(rootMatrix)
            .translate(0.13f, 0.82f, 0f)
            .rotate(Vector3.X, pose.rightLegPitch);
        rThighInst.transform.set(tempMatrix).translate(0f, -0.22f, 0f);
        batch.render(rThighInst, env);

        tempMatrix.translate(0f, -0.22f, 0f).rotate(Vector3.X, pose.rightKneePitch);
        rShinInst.transform.set(tempMatrix).translate(0f, -0.21f, 0f);
        batch.render(rShinInst, env);

        rFootInst.transform.set(tempMatrix).translate(0f, -0.38f, 0.06f);
        batch.render(rFootInst, env);
    }

    @Override
    public void dispose() {
        for (Model m : models) {
            m.dispose();
        }
        models.clear();
    }
}
