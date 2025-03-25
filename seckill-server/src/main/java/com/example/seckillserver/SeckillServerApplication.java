package com.example.seckillserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class SeckillServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SeckillServerApplication.class, args);
	}

}
