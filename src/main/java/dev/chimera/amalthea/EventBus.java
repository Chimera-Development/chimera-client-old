package dev.chimera.amalthea;

import dev.chimera.amalthea.events.AbstractEvent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class EventBus implements EventBusInterface{
    /*TODO separate tagged and untagged listeners for speed (abolish taggedType)
      TODO Add Object and method map to also make it faster (instead of compound Listener type
      TODO Also maybe come up with a better way of registering listeners and change the way exceptions are thrown
    */
    public HashMap<TaggedType, List<Listener>> listeners = new HashMap<>();



    public class TaggedType {
        public final String tag;
        public final Class<?> klass;

        public TaggedType(String tag, Class<?> klass) {
            this.tag = tag;
            this.klass = klass;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TaggedType that = (TaggedType) o;
            return Objects.equals(tag, that.tag) &&
                    Objects.equals(klass, that.klass);
        }

        @Override
        public int hashCode() {
            return Objects.hash(tag, klass);
        }
    }



    public void registerListenersInClass(Object object) throws IllegalAccessException {
        for (Method method : object.getClass().getMethods()) {
            if (method.isAnnotationPresent(EventListener.class)) {
                    register(method, object);
            }
        }
    }

    public void register(Method listener, Object object){
        Class<?>[] parameterTypes = listener.getParameterTypes();
        if (parameterTypes.length != 1) {
            throw new IllegalArgumentException("Invalid listener method: " + listener);
        }
        if (!listener.trySetAccessible()){
                throw new RuntimeException("Invalid listener method, illegal access: " + listener);
        }

        EventListener annotation = listener.getAnnotation(EventListener.class);
        String tag = annotation.tag();
        Class<?> klass = parameterTypes[0];
        TaggedType taggedType = new TaggedType(tag, klass);
        if (!listeners.containsKey(taggedType)) {
            listeners.put(taggedType, new ArrayList<>());
        }
        this.listeners.get(taggedType).add(new Listener(object, listener));
    }

    @Override
    public <T> void post(T event) {
        TaggedType taggedType = new TaggedType("", event.getClass());
        List<Listener> methods = listeners.get(taggedType);
        if (methods != null) {
            for (Listener listener : methods) {
                try {
                    listener.listenerMethod.invoke(listener.listenerObject, event);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }



    @Override
    public <T> void post(String tag, T event) {
        TaggedType taggedType = new TaggedType(tag, event.getClass());
        List<Listener> methods = listeners.get(taggedType);
        if (methods != null) {
            for (Listener listener : methods) {
                try {
                    listener.listenerMethod.invoke(listener.listenerObject, event);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

}
