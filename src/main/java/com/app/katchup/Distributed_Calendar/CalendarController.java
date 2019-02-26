package com.app.katchup.Distributed_Calendar;

import com.app.katchup.MeetingResponse.MeetingResponseRepository;
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
@RequestMapping("/calendar")
public class CalendarController {

//    to do
//    get the username and verify it
    private static final Logger logger = LogManager.getLogger(CalendarController.class);

    @Autowired
    CalendarService calendarService;
    UserService userService;
    MeetingResponseRepository meetingResponseRepository;

//
//    @GetMapping("/{userName}?pwd={password}")
//    public ResponseEntity<User> getVerifiedUser(@PathVariable String userName,@RequestParam String password){
//        User user=  userService.getUserByUserName(userName);
//        boolean t=userService.isCredentialsMatched(userName,password);
//        if(user == null || t==false)
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        return new ResponseEntity<>(user, HttpStatus.OK);
//    }

    @GetMapping("/{userName}pwd={password}")
//    need to create the class for DistributedCalendar
    public ResponseEntity<DistributedCalendar> showUserEvents(@PathVariable String userName, @RequestParam String password) {
        DistributedCalendar distributedCalendar = new DistributedCalendar();
        User user = userService.getUserByUserName(userName);
        boolean t = userService.isCredentialsMatched(userName, password);
        if (user == null || t == false) {
            logger.info("user credentials not valid");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
//        valid user credentials
            List<String> meetings = meetingResponseRepository.findMeetingIdByUserName(userName);
//        for all meets in meetings:
//         get a meeting by meeting id, check if its status is accepted,
//         then, get all meeting details.
//        do this by lambda method.


        }


        return new ResponseEntity<>(distributedCalendar, HttpStatus.OK);


    }

}
