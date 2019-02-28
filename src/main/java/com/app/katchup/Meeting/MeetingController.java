package com.app.katchup.Meeting;

import com.app.katchup.Meeting.model.Meeting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MeetingController {
    @Autowired
    MeetingService meetingService;

    @PostMapping("/meeting/create")
    public ResponseEntity<Meeting> postMeeting(@RequestBody Meeting meeting) {
        Meeting meetingCreated = meetingService.createMeeting(meeting);
        if (meetingCreated == null) {
            return new ResponseEntity<>(meetingCreated, HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(meetingCreated, HttpStatus.CREATED);
    }
}
