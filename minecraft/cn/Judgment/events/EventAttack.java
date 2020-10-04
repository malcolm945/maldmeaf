/*
 * Decompiled with CFR 0.149.
 */
package cn.Judgment.events;

import com.darkmagician6.eventapi.events.Event;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.Packet;

public class EventAttack
implements Event {
    private Entity ent;
    private EventPacketType type;
    public static boolean sendcancel;
    public EntityLivingBase target;
    private boolean cancelled;
    public static boolean recievecancel;
    public Packet packet;

    public Entity getEntity() {
        return this.ent;
    }

    public EventAttack(Entity e) {
        this.ent = e;
    }

    public EventAttack(EventPacketType type, Packet packet) {
        this.type = type;
        this.packet = packet;
    }

    public Packet getPacket() {
        return this.packet;
    }

    public void setPacket(Packet packet) {
        this.packet = packet;
    }

    public static enum EventPacketType {
        SEND,
        RECEIVE;

    }
}

