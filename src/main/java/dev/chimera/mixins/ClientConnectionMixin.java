package dev.chimera.mixins;

import dev.chimera.ChimeraClient;
import dev.chimera.amalthea.events.misc.ChatEvent;
import dev.chimera.amalthea.events.packet.ConnectServerEvent;
import dev.chimera.amalthea.events.packet.PacketReceiveEvent;
import dev.chimera.amalthea.events.packet.PacketSendEvent;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketCallbacks;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.network.packet.s2c.play.ChatMessageS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;

@Mixin(ClientConnection.class)
public class ClientConnectionMixin {

    @Shadow
    private Channel channel;

    @Inject(method = "channelRead0", at = @At("HEAD"), cancellable = true)
    private void channelRead0(ChannelHandlerContext channelHandlerContext, Packet<?> packet, CallbackInfo callback) throws InvocationTargetException, IllegalAccessException {
        if (this.channel.isOpen() && packet != null) {
            PacketReceiveEvent event = new PacketReceiveEvent(packet);
            ChimeraClient.EVENT_BUS.postEvent(event);
          if (packet.getClass().equals(ChatMessageS2CPacket.class)){
                ChatMessageS2CPacket chatPacket;
                ChatEvent.Receive chatEvent;
                chatPacket = (ChatMessageS2CPacket) packet;
                chatEvent = new ChatEvent.Receive(chatPacket);
                ChimeraClient.EVENT_BUS.postEvent(chatEvent); // Error here
//                if (chatEvent.isCancelled()){
//                    event.setCancelled(true);
//                }
           }
            if (event.isCancelled()){
                callback.cancel();
            }

        }

    }

    @Inject(method = "send(Lnet/minecraft/network/Packet;Lnet/minecraft/network/PacketCallbacks;)V", at = @At("HEAD"), cancellable = true)
    private void send(Packet<?> packet, PacketCallbacks packetCallback, CallbackInfo callback) throws InvocationTargetException, IllegalAccessException {
        PacketSendEvent event = new PacketSendEvent(packet);
        ChimeraClient.EVENT_BUS.postEvent(event);

        if (packet.getClass().equals(ChatMessageC2SPacket.class)){
            ChatMessageC2SPacket chatPacket;
            ChatEvent.Send chatEvent;
            chatPacket = (ChatMessageC2SPacket) packet;
            chatEvent = new ChatEvent.Send(chatPacket);
            ChimeraClient.EVENT_BUS.postEvent(chatEvent);
        }
        if (event.isCancelled()) {
            callback.cancel();
        }
    }
    @Inject(method = "connect", at = @At("HEAD"))

    private static void onConnect(InetSocketAddress address, boolean useEpoll, CallbackInfoReturnable<ClientConnection> info) throws InvocationTargetException, IllegalAccessException {
        ConnectServerEvent event = new ConnectServerEvent(address);
        ChimeraClient.EVENT_BUS.postEvent(event);
    }
}
