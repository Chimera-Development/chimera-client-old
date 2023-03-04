package dev.chimera.modules;

import dev.chimera.amalthea.events.misc.TickEvent;

public abstract class Module {
    private final String MODULE_NAME;
    private boolean MODULE_ENABLED;
    
    public Module(String name) {
        MODULE_NAME = name;
        MODULE_ENABLED = false;
    }

    public String getModuleName() { return MODULE_NAME; }

    public boolean getModuleEnabled() { return MODULE_ENABLED; }

    public void setModuleState(boolean state) {
        if (state != MODULE_ENABLED) {
            if (state) onEnable();
            else onDisable();
        }
        MODULE_ENABLED = state;
    }

    public abstract void init();
    public abstract void onEnable();
    public abstract void onDisable();

    public abstract void onTickStart(TickEvent event);
    public abstract void onTickEnd(TickEvent event);
}
