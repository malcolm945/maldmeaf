package cn.Judgment.mod.mods.MOVEMENT;

import com.darkmagician6.eventapi.EventTarget;

import cn.Judgment.Value;
import cn.Judgment.events.EventPostMotion;
import cn.Judgment.events.EventPreMotion;
import cn.Judgment.mod.Category;
import cn.Judgment.mod.Mod;
import cn.Judgment.mod.mods.COMBAT.Killaura;
import cn.Judgment.util.PlayerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C07PacketPlayerDigging.Action;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class NoSlow extends Mod {
   public static Value mods = new Value("NoSlowDown", "Mode", 0);

   public NoSlow() {
      super("NoSlowDown", Category.MOVEMENT);
      mods.addValue("Vanilla");
      mods.addValue("NCP");
   }

   @EventTarget
   public void onPre(EventPreMotion e) {
       this.setDisplayName(mods.getModeAt(mods.getCurrentMode()));
      if(!mods.isCurrentMode("Vanilla")) {
         if(mods.isCurrentMode("NCP")) {
            Minecraft var10000 = mc;
            if(Minecraft.thePlayer.isBlocking() && PlayerUtil.isMoving2()) {
               var10000 = mc;
               Minecraft.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
            }
         }

      }
   }

   @EventTarget
   public void onPost(EventPostMotion e) {
      if(!mods.isCurrentMode("Vanilla")) {
         if(mods.isCurrentMode("NCP")) {
            Minecraft var10000 = mc;
            if(Minecraft.thePlayer.isBlocking() && PlayerUtil.isMoving2() && Killaura.target == null) {
               var10000 = mc;
               Minecraft var10003 = mc;
               Minecraft.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(Minecraft.thePlayer.inventory.getCurrentItem()));
            }
         }

      }
   }

   public static boolean isOnGround(double height) {
      Minecraft var10000 = mc;
      Minecraft var10001 = mc;
      Minecraft var10002 = mc;
      return !Minecraft.theWorld.getCollidingBoundingBoxes(Minecraft.thePlayer, Minecraft.thePlayer.getEntityBoundingBox().offset(0.0D, -height, 0.0D)).isEmpty();
   }
}
