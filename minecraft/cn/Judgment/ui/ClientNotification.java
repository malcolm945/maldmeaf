package cn.Judgment.ui;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import cn.Judgment.mod.mods.ClickGui;
import cn.Judgment.ui.font.FontLoaders;
import cn.Judgment.util.ClientUtil;
import cn.Judgment.util.FlatColors;
import cn.Judgment.util.RenderUtil;
import cn.Judgment.util.fontRenderer.CFont.CFontRenderer;
import cn.Judgment.util.timeUtils.TimeHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.ResourceLocation;

public class ClientNotification
{
    private String message;
    private TimeHelper timer;
    private double lastY;
    private double posY;
    private double width;
    private double height;
    private double animationX;
    private int color;
    private int imageWidth;
    private ResourceLocation image;
    private long stayTime;
    Minecraft mc = Minecraft.getMinecraft();
    
    public ClientNotification(final String message, final Type type) {
        this.message = message;
        (this.timer = new TimeHelper()).reset();
        final CFontRenderer font = FontLoaders.kiona20;
        this.width = font.getStringWidth(message);
        this.height = 16.0;
        this.animationX = this.width;
        this.stayTime = 900L;        
        this.posY = 0.0;
        if (type.equals(Type.INFO)) {
            this.color = ClientUtil.reAlpha(FlatColors.BLUE.c, 0.5F);
            if(ClickGui.Sound.getValueState().booleanValue()) {
            Minecraft.getMinecraft().thePlayer.playSound("random.click", 20.0F, 20.0F);
            }
        }
        else if (type.equals(Type.ERROR)) {
            this.color = ClientUtil.reAlpha(FlatColors.BLACK.c, 0.5F);
            if(ClickGui.Sound.getValueState().booleanValue()) {
            	  Minecraft.thePlayer.playSound("random.click", 0.2F, 0.5F);
            }
        }
        else if (type.equals(Type.SUCCESS)) {
            this.color = ClientUtil.reAlpha(FlatColors.BLACK.c, 0.5F);
            if(ClickGui.Sound.getValueState().booleanValue()) {
            	  Minecraft.thePlayer.playSound("random.click", 0.2F, 0.6F);
        }
}
        else if (type.equals(Type.WARNING)) {
            this.color = ClientUtil.reAlpha(FlatColors.YELLOW.c, 0.5F);
            if(ClickGui.Sound.getValueState().booleanValue()) {
            Minecraft.getMinecraft().thePlayer.playSound("random.orb", 1.0F, 1.0F);
        }
   }
    }
    
    public void draw(final double getY, final double lastY) {
        this.lastY = lastY;
        this.animationX = RenderUtil.getAnimationState(this.animationX, this.isFinished() ? this.width : 0.0, Math.max(this.isFinished() ? 400 : 10, Math.abs(this.animationX - (this.isFinished() ? this.width : 0.0)) * 20.0));
        if (this.posY == 0.0) {
            this.posY = getY;
        }
        else {
            this.posY = RenderUtil.getAnimationState(this.posY, getY, this.isFinished()?50:100);
        }
        final ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
        final int x1 = (int)(res.getScaledWidth() - this.width + this.animationX);
        final int x2 = (int)(res.getScaledWidth() + this.animationX);
        final int y1 = (int)this.posY;
        final int y2 = (int)(y1 + this.height-2);
        
        Gui.drawRect(x1, y1, x2, y2, new Color(30, 29, 30  ,200).getRGB());
        RenderUtil.drawGradientSideways(x1, y2 , x2, y2 + 1f, new Color(50, 80, 249,170).getRGB(),RenderUtil.rainbow(00));
        RenderUtil.drawGradientSideways(x1, y1 , x2, y1 + 1f, new Color(50, 100, 255,170).getRGB(),RenderUtil.rainbow(00));
        RenderUtil.drawGradientSideways(x1, y1 , x1+1, y2+1, new Color(50, 80, 249,170).getRGB(),new Color(50, 100, 255,170).getRGB());
        FontRenderer fr = mc.fontRendererObj;
        final CFontRenderer font = FontLoaders.kiona14;
        font.drawString(this.message, (float)(x1 + this.width / 6.5), (float)(y1 + this.height / 3.5), -1);
        
    }
    
    public boolean shouldDelete() {
        return this.isFinished() && this.animationX >= this.width;
    }
    
    private boolean isFinished() {
        return this.timer.isDelayComplete(this.stayTime) && this.posY == this.lastY;
    }
    
    public double getHeight() {
        return this.height;
    }
    
    public enum Type
    {
        SUCCESS("SUCCESS", 0), 
        INFO("INFO", 1), 
        WARNING("WARNING", 2), 
        ERROR("ERROR", 3);
        
        private Type(final String s, final int n) {
        }
    }
}



