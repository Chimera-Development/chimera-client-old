package dev.chimera.amalthea;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class EventBus{

    /*
      TODO Also maybe come up with a better way of registering listeners and change the way exceptions are thrown
    */

    public HashMap<TaggedType, List<PriorityMethod>> taggedListeners = new HashMap<>();
    public HashMap<Class<?>, List<PriorityMethod>> listeners = new HashMap<>();
    public HashMap<Method, Object> methodObjectHashMap = new HashMap<>();


    public record PriorityMethod(Method method, int priority){

    }

    public record TaggedType(String tag, Class<?> klass) {
    }

    public void registerListenersInClass(Object object){
        for (Method method : object.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(EventListener.class)) {
                EventListener eventListener = method.getAnnotation(EventListener.class);
                if (eventListener.tag().equals("")) {
                    registerListener(method, eventListener);
                } else {
                    registerTaggedListener(method, eventListener);
                }

                this.methodObjectHashMap.put(method, object);

            }
        }
    }

    public void registerTaggedListener(Method listener, EventListener eventListener) {
        Class<?>[] parameterTypes = listener.getParameterTypes();
        if (parameterTypes.length != 1) {
            throw new IllegalArgumentException("Invalid listener method: " + listener);
        }
        if (!listener.trySetAccessible()) {
            throw new RuntimeException("Invalid listener method, illegal access: " + listener);
        }

        Class<?> klass = parameterTypes[0];

        TaggedType taggedType = new TaggedType(eventListener.tag(), klass);
        PriorityMethod priorityMethod = new PriorityMethod(listener, eventListener.priority());
        if (!taggedListeners.containsKey(taggedType)) {
            taggedListeners.put(taggedType, new ArrayList<>());
        }
        this.taggedListeners.get(taggedType).add(priorityMethod);
    }
    public void registerListener(Method listener, EventListener eventListener) {
        Class<?>[] parameterTypes = listener.getParameterTypes();
        if (parameterTypes.length != 1) {
            throw new IllegalArgumentException("Invalid listener method: " + listener);
        }
        if (!listener.trySetAccessible()) {
            throw new RuntimeException("Invalid listener method, illegal access: " + listener);
        }

        Class<?> klass = parameterTypes[0];
        PriorityMethod priorityMethod = new PriorityMethod(listener, eventListener.priority());
        if (!listeners.containsKey(klass)) {
            listeners.put(klass, new ArrayList<>());
        }
        this.listeners.get(klass).add(priorityMethod);
    }
    public <T> void post(T event) {
        List<PriorityMethod> methods = listeners.get(event.getClass());
        Comparator<PriorityMethod> byPriority = Comparator.comparingInt(PriorityMethod::priority).reversed();
        if (methods != null) {
            methods.sort(byPriority);
            for (PriorityMethod listener : methods) {
                try {
                    listener.method.invoke(methodObjectHashMap.get(listener.method), event);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
    public <T> void post(String tag, T event) {
        TaggedType taggedType = new TaggedType(tag, event.getClass());
        List<PriorityMethod> methods = taggedListeners.get(taggedType);
        Comparator<PriorityMethod> byPriority = Comparator.comparingInt(PriorityMethod::priority).reversed();
        methods.sort(byPriority);
        for (PriorityMethod listener : methods) {
            try {
                listener.method.invoke(methodObjectHashMap.get(listener.method), event);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
