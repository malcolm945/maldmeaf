package cn.Judgment.ui.shader;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.shader.Framebuffer;

public class Shader {
	private Framebuffer frameBuffer;
    private ShaderLoader clientShader;
    private static Minecraft mc = Minecraft.getMinecraft();
    private String fragmentShader;

    public Shader(String fragmentShader) {
        this.fragmentShader = fragmentShader;
    }

    public void startShader() {
        ScaledResolution sr;
        if (mc.gameSettings.guiScale != 2 && mc.currentScreen == null) {
            mc.gameSettings.guiScale = 2;
        }
        if (this.frameBuffer == null) {
            this.frameBuffer = new Framebuffer(Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight, false);
        } else if (this.frameBuffer.framebufferWidth != Minecraft.getMinecraft().displayWidth || this.frameBuffer.framebufferHeight != Minecraft.getMinecraft().displayHeight) {
            this.frameBuffer.unbindFramebuffer();
            this.frameBuffer = new Framebuffer(Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight, false);
            if (this.clientShader != null) {
                this.clientShader.delete();
                sr = new ScaledResolution(mc);
                this.clientShader = new ShaderLoader(this.fragmentShader, this.frameBuffer.framebufferTexture);
            }
        }
        if (this.clientShader == null) {
            sr = new ScaledResolution(mc);
            this.clientShader = new ShaderLoader(this.fragmentShader, this.frameBuffer.framebufferTexture);
        }
        GL11.glMatrixMode((int)5888);
        RenderHelper.enableStandardItemLighting();
        this.frameBuffer.bindFramebuffer(false);
        GL11.glClearColor((float)0.0f, (float)0.0f, (float)0.0f, (float)0.0f);
        GL11.glClear((int)16640);
    }

    public void stopShader() {
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        this.clientShader.update();
        Minecraft.getMinecraft().getFramebuffer().bindFramebuffer(true);
        ScaledResolution sr = new ScaledResolution(mc);
        GL11.glEnable((int)3553);
        GL11.glBindTexture((int)3553, (int)this.clientShader.getFboTextureID());
        GL11.glBegin((int)4);
        GL11.glTexCoord2d((double)0.0, (double)1.0);
        GL11.glVertex2d((double)0.0, (double)0.0);
        GL11.glTexCoord2d((double)0.0, (double)0.0);
        double x = 0.0;
        double y = 0.0;
        double width = sr.getScaledWidth();
        double height = sr.getScaledHeight();
        GL11.glVertex2d((double)x, (double)(y + height * 2.0));
        GL11.glTexCoord2d((double)1.0, (double)0.0);
        GL11.glVertex2d((double)(x + width * 2.0), (double)(y + height * 2.0));
        GL11.glTexCoord2d((double)1.0, (double)0.0);
        GL11.glVertex2d((double)(x + width * 2.0), (double)(y + height * 2.0));
        GL11.glTexCoord2d((double)1.0, (double)1.0);
        GL11.glVertex2d((double)(x + width * 2.0), (double)y);
        GL11.glTexCoord2d((double)0.0, (double)1.0);
        GL11.glVertex2d((double)0.0, (double)0.0);
        GL11.glEnd();
    }

    public void deleteShader() {
        try {
            this.clientShader.delete();
            this.frameBuffer.unbindFramebuffer();
            this.frameBuffer.unbindFramebufferTexture();
            this.clientShader = null;
            this.frameBuffer = null;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
