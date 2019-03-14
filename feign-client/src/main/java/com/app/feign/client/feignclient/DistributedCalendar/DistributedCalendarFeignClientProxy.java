package com.app.feign.client.feignclient.DistributedCalendar;

import katchup.DistributedCalendar.model.DistributedCalendar;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;

@FeignClient(name = "katchup-service")
public interface DistributedCalendarFeignClientProxy {

    @GetMapping("/{userName}")
    public ResponseEntity<DistributedCalendar> showUserEvents(@PathVariable String userName, HttpServletRequest request);

    }
