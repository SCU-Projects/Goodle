package katchup.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "katchup.Users.repository.node1",
        mongoTemplateRef = "node1MongoTemplate")
public class node1MongoConfig {
}
