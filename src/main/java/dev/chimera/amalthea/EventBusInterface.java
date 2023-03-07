package dev.chimera.amalthea;

import dev.chimera.amalthea.events.AbstractEvent;

public interface EventBusInterface {

    <T> void post(T event);

    <T> void post(String tag, T event);


}
