package cn.Judgment.events;

import com.darkmagician6.eventapi.events.Event;

public class InsideBlockRenderEvent implements Event {
	public boolean cancelled;
	
	public void setCancelled(boolean b) {
		this.cancelled = b;
	}

	public boolean isCanceled() {
		return cancelled;
	}
	
}
