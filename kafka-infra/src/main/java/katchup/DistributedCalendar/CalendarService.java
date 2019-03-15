package katchup.DistributedCalendar;


import katchup.DistributedCalendar.model.DistributedCalendar;
import katchup.DistributedCalendar.model.Event;
import katchup.Meeting.MeetingService;
import katchup.Meeting.model.Meeting;
import katchup.MeetingResponse.MeetingResponseService;
import katchup.MeetingResponse.model.Decision;
import katchup.MeetingResponse.model.MeetingID;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CalendarService {

    private static final Logger logger = LogManager.getLogger(CalendarService.class);

    @Autowired
    MeetingResponseService meetingResponseService;


    public List<MeetingID> getAcceptedMeetingIds(String userName) {
        return meetingResponseService.getMeetingIdListForUserNameAndDecision(userName, Decision.ACCEPT);
    }

    public DistributedCalendar addAcceptedEventsToDistributedCalendar(String userName, List<MeetingID> meetingIds) {
        List<Meeting> meetingDetailsList;
        List<Event> eventList;
        DistributedCalendar distributedCalendar = new DistributedCalendar();

        List<String> meetingIdsList = meetingIds.stream().map(meetingID -> meetingID.getMeetingId()).collect(Collectors.toList());
        meetingDetailsList = meetingResponseService.getMeetingDetailsListForUserNameHavingMeetingIds(userName, meetingIdsList);
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
        return distributedCalendar;
    }
}