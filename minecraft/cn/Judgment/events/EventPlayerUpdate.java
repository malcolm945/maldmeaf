package cn.Judgment.events;

import com.darkmagician6.eventapi.events.Event;

public class EventPlayerUpdate implements Event {
	public float yaw;
    public float pitch;
    public double x;
    public double y;
    public double z;
    public boolean onGround;

    public EventPlayerUpdate(float yaw, float pitch, double x, double y, double z, boolean onGround) {
        this.yaw = yaw;
        this.pitch = pitch;
        this.y = y;
        this.x = x;
        this.z = z;
        this.onGround = onGround;
    }
}
