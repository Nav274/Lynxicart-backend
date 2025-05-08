package com.kodnest.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
@ComponentScan(basePackages = { "com.kodnest.project", "entities", "controllers", "repositories", "services", "enums", "admincontrollers", "adminservices" })
@EnableJpaRepositories(basePackages = "repositories")
@EntityScan(basePackages = "entities")
public class LynxiCartApplication {

	public static void main(String[] args) {

		SpringApplication.run(LynxiCartApplication.class, args);

	}
	
}
