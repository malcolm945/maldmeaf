package cn.Judgment.mod.mods.COMBAT;

import cn.Judgment.Value;
import cn.Judgment.mod.Category;
import cn.Judgment.mod.Mod;

public class Hitbox extends Mod {

	public static Value<Double> size = new Value("Hitbox_Size",0.5d, 0.1d, 1.0d, 0.1d);
	
	public Hitbox() {
		super("Hitbox", Category.COMBAT);
	}

}
