package dev.chimera.client.modules.modules;

import dev.chimera.client.eventsystem.EventDispatcher;
import dev.chimera.client.eventsystem.EventListener;
import dev.chimera.client.eventsystem.events.PacketSendEvent;
import dev.chimera.client.modules.AbstractModule;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.Identifier;

import static dev.chimera.client.ChimeraClient.MOD_ID;

public class NoFall extends AbstractModule {


    public NoFall() {
        super(new Identifier(MOD_ID, "nofall"), "NoFall");
        EventDispatcher.registerListenersInObject(this);

        this.enable();
    }

    @EventListener
    private void onPacketSend(PacketSendEvent event) {
        MinecraftClient mc = MinecraftClient.getInstance();

        if (!this.isEnabled()) return;

        if (event.packet instanceof PlayerMoveC2SPacket movePacket) {
            double newX = movePacket.getX(mc.player.getX());
            double newY = movePacket.getY(mc.player.getY());
            double newZ = movePacket.getZ(mc.player.getZ());

            boolean wasReplaced = event.isReplaced;
            event.isReplaced = true;
            if (movePacket instanceof PlayerMoveC2SPacket.OnGroundOnly) {
                event.packet = new PlayerMoveC2SPacket.OnGroundOnly(true);
            } else if (movePacket instanceof PlayerMoveC2SPacket.PositionAndOnGround) {
                event.packet = new PlayerMoveC2SPacket.PositionAndOnGround(newX, newY, newZ, true);
            } else if (movePacket instanceof PlayerMoveC2SPacket.LookAndOnGround) {
                event.packet = new PlayerMoveC2SPacket.LookAndOnGround(movePacket.getYaw(0), movePacket.getPitch(0), true);
            } else if (movePacket instanceof PlayerMoveC2SPacket.Full) {
                event.packet = new PlayerMoveC2SPacket.Full(newX, newY, newZ, movePacket.getYaw(0), movePacket.getPitch(0), true);
            } else {
                event.isReplaced = wasReplaced;
            }
        }
    }


}
