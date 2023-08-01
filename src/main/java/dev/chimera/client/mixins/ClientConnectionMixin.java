package dev.chimera.client.mixins;

import dev.chimera.client.eventsystem.EventDispatcher;
import dev.chimera.client.eventsystem.events.PacketReceiveEvent;
import dev.chimera.client.eventsystem.events.PacketSendEvent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.PacketCallbacks;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(ClientConnection.class)
public abstract class ClientConnectionMixin {
    @Shadow
    public abstract void send(Packet<?> packet, @Nullable PacketCallbacks callbacks);

    @Mixin(CustomPayloadC2SPacket.class)
    public interface CustomPayloadAccessor {
        @Accessor("data")
        @Mutable
        void setData(PacketByteBuf data);
    }

    @Shadow
    protected abstract void sendImmediately(Packet<?> packet, @Nullable PacketCallbacks callbacks);

    private PacketSendEvent packetSendEvent = new PacketSendEvent();

    @Inject(at = @At("HEAD"), method = "send(Lnet/minecraft/network/packet/Packet;Lnet/minecraft/network/PacketCallbacks;)V", cancellable = true)
    private void onSend(Packet<?> packetEvent, PacketCallbacks callbacks, CallbackInfo ci) {

        packetSendEvent.packet = packetEvent;
        packetSendEvent.isCancelled = false;
        packetSendEvent.isReplaced = false;

        EventDispatcher.postEvent(packetSendEvent);

        if (packetSendEvent.isCancelled) {
            ci.cancel();
            return;
        }

        if (packetSendEvent.isReplaced) {
            packetEvent = packetSendEvent.packet;
            sendImmediately(packetEvent, callbacks);
            ci.cancel();
        }
    }

    @Unique
    private static PacketReceiveEvent packetReceiveEvent = new PacketReceiveEvent();

    @Inject(method = "handlePacket", at = @At("HEAD"), cancellable = true)
    private static <T extends PacketListener> void onHandlePacket(Packet<T> packet, PacketListener listener, CallbackInfo ci) {

        MinecraftClient.getInstance().getProfiler().push("Packet event");

        packetReceiveEvent.packet = packet;
        packetReceiveEvent.isCancelled = false;
        EventDispatcher.postEvent(packetReceiveEvent);

        if (packetReceiveEvent.isCancelled) {
            ci.cancel();
        }

        MinecraftClient.getInstance().getProfiler().pop();
    }

}