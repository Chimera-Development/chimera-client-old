package dev.chimera.mixins;

import dev.chimera.ChimeraClient;
import dev.chimera.amalthea.events.misc.ChatEvent;
import dev.chimera.amalthea.events.packet.ConnectServerEvent;
import dev.chimera.amalthea.events.packet.PacketEvent;
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
    private PacketEvent.Send sendEvent = new PacketEvent.Send();
    private PacketEvent.Receive receiveEvent = new PacketEvent.Receive();
    private ChatEvent.Receive ReceiveChatEvent = new ChatEvent.Receive();
    private ChatEvent.Send SendChatEvent = new ChatEvent.Send();
    @Inject(method = "channelRead0", at = @At("HEAD"), cancellable = true)
    private void channelRead0(ChannelHandlerContext channelHandlerContext, Packet<?> packet, CallbackInfo callback) throws InvocationTargetException, IllegalAccessException {
        if (this.channel.isOpen() && packet != null) {
            receiveEvent.setPacket(packet);
            ChimeraClient.EVENT_BUS.postEvent(receiveEvent);
          if (packet.getClass().equals(ChatMessageS2CPacket.class)){
                ChatMessageS2CPacket chatPacket = (ChatMessageS2CPacket) packet;
                ReceiveChatEvent.setPacket(chatPacket);
                ChimeraClient.EVENT_BUS.postEvent(ReceiveChatEvent); // Error here
                if (ReceiveChatEvent.isCancelled()){
                    receiveEvent.setCancelled(true);
                }
           }
            if (sendEvent.isCancelled()){
                callback.cancel();
            }

        }

    }

    @Inject(method = "send(Lnet/minecraft/network/Packet;Lnet/minecraft/network/PacketCallbacks;)V", at = @At("HEAD"), cancellable = true)
    private void send(Packet<?> packet, PacketCallbacks packetCallback, CallbackInfo callback) throws InvocationTargetException, IllegalAccessException {
        ChimeraClient.EVENT_BUS.postEvent(sendEvent);

        if (packet.getClass().equals(ChatMessageC2SPacket.class)){
            ChatMessageC2SPacket chatPacket = (ChatMessageC2SPacket) packet;
            SendChatEvent.setPacket(chatPacket);
            ChimeraClient.EVENT_BUS.postEvent(SendChatEvent);
        }
        if (sendEvent.isCancelled()) {
            callback.cancel();
        }
    }
    @Inject(method = "connect", at = @At("HEAD"))

    private static void onConnect(InetSocketAddress address, boolean useEpoll, CallbackInfoReturnable<ClientConnection> info) throws InvocationTargetException, IllegalAccessException {
        ConnectServerEvent event = new ConnectServerEvent(address);
        ChimeraClient.EVENT_BUS.postEvent(event);
    }
}
