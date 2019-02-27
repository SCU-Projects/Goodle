package com.app.katchup.Meeting;

import com.app.katchup.Meeting.model.Meeting;
import com.app.katchup.MeetingResponse.model.Decision;
import com.app.katchup.MeetingResponse.model.MeetingInboxResponse;
import com.app.katchup.MeetingResponse.model.MeetingResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MeetingService {
    @Autowired
    MeetingRepository meetingRepository;
    public Meeting createMeeting(Meeting meeting){
        if(meetingRepository.findMeetingByFilter(meeting.getHost(),
                meeting.getStartDateTime(),meeting.getEndDateTime(),
                meeting.getVenue())==null){
            meetingRepository.save(meeting);
        }

        return meeting;

    }
}
