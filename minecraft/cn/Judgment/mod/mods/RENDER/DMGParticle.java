package cn.Judgment.mod.mods.RENDER;

import java.util.Iterator;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

import java.util.Random;
import java.util.function.Consumer;
import com.darkmagician6.eventapi.EventTarget;

import cn.Judgment.events.EventRender;
import cn.Judgment.events.EventUpdate;
import cn.Judgment.mod.Category;
import cn.Judgment.mod.Mod;
import cn.Judgment.util.Particles;
import cn.Judgment.util.EventLivingUpdate;
import cn.Judgment.util.Location;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

public class DMGParticle extends Mod{
    private HashMap<EntityLivingBase,Float> healthMap = new HashMap<EntityLivingBase, Float>();;
    private List<Particles> particles = new ArrayList<Particles>();;

    public DMGParticle() {
	super("DMGParticle", Category.RENDER);
    }
    
    @EventTarget
    public void onLivingUpdate(EventLivingUpdate e) {
	EntityLivingBase entity = (EntityLivingBase) e.getEntity();
        if (entity == this.mc.thePlayer) {
            return;
        } 
        if (!this.healthMap.containsKey(entity)) {
            this.healthMap.put(entity, ((EntityLivingBase) entity).getHealth());
        }
        float floatValue = this.healthMap.get(entity);
        float health = entity.getHealth();
        if (floatValue != health) { 
            String text;
            if (floatValue - health < 0.0f) {
        	text = "\247a" + roundToPlace((floatValue - health) * -1.0f, 1);               
            }else {
        	text = "\247e" + roundToPlace(floatValue - health, 1);
            }
            Location location = new Location(entity);
            location.setY(entity.getEntityBoundingBox().minY + (entity.getEntityBoundingBox().maxY - entity.getEntityBoundingBox().minY) / 2.0);
            location.setX(location.getX() - 0.5 + new Random(System.currentTimeMillis()).nextInt(5) * 0.1);
            location.setZ(location.getZ() - 0.5 + new Random(System.currentTimeMillis() + (0x203FF36645D9EA2EL ^ 0x203FF36645D9EA2FL)).nextInt(5) * 0.1);
            this.particles.add(new Particles(location, text));
            this.healthMap.remove(entity);
            this.healthMap.put(entity, entity.getHealth());
        }
    }
    
    @EventTarget
    public void onRender(EventRender e) {
        for (Particles p : this.particles) {
            double x = p.location.getX();
            this.mc.getRenderManager();
            double n = x - RenderManager.renderPosX;
            double y = p.location.getY();
            this.mc.getRenderManager();
            double n2 = y - RenderManager.renderPosY;
            double z = p.location.getZ();
            this.mc.getRenderManager();
            double n3 = z - RenderManager.renderPosZ;
            GlStateManager.pushMatrix();
            GlStateManager.enablePolygonOffset();
            GlStateManager.doPolygonOffset(1.0f, -1500000.0f);
            GlStateManager.translate((float)n, (float)n2, (float)n3);
            GlStateManager.rotate(-this.mc.getRenderManager().playerViewY, 0.0f, 1.0f, 0.0f);
            float textY;
            if (this.mc.gameSettings.thirdPersonView == 2) {
        	textY = -1.0f;
            }else {
        	textY = 1.0f;
            }
            GlStateManager.rotate(this.mc.getRenderManager().playerViewX, textY, 0.0f, 0.0f);
            final double size = 0.03;
            GlStateManager.scale(-size, -size, size);
            enableGL2D();
            disableGL2D();
            GL11.glDepthMask(false);
            this.mc.fontRendererObj.drawStringWithShadow(p.text, (float)(-(this.mc.fontRendererObj.getStringWidth(p.text) / 2)), (float)(-(this.mc.fontRendererObj.FONT_HEIGHT - 1)), 0);
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glDepthMask(true);
            GlStateManager.doPolygonOffset(1.0f, 1500000.0f);
            GlStateManager.disablePolygonOffset();
            GlStateManager.popMatrix();
        }
    }
    
    public static void enableGL2D() {
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glDepthMask(true);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
    }
    
    public static void disableGL2D() {
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
        GL11.glDisable(2848);
        GL11.glHint(3154, 4352);
        GL11.glHint(3155, 4352);
    }
    
    public static double roundToPlace(double p_roundToPlace_0_,int p_roundToPlace_2_) {
        if (p_roundToPlace_2_ < 0) {
            throw new IllegalArgumentException();
        }
        return new BigDecimal(p_roundToPlace_0_).setScale(p_roundToPlace_2_, RoundingMode.HALF_UP).doubleValue();
    }
    
    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
        this.particles.forEach(this::lambda$onUpdate$0);
    }
    
    private void lambda$onUpdate$0(Particles update) {
        ++update.ticks;
        if (update.ticks <= 10) {
            update.location.setY(update.location.getY() + update.ticks * 0.005);
        }
        if (update.ticks > 20) {
            this.particles.remove(update);
        }
    }
}