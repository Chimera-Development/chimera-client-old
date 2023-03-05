package dev.chimera.amalthea.events.packet;

import dev.chimera.amalthea.events.AbstractEvent;
import net.minecraft.client.RunArgs;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;

public class JoinGameEvent extends AbstractEvent {
    private GameJoinS2CPacket packet;
    public JoinGameEvent(GameJoinS2CPacket packet){
        this.packet = packet;
    }

    public GameJoinS2CPacket getPacket() {
        return packet;
    }

    public void setPacket(GameJoinS2CPacket packet) {
        this.packet = packet;
    }


}
