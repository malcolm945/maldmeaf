package cn.Judgment.events;

import com.darkmagician6.eventapi.events.Event;
import net.minecraft.network.Packet;

public class EventReceivePacket
implements Event {
    public Packet packet;
    public boolean cancel;
    private boolean outgoing;
    
    public EventReceivePacket(Packet packet) {
        this.packet = packet;
        this.cancel = false;
    }

    public Packet getPacket() {
        return this.packet;
    }

    public boolean isCancelled() {
        return this.cancel;
    }
    
    public boolean isIncoming() {
        if (this.outgoing) return false;
        return true;
    }

    public boolean isOutgoing() {
        return outgoing;
    }

    public void setCanceled(boolean canceled) {
        this.cancel = canceled;
    }


}

