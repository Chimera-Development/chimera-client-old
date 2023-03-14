package dev.chimera.nemean;

import dev.chimera.ChimeraClient;
import dev.chimera.amalthea.EventListenerIDs;
import dev.chimera.amalthea.eventbus.EventListener;
import dev.chimera.amalthea.events.misc.GuiRenderEvent;
import imgui.ImGui;
import imgui.flag.ImGuiCol;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import net.minecraft.client.MinecraftClient;

import java.util.Objects;

public class GuiLayer {
    private static final ImGuiImplGlfw implGlfw = new ImGuiImplGlfw();
    private static final ImGuiImplGl3 implGl3 = new ImGuiImplGl3();

    public GuiLayer()
    {
        ChimeraClient.EVENT_BUS.registerListenersInClass(this);
    }


    public static void config(long windowPtr){

        ImGui.createContext();
        implGlfw.init(windowPtr, true);
        implGl3.init();

        ImGui.getIO().setConfigWindowsMoveFromTitleBarOnly(true);
        ImGui.getStyle().setColor(ImGuiCol.WindowBg,
                251, 7, 255, 50);
        ImGui.getStyle().setWindowRounding(10f);
        ranAlready = true;
    }
    private static boolean ranAlready = false;


    @EventListener(id = EventListenerIDs.firstRenderer, runBefore = EventListenerIDs.lwjglRendererTick)
    public void firstRenderer(GuiRenderEvent event){
        if (!ranAlready && MinecraftClient.getInstance().currentScreen != null) {
            // Only start gui when ingame
            event.cancelled = true;
            return;
        }
        ImGui.getIO().setDisplaySize(MinecraftClient.getInstance().getWindow().getWidth(), MinecraftClient.getInstance().getWindow().getHeight());
//        if (!ranAlready) config();
        MinecraftClient.getInstance().getProfiler().push("ChimeraHUD");
//        if(isActive) {
        //does the imGui stuff
        implGlfw.newFrame();
        ImGui.newFrame();
    }

    @EventListener(id = EventListenerIDs.lastRenderer, runAfter = EventListenerIDs.lwjglRendererTick)
    public void lastRenderer(GuiRenderEvent event){
        ImGui.endFrame();
        ImGui.render();
        implGl3.renderDrawData(Objects.requireNonNull(ImGui.getDrawData()));

        MinecraftClient.getInstance().getProfiler().pop();
    }
}
