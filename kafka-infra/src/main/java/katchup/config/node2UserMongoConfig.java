package katchup.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "katchup.Users.repository.node2",
        mongoTemplateRef = "node2UserMongoTemplate")
public class node2UserMongoConfig {
}
