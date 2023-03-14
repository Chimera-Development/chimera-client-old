package dev.chimera.amalthea.events.packet;

import dev.chimera.amalthea.events.AbstractEvent;
import net.minecraft.network.Packet;

public class PacketReceiveEvent extends AbstractEvent {
    public Packet<?> packet;
}
