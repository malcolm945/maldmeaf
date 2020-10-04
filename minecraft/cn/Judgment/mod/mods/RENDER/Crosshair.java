package cn.Judgment.mod.mods.RENDER;

import java.awt.Color;

import com.darkmagician6.eventapi.EventTarget;

import cn.Judgment.Value;
import cn.Judgment.events.EventRender2D;
import cn.Judgment.mod.Category;
import cn.Judgment.mod.Mod;
import cn.Judgment.util.Colors;
import cn.Judgment.util.RenderUtil;

import net.minecraft.client.gui.ScaledResolution;


public class Crosshair
extends Mod {
    private boolean dragging;
    float hue;
    private Value<Boolean> DYNAMIC = new Value<Boolean>("Crosshair_DYNAMIC", true);
    public static Value<Double> GAP = new Value<Double>("Crosshair_gap", 5.0, 0.25, 15.0, 0.25);
    private Value<Double> WIDTH = new Value<Double>("Crosshair_width", 2.0,0.25,10.0, 0.25);
    public static Value<Double> SIZE = new Value<Double>("Crosshair_size", 7.0,0.25,15.0,0.25);

    public Crosshair() {
        super("Crosshair",Category.RENDER);
      //  this.addValues(this.DYNAMIC,this.GAP,this.WIDTH,this.SIZE);
    }

    @EventTarget
    public void onGui(EventRender2D e) {
    	int red = 255;
        int green = 0;
        int blue = 0;
        int alph = 255;
        double gap = ((Double)this.GAP.getValueState()).doubleValue();
        double width = ((Double) this.WIDTH.getValueState()).doubleValue();
        double size = ((Double) this.SIZE.getValueState()).doubleValue();
        ScaledResolution scaledRes = new ScaledResolution(mc);
        RenderUtil.rectangleBordered(
                scaledRes.getScaledWidth() / 2 - width,
                scaledRes.getScaledHeight() / 2 - gap - size - (isMoving() ? 2 : 0),
                scaledRes.getScaledWidth() / 2 + 1.0f + width,
                scaledRes.getScaledHeight() / 2 - gap - (isMoving() ? 2 : 0),  0.5f, Colors.getColor(red, green, blue, alph),
                new Color(0, 0, 0, alph).getRGB());
        RenderUtil.rectangleBordered(
                scaledRes.getScaledWidth() / 2 - width,
                scaledRes.getScaledHeight() / 2 + gap + 1 + (isMoving() ? 2 : 0) - 0.15,
                scaledRes.getScaledWidth() / 2 + 1.0f + width,
                scaledRes.getScaledHeight() / 2 + 1 + gap + size + (isMoving() ? 2 : 0) - 0.15, 0.5f, Colors.getColor(red, green, blue, alph),
                new Color(0, 0, 0, alph).getRGB());
        RenderUtil.rectangleBordered(
                scaledRes.getScaledWidth() / 2 - gap - size - (isMoving() ? 2 : 0) + 0.15,
                scaledRes.getScaledHeight() / 2 - width,
                scaledRes.getScaledWidth() / 2 - gap - (isMoving() ? 2 : 0) + 0.15,
                scaledRes.getScaledHeight() / 2 + 1.0f + width, 0.5f, Colors.getColor(red, green, blue, alph),
                new Color(0, 0, 0, alph).getRGB());
        RenderUtil.rectangleBordered(
                scaledRes.getScaledWidth() / 2 + 1 + gap + (isMoving() ? 2 : 0),
                scaledRes.getScaledHeight() / 2 - width,
                scaledRes.getScaledWidth() / 2 + size + gap + 1 + (isMoving() ? 2 : 0),
                scaledRes.getScaledHeight() / 2 + 1.0f + width, 0.5f, Colors.getColor(red, green, blue, alph),
                new Color(0, 0, 0, alph).getRGB());
    }
    public boolean isMoving() {
        return DYNAMIC.getValueState() && (!mc.thePlayer.isCollidedHorizontally) && (!mc.thePlayer.isSneaking()) && ((mc.thePlayer.movementInput.moveForward != 0.0F) || (mc.thePlayer.movementInput.moveStrafe != 0.0F));
    }
    
}
