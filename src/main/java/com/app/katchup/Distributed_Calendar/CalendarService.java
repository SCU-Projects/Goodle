package com.app.katchup.Distributed_Calendar;


import com.app.katchup.Users.User;
import com.app.katchup.Users.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class CalendarService {

    UserRepository userRepository;

    private static final Logger logger = LogManager.getLogger(com.app.katchup.Users.UserService.class);


    public boolean verifyUserPassword(String userName,String password)
    {
        User user=userRepository.findByUserName(userName);
        return user.getPassword().equals(password);
    }

}
