package dev.chimera.amalthea.events;

import dev.chimera.ChimeraClient;
import dev.chimera.EventListeners;
import dev.chimera.amalthea.EventListener;

public class EventSystemTest {

    public EventSystemTest() {
        ChimeraClient.EVENT_BUS.registerListenersInClass(this);
    }

//    @EventListener
//    public void ass(String test) {
//        ChimeraClient.LOGGER.warn("WORKS!!!!!" + test);
//    }
//
//    @EventListener(tag = "sussy")
//    private void sussy(String test) {
//        ChimeraClient.LOGGER.warn("Works??" + test);
//    }

    @EventListener(id = "@+id/runSecond", runAfter = {"@+id/runFirst"})
    private void shouldRunSecond(String test){
        ChimeraClient.LOGGER.warn("2" + test);
    }
    @EventListener(id = "@+id/runFirst")
    private void shouldRunFirst(String test){
        ChimeraClient.LOGGER.warn("1" + test);
    }

    @EventListener(id = EventListeners.a)
    private void a(String test){
        ChimeraClient.LOGGER.warn("a");
    }

    @EventListener(id = "@+id/b", runAfter = {EventListeners.a}, runBefore = {"@+id/c"})
    private void b(String test){

        ChimeraClient.LOGGER.warn("b");
    }

    @EventListener(id = "@+id/c", runAfter = {EventListeners.a})
    private void c(String test){

        ChimeraClient.LOGGER.warn("c");
    }

    @EventListener(id = "@+id/d", runAfter = {EventListeners.a, "@+id/b", "@+id/c"})
    private void d(String test){

        ChimeraClient.LOGGER.warn("d");
    }

    @EventListener(id = "@+id/e", runAfter = {EventListeners.a, "@+id/c", "@+id/d"})
    private void e(String test){
        ChimeraClient.LOGGER.warn("e");
    }



}
