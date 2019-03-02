package com.app.katchup.DistributedCalendar;

import com.app.katchup.MeetingResponse.MeetingResponseRepository;
import com.app.katchup.MeetingResponse.model.Decision;
import com.app.katchup.MeetingResponse.model.MeetingID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CalendarService {

    @Autowired
    MeetingResponseRepository meetingResponseRepository;

    public List<MeetingID> getAcceptedMeetingIds(String userName) {
        return meetingResponseRepository.findAllMeetingIdsbyUserNameAndDecision(userName, Decision.ACCEPT);
    }
}
