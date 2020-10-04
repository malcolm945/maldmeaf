package cn.Judgment.mod.mods.PLAYER;

import com.darkmagician6.eventapi.EventTarget;

import cn.Judgment.events.EventChat;
import cn.Judgment.mod.Category;
import cn.Judgment.mod.Mod;
import net.minecraft.client.Minecraft;

public class AutoRejoin extends Mod {
   public AutoRejoin() {
      super("AutoRejoin", Category.PLAYER);
   }

   @EventTarget
   public void onChat(EventChat e) {
      if(e.getMessage().contains("你的网络连接出现小问题")) {
         Minecraft var10000 = mc;
         Minecraft.thePlayer.sendChatMessage("/rejoin");
      }

   }
}
