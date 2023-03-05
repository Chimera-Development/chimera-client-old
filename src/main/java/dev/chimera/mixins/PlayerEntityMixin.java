package dev.chimera.mixins;

import dev.chimera.ChimeraClient;
import dev.chimera.amalthea.events.player.DeathEvent;
import dev.chimera.amalthea.events.player.JumpEvent;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.InvocationTargetException;

import static dev.chimera.ChimeraClient.mc;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    @Inject(method="onDeath",at=@At("HEAD"))
    public void onDeath(DamageSource damageSource, CallbackInfo ci) throws InvocationTargetException, IllegalAccessException {
        DeathEvent event = new DeathEvent(damageSource);
        //ChimeraClient.EVENT_BUS.post("death",event);
        ChimeraClient.EVENT_BUS.postEvent(event);
        ChimeraClient.LOGGER.warn("onDeath");
    }

    @Inject(method="requestRespawn",at=@At("HEAD"))
    public void onRespawn(CallbackInfo ci){

    }
    @Inject(method="jump",at=@At("HEAD"),cancellable = true)
    public void onJump(CallbackInfo ci) throws InvocationTargetException, IllegalAccessException {
        JumpEvent event = new JumpEvent(mc.player);
        ChimeraClient.EVENT_BUS.postEvent(event);
        if (event.isCancelled()){
            ci.cancel();
        }
    }
}
