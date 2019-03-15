package katchup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Configuration;

@Configuration
@SpringBootApplication(scanBasePackages = {"katchup"})
@EnableEurekaClient
public class KatchupApplication {
    public static void main(String[] args) {
        SpringApplication.run(KatchupApplication.class, args);
    }
}
