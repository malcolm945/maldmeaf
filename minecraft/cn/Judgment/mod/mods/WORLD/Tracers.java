package cn.Judgment.mod.mods.WORLD;

import java.util.Iterator;

import org.lwjgl.opengl.GL11;

import com.darkmagician6.eventapi.EventTarget;

import cn.Judgment.events.EventRender;
import cn.Judgment.mod.Category;
import cn.Judgment.mod.Mod;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;

public class Tracers
extends Mod {
    public Tracers() {
    	super("Tracers", Category.RENDER);
    }

    @EventTarget
    public void onRender(EventRender event) {
       Iterator var3 = this.mc.theWorld.playerEntities.iterator();

       while(var3.hasNext()) {
          EntityPlayer player = (EntityPlayer)var3.next();
          if (this.mc.thePlayer != player && !player.isInvisible()) {
             double posX = player.posX;
             double posY = player.posY;
             double posZ = player.posZ;
             this.drawLine(player);
          }
       }

    }

    private void drawLine(EntityPlayer player) {
        this.mc.getRenderManager();
        double x = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double)this.mc.timer.renderPartialTicks - RenderManager.renderPosX;
        this.mc.getRenderManager();
        double y = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double)this.mc.timer.renderPartialTicks - RenderManager.renderPosY;
        this.mc.getRenderManager();
        double z = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double)this.mc.timer.renderPartialTicks - RenderManager.renderPosZ;
        GL11.glPushMatrix();
        GL11.glEnable((int)3042);
        GL11.glEnable((int)2848);
        GL11.glDisable((int)2929);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glLineWidth((float)1.5f);
        float DISTANCE = this.mc.thePlayer.getDistanceToEntity(player);
        if (DISTANCE <= 200.0f) {
            GL11.glColor3f((float)1.0f, (float)(DISTANCE / 40.0f), (float)0.0f);
        }
        GL11.glLoadIdentity();
        boolean bobbing = this.mc.gameSettings.viewBobbing;
        this.mc.gameSettings.viewBobbing = false;
        this.mc.entityRenderer.orientCamera(this.mc.timer.renderPartialTicks);
        GL11.glBegin((int)3);
        GL11.glVertex3d((double)0.0, (double)this.mc.thePlayer.getEyeHeight(), (double)0.0);
        GL11.glVertex3d((double)x, (double)y, (double)z);
        GL11.glVertex3d((double)x, (double)(y + (double)player.getEyeHeight()), (double)z);
        GL11.glEnd();
        this.mc.gameSettings.viewBobbing = bobbing;
        GL11.glEnable((int)3553);
        GL11.glEnable((int)2929);
        GL11.glDisable((int)2848);
        GL11.glDisable((int)3042);
        GL11.glPopMatrix();
    }
}