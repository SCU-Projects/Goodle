package com.app.katchup.DistributedCalendar;

import com.app.katchup.DistributedCalendar.model.DistributedCalendar;
import com.app.katchup.DistributedCalendar.model.Event;
import com.app.katchup.Meeting.MeetingService;
import com.app.katchup.Meeting.model.Meeting;
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
        if (!(userService.isCredentialsMatched(userName, request.getHeader("password"))))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        else {
            List<MeetingID> meetingIds = calendarService.getAcceptedMeetingIds(userName);
            List<Meeting> meetingDetailsList = new ArrayList<>();
            DistributedCalendar distributedCalendar = new DistributedCalendar();
            List<Event> eventList = new ArrayList<Event>();

            if (meetingIds.isEmpty()) {
                if (meetingIds.size() == 0)
                    distributedCalendar.setEventList(eventList);
            } else {
                List<String> meetingIdsList = meetingIds.stream().map(meetingID -> meetingID.getMeetingId()).collect(Collectors.toList());
                meetingDetailsList = meetingService.getMeetingDetailsForMeetingIds(meetingIdsList);
                eventList = meetingDetailsList.stream()
                        .map(meeting -> {
                            Event event = new Event();
                            event.setEndDateTime(meeting.getEndDateTime());
                            event.setHost(meeting.getHost());
                            event.setMeetingId(meeting.getMeetingId());
                            event.setStartDateTime(meeting.getStartDateTime());
                            event.setSubject(meeting.getSubject());
                            event.setVenue(meeting.getVenue());
                            return event;
                        })
                        .collect(Collectors.toList());
                logger.info("all accepted events added to a new distributed calendar object");
                distributedCalendar.setEventList(eventList);
            }
            return new ResponseEntity<>(distributedCalendar, HttpStatus.OK);

        }
    }
}
