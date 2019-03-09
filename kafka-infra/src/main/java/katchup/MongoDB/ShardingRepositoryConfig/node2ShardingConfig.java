package katchup.MongoDB.ShardingRepositoryConfig;

import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "katchup.Sharding.repository.node2",
        mongoTemplateRef = "node2ShardingMongoTemplate")
public class node2ShardingConfig {

    @Value( "${mongodb.node1.host}" )
    String host;

    @Value("${mongodb.node1.port}")
    int port;

    @Value( "${mongodb.database}" )
    String db;
    @Bean(name = "node2ShardingMongoTemplate")
    public MongoTemplate node2MongoTemplate() throws Exception {
        return new MongoTemplate(node2Factory());
    }

    @Bean(name = "node2ShardingMongoFactory")
    public MongoDbFactory node2Factory() throws Exception {
        return new SimpleMongoDbFactory(new MongoClient(host,port),db);
    }

}
