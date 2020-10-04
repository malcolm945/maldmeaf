package cn.Judgment.mod.mods.WORLD;

import com.darkmagician6.eventapi.EventTarget;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import cn.Judgment.events.EventChat;
import cn.Judgment.events.EventRender;
import cn.Judgment.events.EventUpdate;
import cn.Judgment.events.EventWorldLoaded;
import cn.Judgment.mod.Category;
import cn.Judgment.mod.Mod;
import cn.Judgment.util.Colors;
import cn.Judgment.util.PlayerUtil;
import cn.Judgment.util.RenderUtil;
import cn.Judgment.util.TimeHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.opengl.GL11;

public class HideAndSeek extends Mod {
   public static List kids = new ArrayList();
   public TimeHelper timer = new TimeHelper();

   public HideAndSeek() {
      super("HideAndSeek", Category.WORLD);
   }

   public void onEnable() {
      kids.clear();
   }

   public void onDisable() {
      kids.clear();
   }

   @EventTarget
   public void onWorldLoaded(EventWorldLoaded e) {
      kids.clear();
   }

   @EventTarget
   public void onChat(EventChat e) {
      if(e.getMessage().contains("躲猫猫")) {
         this.timer.reset();
      }

   }

   @EventTarget
   public void onRender(EventRender e) {
      Iterator var3 = kids.iterator();

      while(var3.hasNext()) {
         EntityLivingBase entity = (EntityLivingBase)var3.next();
         if(entity == null) {
            return;
         }

         Color color = new Color(Colors.DARKRED.c);
         mc.getRenderManager();
         double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)mc.timer.renderPartialTicks - RenderManager.renderPosX;
         mc.getRenderManager();
         double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)mc.timer.renderPartialTicks - RenderManager.renderPosY;
         mc.getRenderManager();
         double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)mc.timer.renderPartialTicks - RenderManager.renderPosZ;
         double width;
         double height;
         if(entity instanceof EntityPlayer) {
            width = entity.isSneaking()?0.25D:0.0D;
            height = 0.275D;
            GL11.glPushMatrix();
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            double red = -0.25D * (double)(Math.abs(entity.rotationPitch) / 90.0F);
            GL11.glTranslated(0.0D, red, 0.0D);
            GL11.glTranslated((x -= 0.275D) + 0.275D, (y += (double)entity.getEyeHeight() - 0.225D - width) + 0.275D, (z -= 0.275D) + 0.275D);
            GL11.glRotated((double)(-entity.rotationYaw % 360.0F), 0.0D, 1.0D, 0.0D);
            GL11.glTranslated(-(x + 0.275D), -(y + 0.275D), -(z + 0.275D));
            GL11.glTranslated(x + 0.275D, y + 0.275D, z + 0.275D);
            GL11.glRotated((double)entity.rotationPitch, 1.0D, 0.0D, 0.0D);
            GL11.glTranslated(-(x + 0.275D), -(y + 0.275D), -(z + 0.275D));
            GL11.glDisable(3553);
            GL11.glEnable(2848);
            GL11.glDisable(2929);
            GL11.glDepthMask(false);
            GL11.glColor4f((float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F, 1.0F);
            GL11.glLineWidth(1.0F);
            RenderUtil.drawOutlinedBoundingBox(new AxisAlignedBB(x - 0.0025D, y - 0.0025D, z - 0.0025D, x + 0.55D + 0.0025D, y + 0.55D + 0.0025D, z + 0.55D + 0.0025D));
            GL11.glColor4f((float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F, 0.5F);
            RenderUtil.drawBoundingBox(new AxisAlignedBB(x - 0.0025D, y - 0.0025D, z - 0.0025D, x + 0.55D + 0.0025D, y + 0.55D + 0.0025D, z + 0.55D + 0.0025D));
            GL11.glDisable(2848);
            GL11.glEnable(3553);
            GL11.glEnable(2929);
            GL11.glDepthMask(true);
            GL11.glDisable(3042);
            GL11.glPopMatrix();
         } else {
            width = entity.getEntityBoundingBox().maxX - entity.getEntityBoundingBox().minX;
            height = entity.getEntityBoundingBox().maxY - entity.getEntityBoundingBox().minY + 0.25D;
            float red1 = 1.0F;
            float green = 0.0F;
            float blue = 0.0F;
            float alpha = 0.5F;
            float lineRed = 0.0F;
            float lineGreen = 0.5F;
            float lineBlue = 1.0F;
            float lineAlpha = 1.0F;
            float lineWdith = 2.0F;
            RenderUtil.drawEntityESP(x, y, z, width, height, 1.0F, 0.0F, 0.0F, 0.5F, 0.0F, 0.5F, 1.0F, 1.0F, 2.0F);
         }
      }

   }

   @EventTarget
   public void onUpdate(EventUpdate e) {
      Minecraft var10000 = mc;
      Iterator var3 = Minecraft.theWorld.loadedEntityList.iterator();

      while(var3.hasNext()) {
         Entity entity = (Entity)var3.next();
         if(entity instanceof EntityLivingBase && !(entity instanceof EntityPlayer) && !(entity instanceof EntityArmorStand) && !(entity instanceof EntityWither) && !kids.contains(entity) && !entity.getName().contains("§c§l") && this.timer.isDelayComplete(5000L)) {
            double pos = entity.posY - (double)((int)entity.posY);
            if(pos > 0.1D && String.valueOf(pos).length() > 8) {
               kids.add((EntityLivingBase)entity);
               PlayerUtil.tellPlayer("§b[Judgment]§a检测到一个异常动物:" + entity.getName());
            }
         }
      }

      var3 = kids.iterator();

      while(true) {
         EntityLivingBase entity1;
         do {
            if(!var3.hasNext()) {
               return;
            }

            entity1 = (EntityLivingBase)var3.next();
         } while(!entity1.isDead && entity1.getHealth() >= 0.0F);

         kids.remove(entity1);
      }
   }
}
