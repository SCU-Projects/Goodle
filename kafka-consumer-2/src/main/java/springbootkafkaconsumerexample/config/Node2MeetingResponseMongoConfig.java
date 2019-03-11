package springbootkafkaconsumerexample.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "springbootkafkaconsumerexample.repository.node2",
                                                mongoTemplateRef = "node2MeetingResponseMongoTemplate")
public class Node2MeetingResponseMongoConfig {
}
