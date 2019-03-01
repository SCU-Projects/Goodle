package com.app.katchup.MeetingResponse;

import com.app.katchup.Exception.NotAcceptableException;
import com.app.katchup.Exception.NotFoundException;
import com.app.katchup.Exception.UnAuthorizedException;
import com.app.katchup.Meeting.MeetingRepository;
import com.app.katchup.Meeting.MeetingService;
import com.app.katchup.Meeting.model.Meeting;
import com.app.katchup.Meeting.model.Status;
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
    @Autowired
    MeetingRepository meetingRepo;
    @Autowired
    MeetingService meetingService;

    public List<Inbox> getInboxForUserName(String userName){

        List<MeetingID> meetingIdList = meetingResponseRepo.findAllMeetingIdsbyUserName(userName);
        List<Meeting> meetingDetailsList = new ArrayList<>();

        if(meetingIdList.size() > 0){
            List<String> meetingIdsList = meetingIdList.stream().map(meetingID -> meetingID.getMeetingId()).collect(Collectors.toList());
            meetingDetailsList = meetingService.getMeetingDetailsForMeetingIds(meetingIdsList);
        }

        List<Inbox> inboxList = meetingDetailsList.stream().map(meeting -> {
                Inbox inbox = new Inbox();
                inbox.setMeetingId(meeting.getMeetingId());
                inbox.setSubject(meeting.getSubject());
                inbox.setVenue(meeting.getVenue());
                inbox.setHost(meeting.getHost());
                inbox.setStartDateTime(meeting.getStartDateTime());
                inbox.setStatus(meeting.getStatus());
                inbox.setEndDateTime(meeting.getEndDateTime());
                inbox.setPassword(meeting.getPassword());
                if (meeting.getSeats() == -1)
                    inbox.setSeats(1000);
                else
                    inbox.setSeats(meeting.getSeats());
                return inbox;
        }).collect(Collectors.toList());

        return inboxList;
    }

    public MeetingInboxResponse postInboxForUserName(MeetingInboxResponse meetingInboxResponse){
        meetingResponseRepo.save(meetingInboxResponse);
        return meetingInboxResponse;
    }

   public Decision putResponseForMeeting(boolean isExternalParticipant, Meeting meeting, String userName, MeetingRequestBody requestBody)
           throws  NotFoundException, NotAcceptableException {

        MeetingInboxResponse meetingResponse;

        if(!isExternalParticipant)
            meetingResponse = meetingResponseRepo.findByUserNameAndMeetingID(userName, meeting.getMeetingId());
        else
            meetingResponse = this.createExternalMeetingInboxResponse(meeting, userName);

        if(meetingResponse == null)
            throw new UnAuthorizedException("Sorry! You are not authorized to respond for this meeting");

        if(meeting.getSeats() == 0){
            throw new NotFoundException("Sorry! No seats available");
        }

        meetingResponse.setDecision(requestBody.getDecision());

        if(requestBody.getDecision() == Decision.ACCEPT) {

            if(meeting.getSeats() != -1){
                //if first time responding or previously declined
                if(meetingResponse.getDecision() != Decision.ACCEPT)
                    meeting.setSeats(meeting.getSeats() - 1);
                if(meeting.getSeats() == 0)
                    meeting.setStatus(Status.CLOSED);
            }
        }
        else if(requestBody.getDecision() == Decision.DECLINE){
            if(meeting.getSeats() != -1){

                //if previously accepted
                if(meetingResponse.getDecision() == Decision.ACCEPT)
                    meeting.setSeats(meeting.getSeats() + 1);

                if(meeting.getSeats() > 0)
                    meeting.setStatus(Status.OPEN);
            }
        }
        else if (requestBody.getDecision() == Decision.POLL) {
            if (!meeting.isPollAllowed())
                throw new NotAcceptableException("Sorry! The meeting host didn't enable the 'Poll' option");
            meetingResponse.setAlternativeStartDateTime(requestBody.getStartDateTime());
            meetingResponse.setAlternativeEndDateTime(requestBody.getEndDateTime());
        }
        else if (requestBody.getDecision() == Decision.GO_WITH_MAJORITY) {
            if (!meeting.isGoWithMajorityAllowed())
                throw new NotAcceptableException("Sorry! The meeting host didn't enable the 'Go With Majority' option");
        }

        meetingResponseRepo.save(meetingResponse);

        if(isExternalParticipant){
            //add new ext participant to the external participant list in Meeting
            List<String> externalParticipantsList = meeting.getExtParticipantList();
            externalParticipantsList.add(userName);
            meeting.setExtParticipantList(externalParticipantsList);
            meetingRepo.save(meeting);
        }

        return requestBody.getDecision();
   }

    private MeetingInboxResponse createExternalMeetingInboxResponse(Meeting meeting, String userName) {
        MeetingInboxResponse meetingResponse = new MeetingInboxResponse();
        meetingResponse.setUserName(userName);
        meetingResponse.setMeetingId(meeting.getMeetingId());
        return meetingResponse;
    }

    public MeetingInboxResponse getResponseForMeeting(String userName, String meetingId){
        MeetingInboxResponse meetingResponse = meetingResponseRepo.findByUserNameAndMeetingID(userName, meetingId);
        return meetingResponse;
   }

    public MeetingStats getStatsForMeeting(Meeting meeting) {
        List<MeetingInboxResponse> meetingInboxResponseList = meetingResponseRepo.findAllbyMeetingID(meeting.getMeetingId());
        List<String> acceptedInvitees = getUserNameFromMeetingResponses(Decision.ACCEPT, meetingInboxResponseList);
        List<String> declinedInvitees = getUserNameFromMeetingResponses(Decision.DECLINE, meetingInboxResponseList);
        List<PolledParticipants> polledInvitees = getPolledParticipantsFromMeetingResponses(meetingInboxResponseList);
        List<String> goWithMajorityInvitees = getUserNameFromMeetingResponses(Decision.GO_WITH_MAJORITY, meetingInboxResponseList);

        MeetingStats meetingStats = new MeetingStats();
        meetingStats.setMeetingId(meeting.getMeetingId());
        int seatsAvailable = meeting.getSeats() == -1 ? 1000 : meeting.getSeats();
        meetingStats.setSeatsAvailable(seatsAvailable);
        meetingStats.setTotalResponses(meetingInboxResponseList.size());

        if(acceptedInvitees.size() > declinedInvitees.size())
            meetingStats.setSeatsOccupied(acceptedInvitees.size() + goWithMajorityInvitees.size());
        else
            meetingStats.setSeatsOccupied(acceptedInvitees.size());

        meetingStats.setTotalSeats(seatsAvailable + meetingStats.getSeatsOccupied());
        InviteesResponse inviteesResponse = new InviteesResponse();
        inviteesResponse.setAccepted(acceptedInvitees);
        inviteesResponse.setDeclined(declinedInvitees);
        inviteesResponse.setPolled(polledInvitees);
        inviteesResponse.setGoneWithMajority(goWithMajorityInvitees);
        meetingStats.setInviteesResponse(inviteesResponse);
        return meetingStats;
    }

    private List<String> getUserNameFromMeetingResponses(Decision decision, List<MeetingInboxResponse> meetingInboxResponseList){
        List<String> filteredInvitees = meetingInboxResponseList.stream()
                .filter(response -> response.getDecision() == decision)
                .map(MeetingInboxResponse::getUserName)
                .collect(Collectors.toList());
        return filteredInvitees;
    }

    private List<PolledParticipants> getPolledParticipantsFromMeetingResponses(List<MeetingInboxResponse> meetingInboxResponseList) {
        List<PolledParticipants> polledParticipantsList = meetingInboxResponseList.stream()
                .filter(response -> response.getDecision() == Decision.POLL)
                .map(response -> {
                    PolledParticipants participant = new PolledParticipants();
                    participant.setUserName(response.getUserName());
                    participant.setAlternativeStartDateTime(response.getAlternativeStartDateTime());
                    participant.setAlternativeEndDateTime(response.getAlternativeEndDateTime());
                    return participant;
                })
                .collect(Collectors.toList());
        return polledParticipantsList;
    }
}
