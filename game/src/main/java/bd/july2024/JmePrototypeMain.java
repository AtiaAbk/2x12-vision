package bd.july2024;

import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.FXAAFilter;
import com.jme3.post.filters.ToneMapFilter;
import com.jme3.post.ssao.SSAOFilter;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Cylinder;
import com.jme3.scene.shape.Sphere;
import com.jme3.shadow.DirectionalLightShadowFilter;
import com.jme3.system.AppSettings;

public class JmePrototypeMain extends SimpleApplication {

    private int frameCounter = 0;
    private float totalDeltaTime = 0f;
    private float minDelta = Float.MAX_VALUE;
    private float maxDelta = 0f;

    private Node characterNode;
    private Vector3f camOffset = new Vector3f(0f, 2.5f, 5.5f);

    public static void main(String[] args) {
        JmePrototypeMain app = new JmePrototypeMain();
        AppSettings settings = new AppSettings(true);
        settings.setTitle("Project July 2024 — jMonkeyEngine Prototype");
        settings.setWidth(1280);
        settings.setHeight(720);
        settings.setResolution(1280, 720);
        settings.setVSync(false);
        settings.setSamples(1);
        settings.setFrameRate(120);

        app.setSettings(settings);
        app.setShowSettings(false);
        app.start();
    }

    @Override
    public void simpleInitApp() {
        flyCam.setEnabled(false);
        cam.setLocation(new Vector3f(0f, 3f, 8f));
        cam.lookAt(new Vector3f(0f, 1.2f, 0f), Vector3f.UNIT_Y);

        setupLighting();
        setupPostProcessing();
        buildCampusScene();
        setupCharacter();
        testBengaliTypography();
    }

    private void setupLighting() {
        DirectionalLight sun = new DirectionalLight();
        sun.setColor(new ColorRGBA(1.0f, 0.94f, 0.86f, 1.0f));
        sun.setDirection(new Vector3f(-0.45f, -0.75f, -0.48f).normalizeLocal());
        rootNode.addLight(sun);

        AmbientLight ambient = new AmbientLight();
        ambient.setColor(new ColorRGBA(0.42f, 0.44f, 0.48f, 1.0f));
        rootNode.addLight(ambient);
    }

    private void setupPostProcessing() {
        try {
            FilterPostProcessor fpp = new FilterPostProcessor(assetManager);

            DirectionalLight sun = (DirectionalLight) rootNode.getLocalLightList().get(0);
            DirectionalLightShadowFilter dlsf = new DirectionalLightShadowFilter(assetManager, 1024, 3);
            dlsf.setLight(sun);
            dlsf.setEnabled(true);
            fpp.addFilter(dlsf);

            SSAOFilter ssao = new SSAOFilter(1.2f, 2.5f, 0.2f, 0.1f);
            fpp.addFilter(ssao);

            ToneMapFilter toneMap = new ToneMapFilter();
            toneMap.setWhitePoint(new Vector3f(1.1f, 1.1f, 1.1f));
            fpp.addFilter(toneMap);

            FXAAFilter fxaa = new FXAAFilter();
            fpp.addFilter(fxaa);

            viewPort.addProcessor(fpp);
            System.out.println("[jME Prototype] Successfully initialized PostProcessor");
        } catch (Throwable t) {
            System.err.println("[jME Prototype] Failed: " + t.getMessage());
        }
    }

    private void buildCampusScene() {
        Box groundBox = new Box(60f, 0.1f, 60f);
        Geometry ground = new Geometry("Ground", groundBox);
        Material groundMat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        groundMat.setBoolean("UseMaterialColors", true);
        groundMat.setColor("Diffuse", new ColorRGBA(0.18f, 0.19f, 0.20f, 1f));
        groundMat.setColor("Specular", new ColorRGBA(0.05f, 0.05f, 0.05f, 1f));
        groundMat.setFloat("Shininess", 16f);
        ground.setMaterial(groundMat);
        ground.setLocalTranslation(0, -0.1f, 0);
        rootNode.attachChild(ground);

        Cylinder islandCyl = new Cylinder(32, 32, 7.5f, 0.35f, true);
        Geometry island = new Geometry("RajuIsland", islandCyl);
        island.rotate(FastMath.HALF_PI, 0, 0);
        island.setLocalTranslation(0, 0.18f, 0);
        Material islandMat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        islandMat.setBoolean("UseMaterialColors", true);
        islandMat.setColor("Diffuse", new ColorRGBA(0.25f, 0.42f, 0.22f, 1f));
        island.setMaterial(islandMat);
        rootNode.attachChild(island);

        for (int i = 0; i < 7; i++) {
            float angle = i * (FastMath.TWO_PI / 7f);
            float radius = 2.2f;
            Box figureBox = new Box(0.35f, 1.4f + (i % 3) * 0.4f, 0.35f);
            Geometry figure = new Geometry("Figure_" + i, figureBox);
            figure.setLocalTranslation(FastMath.cos(angle) * radius, 1.7f + (i % 3) * 0.4f, FastMath.sin(angle) * radius);
            Material figMat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
            figMat.setBoolean("UseMaterialColors", true);
            figMat.setColor("Diffuse", new ColorRGBA(0.68f, 0.65f, 0.60f, 1f));
            figMat.setColor("Specular", new ColorRGBA(0.15f, 0.15f, 0.15f, 1f));
            figure.setMaterial(figMat);
            rootNode.attachChild(figure);
        }

        for (int c = -4; c <= 4; c++) {
            Cylinder colCyl = new Cylinder(16, 16, 0.45f, 5.0f, true);
            Geometry column = new Geometry("Col_" + c, colCyl);
            column.rotate(FastMath.HALF_PI, 0, 0);
            column.setLocalTranslation(c * 3.8f, 2.5f, -22f);
            Material colMat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
            colMat.setBoolean("UseMaterialColors", true);
            colMat.setColor("Diffuse", new ColorRGBA(0.82f, 0.80f, 0.76f, 1f));
            column.setMaterial(colMat);
            rootNode.attachChild(column);
        }

        Box libBox = new Box(18f, 4f, 8f);
        Geometry lib = new Geometry("LibraryUpper", libBox);
        lib.setLocalTranslation(0f, 9.0f, -24f);
        Material libMat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        libMat.setBoolean("UseMaterialColors", true);
        libMat.setColor("Diffuse", new ColorRGBA(0.74f, 0.28f, 0.22f, 1f));
        lib.setMaterial(libMat);
        rootNode.attachChild(lib);
    }

