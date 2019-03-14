package com.app.feign.client.feignclient.MeetingResponse;

import katchup.MeetingResponse.model.*;
import com.app.feign.client.feignclient.util.UtilityFunctions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class MeetingResponseFeignClientController {

    @Autowired
    RestTemplate restTemplate;

    @PostMapping("/feign/inbox")
    public ResponseEntity<MeetingInboxResponse> postInbox(@RequestBody MeetingInboxResponse meetingInboxObject, HttpServletRequest request) {
        HttpEntity<MeetingInboxResponse> requestObject = new HttpEntity<>(meetingInboxObject, UtilityFunctions.getUserDetailsFromRequest(request));
        ResponseEntity<MeetingInboxResponse> meetingInboxResponseEntity = this.restTemplate.exchange("http://katchup-service/inbox", HttpMethod.POST, requestObject, MeetingInboxResponse.class);
        return meetingInboxResponseEntity;
    }

    @GetMapping("/feign/inbox")
    public ResponseEntity<List<Inbox>> getInboxForUserName(HttpServletRequest request) {
        HttpEntity<String> requestObject = new HttpEntity<>( UtilityFunctions.getUserDetailsFromRequest(request));
        ResponseEntity<List<Inbox>> meetingInboxResponseEntity = (ResponseEntity) this.restTemplate.exchange("http://katchup-service/inbox", HttpMethod.GET, requestObject, MeetingInboxResponse.class);
        return meetingInboxResponseEntity;
    }

    @GetMapping("/feign/meetings/{meetingId}/response")
    public ResponseEntity<MeetingInboxResponse> getMeetingResponseForUser(@PathVariable String meetingId, HttpServletRequest request) {
        HttpEntity<String> requestObject = new HttpEntity<>(UtilityFunctions.getUserDetailsFromRequest(request));
        ResponseEntity<MeetingInboxResponse> meetingInboxResponseEntity = this.restTemplate.exchange("http://katchup-service/meetings/" + meetingId + "/response", HttpMethod.GET, requestObject, MeetingInboxResponse.class);
        return meetingInboxResponseEntity;
    }

    @GetMapping("/feign/meetings/{meetingId}/stats")
    public ResponseEntity<MeetingStats> getMeetingStatsForMeeting(@PathVariable String meetingId, HttpServletRequest request) {
        HttpEntity<String> requestObject = new HttpEntity<>(UtilityFunctions.getUserDetailsFromRequest(request));
        ResponseEntity<MeetingStats> meetingStatsResponseEntity = this.restTemplate.exchange("http://katchup-service/meetings/" + meetingId + "/stats", HttpMethod.GET, requestObject, MeetingStats.class);
        return meetingStatsResponseEntity;
    }

    @PutMapping("/feign/meetings/{meetingId}/response")
    public ResponseEntity<Decision> putUserDecisionForMeetingInviteResponse(@PathVariable String meetingId, HttpServletRequest request, @RequestBody MeetingRequestBody requestBody) {
        HttpEntity<MeetingRequestBody> requestObject = new HttpEntity<>(requestBody, UtilityFunctions.getUserDetailsFromRequest(request));
        ResponseEntity<Decision> decisionResponseEntity = this.restTemplate.exchange("http://katchup-service/meetings/" + meetingId + "/response", HttpMethod.PUT, requestObject, Decision.class);
        return decisionResponseEntity;
    }

}
