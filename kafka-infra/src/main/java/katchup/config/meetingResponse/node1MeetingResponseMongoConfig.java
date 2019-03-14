package katchup.config.meetingResponse;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "katchup.MeetingResponse.repository.node1",
        mongoTemplateRef = "node1MeetingResponseMongoTemplate")
public class node1MeetingResponseMongoConfig {
}
