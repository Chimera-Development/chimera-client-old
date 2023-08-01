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
        targets = "com.terraformersmc.modmenu.event.ModMenuEventHandler",
        remap = false
)
public abstract class ModMenuMixin {
    @Unique
    private static boolean hasFuckedOff = false;

    @Inject(
            at = @At("HEAD"),
            method = "afterTitleScreenInit",
            cancellable = true,
            remap = false
    )
    private static void afterTitleScreenInit(Screen screen, CallbackInfo ci) {
        if (!hasFuckedOff) System.out.println("fuck off modmenu");
        hasFuckedOff = true;
        ci.cancel();
    }
}
