package dev.chimera.client.modules.modules;

import dev.chimera.client.eventsystem.EventDispatcher;
import dev.chimera.client.eventsystem.EventListener;
import dev.chimera.client.eventsystem.events.PacketSendEvent;
import dev.chimera.client.modules.AbstractModule;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import static dev.chimera.client.ChimeraClient.MOD_ID;

public class AntiFlyKick extends AbstractModule {

    Vec3d serverPos;


    public AntiFlyKick() {
        super(new Identifier(MOD_ID, "antiflykick"), "AntiFlyKick");
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

            boolean isFloating = mc.world.isAir(new BlockPos((int) newX, (int) (newY - 0.05), (int) newZ));
            if (mc.player.age % 40 <= 2 && isFloating)
                newY = serverPos.y - 0.04;

            serverPos = new Vec3d(newX, newY, newZ);

            boolean wasReplaced = event.isReplaced;

            event.isReplaced = true;
            if (movePacket instanceof PlayerMoveC2SPacket.PositionAndOnGround) {
                event.packet = new PlayerMoveC2SPacket.PositionAndOnGround(newX, newY, newZ, movePacket.isOnGround());
            } else if (movePacket instanceof PlayerMoveC2SPacket.Full) {
                event.packet = new PlayerMoveC2SPacket.Full(newX, newY, newZ, movePacket.getYaw(0), movePacket.getPitch(0),  movePacket.isOnGround());
            } else {
                event.isReplaced = wasReplaced;
            }
        }
    }


}
