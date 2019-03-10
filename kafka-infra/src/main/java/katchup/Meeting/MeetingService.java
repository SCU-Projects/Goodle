package katchup.Meeting;

import katchup.Exception.GenericException;
import katchup.Exception.NotFoundException;
import katchup.Exception.UnAuthorizedException;
import katchup.Meeting.model.Meeting;
import katchup.Meeting.model.Status;
import katchup.Meeting.repository.node0.MeetingNode0Repository;
import katchup.Meeting.repository.node1.MeetingNode1Repository;
import katchup.Meeting.repository.node2.MeetingNode2Repository;
import katchup.MeetingResponse.model.Decision;
import katchup.MeetingResponse.model.MeetingID;
import katchup.Sharding.ShardingService;
import katchup.Sharding.Utilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class MeetingService {

    @Autowired
    ShardingService shardingService;

    @Autowired
    MeetingNode0Repository meetingNode0Repository;

    @Autowired
    MeetingNode1Repository meetingNode1Repository;

    @Autowired
    MeetingNode2Repository meetingNode2Repository;

    public Meeting createMeeting(Meeting meeting) throws Exception {
        if(retrieveMeetingByFilter(meeting, meeting.getSubject()) != null){
            throw new EntityExistsException("Sorry! Meeting cannot be created. There exists another meeting on the given data");
        }
        if(!isDateTimeValid(meeting))
            throw new GenericException("Either the meeting start time is after end time or the meeting date is  invalid");
        meeting = saveMeetingToTable(meeting);
        return meeting;
    }
    public Meeting updateMeeting(String meetingId, String hostName, Meeting meeting) throws Exception {

        Optional<Meeting> currentMeeting = this.getMeetingDetails(meetingId, hostName);
        currentMeeting.orElseThrow(() -> new NotFoundException("Either you dont have the permission to access this resource or " +
                                                                    "No such meeting found for given meeting id"));

        if(!currentMeeting.get().getHost().equals(hostName))
            throw new UnAuthorizedException("Sorry! You don't have the permission to access this resource");

        Meeting exitingMeeting = retrieveMeetingByFilter(meeting, hostName);

        if (exitingMeeting != null) {
            throw new EntityExistsException("Sorry! Meeting cannot be updated. There exists another meeting on the given data");
        }

        if (!isDateTimeValid(meeting))
            throw new GenericException("Either the meeting start time is after end time or the meeting date is  invalid");

        meeting.setStatus(Status.UPDATE);
        meeting = updateMeetingTable(meeting);
        return meeting;
    }
    
    public Optional<Meeting> getMeetingDetailsForMeetingId(Boolean onlyHost, String meetingId, String userName) throws Exception {
        //get user's meeting's db location and then query on that location
       int sourceDbId = Utilities.getShardedDBLocation(userName).ordinal();
       int targetDbId = shardingService.getMeetingDbLocationForUser(sourceDbId, meetingId);
       Optional<Meeting> meeting =  retrieveFromTable(meetingId, targetDbId);
       meeting.orElseThrow(() -> new NotFoundException("No such meeting found for given meeting id"));
       if(!this.isAuthorizedUserForAccessingMeeting(onlyHost, userName, meeting.get()))
           throw new UnAuthorizedException("Sorry! You don't have the permission to access this resource");
       return meeting;
    }

    public boolean isAuthorizedUserForAccessingMeeting(Boolean onlyHost, String userName, Meeting meeting){
       if(!meeting.getHost().equals(userName)) {
           if(onlyHost)
               return false;
           HashSet<String> invitees = new HashSet<>(meeting.getInviteList());
           invitees.addAll(meeting.getExtParticipantList());
           if (!invitees.contains(userName)) {
               return false;
           }
       }
       return true;
    }

    public List<Meeting> getMeetingDetailsForMeetingIds(Map<Integer, List<String>> databaseIdMeetingIdMap){
        List<Meeting> meetingList = new ArrayList<>();
        databaseIdMeetingIdMap.forEach((k, v) -> {
            try {
                retrieveAllFromTable(k,v).forEach(meetingList::add);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return meetingList;
    }

    public Optional<Meeting> getMeetingDetails(String meetingId, String hostName) throws Exception {
        return retrieveFromTable(meetingId, hostName);
    }


    public void deleteMeeting(String meetingId, String host) throws Exception {
        Optional<Meeting> meeting = retrieveFromTable(meetingId, host);
        meeting.orElseThrow(() -> new NotFoundException("Sorry! The meeting does not exist."));
        meeting.get().setStatus(Status.DELETED);
        saveMeetingToTable(meeting.get());
    }

    private boolean isDateTimeValid(Meeting meeting ){
        return ((meeting.getStartDateTime().compareTo(meeting.getEndDateTime()) <= 0) &&
                (meeting.getStartDateTime().compareTo(LocalDateTime.now()) > 0));
    }

    public Meeting updateMeetingTable(Meeting meeting) throws Exception {
       return saveToTable(meeting);
    }

    public Meeting saveMeetingToTable(Meeting meeting) throws Exception {
        meeting = saveToTable(meeting);
        Integer databaseId = Utilities.getShardedDBLocation(meeting.getHost()).ordinal();
        //shardingService.createLookUpForMeetingId(databaseId, meeting.getMeetingId());{final}
        for(int i = 0; i <= 2; i++)
            shardingService.createLookUpForMeetingId(i, databaseId, meeting.getMeetingId());
        return meeting;
    }

    private Optional<Meeting> retrieveFromTable(String meetingId, String host) throws Exception {
        Integer databaseId = Utilities.getShardedDBLocation(host).ordinal();
        return retrieveFromTable(meetingId, databaseId);
    }

    private Meeting saveToTable(Meeting meeting) throws Exception {
        Integer databaseId = Utilities.getShardedDBLocation(meeting.getHost()).ordinal();
        switch (databaseId){
            case 0:
                meeting = meetingNode0Repository.save(meeting);
                break;
            case 1:
                meeting =  meetingNode1Repository.save(meeting);
                break;
            case 2:
                meeting =  meetingNode2Repository.save(meeting);
                break;
            default:
                throw new Exception("Error saving meeting to the table");
        }
        return meeting;
    }

    public Optional<Meeting> retrieveFromTable(String meetingId, int databaseId){
        switch (databaseId){
            case 0:
                return meetingNode0Repository.findById(meetingId);
            case 1:
                return meetingNode1Repository.findById(meetingId);
            case 2:
                return meetingNode2Repository.findById(meetingId);
        }
        return null;
    }

    private Iterable<Meeting> retrieveAllFromTable(Integer databaseId, List<String> meetingIdList) {
        switch (databaseId){
            case 0:
                return meetingNode0Repository.findAllById(meetingIdList);
            case 1:
                return meetingNode1Repository.findAllById(meetingIdList);
            case 2:
                return meetingNode2Repository.findAllById(meetingIdList);
        }
        return null;
    }

    private Meeting retrieveMeetingByFilter(Meeting meeting, String host){
        Integer databaseId = Utilities.getShardedDBLocation(host).ordinal();
        switch (databaseId){
            case 0:
                return meetingNode0Repository.findMeetingByFilter(meeting.getHost(), meeting.getSubject(), meeting.getStartDateTime(),
                        meeting.getEndDateTime(), meeting.getVenue());
            case 1:
                return meetingNode1Repository.findMeetingByFilter(meeting.getHost(), meeting.getSubject(), meeting.getStartDateTime(),
                        meeting.getEndDateTime(), meeting.getVenue());
            case 2:
                return meetingNode2Repository.findMeetingByFilter(meeting.getHost(), meeting.getSubject(), meeting.getStartDateTime(),
                        meeting.getEndDateTime(), meeting.getVenue());
        }
        return null;
    }
}
