package katchup.config.meeting;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "katchup.Meeting.repository.node2",
        mongoTemplateRef = "node2MeetingMongoTemplate")
public class node2MeetingMongoConfig {
}
