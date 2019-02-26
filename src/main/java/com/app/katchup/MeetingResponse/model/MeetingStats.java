package com.app.katchup.MeetingResponse.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MeetingStats {
    String  meetingId;
    Integer totalSeats;
    Integer seatsOccupied;
    Integer seatsAvailable;
    Integer totalResponses;
    InviteesResponse inviteesResponse;
}
