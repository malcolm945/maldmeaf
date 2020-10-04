package net.minecraft.block.state.pattern;

import com.google.common.base.Predicate;

import cn.Judgment.util.Helper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;

public class BlockHelper implements Predicate<IBlockState>
{
    private final Block block;

    private BlockHelper(Block blockType)
    {
        this.block = blockType;
    }

    public static BlockHelper forBlock(Block blockType)
    {
        return new BlockHelper(blockType);
    }

    public boolean apply(IBlockState p_apply_1_)
    {
        return p_apply_1_ != null && p_apply_1_.getBlock() == this.block;
    }

    
    public static Block getBlock(final double x, final double y, final double z) {
        return Helper.mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
    }
    
    public static boolean isInLiquid() {
        boolean inLiquid = false;
        final int y = (int)Helper.mc.thePlayer.getEntityBoundingBox().minY;
        for (int x = MathHelper.floor_double(Helper.mc.thePlayer.getEntityBoundingBox().minX); x < MathHelper.floor_double(Helper.mc.thePlayer.getEntityBoundingBox().maxX) + 1; ++x) {
            for (int z = MathHelper.floor_double(Helper.mc.thePlayer.getEntityBoundingBox().minZ); z < MathHelper.floor_double(Helper.mc.thePlayer.getEntityBoundingBox().maxZ) + 1; ++z) {
                final Block block = getBlock(x, y, z);
                if (block != null && !(block instanceof BlockAir)) {
                    if (!(block instanceof BlockLiquid)) {
                        return false;
                    }
                    inLiquid = true;
                }
            }
        }
        return inLiquid;
    }
}
