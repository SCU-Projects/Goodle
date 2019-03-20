package katchup.MeetingResponse;

import katchup.Exception.GenericException;
import katchup.Exception.NotAcceptableException;
import katchup.Exception.NotFoundException;
import katchup.Exception.UnAuthorizedException;
import katchup.Meeting.MeetingService;
import katchup.Meeting.model.Meeting;
import katchup.Meeting.model.Status;
import katchup.Meeting.repository.node0.MeetingNode0Repository;
import katchup.Meeting.repository.node1.MeetingNode1Repository;
import katchup.Meeting.repository.node2.MeetingNode2Repository;
import katchup.MeetingResponse.model.*;
import katchup.MeetingResponse.repository.node0.MeetingResponseNode0Repository;
import katchup.MeetingResponse.repository.node1.MeetingResponseNode1Repository;
import katchup.MeetingResponse.repository.node2.MeetingResponseNode2Repository;
import katchup.Sharding.ShardingService;
import katchup.Sharding.Utilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MeetingResponseService {

    @Autowired
    MeetingResponseNode0Repository meetingResponseNode0Repository;

    @Autowired
    MeetingResponseNode1Repository meetingResponseNode1Repository;

    @Autowired
    MeetingResponseNode2Repository meetingResponseNode2Repository;

    @Autowired
    MeetingNode0Repository meetingNode0Repository;

    @Autowired
    MeetingNode1Repository meetingNode1Repository;

    @Autowired
    MeetingNode2Repository meetingNode2Repository;


    @Autowired
    MeetingService meetingService;

    @Autowired
    ShardingService shardingService;

    public List<Inbox> getInboxForUserName(String userName) {

        List<MeetingID> meetingIdList = retrieveAllMeetingIdsByUserNameFromTable(userName);
        List<Meeting> meetingDetailsList = new ArrayList<>();

        if (meetingIdList.size() > 0) {
            List<String> meetingIdsList = meetingIdList.stream().map(meetingID -> meetingID.getMeetingId()).collect(Collectors.toList());
            meetingDetailsList = getMeetingDetailsListForUserNameHavingMeetingIds(userName, meetingIdsList);
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

    public List<Meeting> getMeetingDetailsListForUserNameHavingMeetingIds(String userName, List<String> meetingIdsList) {
        List<Meeting> meetingDetailsList;
        Map<Integer, List<String>> databaseIdmeetingIdMap = shardingService.getDbIdMeetingIdMap(userName, meetingIdsList);
        meetingDetailsList = meetingService.getMeetingDetailsForMeetingIds(databaseIdmeetingIdMap);
        return meetingDetailsList;
    }


    public MeetingInboxResponse postInboxForUserName(MeetingInboxResponse meetingInboxResponse) {
        meetingInboxResponse = saveTableInDb(meetingInboxResponse);
        return meetingInboxResponse;
    }

    public Decision putResponseForMeeting(boolean isExternalParticipant, Meeting meeting, String userName, MeetingRequestBody requestBody)
            throws Exception {

        MeetingInboxResponse meetingResponse;

        if (!isExternalParticipant)
            meetingResponse = retrieveByUserNameAndMeetingID(userName, meeting.getMeetingId());
        else {
            meetingResponse = this.createExternalMeetingInboxResponse(meeting, userName);
        }


        if (meetingResponse == null)
            throw new UnAuthorizedException("Sorry! You are not authorized to respond for this meeting");

        if (meeting.getSeats() == 0 && requestBody.getDecision() != Decision.DECLINE) {
            throw new NotFoundException("Sorry! No seats available");
        }


        if (requestBody.getDecision() == Decision.ACCEPT) {

            if (meeting.getSeats() != -1) {
                //if first time responding or previously declined
                if (meetingResponse.getDecision() != Decision.ACCEPT) {
                    int currentSeats = meeting.getSeats() - 1;
                    meeting.setSeats(currentSeats);
                }

                if (meeting.getSeats() == 0)
                    meeting.setStatus(Status.CLOSED);
            }
        } else if (requestBody.getDecision() == Decision.DECLINE) {
            if (meeting.getSeats() != -1) {

                //if previously accepted
                if (meetingResponse.getDecision() == Decision.ACCEPT) {
                    int currentSeats = meeting.getSeats() + 1;
                    meeting.setSeats(currentSeats);
                }

                if (meeting.getSeats() > 0)
                    meeting.setStatus(Status.OPEN);
            }
        } else if (requestBody.getDecision() == Decision.POLL) {
            if (!meeting.isPollAllowed())
                throw new NotAcceptableException("Sorry! The meeting host didn't enable the 'Poll' option");
            meetingResponse.setAlternativeStartDateTime(requestBody.getStartDateTime());
            meetingResponse.setAlternativeEndDateTime(requestBody.getEndDateTime());
        } else if (requestBody.getDecision() == Decision.GO_WITH_MAJORITY) {
            if (!meeting.isGoWithMajorityAllowed())
                throw new NotAcceptableException("Sorry! The meeting host didn't enable the 'Go With Majority' option");
        }
        meetingResponse.setDecision(requestBody.getDecision());
        saveTableInDb(meetingResponse);

        if (isExternalParticipant) {
            //add new ext participant to the external participant list in Meeting
            List<String> externalParticipantsList = meeting.getExtParticipantList();
            if (externalParticipantsList == null)
                externalParticipantsList = new ArrayList<>();
            externalParticipantsList.add(userName);
            meeting.setExtParticipantList(externalParticipantsList);
        }
        meetingService.updateMeetingTable(meeting);
        return requestBody.getDecision();
    }

    private MeetingInboxResponse createExternalMeetingInboxResponse(Meeting meeting, String userName) {
        MeetingInboxResponse meetingResponse = new MeetingInboxResponse();
        meetingResponse.setUserName(userName);
        meetingResponse.setMeetingId(meeting.getMeetingId());
        return meetingResponse;
    }

    public MeetingInboxResponse getResponseForMeeting(String userName, String meetingId) {
        MeetingInboxResponse meetingResponse = retrieveByUserNameAndMeetingID(userName, meetingId);
        return meetingResponse;
    }

    public MeetingStats getStatsForMeeting(Meeting meeting) {
        List<MeetingInboxResponse> meetingInboxResponseList = retrieveAllFromTableByMeetingId(meeting.getMeetingId());
        List<String> acceptedInvitees = getUserNameFromMeetingResponses(Decision.ACCEPT, meetingInboxResponseList);
        List<String> declinedInvitees = getUserNameFromMeetingResponses(Decision.DECLINE, meetingInboxResponseList);
        List<PolledParticipants> polledInvitees = getPolledParticipantsFromMeetingResponses(meetingInboxResponseList);
        List<String> goWithMajorityInvitees = getUserNameFromMeetingResponses(Decision.GO_WITH_MAJORITY, meetingInboxResponseList);

        MeetingStats meetingStats = new MeetingStats();
        meetingStats.setMeetingId(meeting.getMeetingId());
        int seatsAvailable = meeting.getSeats() == -1 ? 1000 : meeting.getSeats();
        meetingStats.setSeatsAvailable(seatsAvailable);
        meetingStats.setTotalResponses(meetingInboxResponseList.size());

        if (acceptedInvitees.size() > declinedInvitees.size())
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

    public Optional<Meeting> retrieveFromTable(String meetingId, int databaseId) {
        switch (databaseId) {
            case 0:
                return meetingNode0Repository.findById(meetingId);
            case 1:
                return meetingNode1Repository.findById(meetingId);
            case 2:
                return meetingNode2Repository.findById(meetingId);
        }
        return null;
    }

    public List<MeetingInboxResponse> retrieveAllFromTableByMeetingId(String meetingId) {
        List<MeetingInboxResponse> result = new ArrayList<>();
        result.addAll(meetingResponseNode0Repository.findAllbyMeetingID(meetingId));
        result.addAll(meetingResponseNode1Repository.findAllbyMeetingID(meetingId));
        result.addAll(meetingResponseNode2Repository.findAllbyMeetingID(meetingId));
        return result;
    }

    public List<MeetingID> getMeetingIdListForUserNameAndDecision(String userName, Decision decision) {
        return retrieveMeetingIdListForUserNameAndDecision(userName, decision);
    }

    private List<String> getUserNameFromMeetingResponses(Decision decision, List<MeetingInboxResponse> meetingInboxResponseList) {
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

    private Optional<Meeting> retrieveFromTable(String meetingId, String host) throws Exception {
        Integer databaseId = Utilities.getShardedDBLocation(host).ordinal();
        return retrieveFromTable(meetingId, databaseId);
    }

    private List<MeetingID> retrieveAllMeetingIdsByUserNameFromTable(String userName) {
        Integer databaseId = Utilities.getShardedDBLocation(userName).ordinal();
        List<MeetingID> meetingIdList = new ArrayList<>();
        switch (databaseId) {
            case 0:
                meetingIdList = meetingResponseNode0Repository.findAllMeetingIdsbyUserName(userName);
                break;
            case 1:
                meetingIdList = meetingResponseNode1Repository.findAllMeetingIdsbyUserName(userName);
                break;
            case 2:
                meetingIdList = meetingResponseNode2Repository.findAllMeetingIdsbyUserName(userName);
        }
        return meetingIdList;
    }

    private MeetingInboxResponse saveTableInDb(MeetingInboxResponse meetingInboxResponse) {
        Integer databaseId = Utilities.getShardedDBLocation(meetingInboxResponse.getUserName()).ordinal();
        switch (databaseId) {
            case 0:
                return meetingResponseNode0Repository.save(meetingInboxResponse);
            case 1:
                return meetingResponseNode1Repository.save(meetingInboxResponse);
            case 2:
                return meetingResponseNode2Repository.save(meetingInboxResponse);
        }
        throw new GenericException("Error saving record in table");
    }

    private MeetingInboxResponse retrieveByUserNameAndMeetingID(String userName, String meetingId) {
        Integer databaseId = Utilities.getShardedDBLocation(userName).ordinal();
        switch (databaseId) {
            case 0:
                return meetingResponseNode0Repository.findByUserNameAndMeetingID(userName, meetingId);
            case 1:
                return meetingResponseNode1Repository.findByUserNameAndMeetingID(userName, meetingId);
            case 2:
                return meetingResponseNode2Repository.findByUserNameAndMeetingID(userName, meetingId);
        }
        throw new GenericException("Error retrieving record in table");
    }

    private List<MeetingID> retrieveMeetingIdListForUserNameAndDecision(String userName, Decision decision) {
        Integer databaseId = Utilities.getShardedDBLocation(userName).ordinal();
        switch (databaseId) {
            case 0:
                return meetingResponseNode0Repository.findAllMeetingIdsbyUserNameAndDecision(userName, decision);
            case 1:
                return meetingResponseNode1Repository.findAllMeetingIdsbyUserNameAndDecision(userName, decision);
            case 2:
                return meetingResponseNode2Repository.findAllMeetingIdsbyUserNameAndDecision(userName, decision);
        }
        return null;
    }

}
