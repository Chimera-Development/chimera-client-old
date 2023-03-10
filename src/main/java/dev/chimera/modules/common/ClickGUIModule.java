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
        MinecraftClient.getInstance().setScreen(clickGuiScreenInstance);
    }

    @Override
    public void onDisable() {
        ChimeraClient.LOGGER.info("Closing gui!");
        clickGuiScreenInstance.isActive = false;
    }



    @Override
    public void onTickStart(TickEvent.Start event) {

    }
    @EventListener(id = "for now just testing")
    public void onKey(KeyEvents.InGUI.Release event){
        if(event.key == this.getKeyBinding()){
            this.toggle();
        }
    }

    @Override
    public void onTickEnd(TickEvent.End event) {

    }
}
