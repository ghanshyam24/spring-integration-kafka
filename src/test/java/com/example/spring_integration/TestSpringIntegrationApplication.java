package com.example.spring_integration;

import org.springframework.boot.SpringApplication;

public class TestSpringIntegrationApplication {

	public static void main(String[] args) {
		SpringApplication.from(SpringIntegrationApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
