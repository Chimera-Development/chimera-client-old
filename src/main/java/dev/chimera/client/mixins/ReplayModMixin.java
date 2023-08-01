package dev.chimera.client.mixins;

import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(
        targets = "com.replaymod.replay.handler.GuiHandler",
        remap = false
)
public abstract class ReplayModMixin {
    @Unique
    private static boolean hasFuckedOff = false;

    @Inject(
            at = @At("HEAD"),
            method = "properInjectIntoMainMenu",
            cancellable = true,
            remap = false
    )
    private void properInjectIntoMainMenu(Screen screen, CallbackInfo ci) {
        if (!hasFuckedOff) System.out.println("fuck off replaymod");
        hasFuckedOff = true;
        ci.cancel();
    }
}
