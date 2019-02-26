package com.app.katchup.Distributed_Calendar;

import com.app.katchup.MeetingResponse.model.Decision;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DistributedCalendar {

    private String userName;
    private String fullName;
    String venue;
    private String email;
    private String meetingId;
    private String subject;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private String host;
    //    private Status status;
    private Decision response;
}