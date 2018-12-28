package domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Entity {
    protected Long id;

    public boolean isPresent() {
        return id != null;
    }
}
