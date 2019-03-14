package katchup.config.meetingResponse;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "katchup.MeetingResponse.repository.node0",
        mongoTemplateRef = "node0MeetingResponseMongoTemplate")
public class node0MeetingResponseMongoConfig {
}
