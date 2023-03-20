package dev.chimera.amalthea.eventbus;

import dev.chimera.ChimeraClient;
import dev.chimera.amalthea.events.AbstractEvent;
import net.minecraft.client.MinecraftClient;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class EventBus {

    private ConcurrentHashMap<Class<?>, ArrayList<Listener>> listenersByEventType = new ConcurrentHashMap<>();

    private static ConcurrentHashMap<String, Listener> listenerIDs = new ConcurrentHashMap<>();

    private static ConcurrentHashMap<String, ArrayList<String>> dependencyBuffer = new ConcurrentHashMap<>();

    private ConcurrentHashMap<Class<?>, Boolean> updatedListeners = new ConcurrentHashMap<>();

    private boolean listenersChanged = true;

    public void registerListenersInClass(Object object) {
        for (Method method : object.getClass().getDeclaredMethods()) {
            if (!method.isAnnotationPresent(EventListener.class)) continue;
            //Make sure that the listener is correctly configured and accessible
            {
                if (method.getParameterCount() != 1)
                    throw new IllegalArgumentException("Invalid listener method: " + method);

                if (!method.trySetAccessible())
                    throw new RuntimeException("Invalid listener method, illegal access: " + method);
            }

            EventListener eventListener = method.getAnnotation(EventListener.class);
            String eventID = eventListener.id();
            LinkedList<String> dependencies = new LinkedList<>(List.of(eventListener.runAfter()));
            //Add runBefore dependencies
            {

                Arrays.stream(eventListener.runBefore()).forEach((id) -> {
                    if (listenerIDs.containsKey(id)) {
                        listenerIDs.compute(id, (k, v) -> {
                            assert v != null;
                            v.addDependency(eventID);
                            return v;
                        });
                    } else {
                        dependencyBuffer.computeIfAbsent(id, k -> new ArrayList<>());
                        dependencyBuffer.get(id).add(eventListener.id());
                    }
                });
                if (dependencyBuffer.containsKey(eventID)) {
                    ArrayList<String> bufferedDependencies = dependencyBuffer.get(eventID);
                    dependencies.addAll(bufferedDependencies);
                    dependencyBuffer.remove(eventID);
                }
            }

            Listener listener = new Listener(object, method, eventID, dependencies);

            listenerIDs.put(eventID, listener);

            ArrayList<Listener> listeners = listenersByEventType.computeIfAbsent(method.getParameterTypes()[0], k -> new ArrayList<>());
            listeners.add(listener);
            //used to only sort the listeners once they have changed
            updatedListeners.put(method.getParameterTypes()[0], true);
        }
    }

    public Set<String> getAllListenerIDs(){
        return listenerIDs.keySet();
    }

    public static class PrioritySystem {
        private static class ListenerNode {
            Listener listener;
            List<String> runAfter;
            // constructor, getters and setters omitted for brevity

            public List<String> getRunAfter() {
                return runAfter;
            }

            public ListenerNode(Listener listener, List<String> runAfter) {
                this.listener = listener;
                this.runAfter = runAfter;
            }

        }

        static ArrayDeque<ListenerNode> queue = new ArrayDeque<>();

        public static ArrayList<Listener> topologicalSort(ArrayList<Listener> listeners) {
            // Create a map of listener nodes, where each listener node
            // represents a listener and its dependencies
            Map<Listener, ListenerNode> nodeMap = new HashMap<>();
            for (Listener listener : listeners) {
                nodeMap.put(listener, new ListenerNode(listener, listener.getRunAfter()));
            }

            // Create a map of nodes and their incoming edges
            Map<ListenerNode, Integer> incomingEdges = new HashMap<>();
            for (ListenerNode node : nodeMap.values()) {
                incomingEdges.put(node, 0);
            }
            for (ListenerNode node : nodeMap.values()) {
                for (String dependency : node.getRunAfter()) {
                    ListenerNode dependentNode = nodeMap.get(findListenerById(dependency));
                    incomingEdges.put(dependentNode, incomingEdges.get(dependentNode) + 1);
                }
            }

            // Initialize the queue with nodes that have no incoming edges
            incomingEdges.forEach((node, value) -> {
                if (value == 0) {
                    queue.offer(node);
                }
            });

            // Perform the topological sort
            ArrayList<Listener> sortedListeners = new ArrayList<>();
            while (!queue.isEmpty()) {
                ListenerNode node = queue.poll();
                sortedListeners.add(node.listener);
                for (String dependency : node.runAfter) {
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

    public <T extends AbstractEvent> ArrayList<Listener> sortMap(T event) {
        if (updatedListeners.getOrDefault(event.getClass(), true)) {
            updatedListeners.put(event.getClass(), false);
            return listenersByEventType.computeIfPresent(event.getClass(), (k, v) -> PrioritySystem.topologicalSort(v));
        }

        return listenersByEventType.get(event.getClass());
    }

    public void postEvent(AbstractEvent event) {
        ArrayList<Listener> listeners = sortMap(event);
        if (listeners != null) {
            listeners.forEach((listener) -> {
                try {
                    if (event.isCancelled()) return;
                    listener.invoke(event);
                } catch (InvocationTargetException | IllegalAccessException e) {
                    ChimeraClient.LOGGER.error("Posting event \"" + event.getClass() + "\" failed to listener \"" + listener.getId() + "\"");
                    e.printStackTrace();
                }
            });
        }
    }

    public void postEventToListener(AbstractEvent event, String id) {
        Listener listener = listenerIDs.get(id);
        if (listener.getMethod().getParameterTypes()[0] == event.getClass()) {

            try {
                listener.invoke(event);
            } catch (InvocationTargetException | IllegalAccessException e) {
                ChimeraClient.LOGGER.error("Posting event \"" + event.getClass() + "\" failed to listener \"" + listener.getId() + "\"");
                e.printStackTrace();
            }
        }
    }


}
