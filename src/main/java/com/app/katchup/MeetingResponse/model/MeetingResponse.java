package com.app.katchup.MeetingResponse.model;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Data
public class MeetingResponse {
    @NotEmpty
    private String userName;
    @NotEmpty
    private String userPassword;
    @NotEmpty
    private String meetingId;
    @NotEmpty
    private String meetingPassword;
    @NotEmpty
    Decision decision;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
}

