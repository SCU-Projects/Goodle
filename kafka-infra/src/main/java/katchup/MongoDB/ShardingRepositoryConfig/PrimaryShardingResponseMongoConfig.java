package katchup.MongoDB.ShardingRepositoryConfig;

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
public class PrimaryShardingResponseMongoConfig {


    @Bean(name = "primaryShardingResponseMongoTemplate")
    public MongoTemplate primaryMongoTemplate() throws Exception {
        return new MongoTemplate(primaryFactory());
    }


    @Bean(name = "primaryShardingMongoFactory")
    public MongoDbFactory primaryFactory() throws Exception {
        return new SimpleMongoDbFactory(new MongoClient("localhost",27017),"KatchUpDatabase");
    }
}
