package cn.Judgment.mod.mods.RENDER;

import com.darkmagician6.eventapi.EventTarget;

import cn.Judgment.events.EventUpdate;
import cn.Judgment.mod.Category;
import cn.Judgment.mod.Mod;
import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class FullBright extends Mod {
	
	public FullBright() {
		super("FullBright", Category.RENDER);
	}

	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		mc.thePlayer.addPotionEffect(new PotionEffect(Potion.nightVision.getId(), 5200, 1));
		 this.mc.gameSettings.gammaSetting = 10.0f;
	}
	
	@Override
	
	public void onDisable() {
		super.onDisable();
		 this.mc.gameSettings.gammaSetting = 1.0f;
		 this.mc.thePlayer.removePotionEffect(Potion.nightVision.getId());
	}
}
