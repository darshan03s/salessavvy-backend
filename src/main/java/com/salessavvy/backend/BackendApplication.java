package com.salessavvy.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {
		System.out.println("Current Environment: " + System.getenv("ENV"));
		SpringApplication.run(BackendApplication.class, args);
	}
}
