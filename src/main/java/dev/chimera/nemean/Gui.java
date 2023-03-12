package dev.chimera.nemean;

import dev.chimera.ChimeraClient;
import dev.chimera.amalthea.EventListenerIDs;
import dev.chimera.amalthea.eventbus.EventListener;
import dev.chimera.amalthea.events.misc.GuiRenderEvent;
import dev.chimera.modules.ModuleInitializer;
import dev.chimera.modules.common.ClickGUIModule;
import imgui.ImGui;
import imgui.flag.ImGuiCol;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import net.minecraft.client.MinecraftClient;

import java.util.Objects;

public class Gui {

    public static Gui INSTANCE;
    public boolean isActive = false;

    public Gui() {
        ChimeraClient.EVENT_BUS.registerListenersInClass(this);
    }

    @EventListener(id = EventListenerIDs.lwjglRendererTick, runAfter = EventListenerIDs.firstRenderer, runBefore = EventListenerIDs.lastRenderer)
    public void render(GuiRenderEvent event) {
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
