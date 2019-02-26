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

    public User getUserByUserName(String userName) {
        User user = userRepository.findByUserName(userName);
        if(user == null) {
            logger.info("GET User {} not found.",userName);
            return user;
        }
        logger.info("GET User {} returned.",userName);
        return user;
    }

    public boolean verifyUserPassword(String userName,String password)
    {
        User user=userRepository.findByUserName(userName);
        if(user.getPassword().equals(password))
        {
            return  true;
        }
        else{return  false;}
    }

}
