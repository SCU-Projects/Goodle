package katchup.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "katchup.Users.repository.node0",
        mongoTemplateRef = "node0MongoTemplate")
public class node0MongoConfig {
}
