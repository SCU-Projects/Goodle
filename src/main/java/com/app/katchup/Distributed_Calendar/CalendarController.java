package com.app.katchup.Distributed_Calendar;

import com.app.katchup.Users.User;
import com.app.katchup.Users.UserController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;

@RestController
@RequestMapping("/calendar")
public class CalendarController {

//    to do
//    get the username and verify it
    private static final Logger logger = LogManager.getLogger(CalendarController.class);

    @Autowired
    CalendarService calendarService;

    @GetMapping("/{userName}?{password}")
    public ResponseEntity<User> getVerifiedUser(@PathVariable String userName,String password){
        User user=  calendarService.getUserByUserName(userName);
        boolean t=calendarService.verifyUserPassword(userName,password);

        if(user == null || t==false)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

//    @PostMapping()
//    need to create the class for DistributedCalendar
//    public ResponseEntity<DistributedCalendar> showUserEvents()
//    {
//
//    }



    }




}
