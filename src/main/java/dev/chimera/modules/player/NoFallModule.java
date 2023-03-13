package dev.chimera.modules.player;

import dev.chimera.ChimeraClient;
import dev.chimera.amalthea.eventbus.EventListener;
import dev.chimera.amalthea.events.misc.TickEvent;
import dev.chimera.amalthea.events.packet.PacketSendEvent;
import dev.chimera.modules.Module;
import dev.chimera.modules.ModuleCategory;
import dev.chimera.modules.ModuleInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class NoFallModule extends Module {
    public NoFallModule() {
        super(ModuleCategory.PLAYER, "No-Fall");
        ChimeraClient.EVENT_BUS.registerListenersInClass(this);
    }

    MinecraftClient client;
    Vec3d serverPos;

    @Override
    public void init() {
        client = MinecraftClient.getInstance();
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}

    @Override
    public void onTickStart(TickEvent.Start event) {}

    @Override
    public void onTickEnd(TickEvent.End event) {}


    @EventListener(id="nofall")
    public void onPacketSend(PacketSendEvent event)
    {
        Module flightModule = ModuleInitializer.findModule("Flight");
        if (event.packet instanceof PlayerMoveC2SPacket packet1) {
            boolean onGround = client.player.isOnGround();
            if (this.getModuleEnabled()) onGround = true;
            if (serverPos == null) serverPos = new Vec3d(client.player.getX(), client.player.getY(), client.player.getZ());

            double newX = packet1.getX(client.player.getX());
            double newY = packet1.getY(client.player.getY());
            double newZ = packet1.getZ(client.player.getZ());

            if (flightModule != null && flightModule.getModuleEnabled() && client.world.isAir(new BlockPos(newX, serverPos.y - 0.04, newZ)) && client.player.age % 40 >= 0 && client.player.age % 40 <= 2) newY = serverPos.y - 0.04;

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
