package katchup.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "katchup.Users.repository.node2",
        mongoTemplateRef = "node2MongoTemplate")
public class node2MongoConfig {
}
