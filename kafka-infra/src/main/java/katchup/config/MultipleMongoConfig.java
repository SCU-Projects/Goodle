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

    @Bean(name = "node0UserMongoTemplate")
    public MongoTemplate node0UserMongoTemplate() throws Exception {
        return new MongoTemplate(node0UserFactory(this.mongoProperties.getNode0()));
    }
    @Bean(name = "node1UserMongoTemplate")
    public MongoTemplate node1UserMongoTemplate() throws Exception {
        return new MongoTemplate(node1UserFactory(this.mongoProperties.getNode1()));
    }
    @Bean(name = "node2UserMongoTemplate")
    public MongoTemplate node2UserMongoTemplate() throws Exception {
        return new MongoTemplate(node2UserFactory(this.mongoProperties.getNode2()));
    }

    @Bean
    public MongoDbFactory node0UserFactory(final MongoProperties mongo) throws Exception {
        return new SimpleMongoDbFactory(new MongoClient(mongo.getHost(), mongo.getPort()),
                mongo.getDatabase());
    }
    @Bean
    public MongoDbFactory node1UserFactory(final MongoProperties mongo) throws Exception {
        return new SimpleMongoDbFactory(new MongoClient(mongo.getHost(), mongo.getPort()),
                mongo.getDatabase());
    }

    @Bean
    public MongoDbFactory node2UserFactory(final MongoProperties mongo) throws Exception {
        return new SimpleMongoDbFactory(new MongoClient(mongo.getHost(), mongo.getPort()),
                mongo.getDatabase());
    }
}
