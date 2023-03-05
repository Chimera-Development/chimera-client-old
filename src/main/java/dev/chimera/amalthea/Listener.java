package dev.chimera.amalthea;

import java.lang.reflect.*;
import java.util.List;

public class Listener implements Comparable<Listener> {

    private final Object object;
    private final String id;
    private final Method method;
    private final int priority;

    private List<String> dependencies = null;

    public Listener(Object object, Method method, int priority, String id) {
        this.object = object;
        this.method = method;
        this.priority = priority;
        this.id = id;
    }

    public Listener(Object object, Method method, int priority, String id, List<String> dependencies) {
        this.object = object;
        this.method = method;
        this.priority = priority;
        this.id = id;
        this.dependencies = dependencies;
    }

    public void invoke(Object event) throws InvocationTargetException, IllegalAccessException {
        method.invoke(object, event);
    }

    public List<String> getDependencies(){
        return dependencies;
    }

    public String getId(){
        return id;
    }

    public Object getObject() {
        return object;
    }

    public Method getMethod() {
        return method;
    }

    public int getPriority() {
        return priority;
    }

    @Override
    public int compareTo(Listener other) {
        return Integer.compare(other.priority, priority);
    }

}
