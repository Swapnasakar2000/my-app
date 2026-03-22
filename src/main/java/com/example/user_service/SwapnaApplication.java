package com.example.user_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SwapnaApplication {

	public static void main(String[] args) {
		SpringApplication.run(SwapnaApplication.class, args);
	}

}
