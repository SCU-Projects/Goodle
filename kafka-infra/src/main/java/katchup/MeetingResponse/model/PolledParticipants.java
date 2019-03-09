package katchup.MeetingResponse.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PolledParticipants {
    String userName;
    LocalDateTime alternativeStartDateTime;
    LocalDateTime alternativeEndDateTime;
}
