package dev.chimera.mixins;

import dev.chimera.ChimeraClient;
import dev.chimera.EventListeners;
import dev.chimera.amalthea.events.misc.KeyEvents;
import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.event.KeyEvent;
import java.lang.reflect.InvocationTargetException;

@Mixin(Keyboard.class)
public abstract class KeyboardMixin {
    private KeyEvents.InGame.Press pressKeyEvent = new KeyEvents.InGame.Press();
    private KeyEvents.InGame.Release releaseKeyEvent = new KeyEvents.InGame.Release();

    private KeyEvents.InGUI.Press pressKeyEventGUI = new KeyEvents.InGUI.Press();
    private KeyEvents.InGUI.Release releaseKeyEventGUI = new KeyEvents.InGUI.Release();

    @Inject(method = "onKey", at = @At("HEAD"), cancellable = true)
    public void onKey(long window, int key, int scancode, int action, int modifiers, CallbackInfo info) {
        if (key != GLFW.GLFW_KEY_UNKNOWN && MinecraftClient.getInstance().currentScreen == null) {
            pressKeyEvent.setKey(key);
            releaseKeyEvent.setKey(key);
                if (action == 1) ChimeraClient.EVENT_BUS.postEvent(pressKeyEvent);
                if (action == 0) ChimeraClient.EVENT_BUS.postEvent(releaseKeyEvent);
        }
        if(key != GLFW.GLFW_KEY_UNKNOWN && MinecraftClient.getInstance().currentScreen != null){
            pressKeyEventGUI.setKey(key);
            releaseKeyEventGUI.setKey(key);
            if (action == 1) ChimeraClient.EVENT_BUS.postEvent(pressKeyEventGUI);
            if (action == 0) ChimeraClient.EVENT_BUS.postEvent(releaseKeyEventGUI);
        }
    }
}