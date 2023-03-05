package dev.chimera.amalthea.events.packet;

import dev.chimera.amalthea.events.AbstractEvent;
import net.minecraft.network.Packet;

public class PacketEvent extends AbstractEvent {
    private Packet<?> packet;
    public PacketEvent(Packet<?> packet) {
        this.packet = packet;
    }
    public Packet<?> getPacket() {
        return packet;
    }

    public void setPacket(Packet<?> packet) {
        this.packet = packet;
    }
}
