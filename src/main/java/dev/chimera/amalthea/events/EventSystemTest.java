package dev.chimera.amalthea.events;

import dev.chimera.ChimeraClient;
import dev.chimera.amalthea.EventListenerIDs;
import dev.chimera.Utils.Utils;
import dev.chimera.amalthea.eventbus.EventListener;
import dev.chimera.amalthea.events.misc.TickEvent;

import static dev.chimera.ChimeraClient.presence;

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

    @EventListener(id = EventListenerIDs.a)
    private void a(String test){
        ChimeraClient.LOGGER.warn("a");
    }

    @EventListener(id = "@+id/b", runAfter = {EventListenerIDs.a}, runBefore = {"@+id/c"})
    private void b(String test){

        ChimeraClient.LOGGER.warn("b");
    }

    @EventListener(id = "@+id/c", runAfter = {EventListenerIDs.a})
    private void c(String test){

        ChimeraClient.LOGGER.warn("c");
    }

    @EventListener(id = "@+id/d", runAfter = {EventListenerIDs.a, "@+id/b", "@+id/c"})
    private void d(String test){

        ChimeraClient.LOGGER.warn("d");
    }

    @EventListener(id = "@+id/e", runAfter = {EventListenerIDs.a, "@+id/c", "@+id/d"})
    private void e(String test){
            ChimeraClient.LOGGER.warn("e");
    }

    @EventListener(id="tick")
    public void onTick(TickEvent.Start event){
        ChimeraClient.LOGGER.debug("tick");
        presence.setDetails("Playing in " + Utils.getWorldName());
    }

}
