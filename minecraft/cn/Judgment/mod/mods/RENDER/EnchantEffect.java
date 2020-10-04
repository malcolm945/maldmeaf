package cn.Judgment.mod.mods.RENDER;

import cn.Judgment.Value;
import cn.Judgment.mod.Category;
import cn.Judgment.mod.Mod;


	public class EnchantEffect extends Mod{
		public static Value<Double> r = new Value("EnchantEffect_Red",255d, 0d, 255d, 1d);
		public static Value<Double> g = new Value("EnchantEffect_Green",255d, 0d, 255d, 1d);
		public static Value<Double> b = new Value("EnchantEffect_Blue",255d, 0d, 255d, 1d);
		public EnchantEffect() {
			super("EnchantEffect", Category.RENDER);
		}
		
		
		
		
	}

