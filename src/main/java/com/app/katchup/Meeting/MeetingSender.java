package com.app.katchup.Meeting;

import com.app.katchup.Meeting.model.Meeting;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.HashMap;
import java.util.Map;

@Service
public class MeetingSender {

    private  final Logger logger = LogManager.getLogger(MeetingSender.class);

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private static final String TOPIC = "here";

    public void sendData(Meeting meetingObj) {
         Map<String, Object> headers = new HashMap<>();
         headers.put(KafkaHeaders.TOPIC, TOPIC);
        ListenableFuture<SendResult<String, String>> result = kafkaTemplate.send(new GenericMessage<Meeting>(meetingObj, headers));
        System.out.println("Data - " + meetingObj.toString() + " sent to Kafka Topic - " + "test");
        logger.info("Data - " + meetingObj.toString() + " sent to Kafka Topic - \" + \"test \n + Res"+result);
    }
}
