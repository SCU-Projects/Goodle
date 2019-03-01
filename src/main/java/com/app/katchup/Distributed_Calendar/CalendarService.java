package com.app.katchup.Distributed_Calendar;


import com.app.katchup.MeetingResponse.MeetingResponseRepository;
import com.app.katchup.MeetingResponse.model.Decision;
import com.app.katchup.Users.User;
import com.app.katchup.Users.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CalendarService {

    private static final Logger logger = LogManager.getLogger(com.app.katchup.Users.UserService.class);
    UserRepository userRepository;
    MeetingResponseRepository meetingResponseRepository;

    public boolean verifyUserPassword(String userName, String password) {
        User user = userRepository.findByUserName(userName);
        return user.getPassword().equals(password);
    }


    public List<String> getAcceptedMeetingIds(String userName) {

        return meetingResponseRepository.findAllMeetingIdsbyUserNameAndDecision(userName, Decision.ACCEPT);

    }

}
