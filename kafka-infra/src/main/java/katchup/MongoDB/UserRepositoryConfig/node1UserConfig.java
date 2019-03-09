package katchup.MongoDB.UserRepositoryConfig;

import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "katchup.Users.repository.node1",
                         mongoTemplateRef = "node1UserMongoTemplate")
public class node1UserConfig {

    @Value( "${mongodb.node1.host}" )
    String host;

    @Value("${mongodb.node1.port}")
    int port;

    @Value( "${mongodb.database}" )
    String db;

    @Bean(name = "node1UserMongoTemplate")
    public MongoTemplate node1UserMongoTemplate() throws Exception {
        return new MongoTemplate(node1UserMongoFactory());
    }

    @Bean(name = "node1UserMongoFactory")
    public MongoDbFactory node1UserMongoFactory() throws Exception {
        return new SimpleMongoDbFactory(new MongoClient(host, port), db);
    }

//    @Override
//    public MongoClient mongoClient() {
//        return new MongoClient(host, port);
//    }
//
//    @Override
//    protected String getDatabaseName() {
//        return db;
//    }
}