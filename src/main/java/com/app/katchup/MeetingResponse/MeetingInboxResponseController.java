package com.app.katchup.MeetingResponse;

import com.app.katchup.Exception.NotAcceptableException;
import com.app.katchup.Exception.NotFoundException;
import com.app.katchup.Exception.UnAuthorizedException;
import com.app.katchup.Meeting.MeetingService;
import com.app.katchup.Meeting.model.Meeting;
import com.app.katchup.Meeting.model.Status;
import com.app.katchup.MeetingResponse.model.*;
import com.app.katchup.Sharding.ShardingService;
import com.app.katchup.Sharding.Utilities;
import com.app.katchup.Users.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    ShardingService shardingService;

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
                                            HttpServletRequest request) throws Exception {
        if (userService.isCredentialsMatched(request.getHeader("userName"), request.getHeader("password"))) {
            Optional<Meeting> meeting = meetingService.getMeetingDetailsForMeetingId(meetingId, request.getHeader("userName"));
            MeetingStats meetingResponseStats = meetingResponseService.getStatsForMeeting(meeting.get());
            return new ResponseEntity<>(meetingResponseStats, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @PutMapping("/meetings/{meetingId}/response")
    public ResponseEntity<Decision> putUserDecisionForMeetingInviteResponse(@PathVariable String meetingId,
                                HttpServletRequest request, @RequestBody MeetingRequestBody requestBody)
                                throws NotFoundException, NotAcceptableException {

        boolean isExternalParticipant = false;

        requestBody.setMeetingId(meetingId);

        //for comparing the passwords
        if (!userService.isCredentialsMatched(request.getHeader("userName"), request.getHeader("password")))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        int sourceDbId = Utilities.getShardedDBLocation(request.getHeader("userName")).ordinal();
        int targetDbId = shardingService.getMeetingDbLocationForUser(sourceDbId, meetingId);
        Optional<Meeting> meeting = meetingService.retrieveFromTable(meetingId, targetDbId);
        meeting.orElseThrow(() -> new NotFoundException("Sorry! Meeting does not exist"));

        if(meeting.get().getStatus().equals(Status.DELETED))
            throw new NotAcceptableException("Sorry! Meeting has been cancelled by the Host");

        //host or valid invitee
        if(!meetingService.isAuthorizedUserForAccessingMeeting(request.getHeader("userName"), meeting.get())){
            if(!meeting.get().isExternalParticipantsAllowed()){
                throw new UnAuthorizedException("Sorry! User is not allowed to enter this meeting");
            }
            if(!meeting.get().getPassword().equals(requestBody.getMeetingPassword()))
                throw new UnAuthorizedException("No matching records found for the provided meetingid and password");

            isExternalParticipant = true;
        }

        //host or invitee or external participant with valid meeting
        Decision storedDecision = meetingResponseService.putResponseForMeeting(isExternalParticipant, meeting.get(),
                request.getHeader("userName"), requestBody);

        return new ResponseEntity<>(storedDecision, HttpStatus.OK);
    }

}
