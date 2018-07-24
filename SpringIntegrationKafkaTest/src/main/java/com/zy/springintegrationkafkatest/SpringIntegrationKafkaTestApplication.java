package com.zy.springintegrationkafkatest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

@SpringBootApplication(scanBasePackages = {"com.zy.springintegrationkafkatest"})
public class SpringIntegrationKafkaTestApplication {
	
	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(SpringIntegrationKafkaTestApplication.class, args);
		System.out.println(ctx.getId() + " started!");
	}
	
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(SpringIntegrationKafkaTestApplication.class);
	}

}
