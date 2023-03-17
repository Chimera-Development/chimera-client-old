package dev.chimera.nemean;

import dev.chimera.ChimeraClient;
import dev.chimera.amalthea.EventListenerIDs;
import dev.chimera.amalthea.eventbus.EventListener;
import dev.chimera.amalthea.events.misc.GuiRenderEvent;
import imgui.ConfigFlag;
import imgui.ImGui;
import imgui.ImguiKt;
import imgui.classes.Context;
import uno.glfw.GlfwWindow;
import uno.glfw.VSync;
import imgui.impl.gl.ImplGL3;
import imgui.impl.glfw.ImplGlfw;
import net.minecraft.client.MinecraftClient;

import java.util.ArrayList;
import java.util.Objects;

public class  GuiLayer {
    public static ImGui imgui = ImGui.INSTANCE;
    private static ImplGlfw implGlfw;
    private static ImplGL3 implGl3;

    private static ArrayList<Renderable> renderStack = new ArrayList<>();

    public GuiLayer()
    {
        ChimeraClient.EVENT_BUS.registerListenersInClass(this);
    }


    public static void config(long windowPtr){
        ImguiKt.MINECRAFT_BEHAVIORS = true;

        GlfwWindow window = GlfwWindow.from(windowPtr);

        window.makeContextCurrent();
        new Context();

        implGlfw = new ImplGlfw(window, false);
        implGl3 = new ImplGL3();
        /*
        imgui.getIO().setConfigWindowsMoveFromTitleBarOnly(true);
        imgui.getStyle().setColor(ImGuiCol.WindowBg,
                251, 7, 255, 50);*/

        imgui.getStyle().setWindowRounding(10f);
        ranAlready = true;
    }
    private static boolean ranAlready = false;

    public static void registerRenderable(Renderable e){
        renderStack.add(e);
    }

    @EventListener(id = EventListenerIDs.onRender)
    public void onRender(GuiRenderEvent event){

        if (!ranAlready && MinecraftClient.getInstance().currentScreen != null) {
            // Only start gui when ingame
            event.cancelled = true;
            return;
        }
        //ImGui.getIO().setDisplaySize(MinecraftClient.getInstance().getWindow().getWidth(), MinecraftClient.getInstance().getWindow().getHeight());
//        if (!ranAlready) config();
        MinecraftClient.getInstance().getProfiler().push("ChimeraHUD");
//        if(isActive) {
        //does the imGui stuff
        implGl3.newFrame();
        implGlfw.newFrame();
        imgui.newFrame();

        for(Renderable renderable: renderStack){
            renderable.render();
        }

        imgui.render();
        implGl3.renderDrawData(Objects.requireNonNull(imgui.getDrawData()));

        MinecraftClient.getInstance().getProfiler().pop();
    }

//    @EventListener(id = EventListenerIDs.firstRenderer, runBefore = EventListenerIDs.lwjglRendererTick)
//    public void firstRenderer(GuiRenderEvent event){
//
//    }
//
//    @EventListener(id = EventListenerIDs.lastRenderer, runAfter = EventListenerIDs.lwjglRendererTick)
//    public void lastRenderer(GuiRenderEvent event){
//
//    }
}
