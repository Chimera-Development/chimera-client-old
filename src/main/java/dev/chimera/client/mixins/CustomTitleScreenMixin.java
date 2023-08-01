package dev.chimera.client.mixins;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.chimera.client.gui.AnimatedTexturedButtonWidget;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.SplashTextRenderer;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerWarningScreen;
import net.minecraft.client.gui.screen.option.CreditsAndAttributionScreen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.PressableTextWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

@Mixin(value = TitleScreen.class, priority = 2147483647)
public class CustomTitleScreenMixin extends Screen {
    @Unique
    Identifier BACKGROUND_IMAGE = new Identifier("chimera-client", "textures/gui/panorama.png");
    @Unique
    Identifier TITLE_IMAGE = new Identifier("chimera-client", "textures/gui/title.png");
    @Unique
    Identifier SINGLEPLAYER_ICON = new Identifier("chimera-client", "textures/gui/singleplayer.png");
    @Unique
    Identifier MULTIPLAYER_ICON = new Identifier("chimera-client", "textures/gui/multiplayer.png");
    @Unique
    Identifier MODS_ICON = new Identifier("chimera-client", "textures/gui/mods.png");
    @Unique
    Identifier SETTINGS_ICON = new Identifier("chimera-client", "textures/gui/settings.png");

    @Unique
    Identifier EXIT_ICON = new Identifier("chimera-client", "textures/gui/exit.png");

    @Unique
    Identifier VIAFABRIC_ICON = new Identifier("chimera-client", "textures/gui/viafabric.png");
    @Unique
    Identifier REPLAYMOD_ICON = new Identifier("chimera-client", "textures/gui/replaymod.png");

    @Shadow @Nullable private SplashTextRenderer splashText;
    @Shadow @Final public static Text COPYRIGHT;

    @Shadow private long backgroundFadeStart;
//    @Shadow private List<Drawable> drawables;

    protected CustomTitleScreenMixin(Text title) {
        super(title);
    }

    @Inject(at = @At("RETURN"), method = "init")
    private void init(CallbackInfo ci) {
        assert this.client != null;

        this.clearChildren();
        this.splashText = null;

        int copyright_width = this.textRenderer.getWidth(COPYRIGHT);
        int copyright_x = this.width - copyright_width - 2;
        this.addDrawableChild(new PressableTextWidget(copyright_x, this.height - 10, copyright_width, 10, COPYRIGHT, (button) -> {
            this.client.setScreen(new CreditsAndAttributionScreen(this));
        }, this.textRenderer));

        ArrayList<ButtonWidget> widgets = new ArrayList<>();

        widgets.add(new AnimatedTexturedButtonWidget(0, 0, 40, 40, 0, 0, 0, 8, -1, SINGLEPLAYER_ICON, 40, 40, (button) -> {
            this.client.setScreen(new SelectWorldScreen(this));
        }));
        widgets.add(new AnimatedTexturedButtonWidget(0, 0, 40, 40, 0, 0, 0, 8, -1, MULTIPLAYER_ICON, 40, 40, (button) -> {
            Screen screen = this.client.options.skipMultiplayerWarning ? new MultiplayerScreen(this) : new MultiplayerWarningScreen(this);
            this.client.setScreen(screen);
        }));
        widgets.add(new AnimatedTexturedButtonWidget(0, 0, 40, 40, 0, 0, 0, 8, -1, SETTINGS_ICON, 40, 40, (button) -> {
            this.client.setScreen(new OptionsScreen(this, this.client.options));
        }));
        if (FabricLoader.getInstance().isModLoaded("modmenu")) {
            widgets.add(new AnimatedTexturedButtonWidget(0, 0, 40, 40, 0, 0, 0, 8, -1, MODS_ICON, 40, 40, (button) -> {
//                this.client.setScreen(new com.terraformersmc.modmenu.gui.ModsScreen(this));
                try {
                    Class<?> settingsScreen = Class.forName("com.terraformersmc.modmenu.gui.ModsScreen");
                    Object settingsScreenInstance = settingsScreen.getDeclaredConstructor(Screen.class).newInstance(this);
                    this.client.setScreen((Screen) settingsScreenInstance);
                } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }));
        }
        if (FabricLoader.getInstance().isModLoaded("viafabricplus")) {
            widgets.add(new AnimatedTexturedButtonWidget(0, 0, 40, 40, 0, 0, 0, 8, -1, VIAFABRIC_ICON, 40, 40, (button) -> {
//                this.client.setScreen(new de.florianmichael.viafabricplus.screen.impl.settings.SettingsScreen());
                try {
                    Class<?> settingsScreen = Class.forName("de.florianmichael.viafabricplus.screen.impl.settings.SettingsScreen");
                    Object settingsScreenInstance = settingsScreen.getDeclaredConstructor().newInstance();
                    this.client.setScreen((Screen) settingsScreenInstance);
                } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }));
        }
        if (FabricLoader.getInstance().isModLoaded("replaymod")) {
            widgets.add(new AnimatedTexturedButtonWidget(0, 0, 40, 40, 0, 0, 0, 8, -1, REPLAYMOD_ICON, 40, 40, (button) -> {
//                this.client.setScreen(new com.replaymod.replay.gui.screen.GuiReplayViewer(com.replaymod.replay.ReplayModReplay.instance).toMinecraft());
                try {
                    Class<?> replayModReplay = Class.forName("com.replaymod.replay.ReplayModReplay");
                    Field instance = replayModReplay.getDeclaredField("instance");
                    Class<?> guiReplayViewer = Class.forName("com.replaymod.replay.gui.screen.GuiReplayViewer");
                    Method toMinecraft = guiReplayViewer.getMethod("toMinecraft");
                    Object instanceValue = instance.get(null);
                    Object guiReplayViewerInstance = guiReplayViewer.getDeclaredConstructor(instance.getType()).newInstance(instanceValue);
                    Object guiReplayViewerToMinecraft = toMinecraft.invoke(guiReplayViewerInstance);
                    this.client.setScreen((Screen) guiReplayViewerToMinecraft);
                } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException |
                         InvocationTargetException | NoSuchFieldException | InstantiationException e) {
                    e.printStackTrace();
                }
            }));
        }

