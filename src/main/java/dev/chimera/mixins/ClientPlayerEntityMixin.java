package dev.chimera.mixins;

import dev.chimera.ChimeraClient;
import dev.chimera.amalthea.events.movement.PlayerMoveEvent;
import dev.chimera.amalthea.events.world.TickEvent;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.InvocationTargetException;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {
    @Inject(method="move",at=@At("HEAD"),cancellable = true)
    public void move(MovementType movementType, Vec3d movement, CallbackInfo ci) throws InvocationTargetException, IllegalAccessException {
        PlayerMoveEvent event = new PlayerMoveEvent(movementType,movement);
        ChimeraClient.EVENT_BUS.postEvent(event);
        if (event.isCancelled()){
            ci.cancel();
        }
    }

}
