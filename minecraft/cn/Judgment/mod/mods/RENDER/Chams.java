package cn.Judgment.mod.mods.RENDER;

import org.lwjgl.opengl.GL11;

import com.darkmagician6.eventapi.EventTarget;

import cn.Judgment.Value;
import cn.Judgment.events.EventPostRenderPlayer;
import cn.Judgment.events.EventPreRenderPlayer;
import cn.Judgment.mod.Category;
import cn.Judgment.mod.Mod;


public class Chams extends Mod{
	public Chams() {
		super("Chams", Category.RENDER);
		
	}
	
	@EventTarget
	  private void preRenderPlayer(EventPreRenderPlayer e) {
		   GL11.glEnable(32823);
         GL11.glPolygonOffset(0.0F, -1100000.0F);
      
  }
	
	@EventTarget
  private void postRenderPlayer(EventPostRenderPlayer e) {
		GL11.glDisable(32823);
       GL11.glPolygonOffset(1.0F, 1100000.0F);
  }
	
}
