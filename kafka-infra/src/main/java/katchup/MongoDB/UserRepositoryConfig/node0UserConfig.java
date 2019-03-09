package katchup.MongoDB.UserRepositoryConfig;


import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "katchup.Users.repository.node0",
                         mongoTemplateRef = "node0UserMongoTemplate")
public class node0UserConfig extends AbstractMongoConfiguration {
    @Value( "${mongodb.node0.host}" )
    String host;

    @Value("${mongodb.node0.port}")
    int port;

    @Value( "${mongodb.database}")
    String db;

    @Bean(name = "node0UserMongoTemplate")
    public MongoTemplate node0UserMongoTemplate() throws Exception {
        return new MongoTemplate(node0UserMongoFactory());
    }

    @Bean(name = "node0UserMongoFactory")
    public MongoDbFactory node0UserMongoFactory() throws Exception {
        //return new SimpleMongoDbFactory(new MongoClient(host, port), db);
        return new SimpleMongoDbFactory(mongoClient(), db);
    }

    @Override
    public MongoClient mongoClient() {
        return new MongoClient(host, port);
    }

    @Override
    protected String getDatabaseName() {
        return db;
    }

//    @Override
//    protected String getMappingBasePackage() {
//        return "katchup.Users.repository.node0";
//    }
}