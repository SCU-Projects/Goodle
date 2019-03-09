package katchup;

import katchup.Meeting.repository.primary.MeetingRepository;
import katchup.MeetingResponse.repository.node1.MeetingResponseNode1Repository;
import katchup.MeetingResponse.repository.primary.MeetingResponseRepository;
import katchup.Users.repository.node0.UserNode0Repository;
import katchup.Users.repository.node1.UserNode1Repository;
import katchup.Users.repository.node2.UserNode2Repository;
import katchup.Users.repository.primary.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@SpringBootApplication(scanBasePackages={"katchup"} )//, exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
//@EnableMongoRepositories
//@EnableMongoRepositories(basePackageClasses = {UserNode0Repository.class, UserNode1Repository.class, UserNode2Repository.class})
//		UserNode2Repository.class, MeetingResponseNode1Repository.class, MeetingRepository.class, MeetingResponseRepository.class})
public class KatchupApplication {

	public static void main(String[] args) {
		SpringApplication.run(KatchupApplication.class, args);
	}
}
