package com.app.katchup.Meeting;

import com.app.katchup.Meeting.model.Meeting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

//import com.app.katchup.MeetingResponse.model.MeetingInboxResponse;

@Service
public class MeetingService {

    @Autowired
    MeetingRepository meetingRepository;
    public Meeting createMeeting(Meeting meeting){
        if(meetingRepository.findMeetingByFilter(meeting.getHost(),
                meeting.getStartDateTime(),meeting.getEndDateTime(),
                meeting.getVenue())==null){
           Meeting createdMeeting= meetingRepository.save(meeting);
           if(createdMeeting!=null){
               return createdMeeting;
           }
        }

        return null;

    }


    public List<Meeting> getMeetingDetails(List<String> meetingIds) {
        List<Meeting> meetingList = new ArrayList<>();
        meetingRepository.findAllById(meetingIds).forEach(meetingList::add);
        return meetingList;
    }


}
