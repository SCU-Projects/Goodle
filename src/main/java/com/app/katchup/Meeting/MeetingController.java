package com.app.katchup.Meeting;

import com.app.katchup.Meeting.model.Meeting;
import com.app.katchup.Meeting.model.Poll;
import com.app.katchup.MeetingResponse.model.Decision;
import com.app.katchup.MeetingResponse.model.MeetingResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class MeetingController {
    @Autowired
    MeetingService meetingService;

    @PostMapping("/meeting")
    public ResponseEntity<Meeting> postMeeting(@RequestBody Meeting meeting){
        meetingService.createMeeting(meeting);
        return new ResponseEntity<>(meeting, HttpStatus.OK);
    }
}
