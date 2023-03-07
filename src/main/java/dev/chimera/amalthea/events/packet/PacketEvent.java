package dev.chimera.amalthea.events.packet;

import dev.chimera.amalthea.events.AbstractEvent;
import net.minecraft.network.Packet;

public class PacketEvent extends AbstractEvent {
    private Packet<?> packet;
    public PacketEvent(){
    }
    public static class Send extends PacketEvent {
        public Send() {
        }
    }
    public static class Receive extends PacketEvent {
        public Receive() {
        }

    }
    public Packet<?> getPacket() {
        return packet;
    }

    public void setPacket(Packet<?> packet) {
        this.packet = packet;
    }
}
