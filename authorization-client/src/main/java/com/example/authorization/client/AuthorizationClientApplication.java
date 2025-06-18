package com.example.authorization.client;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.core.env.Environment;

import java.util.Arrays;


@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class AuthorizationClientApplication {


	@Autowired
	private static Environment env;

	public static void main(String[] args) {
		SpringApplication.run(AuthorizationClientApplication.class, args);
		check();
	}

	@PostConstruct
	public static void check() {
		System.out.println("Active profiles: " + Arrays.toString(env.getActiveProfiles()));
		System.out.println("Nacos discovery addr: " + env.getProperty("spring.cloud.nacos.discovery.server-addr"));
	}

}
