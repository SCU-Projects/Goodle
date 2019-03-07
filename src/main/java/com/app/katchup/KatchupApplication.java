package com.app.katchup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@SpringBootApplication(scanBasePackages={"com.app.katchup"})
@EnableMongoRepositories
		//exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})//
//@EnableMongoRepositories(basePackageClasses = {UserRepository.class})
//, MeetingResponseNode1Repository.class, MeetingRepository.class,
												//MeetingResponseRepository.class})
public class KatchupApplication {

	public static void main(String[] args) {
		SpringApplication.run(KatchupApplication.class, args);
	}
}
