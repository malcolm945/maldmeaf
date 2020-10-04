package cn.Judgment.mod.mods.MOVEMENT;

import com.darkmagician6.eventapi.EventTarget;

import cn.Judgment.Value;
import cn.Judgment.events.EventMove;
import cn.Judgment.events.EventPreMotion;
import cn.Judgment.events.EventRender;
import cn.Judgment.mod.Category;
import cn.Judgment.mod.Mod;
import cn.Judgment.mod.ModManager;
import cn.Judgment.mod.mods.COMBAT.Killaura;
import cn.Judgment.util.PlayerUtil;
import cn.Judgment.util.RenderUtil;
import cn.Judgment.util.RotationUtil;

import java.awt.Color;
import java.util.Iterator;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;

public class TargetStrafe extends Mod {
   public static Value Radius = new Value("TargetStrafe_Radius", Double.valueOf(1.0D), Double.valueOf(0.0D), Double.valueOf(6.0D), 0.01D);
   public static Value ESP = new Value("TargetStrafe_ESP", Boolean.valueOf(true));
   public static Value WallCheck = new Value("TargetStrafe_WallCheck", Boolean.valueOf(true));
   private int direction = -1;

   public TargetStrafe() {
      super("TargetStrafe", Category.MOVEMENT);
   }

   private void switchDirection() {
      if(this.direction == 1) {
         this.direction = -1;
      } else {
         this.direction = 1;
      }

   }

   public final boolean doStrafeAtSpeed(EventMove event, double moveSpeed) {
      boolean strafe = this.canStrafe();
      if(strafe) {
         float[] rotations = RotationUtil.getRotations(Killaura.target);
         Minecraft var10000 = mc;
         if((double)Minecraft.thePlayer.getDistanceToEntity(Killaura.target) <= ((Double)Radius.getValueState()).doubleValue()) {
            PlayerUtil.setSpeed(event, moveSpeed, rotations[0], (double)this.direction, 0.0D);
         } else {
            PlayerUtil.setSpeed(event, moveSpeed, rotations[0], (double)this.direction, 1.0D);
         }
      }

      return strafe;
   }

   @EventTarget
   public final void onUpdate(EventPreMotion event) {
      Minecraft var10000 = mc;
      if(Minecraft.thePlayer.isCollidedHorizontally) {
         this.switchDirection();
      }

   }

   @EventTarget
   public final void onRender3D(EventRender event) {
      if(((Boolean)ESP.getValueState()).booleanValue()) {
         Minecraft var10000 = mc;
         Iterator var3 = Minecraft.theWorld.getLoadedEntityList().iterator();

         while(true) {
            Entity e2;
            do {
               if(!var3.hasNext()) {
                  return;
               }

               e2 = (Entity)var3.next();
            } while(!this.Check(e2) && e2 != Killaura.target);

            this.drawCircle(e2, event.getPartialTicks(), ((Double)Radius.getValueState()).doubleValue());
         }
      }
   }

   private boolean Check(Entity e2) {
      if(!e2.isEntityAlive()) {
         return false;
      } else {
         Minecraft var10001 = mc;
         return e2 == Minecraft.thePlayer?false:e2 instanceof EntityPlayer;
      }
   }

   private void drawCircle(Entity entity, float partialTicks, double rad) {
      GL11.glPushMatrix();
      GL11.glDisable(3553);
      RenderUtil.startDrawing();
      GL11.glDisable(2929);
      GL11.glDepthMask(false);
      GL11.glLineWidth(1.0F);
      GL11.glBegin(3);
      double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)partialTicks - mc.getRenderManager().viewerPosX;
      double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)partialTicks - mc.getRenderManager().viewerPosY;
      Color color = Color.WHITE;
      double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)partialTicks - mc.getRenderManager().viewerPosZ;
      if(entity == Killaura.target && ModManager.getModByName("Speed").isEnabled()) {
         color = Color.GREEN;
      }

      float r = 0.003921569F * (float)color.getRed();
      float g = 0.003921569F * (float)color.getGreen();
      float b = 0.003921569F * (float)color.getBlue();
      double pix2 = 6.283185307179586D;

      for(int i = 0; i <= 90; ++i) {
         GL11.glColor3f(r, g, b);
         GL11.glVertex3d(x + rad * Math.cos((double)i * 6.283185307179586D / 45.0D), y, z + rad * Math.sin((double)i * 6.283185307179586D / 45.0D));
      }

      GL11.glEnd();
      GL11.glDepthMask(true);
      GL11.glEnable(2929);
      RenderUtil.stopDrawing();
      GL11.glEnable(3553);
      GL11.glPopMatrix();
   }

   public boolean canStrafe() {
      return ModManager.getModByName("Killaura").isEnabled() && Killaura.target != null && this.isEnabled();
   }
}
