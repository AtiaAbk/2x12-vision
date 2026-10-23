package bd.july2024.world;

import com.jme3.asset.AssetManager;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.FXAAFilter;
import com.jme3.post.filters.ToneMapFilter;
import com.jme3.post.ssao.SSAOFilter;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.shadow.DirectionalLightShadowFilter;

/**
 * Realistic Bangladesh Summer Lighting & Post-Processing Manager for Project July 2024.
 * Features warm golden direct afternoon sun, soft atmospheric sky fill,
 * cascaded directional shadows, SSAO, filmic tone mapping, and FXAA.
 */
public class LightingManager {

    private final DirectionalLight sun;
    private final AmbientLight ambient;
    private FilterPostProcessor fpp;

    public LightingManager(AssetManager assetManager, Node rootNode, ViewPort viewPort) {
        // 1. Direct Sun Light (Warm afternoon golden light typical of Bangladesh summer)
        sun = new DirectionalLight();
        sun.setColor(new ColorRGBA(1.0f, 0.94f, 0.86f, 1.0f));
        sun.setDirection(new Vector3f(0.30f, -0.70f, -0.65f).normalizeLocal());
        rootNode.addLight(sun);

        // 2. Ambient Sky Light (Soft cool blue-gray ambient bounce)
        ambient = new AmbientLight();
        ambient.setColor(new ColorRGBA(0.42f, 0.45f, 0.50f, 1.0f));
        rootNode.addLight(ambient);

        // 3. Post-Processing Pipeline
        setupPostProcessing(assetManager, viewPort);
    }

    private void setupPostProcessing(AssetManager assetManager, ViewPort viewPort) {
        if (assetManager == null || viewPort == null) return;
        try {
            fpp = new FilterPostProcessor(assetManager);

            // 1. Cascaded Directional Shadows (PSSM)
            try {
                DirectionalLightShadowFilter shadowFilter = new DirectionalLightShadowFilter(assetManager, 1024, 3);
                shadowFilter.setLight(sun);
                shadowFilter.setShadowIntensity(0.65f);
                shadowFilter.setEnabled(true);
                fpp.addFilter(shadowFilter);
            } catch (Throwable t) {
                System.err.println("[LightingManager] DirectionalLightShadowFilter failed: " + t.getMessage());
            }

            // 2. Screen Space Ambient Occlusion (SSAO)
            try {
                SSAOFilter ssaoFilter = new SSAOFilter(1.2f, 2.4f, 0.2f, 0.1f);
                fpp.addFilter(ssaoFilter);
            } catch (Throwable t) {
                System.err.println("[LightingManager] SSAOFilter failed: " + t.getMessage());
            }

            // 3. Filmic Tone Mapping
            try {
                ToneMapFilter toneMap = new ToneMapFilter();
                toneMap.setWhitePoint(new Vector3f(1.1f, 1.1f, 1.1f));
                fpp.addFilter(toneMap);
            } catch (Throwable t) {
                System.err.println("[LightingManager] ToneMapFilter failed: " + t.getMessage());
            }

            // 4. Fast Approximate Anti-Aliasing (FXAA 3.11)
            try {
                FXAAFilter fxaa = new FXAAFilter();
                fpp.addFilter(fxaa);
            } catch (Throwable t) {
                System.err.println("[LightingManager] FXAAFilter failed: " + t.getMessage());
            }

            viewPort.addProcessor(fpp);
            System.out.println("[LightingManager] Successfully attached FilterPostProcessor (Shadows + SSAO + ToneMap + FXAA) to viewPort.");
        } catch (Throwable t) {
            System.err.println("[LightingManager] Post processor setup failed: " + t.getMessage());
        }
    }

    public DirectionalLight getSun() { return sun; }
    public AmbientLight getAmbient() { return ambient; }
    public FilterPostProcessor getFilterPostProcessor() { return fpp; }
}
