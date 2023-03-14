package dev.chimera.mixins;

import dev.chimera.ChimeraClient;
import dev.chimera.nemean.elements.AddonsScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(TitleScreen.class)
public abstract class ExileSingleplayerMixin extends Screen {
    @Shadow protected abstract void init();
    AddonsScreen addonsScreen = new AddonsScreen();
    protected ExileSingleplayerMixin(Text title) {
        super(title);
    }

    @Redirect(method = "initWidgetsNormal",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/TitleScreen;addDrawableChild(Lnet/minecraft/client/gui/Element;)Lnet/minecraft/client/gui/Element;"))
    public <T extends Element & Drawable & Selectable> T addDrawableChildFromInitWidgetsNormal(TitleScreen instance, T element) {
        if(element instanceof ButtonWidget button && button.getMessage().getContent() instanceof TranslatableTextContent trans && trans.getKey().equals("menu.singleplayer")) {
            ChimeraClient.LOGGER.info("No singleplayer for you!");

            addDrawableChild(ButtonWidget.builder(Text.of("Addons"), (customButton) -> {
                addonsScreen.isActive = true;

                //MinecraftClient.getInstance().setScreen(addonsScreen);
            }).dimensions(this.width / 2 - 100, ((ButtonWidget) element).getY(), 200, 20).build());

            return null;
        }
        return addDrawableChild(element);
    }
}