package com.app.katchup.MongoDB.UserRepositoryConfig;

import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.app.katchup.Meeting.repository.node2",
        mongoTemplateRef = "node2UserMongoTemplate")

public class node2UserConfig {

    @Value( "${mongodb.node2.host}" )
    String host;

    @Value("${mongodb.node2.port}")
    int port;

    @Value( "${mongodb.database}" )
    String db;

    @Bean(name = "node2UserMongoTemplate")
    public MongoTemplate node2MongoTemplate() throws Exception {
        return new MongoTemplate(node2Factory());
    }

    @Bean(name = "node2UserMongoFactory")
    public MongoDbFactory node2Factory() throws Exception {
        return new SimpleMongoDbFactory(new MongoClient(host, port),db);
    }
}