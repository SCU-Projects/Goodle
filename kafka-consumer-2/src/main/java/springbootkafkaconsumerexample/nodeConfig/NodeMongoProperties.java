package springbootkafkaconsumerexample.nodeConfig;

import lombok.Data;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "mongodb")
public class NodeMongoProperties {
    private MongoProperties node2 = new MongoProperties();
}
