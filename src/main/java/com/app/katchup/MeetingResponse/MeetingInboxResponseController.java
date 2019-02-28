package com.app.katchup.MeetingResponse;

import com.app.katchup.MeetingResponse.model.Decision;
import com.app.katchup.MeetingResponse.model.MeetingInboxResponse;
import com.app.katchup.MeetingResponse.model.MeetingRequestBody;
import com.app.katchup.MeetingResponse.model.MeetingStats;
import com.app.katchup.Users.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class MeetingInboxResponseController {
    private static final Logger logger = LogManager.getLogger(MeetingInboxResponseController.class);

    @Autowired
    MeetingResponseService meetingResponseService;

    @Autowired
    UserService userService;

    @PostMapping("/inbox")
    public ResponseEntity<MeetingInboxResponse> postInbox(@RequestBody MeetingInboxResponse meetingInboxObject) {
        MeetingInboxResponse invite = meetingResponseService.postInboxForUserName(meetingInboxObject);
        logger.info(String.format("Posted meeting invite having meeting id %s for user:%s", meetingInboxObject.getMeetingId(),
                meetingInboxObject.getUserName()));
        return new ResponseEntity<>(invite, HttpStatus.OK);
    }

    @GetMapping("/inbox/{userName}")
    public ResponseEntity<List<String>> getInboxForUserName(@PathVariable String userName) {
        //Returns only the meeting-id
        List<String> invites = meetingResponseService.getInboxForUserName(userName);
        logger.info(String.format("Returning %s meeting invites for user:%s", invites.size(), userName));
        return new ResponseEntity<>(invites, HttpStatus.OK);
    }

    @GetMapping("/meetings/{meetingId}/response")
    public ResponseEntity<MeetingInboxResponse> getMeetingResponseForUser(@PathVariable String meetingId,
                                                                          HttpServletRequest request) {
        if (userService.isCredentialsMatched(request.getHeader("userName"), request.getHeader("password"))) {
            //client
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
            //host -> validate with meetingId
            //fill seats and other parameters
            MeetingStats meetingResponseStats = meetingResponseService.getStatsForMeetingId(meetingId);
            return new ResponseEntity<>(meetingResponseStats, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @PutMapping("/meetings/{meetingId}/response")
    public ResponseEntity<Decision> putUserDecisionForMeetingInviteResponse(@PathVariable String meetingId,
                                                                            @RequestBody MeetingRequestBody body) {
        body.setMeetingId(meetingId);
        if (userService.isCredentialsMatched(body.getUserName(), body.getUserPassword())) { //for comparing the passwords
            Decision storedDecision = meetingResponseService.putResponseForMeeting(body);
            return new ResponseEntity<>(storedDecision, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

}
