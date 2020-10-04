package cn.Judgment.mod.mods.RENDER;

import org.lwjgl.opengl.GL11;

import com.darkmagician6.eventapi.EventTarget;

import cn.Judgment.Value;
import cn.Judgment.events.EventRender;
import cn.Judgment.mod.Category;
import cn.Judgment.mod.Mod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;

public class ChestESP extends Mod {
	public static Value<Boolean> chest = new Value("ChestESP_chest", true);
	public static Value<Boolean> enderChest = new Value("ChestESP_enderChest", false);
	public ChestESP() {
		super("ChestESP", Category.RENDER);
		


	}

	@EventTarget
	public void onRender(EventRender event) {
		for (TileEntity tileentity : mc.theWorld.loadedTileEntityList) {
			if(tileentity instanceof TileEntityChest && this.chest.getValueState().booleanValue()) {
				renderChest(tileentity.getPos());
			}
			if(tileentity instanceof TileEntityEnderChest&& this.enderChest.getValueState().booleanValue()) {
				renderEnderChest(tileentity.getPos());
			}
		}
	}
	
	public static void renderChest(BlockPos blockPos) {
        double d0 = (double)blockPos.getX() - Minecraft.getMinecraft().getRenderManager().viewerPosX;
        double d1 = (double)blockPos.getY() - Minecraft.getMinecraft().getRenderManager().viewerPosY;
        double d2 = (double)blockPos.getZ() - Minecraft.getMinecraft().getRenderManager().viewerPosZ;
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glLineWidth(2.0F);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);
        GL11.glColor4d(255.0D, 170.0D, 0.0D, 1.0D);
        RenderGlobal.func_181561_a(new AxisAlignedBB(d0, d1, d2, d0 + 1.0D, d1 + 1.0D, d2 + 1.0D));
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }

    public static void renderEnderChest(BlockPos blockPos) {
        double d0 = (double)blockPos.getX() - Minecraft.getMinecraft().getRenderManager().viewerPosX;
        double d1 = (double)blockPos.getY() - Minecraft.getMinecraft().getRenderManager().viewerPosY;
        double d2 = (double)blockPos.getZ() - Minecraft.getMinecraft().getRenderManager().viewerPosZ;
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glLineWidth(2.0F);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);
        GL11.glColor4d(170.0D, 0.0D, 170.0D, 1.0D);
        RenderGlobal.func_181561_a(new AxisAlignedBB(d0, d1, d2, d0 + 1.0D, d1 + 1.0D, d2 + 1.0D));
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }
}
