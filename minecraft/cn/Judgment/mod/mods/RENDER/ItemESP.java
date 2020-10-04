package cn.Judgment.mod.mods.RENDER;

import com.darkmagician6.eventapi.EventTarget;

import cn.Judgment.Value;
import cn.Judgment.events.EventRender;
import cn.Judgment.events.EventRender2D;
import cn.Judgment.mod.Category;
import cn.Judgment.mod.Mod;
import cn.Judgment.mod.ModManager;
import cn.Judgment.util.ClientUtil;
import cn.Judgment.util.RenderUtil;

import java.awt.Color;
import java.util.Iterator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.opengl.GL11;

public class ItemESP extends Mod {
   private Value mode = new Value("ItemESP", "Mode", 0);
   private Value red = new Value("ItemESP_Red", 255.0D, 0.0D, 255.0D, 5.0D);
   private Value green = new Value("ItemESP_Green", 0.0D, 0.0D, 255.0D, 5.0D);
   private Value blue = new Value("ItemESP_Blue", 0.0D, 0.0D, 255.0D, 5.0D);
   private Value alpha = new Value("ItemESP_Alpha", 100.0D, 0.0D, 255.0D, 5.0D);
 //  private ChestShader shader;
   private AxisAlignedBB Item = new AxisAlignedBB(-0.175D, 0.0D, -0.175D, 0.175D, 0.35D, 0.175D);

   public ItemESP() {
      super("ItemESP", Category.RENDER);
      this.mode.mode.add("2DBox");
      this.mode.mode.add("OutlinedBox");
      this.mode.mode.add("Shader");
      this.mode.mode.add("Box");
      this.mode.mode.add("Circle");
      this.mode.mode.add("2D");
     // this.shader = new ChestShader();
   }

