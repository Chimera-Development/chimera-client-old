package dev.chimera.amalthea.events;

import dev.chimera.ChimeraClient;
import dev.chimera.amalthea.EventListener;

public class EventSystemTest {

    public void main(){
        try{
        ChimeraClient.EVENT_BUS.registerListenersInClass(this);
    }catch (IllegalAccessException e){
            throw new RuntimeException(e);
        }
    }

    @EventListener
    public void ass(String test){
        ChimeraClient.LOGGER.warn("WORKS!!!!!" + test);
    }

    @EventListener(tag = "sussy")
    public void sussy(String test){
        ChimeraClient.LOGGER.warn("Works??" + test);
    }

}
