package dev.chimera.modules;

import dev.chimera.ChimeraClient;
import dev.chimera.amalthea.events.misc.TickEvent;

/*
    **IMPORTANT**
    YOU *MUST* REGISTER ALL MODULES IN ModuleInitializer.java OR THIS *WILL NOT WORK*
*/

public class ExampleModule extends Module {
    public ExampleModule() {
        super("Example Module");
    }

    @Override
    public void init() {
        ChimeraClient.LOGGER.info("Initialized module '" + getModuleName() + "'");
    }

    @Override
    public void onEnable() {
        ChimeraClient.LOGGER.info("Enabled module '" + getModuleName() + "'");
    }

    @Override
    public void onDisable() {
        ChimeraClient.LOGGER.info("Disabled module '" + getModuleName() + "'");
    }

    @Override
    public void onTickStart(TickEvent event) {

    }

    @Override
    public void onTickEnd(TickEvent event) {

    }
}
