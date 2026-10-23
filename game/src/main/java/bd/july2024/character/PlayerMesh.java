package bd.july2024.character;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Cylinder;
import com.jme3.scene.shape.Sphere;

/**
 * Articulated Procedural 3D Student Character Mesh for Arif and Companions.
 * Features realistic PBR materials, denim trousers, cotton shirt, DU student ID card badge,
 * and kinematic limb rotation for walking and running strides.
 */
public class PlayerMesh {

    private final Node rootNode;
    private Node headNode;
    private Node torsoNode;
    private Node leftArmNode;
    private Node rightArmNode;
    private Node leftLegNode;
    private Node rightLegNode;

    public PlayerMesh(AssetManager assetManager, ColorRGBA shirtColor, ColorRGBA pantsColor) {
        rootNode = new Node("StudentMeshRoot");
        buildArticulatedMesh(assetManager, shirtColor, pantsColor);
    }

    private void buildArticulatedMesh(AssetManager assetManager, ColorRGBA shirtColor, ColorRGBA pantsColor) {
        Material skinMat = createLitMaterial(assetManager, new ColorRGBA(0.85f, 0.65f, 0.48f, 1f), 0.1f);
        Material hairMat = createLitMaterial(assetManager, new ColorRGBA(0.08f, 0.07f, 0.06f, 1f), 0.2f);
        Material shirtMat = createLitMaterial(assetManager, shirtColor != null ? shirtColor : new ColorRGBA(0.18f, 0.42f, 0.68f, 1f), 0.05f);
        Material pantsMat = createLitMaterial(assetManager, pantsColor != null ? pantsColor : new ColorRGBA(0.12f, 0.18f, 0.28f, 1f), 0.04f);
        Material shoeMat = createLitMaterial(assetManager, new ColorRGBA(0.10f, 0.10f, 0.12f, 1f), 0.15f);
        Material badgeMat = createLitMaterial(assetManager, new ColorRGBA(0.88f, 0.22f, 0.20f, 1f), 0.4f);
        Material backpackMat = createLitMaterial(assetManager, new ColorRGBA(0.18f, 0.22f, 0.26f, 1f), 0.08f);
        Material headbandGreenMat = createLitMaterial(assetManager, new ColorRGBA(0.02f, 0.44f, 0.24f, 1f), 0.05f);
        Material headbandRedMat = createLitMaterial(assetManager, new ColorRGBA(0.88f, 0.18f, 0.16f, 1f), 0.05f);

        // 1. Torso
        torsoNode = new Node("Torso");
        torsoNode.setLocalTranslation(0f, 0.95f, 0f);
        Box torsoBox = new Box(0.22f, 0.32f, 0.14f);
        Geometry torsoGeom = new Geometry("TorsoGeom", torsoBox);
        torsoGeom.setMaterial(shirtMat);
        torsoGeom.setLocalTranslation(0f, 0.32f, 0f);
        torsoNode.attachChild(torsoGeom);

        // DU Student ID Card Badge (on chest / front)
        Box badgeBox = new Box(0.05f, 0.065f, 0.005f);
        Geometry badgeGeom = new Geometry("Badge", badgeBox);
        badgeGeom.setMaterial(badgeMat);
        badgeGeom.setLocalTranslation(0.10f, 0.38f, -0.145f);
        torsoNode.attachChild(badgeGeom);

        // Student Canvas Backpack (on back of torso)
        Box packBox = new Box(0.17f, 0.25f, 0.09f);
        Geometry packGeom = new Geometry("Backpack", packBox);
        packGeom.setMaterial(backpackMat);
        packGeom.setLocalTranslation(0f, 0.32f, 0.23f);
        torsoNode.attachChild(packGeom);

        // Backpack Top Flap & Zipper Pocket
        Box flapBox = new Box(0.16f, 0.05f, 0.095f);
        Geometry flapGeom = new Geometry("BackpackFlap", flapBox);
        flapGeom.setMaterial(backpackMat);
        flapGeom.setLocalTranslation(0f, 0.52f, 0.23f);
        torsoNode.attachChild(flapGeom);

        // Backpack Shoulder Straps
        Box strapL = new Box(0.025f, 0.24f, 0.005f);
        Geometry strapGeomL = new Geometry("StrapL", strapL);
        strapGeomL.setMaterial(backpackMat);
        strapGeomL.setLocalTranslation(-0.14f, 0.34f, -0.142f);
        torsoNode.attachChild(strapGeomL);

        Box strapR = new Box(0.025f, 0.24f, 0.005f);
        Geometry strapGeomR = new Geometry("StrapR", strapR);
        strapGeomR.setMaterial(backpackMat);
        strapGeomR.setLocalTranslation(0.14f, 0.34f, -0.142f);
        torsoNode.attachChild(strapGeomR);

        rootNode.attachChild(torsoNode);

        // 2. Head & Neck
        headNode = new Node("Head");
        headNode.setLocalTranslation(0f, 0.66f, 0f);

        Sphere headSphere = new Sphere(16, 16, 0.14f);
        Geometry headGeom = new Geometry("HeadGeom", headSphere);
        headGeom.setMaterial(skinMat);
        headGeom.setLocalTranslation(0f, 0.14f, 0f);
        headNode.attachChild(headGeom);

        // Hair Cap (towards back and crown of head)
        Sphere hairSphere = new Sphere(16, 16, 0.145f);
        Geometry hairGeom = new Geometry("HairGeom", hairSphere);
        hairGeom.setMaterial(hairMat);
        hairGeom.setLocalTranslation(0f, 0.17f, 0.02f);
        headNode.attachChild(hairGeom);

        // Bangladesh National Flag Headband (July 2024 Student Movement Symbol)
        Box headbandBox = new Box(0.146f, 0.025f, 0.146f);
        Geometry headbandGeom = new Geometry("Headband", headbandBox);
        headbandGeom.setMaterial(headbandGreenMat);
        headbandGeom.setLocalTranslation(0f, 0.155f, 0f);
        headNode.attachChild(headbandGeom);

        // Red Disc on Forehead Center of Headband
        Cylinder redDisc = new Cylinder(16, 16, 0.032f, 0.006f, true);
        Geometry redDiscGeom = new Geometry("HeadbandRedDisc", redDisc);
        redDiscGeom.setMaterial(headbandRedMat);
        redDiscGeom.setLocalTranslation(0f, 0.155f, -0.147f);
        headNode.attachChild(redDiscGeom);

        torsoNode.attachChild(headNode);

        // 3. Left & Right Arms
        leftArmNode = createArm(skinMat, shirtMat, true);
        leftArmNode.setLocalTranslation(-0.28f, 0.58f, 0f);
        torsoNode.attachChild(leftArmNode);

        rightArmNode = createArm(skinMat, shirtMat, false);
        rightArmNode.setLocalTranslation(0.28f, 0.58f, 0f);
        torsoNode.attachChild(rightArmNode);

        // 4. Left & Right Legs
        leftLegNode = createLeg(pantsMat, shoeMat);
        leftLegNode.setLocalTranslation(-0.12f, 0.95f, 0f);
        rootNode.attachChild(leftLegNode);

        rightLegNode = createLeg(pantsMat, shoeMat);
        rightLegNode.setLocalTranslation(0.12f, 0.95f, 0f);
        rootNode.attachChild(rightLegNode);
    }

