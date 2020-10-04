package cn.Judgment.mod.mods.MOVEMENT;

import com.darkmagician6.eventapi.EventTarget;

import cn.Judgment.Value;
import cn.Judgment.events.EventPacket;
import cn.Judgment.events.EventUpdate;
import cn.Judgment.mod.Category;
import cn.Judgment.mod.Mod;
import cn.Judgment.util.PlayerUtil;
import cn.Judgment.util.timeUtils.TimeHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.util.AxisAlignedBB;

public class NoFall extends Mod {
   private float b = 1.0F;
   private boolean c = true;
   public static boolean Set = false;
   public double ticks = 0.0D;
   private int d;
   private Value mode = new Value("NoFall", "Mode", 0);
   public TimeHelper timer = new TimeHelper();
   public TimeHelper NoFallDelay = new TimeHelper();
   private int times;
   private float lastFall;
   private boolean showed;
   private boolean showed2;
   private int ongroundtick;

   public NoFall() {
      super("NoFall", Category.PLAYER);
      this.mode.mode.add("Hypixel");
      this.mode.mode.add("Mineplex");
      this.mode.mode.add("AAC");
      this.showValue = this.mode;
   }

   @EventTarget
   public void onUpdate(EventUpdate e2) {
      Minecraft var10000;
      if(this.mode.isCurrentMode("Hypixel")) {
          this.setDisplayName("Hypixel");
			if(mc.thePlayer.fallDistance > 2F) {
	              mc.thePlayer.onGround = false;
	              mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
			   }
      } else if(this.mode.isCurrentMode("Mineplex")) {
         this.setDisplayName("Mineplex");
         var10000 = mc;
         if(Minecraft.thePlayer.fallDistance > 2.5F) {
            var10000 = mc;
            Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
            var10000 = mc;
            Minecraft.thePlayer.fallDistance = 0.5F;
         }
      }

   }

   public static boolean isBlockUnder() {
      Minecraft var10000 = mc;
      if(Minecraft.thePlayer.posY < 0.0D) {
         return false;
      } else {
         int off = 0;

         while(true) {
            Minecraft var10001 = mc;
            if(off >= (int)Minecraft.thePlayer.posY + 2) {
               return false;
            }

            var10000 = mc;
            AxisAlignedBB bb = Minecraft.thePlayer.boundingBox.offset(0.0D, (double)(-off), 0.0D);
            var10000 = mc;
            var10001 = mc;
            if(!Minecraft.theWorld.getCollidingBoundingBoxes(Minecraft.thePlayer, bb).isEmpty()) {
               return true;
            }

            off += 2;
         }
      }
   }

   @EventTarget
   public void onPacket(EventPacket e) {
      if(this.mode.isCurrentMode("AAC") && e.getPacket() instanceof C03PacketPlayer) {
          this.setDisplayName("AAC");
         Minecraft var10000 = mc;
         if(Minecraft.thePlayer.fallDistance > 3.0F && this.timer.isDelayComplete(200L)) {
            var10000 = mc;
            Minecraft.thePlayer.fallDistance = 0.5F;
            this.timer.reset();
            Minecraft var10002 = mc;
            Minecraft var10004 = mc;
            C04PacketPlayerPosition p = new C04PacketPlayerPosition(Minecraft.thePlayer.posX, Double.NaN, Minecraft.thePlayer.posZ, true);
            var10000 = mc;
            Minecraft.thePlayer.sendQueue.addToSendQueue(p);
         }
      }

   }

   public void onEnable() {
      super.onEnable();
   }

   public void onDisable() {
      super.onDisable();
   }
}
