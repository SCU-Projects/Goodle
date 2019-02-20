package com.app.katchup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.app.katchup")
public class KatchupApplication {

	public static void main(String[] args) {
		SpringApplication.run(KatchupApplication.class, args);
	}
}
