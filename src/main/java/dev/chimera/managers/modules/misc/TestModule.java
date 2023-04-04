package dev.chimera.managers.modules.misc;

import dev.chimera.amalthea.eventbus.EventListener;
import dev.chimera.amalthea.events.packet.PacketEvent;
import dev.chimera.managers.modules.AbstractModule;
import dev.chimera.managers.modules.ModuleCategory;

public class TestModule extends AbstractModule {
    public TestModule() {
        super("TestModule", ModuleCategory.MISC);
    }

    @EventListener(id = "susser")
    private void onPacketSend(PacketEvent.Send event) {
        System.out.println("susser");
    }
}
