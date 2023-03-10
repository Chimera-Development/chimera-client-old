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


import dev.chimera.modules.ModuleInitializer;
import imgui.ImGui;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.Objects;

public class TestGUI extends Screen {

    public static TestGUI INSTANCE;
    public boolean isActive = false;
    private final ImGuiImplGlfw implGlfw = new ImGuiImplGlfw();
    private final ImGuiImplGl3 implGl3 = new ImGuiImplGl3();

    public TestGUI() {
        super(Text.of("idkpleasework"));
        long windowPtr = MinecraftClient.getInstance().getWindow().getHandle();
        INSTANCE = this;
        ImGui.createContext();
        implGlfw.init(windowPtr, true);
        implGl3.init();
        ImGui.getIO().setConfigWindowsMoveFromTitleBarOnly(true);
        ImGui.getIO().setDisplaySize(MinecraftClient.getInstance().getWindow().getWidth(), MinecraftClient.getInstance().getWindow().getHeight());
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
//        if(isActive) {
            //does the imGui stuff
            implGlfw.newFrame();
            ImGui.newFrame();

            ImGui.begin("ClickGUI!");
            ModuleInitializer.getAllModules().forEach((module) -> {
                if (ImGui.checkbox(module.getModuleName(), module.getModuleEnabled())) {
                    module.toggle();
                }
            });


            ImGui.end();

            ImGui.endFrame();
            ImGui.render();
            implGl3.renderDrawData(Objects.requireNonNull(ImGui.getDrawData()));

//        }
    }



}
