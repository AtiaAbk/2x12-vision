/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.jme3.opencl.Context
 *  com.jme3.opencl.DefaultPlatformChooser
 *  com.jme3.opencl.Device
 *  com.jme3.opencl.PlatformChooser
 *  com.jme3.renderer.Renderer
 *  com.jme3.renderer.RendererException
 *  com.jme3.renderer.opengl.GL
 *  com.jme3.renderer.opengl.GL2
 *  com.jme3.renderer.opengl.GL3
 *  com.jme3.renderer.opengl.GL4
 *  com.jme3.renderer.opengl.GLDebug
 *  com.jme3.renderer.opengl.GLExt
 *  com.jme3.renderer.opengl.GLFbo
 *  com.jme3.renderer.opengl.GLRenderer
 *  com.jme3.renderer.opengl.GLTiming
 *  com.jme3.renderer.opengl.GLTimingState
 *  com.jme3.renderer.opengl.GLTracer
 *  com.jme3.system.AppSettings
 *  com.jme3.system.JmeContext
 *  com.jme3.system.SystemListener
 *  com.jme3.system.Timer
 *  org.lwjgl.PointerBuffer
 *  org.lwjgl.Version
 *  org.lwjgl.glfw.GLFW
 *  org.lwjgl.glfw.GLFWJoystickCallback
 *  org.lwjgl.glfw.GLFWJoystickCallbackI
 *  org.lwjgl.glfw.GLFWNativeGLX
 *  org.lwjgl.glfw.GLFWNativeWGL
 *  org.lwjgl.glfw.GLFWNativeX11
 *  org.lwjgl.opencl.CL10
 *  org.lwjgl.opengl.ARBDebugOutput
 *  org.lwjgl.opengl.CGL
 *  org.lwjgl.opengl.GL
 *  org.lwjgl.opengl.GL11
 *  org.lwjgl.opengl.GLCapabilities
 *  org.lwjgl.opengl.GLDebugMessageARBCallbackI
 *  org.lwjgl.opengl.WGL
 *  org.lwjgl.system.MemoryStack
 *  org.lwjgl.system.Platform
 */
package com.jme3.system.lwjgl;

import com.jme3.input.lwjgl.GlfwJoystickInput;
import com.jme3.input.lwjgl.GlfwKeyInput;
import com.jme3.input.lwjgl.GlfwMouseInput;
import com.jme3.lwjgl3.utils.APIUtil;
import com.jme3.opencl.Context;
import com.jme3.opencl.DefaultPlatformChooser;
import com.jme3.opencl.Device;
import com.jme3.opencl.PlatformChooser;
import com.jme3.opencl.lwjgl.LwjglDevice;
import com.jme3.opencl.lwjgl.LwjglPlatform;
import com.jme3.opencl.lwjgl.Utils;
import com.jme3.renderer.Renderer;
import com.jme3.renderer.RendererException;
import com.jme3.renderer.lwjgl.LwjglGL;
import com.jme3.renderer.lwjgl.LwjglGLExt;
import com.jme3.renderer.lwjgl.LwjglGLFboEXT;
import com.jme3.renderer.lwjgl.LwjglGLFboGL3;
import com.jme3.renderer.lwjgl.LwjglRender;
import com.jme3.renderer.opengl.GL2;
import com.jme3.renderer.opengl.GL3;
import com.jme3.renderer.opengl.GL4;
import com.jme3.renderer.opengl.GLDebug;
import com.jme3.renderer.opengl.GLExt;
import com.jme3.renderer.opengl.GLFbo;
import com.jme3.renderer.opengl.GLRenderer;
import com.jme3.renderer.opengl.GLTiming;
import com.jme3.renderer.opengl.GLTimingState;
import com.jme3.renderer.opengl.GLTracer;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeContext;
import com.jme3.system.SystemListener;
import com.jme3.system.Timer;
import com.jme3.system.lwjgl.LwjglGLDebugOutputHandler;
import com.jme3.util.LWJGLBufferAllocator;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.lwjgl.PointerBuffer;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWJoystickCallback;
import org.lwjgl.glfw.GLFWJoystickCallbackI;
import org.lwjgl.glfw.GLFWNativeGLX;
import org.lwjgl.glfw.GLFWNativeWGL;
import org.lwjgl.glfw.GLFWNativeX11;
import org.lwjgl.opencl.CL10;
import org.lwjgl.opengl.ARBDebugOutput;
import org.lwjgl.opengl.CGL;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLCapabilities;
import org.lwjgl.opengl.GLDebugMessageARBCallbackI;
import org.lwjgl.opengl.WGL;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.Platform;

