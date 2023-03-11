package dev.chimera.mixins;

import dev.chimera.modules.Module;
import dev.chimera.modules.ModuleInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ClientConnection.class)
public class PacketFilterMixin {
    Vec3d serverPos;

    @ModifyVariable(at = @At("HEAD"), method = "send(Lnet/minecraft/network/Packet;Lnet/minecraft/network/PacketCallbacks;)V", argsOnly = true )
    private Packet<?> injected(Packet<?> packet) {
        Packet newPacket = packet;
        MinecraftClient client = MinecraftClient.getInstance();
        Module nofallModule = ModuleInitializer.findModule("No-Fall");
        Module flightModule = ModuleInitializer.findModule("Flight");

        if (packet instanceof PlayerMoveC2SPacket packet1) {
            boolean onGround = client.player.isOnGround();
            if (nofallModule != null && nofallModule.getModuleEnabled()) onGround = true;
            if (serverPos == null) serverPos = new Vec3d(client.player.getX(), client.player.getY(), client.player.getZ());

            double newX = packet1.getX(client.player.getX());
            double newY = packet1.getY(client.player.getY());
            double newZ = packet1.getZ(client.player.getZ());

            if (flightModule != null && flightModule.getModuleEnabled() && client.world.isAir(new BlockPos(newX, serverPos.y - 0.04, newZ)) && client.player.age % 40 >= 0 && client.player.age % 40 <= 2) newY = serverPos.y - 0.04;

            serverPos = new Vec3d(newX, newY, newZ);

            if (packet1 instanceof PlayerMoveC2SPacket.OnGroundOnly) {
                newPacket = new PlayerMoveC2SPacket.OnGroundOnly(onGround);
            } else if (packet1 instanceof PlayerMoveC2SPacket.PositionAndOnGround) {
                newPacket = new PlayerMoveC2SPacket.PositionAndOnGround(newX, newY, newZ, onGround);
            } else if (packet1 instanceof PlayerMoveC2SPacket.LookAndOnGround) {
                newPacket = new PlayerMoveC2SPacket.LookAndOnGround(packet1.getYaw(0), packet1.getPitch(0), onGround);
            } else if (packet1 instanceof PlayerMoveC2SPacket.Full) {
                newPacket = new PlayerMoveC2SPacket.Full(newX, newY, newZ, packet1.getYaw(0), packet1.getPitch(0), onGround);
            }
        }

        return newPacket;
    }
}
