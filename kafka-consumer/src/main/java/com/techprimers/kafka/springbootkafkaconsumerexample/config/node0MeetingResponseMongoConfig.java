package com.techprimers.kafka.springbootkafkaconsumerexample.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.techprimers.kafka.springbootkafkaconsumerexample.repository.node0",
        mongoTemplateRef = "node0MeetingResponseMongoTemplate")
public class node0MeetingResponseMongoConfig {
}
