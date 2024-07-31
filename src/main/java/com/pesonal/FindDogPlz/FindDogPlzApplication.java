package com.pesonal.FindDogPlz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class FindDogPlzApplication {

	public static void main(String[] args) {
		SpringApplication.run(FindDogPlzApplication.class, args);
	}

}
