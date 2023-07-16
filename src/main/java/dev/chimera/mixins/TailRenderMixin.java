package dev.chimera.mixins;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.chimera.amalthea.events.misc.GuiRenderEvent;
import dev.chimera.nemean.ChimeraGUI;
import dev.chimera.nemean.ScreenRenderingAttempt;
import dev.chimera.nemean.components.Trollface;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = RenderSystem.class, remap = false)
public class TailRenderMixin {
    private static GuiRenderEvent event = new GuiRenderEvent();
    private static ScreenRenderingAttempt sra = new ScreenRenderingAttempt();
    @Inject(at = @At("HEAD"), method = "flipFrame")
    private static void runTickTail(CallbackInfo ci) {
        MinecraftClient.getInstance().getProfiler().push("ChimeraGUI-2");

        sra.render();

        MinecraftClient.getInstance().getProfiler().pop();
    }
}

