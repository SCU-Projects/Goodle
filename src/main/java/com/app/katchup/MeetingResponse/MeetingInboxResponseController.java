package com.app.katchup.MeetingResponse;

import com.app.katchup.Exception.GenericException;
import com.app.katchup.Meeting.MeetingService;
import com.app.katchup.Meeting.model.Meeting;
import com.app.katchup.MeetingResponse.model.*;
import com.app.katchup.Users.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@RestController
public class MeetingInboxResponseController {
    private static final Logger logger = LogManager.getLogger(MeetingInboxResponseController.class);

    @Autowired
    MeetingResponseService meetingResponseService;

    @Autowired
    MeetingService meetingService;

    @Autowired
    UserService userService;

    @PostMapping("/inbox")
    public ResponseEntity<MeetingInboxResponse> postInbox(@RequestBody MeetingInboxResponse meetingInboxObject,
                                                                                            HttpServletRequest request) {
        if (userService.isCredentialsMatched(request.getHeader("userName"), request.getHeader("password"))) {
            meetingInboxObject.setUserName(request.getHeader("userName"));
            MeetingInboxResponse invite = meetingResponseService.postInboxForUserName(meetingInboxObject);
            logger.info(String.format("Posted meeting invite having meeting id %s for user:%s", meetingInboxObject.getMeetingId(),
                    meetingInboxObject.getUserName()));
            return new ResponseEntity<>(invite, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/inbox")
    public ResponseEntity<List<Inbox>> getInboxForUserName(HttpServletRequest request) {
        if (userService.isCredentialsMatched(request.getHeader("userName"), request.getHeader("password"))) {
            List<Inbox> meetingInboxList = meetingResponseService.getInboxForUserName(request.getHeader("userName"));
            logger.info(String.format("Returning %s meeting invites for user:%s", meetingInboxList.size(), request.getHeader("userName")));
            return new ResponseEntity<>(meetingInboxList, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/meetings/{meetingId}/response")
    public ResponseEntity<MeetingInboxResponse> getMeetingResponseForUser(@PathVariable String meetingId,
                                                                          HttpServletRequest request) {
        if (userService.isCredentialsMatched(request.getHeader("userName"), request.getHeader("password"))) {
            MeetingInboxResponse meetingResponse = meetingResponseService.getResponseForMeeting(
                                                            request.getHeader("userName"), meetingId);
            return new ResponseEntity<>(meetingResponse, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/meetings/{meetingId}/stats")
    public ResponseEntity<MeetingStats> getMeetingStatsForMeeting(@PathVariable String meetingId,
                                                                                        HttpServletRequest request) {
        if (userService.isCredentialsMatched(request.getHeader("userName"), request.getHeader("password"))) {
            Optional<Meeting> meeting = meetingService.getMeetingDetailsForMeetingIds(meetingId, request.getHeader("userName"));
            MeetingStats meetingResponseStats = meetingResponseService.getStatsForMeeting(meeting.get());
            return new ResponseEntity<>(meetingResponseStats, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @PutMapping("/meetings/{meetingId}/response")
    public ResponseEntity<Decision> putUserDecisionForMeetingInviteResponse(@PathVariable String meetingId,
                                                            HttpServletRequest request, @RequestBody MeetingRequestBody requestBody) throws Exception, GenericException {
        requestBody.setMeetingId(meetingId);
        if (userService.isCredentialsMatched(request.getHeader("userName"), request.getHeader("password"))) { //for comparing the passwords
            Optional<Meeting> meeting = meetingService.getMeetingDetails(meetingId);
            meeting.orElseThrow(() -> new EntityNotFoundException());
            Decision storedDecision = meetingResponseService.putResponseForMeeting(meeting.get(),
                    request.getHeader("userName"), requestBody);
            return new ResponseEntity<>(storedDecision, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

    }

}
