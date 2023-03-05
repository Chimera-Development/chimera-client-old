package dev.chimera.amalthea;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;




public class EventBus {

    /*
      TODO Actual make the tag system again
    */

    private final HashMap<Class<?>, List<Listener>> listenersByEventType = new HashMap<>();
    private static final HashMap<String, Listener> listenerIDs = new HashMap<>();

    private boolean listenersChanged = true;

    public void registerListenersInClass(Object object){
        for (Method method : object.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(EventListener.class)) {

                Class<?>[] parameterTypes = method.getParameterTypes();
                if (parameterTypes.length != 1) {
                    throw new IllegalArgumentException("Invalid listener method: " + method);
                }
                if (!method.trySetAccessible()) {
                    throw new RuntimeException("Invalid listener method, illegal access: " + method);
                }
                EventListener eventListener = method.getAnnotation(EventListener.class);
                Listener listener = new Listener(object, method, eventListener.priority(), eventListener.id(), Arrays.stream(eventListener.dependencies()).toList());
                registerListener(listener, parameterTypes[0]);
            }
        }
    }

    private <T> void registerListener(Listener listener, Class<T> eventType) {
        List<Listener> listeners = listenersByEventType.computeIfAbsent(eventType, k -> new ArrayList<>());
        listenerIDs.put(listener.getId(), listener);
        //todo
        listeners.add(listener);
        listenersChanged = true;
    }



    public static class PrioritySystem {
        private static class ListenerNode {
            Listener listener;
            List<String> dependencies;
            // constructor, getters and setters omitted for brevity


            public List<String> getDependencies() {
                return dependencies;
            }

            public ListenerNode(Listener listener, List<String> dependencies) {
                this.listener = listener;
                this.dependencies = dependencies;
            }

        }



        public static List<Listener> topologicalSort(List<Listener> listeners) {
            // Create a map of listener nodes, where each listener node
            // represents a listener and its dependencies
            Map<Listener, ListenerNode> nodeMap = new HashMap<>();
            for (Listener listener : listeners) {
                ListenerNode node = new ListenerNode(listener, listener.getDependencies());
                nodeMap.put(listener, node);
            }

            // Create a map of nodes and their incoming edges
            Map<ListenerNode, Integer> incomingEdges = new HashMap<>();
            for (ListenerNode node : nodeMap.values()) {
                incomingEdges.put(node, 0);
            }
            for (ListenerNode node : nodeMap.values()) {
                for (String dependency : node.getDependencies()) {
                    ListenerNode dependentNode = nodeMap.get(findListenerById(dependency));
                    incomingEdges.put(dependentNode, incomingEdges.get(dependentNode) + 1);
                }
            }

            // Initialize the queue with nodes that have no incoming edges
            Queue<ListenerNode> queue = new LinkedList<>();
            for (ListenerNode node : incomingEdges.keySet()) {
                if (incomingEdges.get(node) == 0) {
                    queue.offer(node);
                }
            }

            // Perform the topological sort
            List<Listener> sortedListeners = new ArrayList<>();
            while (!queue.isEmpty()) {
                ListenerNode node = queue.poll();
                sortedListeners.add(node.listener);
                for (String dependency : node.dependencies) {
                    ListenerNode dependentNode = nodeMap.get(findListenerById(dependency));
                    incomingEdges.put(dependentNode, incomingEdges.get(dependentNode) - 1);
                    if (incomingEdges.get(dependentNode) == 0) {
                        queue.offer(dependentNode);
                    }
                }
            }

            // Check for cycles
            if (sortedListeners.size() != listeners.size()) {
                throw new IllegalArgumentException("Graph contains a cycle");
            }
            Collections.reverse(sortedListeners);
            return sortedListeners;
        }

        public static Listener findListenerById(String id) {
            return listenerIDs.computeIfAbsent(id, (k) -> {
                throw new IllegalArgumentException("No listener found with id: " + id);
            });
        }
    }



    public <T> void postEvent(T event) throws InvocationTargetException, IllegalAccessException {
        List<Listener> listeners;
        if(!listenersByEventType.containsKey(event.getClass()))
        {
            // No listeners for event
            return;
        }
        if(listenersChanged){
            listeners = PrioritySystem.topologicalSort(listenersByEventType.get(event.getClass()));
            listenersChanged = false;
        }else{
            listeners = listenersByEventType.get(event.getClass());
        }
        for (Listener listener : listeners) {
                listener.invoke(event);
        }
    }

    public <T> void postEventToListener(T event, String id) throws InvocationTargetException, IllegalAccessException {
        Listener listener = listenerIDs.get(id);
        if(listener.getMethod().getParameterTypes()[0] == event.getClass()){
            listener.invoke(event);
        }
    }
}
