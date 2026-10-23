/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.jme3.renderer.opengl.GLFbo
 *  org.lwjgl.opengl.GL30
 */
package com.jme3.renderer.lwjgl;

import com.jme3.renderer.lwjgl.LwjglRender;
import com.jme3.renderer.opengl.GLFbo;
import java.nio.IntBuffer;
import org.lwjgl.opengl.GL30;

public class LwjglGLFboGL3
extends LwjglRender
implements GLFbo {
    public void glBlitFramebufferEXT(int srcX0, int srcY0, int srcX1, int srcY1, int dstX0, int dstY0, int dstX1, int dstY1, int mask, int filter) {
        GL30.glBlitFramebuffer((int)srcX0, (int)srcY0, (int)srcX1, (int)srcY1, (int)dstX0, (int)dstY0, (int)dstX1, (int)dstY1, (int)mask, (int)filter);
    }

    public void glRenderbufferStorageMultisampleEXT(int target, int samples, int internalFormat, int width, int height) {
        GL30.glRenderbufferStorageMultisample((int)target, (int)samples, (int)internalFormat, (int)width, (int)height);
    }

    public void glBindFramebufferEXT(int target, int frameBuffer) {
        GL30.glBindFramebuffer((int)target, (int)frameBuffer);
    }

    public void glBindRenderbufferEXT(int target, int renderBuffer) {
        GL30.glBindRenderbuffer((int)target, (int)renderBuffer);
    }

    public int glCheckFramebufferStatusEXT(int target) {
        return GL30.glCheckFramebufferStatus((int)target);
    }

    public void glDeleteFramebuffersEXT(IntBuffer frameBuffers) {
        LwjglGLFboGL3.checkLimit(frameBuffers);
        GL30.glDeleteFramebuffers((IntBuffer)frameBuffers);
    }

    public void glDeleteRenderbuffersEXT(IntBuffer renderBuffers) {
        LwjglGLFboGL3.checkLimit(renderBuffers);
        GL30.glDeleteRenderbuffers((IntBuffer)renderBuffers);
    }

    public void glFramebufferRenderbufferEXT(int target, int attachment, int renderBufferTarget, int renderBuffer) {
        GL30.glFramebufferRenderbuffer((int)target, (int)attachment, (int)renderBufferTarget, (int)renderBuffer);
    }

    public void glFramebufferTexture2DEXT(int target, int attachment, int texTarget, int texture, int level) {
        GL30.glFramebufferTexture2D((int)target, (int)attachment, (int)texTarget, (int)texture, (int)level);
    }

    public void glGenFramebuffersEXT(IntBuffer frameBuffers) {
        int id = GL30.glGenFramebuffers();
        frameBuffers.put(0, id);
    }

    public void glGenRenderbuffersEXT(IntBuffer renderBuffers) {
        int id = GL30.glGenRenderbuffers();
        renderBuffers.put(0, id);
    }

    public void glGenerateMipmapEXT(int target) {
        GL30.glGenerateMipmap((int)target);
    }

    public void glRenderbufferStorageEXT(int target, int internalFormat, int width, int height) {
        GL30.glRenderbufferStorage((int)target, (int)internalFormat, (int)width, (int)height);
    }

    public void glFramebufferTextureLayerEXT(int target, int attachment, int texture, int level, int layer) {
        GL30.glFramebufferTextureLayer((int)target, (int)attachment, (int)texture, (int)level, (int)layer);
    }
}

