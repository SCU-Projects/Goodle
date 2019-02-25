package com.app.katchup.MeetingResponse;

import com.app.katchup.MeetingResponse.model.Decision;
import com.app.katchup.MeetingResponse.model.Inbox;
import com.app.katchup.MeetingResponse.model.MeetingInboxResponse;
import com.app.katchup.MeetingResponse.model.MeetingResponse;
import com.app.katchup.Users.User;
import com.app.katchup.Users.UserService;
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

    @Autowired
    UserService userService;

    @PostMapping("/inbox")
    public ResponseEntity<MeetingInboxResponse> postInbox(@RequestBody MeetingInboxResponse meetingInboxObject){
        MeetingInboxResponse invite = meetingResponseService.postInboxForUserName(meetingInboxObject);
        logger.info(String.format("Posted meeting invite having meeting id %s for user:%s", meetingInboxObject.getMeetingId(),
                meetingInboxObject.getUserName()));
        return new ResponseEntity<>(invite, HttpStatus.OK);
    }

    @GetMapping("/inbox/{userName}")
    public ResponseEntity<List<Inbox>> getInboxForUserName(@PathVariable String userName){
        //Returns only the meeting-id
        List<Inbox> invites = meetingResponseService.getInboxForUserName(userName);
        logger.info(String.format("Returning %s meeting invites for user:%s", invites.size(), userName));
        return new ResponseEntity<>(invites, HttpStatus.OK);
    }

    @PutMapping("/meetings/{meetingId}/response")
    public ResponseEntity<Decision> putInboxForUserDecision(@PathVariable String meetingId,
                                                                        @RequestBody MeetingResponse body) {
        body.setMeetingId(meetingId);
        User user =  userService.getUserByUserName(body.getUserName());
          if(user.getPassword().equals(body.getUserPassword())) { //for comparing the passwords
            Decision storedDecision = meetingResponseService.putResponseForUserDecision(body);
            return new ResponseEntity<>(storedDecision, HttpStatus.OK);
          }
          else{
              return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
          }

    }
}
