package com.zy.springintegrationkafkatest.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "com.zy.kafka")
public class IntegrationKafkaProperties {
	
	private String topic;
	
	private String messageKey;

}
