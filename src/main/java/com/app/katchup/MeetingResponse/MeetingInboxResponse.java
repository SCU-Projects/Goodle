package com.app.katchup.MeetingResponse;

import com.app.katchup.Users.UserConstants;
import org.springframework.data.annotation.Id;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

public class MeetingInboxResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String responseId;
    @NotNull(message = UserConstants.userNameNullMessage)
    @Size(min = UserConstants.userNameMinLength, max = UserConstants.userNameMaxLength)
    private String userName;
    @NotEmpty
    private String meetingId;
    @NotEmpty
    private Decision decision;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

}

