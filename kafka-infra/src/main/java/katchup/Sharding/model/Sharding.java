package katchup.Sharding.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "Sharding")
public class Sharding {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    String shardingId;

    @NotNull
    String meetingId;

    @NotNull
    Integer databaseId;
}
