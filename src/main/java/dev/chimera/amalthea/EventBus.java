package dev.chimera.amalthea;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EventBus implements EventBusInterface {

    /*
      TODO Add Object and method map to also make it faster (instead of compound Listener type
      TODO Also maybe come up with a better way of registering listeners and change the way exceptions are thrown
      TODO Also make listener registration work in a similar way to mixin registration
    */

    public HashMap<TaggedType, List<Method>> taggedListeners = new HashMap<>();
    public HashMap<Class<?>, List<Method>> listeners = new HashMap<>();

    public HashMap<Method, Object> methodObjectHashMap = new HashMap<>();

    public record TaggedType(String tag, Class<?> klass) {
    }


    public void registerListenersInClass(Object object) throws IllegalAccessException {
        for (Method method : object.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(EventListener.class)) {

                String tag = method.getAnnotation(EventListener.class).tag();
                if(tag.equals("")){
                    registerListener(method);
                }else{
                    registerTaggedListener(method, tag);
                }

                this.methodObjectHashMap.put(method, object);
            }
        }

    }

    public void registerTaggedListener(Method listener, String tag){
        Class<?>[] parameterTypes = listener.getParameterTypes();
        if (parameterTypes.length != 1) {
            throw new IllegalArgumentException("Invalid listener method: " + listener);
        }
        if (!listener.trySetAccessible()) {
            throw new RuntimeException("Invalid listener method, illegal access: " + listener);
        }

        Class<?> klass = parameterTypes[0];

        TaggedType taggedType = new TaggedType(tag, klass);
        if (!taggedListeners.containsKey(taggedType)) {
            taggedListeners.put(taggedType, new ArrayList<>());
        }
        this.taggedListeners.get(taggedType).add(listener);
    }

    public void registerListener(Method listener) {
        Class<?>[] parameterTypes = listener.getParameterTypes();
        if (parameterTypes.length != 1) {
            throw new IllegalArgumentException("Invalid listener method: " + listener);
        }
        if (!listener.trySetAccessible()) {
            throw new RuntimeException("Invalid listener method, illegal access: " + listener);
        }

        Class<?> klass = parameterTypes[0];

        if (!listeners.containsKey(klass)) {
            listeners.put(klass, new ArrayList<>());
        }
        this.listeners.get(klass).add(listener);
    }

    @Override
    public <T> void post(T event) {
        List<Method> methods = listeners.get(event.getClass());
        if (methods != null) {
            for (Method listener : methods) {
                try {
                    listener.invoke(methodObjectHashMap.get(listener), event);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Override
    public <T> void post(String tag, T event) {
        TaggedType taggedType = new TaggedType(tag, event.getClass());
        List<Method> methods = taggedListeners.get(taggedType);
        if (methods != null) {
            for (Method listener : methods) {
                try {
                    listener.invoke(methodObjectHashMap.get(listener), event);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

}
