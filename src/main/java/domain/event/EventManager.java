package domain.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventManager {
    private Map<Class<? extends Event>, List<EventListener>> listeners;

    private EventManager() {
        listeners = new HashMap<>();
    }

    public Map<Class<? extends Event>, List<EventListener>> getListeners() {
        return listeners;
    }

    public void subscribe(Class<? extends Event> eventClass, EventListener listener) {
        listeners.putIfAbsent(eventClass, new ArrayList<>());
        List<EventListener> eventListeners = listeners.get(eventClass);
        eventListeners.add(listener);
    }

    public void unsubscribe(Class<? extends Event> eventClass, EventListener listener) {
        listeners.computeIfPresent(eventClass, (eClass, listeners) -> {
            listeners.remove(listener);
            return listeners;
        });
    }

    public <T extends Event> void publish(T event) {
        listeners.get(event.getClass())
                .forEach(listener ->
                        listener.notify(event));
    }

    private static EventManager eventManager = new EventManager();

    public static EventManager getInstance(){
        return eventManager;
    }
}
