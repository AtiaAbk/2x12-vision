package bd.july2024.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Disposable;

/**
 * AAA Filmic Post-Processing Pipeline for Project July 2024.
 * Features ACES filmic tonemapping, FXAA 3.11 antialiasing, subtle vignette,
 * film grain, and warm Bangladesh summer color grading.
 */
public class PostProcessor implements Disposable {

    private FrameBuffer fbo;
    private ShaderProgram shader;
    private Mesh fullscreenQuad;

    private int width;
    private int height;
    private float time = 0f;

    // Cinematic Grading Configuration
    public float exposure = 1.08f;
    public float contrast = 1.12f;
    public float saturation = 1.06f;
    public float colorTemperature = 0.04f; // Warm summer afternoon sunlight
    public float colorTint = -0.01f;
    public float shadowLift = 0.05f;
    public float vignetteStrength = 0.24f;
    public float filmGrainStrength = 0.02f;
    public boolean enableFxaa = true;

    public PostProcessor(int width, int height) {
        this.width = width > 0 ? width : 1280;
        this.height = height > 0 ? height : 720;

        initFbo();
        initShader();
        initFullscreenQuad();
    }

    private void initFbo() {
        if (fbo != null) {
            fbo.dispose();
        }
        fbo = new FrameBuffer(Format.RGBA8888, width, height, true);
    }

    private void initShader() {
        if (Gdx.files == null) return;
        try {
            if (Gdx.files.internal("shaders/postprocess.vert").exists() &&
                Gdx.files.internal("shaders/postprocess.frag").exists()) {
                String vert = Gdx.files.internal("shaders/postprocess.vert").readString();
                String frag = Gdx.files.internal("shaders/postprocess.frag").readString();
                shader = new ShaderProgram(vert, frag);
                if (!shader.isCompiled()) {
                    Gdx.app.error("PostProcessor", "Shader compile error: " + shader.getLog());
                    shader = null;
                }
            }
        } catch (Throwable t) {
            shader = null;
        }
    }

    private void initFullscreenQuad() {
        fullscreenQuad = new Mesh(true, 4, 6,
            new VertexAttribute(Usage.Position, 2, "a_position"),
            new VertexAttribute(Usage.TextureCoordinates, 2, "a_texCoord0")
        );

        fullscreenQuad.setVertices(new float[]{
            -1.0f, -1.0f, 0.0f, 0.0f,
             1.0f, -1.0f, 1.0f, 0.0f,
             1.0f,  1.0f, 1.0f, 1.0f,
            -1.0f,  1.0f, 0.0f, 1.0f
        });

        fullscreenQuad.setIndices(new short[]{0, 1, 2, 2, 3, 0});
    }

    public void resize(int newWidth, int newHeight) {
        if (newWidth <= 0 || newHeight <= 0) return;
        if (this.width != newWidth || this.height != newHeight) {
            this.width = newWidth;
            this.height = newHeight;
            initFbo();
        }
    }

    public void begin() {
        if (fbo != null) {
            fbo.begin();
            Gdx.gl.glClearColor(0.08f, 0.09f, 0.11f, 1f);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        }
    }

    public void endAndRender(float delta) {
        if (fbo == null) return;
        fbo.end();

        time += delta;

        if (shader != null) {
            Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);
            Gdx.gl.glDepthMask(false);

            shader.bind();
            fbo.getColorBufferTexture().bind(0);

            shader.setUniformi("u_texture", 0);
            shader.setUniform2fv("u_resolution", new float[]{width, height}, 0, 2);

            // Photographic exposure & color grading
            shader.setUniformf("u_exposure", exposure);
            shader.setUniformf("u_contrast", contrast);
            shader.setUniformf("u_saturation", saturation);
            shader.setUniformf("u_colorTemperature", colorTemperature);
            shader.setUniformf("u_colorTint", colorTint);
            shader.setUniformf("u_shadowLift", shadowLift);

            // Optics & vignette
            shader.setUniformf("u_bloomStrength", 0.0f);
            shader.setUniformf("u_vignetteStrength", vignetteStrength);
            shader.setUniformf("u_vignetteRadius", 0.85f);
            shader.setUniformf("u_filmGrainStrength", filmGrainStrength);

            // Anti-aliasing & ACES tone mapping
            shader.setUniformf("u_enableFxaa", enableFxaa ? 1.0f : 0.0f);
            shader.setUniformi("u_toneMappingMode", 1); // 1 = ACES Filmic
            shader.setUniformf("u_time", time);

            fullscreenQuad.render(shader, GL20.GL_TRIANGLES);

            Gdx.gl.glDepthMask(true);
            Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
        }
    }

    public FrameBuffer getFrameBuffer() {
        return fbo;
    }

    @Override
    public void dispose() {
        if (fbo != null) { fbo.dispose(); fbo = null; }
        if (shader != null) { shader.dispose(); shader = null; }
        if (fullscreenQuad != null) { fullscreenQuad.dispose(); fullscreenQuad = null; }
    }
}
