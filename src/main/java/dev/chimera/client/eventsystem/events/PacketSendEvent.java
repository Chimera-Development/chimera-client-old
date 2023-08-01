package dev.chimera.client.eventsystem.events;

import dev.chimera.client.eventsystem.AbstractEvent;
import net.minecraft.network.packet.Packet;

public class PacketSendEvent extends AbstractEvent {
    public boolean isReplaced = false;
    public Packet<?> packet;
}
