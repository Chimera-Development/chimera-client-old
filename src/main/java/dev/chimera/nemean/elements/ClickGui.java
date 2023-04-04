package dev.chimera.nemean.elements;

import dev.chimera.ChimeraClient;
import dev.chimera.managers.modules.AbstractModule;
import dev.chimera.managers.modules.ModuleCategory;
import dev.chimera.managers.modules.ModuleManager;
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
        AbstractModule module = Objects.requireNonNull(ModuleManager.findModule("ClickGUI"));
        module.toggle();
        super.close();
    }

    @Override
    public void render() {
        if (!this.isActive)
            return;

        HashMap<ModuleCategory, ArrayList<AbstractModule>> categorized = new HashMap<>();

        ModuleManager.getModules().forEach((module) -> {
            if(!categorized.containsKey(module.getCategory()))
                categorized.put(module.getCategory(), new ArrayList<>());
            categorized.get(module.getCategory()).add(module);
        });

        // Sort modules alphabetically
        for(ArrayList<AbstractModule> modulesInCategory : categorized.values())
            modulesInCategory.sort(Comparator.comparing(AbstractModule::getName));

        ImGui.frame(() -> {
            for (Map.Entry<ModuleCategory, ArrayList<AbstractModule>> entry : categorized.entrySet()) {
                ImGui.window(entry.getKey().name(), () -> {
                    for (AbstractModule module : entry.getValue()) {
                        if (ImGui.checkbox(module.getName(), module.isEnabled())) {
                            module.toggle();
                        }
                    }
              });
            }
        });
    }
}
