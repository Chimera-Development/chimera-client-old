package dev.chimera.modules.player;

import dev.chimera.amalthea.events.misc.TickEvent;
import dev.chimera.modules.Module;

public class NoFallModule extends Module {
    public NoFallModule() {
        super("No-Fall");
    }

    @Override
    public void init() {
        setModuleState(true);
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}

    @Override
    public void onTickStart(TickEvent.Start event) {}

    @Override
    public void onTickEnd(TickEvent.End event) {}
}