    private Node createArm(Material skinMat, Material shirtMat, boolean isLeft) {
        Node arm = new Node("Arm");
        // Upper sleeve
        Box sleeveBox = new Box(0.065f, 0.18f, 0.07f);
        Geometry sleeve = new Geometry("Sleeve", sleeveBox);
        sleeve.setMaterial(shirtMat);
        sleeve.setLocalTranslation(0f, -0.18f, 0f);
        arm.attachChild(sleeve);

        // Forearm & Hand
        Box handBox = new Box(0.055f, 0.16f, 0.06f);
        Geometry hand = new Geometry("Hand", handBox);
        hand.setMaterial(skinMat);
        hand.setLocalTranslation(0f, -0.48f, 0f);
        arm.attachChild(hand);
        return arm;
    }

    private Node createLeg(Material pantsMat, Material shoeMat) {
        Node leg = new Node("Leg");
        // Trousers
        Box legBox = new Box(0.085f, 0.42f, 0.09f);
        Geometry legGeom = new Geometry("Trousers", legBox);
        legGeom.setMaterial(pantsMat);
        legGeom.setLocalTranslation(0f, -0.42f, 0f);
        leg.attachChild(legGeom);

        // Shoe
        Box shoeBox = new Box(0.09f, 0.055f, 0.13f);
        Geometry shoeGeom = new Geometry("Shoe", shoeBox);
        shoeGeom.setMaterial(shoeMat);
        shoeGeom.setLocalTranslation(0f, -0.89f, 0.03f);
        leg.attachChild(shoeGeom);
        return leg;
    }

    private Material createLitMaterial(AssetManager assetManager, ColorRGBA color, float shininess) {
        Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        mat.setBoolean("UseMaterialColors", true);
        mat.setColor("Diffuse", color);
        mat.setColor("Specular", ColorRGBA.White.mult(0.15f));
        mat.setFloat("Shininess", shininess);
        return mat;
    }

    public void animate(float speed, float stridePhase, float idleTime, boolean isCrouching) {
        // Natural idle breathing
        float breath = FastMath.sin(idleTime * 2.2f) * 0.015f;
        torsoNode.setLocalTranslation(0f, (isCrouching ? 0.65f : 0.95f) + breath, 0f);

        // Locomotion lean & subtle lateral spine roll
        float lean = isCrouching ? 0.22f : (speed > 5f ? 0.14f : (speed > 0.2f ? 0.06f : 0f));
        float roll = (speed > 0.2f) ? FastMath.sin(stridePhase) * 0.02f : 0f;
        torsoNode.setLocalRotation(new Quaternion().fromAngles(lean, 0f, roll));

        if (speed > 0.2f) {
            float swingMax = (speed > 5f) ? 0.78f : 0.46f;
            float legSwing = FastMath.sin(stridePhase) * swingMax;
            float armSwing = -legSwing * 0.88f;

            // Pitch rotation for legs and arms with natural slight outward arm splay
            leftLegNode.setLocalRotation(new Quaternion().fromAngleAxis(legSwing, Vector3f.UNIT_X));
            rightLegNode.setLocalRotation(new Quaternion().fromAngleAxis(-legSwing, Vector3f.UNIT_X));

            leftArmNode.setLocalRotation(new Quaternion().fromAngles(armSwing, 0f, -0.05f));
            rightArmNode.setLocalRotation(new Quaternion().fromAngles(-armSwing, 0f, 0.05f));
        } else {
            // Smoothly return to neutral idle stance
            leftLegNode.setLocalRotation(Quaternion.IDENTITY);
            rightLegNode.setLocalRotation(Quaternion.IDENTITY);
            leftArmNode.setLocalRotation(Quaternion.IDENTITY);
            rightArmNode.setLocalRotation(Quaternion.IDENTITY);
        }
    }

    public Node getRootNode() {
        return rootNode;
    }
}
