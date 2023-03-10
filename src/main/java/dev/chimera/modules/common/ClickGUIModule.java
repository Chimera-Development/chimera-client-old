package dev.chimera.modules.common;

import dev.chimera.ChimeraClient;
import dev.chimera.amalthea.eventbus.EventListener;
import dev.chimera.amalthea.events.misc.KeyEvents;
import dev.chimera.amalthea.events.misc.TickEvent;
import dev.chimera.modules.Module;
import dev.chimera.nemean.TestGUI;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;

public class ClickGUIModule extends Module {


    public TestGUI clickGuiScreenInstance;

    public ClickGUIModule()
    {
        super("ClickGUI", GLFW.GLFW_KEY_RIGHT_SHIFT, false);
        ChimeraClient.EVENT_BUS.registerListenersInClass(this);
    }

    @Override
    public void init() {

    }

    @Override
    public void onEnable() {
        ChimeraClient.LOGGER.info("Opening gui!");
        clickGuiScreenInstance = new TestGUI();
        clickGuiScreenInstance.isActive = true;
        justOpenedGui = true;
        MinecraftClient.getInstance().setScreen(clickGuiScreenInstance);
    }

    boolean justOpenedGui = false;

    @Override
    public void onDisable() {
        ChimeraClient.LOGGER.info("Closing gui!");
        clickGuiScreenInstance.isActive = false;
        // Screen has to be closed, otherwise gui will stay showing even if set as inactive
        if (MinecraftClient.getInstance().currentScreen != null) {
            MinecraftClient.getInstance().currentScreen.close();
        }
    }



    @Override
    public void onTickStart(TickEvent.Start event) {

    }
    @EventListener(id = "for now just testing")
    public void onKey(KeyEvents.InGUI.Press event){
        if(justOpenedGui)
        {
            // This is a temporary fix for instant closing of gui
            // It appears that the onKey event triggers at same time as gui open.
            justOpenedGui = false;
            return;
        }
        if(event.key == this.getKeyBinding()){
            this.toggle();
        }
    }

    @Override
    public void onTickEnd(TickEvent.End event) {

    }
}
