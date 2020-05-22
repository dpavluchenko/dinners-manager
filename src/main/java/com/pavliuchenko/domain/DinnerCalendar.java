package com.pavliuchenko.domain;

import com.pavliuchenko.dao.client.DayMenuDataMapper;
import com.pavliuchenko.domain.event.AddMenuEvent;
import com.pavliuchenko.domain.event.EventListener;
import com.pavliuchenko.domain.event.EventManager;
import com.pavliuchenko.domain.event.RemoveMenuEvent;
import com.pavliuchenko.infrastructure.annotation.InjectType;
import com.pavliuchenko.infrastructure.annotation.Singleton;

import javax.annotation.PostConstruct;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.WeekFields;
import java.util.Calendar;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Singleton
public class DinnerCalendar {

    private final ScheduledExecutorService scheduledTask = Executors.newSingleThreadScheduledExecutor();
    private final Map<LocalDate, LocalDate> dateStorage = new ConcurrentHashMap<>();
    private final WeekFields week = WeekFields.ISO;
    private LocalDate maxDate;

    @InjectType
    private EventManager eventManager;
    @InjectType
    private DayMenuDataMapper mapper;

    @PostConstruct
    private void init(){
        initDates();
        subscribeToEvents();
        runClearDateStorage();
    }

    public Period findMenuPeriod() {
        if (maxDate.get(week.weekOfWeekBasedYear()) > LocalDate.now().get(week.weekOfWeekBasedYear())) {
            return new Period(LocalDate.now(), maxDate);
        }
        return new Period(findFirstDayOfWeekDate(), maxDate);
    }

    private void addMealDate(LocalDate date) {
        dateStorage.put(date, date);
        if (date.isAfter(maxDate)) maxDate = date;
    }

    private void removeMealDate(LocalDate date) {
        dateStorage.remove(date);
        if (date.isEqual(maxDate)) {
            if (!dateStorage.isEmpty()) maxDate = Collections.max(dateStorage.keySet());
            else initDates();
        }
    }

    private LocalDate findFirstDayOfWeekDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return cal.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private void initDates() {
        LocalDate max = mapper.findMaxMealDate();
        this.maxDate = max != null ? max : LocalDate.now();
        this.dateStorage.put(maxDate, maxDate);
    }

    private void runClearDateStorage() {
        int delay = DayOfWeek.values().length;
        scheduledTask.scheduleWithFixedDelay(() -> {
                    LocalDate now = LocalDate.now();
                    dateStorage.entrySet().removeIf(entry -> {
                        LocalDate date = entry.getKey();
                        return date.isBefore(now) &&
                                date.get(week.weekOfWeekBasedYear()) != now.get(week.weekOfWeekBasedYear());
                    });
                }, delay,
                delay,
                TimeUnit.DAYS);
    }

    private void subscribeToEvents() {
        EventListener<AddMenuEvent> addMealListener = event -> addMealDate(event.getDate());
        EventListener<RemoveMenuEvent> removeMealListener = event -> removeMealDate(event.getDate());
        eventManager.subscribe(AddMenuEvent.class, addMealListener);
        eventManager.subscribe(RemoveMenuEvent.class, removeMealListener);
    }
}
