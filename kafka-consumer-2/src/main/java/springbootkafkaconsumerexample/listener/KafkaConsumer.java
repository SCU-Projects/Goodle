package springbootkafkaconsumerexample.listener;

import katchup.Exception.NotAcceptableException;
import katchup.MeetingResponse.model.MeetingInboxResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import springbootkafkaconsumerexample.model.User;
import springbootkafkaconsumerexample.repository.node2.Node2Repository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static katchup.Sharding.Utilities.getShardedDBLocation;

@Service
public class KafkaConsumer {

    private static final Logger logger = LogManager.getLogger(KafkaConsumer.class);

    @Autowired
    Node2Repository node2Repository;

    @KafkaListener(topics = "example", groupId = "group_id-2")
    public void consume(String message) {
        System.out.println("Consumed message: " + message);
    }

    @KafkaListener(topics = "MEETING-CREATION-FROM-INFRA", groupId = "group_json-2",
            containerFactory = "userKafkaListenerFactory")
    public void consumeJson(User user) {
        System.out.println("Consumed JSON Message: " + user);
        LocalDateTime time = LocalDateTime.now();
        logger.info(String.format("Starting to save: %s", time));
        saveToTable(user.toString());
        logger.info(String.format("Ending to save: %s", LocalDateTime.now()));
        Duration duration = Duration.between(time, LocalDateTime.now());
        logger.info(String.format("Took:%d", duration.getNano()));
    }

    private void saveToTable(String inputString){
        List<String> inviteesList = getInvitees(inputString);
        String meetingId = getMeetingId(inputString);

        List<MeetingInboxResponse> meetingInvites = new ArrayList<>();
        meetingInvites = inviteesList
                            .stream()
                            .filter(invitee -> getShardedDBLocation(invitee).ordinal()  == 2)
                            .map(invitee -> {
                                MeetingInboxResponse meetingInvite = new MeetingInboxResponse();
                                meetingInvite.setMeetingId(meetingId);
                                meetingInvite.setUserName(invitee);
                                return meetingInvite;
                            })
                            .collect(Collectors.toList());
        node2Repository.saveAll(meetingInvites);
        logger.info("Invitee list saved successfully in DB-2");
    }

    private List<String> getInvitees(String inputString){
        String pattern = "\\[(.+)\\]:(.*)";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(inputString);

        if (m.find()) {
            List<String> invitees = Arrays.asList(m.group(1).split(","));
            logger.info("Invitee list parsed successfully in Kafka");
            return invitees;
        }
        logger.error("Error parsing invitee list in Kafka");
        throw new NotAcceptableException("Error parsing invitee list in Kafka");

    }

    private String getMeetingId(String inputString){
        String pattern = "\\[(.+)\\]:(.*)";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(inputString);
        m.find();
        return m.group(2);
    }




}
