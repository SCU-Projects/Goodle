package katchup.DistributedCalendar.model;

import lombok.Data;

import java.util.List;

@Data
public class DistributedCalendar {
    List<Event> eventList;
}