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
 * Articulated Procedural Humanoid Student Character Mesh for Arif and Companions.
 *
 * Implements refined anatomical student proportions:
 * - Tapered athletic torso (broad shoulders, tapered waist, leather belt)
 * - Defined neck cylinder connecting shirt collar to chin
 * - Ovaloid head with styled student hair, facial eyes, and Bangladesh flag protest headband
 * - Articulated arms with rolled-up sleeves, forearms, and hands
 * - Articulated legs with denim thighs, defined knee joints, calves, and campus sneakers
 * - Authentic canvas student backpack with zipper flap, side bottle, and shoulder straps
 * - Kinematic stride animation with velocity lean, lateral roll, and natural limb swings.
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
        Material skinMat = createLitMaterial(assetManager, new ColorRGBA(0.82f, 0.62f, 0.46f, 1f), 14f);
        Material hairMat = createLitMaterial(assetManager, new ColorRGBA(0.06f, 0.05f, 0.04f, 1f), 28f);
        Material eyeMat = createLitMaterial(assetManager, new ColorRGBA(0.08f, 0.07f, 0.06f, 1f), 45f);
        Material shirtMat = createLitMaterial(assetManager, shirtColor != null ? shirtColor : new ColorRGBA(0.18f, 0.42f, 0.68f, 1f), 10f);
        Material pantsMat = createLitMaterial(assetManager, pantsColor != null ? pantsColor : new ColorRGBA(0.12f, 0.18f, 0.28f, 1f), 8f);
        Material beltMat = createLitMaterial(assetManager, new ColorRGBA(0.18f, 0.12f, 0.08f, 1f), 20f);
        Material buckleMat = createLitMaterial(assetManager, new ColorRGBA(0.85f, 0.85f, 0.85f, 1f), 60f);
        Material shoeMat = createLitMaterial(assetManager, new ColorRGBA(0.12f, 0.12f, 0.14f, 1f), 18f);
        Material shoeSoleMat = createLitMaterial(assetManager, new ColorRGBA(0.92f, 0.92f, 0.90f, 1f), 24f);
        Material badgeMat = createLitMaterial(assetManager, new ColorRGBA(0.88f, 0.22f, 0.20f, 1f), 30f);
        Material backpackMat = createLitMaterial(assetManager, new ColorRGBA(0.20f, 0.24f, 0.28f, 1f), 12f);
        Material bottleMat = createLitMaterial(assetManager, new ColorRGBA(0.25f, 0.55f, 0.70f, 1f), 45f);
        Material headbandGreenMat = createLitMaterial(assetManager, new ColorRGBA(0.02f, 0.44f, 0.24f, 1f), 10f);
        Material headbandRedMat = createLitMaterial(assetManager, new ColorRGBA(0.88f, 0.18f, 0.16f, 1f), 10f);

        // 1. Torso Root (positioned at waist pivot ~0.95m from ground)
        torsoNode = new Node("Torso");
        torsoNode.setLocalTranslation(0f, 0.95f, 0f);

        // Lower Torso / Waist
        Box waistBox = new Box(0.16f, 0.12f, 0.10f);
        Geometry waistGeom = new Geometry("Waist", waistBox);
        waistGeom.setMaterial(shirtMat);
        waistGeom.setLocalTranslation(0f, 0.12f, 0f);
        torsoNode.attachChild(waistGeom);

        // Leather Belt
        Box beltBox = new Box(0.165f, 0.025f, 0.105f);
        Geometry beltGeom = new Geometry("Belt", beltBox);
        beltGeom.setMaterial(beltMat);
        beltGeom.setLocalTranslation(0f, 0.025f, 0f);
        torsoNode.attachChild(beltGeom);

        // Silver Belt Buckle
        Box buckleBox = new Box(0.035f, 0.022f, 0.005f);
        Geometry buckleGeom = new Geometry("Buckle", buckleBox);
        buckleGeom.setMaterial(buckleMat);
        buckleGeom.setLocalTranslation(0f, 0.025f, -0.108f);
        torsoNode.attachChild(buckleGeom);

        // Upper Chest & Shoulders (athletic student taper)
        Box chestBox = new Box(0.19f, 0.18f, 0.12f);
        Geometry chestGeom = new Geometry("Chest", chestBox);
        chestGeom.setMaterial(shirtMat);
        chestGeom.setLocalTranslation(0f, 0.40f, 0f);
        torsoNode.attachChild(chestGeom);

        // DU Student ID Card Badge (on chest)
        Box badgeBox = new Box(0.045f, 0.060f, 0.005f);
        Geometry badgeGeom = new Geometry("Badge", badgeBox);
        badgeGeom.setMaterial(badgeMat);
        badgeGeom.setLocalTranslation(0.09f, 0.44f, -0.125f);
        torsoNode.attachChild(badgeGeom);

        // Student Canvas Backpack (on back of chest)
        Box packBox = new Box(0.15f, 0.22f, 0.08f);
        Geometry packGeom = new Geometry("Backpack", packBox);
        packGeom.setMaterial(backpackMat);
        packGeom.setLocalTranslation(0f, 0.38f, 0.20f);
        torsoNode.attachChild(packGeom);

        // Backpack Top Flap
        Box flapBox = new Box(0.155f, 0.045f, 0.085f);
        Geometry flapGeom = new Geometry("BackpackFlap", flapBox);
        flapGeom.setMaterial(backpackMat);
        flapGeom.setLocalTranslation(0f, 0.56f, 0.20f);
        torsoNode.attachChild(flapGeom);

        // Side Water Bottle
        Cylinder bottleCyl = new Cylinder(12, 12, 0.035f, 0.16f, true);
        Geometry bottleGeom = new Geometry("WaterBottle", bottleCyl);
        bottleGeom.rotate(FastMath.HALF_PI, 0, 0);
        bottleGeom.setMaterial(bottleMat);
        bottleGeom.setLocalTranslation(0.17f, 0.38f, 0.20f);
        torsoNode.attachChild(bottleGeom);

        // Dual Shoulder Straps
        Box strapL = new Box(0.025f, 0.22f, 0.005f);
        Geometry strapGeomL = new Geometry("StrapL", strapL);
        strapGeomL.setMaterial(backpackMat);
        strapGeomL.setLocalTranslation(-0.12f, 0.40f, -0.122f);
        torsoNode.attachChild(strapGeomL);

        Box strapR = new Box(0.025f, 0.22f, 0.005f);
        Geometry strapGeomR = new Geometry("StrapR", strapR);
        strapGeomR.setMaterial(backpackMat);
        strapGeomR.setLocalTranslation(0.12f, 0.40f, -0.122f);
        torsoNode.attachChild(strapGeomR);

        rootNode.attachChild(torsoNode);

        // 2. Head & Neck
        headNode = new Node("Head");
        headNode.setLocalTranslation(0f, 0.58f, 0f);

        // Defined Neck Cylinder
        Cylinder neckCyl = new Cylinder(14, 14, 0.055f, 0.10f, true);
        Geometry neckGeom = new Geometry("Neck", neckCyl);
        neckGeom.rotate(FastMath.HALF_PI, 0, 0);
        neckGeom.setMaterial(skinMat);
        neckGeom.setLocalTranslation(0f, 0.05f, 0f);
        headNode.attachChild(neckGeom);

        // Head Ovaloid
        Sphere headSphere = new Sphere(18, 18, 0.13f);
        Geometry headGeom = new Geometry("HeadGeom", headSphere);
        headGeom.setMaterial(skinMat);
        headGeom.setLocalTranslation(0f, 0.18f, 0f);
        headGeom.setLocalScale(0.92f, 1.05f, 0.95f);
        headNode.attachChild(headGeom);

        // Styled Student Hair Cap
        Sphere hairSphere = new Sphere(16, 16, 0.135f);
        Geometry hairGeom = new Geometry("HairGeom", hairSphere);
        hairGeom.setMaterial(hairMat);
        hairGeom.setLocalTranslation(0f, 0.21f, 0.02f);
        hairGeom.setLocalScale(0.95f, 1.0f, 1.02f);
        headNode.attachChild(hairGeom);

        // Eyes
        Sphere eyeL = new Sphere(8, 8, 0.018f);
        Geometry eyeGeomL = new Geometry("EyeL", eyeL);
        eyeGeomL.setMaterial(eyeMat);
        eyeGeomL.setLocalTranslation(-0.045f, 0.19f, -0.120f);
        headNode.attachChild(eyeGeomL);

        Sphere eyeR = new Sphere(8, 8, 0.018f);
        Geometry eyeGeomR = new Geometry("EyeR", eyeR);
        eyeGeomR.setMaterial(eyeMat);
        eyeGeomR.setLocalTranslation(0.045f, 0.19f, -0.120f);
        headNode.attachChild(eyeGeomR);

        // Bangladesh National Flag Headband (July 2024 Movement Symbol)
        Box headbandBox = new Box(0.135f, 0.022f, 0.135f);
        Geometry headbandGeom = new Geometry("Headband", headbandBox);
        headbandGeom.setMaterial(headbandGreenMat);
        headbandGeom.setLocalTranslation(0f, 0.21f, 0f);
        headNode.attachChild(headbandGeom);

        // Red Disc on Forehead Center of Headband
        Cylinder redDisc = new Cylinder(14, 14, 0.030f, 0.006f, true);
        Geometry redDiscGeom = new Geometry("HeadbandRedDisc", redDisc);
        redDiscGeom.setMaterial(headbandRedMat);
        redDiscGeom.setLocalTranslation(0f, 0.21f, -0.138f);
        headNode.attachChild(redDiscGeom);

        torsoNode.attachChild(headNode);

        // 3. Left & Right Arms (cylindrical with rolled shirt sleeves)
        leftArmNode = createArm(skinMat, shirtMat, true);
        leftArmNode.setLocalTranslation(-0.25f, 0.52f, 0f);
        torsoNode.attachChild(leftArmNode);

        rightArmNode = createArm(skinMat, shirtMat, false);
        rightArmNode.setLocalTranslation(0.25f, 0.52f, 0f);
        torsoNode.attachChild(rightArmNode);

        // 4. Left & Right Legs (cylindrical denim trousers & sneakers)
        leftLegNode = createLeg(pantsMat, shoeMat, shoeSoleMat);
        leftLegNode.setLocalTranslation(-0.11f, 0.95f, 0f);
        rootNode.attachChild(leftLegNode);

        rightLegNode = createLeg(pantsMat, shoeMat, shoeSoleMat);
        rightLegNode.setLocalTranslation(0.11f, 0.95f, 0f);
        rootNode.attachChild(rightLegNode);
    }

    private Node createArm(Material skinMat, Material shirtMat, boolean isLeft) {
        Node arm = new Node("Arm");

        // Upper arm with rolled shirt sleeve (cylindrical)
        Cylinder sleeveCyl = new Cylinder(14, 14, 0.052f, 0.18f, true);
        Geometry sleeve = new Geometry("Sleeve", sleeveCyl);
        sleeve.rotate(FastMath.HALF_PI, 0, 0);
        sleeve.setMaterial(shirtMat);
        sleeve.setLocalTranslation(0f, -0.09f, 0f);
        arm.attachChild(sleeve);

        // Forearm (skin tone, slightly tapered)
        Cylinder forearmCyl = new Cylinder(12, 12, 0.042f, 0.18f, true);
        Geometry forearm = new Geometry("Forearm", forearmCyl);
        forearm.rotate(FastMath.HALF_PI, 0, 0);
        forearm.setMaterial(skinMat);
        forearm.setLocalTranslation(0f, -0.27f, 0f);
        arm.attachChild(forearm);

        // Hand
        Box handBox = new Box(0.035f, 0.055f, 0.025f);
        Geometry hand = new Geometry("Hand", handBox);
        hand.setMaterial(skinMat);
        hand.setLocalTranslation(0f, -0.41f, 0f);
        arm.attachChild(hand);

        return arm;
    }

    private Node createLeg(Material pantsMat, Material shoeMat, Material shoeSoleMat) {
        Node leg = new Node("Leg");

        // Thigh (denim trouser cylinder)
        Cylinder thighCyl = new Cylinder(14, 14, 0.072f, 0.40f, true);
        Geometry thighGeom = new Geometry("Thigh", thighCyl);
        thighGeom.rotate(FastMath.HALF_PI, 0, 0);
        thighGeom.setMaterial(pantsMat);
        thighGeom.setLocalTranslation(0f, -0.20f, 0f);
        leg.attachChild(thighGeom);

        // Knee Joint
        Sphere kneeSphere = new Sphere(10, 10, 0.068f);
        Geometry kneeGeom = new Geometry("Knee", kneeSphere);
        kneeGeom.setMaterial(pantsMat);
        kneeGeom.setLocalTranslation(0f, -0.40f, 0f);
        leg.attachChild(kneeGeom);

        // Calf (denim lower leg cylinder)
        Cylinder calfCyl = new Cylinder(14, 14, 0.062f, 0.38f, true);
        Geometry calfGeom = new Geometry("Calf", calfCyl);
        calfGeom.rotate(FastMath.HALF_PI, 0, 0);
        calfGeom.setMaterial(pantsMat);
        calfGeom.setLocalTranslation(0f, -0.59f, 0f);
        leg.attachChild(calfGeom);

        // Sneaker Upper (Dark canvas campus shoe)
        Box shoeUpper = new Box(0.068f, 0.040f, 0.115f);
        Geometry shoeGeom = new Geometry("ShoeUpper", shoeUpper);
        shoeGeom.setMaterial(shoeMat);
        shoeGeom.setLocalTranslation(0f, -0.83f, 0.025f);
        leg.attachChild(shoeGeom);

        // Sneaker White Rubber Outsole Rim
        Box shoeSole = new Box(0.072f, 0.014f, 0.120f);
        Geometry soleGeom = new Geometry("ShoeSole", shoeSole);
        soleGeom.setMaterial(shoeSoleMat);
        soleGeom.setLocalTranslation(0f, -0.875f, 0.025f);
        leg.attachChild(soleGeom);

        return leg;
    }

    private Material createLitMaterial(AssetManager assetManager, ColorRGBA color, float shininess) {
        Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        mat.setBoolean("UseMaterialColors", true);
        mat.setColor("Ambient", color.mult(0.65f));
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
