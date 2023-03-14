package dev.chimera.mixins;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.chimera.ChimeraClient;
import dev.chimera.amalthea.events.misc.GuiRenderEvent;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = RenderSystem.class, remap = false)
public class TailRenderMixin {
    private static GuiRenderEvent event = new GuiRenderEvent();
    @Inject(at = @At("HEAD"), method="flipFrame")
    private static void runTickTail(CallbackInfo ci) {
        MinecraftClient.getInstance().getProfiler().push("ImGui Render");
        event.cancelled = false;
        ChimeraClient.EVENT_BUS.postEvent(event);
        MinecraftClient.getInstance().getProfiler().pop();
    }
}

