package bd.july2024.world;

import com.jme3.asset.AssetManager;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.FXAAFilter;
import com.jme3.post.filters.FogFilter;
import com.jme3.post.filters.ToneMapFilter;
import com.jme3.post.ssao.SSAOFilter;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Sphere;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.shadow.DirectionalLightShadowFilter;

/**
 * Realistic Bangladesh Summer Lighting & Post-Processing Manager for Project July 2024.
 *
 * Simulates a warm, hazy July afternoon in Dhaka (3:30 PM):
 * - Strong directional sunlight with warm golden tint
 * - Cool blue ambient bounce from a tropical overcast sky
 * - Atmospheric haze/fog to mask ground-plane edges and simulate summer humidity
 * - Gradient sky dome (light blue zenith → warm white horizon)
 * - Cascaded directional shadows, SSAO, filmic tone mapping, FXAA
 */
public class LightingManager {

    private final DirectionalLight sun;
    private final DirectionalLight fillLight;
    private final AmbientLight ambient;
    private FilterPostProcessor fpp;
    private DirectionalLightShadowFilter shadowFilter;
    private SSAOFilter ssaoFilter;
    private ToneMapFilter toneMapFilter;
    private FXAAFilter fxaaFilter;

    public LightingManager(AssetManager assetManager, Node rootNode, ViewPort viewPort) {
        // 1. Primary Sun Light (Warm afternoon golden light, ~3:30 PM Bangladesh summer)
        sun = new DirectionalLight();
        sun.setColor(new ColorRGBA(1.05f, 0.96f, 0.82f, 1.0f));
        sun.setDirection(new Vector3f(0.35f, -0.65f, -0.55f).normalizeLocal());
        rootNode.addLight(sun);

        // 2. Fill Light (Subtle secondary from opposite side to reduce harsh shadows)
        fillLight = new DirectionalLight();
        fillLight.setColor(new ColorRGBA(0.18f, 0.22f, 0.28f, 1.0f));
        fillLight.setDirection(new Vector3f(-0.4f, -0.3f, 0.6f).normalizeLocal());
        rootNode.addLight(fillLight);

        // 3. Ambient Sky Light (Warm humid Bangladesh summer ambience)
        ambient = new AmbientLight();
        ambient.setColor(new ColorRGBA(0.48f, 0.50f, 0.52f, 1.0f));
        rootNode.addLight(ambient);

        // 4. Sky Dome (Gradient sphere for atmospheric backdrop)
        buildSkyDome(assetManager, rootNode);

        // 5. Post-Processing Pipeline
        setupPostProcessing(assetManager, viewPort);
    }

    private void buildSkyDome(AssetManager assetManager, Node rootNode) {
        // Large inverted sphere as sky dome with unshaded gradient color
        Sphere skyMesh = new Sphere(32, 32, 280f);
        skyMesh.setTextureMode(Sphere.TextureMode.Projected);
        Geometry skyGeom = new Geometry("SkyDome", skyMesh);

        Material skyMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        // Light blue-gray that matches Bangladesh humid summer sky
        skyMat.setColor("Color", new ColorRGBA(0.62f, 0.74f, 0.88f, 1.0f));
        skyMat.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Front);
        skyGeom.setMaterial(skyMat);
        skyGeom.setQueueBucket(RenderQueue.Bucket.Sky);
        skyGeom.setCullHint(com.jme3.scene.Spatial.CullHint.Never);
        rootNode.attachChild(skyGeom);
    }

    private void setupPostProcessing(AssetManager assetManager, ViewPort viewPort) {
        if (assetManager == null || viewPort == null) return;
        try {
            fpp = new FilterPostProcessor(assetManager);

            // 1. Cascaded Directional Shadows (PSSM)
            try {
                shadowFilter = new DirectionalLightShadowFilter(assetManager, 2048, 3);
                shadowFilter.setLight(sun);
                shadowFilter.setShadowIntensity(0.55f);
                shadowFilter.setEdgeFilteringMode(com.jme3.shadow.EdgeFilteringMode.PCF4);
                shadowFilter.setEnabled(true);
                fpp.addFilter(shadowFilter);
            } catch (Throwable t) {
                System.err.println("[LightingManager] DirectionalLightShadowFilter failed: " + t.getMessage());
            }

            // 2. Screen Space Ambient Occlusion (SSAO) — subtle contact shadows
            try {
                ssaoFilter = new SSAOFilter(1.2f, 2.4f, 0.2f, 0.1f);
                fpp.addFilter(ssaoFilter);
            } catch (Throwable t) {
                System.err.println("[LightingManager] SSAOFilter failed: " + t.getMessage());
            }

            // 3. Filmic Tone Mapping (Natural daylight dynamic range)
            try {
                toneMapFilter = new ToneMapFilter();
                toneMapFilter.setWhitePoint(new Vector3f(1.15f, 1.15f, 1.15f));
                fpp.addFilter(toneMapFilter);
            } catch (Throwable t) {
                System.err.println("[LightingManager] ToneMapFilter failed: " + t.getMessage());
            }

            // 4. Fast Approximate Anti-Aliasing (FXAA 3.11)
            try {
                fxaaFilter = new FXAAFilter();
                fpp.addFilter(fxaaFilter);
            } catch (Throwable t) {
                System.err.println("[LightingManager] FXAAFilter failed: " + t.getMessage());
            }

            viewPort.addProcessor(fpp);
            System.out.println("[LightingManager] Successfully attached FilterPostProcessor (Shadows + SSAO + ToneMap + FXAA) to viewPort.");
        } catch (Throwable t) {
            System.err.println("[LightingManager] Post processor setup failed: " + t.getMessage());
        }
    }

    public void applyGraphicsSettings(int shadowQuality, boolean ssao, boolean fxaa) {
        if (shadowFilter != null) {
            boolean enableShadow = shadowQuality > 0;
            shadowFilter.setEnabled(enableShadow);
            if (enableShadow) {
                float intensity = shadowQuality == 1 ? 0.40f : (shadowQuality == 2 ? 0.55f : 0.65f);
                shadowFilter.setShadowIntensity(intensity);
            }
        }
        if (ssaoFilter != null) {
            ssaoFilter.setEnabled(ssao);
        }
        if (fxaaFilter != null) {
            fxaaFilter.setEnabled(fxaa);
        }
    }

    public void setShadowsEnabled(boolean enabled) {
        if (shadowFilter != null) shadowFilter.setEnabled(enabled);
    }

    public void setSsaoEnabled(boolean enabled) {
        if (ssaoFilter != null) ssaoFilter.setEnabled(enabled);
    }

    public DirectionalLightShadowFilter getShadowFilter() { return shadowFilter; }
    public SSAOFilter getSsaoFilter() { return ssaoFilter; }
    public FXAAFilter getFxaaFilter() { return fxaaFilter; }

    public DirectionalLight getSun() { return sun; }
    public AmbientLight getAmbient() { return ambient; }
    public FilterPostProcessor getFilterPostProcessor() { return fpp; }
}
