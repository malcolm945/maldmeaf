/*
 * Decompiled with CFR 0.149.
 */
package cn.Judgment.util;

import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

class Companion {
    public final PlaceInfo get(BlockPos blockPos) {
        if (PlaceInfo.canBeClicked(blockPos.add(0, -1, 0))) {
            BlockPos blockPos2 = blockPos.add(0, -1, 0);
            return new PlaceInfo(blockPos2, EnumFacing.UP, null, 4);
        }
        if (PlaceInfo.canBeClicked(blockPos.add(0, 0, 1))) {
            BlockPos blockPos3 = blockPos.add(0, 0, 1);
            return new PlaceInfo(blockPos3, EnumFacing.NORTH, null, 4);
        }
        if (PlaceInfo.canBeClicked(blockPos.add(-1, 0, 0))) {
            BlockPos blockPos4 = blockPos.add(-1, 0, 0);
            return new PlaceInfo(blockPos4, EnumFacing.EAST, null, 4);
        }
        if (PlaceInfo.canBeClicked(blockPos.add(0, 0, -1))) {
            BlockPos blockPos5 = blockPos.add(0, 0, -1);
            return new PlaceInfo(blockPos5, EnumFacing.SOUTH, null, 4);
        }
        if (PlaceInfo.canBeClicked(blockPos.add(1, 0, 0))) {
            BlockPos blockPos6 = blockPos.add(1, 0, 0);
            return new PlaceInfo(blockPos6, EnumFacing.WEST, null, 4);
        }
        PlaceInfo placeInfo = null;
        return placeInfo;
    }

    Companion() {
    }
}

