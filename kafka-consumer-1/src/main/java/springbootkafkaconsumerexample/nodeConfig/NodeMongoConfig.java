package springbootkafkaconsumerexample.nodeConfig;


import com.mongodb.MongoClient;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
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
    public MongoDbFactory node1MeetingResponseFactory(final NodeMongoProperties mongo) throws Exception {
        return new SimpleMongoDbFactory(new MongoClient(mongo.getNode1().getHost(), mongo.getNode1().getPort()),
                mongo.getNode1().getDatabase());
    }

    @Bean(name = "node1MeetingResponseMongoTemplate")
    public MongoTemplate node1MeetingResponseMongoTemplate() throws Exception {
        return new MongoTemplate(node1MeetingResponseFactory(this.mongoProperties));
    }
}
