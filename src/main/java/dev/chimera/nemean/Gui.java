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
    private final ImGuiImplGlfw implGlfw = new ImGuiImplGlfw();
    private final ImGuiImplGl3 implGl3 = new ImGuiImplGl3();

    public Gui() {
        ChimeraClient.EVENT_BUS.registerListenersInClass(this);
    }

    private void config(){
        long windowPtr = MinecraftClient.getInstance().getWindow().getHandle();
        INSTANCE = this;

        ImGui.createContext();
        implGlfw.init(windowPtr, true);
        implGl3.init();

        ImGui.getIO().setConfigWindowsMoveFromTitleBarOnly(true);
        ImGui.getIO().setDisplaySize(MinecraftClient.getInstance().getWindow().getWidth(), MinecraftClient.getInstance().getWindow().getHeight());
        ImGui.getStyle().setColor(ImGuiCol.WindowBg,
                251, 7, 255, 50);
        ImGui.getStyle().setWindowRounding(10f);
        ranAlready = true;
    }
    private boolean ranAlready = false;
    @EventListener(id = EventListenerIDs.lwjglRendererTick)
    public void render(GuiRenderEvent event) {
        if(!ranAlready && MinecraftClient.getInstance().currentScreen != null) return; // Only start gui when ingame
        if(!ranAlready) config();
        MinecraftClient.getInstance().getProfiler().push("ChimeraHUD");
//        if(isActive) {
        //does the imGui stuff
        implGlfw.newFrame();
        ImGui.newFrame();

        ImGui.begin("ChimeraGUI");
        ModuleInitializer.getEnabledModuleList().forEach((module) -> {
//            if (ImGui.checkbox(module.getModuleName(), module.getModuleEnabled())) {
//                module.toggle();
//            }
            ImGui.text(module.getModuleName());
        });

        ImGui.end();

        ClickGUIModule m = (ClickGUIModule) ModuleInitializer.findModule("ClickGUI");
        if (m.getModuleEnabled())
        {
            m.clickGuiScreenInstance.renderClickGUI();
        }

        ImGui.endFrame();
        ImGui.render();
        implGl3.renderDrawData(Objects.requireNonNull(ImGui.getDrawData()));

//        }
        MinecraftClient.getInstance().getProfiler().pop();
    }

//    @Override
//    public void close() {
//        isActive = false;
//        Module module = Objects.requireNonNull(ModuleInitializer.findModule("ClickGUI"));
//        module.toggle();
//        super.close();
//    }
}
