package cn.Judgment.mod.mods.WORLD;

import com.darkmagician6.eventapi.EventTarget;

import cn.Judgment.events.EventUpdate;
import cn.Judgment.mod.Category;
import cn.Judgment.mod.Mod;
import net.minecraft.item.ItemBlock;

public class FastPlace extends Mod {
	
	public FastPlace() {
		super("FastPlace", Category.WORLD);
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		if(mc.thePlayer.inventory.getCurrentItem() != null) {
			if(mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBlock) {
				mc.rightClickDelayTimer = 0;
			} else {
				mc.rightClickDelayTimer = 4;
			}
		}
	}
	
	@Override
	public void onDisable() {
		mc.rightClickDelayTimer = 4;
		super.onDisable();
	}
}
