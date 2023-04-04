package dev.chimera.amalthea.events.packet;

import dev.chimera.amalthea.events.AbstractEvent;
import dev.chimera.amalthea.events.CancellableEvent;
import lombok.Getter;
import net.minecraft.network.Packet;

public class PacketEvent extends CancellableEvent {
    @Getter
    public Packet<?> packet;
    @Getter
    private boolean replaced = false;

    public PacketEvent(Packet<?> packet) {
        this.packet = packet;
    }

    public void setPacket(Packet<?> packet) {
        this.replaced = true;
        this.packet = packet;
    }

    public static class Receive extends PacketEvent {
        public Receive(Packet<?> packet) {
            super(packet);
        }
    }

    public static class Send extends PacketEvent {
        public Send(Packet<?> packet) {
            super(packet);
        }
    }
}
