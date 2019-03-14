package com.app.feign.client.feignclient.MeetingResponse;

import katchup.MeetingResponse.model.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@FeignClient(name = "katchup-service")
public interface MeetingResponseFeignClientProxy {

    @PostMapping("/inbox")
    public ResponseEntity<MeetingInboxResponse> postInbox(@RequestBody MeetingInboxResponse meetingInboxObject, HttpServletRequest request);

    @GetMapping("/inbox")
    public ResponseEntity<List<Inbox>> getInboxForUserName(HttpServletRequest request);

    @GetMapping("/meetings/{meetingId}/response")
    public ResponseEntity<MeetingInboxResponse> getMeetingResponseForUser(@PathVariable String meetingId, HttpServletRequest request);
    @GetMapping("/meetings/{meetingId}/stats")
    public ResponseEntity<MeetingStats> getMeetingStatsForMeeting(@PathVariable String meetingId, HttpServletRequest request);

    @PutMapping("/meetings/{meetingId}/response")
    public ResponseEntity<Decision> putUserDecisionForMeetingInviteResponse(@PathVariable String meetingId, HttpServletRequest request, @RequestBody MeetingRequestBody requestBody);


}
