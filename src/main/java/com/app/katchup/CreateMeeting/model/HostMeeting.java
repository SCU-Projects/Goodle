package com.app.katchup.CreateMeeting.model;


import com.app.katchup.Users.UserConstants;
import org.springframework.data.annotation.Id;
import sun.util.resources.Bundles;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

public class HostMeeting {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String meetingId;
    @NotNull(message = UserConstants.userNameNullMessage)
    @NotEmpty
    private Response decision;
    @NotNull(message = UserConstants.dateTimeNullMessage)
    @NotEmpty
    private LocalDateTime startDateTime;
    @NotNull(message = UserConstants.dateTimeNullMessage)
    @NotEmpty
    private LocalDateTime endDateTime;
    @NotNull(message = UserConstants.toListNullMessage)
    List<String> to;
    @NotNull(message = UserConstants.venueNullMessage)
    @NotEmpty
    String Venue;
    int Seats;









}
