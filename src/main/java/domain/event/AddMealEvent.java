package domain.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
@Getter
@AllArgsConstructor
public class AddMealEvent extends Event{
    private LocalDate date;
}
