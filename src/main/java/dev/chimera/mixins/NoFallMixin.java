package dev.chimera.mixins;

import dev.chimera.modules.Module;
import dev.chimera.modules.ModuleInitializer;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ClientConnection.class)
public class NoFallMixin {
    @ModifyVariable(at = @At("HEAD"), method = "send(Lnet/minecraft/network/Packet;Lnet/minecraft/network/PacketCallbacks;)V", argsOnly = true )
    private Packet<?> injected(Packet<?> packet) {
        Module checkEnabled = ModuleInitializer.findModule("No-Fall");
        if (checkEnabled == null) return packet;
        if (checkEnabled.getModuleEnabled() && packet instanceof PlayerMoveC2SPacket packet1) {
            if (packet1 instanceof PlayerMoveC2SPacket.OnGroundOnly) {
                return new PlayerMoveC2SPacket.OnGroundOnly(true);
            } else if (packet1 instanceof PlayerMoveC2SPacket.PositionAndOnGround) {
                return new PlayerMoveC2SPacket.PositionAndOnGround(packet1.getX(0), packet1.getY(0), packet1.getZ(0), true);
            } else if (packet1 instanceof PlayerMoveC2SPacket.LookAndOnGround) {
                return new PlayerMoveC2SPacket.LookAndOnGround(packet1.getYaw(0), packet1.getPitch(0), true);
            } else if (packet1 instanceof PlayerMoveC2SPacket.Full) {
                return new PlayerMoveC2SPacket.Full(packet1.getX(0), packet1.getY(0), packet1.getZ(0), packet1.getYaw(0), packet1.getPitch(0), true);
            }
        }
        return packet;
    }
}
