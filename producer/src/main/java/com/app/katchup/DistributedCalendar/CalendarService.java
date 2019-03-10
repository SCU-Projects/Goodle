package com.app.katchup.DistributedCalendar;

import com.app.katchup.DistributedCalendar.model.DistributedCalendar;
import com.app.katchup.DistributedCalendar.model.Event;
import com.app.katchup.Meeting.MeetingService;
import com.app.katchup.Meeting.model.Meeting;
import com.app.katchup.MeetingResponse.MeetingResponseRepository;
import com.app.katchup.MeetingResponse.model.Decision;
import com.app.katchup.MeetingResponse.model.MeetingID;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CalendarService {

    @Autowired
    MeetingResponseRepository meetingResponseRepository;
    private static final Logger logger = LogManager.getLogger(CalendarService.class);
    @Autowired
    MeetingService meetingService;


    public List<MeetingID> getAcceptedMeetingIds(String userName) {
        return meetingResponseRepository.findAllMeetingIdsbyUserNameAndDecision(userName, Decision.ACCEPT);
    }

    public DistributedCalendar addAcceptedEventsToDistributedCalendar(List<MeetingID> meetingIds) {
        List<Meeting> meetingDetailsList;
        List<Event> eventList;
        DistributedCalendar distributedCalendar = null;

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
        return distributedCalendar;
    }
}
