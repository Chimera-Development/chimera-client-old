package dev.chimera.mixins;

import dev.chimera.ChimeraClient;
import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.input.Input;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.event.KeyEvent;

@Mixin(Keyboard.class)
public abstract class KeyboardMixin {
    @Shadow @Final
    private MinecraftClient client;

    @Inject(method = "onKey", at = @At("HEAD"), cancellable = true)
    public void onKey(long window, int key, int scancode, int action, int modifiers, CallbackInfo info) {
        if (key != GLFW.GLFW_KEY_UNKNOWN && MinecraftClient.getInstance().currentScreen == null) {
            if (action == 1) ChimeraClient.EVENT_BUS.post("key-press", KeyEvent.getKeyText(key));
            if (action == 0) ChimeraClient.EVENT_BUS.post("key-release", KeyEvent.getKeyText(key));
        }
    }
}