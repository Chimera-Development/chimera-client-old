package dev.chimera.client.eventsystem;


import dev.chimera.client.ChimeraClient;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class EventDispatcher {
    private static Map<Class<? extends AbstractEvent>, List<Listener>> eventListeners = new HashMap<>();

    private static Map<String, Listener> listenerIDs = new HashMap<>();

    private static Map<String, List<String>> dependencyBuffer = new HashMap<>();

    private static Map<Class<? extends AbstractEvent>, Boolean> haveListenersChanged = new HashMap<>();


    /*
        To register listeners in a class, usually, you would add registerListenersInClass(this) to the constructor.
        However, you can also use this with any object, in case you want to only register the eventListeners for specific instances.
     */

    public static void registerListenersInObject(Object listenerContainer) {
        for (Method method : listenerContainer.getClass().getDeclaredMethods()) {
            if (!method.isAnnotationPresent(EventListener.class)) continue;
            Class<? extends AbstractEvent> eventType;

            //Make sure that the listener is correctly configured and accessible
            {
                try {
                    eventType = method.getParameterTypes()[0].asSubclass(AbstractEvent.class);
                } catch (ClassCastException e) {
                    throw new IllegalArgumentException("Invalid parameter: should be subclass of AbstractEvent: " + method);
                }

                if (method.getParameterCount() != 1)
                    throw new IllegalArgumentException("Invalid listener method: " + method);
                if (!method.trySetAccessible())
                    throw new RuntimeException("Invalid listener method, illegal access: " + method);
            }

            EventListener eventListener = method.getAnnotation(EventListener.class);

            String id = listenerContainer.getClass().getName() + ":" + method.getName() + ":" + eventType.getName();

            ChimeraClient.LOG.info("Formed ID: " + id);

            List<String> dependencies = new ArrayList<>(List.of(eventListener.runAfter()));

            //Add runBefore dependencies
            {

                for (String runBeforeId : eventListener.runBefore()) {

                    listenerIDs.computeIfPresent(id, (k, v) -> {
                        v.addDependency(id);
                        return v;
                    });

                    dependencyBuffer.computeIfAbsent(id, k -> {
                        List<String> tempList = new ArrayList<>();
                        tempList.add(id);
                        return tempList;
                    });

                }

                if (dependencyBuffer.containsKey(id)) {
                    dependencies.addAll(dependencyBuffer.get(id));
                    dependencyBuffer.remove(id);
                }
            }

            Listener listener = new Listener(listenerContainer, method, id, dependencies);
            listenerIDs.put(id, listener);

            List<Listener> listeners = eventListeners.computeIfAbsent(eventType, k -> new ArrayList<>());
            listeners.add(listener);

            //used to only sort the listeners once they have changed
            haveListenersChanged.put(eventType, true);
        }
    }


    public static class PrioritySystem {
        private static class ListenerNode {
            Listener listener;
            List<String> runAfter;

            public List<String> getRunAfter() {
                return runAfter;
            }

            public ListenerNode(Listener listener, List<String> runAfter) {
                this.listener = listener;
                this.runAfter = runAfter;
            }

        }

        static ArrayDeque<ListenerNode> queue = new ArrayDeque<>();

        public static List<Listener> topologicalSort(List<Listener> listeners) {
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
    }

    //Will return null if no listeners exist
    public static <T extends AbstractEvent> List<Listener> sortMap(T event) {

        if (!haveListenersChanged.containsKey(event.getClass()))
            return null;


        if (haveListenersChanged.get(event.getClass())) {
            haveListenersChanged.put(event.getClass(), false);
            return eventListeners.computeIfPresent(event.getClass(), (k, v) -> PrioritySystem.topologicalSort(v));
        }

        return eventListeners.get(event.getClass());
    }

    public static Listener findListenerById(String id) {
        return listenerIDs.computeIfAbsent(id, (k) -> {
            throw new IllegalArgumentException("No listener found with id: " + id);
        });
    }

    public static <T extends AbstractEvent> void postEvent(T event) {
        List<Listener> listeners = sortMap(event);
        if (listeners != null) {
            for (Listener listener : listeners) {
                try {
                    if (event.isCancelled) {
                        // Do not send event to additional listeners when cancelled. (since its easy to miss and cause bugs)
                        break;
                    }
                    listener.invoke(event);
                } catch (InvocationTargetException | IllegalAccessException e) {
                    ChimeraClient.LOG.error("Posting event \"" + event.getClass() + "\" failed to listener \"" + listener.getId() + "\"");
                    e.printStackTrace();
                }
            }
            ;
        } else {
//            ChimeraClient.LOG.error("No listeners exist for this event!" + event.getClass().getName());
        }
    }

}
