package dev.chimera.modules;

import dev.chimera.ChimeraClient;
import dev.chimera.amalthea.events.misc.TickEvent;
import org.lwjgl.glfw.GLFW;

/*
    **IMPORTANT**
    YOU *MUST* REGISTER ALL MODULES IN ModuleInitializerOld.java OR THIS *WILL NOT WORK*
*/

public class ExampleModule extends Module {
    public ExampleModule() {
        super("Example Module", GLFW.GLFW_KEY_Y, true);
    }

    @Override
    public void init() {
        ChimeraClient.LOGGER.info("t Initialized module '" + getModuleName() + "'");
    }

    @Override
    public void onEnable() {
        ChimeraClient.LOGGER.info("t Enabled module '" + getModuleName() + "'");
    }

    @Override
    public void onDisable() {
        ChimeraClient.LOGGER.info("t Disabled module '" + getModuleName() + "'");
    }

    @Override
    public void onTickStart(TickEvent.Start event) {

    }

    @Override
    public void onTickEnd(TickEvent.End event) {

    }
}