public abstract class LwjglContext
implements JmeContext {
    private static final Logger logger = Logger.getLogger(LwjglContext.class.getName());
    private static final Set<String> SUPPORTED_RENDERS;
    public static final boolean CL_GL_SHARING_POSSIBLE = true;
    protected final Object createdLock = new Object();
    protected final AtomicBoolean created = new AtomicBoolean(false);
    protected final AtomicBoolean renderable = new AtomicBoolean(false);
    protected final AppSettings settings = new AppSettings(true);
    protected GlfwKeyInput keyInput;
    protected GlfwMouseInput mouseInput;
    protected GlfwJoystickInput joyInput;
    protected Timer timer;
    protected Renderer renderer;
    protected SystemListener listener;
    protected com.jme3.opencl.lwjgl.LwjglContext clContext;

    public SystemListener getSystemListener() {
        return this.listener;
    }

    public void setSystemListener(SystemListener listener) {
        this.listener = listener;
    }

    protected void printContextInitInfo() {
        if (logger.isLoggable(Level.INFO)) {
            logger.log(Level.INFO, "LWJGL {0} context running on thread {1}\n * Graphics Adapter: GLFW {2}", APIUtil.toArray(Version.getVersion(), Thread.currentThread().getName(), GLFW.glfwGetVersionString()));
        }
    }

    protected int determineMaxSamples() {
        if (GLFW.glfwExtensionSupported((CharSequence)"GL_ARB_framebuffer_object")) {
            return GL11.glGetInteger((int)36183);
        }
        if (GLFW.glfwExtensionSupported((CharSequence)"GL_EXT_framebuffer_multisample")) {
            return GL11.glGetInteger((int)36183);
        }
        return Integer.MAX_VALUE;
    }

    protected int getNumSamplesToUse() {
        int samples = 0;
        if (this.settings.getSamples() > 1) {
            samples = this.settings.getSamples();
            int supportedSamples = this.determineMaxSamples();
            if (supportedSamples < samples) {
                logger.log(Level.WARNING, "Couldn't satisfy antialiasing samples requirement: x{0}. Video hardware only supports: x{1}", APIUtil.toArray(samples, supportedSamples));
                samples = supportedSamples;
            }
        }
        return samples;
    }

    protected void reinitContext() {
        this.initContext(false);
    }

    protected void initContextFirstTime() {
        this.initContext(true);
    }

    private void initContext(boolean first) {
        String renderer = this.settings.getRenderer();
        GLCapabilities capabilities = GL.createCapabilities((!renderer.equals("LWJGL-OpenGL2") ? 1 : 0) != 0);
        if (!capabilities.OpenGL20) {
            throw new RendererException("OpenGL 2.0 or higher is required for jMonkeyEngine");
        }
        if (!SUPPORTED_RENDERS.contains(renderer)) {
            throw new UnsupportedOperationException("Unsupported renderer: " + renderer);
        }
        if (first) {
            com.jme3.renderer.opengl.GL gl = new LwjglGL();
            GLExt glext = new LwjglGLExt();
            GLFbo glfbo = (capabilities.OpenGL30 || capabilities.OpenGL32 || capabilities.OpenGL41) ? new LwjglGLFboGL3() : new LwjglGLFboEXT();
            System.out.println("[LwjglContext] capabilities.OpenGL30=" + capabilities.OpenGL30 + ", OpenGL32=" + capabilities.OpenGL32 + ", glfbo=" + glfbo.getClass().getName());
            if (this.settings.isGraphicsDebug()) {
                gl = (com.jme3.renderer.opengl.GL)GLDebug.createProxy((com.jme3.renderer.opengl.GL)gl, (Object)gl, (Class[])new Class[]{com.jme3.renderer.opengl.GL.class, GL2.class, GL3.class, GL4.class});
                glext = (GLExt)GLDebug.createProxy((com.jme3.renderer.opengl.GL)glext, (Object)glext, (Class[])new Class[]{GLExt.class});
                glfbo = (GLFbo)GLDebug.createProxy((com.jme3.renderer.opengl.GL)glfbo, (Object)glfbo, (Class[])new Class[]{GLFbo.class});
            }
            if (this.settings.isGraphicsTiming()) {
                GLTimingState timingState = new GLTimingState();
                gl = (com.jme3.renderer.opengl.GL)GLTiming.createGLTiming((Object)gl, (GLTimingState)timingState, (Class[])new Class[]{com.jme3.renderer.opengl.GL.class, GL2.class, GL3.class, GL4.class});
                glext = (GLExt)GLTiming.createGLTiming((Object)glext, (GLTimingState)timingState, (Class[])new Class[]{GLExt.class});
                glfbo = (GLFbo)GLTiming.createGLTiming((Object)glfbo, (GLTimingState)timingState, (Class[])new Class[]{GLFbo.class});
            }
            if (this.settings.isGraphicsTrace()) {
                gl = (com.jme3.renderer.opengl.GL)GLTracer.createDesktopGlTracer((Object)gl, (Class[])new Class[]{com.jme3.renderer.opengl.GL.class, GL2.class, GL3.class, GL4.class});
                glext = (GLExt)GLTracer.createDesktopGlTracer((Object)glext, (Class[])new Class[]{GLExt.class});
                glfbo = (GLFbo)GLTracer.createDesktopGlTracer((Object)glfbo, (Class[])new Class[]{GLFbo.class});
            }
            this.renderer = new GLRenderer(gl, glext, glfbo);
            if (this.settings.isGraphicsDebug()) {
                ((GLRenderer)this.renderer).setDebugEnabled(true);
            }
        }
        this.renderer.initialize();
        if (capabilities.GL_ARB_debug_output && this.settings.isGraphicsDebug()) {
            ARBDebugOutput.glDebugMessageCallbackARB((GLDebugMessageARBCallbackI)new LwjglGLDebugOutputHandler(), (long)0L);
        }
        this.renderer.setMainFrameBufferSrgb(this.settings.isGammaCorrection());
        this.renderer.setLinearizeSrgbImages(this.settings.isGammaCorrection());
        if (first) {
            if (this.keyInput != null) {
                this.keyInput.initialize();
            }
            if (this.mouseInput != null) {
                this.mouseInput.initialize();
            }
            if (this.joyInput != null) {
                this.joyInput.initialize();
            }
            GLFW.glfwSetJoystickCallback((GLFWJoystickCallbackI)new GLFWJoystickCallback(){

                public void invoke(int jid, int event) {
                    if (event == 262145) {
                        LwjglContext.this.joyInput.reloadJoysticks();
                        LwjglContext.this.joyInput.fireJoystickConnectedEvent(jid);
                    } else {
                        LwjglContext.this.joyInput.fireJoystickDisconnectedEvent(jid);
                        LwjglContext.this.joyInput.reloadJoysticks();
                    }
                }
            });
        }
        this.renderable.set(true);
    }

    private static long[] getPlatforms() {
        try (MemoryStack stack = MemoryStack.stackPush();){
            IntBuffer countBuffer = stack.callocInt(1);
            int errcode = CL10.clGetPlatformIDs(null, (IntBuffer)countBuffer);
            Utils.checkError(errcode, "clGetDeviceIDs");
            int count = countBuffer.get();
            PointerBuffer pointer = stack.callocPointer(count);
            errcode = CL10.clGetPlatformIDs((PointerBuffer)pointer, (IntBuffer)null);
            Utils.checkError(errcode, "clGetDeviceIDs");
            long[] platformIDs = new long[count];
            for (int i = 0; i < count; ++i) {
                platformIDs[i] = pointer.get();
            }
            long[] lArray = platformIDs;
            return lArray;
        }
    }

    protected void initOpenCL(long window) {
        List chosenDevices;
        Optional<Device> unsupportedDevice;
        logger.info("Initialize OpenCL with LWJGL3");
        StringBuilder platformInfos = new StringBuilder();
        ArrayList<LwjglPlatform> platforms = new ArrayList<LwjglPlatform>();
        for (long platformId : LwjglContext.getPlatforms()) {
            platforms.add(new LwjglPlatform(platformId));
        }
        platformInfos.append("Available OpenCL platforms:");
        for (int i = 0; i < platforms.size(); ++i) {
            LwjglPlatform platform = (LwjglPlatform)platforms.get(i);
            platformInfos.append("\n * Platform ").append(i + 1);
            platformInfos.append("\n *   Name: ").append(platform.getName());
            platformInfos.append("\n *   Vendor: ").append(platform.getVendor());
            platformInfos.append("\n *   Version: ").append(platform.getVersion());
            platformInfos.append("\n *   Profile: ").append(platform.getProfile());
            platformInfos.append("\n *   Supports interop: ").append(platform.hasOpenGLInterop());
            List<LwjglDevice> devices = platform.getDevices();
            platformInfos.append("\n *   Available devices:");
            for (int j = 0; j < devices.size(); ++j) {
                LwjglDevice device = devices.get(j);
                platformInfos.append("\n *    * Device ").append(j + 1);
                platformInfos.append("\n *    *   Name: ").append(device.getName());
                platformInfos.append("\n *    *   Vendor: ").append(device.getVendor());
                platformInfos.append("\n *    *   Version: ").append(device.getVersion());
                platformInfos.append("\n *    *   Profile: ").append(device.getProfile());
                platformInfos.append("\n *    *   Compiler version: ").append(device.getCompilerVersion());
                platformInfos.append("\n *    *   Device type: ").append(device.getDeviceType());
                platformInfos.append("\n *    *   Compute units: ").append(device.getComputeUnits());
                platformInfos.append("\n *    *   Work group size: ").append(device.getMaxiumWorkItemsPerGroup());
                platformInfos.append("\n *    *   Global memory: ").append(device.getGlobalMemorySize()).append("B");
                platformInfos.append("\n *    *   Local memory: ").append(device.getLocalMemorySize()).append("B");
                platformInfos.append("\n *    *   Constant memory: ").append(device.getMaximumConstantBufferSize()).append("B");
                platformInfos.append("\n *    *   Supports double: ").append(device.hasDouble());
                platformInfos.append("\n *    *   Supports half floats: ").append(device.hasHalfFloat());
                platformInfos.append("\n *    *   Supports writable 3d images: ").append(device.hasWritableImage3D());
                platformInfos.append("\n *    *   Supports interop: ").append(device.hasOpenGLInterop());
            }
        }
        logger.info(platformInfos.toString());
        PlatformChooser chooser = null;
        if (this.settings.getOpenCLPlatformChooser() != null) {
            try {
                chooser = (PlatformChooser)Class.forName(this.settings.getOpenCLPlatformChooser()).getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
            }
            catch (Exception ex) {
                logger.log(Level.WARNING, "Unable to instantiate custom PlatformChooser", ex);
            }
        }
        if (chooser == null) {
            chooser = new DefaultPlatformChooser();
        }
        if ((unsupportedDevice = (chosenDevices = chooser.chooseDevices(platforms)).stream().filter(dev -> !(dev instanceof LwjglDevice)).findAny()).isPresent()) {
            logger.log(Level.SEVERE, "attempt to return a custom Device implementation from PlatformChooser: {0}", unsupportedDevice.get());
            return;
        }
        Set lwjglPlatforms = (Set) chosenDevices.stream().map(d -> ((LwjglDevice)d).getPlatform()).collect(Collectors.toSet());
        if (lwjglPlatforms.size() != 1) {
            logger.severe("attempt to use devices from different platforms");
            return;
        }
        long[] deviceIds = chosenDevices.stream().mapToLong(d -> ((LwjglDevice)d).getDevice()).toArray();
        if (deviceIds.length < 1) {
            logger.warning("no devices specified, no OpenCL context created");
            return;
        }
        LwjglPlatform platform = (LwjglPlatform)lwjglPlatforms.iterator().next();
        logger.log(Level.INFO, "chosen platform: {0}", platform.getName());
        logger.log(Level.INFO, "chosen devices: {0}", chosenDevices);
        try {
            long context = this.createContext(platform.getPlatform(), deviceIds, window);
            this.clContext = new com.jme3.opencl.lwjgl.LwjglContext(context, chosenDevices);
        }
        catch (Exception ex) {
            logger.log(Level.SEVERE, "Unable to create OpenCL context", ex);
            return;
        }
        logger.info("OpenCL context created");
    }

    private long createContext(long platform, long[] devices, long window) {
        try (MemoryStack stack = MemoryStack.stackPush();){
            int propertyCount = 7;
            PointerBuffer properties = stack.callocPointer(7 + devices.length);
            switch (Platform.get()) {
                case WINDOWS: {
                    properties.put(8200L).put(GLFWNativeWGL.glfwGetWGLContext((long)window)).put(8203L).put(WGL.wglGetCurrentDC());
                    break;
                }
                case LINUX: {
                    properties.put(8200L).put(GLFWNativeGLX.glfwGetGLXContext((long)window)).put(8202L).put(GLFWNativeX11.glfwGetX11Display());
                    break;
                }
                case MACOSX: {
                    properties.put(0x10000000L).put(CGL.CGLGetShareGroup((long)CGL.CGLGetCurrentContext()));
                    break;
                }
            }
            properties.put(4228L).put(platform);
            properties.put(0L);
            properties.flip();
            IntBuffer error = stack.callocInt(1);
            PointerBuffer deviceBuffer = stack.callocPointer(devices.length);
            for (long deviceId : devices) {
                deviceBuffer.put(deviceId);
            }
            deviceBuffer.flip();
            long context = CL10.clCreateContext((PointerBuffer)properties, (PointerBuffer)deviceBuffer, null, (long)0L, (IntBuffer)error);
            Utils.checkError(error, "clCreateContext");
            long l = context;
            return l;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void internalDestroy() {
        this.renderer = null;
        this.timer = null;
        this.renderable.set(false);
        Object object = this.createdLock;
        synchronized (object) {
            this.created.set(false);
            this.createdLock.notifyAll();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void internalCreate() {
        Object object = this.createdLock;
        synchronized (object) {
            this.created.set(true);
            this.createdLock.notifyAll();
        }
        this.initContextFirstTime();
    }

    public void create() {
        this.create(false);
    }

    public void destroy() {
        this.destroy(false);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    protected void waitFor(boolean createdVal) {
        Object object = this.createdLock;
        synchronized (object) {
            while (this.created.get() != createdVal) {
                try {
                    this.createdLock.wait();
                }
                catch (InterruptedException interruptedException) {}
            }
        }
    }

    public boolean isCreated() {
        return this.created.get();
    }

    public boolean isRenderable() {
        return this.renderable.get();
    }

    public void setSettings(AppSettings settings) {
        this.settings.copyFrom(settings);
    }

    public AppSettings getSettings() {
        return this.settings;
    }

    public Renderer getRenderer() {
        return this.renderer;
    }

    public Timer getTimer() {
        return this.timer;
    }

    public Context getOpenCLContext() {
        return this.clContext;
    }

    static {
        String implementation = "com.jme3.BufferAllocatorImplementation";
        if (System.getProperty("com.jme3.BufferAllocatorImplementation") == null) {
            if (Boolean.parseBoolean(System.getProperty("com.jme3.lwjgl3.ConcurrentBufferAllocator", "true"))) {
                System.setProperty("com.jme3.BufferAllocatorImplementation", LWJGLBufferAllocator.ConcurrentLWJGLBufferAllocator.class.getName());
            } else {
                System.setProperty("com.jme3.BufferAllocatorImplementation", LWJGLBufferAllocator.class.getName());
            }
        }
        SUPPORTED_RENDERS = new HashSet<String>(Arrays.asList("LWJGL-OpenGL2", "LWJGL-OpenGL30", "LWJGL-OpenGL31", "LWJGL-OpenGL3", "LWJGL-OpenGL33", "LWJGL-OpenGL4", "LWJGL-OpenGL41", "LWJGL-OpenGL42", "LWJGL-OpenGL43", "LWJGL-OpenGL44", "LWJGL-OpenGL45"));
    }
}

