package com.example.authorization.client;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.servlet.OAuth2ClientAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.core.env.Environment;

import java.util.Arrays;


@SpringBootApplication(exclude = {
		SecurityAutoConfiguration.class,
		OAuth2ClientAutoConfiguration.class})
@EnableDiscoveryClient
@EnableFeignClients
public class AuthorizationClientApplication {


	public static void main(String[] args) {
		SpringApplication.run(AuthorizationClientApplication.class, args);

	}



}
