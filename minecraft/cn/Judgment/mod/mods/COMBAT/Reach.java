package cn.Judgment.mod.mods.COMBAT;

import cn.Judgment.Value;
import cn.Judgment.mod.Category;
import cn.Judgment.mod.Mod;

public class Reach extends Mod {
	
	public static Value<Double> range = new Value("Reach_Range", 4.5d, 3d, 10.0d, 0.1d);
	
	public Reach() {
		super("Reach", Category.COMBAT);
	}

}
