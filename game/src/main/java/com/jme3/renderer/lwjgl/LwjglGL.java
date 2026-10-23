/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.jme3.renderer.opengl.GL
 *  com.jme3.renderer.opengl.GL2
 *  com.jme3.renderer.opengl.GL3
 *  com.jme3.renderer.opengl.GL4
 *  org.lwjgl.opengl.ARBTimerQuery
 *  org.lwjgl.opengl.ARBVertexArrayObject
 *  org.lwjgl.opengl.GL11
 *  org.lwjgl.opengl.GL12
 *  org.lwjgl.opengl.GL13
 *  org.lwjgl.opengl.GL14
 *  org.lwjgl.opengl.GL15
 *  org.lwjgl.opengl.GL20
 *  org.lwjgl.opengl.GL30
 *  org.lwjgl.opengl.GL31
 *  org.lwjgl.opengl.GL40
 *  org.lwjgl.opengl.GL43
 */
package com.jme3.renderer.lwjgl;

import com.jme3.renderer.lwjgl.LwjglRender;
import com.jme3.renderer.opengl.GL;
import com.jme3.renderer.opengl.GL2;
import com.jme3.renderer.opengl.GL3;
import com.jme3.renderer.opengl.GL4;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import org.lwjgl.opengl.ARBTimerQuery;
import org.lwjgl.opengl.ARBVertexArrayObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL31;
import org.lwjgl.opengl.GL40;
import org.lwjgl.opengl.GL43;

