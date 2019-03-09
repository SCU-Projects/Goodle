package katchup.config.sharding;


import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "katchup.Sharding.repository.node0",
        mongoTemplateRef = "node0ShardingMongoTemplate")
public class node0ShardingMongoConfig {
}
