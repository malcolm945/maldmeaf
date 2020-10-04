/*
 * Decompiled with CFR 0.149.
 */
package cn.Judgment.util;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;

public final class PlaceInfo {
    private final BlockPos blockPos;
    private final EnumFacing enumFacing;
    private Vec3 vec3;
    public static final Companion Companion = new Companion();

    public PlaceInfo(BlockPos blockPos, EnumFacing enumFacing, Vec3 vec3) {
        this.blockPos = blockPos;
        this.enumFacing = enumFacing;
        this.vec3 = vec3;
    }

    public final BlockPos getBlockPos() {
        return this.blockPos;
    }

    public final EnumFacing getEnumFacing() {
        return this.enumFacing;
    }

    public final Vec3 getVec3() {
        return this.vec3;
    }

    public final void setVec3(Vec3 vec3) {
        this.vec3 = vec3;
    }

    public PlaceInfo(BlockPos blockPos, EnumFacing enumFacing, Vec3 vec3, int n) {
        this(blockPos, enumFacing, vec3);
        if ((n & 4) != 0) {
            vec3 = new Vec3((double)blockPos.getX() + 0.5, (double)blockPos.getY() + 0.5, (double)blockPos.getZ() + 0.5);
        }
    }

    public static final PlaceInfo get(BlockPos blockPos) {
        return Companion.get(blockPos);
    }

    public static final Block getBlock(BlockPos blockPos) {
        IBlockState var1;
        Minecraft.getMinecraft();
        WorldClient var10000 = Minecraft.theWorld;
        if (var10000 != null && (var1 = var10000.getBlockState(blockPos)) != null) {
            Block var2 = var1.getBlock();
            return var2;
        }
        Block var2 = null;
        return var2;
    }

    public static final IBlockState getState(BlockPos blockPos) {
        Minecraft.getMinecraft();
        IBlockState var10000 = Minecraft.theWorld.getBlockState(blockPos);
        return var10000;
    }

    public static final boolean canBeClicked(BlockPos blockPos) {
        Block var10000 = PlaceInfo.getBlock(blockPos);
        if (var10000 != null && var10000.canCollideCheck(PlaceInfo.getState(blockPos), false)) {
            Minecraft.getMinecraft();
            WorldClient var1 = Minecraft.theWorld;
            if (var1.getWorldBorder().contains(blockPos)) {
                boolean var2 = true;
                return var2;
            }
        }
        boolean var2 = false;
        return var2;
    }
}

