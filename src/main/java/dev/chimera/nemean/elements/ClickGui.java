package dev.chimera.nemean.elements;

import dev.chimera.ChimeraClient;
import dev.chimera.modules.Module;
import dev.chimera.modules.ModuleCategory;
import dev.chimera.modules.ModuleInitializer;
import dev.chimera.nemean.Renderable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import dev.chimera.nemean.ImGui;
import java.util.*;
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
            ImGui.begin(entry.getKey().getName());
            for(Module module : entry.getValue()) {
                if (ImGui.checkbox(module.getModuleName(), module.getModuleEnabled())) {
                    module.toggle();
                }
            }
            ImGui.end();
        }
    }
}
