package bd.july2024.level;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Cylinder;

/**
 * In-world 3D animated objective beacon and waypoint marker for Project July 2024.
 * Displays a rotating, bobbing amber diamond marker, a vertical light pillar,
 * and a pulsing ground projection ring at the active mission target position.
 */
public class ObjectiveMarker {

    private final Node rootNode;
    private final Node diamondNode;
    private final Geometry pillarGeom;
    private final Geometry ringGeom;
    private final Vector3f currentPos = new Vector3f();
    private float animTime = 0f;

    public ObjectiveMarker(AssetManager assetManager) {
        rootNode = new Node("ObjectiveMarkerRoot");

        // Amber gold color matching the Project July HUD accent
        ColorRGBA markerColor = new ColorRGBA(1.0f, 0.78f, 0.22f, 0.75f);
        Material markerMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        markerMat.setColor("Color", markerColor);
        markerMat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        markerMat.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
        markerMat.getAdditionalRenderState().setDepthWrite(false);

        // 1. Floating rotating diamond mesh (45-degree tilted box)
        diamondNode = new Node("DiamondNode");
        Box diamondBox = new Box(0.24f, 0.24f, 0.24f);
        Geometry diamondGeom = new Geometry("ObjectiveDiamond", diamondBox);
        diamondGeom.setMaterial(markerMat);
        diamondGeom.setQueueBucket(RenderQueue.Bucket.Transparent);
        diamondGeom.setLocalRotation(new Quaternion().fromAngles(FastMath.QUARTER_PI, FastMath.QUARTER_PI, 0f));
        diamondNode.attachChild(diamondGeom);
        diamondNode.setLocalTranslation(0f, 2.5f, 0f);
        rootNode.attachChild(diamondNode);

        // 2. Vertical light pillar beam
        Cylinder pillarMesh = new Cylinder(2, 16, 0.08f, 4.5f, true);
        pillarGeom = new Geometry("ObjectivePillar", pillarMesh);
        pillarGeom.setMaterial(markerMat);
        pillarGeom.setQueueBucket(RenderQueue.Bucket.Transparent);
        pillarGeom.setLocalRotation(new Quaternion().fromAngleAxis(FastMath.HALF_PI, Vector3f.UNIT_X));
        pillarGeom.setLocalTranslation(0f, 2.25f, 0f);
        rootNode.attachChild(pillarGeom);

        // 3. Ground pulse ring
        Cylinder ringMesh = new Cylinder(2, 24, 1.35f, 0.02f, true);
        ringGeom = new Geometry("ObjectiveRing", ringMesh);
        ringGeom.setMaterial(markerMat);
        ringGeom.setQueueBucket(RenderQueue.Bucket.Transparent);
        ringGeom.setLocalRotation(new Quaternion().fromAngleAxis(FastMath.HALF_PI, Vector3f.UNIT_X));
        ringGeom.setLocalTranslation(0f, 0.03f, 0f);
        rootNode.attachChild(ringGeom);

        rootNode.setCullHint(Spatial.CullHint.Always);
    }

    public Node getNode() {
        return rootNode;
    }

    public void update(float tpf, float targetX, float groundY, float targetZ, boolean visible) {
        if (!visible) {
            rootNode.setCullHint(Spatial.CullHint.Always);
            return;
        }

        rootNode.setCullHint(Spatial.CullHint.Never);
        animTime += tpf;

        // Smoothly position at the target coordinates
        currentPos.set(targetX, groundY, targetZ);
        rootNode.setLocalTranslation(currentPos);

        // Rotate diamond
        diamondNode.rotate(0f, 1.8f * tpf, 0f);

        // Bob diamond vertically
        float bob = FastMath.sin(animTime * 3.2f) * 0.15f;
        diamondNode.setLocalTranslation(0f, 2.4f + bob, 0f);

        // Pulse ground ring radius
        float pulse = 1.0f + FastMath.sin(animTime * 2.8f) * 0.12f;
        ringGeom.setLocalScale(pulse, 1.0f, pulse);
    }
}
