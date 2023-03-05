package dev.chimera.amalthea.events;

import dev.chimera.ChimeraClient;
import dev.chimera.EventListeners;
import dev.chimera.amalthea.EventListener;
import dev.chimera.amalthea.Priority;

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

    @EventListener(id = "@+id/runSecond", dependencies = {"@+id/runFirst"})
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

    @EventListener(id = "@+id/b", dependencies = {EventListeners.a})
    private void b(String test){

        ChimeraClient.LOGGER.warn("b");
    }

    @EventListener(id = "@+id/c", dependencies = {EventListeners.a, "@+id/b"})
    private void c(String test){

        ChimeraClient.LOGGER.warn("c");
    }

    @EventListener(id = "@+id/d", dependencies = {EventListeners.a, "@+id/b", "@+id/c"})
    private void d(String test){

        ChimeraClient.LOGGER.warn("d");
    }

    @EventListener(id = "@+id/e", dependencies = {EventListeners.a, "@+id/c", "@+id/d"})
    private void e(String test){

        ChimeraClient.LOGGER.warn("e");
    }



}
