package com.pavliuchenko.domain.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class RemoveMenuEvent extends Event{
    private LocalDate date;
}