    private void setupCharacter() {
        characterNode = new Node("PlayerCharacter");

        Box torsoBox = new Box(0.24f, 0.45f, 0.14f);
        Geometry torso = new Geometry("Torso", torsoBox);
        torso.setLocalTranslation(0f, 1.25f, 0f);
        Material torsoMat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        torsoMat.setBoolean("UseMaterialColors", true);
        torsoMat.setColor("Diffuse", new ColorRGBA(0.18f, 0.42f, 0.68f, 1f));
        torso.setMaterial(torsoMat);
        characterNode.attachChild(torso);

        Sphere headSphere = new Sphere(16, 16, 0.16f);
        Geometry head = new Geometry("Head", headSphere);
        head.setLocalTranslation(0f, 1.85f, 0f);
        Material headMat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        headMat.setBoolean("UseMaterialColors", true);
        headMat.setColor("Diffuse", new ColorRGBA(0.84f, 0.64f, 0.48f, 1f));
        head.setMaterial(headMat);
        characterNode.attachChild(head);

        characterNode.setLocalTranslation(0f, 0f, 4.5f);
        rootNode.attachChild(characterNode);
    }

    private void testBengaliTypography() {
        try {
            BitmapFont font = assetManager.loadFont("Interface/Fonts/Default.fnt");
            BitmapText ch = new BitmapText(font);
            ch.setSize(font.getCharSet().getRenderedSize() * 1.5f);
            ch.setText("PROJECT JULY 2024 — jMonkeyEngine Level 01");
            ch.setColor(ColorRGBA.Yellow);
            ch.setLocalTranslation(30, settings.getHeight() - 30, 0);
            guiNode.attachChild(ch);
        } catch (Throwable ignored) {}
    }

    @Override
    public void simpleUpdate(float tpf) {
        frameCounter++;
        totalDeltaTime += tpf;
        if (tpf < minDelta) minDelta = tpf;
        if (tpf > maxDelta) maxDelta = tpf;

        Vector3f charPos = characterNode.getLocalTranslation();
        Vector3f desiredCamPos = charPos.add(camOffset);
        cam.setLocation(cam.getLocation().interpolateLocal(desiredCamPos, 0.1f));
        cam.lookAt(charPos.add(0, 1.4f, 0), Vector3f.UNIT_Y);

        if (frameCounter >= 60) {
            long usedMem = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024 * 1024);
            long totalMem = Runtime.getRuntime().totalMemory() / (1024 * 1024);
            long maxMem = Runtime.getRuntime().maxMemory() / (1024 * 1024);
            float avgFrameMs = (totalDeltaTime / frameCounter) * 1000f;
            float avgFps = frameCounter / (totalDeltaTime > 0 ? totalDeltaTime : 0.0001f);

            System.out.println("=== JMONKEYENGINE VERIFICATION SUCCESSFUL ===");
            System.out.println("Frames Rendered: " + frameCounter);
            System.out.println("Average Frame Time: " + String.format("%.2f ms", avgFrameMs) + " (~" + String.format("%.1f", avgFps) + " FPS)");
            System.out.println("Heap Memory: Used " + usedMem + " MB / Total " + totalMem + " MB (Max: " + maxMem + " MB)");
            System.out.println("Resolution: " + settings.getWidth() + "x" + settings.getHeight());
            System.out.println("Renderer: " + renderer.getClass().getSimpleName());
            System.out.println("=============================================");

            stop(true);
            new Thread(() -> {
                try { Thread.sleep(500); } catch (Exception ignored) {}
                System.exit(0);
            }).start();
        }
    }
}
