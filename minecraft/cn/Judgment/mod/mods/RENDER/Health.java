package cn.Judgment.mod.mods.RENDER;

import java.awt.Color;

import com.darkmagician6.eventapi.EventTarget;

import cn.Judgment.events.EventRender2D;
import cn.Judgment.mod.Category;
import cn.Judgment.mod.Mod;
import net.minecraft.client.gui.ScaledResolution;

public class Health extends Mod{
	
	public Health() {
		super("Health", Category.RENDER);
	}

	@EventTarget
	public void onEvent(EventRender2D event) {
		 ScaledResolution sr = new ScaledResolution(this.mc);
		 
          String health = "" + (int)mc.thePlayer.getHealth();
          float health1 = mc.thePlayer.getHealth();
          if (health1 > 20.0f) {
              health1 = 20.0f;
          }
          int red = (int)Math.abs(health1 * 5.0f * 0.01f * 0.0f + (1.0f - health1 * 5.0f * 0.01f) * 255.0f);
          int green = (int)Math.abs(health1 * 5.0f * 0.01f * 255.0f + (1.0f - health1 * 5.0f * 0.01f) * 0.0f);
          Color customColor = new Color(red, green, 0).brighter();
          mc.fontRendererObj.drawStringWithShadow(health, sr.getScaledWidth() / 2 - mc.fontRendererObj.getStringWidth(health)/ 2, sr.getScaledHeight() / 2 - 15, customColor.getRGB());

          
	}
	
}
