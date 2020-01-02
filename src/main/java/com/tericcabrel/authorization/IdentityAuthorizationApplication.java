package com.tericcabrel.authorization;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class IdentityAuthorizationApplication {

	public static void main(String[] args) {
		SpringApplication.run(IdentityAuthorizationApplication.class, args);
	}

}
