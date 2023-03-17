package dev.chimera.nemean.elements;
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
import dev.chimera.modules.Module;
import dev.chimera.modules.ModuleCategory;
import dev.chimera.modules.ModuleInitializer;
import dev.chimera.nemean.Renderable;
import imgui.ImGui;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.*;

import static dev.chimera.nemean.GuiLayer.imgui;

public class ClickGui extends Screen implements Renderable {

    public static ClickGui INSTANCE;
    public boolean isActive = false;
    public ClickGui() {
        super(Text.of("ClickGUI"));
        INSTANCE = this;
        ChimeraClient.EVENT_BUS.registerListenersInClass(this);
        this.registerRenderable();
    }

    @Override
    public void close() {
        isActive = false;
        Module module = Objects.requireNonNull(ModuleInitializer.findModule("ClickGUI"));
        module.toggle();
        super.close();
    }

    @Override
    public void render() {
        if (!this.isActive)
            return;

        HashMap<ModuleCategory, ArrayList<Module>> categorized = new HashMap<>();

        ModuleInitializer.getAllModules().forEach((module) -> {
            if(!categorized.containsKey(module.getModuleCategory()))
                categorized.put(module.getModuleCategory(), new ArrayList<>());
            categorized.get(module.getModuleCategory()).add(module);
        });

        // Sort modules alphabetically
        for(ArrayList<Module> modulesInCategory : categorized.values())
            modulesInCategory.sort(Comparator.comparing(Module::getModuleName));

        for(Map.Entry<ModuleCategory, ArrayList<Module>> entry : categorized.entrySet()) {
            imgui.begin(entry.getKey().getName(), new boolean[] {true}, 0);
            for(Module module : entry.getValue()) {
                if (imgui.checkbox(module.getModuleName(), new boolean[] {module.getModuleEnabled()}, 0)) {
                    module.toggle();
                }
            }
            imgui.end();
        }
    }
}
