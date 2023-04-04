package dev.chimera.managers.modules.common;

import dev.chimera.ChimeraClient;
import dev.chimera.amalthea.eventbus.EventListener;
import dev.chimera.managers.modules.AbstractModule;
import dev.chimera.managers.modules.ModuleCategory;
import dev.chimera.nemean.elements.ClickGui;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;

public class ClickGUIModule extends AbstractModule {
    public ClickGui clickGuiScreenInstance;

    public ClickGUIModule() {
        super(ModuleCategory.MISC, "ClickGUI", GLFW.GLFW_KEY_RIGHT_SHIFT, false);
        clickGuiScreenInstance = new ClickGui();
        ChimeraClient.EVENT_BUS.registerListenersInClass(this);
    }

    @Override
    public void onEnable() {
        ChimeraClient.LOGGER.info("Opening gui!");
        clickGuiScreenInstance.isActive = true;
        justOpenedGui = true;
        MinecraftClient.getInstance().setScreen(clickGuiScreenInstance);
    }

    boolean justOpenedGui = false;

    @Override
    public void onDisable() {
        ChimeraClient.LOGGER.info("Closing gui!");
    }

    /*
    This should be fixed soon, but for now its temporary disabled until the gui is actually in the works

    @EventListener(id="chimera:closeGui")
    public void onKey(KeyEvents.InGUI.Press event) {
        if (justOpenedGui) {
            // This is a temporary fix for instant closing of gui
            // It appears that the onKey event triggers at same time as gui open.
            justOpenedGui = false;
            return;
        }
        if (event.key == this.getKeyBinding() && MinecraftClient.getInstance().currentScreen != null) {
            MinecraftClient.getInstance().currentScreen.close();
        }
    }
    */
}
