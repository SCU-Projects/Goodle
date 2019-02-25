package com.app.katchup.MeetingResponse;

import com.app.katchup.MeetingResponse.model.Decision;
import com.app.katchup.MeetingResponse.model.Inbox;
import com.app.katchup.MeetingResponse.model.MeetingInboxResponse;
import com.app.katchup.MeetingResponse.model.MeetingResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
   public Decision putResponseForUserDecision(MeetingResponse meetingUserResponse){
        MeetingInboxResponse meetingResponse = meetingResponseRepo.findbyUserNameAndMeetingID(meetingUserResponse.getUserName(),
                meetingUserResponse.getMeetingId());
        if(meetingUserResponse == null)
            return null;
        meetingResponse.setDecision(meetingUserResponse.getDecision());
        meetingResponseRepo.save(meetingResponse);
        return meetingUserResponse.getDecision();

   }
}
