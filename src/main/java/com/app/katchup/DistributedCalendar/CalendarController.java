package com.app.katchup.DistributedCalendar;

import com.app.katchup.DistributedCalendar.model.DistributedCalendar;
import com.app.katchup.DistributedCalendar.model.Event;
import com.app.katchup.Meeting.MeetingService;
import com.app.katchup.Meeting.model.Meeting;
import com.app.katchup.Users.User;
import com.app.katchup.Users.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/calendar")
public class CalendarController {

    private static final Logger logger = LogManager.getLogger(CalendarController.class);

    @Autowired
    CalendarService calendarService;
    @Autowired
    UserService userService;
    @Autowired
    MeetingService meetingService;


    @GetMapping("/{userName}")
    public ResponseEntity<DistributedCalendar> showUserEvents(@PathVariable String userName, HttpServletRequest request) {

        User user = userService.getUserByUserName(userName);
        if (user == null) {
            logger.info("user name is not valid");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        boolean t = userService.isCredentialsMatched(user.getUserName(), request.getHeader("password"));
        if (t) {
            logger.info("user password not valid");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else {
            List<String> meetingIds = calendarService.getAcceptedMeetingIds(user.getUserName());
            Map<Integer, List<String>> databaseIdmeetingIdMap = new HashMap<>();
            //get sharding user to groupBy db ids
            List<Meeting> meetingList = meetingService.getMeetingDetailsForMeetingIds(databaseIdmeetingIdMap);

            DistributedCalendar distributedCalendar = new DistributedCalendar();

            List<Event> eventList = meetingList.stream()
                    .map(meeting -> {
                        Event event = new Event();
                        event.setEndDateTime(meeting.getEndDateTime());
                        event.setHost(meeting.getHost());
                        event.setMeetingId(meeting.getMeetingId());
                        event.setStartDateTime(meeting.getStartDateTime());
                        //                        distributedCalendar.setSubject(meeting.getSubject());
                        event.setVenue(meeting.getVenue());
                        return event;
                    })
                    .collect(Collectors.toList());
            distributedCalendar.setEventList(eventList);

            return new ResponseEntity<>(distributedCalendar, HttpStatus.OK);
        }


    }

}
