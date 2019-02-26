package com.app.katchup.Meeting.model;


import com.app.katchup.MeetingResponse.model.Decision;
import com.app.katchup.Users.UserConstants;
import org.springframework.data.annotation.Id;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.ws.Response;
import java.time.LocalDateTime;
import java.util.List;

public class HostMeeting {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String meetingId;
    @GeneratedValue(strategy = GenerationType.AUTO)
    private  String password;
    @NotNull(message = UserConstants.dateTimeNullMessage)
    @NotEmpty
    private LocalDateTime startDateTime;
    @NotNull(message = UserConstants.dateTimeNullMessage)
    @NotEmpty
    private LocalDateTime endDateTime;
    @NotNull(message = UserConstants.toListNullMessage)
    List<String> inviteList;
    @NotNull(message = UserConstants.venueNullMessage)
    @NotEmpty
    String Venue;
    @NotNull(message = UserConstants.responseNullMessage)
    @NotEmpty
    private Decision response;
    @NotNull(message = UserConstants.durationNullMessage)
    @NotEmpty
    int duration;
    @NotNull(message = UserConstants.userNameNullMessage)
    @Size(min = UserConstants.userNameMinLength, max = UserConstants.userNameMaxLength)
    private String host;
    @NotNull(message = UserConstants.pollNullMessage)
    private Decision poll;
    int Seats;










}
