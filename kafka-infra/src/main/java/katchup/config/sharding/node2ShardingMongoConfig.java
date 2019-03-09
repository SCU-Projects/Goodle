package katchup.config.sharding;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "katchup.Sharding.repository.node2",
        mongoTemplateRef = "node2ShardingMongoTemplate")
public class node2ShardingMongoConfig {
}
