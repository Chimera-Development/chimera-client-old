package dev.chimera.nemean;
//totally didnt copy half of Authoms code: https://github.com/fartdev/Atomic/blob/master/src/main/java/me/zeroX150/atomic/feature/gui/ImGuiScreen.java
//0x's code didnt work. switched to :https://www.youtube.com/watch?v=6jmxwRMb-aY
//Imgui GitHub: https://github.com/SpaiR/imgui-java/tree/v1.84.1.2

/*
my implementation was

        make instance
        on clickgui open, show instance (this was taken from 0x150's pre-existing clickgui system, presumably to preserve the state of windows)
        have a command to reinitialize imgui

        so then you could just have it work once and preserve that imgui instance (shitty method)

        This is what Authom(The main guy of this gui shit told me was a solution to crashing and derping clickgui.)
*/


import dev.chimera.ChimeraClient;
import dev.chimera.amalthea.EventListenerIDs;
import dev.chimera.amalthea.eventbus.EventListener;
import dev.chimera.amalthea.events.misc.GuiRenderEvent;
import dev.chimera.modules.Module;
import dev.chimera.modules.ModuleInitializer;
import imgui.ImGui;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.Objects;

public class ClickGui extends Screen {

    public static ClickGui INSTANCE;
    public boolean isActive = false;
    //FUNNY TEST
    public ClickGui() {
        super(Text.of("idkpleasework"));
        INSTANCE = this;
        ChimeraClient.EVENT_BUS.registerListenersInClass(this);
    }


    @EventListener(id = EventListenerIDs.lwjglRendererTick, runAfter = EventListenerIDs.firstRenderer, runBefore = EventListenerIDs.lastRenderer)
    public void renderClickGUI(GuiRenderEvent event)
    {
        if (!this.isActive)
            return;
        ImGui.begin("ClickGUI!");
        ModuleInitializer.getAllModules().forEach((module) -> {
            if (ImGui.checkbox(module.getModuleName(), module.getModuleEnabled())) {
                module.toggle();
            }
        });


        ImGui.end();
    }

    @Override
    public void close() {
        isActive = false;
        Module module = Objects.requireNonNull(ModuleInitializer.findModule("ClickGUI"));
        module.toggle();
        super.close();
    }
}
