package katchup.DistributedCalendar;

import katchup.DistributedCalendar.model.*;
import katchup.DistributedCalendar.CalendarService;
import katchup.Meeting.MeetingService;
import katchup.Meeting.model.Meeting;
import katchup.MeetingResponse.model.MeetingID;
import katchup.Users.User;
import katchup.Users.UserService;
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

    @GetMapping()
    public ResponseEntity<DistributedCalendar> showUserEvents(HttpServletRequest request) {

        if (!userService.isCredentialsMatched(request.getHeader("username"), request.getHeader("password")))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        List<MeetingID> meetingIds = calendarService.getAcceptedMeetingIds(request.getHeader("username"));
        DistributedCalendar distributedCalendar = new DistributedCalendar();

        if (meetingIds.isEmpty()) {
            logger.info("there are no meetings accepted by user, returning an empty event list");
            List<Event> eventList = new ArrayList<Event>();
            distributedCalendar.setEventList(eventList);
        }
        else
            distributedCalendar = calendarService.addAcceptedEventsToDistributedCalendar(request.getHeader("username"), meetingIds);

        return new ResponseEntity<>(distributedCalendar, HttpStatus.OK);

    }
}


