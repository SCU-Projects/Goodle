package com.app.katchup.MeetingResponse;

import com.app.katchup.MeetingResponse.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MeetingResponseService {
    @Autowired
    MeetingResponseRepository meetingResponseRepo;

    public List<Inbox> getInboxForUserName(String userName){
        return meetingResponseRepo.findInboxByUserName(userName);
    }

    public MeetingInboxResponse postInboxForUserName(MeetingInboxResponse meetingInboxResponse){
        meetingResponseRepo.save(meetingInboxResponse);
        return meetingInboxResponse;
    }

 //update decision for client
   public Decision putResponseForUserDecision(MeetingRequestBody meetingUserResponse){
        MeetingInboxResponse meetingResponse = meetingResponseRepo.findbyUserNameAndMeetingID(meetingUserResponse.getUserName(),
                meetingUserResponse.getMeetingId());
        if(meetingUserResponse == null)
            return null;
        meetingResponse.setDecision(meetingUserResponse.getDecision());
        meetingResponseRepo.save(meetingResponse);
        return meetingUserResponse.getDecision();

   }

   public MeetingInboxResponse getResponseForUserMeeting(String userName, String meetingId){
        MeetingInboxResponse meetingResponse = meetingResponseRepo.findbyUserNameAndMeetingID(userName, meetingId);
        return meetingResponse;
   }

    public MeetingStats getStatsForMeetingId(String meetingId) {
        List<MeetingInboxResponse> meetingInboxResponseList = meetingResponseRepo.findAllbyMeetingID(meetingId);
        List<String> acceptedInvitees = getUserNameFromMeetingResponses(Decision.ACCEPT, meetingInboxResponseList);
        List<String> declinedInvitees = getUserNameFromMeetingResponses(Decision.DECLINE, meetingInboxResponseList);
        List<String> polledInvitees = getUserNameFromMeetingResponses(Decision.POLL, meetingInboxResponseList);
        List<String> goWithMajorityInvitees = getUserNameFromMeetingResponses(Decision.POLL, meetingInboxResponseList);

        MeetingStats meetingStats = new MeetingStats();
        meetingStats.setMeetingId(meetingId);
        meetingStats.setTotalResponses(meetingInboxResponseList.size());

        if(acceptedInvitees.size() > declinedInvitees.size())
            meetingStats.setSeatsOccupied(acceptedInvitees.size() + goWithMajorityInvitees.size());
        else
            meetingStats.setSeatsOccupied(acceptedInvitees.size());

        InviteesResponse inviteesResponse = new InviteesResponse();
        inviteesResponse.setAccept(acceptedInvitees);
        inviteesResponse.setDecline(declinedInvitees);
        inviteesResponse.setPoll(polledInvitees);
        inviteesResponse.setGoWithMajority(goWithMajorityInvitees);
        meetingStats.setInviteesResponse(inviteesResponse);
        return meetingStats;
    }

    private List<String> getUserNameFromMeetingResponses(Decision decision, List<MeetingInboxResponse> meetingInboxResponseList){
        List<String> results = new ArrayList<>();
        List<String> filteredInvitees = meetingInboxResponseList.stream()
                .filter(response -> response.getDecision() == decision)
                .map(MeetingInboxResponse::getUserName)
                .collect(Collectors.toList());
        return filteredInvitees;
    }
}
