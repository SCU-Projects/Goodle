package com.app.katchup.Meeting;

import com.app.katchup.Exception.GenericException;
import com.app.katchup.Exception.NotFoundException;
import com.app.katchup.Exception.UnAuthorizedException;
import com.app.katchup.Meeting.model.Meeting;
import com.app.katchup.Meeting.model.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class MeetingService {

    @Autowired
    MeetingRepository meetingRepository;

    public Meeting createMeeting(Meeting meeting) throws GenericException {
        if(meetingRepository.findMeetingByFilter(meeting.getHost(), meeting.getStartDateTime(),
                                                    meeting.getEndDateTime(), meeting.getVenue()) != null){
            throw new EntityExistsException("Sorry! Meeting cannot be created. There exists another meeting on the given data");
        }
        if(!isDateTimeValid(meeting))
            throw new GenericException("Either the meeting start time is after end time or the meeting date is  invalid");
        meeting = meetingRepository.save(meeting);
        return meeting;
    }
    public Meeting updateMeeting(String meetingId, String hostName, Meeting meeting) throws GenericException {

        Optional<Meeting> currentMeeting = this.getMeetingDetails(meetingId);
        currentMeeting.orElseThrow(() -> new EntityNotFoundException("No such meeting found for given meeting id"));

        if(!currentMeeting.get().getHost().equals(hostName))
            throw new UnAuthorizedException("Sorry! You don't have the permission to access this resource");

        Meeting exitingMeeting = meetingRepository.findMeetingByFilter(meeting.getHost(), meeting.getStartDateTime(),
                meeting.getEndDateTime(), meeting.getVenue());

        if (exitingMeeting != null) {
            throw new EntityExistsException("Sorry! Meeting cannot be updated. There exists another meeting on the given data");
        }

        if (!isDateTimeValid(meeting))
            throw new GenericException("Either the meeting start time is after end time or the meeting date is  invalid");

        meeting.setStatus(Status.UPDATE);
        meeting = meetingRepository.save(meeting);
        return meeting;
    }
    
    public Optional<Meeting> getMeetingDetailsForMeetingId(String meetingId, String userName) throws NotFoundException {
       Optional<Meeting> meeting =  meetingRepository.findById(meetingId);
       meeting.orElseThrow(() -> new NotFoundException("No such meeting found for given meeting id"));
       if(!this.isAuthorizedUserForAccessingMeeting(userName, meeting.get()))
           throw new UnAuthorizedException("Sorry! You don't have the permission to access this resource");
       return meeting;
    }

    public boolean isAuthorizedUserForAccessingMeeting(String userName, Meeting meeting){
       if(!meeting.getHost().equals(userName)) {
           HashSet<String> invitees = new HashSet<>(meeting.getInviteList());
           if (!invitees.contains(userName)) {
               return false;
           }
       }
       return true;
    }

    public List<Meeting> getMeetingDetailsForMeetingIds(List<String> meetingIds){
        List<Meeting> meetingList = new ArrayList<>();
        meetingRepository.findAllById(meetingIds).forEach(meetingList::add);
        return meetingList;
    }

    public Optional<Meeting> getMeetingDetails(String meetingId){
        return meetingRepository.findById(meetingId);
    }

    private boolean isDateTimeValid(Meeting meeting ){
       return ((meeting.getStartDateTime().compareTo(meeting.getEndDateTime()) <= 0) &&
                (meeting.getStartDateTime().compareTo(LocalDateTime.now()) > 0));
    }

    public void deleteMeeting(String meetingId) throws GenericException {
        Optional<Meeting> meeting = meetingRepository.findById(meetingId);
        meeting.orElseThrow(() -> new GenericException("No such meeting found for given meeting id"));
        meeting.get().setStatus(Status.DELETED);
        meetingRepository.save(meeting.get());
    }
}
