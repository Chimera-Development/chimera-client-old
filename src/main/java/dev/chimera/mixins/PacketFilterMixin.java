package dev.chimera.mixins;

import dev.chimera.ChimeraClient;
import dev.chimera.amalthea.events.packet.PacketEvent;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketCallbacks;
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

    @Inject(at = @At("HEAD"), method = "send(Lnet/minecraft/network/Packet;Lnet/minecraft/network/PacketCallbacks;)V", cancellable = true )
    private void onSend(Packet<?> packet, PacketCallbacks callbacks, CallbackInfo ci) {
        PacketEvent.Send packetSendEvent = new PacketEvent.Send(packet);
        ChimeraClient.EVENT_BUS.postEvent(packetSendEvent);

        if (packetSendEvent.isCancelled()) ci.cancel();
        else if (packetSendEvent.isReplaced())
        {
            send(packetSendEvent.getPacket(), callbacks);
            ci.cancel();
        }
    }

    @Inject(at = @At("HEAD"), method = "channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/Packet;)V", cancellable = true )
    public void onReceive(ChannelHandlerContext channelHandlerContext, Packet<?> packet, CallbackInfo ci)
    {
        PacketEvent.Receive packetReceiveEvent = new PacketEvent.Receive(packet);
        ChimeraClient.EVENT_BUS.postEvent(packetReceiveEvent);

        if (packetReceiveEvent.isCancelled()) ci.cancel();
        else if (packetReceiveEvent.isReplaced())
        {
            channelHandlerContext.fireChannelRead(packetReceiveEvent.getPacket());
            ci.cancel();
        }
    }
}
