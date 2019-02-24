package com.app.katchup.MeetingResponse;


import com.app.katchup.MeetingResponse.model.Inbox;
import com.app.katchup.MeetingResponse.model.MeetingInboxResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MeetingInboxResponseController {
    private static final Logger logger = LogManager.getLogger(MeetingInboxResponseController.class);

    @Autowired
    MeetingResponseService meetingResponseService;

    @PostMapping("/inbox")
    public ResponseEntity<MeetingInboxResponse> postInbox(@RequestBody MeetingInboxResponse meetingInboxObject){
        MeetingInboxResponse invite = meetingResponseService.postInboxForUserName(meetingInboxObject);
        logger.info(String.format("Posted meeting invite having meeting id %s for user:%s", meetingInboxObject.getMeetingId() ,meetingInboxObject.getUserName()));
        return new ResponseEntity<>(invite, HttpStatus.OK);
    }

    @GetMapping("/inbox/{userName}")
    public ResponseEntity<List<Inbox>> getInboxForUserName(@PathVariable String userName){
        //Returns only the meeting-id
        List<Inbox> invites = meetingResponseService.getInboxForUserName(userName);
        logger.info(String.format("Returning %s meeting invites for user:%s", invites.size(), userName));
        return new ResponseEntity<>(invites, HttpStatus.OK);
    }
}
