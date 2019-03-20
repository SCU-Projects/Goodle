package com.app.feign.client.feignclient.Meeting;


import katchup.Meeting.model.Meeting;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@FeignClient(name = "katchup-service")
public interface MeetingFeignClientProxy {

    @PostMapping("/meeting/create")
    ResponseEntity<Meeting> postMeeting(@RequestBody Meeting meeting, HttpServletRequest request);

    @GetMapping("/meetings/{meetingId}/details")
    ResponseEntity<Optional<Meeting>> getMeetingDetails(@PathVariable String meetingId, HttpServletRequest request);

    @PutMapping("meetings/{meetingId}")
    ResponseEntity<Meeting> putMeeting(@PathVariable String meetingId, @RequestBody Meeting meeting, HttpServletRequest request);

    @DeleteMapping("/meetings/{meetingId}")
    ResponseEntity<Meeting> deleteMeeting(@PathVariable String meetingId, HttpServletRequest request);

    }
