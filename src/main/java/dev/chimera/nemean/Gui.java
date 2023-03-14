package dev.chimera.nemean;

import dev.chimera.ChimeraClient;
import dev.chimera.Utils.Utils;
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
import net.minecraft.client.network.PlayerListEntry;

import java.awt.geom.QuadCurve2D;
import java.util.Objects;

import static dev.chimera.ChimeraClient.mc;
import static dev.chimera.Utils.Utils.tpsHistory;

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
        renderDebug();


    }
    private void renderDebug(){
        ImGui.begin("Debug");
        // TPS
//        float[] floatArray = new float[tpsHistory.size()];
//        for (int i = 0; i < tpsHistory.size(); i++) {
//            floatArray[i] = tpsHistory.get(i).floatValue();
//        }
//        ImGui.plotLines("Tps",floatArray,floatArray.length);
        ImGui.text("TPS: " + Utils.getTPS());

        // Ping
        PlayerListEntry playerEntry = mc.player.networkHandler.getPlayerListEntry(mc.player.getGameProfile().getId());
        int ping = playerEntry == null ? 0 : playerEntry.getLatency();
        ImGui.text("Ping: "+ ping);
        // FPS
        int fps = mc.getCurrentFps();
        ImGui.text("FPS: " + fps);
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
