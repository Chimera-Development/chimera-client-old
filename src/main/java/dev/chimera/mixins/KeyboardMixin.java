package dev.chimera.mixins;

import dev.chimera.ChimeraClient;
import dev.chimera.amalthea.events.input.KeyboardEvent;
import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Keyboard.class)
public abstract class KeyboardMixin {
    @Inject(method = "onKey", at = @At("HEAD"), cancellable = true)
    public void onKey(long window, int key, int scancode, int action, int modifiers, CallbackInfo info) {
        if(key != GLFW.GLFW_KEY_UNKNOWN && MinecraftClient.getInstance().currentScreen != null){
            KeyboardEvent event;

            if (action == 0) event = new KeyboardEvent.Release(key);
            else if (action == 1) event = new KeyboardEvent.Press(key);
            else event = new KeyboardEvent.Unnamed(key);

            ChimeraClient.EVENT_BUS.postEvent(event);

            if (event.isCancelled()) info.cancel();
        }
    }
}