   @EventTarget
   public void onRender(EventRender event) {
      int countMod = 0;
      int rainbow = Gui.rainbow(System.nanoTime(), (float)countMod, 1.0F).getRGB();
      GL11.glPushMatrix();
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      GL11.glEnable(2848);
      GL11.glLineWidth(2.0F);
      GL11.glDisable(3553);
      GL11.glEnable(2884);
      GL11.glDisable(2929);
      double renderPosX = this.mc.getRenderManager().viewerPosX;
      double renderPosY = this.mc.getRenderManager().viewerPosY;
      double renderPosZ = this.mc.getRenderManager().viewerPosZ;
      GL11.glTranslated(-renderPosX, -renderPosY, -renderPosZ);
      GL11.glColor4d(((Double)this.red.getValueState()).doubleValue() / 255.0D, ((Double)this.green.getValueState()).doubleValue() / 255.0D, ((Double)this.blue.getValueState()).doubleValue() / 255.0D, ((Double)this.alpha.getValueState()).doubleValue() / 255.0D);
      Iterator var11 = this.mc.theWorld.loadedEntityList.iterator();

      while(var11.hasNext()) {
         Entity entity = (Entity)var11.next();
         if (entity instanceof EntityItem) {
            GL11.glPushMatrix();
            GL11.glTranslated(entity.posX, entity.posY, entity.posZ);
            if (this.mode.isCurrentMode("OutlinedBox")) {
               RenderUtil.drawOutlinedBoundingBox(this.Item);
            } else if (this.mode.isCurrentMode("Box")) {
               RenderUtil.drawBoundingBox(this.Item);
            } else if (this.mode.isCurrentMode("Circle")) {
               GlStateManager.rotate(-this.mc.getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
               Gui.drawFilledCircle((float)((int)this.Item.minX), (float)((int)this.Item.minY) + 0.25F, 0.3F, ClientUtil.reAlpha((new Color(((Double)this.red.getValueState()).intValue(), ((Double)this.green.getValueState()).intValue(), ((Double)this.blue.getValueState()).intValue())).getRGB(), ((Double)this.alpha.getValueState()).floatValue() / 255.0F));
            } else if (this.mode.isCurrentMode("2D")) {
               GlStateManager.rotate(-this.mc.getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
               GlStateManager.scale(-0.0267F, -0.0267F, 0.0267F);
               Gui.drawRect(-10, -18, 12, -20, ClientUtil.reAlpha((new Color(((Double)this.red.getValueState()).intValue(), ((Double)this.green.getValueState()).intValue(), ((Double)this.blue.getValueState()).intValue())).getRGB(), ((Double)this.alpha.getValueState()).floatValue() / 255.0F));
               Gui.drawRect(-10, 0, 12, -2, ClientUtil.reAlpha((new Color(((Double)this.red.getValueState()).intValue(), ((Double)this.green.getValueState()).intValue(), ((Double)this.blue.getValueState()).intValue())).getRGB(), ((Double)this.alpha.getValueState()).floatValue() / 255.0F));
               Gui.drawRect(10, -20, 12, 0, ClientUtil.reAlpha((new Color(((Double)this.red.getValueState()).intValue(), ((Double)this.green.getValueState()).intValue(), ((Double)this.blue.getValueState()).intValue())).getRGB(), ((Double)this.alpha.getValueState()).floatValue() / 255.0F));
               Gui.drawRect(-10, -20, -12, 0, ClientUtil.reAlpha((new Color(((Double)this.red.getValueState()).intValue(), ((Double)this.green.getValueState()).intValue(), ((Double)this.blue.getValueState()).intValue())).getRGB(), ((Double)this.alpha.getValueState()).floatValue() / 255.0F));
            }

            GL11.glPopMatrix();
         }
      }

      GL11.glColor4f(0.0F, 0.0F, 1.0F, 1.0F);
      GL11.glEnable(2929);
      GL11.glEnable(3553);
      GL11.glDisable(3042);
      GL11.glDisable(2848);
      GL11.glPopMatrix();
   }

   @EventTarget
   public void onRender(EventRender2D event) {
      if (this.mode.isCurrentMode("Shader")) {
         if (this.mc.gameSettings.ofFastRender) {
            this.set(false);
          //  ClientUtil.sendClientMessage("Options->Video Settings->Performance->Fast Render->Off", ClientNotification.Type.ERROR);
            return;
         }

        /* if (ModManager.getModByName("ESP").isEnabled() && ESP.modes.isCurrentMode("Shader")) {
            this.set(false);
            ClientUtil.sendClientMessage("Please disable ShaderESP", ClientNotification.Type.ERROR);
            return;
         }*/

       //  this.shader.startShader();
         Minecraft.getMinecraft().entityRenderer.setupCameraTransform(this.mc.timer.renderPartialTicks, 0);
         Iterator var3 = Minecraft.getMinecraft().theWorld.loadedEntityList.iterator();

         while(var3.hasNext()) {
            Object o = var3.next();
            Entity entity = (Entity)o;
            if (entity instanceof EntityItem) {
               Minecraft.getMinecraft().entityRenderer.disableLightmap();
               RenderHelper.disableStandardItemLighting();
               Render entityRender = this.mc.getRenderManager().getEntityRenderObject(entity);
               if (entityRender != null) {
                  RenderItem.field_175058_l = false;
                  entityRender.doRender(entity, interpolate(entity.posX, entity.lastTickPosX) - RenderManager.renderPosX, interpolate(entity.posY, entity.lastTickPosY) - RenderManager.renderPosY, interpolate(entity.posZ, entity.lastTickPosZ) - RenderManager.renderPosZ, this.mc.thePlayer.rotationYaw, this.mc.timer.renderPartialTicks);
                  RenderItem.field_175058_l = true;
               }
            }
         }

         Minecraft.getMinecraft().entityRenderer.disableLightmap();
         RenderHelper.disableStandardItemLighting();
         Minecraft.getMinecraft().entityRenderer.setupOverlayRendering();
       //  this.shader.stopShader();
         Gui.drawRect(0, 0, 0, 0, 0);
      }

   }

   public static double interpolate(double now, double then) {
      return then + (now - then) * (double)Minecraft.getMinecraft().timer.renderPartialTicks;
   }

   public double[] interpolate(Entity entity) {
      double posX = interpolate(entity.posX, entity.lastTickPosX) - RenderManager.renderPosX;
      double posY = interpolate(entity.posY, entity.lastTickPosY) - RenderManager.renderPosY;
      double posZ = interpolate(entity.posZ, entity.lastTickPosZ) - RenderManager.renderPosZ;
      return new double[]{posX, posY, posZ};
   }
   
   @EventTarget
	public void onUpdate(EventRender event) {
	   if (this.mode.isCurrentMode("2DBox")) {
		for (Object o : mc.theWorld.loadedEntityList) {
   		if (!(o instanceof EntityItem)) continue;
   		EntityItem item = (EntityItem)o;
		   	double var10000 = item.posX;
		   	Minecraft.getMinecraft().getRenderManager();
		   	double x = var10000 - RenderManager.renderPosX;
		   	var10000 = item.posY + 0.5D;
		   	Minecraft.getMinecraft().getRenderManager();
		   	double y = var10000 - RenderManager.renderPosY;
		   	var10000 = item.posZ;
		   	Minecraft.getMinecraft().getRenderManager();
		   	double z = var10000 - RenderManager.renderPosZ;
		   	GL11.glEnable(3042);
		   	GL11.glLineWidth(2.0F);
		   	GL11.glColor4f(1, 1, 1, .75F);
		   	GL11.glDisable(3553);
		   	GL11.glDisable(2929);
		   	GL11.glDepthMask(false);
	   		RenderUtil.drawOutlinedBoundingBox(new AxisAlignedBB(x - .2D, y - 0.3d, z - .2D, x + .2D, y - 0.4d, z + .2D));
	   		GL11.glColor4f(1, 1, 1, 0.15f);
	   		RenderUtil.drawBoundingBox(new AxisAlignedBB(x - .2D, y - 0.3d, z - .2D, x + .2D, y - 0.4d, z + .2D));
	   		GL11.glEnable(3553);
	   		GL11.glEnable(2929);
	   		GL11.glDepthMask(true);
	   		GL11.glDisable(3042);
   	}
	 }
  }
   @Override
   public void onDisable() {
       super.onDisable();

   }
   public void onEnable() {
   	super.isEnabled();

   }
}
