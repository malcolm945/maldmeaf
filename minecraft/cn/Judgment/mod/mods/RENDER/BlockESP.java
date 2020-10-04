package cn.Judgment.mod.mods.RENDER;

import java.util.ArrayList;

import com.darkmagician6.eventapi.EventTarget;

import cn.Judgment.Value;
import cn.Judgment.events.EventRender;
import cn.Judgment.events.EventRenderBlock;
import cn.Judgment.mod.Category;
import cn.Judgment.mod.Mod;
import cn.Judgment.util.RenderUtil;
import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;

public class BlockESP extends Mod {
	
	private static ArrayList<Integer> blockIds = new ArrayList();
    private ArrayList<BlockPos> toRender = new ArrayList();
    
    public Value<Double> limit = new Value("BlockESP_BlockLimit", 250d, 10d, 1000d, 1d);
    
	public BlockESP() {
		super("BlockESP", Category.RENDER);
	}
	
	@Override
    public void onEnable() {
        this.mc.renderGlobal.loadRenderers();
        this.toRender.clear();
        super.onEnable();
    }
	
	@EventTarget
    public void onRenderBlock(EventRenderBlock event) {
		BlockPos pos = new BlockPos(event.getX(), event.getY(), event.getZ());
		if ((double)this.toRender.size() < this.limit.getValueState().intValue() && !this.toRender.contains(pos) && blockIds.contains(new Integer(Block.getIdFromBlock(event.getBlock())))) {
            this.toRender.add(pos);
        }
        int i = 0;
        while (i < this.toRender.size()) {
            BlockPos pos_1 = this.toRender.get(i);
            int id = Block.getIdFromBlock(this.mc.theWorld.getBlockState(pos_1).getBlock());
            if (!blockIds.contains(id)) {
                this.toRender.remove(i);
            }
            ++i;
        }
	}
	
	@EventTarget
    public void onRender(EventRender event) {
        for (BlockPos pos : this.toRender) {
            this.renderBlock(pos);
        }
    }
	
	private void renderBlock(BlockPos pos) {
        this.mc.getRenderManager();
        double x = (double)pos.getX() - mc.getRenderManager().renderPosX;
        this.mc.getRenderManager();	
        double y = (double)pos.getY() - mc.getRenderManager().renderPosY;
        this.mc.getRenderManager();
        double z = (double)pos.getZ() - mc.getRenderManager().renderPosZ;
        RenderUtil.drawSolidBlockESP(x, y, z, 0.0f, 0.5f, 1.0f, 0.25f);
    }

    public static ArrayList<Integer> getBlockIds() {
        return blockIds;
    }
	
	
}
