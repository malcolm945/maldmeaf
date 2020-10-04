/*
 * Decompiled with CFR 0_132.
 */
package cn.Judgment.events;

import net.minecraft.network.Packet;

public class EventPacketRecieve
extends Event {
    public Packet packet;

    public EventPacketRecieve(Packet packet) {
        this.packet = packet;
    }

    public Packet getPacket() {
        return this.packet;
    }

    public void setPacket(Packet packet) {
        this.packet = packet;
    }
}

