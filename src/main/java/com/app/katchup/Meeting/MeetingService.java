package com.app.katchup.Meeting;

import com.app.katchup.Exception.UnauthorizedException;
import com.app.katchup.Meeting.model.Meeting;
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

    public Meeting createMeeting(Meeting meeting) throws Exception{
        if(meetingRepository.findMeetingByFilter(meeting.getHost(), meeting.getStartDateTime(),
                                                    meeting.getEndDateTime(), meeting.getVenue()) == null){
            if(isDateTimeValid(meeting)){
                meeting = meetingRepository.save(meeting);
                return meeting;
            }else {

                    throw new Exception("startDateTime is before EndDateTime or date is not valid");
            }
        }
        else{
            throw new EntityExistsException("Sorry! Meeting cannot be created. There exists another meeting on the given data");
        }
    }

    public MeetingService() {
        super();
    }

    public Optional<Meeting> getMeetingDetailsForMeetingIds(String meetingId, String userName) {
       Optional<Meeting> meeting =  meetingRepository.findById(meetingId);
       meeting.orElseThrow(() -> new EntityNotFoundException("No such meeting found for given meeting id"));
       if(!this.isAuthorizedUserForAccessingMeeting(userName, meeting.get()))
           throw new UnauthorizedException("Sorry! You don't have the permission to access this resource");
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
       if ((meeting.getStartDateTime().compareTo(meeting.getEndDateTime()) <= 0) &&
                (meeting.getStartDateTime().compareTo(LocalDateTime.now())>0)){
            return true;
        }
       return false;
    }

 }
}
