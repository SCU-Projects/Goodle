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

    @Bean(name = "node0MeetingMongoTemplate")
    public MongoTemplate node0MeetingMongoTemplate() throws Exception {
        return new MongoTemplate(node0MeetingFactory(this.mongoProperties.getNode0()));
    }
    @Bean(name = "node1MeetingMongoTemplate")
    public MongoTemplate node1MeetingMongoTemplate() throws Exception {
        return new MongoTemplate(node1MeetingFactory(this.mongoProperties.getNode1()));
    }
    @Bean(name = "node2MeetingMongoTemplate")
    public MongoTemplate node2MeetingMongoTemplate() throws Exception {
        return new MongoTemplate(node2MeetingFactory(this.mongoProperties.getNode2()));
    }

    @Bean(name = "node0MeetingResponseMongoTemplate")
    public MongoTemplate node0MeetingResponseMongoTemplate() throws Exception {
        return new MongoTemplate(node0MeetingResponseFactory(this.mongoProperties.getNode0()));
    }
    @Bean(name = "node1MeetingResponseMongoTemplate")
    public MongoTemplate node1MeetingResponseMongoTemplate() throws Exception {
        return new MongoTemplate(node1MeetingResponseFactory(this.mongoProperties.getNode1()));
    }
    @Bean(name = "node2MeetingResponseMongoTemplate")
    public MongoTemplate node2MeetingResponseMongoTemplate() throws Exception {
        return new MongoTemplate(node2MeetingResponseFactory(this.mongoProperties.getNode2()));
    }

    @Bean(name = "node0ShardingMongoTemplate")
    public MongoTemplate node0ShardingMongoTemplate() throws Exception {
        return new MongoTemplate(node0ShardingFactory(this.mongoProperties.getNode0()));
    }
    @Bean(name = "node1ShardingMongoTemplate")
    public MongoTemplate node1ShardingMongoTemplate() throws Exception {
        return new MongoTemplate(node1ShardingFactory(this.mongoProperties.getNode1()));
    }
    @Bean(name = "node2ShardingMongoTemplate")
    public MongoTemplate node2ShardingMongoTemplate() throws Exception {
        return new MongoTemplate(node2ShardingFactory(this.mongoProperties.getNode2()));
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

    @Bean
    public MongoDbFactory node0MeetingFactory(final MongoProperties mongo) throws Exception {
        return new SimpleMongoDbFactory(new MongoClient(mongo.getHost(), mongo.getPort()),
                mongo.getDatabase());
    }
    @Bean
    public MongoDbFactory node1MeetingFactory(final MongoProperties mongo) throws Exception {
        return new SimpleMongoDbFactory(new MongoClient(mongo.getHost(), mongo.getPort()),
                mongo.getDatabase());
    }

    @Bean
    public MongoDbFactory node2MeetingFactory(final MongoProperties mongo) throws Exception {
        return new SimpleMongoDbFactory(new MongoClient(mongo.getHost(), mongo.getPort()),
                mongo.getDatabase());
    }

    @Bean
    public MongoDbFactory node0MeetingResponseFactory(final MongoProperties mongo) throws Exception {
        return new SimpleMongoDbFactory(new MongoClient(mongo.getHost(), mongo.getPort()),
                mongo.getDatabase());
    }
    @Bean
    public MongoDbFactory node1MeetingResponseFactory(final MongoProperties mongo) throws Exception {
        return new SimpleMongoDbFactory(new MongoClient(mongo.getHost(), mongo.getPort()),
                mongo.getDatabase());
    }

    @Bean
    public MongoDbFactory node2MeetingResponseFactory(final MongoProperties mongo) throws Exception {
        return new SimpleMongoDbFactory(new MongoClient(mongo.getHost(), mongo.getPort()),
                mongo.getDatabase());
    }

    @Bean
    public MongoDbFactory node0ShardingFactory(final MongoProperties mongo) throws Exception {
        return new SimpleMongoDbFactory(new MongoClient(mongo.getHost(), mongo.getPort()),
                mongo.getDatabase());
    }
    @Bean
    public MongoDbFactory node1ShardingFactory(final MongoProperties mongo) throws Exception {
        return new SimpleMongoDbFactory(new MongoClient(mongo.getHost(), mongo.getPort()),
                mongo.getDatabase());
    }

    @Bean
    public MongoDbFactory node2ShardingFactory(final MongoProperties mongo) throws Exception {
        return new SimpleMongoDbFactory(new MongoClient(mongo.getHost(), mongo.getPort()),
                mongo.getDatabase());
    }

}
