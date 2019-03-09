package katchup.DistributedCalendar;


import katchup.MeetingResponse.repository.primary.MeetingResponseRepository;
import katchup.MeetingResponse.model.Decision;
import katchup.Users.User;
import katchup.Users.repository.primary.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CalendarService {

    private static final Logger logger = LogManager.getLogger(katchup.Users.UserService.class);
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
