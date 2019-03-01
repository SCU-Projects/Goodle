package com.app.katchup.Distributed_Calendar.model;

import lombok.Data;

import java.util.List;

@Data
public class DistributedCalendar {
    List<Event> eventList;
}