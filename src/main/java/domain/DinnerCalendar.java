package domain;

import dao.client.DayMenuDataMapper;
import dao.client.SimpleDataMapperFactory;
import domain.event.AddMenuEvent;
import domain.event.EventListener;
import domain.event.EventManager;
import domain.event.RemoveMenuEvent;

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

public class DinnerCalendar {

    private final ScheduledExecutorService scheduledTask;
    private final Map<LocalDate, LocalDate> dateStorage;
    private final WeekFields week;
    private LocalDate maxDate;

    private DinnerCalendar() {
        this.dateStorage = new ConcurrentHashMap<>();
        this.week = WeekFields.ISO;
        this.scheduledTask = Executors.newSingleThreadScheduledExecutor();
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

    private void initDates() {
        DayMenuDataMapper mapper = SimpleDataMapperFactory.getDataMapperFor(DayMenuDataMapper.class);
        LocalDate max = mapper.findMaxMealDate();
        this.maxDate = max != null ? max : LocalDate.now();
        this.dateStorage.put(maxDate, maxDate);
    }

    private LocalDate findFirstDayOfWeekDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return cal.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
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
        EventManager eventManager = EventManager.getInstance();
        EventListener<AddMenuEvent> addMealListener = event -> addMealDate(event.getDate());
        EventListener<RemoveMenuEvent> removeMealListener = event -> removeMealDate(event.getDate());
        eventManager.subscribe(AddMenuEvent.class, addMealListener);
        eventManager.subscribe(RemoveMenuEvent.class, removeMealListener);
    }

    static {
        dinnerCalendar = new DinnerCalendar();
    }

    private static final DinnerCalendar dinnerCalendar;

    public static DinnerCalendar getInstance() {
        return dinnerCalendar;
    }
}
