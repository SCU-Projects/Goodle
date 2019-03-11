package katchup.Meeting;

import katchup.Exception.GenericException;
import katchup.Exception.NotFoundException;
import katchup.Meeting.model.Meeting;
import katchup.Users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.UUID;

@RestController
public class MeetingController {
    @Autowired
    MeetingService meetingService;

    @Autowired
    UserService userService;

    @Autowired
    MeetingSender meetingSender;

    @PostMapping("/meeting/create")
    public ResponseEntity<Meeting> postMeeting(@RequestBody Meeting meeting, HttpServletRequest request) throws Exception {
        if(userService.isCredentialsMatched(request.getHeader("userName"), request.getHeader("password"))) {

            if(meeting.isGoWithMajorityAllowed() && meeting.getSeats() != -1)
                throw new GenericException("Go with majority option is allowed for meetings with unlimited capacity.");

            meeting.setHost(request.getHeader("userName"));
            meeting.setPassword(this.generatePassword());
            Meeting meetingObj = meetingService.createMeeting(meeting);
            String meetingData = meeting.getInviteList()+":"+meeting.getMeetingId();
            meetingSender.sendData(meetingData);
            if(meeting != null)
                return new ResponseEntity<>(meetingObj, HttpStatus.CREATED);
            return new ResponseEntity<>(meetingObj,HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/meetings/{meetingId}/details")
    public ResponseEntity<Optional<Meeting>> getMeetingDetails(@PathVariable String meetingId, HttpServletRequest request) throws Exception {
        if(userService.isCredentialsMatched(request.getHeader("userName"), request.getHeader("password"))) {
            Optional<Meeting> meetingDetails = meetingService.getMeetingDetailsForMeetingId(false, meetingId, request.getHeader("userName"));
            if(meetingDetails != null) {
                if (meetingDetails.get().getSeats() == -1)
                    meetingDetails.get().setSeats(1000);
                return new ResponseEntity<>(meetingDetails, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @DeleteMapping("/meetings/{meetingId}")
    public ResponseEntity<Meeting> deleteMeeting(@PathVariable String meetingId, HttpServletRequest request) throws Exception {
        if(userService.isCredentialsMatched(request.getHeader("userName"), request.getHeader("password"))) {
            meetingService.deleteMeeting(meetingId, request.getHeader("userName"));
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    private String generatePassword(){
        String password = UUID.randomUUID().toString();
        return password;
    }

    @PutMapping("meetings/{meetingId}")
    public ResponseEntity<Meeting> putMeeting(@PathVariable String meetingId, @RequestBody Meeting meeting,
                                              HttpServletRequest request) throws Exception {
        if (userService.isCredentialsMatched(request.getHeader("userName"), request.getHeader("password"))) {
            if (meeting.isGoWithMajorityAllowed() && meeting.getSeats() != -1)
                throw new GenericException("Go with majority option is allowed for meetings with unlimited capacity.");
            meeting.setHost(request.getHeader("userName"));
            Meeting meetingObj = meetingService.updateMeeting(meetingId, request.getHeader("userName"), meeting);
            String meetingUpdate = meeting.getMeetingId();
            meetingSender.sendUpdate(meetingUpdate);
            if (meetingObj != null)
                return new ResponseEntity<>(meetingObj, HttpStatus.OK);
            return new ResponseEntity<>(meetingObj, HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
}
