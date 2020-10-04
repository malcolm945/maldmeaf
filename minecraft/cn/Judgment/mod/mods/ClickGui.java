package cn.Judgment.mod.mods;

import org.lwjgl.input.Keyboard;

import cn.Judgment.Client;
import cn.Judgment.Value;
import cn.Judgment.mod.Category;
import cn.Judgment.mod.Mod;

public class ClickGui extends Mod {
	public static Value<Boolean> snowflake = new Value("ClickGui_snowflake", false);
	public static Value<Boolean> Sound = new Value("ClickGui_Sound", true);
	public static Value<Boolean> Open = new Value("ClickGui_Prompt", true);
	public static Value<Boolean> Cape = new Value("ClickGui_Cape", true);
	public ClickGui() {
		super("ClickGui", Category.RENDER);
		this.setKey(Keyboard.KEY_RSHIFT);
	}
	
	@Override
	public void onEnable() {
		this.mc.displayGuiScreen(Client.instance.showClickGui());
		this.set(false);
	}

}
