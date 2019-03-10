package katchup.config;


import lombok.Data;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "mongodb")
public class MultipleMongoProperties {
    private MongoProperties node0 = new MongoProperties();
    private MongoProperties node1 = new MongoProperties();
    private MongoProperties node2 = new MongoProperties();

}
