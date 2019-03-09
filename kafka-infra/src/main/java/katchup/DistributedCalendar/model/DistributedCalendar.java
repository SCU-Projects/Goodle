package katchup.DistributedCalendar.model;

import katchup.DistributedCalendar.model.Event;
import lombok.Data;

import java.util.List;

@Data
public class DistributedCalendar {
    List<Event> eventList;
}