package dev.chimera.nemean.elements;

import dev.chimera.ChimeraClient;
import dev.chimera.amalthea.eventbus.EventListener;
import dev.chimera.amalthea.events.input.KeyboardEvent;
import dev.chimera.nemean.Renderable;
import dev.chimera.sisyphus.Addon;
import dev.chimera.sisyphus.AddonInitializer;
import imgui.ImGui;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class AddonsScreen extends Screen implements Renderable {

    public boolean isActive = false;

    public AddonsScreen() {
        super(Text.of("Addons Screen"));
        this.registerRenderable();
        ChimeraClient.EVENT_BUS.registerListenersInClass(this);
    }

    @EventListener(id = "AddonsScreenEscape")
    public void handleEsc(KeyboardEvent.Press escKey) {
        if (escKey.getKey() == GLFW.GLFW_KEY_ESCAPE)
            this.isActive = false;
    }

    @Override
    public void render() {
        if (!isActive)
            return;
        ImGui.begin("Addons");

        for (Addon addon : AddonInitializer.ADDON_LIST) {
            if (addon.MOD_ID == null || addon.name == null)
                continue;
            if (ImGui.collapsingHeader(addon.name)) {
                ImGui.sameLine();
//                ImGui.image();
            }
        }

        ImGui.end();
    }

    @Override
    public void close() {
        isActive = false;

        super.close();
    }
}
