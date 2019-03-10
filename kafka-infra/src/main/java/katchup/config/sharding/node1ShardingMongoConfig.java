package katchup.config.sharding;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "katchup.Sharding.repository.node1",
        mongoTemplateRef = "node1ShardingMongoTemplate")
public class node1ShardingMongoConfig {
}
