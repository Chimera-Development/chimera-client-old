package dev.chimera.amalthea.events;

import dev.chimera.ChimeraClient;
import dev.chimera.amalthea.EventListener;

public class EventSystemTest {

    public void main() {
            ChimeraClient.EVENT_BUS.registerListenersInClass(this);
    }

    @EventListener
    public void ass(String test) {
        ChimeraClient.LOGGER.warn("WORKS!!!!!" + test);
    }

    @EventListener(tag = "sussy")
    private void sussy(String test) {
        ChimeraClient.LOGGER.warn("Works??" + test);
    }

}
