package dev.chimera.amalthea.events;

import dev.chimera.ChimeraClient;
import dev.chimera.amalthea.EventListener;
import dev.chimera.amalthea.Priority;

public class EventSystemTest {

    public EventSystemTest() {
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
    @EventListener(priority = Priority.HIGH)
    private void shouldRunSecond(String test){
        ChimeraClient.LOGGER.warn("2" + test);
    }
    @EventListener(priority = Priority.HIGHEST)
    private void shouldRunFirst(String test){
        ChimeraClient.LOGGER.warn("1" + test);
    }
    @EventListener(priority = Priority.MEDIUM)
    private void shouldRunThird(String test){
        ChimeraClient.LOGGER.warn("3" + test);
    }

}
