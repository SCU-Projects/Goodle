package katchup.config.user;


import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "katchup.Users.repository.node0",
        mongoTemplateRef = "node0UserMongoTemplate")
public class node0UserMongoConfig {
}
