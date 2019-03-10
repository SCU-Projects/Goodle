package katchup.MeetingResponse.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
