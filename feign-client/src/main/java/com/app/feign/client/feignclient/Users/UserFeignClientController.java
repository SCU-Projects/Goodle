package com.app.feign.client.feignclient.Users;

import katchup.Users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.List;
import java.util.logging.Logger;

@RestController
public class UserFeignClientController {
    private Logger logger = (Logger) Logger.getLogger(String.valueOf(this.getClass()));

    @Autowired
    RestTemplate restTemplate;

    @GetMapping(value = "/feign/users")
    public ResponseEntity<List<User>> getAllUsers(){
        ResponseEntity<List<User>>  users =  (ResponseEntity) this.restTemplate.getForEntity("http://katchup-service/users/",List.class);
        logger.info(String.valueOf(users));
        return users;
    }

    @GetMapping("/feign/users/{userName}")
    public ResponseEntity<User> getUserByUserName(@PathVariable String userName){
        ResponseEntity<User> userResponseEntity= this.restTemplate.getForEntity("http://katchup-service/users/"+userName,User.class);
        return  userResponseEntity;
    }

    @PostMapping()
    public ResponseEntity<User> addUser(@RequestBody @Valid User user)
    {
        ResponseEntity<User> userResponseEntity= this.restTemplate.postForEntity("http://katchup-service/users", user ,User.class);
        return  userResponseEntity;
    }



}
