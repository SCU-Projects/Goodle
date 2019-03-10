package katchup.DistributedCalendar.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Event {
    private String meetingId;
    private String subject;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private String venue;
    private String host;
}