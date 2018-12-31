package domain.event;

public interface EventListener<T extends Event> {
    void notify(T event);
}
