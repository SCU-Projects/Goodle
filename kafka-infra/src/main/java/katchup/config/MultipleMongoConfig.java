package katchup.config;


import com.mongodb.MongoClient;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(MultipleMongoProperties.class)
public class MultipleMongoConfig {
    private final MultipleMongoProperties mongoProperties;
    
    @Bean(name = "node0MongoTemplate")
    public MongoTemplate node0MongoTemplate() throws Exception {
        return new MongoTemplate(node0Factory(this.mongoProperties.getNode0()));
    }
    @Bean(name = "node1MongoTemplate")
    public MongoTemplate node1MongoTemplate() throws Exception {
        return new MongoTemplate(node1Factory(this.mongoProperties.getNode1()));
    }
    @Bean(name = "node2MongoTemplate")
    public MongoTemplate node2MongoTemplate() throws Exception {
        return new MongoTemplate(node2Factory(this.mongoProperties.getNode2()));
    }

    @Bean
    public MongoDbFactory node0Factory(final MongoProperties mongo) throws Exception {
        return new SimpleMongoDbFactory(new MongoClient(mongo.getHost(), mongo.getPort()),
                mongo.getDatabase());
    }
    @Bean
    public MongoDbFactory node1Factory(final MongoProperties mongo) throws Exception {
        return new SimpleMongoDbFactory(new MongoClient(mongo.getHost(), mongo.getPort()),
                mongo.getDatabase());
    }

    @Bean
    public MongoDbFactory node2Factory(final MongoProperties mongo) throws Exception {
        return new SimpleMongoDbFactory(new MongoClient(mongo.getHost(), mongo.getPort()),
                mongo.getDatabase());
    }
}
