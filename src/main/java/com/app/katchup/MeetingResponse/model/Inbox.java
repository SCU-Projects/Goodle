package com.app.katchup.MeetingResponse.model;

import com.app.katchup.Meeting.model.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Inbox {

    private String meetingId;

    private String subject;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

    private String venue;

    private String host;

    private Status status;

    private int seats;

    private String password;
}
