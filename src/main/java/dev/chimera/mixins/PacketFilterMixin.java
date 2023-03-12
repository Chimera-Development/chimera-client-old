package dev.chimera.mixins;

import dev.chimera.ChimeraClient;
import dev.chimera.amalthea.events.packet.PacketEvent;

import dev.chimera.modules.Module;
import dev.chimera.modules.ModuleInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketCallbacks;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientConnection.class)
public abstract class PacketFilterMixin {
    @Shadow public abstract void send(Packet<?> packet, @Nullable PacketCallbacks callbacks);

    Vec3d serverPos;
    private PacketEvent.Send packetSendEvent = new PacketEvent.Send();

    @Inject(at = @At("HEAD"), method = "send(Lnet/minecraft/network/Packet;Lnet/minecraft/network/PacketCallbacks;)V", cancellable = true )
    private void injected(Packet<?> packet, PacketCallbacks callbacks, CallbackInfo ci) {
        // This probably isn't the most performant way of doing this, but there is no other way to modify arguments while still being able to cancel call.
        if (packetSendEvent.packet == packet)
        {
            packetSendEvent.packet = null;
            return;
        }

        packetSendEvent.cancelled = false;
        packetSendEvent.packet = packet;
        ChimeraClient.EVENT_BUS.postEvent(packetSendEvent);
        if  (packetSendEvent.cancelled) {
            //ChimeraClient.LOGGER.info("Cancelled packet!");
            packetSendEvent.packet = null;
            ci.cancel();
            return;
        }
        else if (packetSendEvent.packet != packet)
        {
            send(packetSendEvent.packet, callbacks);
            ci.cancel();
            return;
        }

        packetSendEvent.packet = null;
    }
}
