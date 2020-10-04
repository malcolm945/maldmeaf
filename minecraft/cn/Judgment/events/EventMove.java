package cn.Judgment.events;

import com.darkmagician6.eventapi.events.Event;

import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;

public class EventMove implements Event {
	public static double x;
    public static double y;
    public static double z;
    private double motionX;
    private double motionY;
    private double motionZ;

	    
    public EventMove(double a, double b, double c) {
		this.x = a;
		this.y = b;
		this.z = c;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public static void setY(double y) {
		EventMove.y = y;
	}

	public double getZ() {
		return z;
	}

	public void setZ(double z) {
		this.z = z;
	}

	public Entity getEntity() {
		// TODO 自动生成的方法存根
		return null;
	}


}
