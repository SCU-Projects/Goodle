package katchup.MeetingResponse.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InviteesResponse {
    List<String> accepted;
    List<String> declined;
    List<PolledParticipants> polled;
    List<String> goneWithMajority;
}
