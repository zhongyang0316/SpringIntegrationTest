package com.zy.springintegrationmqtttest.endpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zy.springintegrationmqtttest.model.MessageModel;

@MessageEndpoint
public class InputMessageActivator {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private ObjectMapper mqttObjectMapper;
	
	@ServiceActivator(inputChannel = "mqttInputChannel")
	public void inputMessageProcess(String message){
		this.logger.info("接收mqtt消息:{}", message);
		try {
			MessageModel messageModel = this.mqttObjectMapper.readValue(message, MessageModel.class);
			this.logger.info("MessageModel:{}", messageModel);
		} catch (Exception e) {
			this.logger.error("Serlize MessageModel error:{}", e);
		}
//		return "已收到";
	}

}
