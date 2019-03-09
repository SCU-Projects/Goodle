package katchup.MongoDB.MeetingConfig;

import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "katchup.Meeting.repository.node1",
                         mongoTemplateRef = "node1MeetingMongoTemplate")
public class node1MeetingConfig {

    @Value( "${mongodb.node1.host}" )
    String host;

    @Value("${mongodb.node1.port}")
    int port;

    @Value( "${mongodb.database}" )
    String db;
    @Bean(name = "node1MeetingMongoTemplate")
    public MongoTemplate node1MongoTemplate() throws Exception {
        return new MongoTemplate(node1Factory());
    }

    @Bean(name = "node1MeetingMongoFactory")
    public MongoDbFactory node1Factory() throws Exception {
        return new SimpleMongoDbFactory(new MongoClient(host,port),db);
    }

}