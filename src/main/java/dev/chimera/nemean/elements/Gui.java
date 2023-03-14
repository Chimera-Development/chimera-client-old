package dev.chimera.nemean.elements;

import dev.chimera.ChimeraClient;
import dev.chimera.modules.ModuleInitializer;
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

//    @EventListener(id = EventListenerIDs.lwjglRendererTick, runAfter = EventListenerIDs.firstRenderer, runBefore = EventListenerIDs.lastRenderer)
//    public void render(GuiRenderEvent event) {
//
//    }

    @Override
    public void render() {
        //TODO should probably add more screens where gui should not be visible (or we could separate general render and ingame render)
        if(MinecraftClient.getInstance().currentScreen instanceof TitleScreen || MinecraftClient.getInstance().currentScreen instanceof MultiplayerScreen)
            return;
        ImGui.begin("ChimeraGUI");
        ModuleInitializer.getEnabledModuleList().forEach((module) -> {
//            if (ImGui.checkbox(module.getModuleName(), module.getModuleEnabled())) {
//                module.toggle();
//            }
            ImGui.text(module.getModuleName());
        });

        ImGui.end();
    }
//    @Override
//    public void close() {
//        isActive = false;
//        Module module = Objects.requireNonNull(ModuleInitializer.findModule("ClickGUI"));
//        module.toggle();
//        super.close();
//    }
}
