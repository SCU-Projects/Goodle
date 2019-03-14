package com.app.feign.client.feignclient.Meeting;

import katchup.Meeting.model.Meeting;
import com.app.feign.client.feignclient.util.UtilityFunctions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RestController
public class MeetingFeignClientController {
    @Autowired
    RestTemplate restTemplate;

    @PostMapping("/feign/meeting/create")
    public ResponseEntity<Meeting> postMeeting(@RequestBody Meeting meeting, HttpServletRequest request) {
        HttpEntity<Meeting> requestObject = new HttpEntity<>(meeting, UtilityFunctions.getUserDetailsFromRequest(request));
        ResponseEntity<Meeting> meetingResponseEntity = restTemplate.exchange("http://katchup-service/meeting/create", HttpMethod.POST, requestObject, Meeting.class);

        return meetingResponseEntity;
    }

    @GetMapping("/feign/meetings/{meetingId}/details")
    public ResponseEntity<Optional<Meeting>> getMeetingDetails(@PathVariable String meetingId, HttpServletRequest request) {
        HttpEntity<String> requestObject = new HttpEntity<>(UtilityFunctions.getUserDetailsFromRequest(request));
        ResponseEntity<Optional<Meeting>> optionalResponseEntity = (ResponseEntity) this.restTemplate.exchange("http://katchup-service/meetings/" + meetingId + "/details", HttpMethod.GET, requestObject, Meeting.class);
        return optionalResponseEntity;
    }

    @PutMapping("/feign/meetings/{meetingId}")
    public ResponseEntity<Meeting> putMeeting(@PathVariable String meetingId, @RequestBody Meeting meeting, HttpServletRequest request) {
        HttpEntity<Meeting> requestObject = new HttpEntity<>(meeting, UtilityFunctions.getUserDetailsFromRequest(request));
        ResponseEntity<Meeting> meetingResponseEntity = (ResponseEntity<Meeting>) this.restTemplate.exchange("http://katchup-service/meetings/" + meetingId, HttpMethod.PUT, requestObject, Meeting.class);
        return meetingResponseEntity;
    }

    @DeleteMapping("/meetings/{meetingId}")
    public ResponseEntity<Meeting> deleteMeeting(@PathVariable String meetingId, HttpServletRequest request) {
        HttpEntity<String> requestObject = new HttpEntity<>(UtilityFunctions.getUserDetailsFromRequest(request));
        ResponseEntity<Meeting> optionalResponseEntity = (ResponseEntity) this.restTemplate.exchange("http://katchup-service/meetings/" + meetingId, HttpMethod.DELETE, requestObject, Meeting.class);
        return optionalResponseEntity;

    }
}
