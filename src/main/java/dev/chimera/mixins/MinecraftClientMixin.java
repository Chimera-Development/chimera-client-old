package dev.chimera.mixins;

import dev.chimera.ChimeraClient;
import dev.chimera.amalthea.events.world.TickEvent;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.InvocationTargetException;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Inject(method="tick",at=@At("HEAD"))
    public void tickPRE(CallbackInfo ci) throws InvocationTargetException, IllegalAccessException {
        TickEvent.Pre event = new TickEvent.Pre();
        ChimeraClient.EVENT_BUS.postEvent(event);
    }
    @Inject(method="tick",at=@At("TAIL"))
    public void tickPOST(CallbackInfo ci) throws InvocationTargetException, IllegalAccessException {
        TickEvent.Post event = new TickEvent.Post();
        ChimeraClient.EVENT_BUS.postEvent(event);
    }
}