public class LwjglGL
extends LwjglRender
implements GL,
GL2,
GL3,
GL4 {
    public void resetStats() {
    }

    public void glActiveTexture(int texture) {
        GL13.glActiveTexture((int)texture);
    }

    public void glAlphaFunc(int func, float ref) {
        GL11.glAlphaFunc((int)func, (float)ref);
    }

    public void glAttachShader(int program, int shader) {
        GL20.glAttachShader((int)program, (int)shader);
    }

    public void glBeginQuery(int target, int query) {
        GL15.glBeginQuery((int)target, (int)query);
    }

    public void glBindBuffer(int target, int buffer) {
        GL15.glBindBuffer((int)target, (int)buffer);
    }

    public void glBindTexture(int target, int texture) {
        GL11.glBindTexture((int)target, (int)texture);
    }

    public void glBlendEquationSeparate(int colorMode, int alphaMode) {
        GL20.glBlendEquationSeparate((int)colorMode, (int)alphaMode);
    }

    public void glBlendFunc(int sFactor, int dFactor) {
        GL11.glBlendFunc((int)sFactor, (int)dFactor);
    }

    public void glBlendFuncSeparate(int sFactorRGB, int dFactorRGB, int sFactorAlpha, int dFactorAlpha) {
        GL14.glBlendFuncSeparate((int)sFactorRGB, (int)dFactorRGB, (int)sFactorAlpha, (int)dFactorAlpha);
    }

    public void glBufferData(int target, long dataSize, int usage) {
        GL15.glBufferData((int)target, (long)dataSize, (int)usage);
    }

    public void glBufferData(int target, FloatBuffer data, int usage) {
        LwjglGL.checkLimit(data);
        GL15.glBufferData((int)target, (FloatBuffer)data, (int)usage);
    }

    public void glBufferData(int target, ShortBuffer data, int usage) {
        LwjglGL.checkLimit(data);
        GL15.glBufferData((int)target, (ShortBuffer)data, (int)usage);
    }

    public void glBufferData(int target, ByteBuffer data, int usage) {
        LwjglGL.checkLimit(data);
        GL15.glBufferData((int)target, (ByteBuffer)data, (int)usage);
    }

    public void glBufferSubData(int target, long offset, FloatBuffer data) {
        LwjglGL.checkLimit(data);
        GL15.glBufferSubData((int)target, (long)offset, (FloatBuffer)data);
    }

    public void glBufferSubData(int target, long offset, ShortBuffer data) {
        LwjglGL.checkLimit(data);
        GL15.glBufferSubData((int)target, (long)offset, (ShortBuffer)data);
    }

    public void glBufferSubData(int target, long offset, ByteBuffer data) {
        LwjglGL.checkLimit(data);
        GL15.glBufferSubData((int)target, (long)offset, (ByteBuffer)data);
    }

    public void glClear(int mask) {
        GL11.glClear((int)mask);
    }

    public void glClearColor(float red, float green, float blue, float alpha) {
        GL11.glClearColor((float)red, (float)green, (float)blue, (float)alpha);
    }

    public void glColorMask(boolean red, boolean green, boolean blue, boolean alpha) {
        GL11.glColorMask((boolean)red, (boolean)green, (boolean)blue, (boolean)alpha);
    }

    public void glCompileShader(int shader) {
        GL20.glCompileShader((int)shader);
    }

    public void glCompressedTexImage2D(int target, int level, int internalFormat, int width, int height, int border, ByteBuffer data) {
        LwjglGL.checkLimit(data);
        GL13.glCompressedTexImage2D((int)target, (int)level, (int)internalFormat, (int)width, (int)height, (int)border, (ByteBuffer)data);
    }

    public void glCompressedTexImage3D(int target, int level, int internalFormat, int width, int height, int depth, int border, ByteBuffer data) {
        LwjglGL.checkLimit(data);
        GL13.glCompressedTexImage3D((int)target, (int)level, (int)internalFormat, (int)width, (int)height, (int)depth, (int)border, (ByteBuffer)data);
    }

    public void glCompressedTexSubImage2D(int target, int level, int xoffset, int yoffset, int width, int height, int format, ByteBuffer data) {
        LwjglGL.checkLimit(data);
        GL13.glCompressedTexSubImage2D((int)target, (int)level, (int)xoffset, (int)yoffset, (int)width, (int)height, (int)format, (ByteBuffer)data);
    }

    public void glCompressedTexSubImage3D(int target, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, ByteBuffer data) {
        LwjglGL.checkLimit(data);
        GL13.glCompressedTexSubImage3D((int)target, (int)level, (int)xoffset, (int)yoffset, (int)zoffset, (int)width, (int)height, (int)depth, (int)format, (ByteBuffer)data);
    }

    public int glCreateProgram() {
        return GL20.glCreateProgram();
    }

    public int glCreateShader(int shaderType) {
        return GL20.glCreateShader((int)shaderType);
    }

    public void glCullFace(int mode) {
        GL11.glCullFace((int)mode);
    }

    public void glDeleteBuffers(IntBuffer buffers) {
        LwjglGL.checkLimit(buffers);
        GL15.glDeleteBuffers((IntBuffer)buffers);
    }

    public void glDeleteProgram(int program) {
        GL20.glDeleteProgram((int)program);
    }

    public void glDeleteShader(int shader) {
        GL20.glDeleteShader((int)shader);
    }

    public void glDeleteTextures(IntBuffer textures) {
        LwjglGL.checkLimit(textures);
        GL11.glDeleteTextures((IntBuffer)textures);
    }

    public void glDepthFunc(int func) {
        GL11.glDepthFunc((int)func);
    }

    public void glDepthMask(boolean flag) {
        GL11.glDepthMask((boolean)flag);
    }

    public void glDepthRange(double nearVal, double farVal) {
        GL11.glDepthRange((double)nearVal, (double)farVal);
    }

    public void glDetachShader(int program, int shader) {
        GL20.glDetachShader((int)program, (int)shader);
    }

    public void glDisable(int cap) {
        GL11.glDisable((int)cap);
    }

    public void glDisableVertexAttribArray(int index) {
        GL20.glDisableVertexAttribArray((int)index);
    }

    public void glDrawArrays(int mode, int first, int count) {
        GL11.glDrawArrays((int)mode, (int)first, (int)count);
    }

    public void glDrawBuffer(int mode) {
        GL11.glDrawBuffer((int)mode);
    }

    public void glDrawRangeElements(int mode, int start, int end, int count, int type, long indices) {
        GL12.glDrawRangeElements((int)mode, (int)start, (int)end, (int)count, (int)type, (long)indices);
    }

    public void glEnable(int cap) {
        GL11.glEnable((int)cap);
    }

    public void glEnableVertexAttribArray(int index) {
        GL20.glEnableVertexAttribArray((int)index);
    }

    public void glEndQuery(int target) {
        GL15.glEndQuery((int)target);
    }

    public void glGenBuffers(IntBuffer buffers) {
        LwjglGL.checkLimit(buffers);
        GL15.glGenBuffers((IntBuffer)buffers);
    }

    public void glGenTextures(IntBuffer textures) {
        LwjglGL.checkLimit(textures);
        GL11.glGenTextures((IntBuffer)textures);
    }

    public void glGenQueries(int num, IntBuffer ids) {
        GL15.glGenQueries((IntBuffer)ids);
    }

    public void glGetBoolean(int pname, ByteBuffer params) {
        LwjglGL.checkLimit(params);
        GL11.glGetBooleanv((int)pname, (ByteBuffer)params);
    }

    public void glGetBufferSubData(int target, long offset, ByteBuffer data) {
        LwjglGL.checkLimit(data);
        GL15.glGetBufferSubData((int)target, (long)offset, (ByteBuffer)data);
    }

    public int glGetError() {
        return GL11.glGetError();
    }

    public void glGetFloat(int parameterId, FloatBuffer storeValues) {
        LwjglGL.checkLimit(storeValues);
        GL11.glGetFloatv((int)parameterId, (FloatBuffer)storeValues);
    }

    public void glGetInteger(int pname, IntBuffer params) {
        LwjglGL.checkLimit(params);
        GL11.glGetIntegerv((int)pname, (IntBuffer)params);
    }

    public void glGetProgram(int program, int pname, IntBuffer params) {
        LwjglGL.checkLimit(params);
        GL20.glGetProgramiv((int)program, (int)pname, (IntBuffer)params);
    }

    public void glGetShader(int shader, int pname, IntBuffer params) {
        LwjglGL.checkLimit(params);
        GL20.glGetShaderiv((int)shader, (int)pname, (IntBuffer)params);
    }

    public String glGetString(int name) {
        return GL11.glGetString((int)name);
    }

    public String glGetString(int name, int index) {
        return GL30.glGetStringi((int)name, (int)index);
    }

    public boolean glIsEnabled(int cap) {
        return GL11.glIsEnabled((int)cap);
    }

    public void glLineWidth(float width) {
        GL11.glLineWidth((float)width);
    }

    public void glLinkProgram(int program) {
        GL20.glLinkProgram((int)program);
    }

    public void glPixelStorei(int pname, int param) {
        GL11.glPixelStorei((int)pname, (int)param);
    }

    public void glPointSize(float size) {
        GL11.glPointSize((float)size);
    }

    public void glPolygonMode(int face, int mode) {
        GL11.glPolygonMode((int)face, (int)mode);
    }

    public void glPolygonOffset(float factor, float units) {
        GL11.glPolygonOffset((float)factor, (float)units);
    }

    public void glReadBuffer(int mode) {
        GL11.glReadBuffer((int)mode);
    }

    public void glReadPixels(int x, int y, int width, int height, int format, int type, ByteBuffer data) {
        LwjglGL.checkLimit(data);
        GL11.glReadPixels((int)x, (int)y, (int)width, (int)height, (int)format, (int)type, (ByteBuffer)data);
    }

    public void glReadPixels(int x, int y, int width, int height, int format, int type, long offset) {
        GL11.glReadPixels((int)x, (int)y, (int)width, (int)height, (int)format, (int)type, (long)offset);
    }

    public void glScissor(int x, int y, int width, int height) {
        GL11.glScissor((int)x, (int)y, (int)width, (int)height);
    }

    public void glStencilFuncSeparate(int face, int func, int ref, int mask) {
        GL20.glStencilFuncSeparate((int)face, (int)func, (int)ref, (int)mask);
    }

    public void glStencilOpSeparate(int face, int sfail, int dpfail, int dppass) {
        GL20.glStencilOpSeparate((int)face, (int)sfail, (int)dpfail, (int)dppass);
    }

    public void glTexImage2D(int target, int level, int internalFormat, int width, int height, int border, int format, int type, ByteBuffer data) {
        LwjglGL.checkLimit(data);
        GL11.glTexImage2D((int)target, (int)level, (int)internalFormat, (int)width, (int)height, (int)border, (int)format, (int)type, (ByteBuffer)data);
    }

    public void glTexImage3D(int target, int level, int internalFormat, int width, int height, int depth, int border, int format, int type, ByteBuffer data) {
        LwjglGL.checkLimit(data);
        GL12.glTexImage3D((int)target, (int)level, (int)internalFormat, (int)width, (int)height, (int)depth, (int)border, (int)format, (int)type, (ByteBuffer)data);
    }

    public void glTexParameterf(int target, int pname, float param) {
        GL11.glTexParameterf((int)target, (int)pname, (float)param);
    }

    public void glTexParameteri(int target, int pname, int param) {
        GL11.glTexParameteri((int)target, (int)pname, (int)param);
    }

    public void glTexSubImage2D(int target, int level, int xoffset, int yoffset, int width, int height, int format, int type, ByteBuffer data) {
        LwjglGL.checkLimit(data);
        GL11.glTexSubImage2D((int)target, (int)level, (int)xoffset, (int)yoffset, (int)width, (int)height, (int)format, (int)type, (ByteBuffer)data);
    }

    public void glTexSubImage3D(int target, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, ByteBuffer data) {
        LwjglGL.checkLimit(data);
        GL12.glTexSubImage3D((int)target, (int)level, (int)xoffset, (int)yoffset, (int)zoffset, (int)width, (int)height, (int)depth, (int)format, (int)type, (ByteBuffer)data);
    }

    public void glUniform1(int location, FloatBuffer value) {
        LwjglGL.checkLimit(value);
        GL20.glUniform1fv((int)location, (FloatBuffer)value);
    }

    public void glUniform1(int location, IntBuffer value) {
        LwjglGL.checkLimit(value);
        GL20.glUniform1iv((int)location, (IntBuffer)value);
    }

    public void glUniform1f(int location, float v0) {
        GL20.glUniform1f((int)location, (float)v0);
    }

    public void glUniform1i(int location, int v0) {
        GL20.glUniform1i((int)location, (int)v0);
    }

    public void glUniform2(int location, IntBuffer value) {
        LwjglGL.checkLimit(value);
        GL20.glUniform2iv((int)location, (IntBuffer)value);
    }

    public void glUniform2(int location, FloatBuffer value) {
        LwjglGL.checkLimit(value);
        GL20.glUniform2fv((int)location, (FloatBuffer)value);
    }

    public void glUniform2f(int location, float v0, float v1) {
        GL20.glUniform2f((int)location, (float)v0, (float)v1);
    }

    public void glUniform3(int location, IntBuffer value) {
        LwjglGL.checkLimit(value);
        GL20.glUniform3iv((int)location, (IntBuffer)value);
    }

    public void glUniform3(int location, FloatBuffer value) {
        LwjglGL.checkLimit(value);
        GL20.glUniform3fv((int)location, (FloatBuffer)value);
    }

    public void glUniform3f(int location, float v0, float v1, float v2) {
        GL20.glUniform3f((int)location, (float)v0, (float)v1, (float)v2);
    }

    public void glUniform4(int location, FloatBuffer value) {
        LwjglGL.checkLimit(value);
        GL20.glUniform4fv((int)location, (FloatBuffer)value);
    }

    public void glUniform4(int location, IntBuffer value) {
        LwjglGL.checkLimit(value);
        GL20.glUniform4iv((int)location, (IntBuffer)value);
    }

    public void glUniform4f(int location, float v0, float v1, float v2, float v3) {
        GL20.glUniform4f((int)location, (float)v0, (float)v1, (float)v2, (float)v3);
    }

    public void glUniformMatrix3(int location, boolean transpose, FloatBuffer value) {
        LwjglGL.checkLimit(value);
        GL20.glUniformMatrix3fv((int)location, (boolean)transpose, (FloatBuffer)value);
    }

    public void glUniformMatrix4(int location, boolean transpose, FloatBuffer value) {
        LwjglGL.checkLimit(value);
        GL20.glUniformMatrix4fv((int)location, (boolean)transpose, (FloatBuffer)value);
    }

    public void glUseProgram(int program) {
        GL20.glUseProgram((int)program);
    }

    public void glVertexAttribPointer(int index, int size, int type, boolean normalized, int stride, long pointer) {
        GL20.glVertexAttribPointer((int)index, (int)size, (int)type, (boolean)normalized, (int)stride, (long)pointer);
    }

    public void glViewport(int x, int y, int width, int height) {
        GL11.glViewport((int)x, (int)y, (int)width, (int)height);
    }

    public int glGetAttribLocation(int program, String name) {
        return GL20.glGetAttribLocation((int)program, (CharSequence)(name + "\u0000"));
    }

    public int glGetUniformLocation(int program, String name) {
        return GL20.glGetUniformLocation((int)program, (CharSequence)(name + "\u0000"));
    }

    public void glShaderSource(int shader, String[] strings, IntBuffer length) {
        LwjglGL.checkLimit(length);
        GL20.glShaderSource((int)shader, (CharSequence[])strings);
    }

    public String glGetProgramInfoLog(int program, int maxSize) {
        return GL20.glGetProgramInfoLog((int)program, (int)maxSize);
    }

    public long glGetQueryObjectui64(int query, int target) {
        return ARBTimerQuery.glGetQueryObjectui64((int)query, (int)target);
    }

    public int glGetQueryObjectiv(int query, int pname) {
        return GL15.glGetQueryObjecti((int)query, (int)pname);
    }

    public String glGetShaderInfoLog(int shader, int maxSize) {
        return GL20.glGetShaderInfoLog((int)shader, (int)maxSize);
    }

    public void glBindFragDataLocation(int program, int colorNumber, String name) {
        GL30.glBindFragDataLocation((int)program, (int)colorNumber, (CharSequence)name);
    }

    public void glBindVertexArray(int array) {
        GL30.glBindVertexArray((int)array);
    }

    public void glGenVertexArrays(IntBuffer arrays) {
        LwjglGL.checkLimit(arrays);
        GL30.glGenVertexArrays((IntBuffer)arrays);
    }

    public void glPatchParameter(int count) {
        GL40.glPatchParameteri((int)36466, (int)count);
    }

    public int glGetProgramResourceIndex(int program, int programInterface, String name) {
        return GL43.glGetProgramResourceIndex((int)program, (int)programInterface, (CharSequence)name);
    }

    public void glShaderStorageBlockBinding(int program, int storageBlockIndex, int storageBlockBinding) {
        GL43.glShaderStorageBlockBinding((int)program, (int)storageBlockIndex, (int)storageBlockBinding);
    }

    public void glDeleteVertexArrays(IntBuffer arrays) {
        LwjglGL.checkLimit(arrays);
        ARBVertexArrayObject.glDeleteVertexArrays((IntBuffer)arrays);
    }

    public int glGetUniformBlockIndex(int program, String uniformBlockName) {
        return GL31.glGetUniformBlockIndex((int)program, (CharSequence)uniformBlockName);
    }

    public void glBindBufferBase(int target, int index, int buffer) {
        GL30.glBindBufferBase((int)target, (int)index, (int)buffer);
    }

    public void glUniformBlockBinding(int program, int uniformBlockIndex, int uniformBlockBinding) {
        GL31.glUniformBlockBinding((int)program, (int)uniformBlockIndex, (int)uniformBlockBinding);
    }
}

