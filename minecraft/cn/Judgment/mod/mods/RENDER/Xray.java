package cn.Judgment.mod.mods.RENDER;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;

import cn.Judgment.Value;
import cn.Judgment.mod.Category;
import cn.Judgment.mod.Mod;
import cn.Judgment.util.Helper;
import net.minecraft.util.BlockPos;

public class Xray extends Mod {
	public static Value<Boolean> Bypass = new Value("Xray_Cave", false);
	public Value<Double> Opacity = new Value<Double>("Xray_Opacity", 160.0, 0.0, 255.0, 5.0);
	private static HashSet blockIDs = new HashSet();
	public static ArrayList<BlockPos> glow;
	public static ArrayList<Integer> blocks;
	Thread updater;
	public boolean loaded;
	
	List KEY_IDS = Lists.newArrayList(new Integer[] { 10, 11, 8, 9, 14, 15, 16, 21, 41, 42, 46, 48, 52, 56, 57, 61, 62,
			73, 74, 84, 89, 103, 116, 117, 118, 120, 129, 133, 137, 145, 152, 153, 154 });
	
	static {
	    	Xray.glow = new ArrayList<BlockPos>();
	    	Xray.blocks = new ArrayList<Integer>();
		}
    public Xray() {
        super("Xray", Category.WORLD);

    }
   

	@Override
	public void onEnable() {
		super.onEnable();
		Helper.dimblock.clear();
		blockIDs.clear();
		Helper.glow.clear();

		try {
			Iterator var1 = this.KEY_IDS.iterator();

			while (var1.hasNext()) {
				Integer o = (Integer) var1.next();
				blockIDs.add(o);
				Xray.blocks.add(o);
			}
		} catch (Exception var3) {
			var3.printStackTrace();
		}
		Helper.bypass = Bypass.getValueState()? true : false;
		Helper.opacity =  Opacity.getValueState().intValue();
		Helper.blockIDs = blockIDs;
		Helper.xray = true;
		mc.renderGlobal.loadRenderers();
		final int var0 = (int) mc.thePlayer.posX;
		final int var = (int) mc.thePlayer.posY;
		final int var2 = (int) mc.thePlayer.posZ;
		mc.renderGlobal.markBlockRangeForRenderUpdate(var0 - 900, var - 900, var2 - 900, var0 + 900, var + 900,
				var2 + 900);

		loaded = true;

	}

	@Override
	public void onDisable() {
		super.onDisable();
		Helper.xray = false;
		mc.renderGlobal.loadRenderers();
		Helper.dimblock.clear();
		loaded = false;
		try {
			this.updater.interrupt();
			this.updater = null;
		} catch (Exception ex) {
		}
	}

}