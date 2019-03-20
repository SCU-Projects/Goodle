package com.techprimers.kafka.springbootkafkaconsumerexample;

import katchup.Meeting.MeetingService;
import katchup.Sharding.ShardingService;
import katchup.Sharding.repository.node0.ShardingNode0Repository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication()
public class SpringBootKafkaConsumerExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootKafkaConsumerExampleApplication.class, args);
    }
}
