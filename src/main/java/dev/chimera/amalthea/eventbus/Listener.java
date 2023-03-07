package dev.chimera.amalthea.eventbus;

import java.lang.reflect.*;
import java.util.List;

public class Listener{

    private final Object object;
    private final String id;
    private final Method method;
    //If a listener wants to run before another, it will just add itself to that modules runAfter list
    private List<String> runAfter = null;

    public Listener(Object object, Method method, String id) {
        this.object = object;
        this.method = method;
        this.id = id;
    }

    public Listener(Object object, Method method, String id, List<String> runAfter) {
        this.object = object;
        this.method = method;
        this.id = id;
        this.runAfter = runAfter;
    }

    public void addDependency(String dependency){
        runAfter.add(dependency);
    }

    public void invoke(Object event) throws InvocationTargetException, IllegalAccessException {
        method.invoke(object, event);
    }

    public List<String> getRunAfter(){
        return runAfter;
    }

    public String getId(){
        return id;
    }

    public Method getMethod() {
        return method;
    }

}
