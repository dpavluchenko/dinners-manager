package com.pavliuchenko.domain.event;

import com.pavliuchenko.infrastructure.annotation.Singleton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class EventManager {
    private Map<Class<? extends Event>, List<EventListener>> listeners = new HashMap<>();

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
}
