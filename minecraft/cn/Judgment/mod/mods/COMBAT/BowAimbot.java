package cn.Judgment.mod.mods.COMBAT;

import com.darkmagician6.eventapi.EventTarget;

import cn.Judgment.Value;
import cn.Judgment.events.EventPreMotion;
import cn.Judgment.mod.Category;
import cn.Judgment.mod.Mod;
import cn.Judgment.util.ClientUtil;
import cn.Judgment.util.CombatUtil;
import cn.Judgment.util.PlayerUtil;
import cn.Judgment.util.Wrapper;

import java.util.Iterator;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBow;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C07PacketPlayerDigging.Action;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class BowAimbot extends Mod {
   public Value attackPlayers = new Value("BowAimbot_AttackPlayers", Boolean.valueOf(true));
   public Value attackAnimals = new Value("BowAimbot_AttackAnimals", Boolean.valueOf(true));
   public Value attackMobs = new Value("BowAimbot_AttackMobs", Boolean.valueOf(true));
   Value prediction = new Value("BowAimbot_Prediction", Boolean.valueOf(true));
   public Value auto = new Value("BowAimbot_AutoShot", Boolean.valueOf(true));
   private Value mode = new Value("BowAimbot", "Mode", 0);
   private EntityLivingBase currentTarget;
   private Random random = new Random();
   private float oldYaw;
   private float oldPitch;
   private double maxAttackDistance = 60.0D;

   public BowAimbot() {
      super("BowAimbot", Category.COMBAT);
      this.mode.addValue("Legit");
      this.mode.addValue("YawHead");
      this.mode.addValue("Gyroscope");
   }

   @EventTarget
   public void onUpdate(EventPreMotion event) {
      label24: {
         this.showValue = this.mode;
         Minecraft var10000 = mc;
         if(Minecraft.thePlayer.getHeldItem() != null) {
            var10000 = mc;
            if(Minecraft.thePlayer.getHeldItem().getItem() instanceof ItemBow) {
               var10000 = mc;
               if(Minecraft.thePlayer.isUsingItem()) {
                  if(this.currentTarget == null) {
                     this.currentTarget = this.getClosestEntity();
                  } else if(this.isValid(this.currentTarget)) {
                     this.doAimbot(event);
                  } else {
                     this.currentTarget = null;
                  }
                  break label24;
               }
            }
         }

         this.currentTarget = null;
      }

      if(this.currentTarget == null) {
         this.oldYaw = this.oldPitch = 0.0F;
      }

   }

   private void doAimbot(EventPreMotion event) {
      float[] rotations = CombatUtil.getRotations(this.currentTarget);
      Minecraft var10000 = mc;
      int var17 = Minecraft.thePlayer.getHeldItem().getMaxItemUseDuration();
      Minecraft var10001 = mc;
      int i = var17 - Minecraft.thePlayer.getItemInUseCount();
      float f = (float)i / 20.0F;
      f = (f * f + f * 2.0F) / 3.0F;
      if(f > 1.0F) {
         f = 1.0F;
      }

      var10001 = mc;
      double diffX = this.currentTarget.posX - Minecraft.thePlayer.posX;
      double var18 = this.currentTarget.posY + (double)(this.currentTarget.getEyeHeight() / 2.0F);
      var10001 = mc;
      Minecraft var10002 = mc;
      double diffY = var18 - (Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight());
      var10001 = mc;
      double diffZ = this.currentTarget.posZ - Minecraft.thePlayer.posZ;
      double dist = Math.sqrt(diffX * diffX + diffZ * diffZ);
      float step = 0.006F;
      float rotYaw = rotations[0];
      float addPitch = (float)(Math.pow((double)f, 4.0D) - (double)step * ((double)step * dist * dist + 2.0D * diffY * (double)(f * f)));
      float rotPitch = (float)(-((double)((float)Math.atan(((double)(f * f) - Math.sqrt((double)addPitch)) / ((double)step * dist)) * 180.0F) / 3.141592653589793D));
      if(!Float.isNaN(rotPitch)) {
         if(((Boolean)this.prediction.getValueState()).booleanValue()) {
            float var19 = rotYaw - this.oldYaw;
            var10002 = mc;
            rotYaw += var19 * Math.min(Minecraft.thePlayer.getDistanceToEntity(this.currentTarget), 15.0F);
         }

         this.oldYaw = rotYaw;
         this.oldPitch = rotPitch;
         if(PlayerUtil.getSpeed() > 0.0D) {
            double var21 = (double)this.oldYaw;
            double var22 = PlayerUtil.getSpeed() / 2.0D;
            Minecraft var10003 = mc;
            this.oldYaw = (float)(var21 + var22 * (double)Math.min(Minecraft.thePlayer.getDistanceToEntity(this.currentTarget), 15.0F) * (double)(rotYaw - this.oldYaw) / (double)Math.abs(rotYaw - this.oldYaw));
         }

         if(this.mode.isCurrentMode("Legit")) {
            var10000 = mc;
            Minecraft.thePlayer.rotationYaw = rotYaw;
            var10000 = mc;
            Minecraft.thePlayer.rotationPitch = rotPitch;
         } else {
            event.yaw = rotYaw;
            event.pitch = rotPitch;
            if(this.mode.isCurrentMode("YawHead")) {
               Wrapper.setLook(event.yaw, event.pitch);
            }
         }

         var10000 = mc;
         if(Minecraft.thePlayer.getItemInUseDuration() > 20 && ((Boolean)this.auto.getValueState()).booleanValue()) {
            var10000 = mc;
            NetHandlerPlayClient var20 = Minecraft.thePlayer.sendQueue;
            Action var23 = Action.RELEASE_USE_ITEM;
            Minecraft var10004 = mc;
            BlockPos var24 = Minecraft.thePlayer.getPosition();
            Minecraft var10005 = mc;
            var20.addToSendQueue(new C07PacketPlayerDigging(var23, var24, EnumFacing.fromAngle((double)Minecraft.thePlayer.rotationYaw)));
            var10000 = mc;
            Minecraft.thePlayer.stopUsingItem();
         }
      }

   }

   private EntityLivingBase getClosestEntity() {
      EntityLivingBase closest = null;
      Minecraft var10000 = mc;
      Iterator var3 = Minecraft.theWorld.playerEntities.iterator();

      while(var3.hasNext()) {
         Entity e = (Entity)var3.next();
         if(e instanceof EntityLivingBase && this.isValid((EntityLivingBase)e)) {
            if(closest == null) {
               closest = (EntityLivingBase)e;
            } else {
               var10000 = mc;
               float var4 = Minecraft.thePlayer.getDistanceToEntity(e);
               Minecraft var10001 = mc;
               if(var4 < Minecraft.thePlayer.getDistanceToEntity(closest)) {
                  closest = (EntityLivingBase)e;
               }
            }
         }
      }

      return closest;
   }

   private boolean isValid(EntityLivingBase entity) {
      boolean var10000;
      if(entity.isDead) {
         var10000 = false;
      } else {
         Minecraft var10001 = mc;
         if(entity == Minecraft.thePlayer) {
            var10000 = false;
         } else if(entity instanceof EntityAnimal && !((Boolean)this.attackAnimals.getValueState()).booleanValue()) {
            var10000 = false;
         } else if(entity instanceof EntityMob && !((Boolean)this.attackMobs.getValueState()).booleanValue()) {
            var10000 = false;
         } else if(entity instanceof EntityPlayer && !((Boolean)this.attackPlayers.getValueState()).booleanValue()) {
            var10000 = false;
         } else {
            if(entity instanceof EntityPlayer && entity instanceof EntityPlayer && !entity.isInvisible()) {
               Minecraft var10002 = mc;
               double var2 = Minecraft.thePlayer.posX;
               Minecraft var10003 = mc;
               Minecraft var10004 = mc;
               double var3 = Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight();
               var10004 = mc;
               if(!ClientUtil.isBlockBetween(new BlockPos(var2, var3, Minecraft.thePlayer.posZ), new BlockPos(entity.posX, entity.posY + (double)entity.getEyeHeight(), entity.posZ))) {
                  var10000 = true;
                  return var10000;
               }
            }

            var10000 = false;
         }
      }

      return var10000;
   }
}
