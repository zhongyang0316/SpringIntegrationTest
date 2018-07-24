package com.zy.springintegrationredistest.endpoint;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zy.springintegrationredistest.model.PubsubMessage;

@MessageEndpoint
public class RedisPubsubActivator {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private ObjectMapper redisObjectMapper;
	
	@ServiceActivator(inputChannel = "redisPubsubChannel")
	public void redisPubsubProcess(String pubsubMessage){
		this.logger.info("接收Redis广播消息:{}", pubsubMessage);
		try {
			PubsubMessage message = this.redisObjectMapper.readValue(pubsubMessage, PubsubMessage.class);
			this.logger.info("PubsubMessag解析:{}", message);
		} catch (Exception e) {
			this.logger.error("解析失败");
		}
	}

}
