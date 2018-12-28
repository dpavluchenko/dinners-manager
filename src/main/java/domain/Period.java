package domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
@Getter
@AllArgsConstructor
public class Period {
    private LocalDate startDate;
    private LocalDate endDate;
}
