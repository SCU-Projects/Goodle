package com.app.katchup.Meeting;

import com.app.katchup.Meeting.model.Meeting;
import com.app.katchup.Users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@RestController
public class MeetingController {
    @Autowired
    MeetingService meetingService;

    @Autowired
    UserService userService;

    @PostMapping("/meeting/create")
    public ResponseEntity<Meeting> postMeeting(@RequestBody Meeting meeting, HttpServletRequest request){
        if(userService.isCredentialsMatched(request.getHeader("userName"), request.getHeader("password"))) {
            meeting.setHost(request.getHeader("userName"));
            meeting.setPassword(this.generatePassword());
            Meeting meetingObj = meetingService.createMeeting(meeting);
            if(meeting != null)
                return new ResponseEntity<>(meetingObj, HttpStatus.CREATED);
            return new ResponseEntity<>(meetingObj,HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/meetings/{meetingId}/details")
    public ResponseEntity<Optional<Meeting>> getMeetingDetails(@PathVariable String meetingId, HttpServletRequest request){
        if(userService.isCredentialsMatched(request.getHeader("userName"), request.getHeader("password"))) {
            Optional<Meeting> meetingDetails = meetingService.getMeetingDetailsForMeetingIds(meetingId, request.getHeader("userName"));
            if(meetingDetails != null) {
                if (meetingDetails.get().getSeats() == -1)
                    meetingDetails.get().setSeats(1000);
                return new ResponseEntity<>(meetingDetails, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    private String generatePassword(){
        String password = UUID.randomUUID().toString();
        return password;
    }
}
