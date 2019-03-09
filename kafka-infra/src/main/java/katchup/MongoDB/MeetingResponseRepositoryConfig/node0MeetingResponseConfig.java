package katchup.MongoDB.MeetingResponseRepositoryConfig;


import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "katchup.MeetingResponse.repository.node0",
                                    mongoTemplateRef = "node0MeetingResponseMongoTemplate")
public class node0MeetingResponseConfig {
    @Value( "${mongodb.node0.host}" )
    String host;

    @Value("${mongodb.node0.port}")
    int port;

    @Value( "${mongodb.database}")
    String db;

    @Bean(name = "node0MeetingResponseMongoTemplate")
    public MongoTemplate node0MongoTemplate() throws Exception {
        return new MongoTemplate(node0Factory());
    }

    @Bean(name = "node0MeetingResponseMongoFactory")
    public MongoDbFactory node0Factory() throws Exception {
        return new SimpleMongoDbFactory(new MongoClient(host, port), db);
    }
}