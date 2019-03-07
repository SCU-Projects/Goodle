package com.app.katchup.DistributedCalendar;

import com.app.katchup.DistributedCalendar.model.DistributedCalendar;
import com.app.katchup.DistributedCalendar.model.Event;
import com.app.katchup.Meeting.MeetingService;
import com.app.katchup.MeetingResponse.model.MeetingID;
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
import java.util.ArrayList;
import java.util.List;

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
        if (!(userService.isCredentialsMatched(userName, request.getHeader("password"))))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        List<MeetingID> meetingIds = calendarService.getAcceptedMeetingIds(userName);

        if (meetingIds.isEmpty()) {
            logger.info("there are no meetings accepted by user, so returns an empty event list");
            DistributedCalendar distributedCalendar = new DistributedCalendar();
            List<Event> eventList = new ArrayList<Event>();
            distributedCalendar.setEventList(eventList);
            return new ResponseEntity<>(distributedCalendar, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(calendarService.addAcceptedEventsToDistributedCalendar(meetingIds), HttpStatus.OK);
        }
    }
}
