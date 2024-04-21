package com.alo.digital.transportes.webapp.efact;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
//@EnableScheduling
public class EfactApplication {

	public static void main(String[] args) {
		SpringApplication.run(EfactApplication.class, args);
	}

}
