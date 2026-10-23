/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.jme3.input.JoyInput
 *  com.jme3.input.KeyInput
 *  com.jme3.input.MouseInput
 *  com.jme3.input.TouchInput
 *  com.jme3.math.Vector2f
 *  com.jme3.system.AppSettings
 *  com.jme3.system.JmeContext$Type
 *  com.jme3.system.JmeSystem
 *  com.jme3.system.NanoTimer
 *  com.jme3.util.BufferUtils
 *  com.jme3.util.SafeArrayList
 *  org.lwjgl.Version
 *  org.lwjgl.glfw.GLFW
 *  org.lwjgl.glfw.GLFWErrorCallback
 *  org.lwjgl.glfw.GLFWErrorCallbackI
 *  org.lwjgl.glfw.GLFWFramebufferSizeCallback
 *  org.lwjgl.glfw.GLFWFramebufferSizeCallbackI
 *  org.lwjgl.glfw.GLFWImage
 *  org.lwjgl.glfw.GLFWImage$Buffer
 *  org.lwjgl.glfw.GLFWVidMode
 *  org.lwjgl.glfw.GLFWWindowFocusCallback
 *  org.lwjgl.glfw.GLFWWindowFocusCallbackI
 *  org.lwjgl.glfw.GLFWWindowSizeCallback
 *  org.lwjgl.glfw.GLFWWindowSizeCallbackI
 *  org.lwjgl.system.Platform
 *  org.lwjgl.system.Struct
 */
package com.jme3.system.lwjgl;

import com.jme3.input.JoyInput;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.TouchInput;
import com.jme3.input.lwjgl.GlfwJoystickInput;
import com.jme3.input.lwjgl.GlfwKeyInput;
import com.jme3.input.lwjgl.GlfwMouseInput;
import com.jme3.math.Vector2f;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeContext;
import com.jme3.system.JmeSystem;
import com.jme3.system.NanoTimer;
import com.jme3.system.lwjgl.LwjglContext;
import com.jme3.system.lwjgl.Sync;
import com.jme3.system.lwjgl.WindowSizeListener;
import com.jme3.util.BufferUtils;
import com.jme3.util.SafeArrayList;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWErrorCallbackI;
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.glfw.GLFWFramebufferSizeCallbackI;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowFocusCallback;
import org.lwjgl.glfw.GLFWWindowFocusCallbackI;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.glfw.GLFWWindowSizeCallbackI;
import org.lwjgl.system.Platform;
import org.lwjgl.system.Struct;

