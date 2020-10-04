package cn.Judgment.mod.mods.WORLD;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.darkmagician6.eventapi.EventTarget;

import cn.Judgment.Value;
import cn.Judgment.events.EventChat;
import cn.Judgment.events.EventPreMotion;
import cn.Judgment.events.EventRender2D;
import cn.Judgment.events.EventUpdate;
import cn.Judgment.mod.Category;
import cn.Judgment.mod.Mod;
import cn.Judgment.ui.ClientNotification;
import cn.Judgment.util.ClientUtil;
import cn.Judgment.util.PlayerUtil;
import cn.Judgment.util.timeUtils.TimeHelper;
import cn.Judgment.util.timeUtils.TimeUtil;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

public class ModCheck extends Mod {

	private String[] modlist = new String[] {"绅士龙","时光易老不忘初心","crazyforlove","造化钟神秀","chen_duxiu","魂魄妖梦","Lyra2REv2","chen_xixi","tanker_01","Owenkill","heav3ns","SnowDay","Chrisan","小阿狸","bingmo","hefew","mxu","StartOver_","IntelXeonE"};	private String modname;
	private TimeHelper timer = new TimeHelper();
	private List<String> offlinemod = new ArrayList();
	private List<String> onlinemod = new ArrayList();
	private Value<Boolean> showOffline = new Value("ModCheck_ShowOffline", true);
	private Value<Boolean> showOnline = new Value("ModCheck_ShowOnline", true);
	
	private int counter;
	private boolean isFinished;
	
    public ModCheck() {
        super("ModCheck", Category.WORLD);
    }
	
	@EventTarget
	public void onRender(EventRender2D e) {
		FontRenderer font = mc.fontRendererObj;
		List<String> listArray = Arrays.asList(modlist);
		listArray.sort((o1, o2) -> {
			return font.getStringWidth(o2) - font.getStringWidth(o1);
		});
		int counter2 = 0;
		for(String mods : listArray) {
			if(offlinemod.contains(mods) && showOffline.getValueState()) {
				font.drawStringWithShadow(mods, 5, 120 + counter2 * 10, Color.RED.getRGB());
				counter2++;
			}
			if(onlinemod.contains(mods) && showOnline.getValueState()) {
				font.drawStringWithShadow(mods, 5, 120 + counter2 * 10, Color.GREEN.getRGB());
				counter2++;
			}
			
		}
	}
	
	@EventTarget
	public void onChat(EventChat e) {
		if(e.getMessage().contains("这名玩家不在线！") || e.getMessage().contains("That player is not online!")) {
			e.setCancelled(true);
			if(onlinemod.contains(modname)) {
				ClientUtil.sendClientMessage("[Kyra Client] " + modname + " 已下线！", ClientNotification.Type.INFO);
				onlinemod.remove(modname);
				offlinemod.add(modname);
				return;
			}
			if(!offlinemod.contains(modname)) {
				ClientUtil.sendClientMessage("[Kyra Client] " + modname + " 不在线！", ClientNotification.Type.INFO);
				offlinemod.add(modname);
			}
		}
		
		if(e.getMessage().contains("You cannot message this player.")) {
			e.setCancelled(true);
			if(offlinemod.contains(modname)) {
				ClientUtil.sendClientMessage("[Kyra Client] " + modname + " 已上线！", ClientNotification.Type.INFO);
				offlinemod.remove(modname);
				onlinemod.add(modname);
				return;
			}
			if(!onlinemod.contains(modname)) {
				ClientUtil.sendClientMessage("[Kyra Client] " + modname + " 在线！", ClientNotification.Type.INFO);
				onlinemod.add(modname);
			}
		}

		if(e.getMessage().contains("找不到名为 \"" + modname + "\" 的玩家")) {
			e.setCancelled(true);
		}
	}
	
	@EventTarget
	public void onUpdate(EventUpdate e) {
		if(timer.isDelayComplete(isFinished ? 10000L : 2000L)) {
			if(counter >= modlist.length) {
				counter = -1;
				if(!isFinished) {
					isFinished = true;
				}
				
			}
			counter++;
			modname = modlist[counter];
			mc.thePlayer.sendChatMessage("/message " + modname + " hi");
			timer.reset();
		}
	}

}
