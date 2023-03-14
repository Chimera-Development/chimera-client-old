package dev.chimera.nemean.elements;

import dev.chimera.nemean.Renderable;
import dev.chimera.sisyphus.Addon;
import dev.chimera.sisyphus.AddonInitializer;
import imgui.ImGui;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class AddonsScreen extends Screen implements Renderable {

    public boolean isActive = false;

    public AddonsScreen() {
        super(Text.of("Addons Screen"));
        this.registerRenderable();
    }

    @Override
    public void render() {

        if (!isActive)
            return;

        for (Addon addon : AddonInitializer.ADDON_LIST) {
            if (addon.MOD_ID == null || addon.name == null)
                continue;
            ImGui.begin(addon.name);

            if (ImGui.collapsingHeader(addon.name)) {
                ImGui.text(addon.MOD_ID);
            }

            ImGui.end();
        }

    }

    @Override
    public void close() {
        isActive = false;

        super.close();
    }
}