public abstract class LwjglWindow
extends LwjglContext
implements Runnable {
    private static final Logger LOGGER = Logger.getLogger(LwjglWindow.class.getName());
    private static final EnumSet<JmeContext.Type> SUPPORTED_TYPES = EnumSet.of(JmeContext.Type.Display, JmeContext.Type.Canvas, JmeContext.Type.OffscreenSurface);
    private static final Map<String, Runnable> RENDER_CONFIGS = new HashMap<String, Runnable>();
    protected final AtomicBoolean needClose = new AtomicBoolean(false);
    protected final AtomicBoolean needRestart = new AtomicBoolean(false);
    private final JmeContext.Type type;
    private final SafeArrayList<WindowSizeListener> windowSizeListeners = new SafeArrayList(WindowSizeListener.class);
    private GLFWErrorCallback errorCallback;
    private GLFWWindowSizeCallback windowSizeCallback;
    private GLFWFramebufferSizeCallback framebufferSizeCallback;
    private GLFWWindowFocusCallback windowFocusCallback;
    private Thread mainThread;
    private long window = 0L;
    private int frameRateLimit = -1;
    protected boolean wasActive = false;
    protected boolean autoFlush = true;
    protected boolean allowSwapBuffers = false;
    private final int[] width = new int[1];
    private final int[] height = new int[1];
    private int oldFramebufferWidth;
    private int oldFramebufferHeight;
    private final Vector2f oldScale = new Vector2f(1.0f, 1.0f);

    public LwjglWindow(JmeContext.Type type) {
        if (!SUPPORTED_TYPES.contains(type)) {
            throw new IllegalArgumentException("Unsupported type '" + type.name() + "' provided");
        }
        this.type = type;
    }

    public void registerWindowSizeListener(WindowSizeListener listener) {
        this.windowSizeListeners.add(listener);
    }

    public void removeWindowSizeListener(WindowSizeListener listener) {
        this.windowSizeListeners.remove(listener);
    }

    public JmeContext.Type getType() {
        return this.type;
    }

    public void setTitle(String title) {
        if (this.created.get() && this.window != 0L) {
            GLFW.glfwSetWindowTitle((long)this.window, (CharSequence)title);
        }
    }

    public void restart() {
        if (this.created.get()) {
            this.needRestart.set(true);
        } else {
            LOGGER.warning("Display is not created, cannot restart window.");
        }
    }

    protected void createContext(AppSettings settings) {
        this.errorCallback = new GLFWErrorCallback(){

            public void invoke(int error, long description) {
                String message = GLFWErrorCallback.getDescription((long)description);
                LwjglWindow.this.listener.handleError(message, (Throwable)new Exception(message));
            }
        };
        GLFW.glfwSetErrorCallback((GLFWErrorCallbackI)this.errorCallback);
        if (!GLFW.glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }
        GLFW.glfwDefaultWindowHints();
        String renderer = settings.getRenderer();
        GLFW.glfwWindowHint((int)139270, (int)1);
        GLFW.glfwWindowHint((int)139272, (int)204801);
        RENDER_CONFIGS.computeIfAbsent(renderer, s -> () -> {
            GLFW.glfwWindowHint((int)139270, (int)0);
            GLFW.glfwWindowHint((int)139272, (int)0);
            GLFW.glfwWindowHint((int)139266, (int)2);
            GLFW.glfwWindowHint((int)139267, (int)0);
        }).run();
        if (settings.getBoolean("RendererDebug")) {
            GLFW.glfwWindowHint((int)139271, (int)1);
        }
        if (settings.isGammaCorrection()) {
            GLFW.glfwWindowHint((int)135182, (int)1);
        }
        GLFW.glfwWindowHint((int)131076, (int)0);
        GLFW.glfwWindowHint((int)131075, (int)(settings.isResizable() ? 1 : 0));
        GLFW.glfwWindowHint((int)135173, (int)settings.getDepthBits());
        GLFW.glfwWindowHint((int)135174, (int)settings.getStencilBits());
        GLFW.glfwWindowHint((int)135181, (int)settings.getSamples());
        GLFW.glfwWindowHint((int)135180, (int)(settings.useStereo3D() ? 1 : 0));
        GLFW.glfwWindowHint((int)135183, (int)(settings.getFrequency() <= 0 ? -1 : settings.getFrequency()));
        GLFW.glfwWindowHint((int)143361, (int)(settings.isUseRetinaFrameBuffer() ? 1 : 0));
        if (settings.getBitsPerPixel() == 24) {
            GLFW.glfwWindowHint((int)135169, (int)8);
            GLFW.glfwWindowHint((int)135170, (int)8);
            GLFW.glfwWindowHint((int)135171, (int)8);
        } else if (settings.getBitsPerPixel() == 16) {
            GLFW.glfwWindowHint((int)135169, (int)5);
            GLFW.glfwWindowHint((int)135170, (int)6);
            GLFW.glfwWindowHint((int)135171, (int)5);
        }
        GLFW.glfwWindowHint((int)135172, (int)settings.getAlphaBits());
        long primaryMonitor = GLFW.glfwGetPrimaryMonitor();
        long monitor = 0L;
        if (settings.isFullscreen()) {
            monitor = primaryMonitor;
        }
        GLFWVidMode videoMode = primaryMonitor != 0L ? GLFW.glfwGetVideoMode(primaryMonitor) : null;
        int requestWidth = settings.getWindowWidth() > 0 ? settings.getWindowWidth() : settings.getWidth();
        int requestHeight = settings.getWindowHeight() > 0 ? settings.getWindowHeight() : settings.getHeight();
        if (requestWidth <= 0 || requestHeight <= 0) {
            requestWidth = videoMode != null ? videoMode.width() : 1280;
            requestHeight = videoMode != null ? videoMode.height() : 720;
        }
        this.window = GLFW.glfwCreateWindow((int)requestWidth, (int)requestHeight, (CharSequence)settings.getTitle(), (long)monitor, (long)0L);
        if (this.window == 0L) {
            throw new RuntimeException("Failed to create the GLFW window");
        }
        this.windowFocusCallback = new GLFWWindowFocusCallback(){

            public void invoke(long window, boolean focus) {
                if (LwjglWindow.this.wasActive != focus) {
                    if (!LwjglWindow.this.wasActive) {
                        LwjglWindow.this.listener.gainFocus();
                        LwjglWindow.this.timer.reset();
                    } else {
                        LwjglWindow.this.listener.loseFocus();
                    }
                    LwjglWindow.this.wasActive = !LwjglWindow.this.wasActive;
                }
            }
        };
        GLFW.glfwSetWindowFocusCallback((long)this.window, (GLFWWindowFocusCallbackI)this.windowFocusCallback);
        if (!settings.isFullscreen()) {
            if (settings.getCenterWindow() && videoMode != null) {
                GLFW.glfwSetWindowPos((long)this.window, (int)((videoMode.width() - requestWidth) / 2), (int)((videoMode.height() - requestHeight) / 2));
            } else {
                GLFW.glfwSetWindowPos((long)this.window, (int)settings.getWindowXPosition(), (int)settings.getWindowYPosition());
            }
        }
        GLFW.glfwMakeContextCurrent((long)this.window);
        if (settings.isVSync()) {
            GLFW.glfwSwapInterval((int)1);
        } else {
            GLFW.glfwSwapInterval((int)0);
        }
        this.setWindowIcon(settings);
        this.showWindow();
        for (int i = 0; i < 4; ++i) {
            GLFW.glfwPollEvents();
        }
        this.windowSizeCallback = new GLFWWindowSizeCallback(){

            public void invoke(long window, int width, int height) {
                LwjglWindow.this.updateSizes();
            }
        };
        GLFW.glfwSetWindowSizeCallback((long)this.window, (GLFWWindowSizeCallbackI)this.windowSizeCallback);
        this.framebufferSizeCallback = new GLFWFramebufferSizeCallback(){

            public void invoke(long window, int width, int height) {
                LwjglWindow.this.updateSizes();
            }
        };
        GLFW.glfwSetFramebufferSizeCallback((long)this.window, (GLFWFramebufferSizeCallbackI)this.framebufferSizeCallback);
        this.allowSwapBuffers = settings.isSwapBuffers();
        if (settings.isOpenCLSupport()) {
            this.initOpenCL(this.window);
        }
        this.updateSizes();
    }

    private void updateSizes() {
        int windowHeight;
        GLFW.glfwGetWindowSize((long)this.window, (int[])this.width, (int[])this.height);
        int windowWidth = this.width[0] < 1 ? 1 : this.width[0];
        int n = windowHeight = this.height[0] < 1 ? 1 : this.height[0];
        if (this.settings.getWindowWidth() != windowWidth || this.settings.getWindowHeight() != windowHeight) {
            this.settings.setWindowSize(windowWidth, windowHeight);
            for (WindowSizeListener wsListener : (WindowSizeListener[])this.windowSizeListeners.getArray()) {
                wsListener.onWindowSizeChanged(windowWidth, windowHeight);
            }
        }
        GLFW.glfwGetFramebufferSize((long)this.window, (int[])this.width, (int[])this.height);
        int framebufferWidth = this.width[0];
        int framebufferHeight = this.height[0];
        if (framebufferWidth != this.oldFramebufferWidth || framebufferHeight != this.oldFramebufferHeight) {
            this.settings.setResolution(framebufferWidth, framebufferHeight);
            this.listener.reshape(framebufferWidth, framebufferHeight);
            this.oldFramebufferWidth = framebufferWidth;
            this.oldFramebufferHeight = framebufferHeight;
        }
        float xScale = framebufferWidth / windowWidth;
        float yScale = framebufferHeight / windowHeight;
        if (this.oldScale.x != xScale || this.oldScale.y != yScale) {
            this.listener.rescale(xScale, yScale);
            this.oldScale.set(xScale, yScale);
        }
    }

    protected void showWindow() {
        GLFW.glfwShowWindow((long)this.window);
    }

    protected void setWindowIcon(AppSettings settings) {
        Object[] icons = settings.getIcons();
        if (icons == null) {
            return;
        }
        GLFWImage[] images = this.imagesToGLFWImages(icons);
        try (GLFWImage.Buffer iconSet = GLFWImage.malloc((int)images.length);){
            for (int i = images.length - 1; i >= 0; --i) {
                GLFWImage image = images[i];
                iconSet.put(i, image);
            }
            GLFW.glfwSetWindowIcon((long)this.window, (GLFWImage.Buffer)iconSet);
        }
    }

    private GLFWImage[] imagesToGLFWImages(Object[] images) {
        GLFWImage[] out = new GLFWImage[images.length];
        for (int i = 0; i < images.length; ++i) {
            BufferedImage image = (BufferedImage)images[i];
            out[i] = this.imageToGLFWImage(image);
        }
        return out;
    }

    private GLFWImage imageToGLFWImage(BufferedImage image) {
        if (image.getType() != 3) {
            BufferedImage convertedImage = new BufferedImage(image.getWidth(), image.getHeight(), 3);
            Graphics2D graphics = convertedImage.createGraphics();
            int targetWidth = image.getWidth();
            int targetHeight = image.getHeight();
            graphics.drawImage(image, 0, 0, targetWidth, targetHeight, null);
            graphics.dispose();
            image = convertedImage;
        }
        ByteBuffer buffer = BufferUtils.createByteBuffer((int)(image.getWidth() * image.getHeight() * 4));
        for (int i = 0; i < image.getHeight(); ++i) {
            for (int j = 0; j < image.getWidth(); ++j) {
                int colorSpace = image.getRGB(j, i);
                buffer.put((byte)(colorSpace << 8 >> 24));
                buffer.put((byte)(colorSpace << 16 >> 24));
                buffer.put((byte)(colorSpace << 24 >> 24));
                buffer.put((byte)(colorSpace >> 24));
            }
        }
        buffer.flip();
        GLFWImage result = GLFWImage.create();
        result.set(image.getWidth(), image.getHeight(), buffer);
        return result;
    }

    protected void destroyContext() {
        try {
            if (this.renderer != null) {
                this.renderer.cleanup();
            }
            if (this.errorCallback != null) {
                GLFW.glfwSetErrorCallback(null);
                this.errorCallback.close();
                this.errorCallback = null;
            }
            if (this.windowSizeCallback != null) {
                this.windowSizeCallback.close();
                this.windowSizeCallback = null;
            }
            if (this.framebufferSizeCallback != null) {
                this.framebufferSizeCallback.close();
                this.framebufferSizeCallback = null;
            }
            if (this.windowFocusCallback != null) {
                this.windowFocusCallback.close();
                this.windowFocusCallback = null;
            }
            if (this.window != 0L) {
                GLFW.glfwDestroyWindow((long)this.window);
                this.window = 0L;
            }
        }
        catch (Exception ex) {
            this.listener.handleError("Failed to destroy context", (Throwable)ex);
        }
    }

    public void create(boolean waitFor) {
        if (this.created.get()) {
            LOGGER.warning("create() called when display is already created!");
            return;
        }
        if (Platform.get() == Platform.MACOSX) {
            this.mainThread = Thread.currentThread();
            this.mainThread.setName("jME3 Main");
            if (waitFor) {
                LOGGER.warning("create(true) is not supported for macOS!");
            }
            this.run();
        } else {
            this.mainThread = new Thread((Runnable)this, "jME3 Main");
            this.mainThread.start();
            if (waitFor) {
                this.waitFor(true);
            }
        }
    }

    protected boolean initInThread() {
        try {
            if (!JmeSystem.isLowPermissions()) {
                Thread.currentThread().setUncaughtExceptionHandler((thread, thrown) -> {
                    this.listener.handleError("Uncaught exception thrown in " + thread.toString(), thrown);
                    if (this.needClose.get()) {
                        this.deinitInThread();
                    }
                });
            }
            this.timer = new NanoTimer();
            this.createContext(this.settings);
            this.printContextInitInfo();
            this.created.set(true);
            super.internalCreate();
        }
        catch (Exception ex) {
            try {
                if (this.window != 0L) {
                    GLFW.glfwDestroyWindow((long)this.window);
                    this.window = 0L;
                }
            }
            catch (Exception ex2) {
                LOGGER.log(Level.WARNING, null, ex2);
            }
            this.listener.handleError("Failed to create display", (Throwable)ex);
            return false;
        }
        this.listener.initialize();
        this.updateSizes();
        return true;
    }

    protected void runLoop() {
        if (this.needRestart.getAndSet(false)) {
            this.restartContext();
        }
        if (!this.created.get()) {
            throw new IllegalStateException();
        }
        this.listener.update();
        if (this.renderable.get()) {
            try {
                if (this.type != JmeContext.Type.Canvas && this.allowSwapBuffers && this.autoFlush) {
                    GLFW.glfwSwapBuffers((long)this.window);
                }
            }
            catch (Throwable ex) {
                this.listener.handleError("Error while swapping buffers", ex);
            }
        }
        if (this.renderer != null) {
            this.renderer.postFrame();
        }
        if (this.autoFlush) {
            if (this.frameRateLimit != this.getSettings().getFrameRate()) {
                this.setFrameRateLimit(this.getSettings().getFrameRate());
            }
        } else if (this.frameRateLimit != 20) {
            this.setFrameRateLimit(20);
        }
        Sync.sync(this.frameRateLimit);
        GLFW.glfwPollEvents();
    }

    private void restartContext() {
        try {
            this.destroyContext();
            this.createContext(this.settings);
        }
        catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Failed to set display settings!", ex);
        }
        this.reinitContext();
        if (this.keyInput != null && this.keyInput.isInitialized()) {
            this.keyInput.resetContext();
        }
        if (this.mouseInput != null && this.mouseInput.isInitialized()) {
            this.mouseInput.resetContext();
        }
        LOGGER.fine("Display restarted.");
    }

    private void setFrameRateLimit(int frameRateLimit) {
        this.frameRateLimit = frameRateLimit;
    }

    protected void deinitInThread() {
        this.listener.destroy();
        this.destroyContext();
        super.internalDestroy();
        GLFW.glfwTerminate();
        LOGGER.fine("Display destroyed.");
    }

    @Override
    public void run() {
        if (this.listener == null) {
            throw new IllegalStateException("SystemListener is not set on context!Must set with JmeContext.setSystemListener().");
        }
        LOGGER.log(Level.FINE, "Using LWJGL {0}", Version.getVersion());
        if (!this.initInThread()) {
            LOGGER.log(Level.SEVERE, "Display initialization failed. Cannot continue.");
            return;
        }
        while (true) {
            this.runLoop();
            if (this.needClose.get()) break;
            if (!GLFW.glfwWindowShouldClose((long)this.window)) continue;
            this.listener.requestClose(false);
        }
        this.deinitInThread();
    }

    public JoyInput getJoyInput() {
        if (this.joyInput == null) {
            this.joyInput = new GlfwJoystickInput();
        }
        return this.joyInput;
    }

    public MouseInput getMouseInput() {
        if (this.mouseInput == null) {
            this.mouseInput = new GlfwMouseInput(this);
        }
        return this.mouseInput;
    }

    public KeyInput getKeyInput() {
        if (this.keyInput == null) {
            this.keyInput = new GlfwKeyInput(this);
        }
        return this.keyInput;
    }

    public TouchInput getTouchInput() {
        return null;
    }

    public void setAutoFlushFrames(boolean enabled) {
        this.autoFlush = enabled;
    }

    public void destroy(boolean waitFor) {
        this.needClose.set(true);
        if (this.mainThread == Thread.currentThread()) {
            return;
        }
        if (waitFor) {
            this.waitFor(false);
        }
    }

    public long getWindowHandle() {
        return this.window;
    }

    public Vector2f getWindowContentScale(Vector2f store) {
        if (store == null) {
            store = new Vector2f();
        }
        GLFW.glfwGetFramebufferSize((long)this.window, (int[])this.width, (int[])this.height);
        store.set((float)this.width[0], (float)this.height[0]);
        GLFW.glfwGetWindowSize((long)this.window, (int[])this.width, (int[])this.height);
        store.x /= (float)this.width[0];
        store.y /= (float)this.height[0];
        return store;
    }

    public int getFramebufferHeight() {
        GLFW.glfwGetFramebufferSize((long)this.window, (int[])this.width, (int[])this.height);
        int result = this.height[0];
        return result;
    }

    public int getFramebufferWidth() {
        GLFW.glfwGetFramebufferSize((long)this.window, (int[])this.width, (int[])this.height);
        int result = this.width[0];
        return result;
    }

    public int getWindowXPosition() {
        GLFW.glfwGetWindowPos((long)this.window, (int[])this.width, (int[])this.height);
        int result = this.width[0];
        return result;
    }

    public int getWindowYPosition() {
        GLFW.glfwGetWindowPos((long)this.window, (int[])this.width, (int[])this.height);
        int result = this.height[0];
        return result;
    }

    static {
        RENDER_CONFIGS.put("LWJGL-OpenGL30", () -> {
            GLFW.glfwWindowHint((int)139272, (int)0);
            GLFW.glfwWindowHint((int)139266, (int)3);
            GLFW.glfwWindowHint((int)139267, (int)0);
        });
        RENDER_CONFIGS.put("LWJGL-OpenGL31", () -> {
            GLFW.glfwWindowHint((int)139272, (int)0);
            GLFW.glfwWindowHint((int)139266, (int)3);
            GLFW.glfwWindowHint((int)139267, (int)1);
        });
        RENDER_CONFIGS.put("LWJGL-OpenGL3", () -> {
            GLFW.glfwWindowHint((int)139266, (int)3);
            GLFW.glfwWindowHint((int)139267, (int)2);
        });
        RENDER_CONFIGS.put("LWJGL-OpenGL33", () -> {
            GLFW.glfwWindowHint((int)139266, (int)3);
            GLFW.glfwWindowHint((int)139267, (int)3);
        });
        RENDER_CONFIGS.put("LWJGL-OpenGL4", () -> {
            GLFW.glfwWindowHint((int)139266, (int)4);
            GLFW.glfwWindowHint((int)139267, (int)0);
        });
        RENDER_CONFIGS.put("LWJGL-OpenGL41", () -> {
            GLFW.glfwWindowHint((int)139266, (int)4);
            GLFW.glfwWindowHint((int)139267, (int)1);
        });
        RENDER_CONFIGS.put("LWJGL-OpenGL42", () -> {
            GLFW.glfwWindowHint((int)139266, (int)4);
            GLFW.glfwWindowHint((int)139267, (int)2);
        });
        RENDER_CONFIGS.put("LWJGL-OpenGL43", () -> {
            GLFW.glfwWindowHint((int)139266, (int)4);
            GLFW.glfwWindowHint((int)139267, (int)3);
        });
        RENDER_CONFIGS.put("LWJGL-OpenGL44", () -> {
            GLFW.glfwWindowHint((int)139266, (int)4);
            GLFW.glfwWindowHint((int)139267, (int)4);
        });
        RENDER_CONFIGS.put("LWJGL-OpenGL45", () -> {
            GLFW.glfwWindowHint((int)139266, (int)4);
            GLFW.glfwWindowHint((int)139267, (int)5);
        });
    }
}