        int widgetsWidth = widgets.size() * 40 + (widgets.size() - 1) * 10;
        int widgetsLeft = this.width / 2 - widgetsWidth / 2;
        int widgetsTop = this.height / 2 + 10;
        for (int i = 0; i < widgets.size(); i++) {
            ButtonWidget widget = widgets.get(i);
            widget.setPosition(widgetsLeft + i * 50, widgetsTop);
            if (widget instanceof AnimatedTexturedButtonWidget animatedWidget) {
                animatedWidget.startFadeIn = ((double)i / (double)widgets.size()) + 1;
            }
            this.addDrawableChild(widget);
        }

        this.addDrawableChild(new AnimatedTexturedButtonWidget(this.width - 30 - 10, 10, 30, 30, 0, 0, 0, 4, -1, EXIT_ICON, 30, 30, (button) -> {
            this.client.scheduleStop();
        }, Text.translatable("menu.quit")));
    }

    @Unique
    private static boolean firstBackgroundRender = true;

    @Inject(at = @At("HEAD"), method = "render", cancellable = true)
    private void render(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (firstBackgroundRender) {
            this.backgroundFadeStart = Util.getMeasuringTimeMs();
            firstBackgroundRender = false;
        }

        double fade = (Util.getMeasuringTimeMs() - this.backgroundFadeStart) / 1000.0;

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        context.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        int zoom = 10;
        context.drawTexture(BACKGROUND_IMAGE, 0, 0, (float)mouseX / (float)this.width * zoom * 2, (float)mouseY / (float)this.height * zoom, this.width, this.height, this.width + zoom * 2, this.height + zoom);
        context.drawTexture(TITLE_IMAGE, this.width / 2 - 128, this.height / 2 - 74, 0, 0, 256, 64, 256, 64);

        for (Drawable drawable : this.drawables) {
            if (drawable instanceof AnimatedTexturedButtonWidget button) {
                if (button.visible) {
                    button.hovered = mouseX >= button.getX() && mouseY >= button.getY() && mouseX < button.getX() + button.getWidth() && mouseY < button.getY() + button.getHeight();
                    button.renderButton(context, mouseX, mouseY, delta, fade);
                    button.applyTooltip();
                }
            } else {
                drawable.render(context, mouseX, mouseY, delta);
            }
        }

        RenderSystem.disableBlend();

        ci.cancel();
    }
}