package katchup.config.meeting;


import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "katchup.Meeting.repository.node0",
        mongoTemplateRef = "node0MeetingMongoTemplate")
public class node0MeetingMongoConfig {
}
