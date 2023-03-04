package dev.chimera.amalthea;

import dev.chimera.amalthea.events.AbstractEvent;

import java.lang.reflect.Method;

public class Listener{

    public Object listenerObject;
    public Method listenerMethod;

    public Listener(Object object, Method method){
        this.listenerMethod = method;
        this.listenerObject = object;
    }

}