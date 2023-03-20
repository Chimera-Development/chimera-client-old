package dev.chimera.managers.modules.player;

import dev.chimera.amalthea.eventbus.EventListener;
import dev.chimera.amalthea.events.packet.PacketEvent;
import dev.chimera.managers.modules.AbstractModule;
import dev.chimera.managers.modules.ModuleCategory;
import dev.chimera.managers.modules.ModuleManager;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class NoFallModule extends AbstractModule {
    public NoFallModule() {
        super("No-Fall", ModuleCategory.PLAYER);
    }

    Vec3d serverPos;

    @EventListener(id="nofall")
    public void onPacketSend(PacketEvent.Send event)
    {
        FlightModule flightModule = ModuleManager.findModule(FlightModule.class);
        if (event.packet instanceof PlayerMoveC2SPacket packet1) {
            boolean onGround = mc.player.isOnGround();
            if (this.isEnabled()) onGround = true;
            if (serverPos == null) serverPos = new Vec3d(mc.player.getX(), mc.player.getY(), mc.player.getZ());

            double newX = packet1.getX(mc.player.getX());
            double newY = packet1.getY(mc.player.getY());
            double newZ = packet1.getZ(mc.player.getZ());

            if (flightModule != null && flightModule.isEnabled() && mc.world.isAir(new BlockPos(newX, serverPos.y - 0.04, newZ)) && mc.player.age % 40 >= 0 && mc.player.age % 40 <= 2) newY = serverPos.y - 0.04;

            serverPos = new Vec3d(newX, newY, newZ);

            if (packet1 instanceof PlayerMoveC2SPacket.OnGroundOnly) {
                event.packet = new PlayerMoveC2SPacket.OnGroundOnly(onGround);
            } else if (packet1 instanceof PlayerMoveC2SPacket.PositionAndOnGround) {
                event.packet = new PlayerMoveC2SPacket.PositionAndOnGround(newX, newY, newZ, onGround);
            } else if (packet1 instanceof PlayerMoveC2SPacket.LookAndOnGround) {
                event.packet = new PlayerMoveC2SPacket.LookAndOnGround(packet1.getYaw(0), packet1.getPitch(0), onGround);
            } else if (packet1 instanceof PlayerMoveC2SPacket.Full) {
                event.packet = new PlayerMoveC2SPacket.Full(newX, newY, newZ, packet1.getYaw(0), packet1.getPitch(0), onGround);
            }
        }
    }
}
