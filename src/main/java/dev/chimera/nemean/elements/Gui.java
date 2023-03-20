package dev.chimera.nemean.elements;

import dev.chimera.ChimeraClient;
import dev.chimera.managers.modules.ModuleManager;
import dev.chimera.nemean.Renderable;
import imgui.ImGui;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;

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
        ImGui.begin("ChimeraGUI");
        ModuleManager.getEnabledModuleList().forEach((module) -> {
            ImGui.text(module.getModuleName());
        });

        ImGui.end();
    }
}
