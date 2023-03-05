package dev.chimera.modules;

import dev.chimera.ChimeraClient;
import dev.chimera.amalthea.events.misc.TickEvent;

public abstract class Module {
    private final String MODULE_NAME;
    private boolean MODULE_ENABLED;
    private int KEY_BINDING;
    public boolean releaseToToggle;

    public Module(String name, int bind, boolean toggleOnRelease) {
        MODULE_NAME = name;
        MODULE_ENABLED = false;
        KEY_BINDING = bind;
        releaseToToggle = toggleOnRelease;
    }
    public Module(String name, int bind) {
        this(name, bind, false);
    }
    public Module(String name, boolean toggleOnRelease) {
        this(name, -1, toggleOnRelease);
    }
    public Module(String name) {
        this(name, -1, false);
    }


    public String getModuleName() { return MODULE_NAME; }

    public boolean getModuleEnabled() { return MODULE_ENABLED; }

    public int getKeyBinding() { return KEY_BINDING; }
    public void setKeyBinding(int bind) { KEY_BINDING = bind; }
    public boolean keyBindingMatches(int compareBind) {
        return KEY_BINDING == compareBind;
    }

    public void setModuleState(boolean state) {
        if (state != MODULE_ENABLED) {
            if (state) onEnable();
            else onDisable();
        }
        MODULE_ENABLED = state;
    }

    public void toggle() { setModuleState(!getModuleEnabled()); }

    public abstract void init();
    public abstract void onEnable();
    public abstract void onDisable();

    public abstract void onTickStart(TickEvent.Start event);
    public abstract void onTickEnd(TickEvent.End event);
}
