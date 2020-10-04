package cn.Judgment.mod.mods.MOVEMENT;

import org.lwjgl.input.Keyboard;

import com.darkmagician6.eventapi.EventTarget;

import cn.Judgment.Value;
import cn.Judgment.events.EventUpdate;
import cn.Judgment.mod.Category;
import cn.Judgment.mod.Mod;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.KeyBinding;

public class InvMove extends Mod {
	
	public Value<Boolean> aacp = new Value("InvMove_AACP", false);
	
	public InvMove() {
		super("InvMove", Category.MOVEMENT);
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		if (this.mc.currentScreen != null && !(this.mc.currentScreen instanceof GuiChat)) {
			KeyBinding[] key = { this.mc.gameSettings.keyBindForward, this.mc.gameSettings.keyBindBack, this.mc.gameSettings.keyBindLeft, this.mc.gameSettings.keyBindRight, this.mc.gameSettings.keyBindSprint, this.mc.gameSettings.keyBindJump };
			KeyBinding[] array;
			for (int length = (array = key).length, i = 0; i < length; ++i) {
				KeyBinding b = array[i];
				KeyBinding.setKeyBindState(b.getKeyCode(), Keyboard.isKeyDown(b.getKeyCode()));
			}
			if(this.aacp.getValueState().booleanValue()) {
				mc.thePlayer.setSprinting(false);
			}
		}
	}
}
