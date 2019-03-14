package katchup.config.meetingResponse;


import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "katchup.MeetingResponse.repository.node2",
        mongoTemplateRef = "node2MeetingResponseMongoTemplate")
public class node2MeetingResponseMongoConfig {
}
