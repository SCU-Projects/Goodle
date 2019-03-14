package katchup.config.meeting;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "katchup.Meeting.repository.node1",
        mongoTemplateRef = "node1MeetingMongoTemplate")
public class node1MeetingMongoConfig {
}
