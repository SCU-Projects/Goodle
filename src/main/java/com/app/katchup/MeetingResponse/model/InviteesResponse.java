package com.app.katchup.MeetingResponse.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InviteesResponse {
    List<String> accept;
    List<String> decline;
    List<PolledParticipants> poll;
    List<String> goWithMajority;
}
