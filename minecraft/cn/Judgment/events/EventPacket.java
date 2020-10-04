package cn.Judgment.events;

import com.darkmagician6.eventapi.events.Event;

import net.minecraft.network.Packet;

public class EventPacket implements Event {
	public Packet packet;
    private boolean cancelled;
    public byte type;
    private boolean outgoing;


    
    public EventPacket(Packet p) {
        this.packet = p;
    }

    public Packet getPacket() {
        return this.packet;
    }

    public void setCancelled(boolean state) {
        this.cancelled = state;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setPacket(Packet p) {
        this.packet = p;
    }

    public byte getType() {
        return this.type;
    }
    
    public void setType(byte type) {
        this.type = type;
    }

    
    public boolean isIncoming() {
        if (this.outgoing) return false;
        return true;
    }

    public boolean isOutgoing() {
        return outgoing;
    }
}
