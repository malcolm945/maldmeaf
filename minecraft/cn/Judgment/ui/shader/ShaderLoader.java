package cn.Judgment.ui.shader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import cn.Judgment.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class ShaderLoader {
	private int vertexShaderId;
    private int fragmentShaderId;
    private int programmId;
    private int fboTextureID;
    private int fboID;
    private int renderBufferID;
    private String vertexShaderFileName;
    private String fragmenShaderFileName;
    private int resolutionUniformId;
    private int timeUniformID;
    private int mouseUniformId;
    private int texelUniformId;
    private int frameBufferTextureId;
    private int diffuseSamperUniformID;
    private float time = 0.0f;

    public ShaderLoader(String fragmentShader, int frameBufferTextureId) {
        this.reset();
        this.vertexShaderFileName = "vertex.shader";
        this.fragmenShaderFileName = fragmentShader;
        this.frameBufferTextureId = frameBufferTextureId;
        this.generateFBO();
        this.initShaders();
    }

    private void generateFBO() {
        this.fboID = EXTFramebufferObject.glGenFramebuffersEXT();
        this.fboTextureID = GL11.glGenTextures();
        this.renderBufferID = EXTFramebufferObject.glGenRenderbuffersEXT();
        GL11.glBindTexture((int)3553, (int)this.fboTextureID);
        GL11.glTexParameterf((int)3553, (int)10241, (float)9729.0f);
        GL11.glTexParameterf((int)3553, (int)10240, (float)9729.0f);
        GL11.glTexParameterf((int)3553, (int)10242, (float)10496.0f);
        GL11.glTexParameterf((int)3553, (int)10243, (float)10496.0f);
        GL11.glBindTexture((int)3553, (int)0);
        GL11.glBindTexture((int)3553, (int)this.fboTextureID);
        GL11.glTexImage2D((int)3553, (int)0, (int)32856, (int)Minecraft.getMinecraft().displayWidth, (int)Minecraft.getMinecraft().displayHeight, (int)0, (int)6408, (int)5121, (ByteBuffer)null);
        EXTFramebufferObject.glBindFramebufferEXT((int)36160, (int)this.fboID);
        EXTFramebufferObject.glFramebufferTexture2DEXT((int)36160, (int)36064, (int)3553, (int)this.fboTextureID, (int)0);
        EXTFramebufferObject.glBindRenderbufferEXT((int)36161, (int)this.renderBufferID);
        EXTFramebufferObject.glRenderbufferStorageEXT((int)36161, (int)34041, (int)Minecraft.getMinecraft().displayWidth, (int)Minecraft.getMinecraft().displayHeight);
        EXTFramebufferObject.glFramebufferRenderbufferEXT((int)36160, (int)36128, (int)36161, (int)this.renderBufferID);
    }

    public int getFboTextureID() {
        return this.fboTextureID;
    }

    public void initShaders() {
        if (this.programmId == -1) {
            this.programmId = ARBShaderObjects.glCreateProgramObjectARB();
            try {
                InputStream in;
                if (this.vertexShaderId == -1) {
                    in = this.getClass().getResourceAsStream(this.vertexShaderFileName);
                    String vertexShaderCode = RenderUtil.getShaderCode(new InputStreamReader(in));
                    this.vertexShaderId = RenderUtil.createShader(vertexShaderCode, 35633);
                }
                if (this.fragmentShaderId == -1) {
                    in = this.getClass().getResourceAsStream("fragment/" + this.fragmenShaderFileName);
                    String fragmentShader = RenderUtil.getShaderCode(new InputStreamReader(in));
                    this.fragmentShaderId = RenderUtil.createShader(fragmentShader, 35632);
                }
            }
            catch (Exception ex) {
                this.programmId = -1;
                this.vertexShaderId = -1;
                this.fragmentShaderId = -1;
                ex.printStackTrace();
            }
            if (this.programmId != -1) {
                ARBShaderObjects.glAttachObjectARB((int)this.programmId, (int)this.vertexShaderId);
                ARBShaderObjects.glAttachObjectARB((int)this.programmId, (int)this.fragmentShaderId);
                ARBShaderObjects.glLinkProgramARB((int)this.programmId);
                if (ARBShaderObjects.glGetObjectParameteriARB((int)this.programmId, (int)35714) == 0) {
                    System.err.println(this.programmId);
                    return;
                }
                ARBShaderObjects.glValidateProgramARB((int)this.programmId);
                if (ARBShaderObjects.glGetObjectParameteriARB((int)this.programmId, (int)35715) == 0) {
                    System.err.println(this.programmId);
                    return;
                }
                ARBShaderObjects.glUseProgramObjectARB((int)0);
                this.resolutionUniformId = ARBShaderObjects.glGetUniformLocationARB((int)this.programmId, (CharSequence)"resolution");
                this.timeUniformID = ARBShaderObjects.glGetUniformLocationARB((int)this.programmId, (CharSequence)"timeHelper");
                this.mouseUniformId = ARBShaderObjects.glGetUniformLocationARB((int)this.programmId, (CharSequence)"mouse");
                this.diffuseSamperUniformID = ARBShaderObjects.glGetUniformLocationARB((int)this.programmId, (CharSequence)"diffuseSamper");
                this.texelUniformId = ARBShaderObjects.glGetUniformLocationARB((int)this.programmId, (CharSequence)"texel");
            }
        }
    }

    public ShaderLoader update() {
        if (this.fboID == -1 || this.renderBufferID == -1 || this.programmId == -1) {
            throw new RuntimeException("Invalid IDs!");
        }
        EXTFramebufferObject.glBindFramebufferEXT((int)36160, (int)this.fboID);
        int var9 = Math.max(Minecraft.getDebugFPS(), 30);
        GL11.glClear((int)16640);
        ARBShaderObjects.glUseProgramObjectARB((int)this.programmId);
        ARBShaderObjects.glUniform1iARB((int)this.diffuseSamperUniformID, (int)0);
        GL13.glActiveTexture((int)33984);
        GL11.glEnable((int)3553);
        GL11.glBindTexture((int)3553, (int)this.frameBufferTextureId);
        ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
        FloatBuffer resolutionBuffer = BufferUtils.createFloatBuffer((int)2);
        resolutionBuffer.position(0);
        resolutionBuffer.put(Minecraft.getMinecraft().displayWidth / 2);
        resolutionBuffer.put(Minecraft.getMinecraft().displayHeight / 2);
        resolutionBuffer.flip();
        ARBShaderObjects.glUniform2ARB((int)this.resolutionUniformId, (FloatBuffer)resolutionBuffer);
        FloatBuffer texelSizeBuffer = BufferUtils.createFloatBuffer((int)2);
        texelSizeBuffer.position(0);
        texelSizeBuffer.put(1.0f / (float)Minecraft.getMinecraft().displayWidth * 2.0f);
        texelSizeBuffer.put(1.0f / (float)Minecraft.getMinecraft().displayHeight * 2.0f);
        texelSizeBuffer.flip();
        ARBShaderObjects.glUniform2ARB((int)this.texelUniformId, (FloatBuffer)texelSizeBuffer);
        float mouseX = (float)Mouse.getX() / (float)Minecraft.getMinecraft().displayWidth;
        float mouseY = (float)Mouse.getY() / (float)Minecraft.getMinecraft().displayHeight;
        FloatBuffer mouseBuffer = BufferUtils.createFloatBuffer((int)2);
        mouseBuffer.position(0);
        mouseBuffer.put(mouseX);
        mouseBuffer.put(mouseY);
        mouseBuffer.flip();
        ARBShaderObjects.glUniform2ARB((int)this.mouseUniformId, (FloatBuffer)mouseBuffer);
        this.time = (float)((double)this.time + (double)RenderUtil.delta * 0.7);
        ARBShaderObjects.glUniform1fARB((int)this.timeUniformID, (float)this.time);
        double width = res.getScaledWidth();
        double height = res.getScaledHeight();
        GL11.glDisable((int)3553);
        GL11.glBegin((int)4);
        GL11.glTexCoord2d((double)0.0, (double)1.0);
        GL11.glVertex2d((double)0.0, (double)0.0);
        GL11.glTexCoord2d((double)0.0, (double)0.0);
        GL11.glVertex2d((double)0.0, (double)(height / 2.0));
        GL11.glTexCoord2d((double)1.0, (double)0.0);
        GL11.glVertex2d((double)(width / 2.0), (double)(height / 2.0));
        GL11.glTexCoord2d((double)1.0, (double)0.0);
        GL11.glVertex2d((double)(width / 2.0), (double)(height / 2.0));
        GL11.glTexCoord2d((double)1.0, (double)1.0);
        GL11.glVertex2d((double)(width / 2.0), (double)0.0);
        GL11.glTexCoord2d((double)0.0, (double)1.0);
        GL11.glVertex2d((double)0.0, (double)0.0);
        GL11.glEnd();
        ARBShaderObjects.glUseProgramObjectARB((int)0);
        return this;
    }

    private void reset() {
        this.vertexShaderId = -1;
        this.fragmentShaderId = -1;
        this.programmId = -1;
        this.fboTextureID = -1;
        this.fboID = -1;
        this.renderBufferID = -1;
    }

    public void delete() {
        if (this.renderBufferID > -1) {
            EXTFramebufferObject.glDeleteRenderbuffersEXT((int)this.renderBufferID);
        }
        if (this.fboID > -1) {
            EXTFramebufferObject.glDeleteFramebuffersEXT((int)this.fboID);
        }
        if (this.fboTextureID > -1) {
            GL11.glDeleteTextures((int)this.fboTextureID);
        }
    }
}
