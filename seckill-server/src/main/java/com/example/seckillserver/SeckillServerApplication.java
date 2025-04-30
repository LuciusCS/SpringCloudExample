package com.example.seckillserver;
import com.alibaba.csp.sentinel.init.InitFunc;
import com.alibaba.csp.sentinel.init.InitExecutor;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class SeckillServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SeckillServerApplication.class, args);
	}

	@PostConstruct
	public void initSentinel() {
		InitExecutor.doInit();
	}

}
