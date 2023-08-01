package dev.chimera.client.eventsystem.events;

import dev.chimera.client.eventsystem.AbstractEvent;
import net.minecraft.network.packet.Packet;

public class PacketReceiveEvent extends AbstractEvent {
    public Packet<?> packet;
}
