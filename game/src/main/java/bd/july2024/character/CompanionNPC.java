package bd.july2024.character;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Sphere;

/**
 * Dedicated Interactive Student Companion NPC in Project July 2024.
 * Turns to face Arif and displays an active interaction beacon.
 */
public class CompanionNPC {

    private final String id;
    private final String nameBengali;
    private final String roleEnglish;
    private final Vector3f position;
    private final Node npcNode;
    private final PlayerMesh mesh;
    private Geometry beaconGeom;
    private float animTime = 0f;

    public CompanionNPC(AssetManager assetManager, String id, String nameBengali, String roleEnglish, Vector3f position, ColorRGBA shirtColor, ColorRGBA pantsColor) {
        this(assetManager, CharacterSpec.ARIF, id, nameBengali, roleEnglish, position, shirtColor, pantsColor);
    }

    public CompanionNPC(AssetManager assetManager, CharacterSpec spec, String id, String nameBengali, String roleEnglish, Vector3f position, ColorRGBA shirtColor, ColorRGBA pantsColor) {
        this.id = id;
        this.nameBengali = nameBengali;
        this.roleEnglish = roleEnglish;
        this.position = position.clone();

        npcNode = new Node("NPC_" + id);
        mesh = new PlayerMesh(assetManager, spec, shirtColor, pantsColor);
        npcNode.attachChild(mesh.getRootNode());
        npcNode.setLocalTranslation(position);

        // Interaction Beacon / Overhead Marker
        Sphere beaconSphere = new Sphere(16, 16, 0.12f);
        beaconGeom = new Geometry("Beacon_" + id, beaconSphere);
        Material beaconMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        beaconMat.setColor("Color", new ColorRGBA(1.0f, 0.85f, 0.25f, 0.9f));
        beaconGeom.setMaterial(beaconMat);
        beaconGeom.setLocalTranslation(0f, 2.3f, 0f);
        npcNode.attachChild(beaconGeom);
    }

    public void update(float delta, Vector3f playerPos) {
        animTime += delta;
        mesh.animate(0f, 0f, animTime, false);

        // Gentle pulsing overhead beacon
        if (beaconGeom != null) {
            float pulse = FastMath.sin(animTime * 3.5f) * 0.08f;
            beaconGeom.setLocalTranslation(0f, 2.3f + pulse, 0f);
        }

        // Dynamically rotate towards player if nearby
        if (playerPos != null && playerPos.distance(position) < 8.0f) {
            float dx = playerPos.x - position.x;
            float dz = playerPos.z - position.z;
            float angle = FastMath.atan2(dx, dz);
            npcNode.setLocalRotation(new com.jme3.math.Quaternion().fromAngleAxis(angle, Vector3f.UNIT_Y));
        }
    }

    public String getId() { return id; }
    public String getNameBengali() { return nameBengali; }
    public String getRoleEnglish() { return roleEnglish; }
    public Vector3f getPosition() { return position; }
    public Node getNode() { return npcNode; }
}
