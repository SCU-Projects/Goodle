package com.app.katchup.Meeting;

import com.app.katchup.Meeting.model.Meeting;
import com.app.katchup.Users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RestController
public class MeetingController {
    @Autowired
    MeetingService meetingService;

    @Autowired
    UserService userService;

    @PostMapping("/meeting/create")
    public ResponseEntity<Meeting> postMeeting(@RequestBody Meeting meeting, HttpServletRequest request){
        if(userService.isCredentialsMatched(request.getHeader("userName"), request.getHeader("password"))) {
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
            if(meetingDetails != null){
                return new ResponseEntity<>(meetingDetails, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
}
