package com.app.feign.client.feignclient.DistributedCalendar;


import katchup.DistributedCalendar.model.DistributedCalendar;
import com.app.feign.client.feignclient.util.UtilityFunctions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;


@RestController
public class DistributedCalendarFeignClientController {

    @Autowired
    Environment environment;

    @Autowired
    RestTemplate restTemplate;


    @GetMapping("/feign/calendar/{userName}")
    public ResponseEntity<DistributedCalendar> showUserEvents(@PathVariable String userName, HttpServletRequest request)
    {
        HttpEntity<String> requestObject = new HttpEntity<>(UtilityFunctions.getUserDetailsFromRequest(request));
        ResponseEntity<DistributedCalendar> distributedCalendarResponseEntity= this.restTemplate.exchange("http://katchup-service//calendar/"+userName, HttpMethod.GET,requestObject,DistributedCalendar.class);
        return  distributedCalendarResponseEntity;
    }


}
