package dev.chimera.nemean.elements;

import dev.chimera.ChimeraClient;
import dev.chimera.modules.ModuleInitializer;
import dev.chimera.nemean.Renderable;
import imgui.ImGui;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;

import static dev.chimera.nemean.GuiLayer.imgui;


public class Gui implements Renderable {

    public static Gui INSTANCE;
    public boolean isActive = false;

    public Gui() {
        ChimeraClient.EVENT_BUS.registerListenersInClass(this);
        this.registerRenderable();
    }

    @Override
    public void render() {
        //TODO should probably add more screens where gui should not be visible (or we could separate general render and ingame render)
        if (MinecraftClient.getInstance().currentScreen instanceof TitleScreen || MinecraftClient.getInstance().currentScreen instanceof MultiplayerScreen)
            return;

        imgui.begin("ChimeraGUI", new boolean[] {true}, 0);
        ModuleInitializer.getEnabledModuleList().forEach((module) -> {
            imgui.text(module.getModuleName());
        });

        imgui.end();
    }
}
