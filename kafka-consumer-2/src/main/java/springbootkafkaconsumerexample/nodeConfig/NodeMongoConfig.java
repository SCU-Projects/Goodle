package springbootkafkaconsumerexample.nodeConfig;


import com.mongodb.MongoClient;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(NodeMongoProperties.class)
public class NodeMongoConfig {
    private final NodeMongoProperties mongoProperties;

    @Bean
    public MongoDbFactory node2MeetingResponseFactory(final NodeMongoProperties mongo) throws Exception {
        return new SimpleMongoDbFactory(new MongoClient(mongo.getNode2().getHost(), mongo.getNode2().getPort()),
                mongo.getNode2().getDatabase());
    }

    @Bean(name = "node2MeetingResponseMongoTemplate")
    public MongoTemplate node2MeetingResponseMongoTemplate() throws Exception {
        return new MongoTemplate(node2MeetingResponseFactory(this.mongoProperties));
    }
}
