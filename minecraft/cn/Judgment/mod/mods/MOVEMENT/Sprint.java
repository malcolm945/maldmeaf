package cn.Judgment.mod.mods.MOVEMENT;

import com.darkmagician6.eventapi.EventTarget;

import cn.Judgment.Value;
import cn.Judgment.events.EventUpdate;
import cn.Judgment.mod.Category;
import cn.Judgment.mod.Mod;

public class Sprint extends Mod {
	
	public Value<Boolean> omni = new Value("Sprint_Omni", false);
	
	public Sprint() {
		super("Sprint", Category.MOVEMENT);
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		if (this.mc.thePlayer.getFoodStats().getFoodLevel() > 6 && this.omni.getValueState().booleanValue() != false ? this.mc.thePlayer.moving() : this.mc.thePlayer.moveForward > 0.0f) {
		 this.mc.thePlayer.setSprinting(true);
		}
	}
	
	@Override
	public void onDisable() {
		mc.thePlayer.setSprinting(false);
		super.onDisable();
	}
}
