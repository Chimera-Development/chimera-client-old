package dev.chimera.mixins;

import dev.chimera.ChimeraClient;
import dev.chimera.amalthea.events.packet.JoinGameEvent;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.InvocationTargetException;

import static dev.chimera.ChimeraClient.mc;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {
    @Inject(method="onGameJoin",at=@At("TAIL"))
    public void onGameJoin(GameJoinS2CPacket packet, CallbackInfo ci){
        JoinGameEvent event = new JoinGameEvent(packet);
        try {
            ChimeraClient.EVENT_BUS.postEvent(event);
        } catch (InvocationTargetException | IllegalAccessException  e) {
            throw new RuntimeException(e);
        }
    }
}
