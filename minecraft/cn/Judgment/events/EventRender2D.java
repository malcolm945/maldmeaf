package cn.Judgment.events;

import com.darkmagician6.eventapi.events.Event;

import net.minecraft.client.gui.ScaledResolution;

public class EventRender2D implements Event {
	
	private ScaledResolution resolution;

	 public ScaledResolution getResolution() {
	        return this.resolution;
	    }
}
