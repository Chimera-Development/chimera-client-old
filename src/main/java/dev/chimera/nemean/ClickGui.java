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


import dev.chimera.modules.Module;
import dev.chimera.modules.ModuleCategory;
import dev.chimera.modules.ModuleInitializer;
import imgui.ImGui;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.*;

public class ClickGui extends Screen {

    public static ClickGui INSTANCE;
    public boolean isActive = false;
    private final ImGuiImplGlfw implGlfw = new ImGuiImplGlfw();
    private final ImGuiImplGl3 implGl3 = new ImGuiImplGl3();
    //FUNNY TEST
    public ClickGui() {
        super(Text.of("idkpleasework"));
        long windowPtr = MinecraftClient.getInstance().getWindow().getHandle();
        INSTANCE = this;
//        ImGui.createContext();
        implGlfw.init(windowPtr, true);
        implGl3.init();
        ImGui.getIO().setConfigWindowsMoveFromTitleBarOnly(true);
        ImGui.getIO().setDisplaySize(MinecraftClient.getInstance().getWindow().getWidth(), MinecraftClient.getInstance().getWindow().getHeight());
    }

    /*
    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        MinecraftClient.getInstance().getProfiler().push("ChimeraClickGUI");
//        if(isActive) {
        //does the imGui stuff
        implGlfw.newFrame();
        ImGui.newFrame();

        ImGui.endFrame();
        ImGui.render();
        implGl3.renderDrawData(Objects.requireNonNull(ImGui.getDrawData()));

//        }
        MinecraftClient.getInstance().getProfiler().pop();
    }*/

    public void renderClickGUI()
    {
        HashMap<ModuleCategory, ArrayList<Module>> categorized = new HashMap<>();
        ModuleInitializer.getAllModules().forEach((module) -> {
            if(!categorized.containsKey(module.getModuleCategory()))
                categorized.put(module.getModuleCategory(), new ArrayList<>());
            categorized.get(module.getModuleCategory()).add(module);
        });

        // Sort modules alphabetically
        for(ArrayList<Module> modulesInCategory : categorized.values())
            Collections.sort(modulesInCategory, Comparator.comparing(Module::getModuleName));

        for(Map.Entry<ModuleCategory, ArrayList<Module>> entry : categorized.entrySet()) {
            ImGui.begin(entry.getKey().getName());
            for(Module module : entry.getValue()) {
                if (ImGui.checkbox(module.getModuleName(), module.getModuleEnabled())) {
                    module.toggle();
                }
            }
            ImGui.end();
        }
    }

    @Override
    public void close() {
        isActive = false;
        Module module = Objects.requireNonNull(ModuleInitializer.findModule("ClickGUI"));
        module.toggle();
        super.close();
    }
}
