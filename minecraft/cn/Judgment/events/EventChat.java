package cn.Judgment.events;

import com.darkmagician6.eventapi.events.Event;

public class EventChat implements Event {
	
	public String message;
	public boolean cancelled;
	
	public EventChat(String chat) {
		message = chat;
	}

	public String getMessage() {
		return message;
	}
	
	public void setCancelled(boolean b) {
		this.cancelled = b;
	}
}
