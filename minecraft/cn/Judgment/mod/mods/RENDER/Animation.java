package cn.Judgment.mod.mods.RENDER;

import java.util.ArrayList;

import cn.Judgment.Value;
import cn.Judgment.mod.Category;
import cn.Judgment.mod.Mod;

public class Animation extends Mod {
	
	public static Value<String> mode = new Value("Animation", "Mode", 0);
	public static Value<Boolean> sanimation = new Value("Animation_SwingAnimation", true);
	
	public Animation() {
		super("Animation", Category.RENDER);
		this.mode.mode.add("None");
		this.mode.mode.add("Push");
		this.mode.mode.add("BLC");
		this.mode.mode.add("BLC2");
		this.mode.mode.add("1.7");
		this.mode.mode.add("Luna");
		this.mode.mode.add("Exhibition");
		this.mode.mode.add("Sigma");
		this.mode.mode.add("Winter");
	}

}
