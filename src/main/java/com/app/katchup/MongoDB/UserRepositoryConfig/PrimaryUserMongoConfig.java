package com.app.katchup.MongoDB.UserRepositoryConfig;

import com.mongodb.MongoClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;


@Configuration
@RequiredArgsConstructor
public class PrimaryUserMongoConfig {

    @Primary
    @Bean(name = "primaryUserMongoTemplate")
    public MongoTemplate primaryUserMongoTemplate() throws Exception {
        return new MongoTemplate(primaryFactory());
    }

    @Primary
    @Bean
    public MongoDbFactory primaryFactory() throws Exception {
        return new SimpleMongoDbFactory(new MongoClient("localhost",27017),"KatchUpDatabase");
    }
}
