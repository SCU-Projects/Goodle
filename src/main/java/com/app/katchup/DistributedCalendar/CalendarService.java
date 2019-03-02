package com.app.katchup.DistributedCalendar;

import com.app.katchup.MeetingResponse.MeetingResponseRepository;
import com.app.katchup.MeetingResponse.model.Decision;
import com.app.katchup.MeetingResponse.model.MeetingID;
import com.app.katchup.Users.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CalendarService {
    UserRepository userRepository;
    private static final Logger logger = LogManager.getLogger(com.app.katchup.Users.UserService.class);
    @Autowired
    MeetingResponseRepository meetingResponseRepository;

    public List<MeetingID> getAcceptedMeetingIds(String userName) {
        return meetingResponseRepository.findAllMeetingIdsbyUserNameAndDecision(userName, Decision.ACCEPT);
    }
}
