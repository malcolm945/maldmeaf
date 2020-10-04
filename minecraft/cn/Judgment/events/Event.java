package cn.Judgment.events;

public abstract class Event {
	   protected boolean cancelled;
	private boolean onGround;

	   public void fire() {
	      this.cancelled = false;
	   }

	   public void setCancelled(boolean cancelled) {
	      this.cancelled = cancelled;
	   }

	   public boolean isCancelled() {
	      return this.cancelled;
	   }

	    public void setOnGround(boolean onGround) {
	        this.onGround = onGround;
	    }
	    
	    
	}
