package com.zy.springintegrationmqtttest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zy.springintegrationmqtttest.config.IntegrationMqttConfig.MqttOutGateway;
import com.zy.springintegrationmqtttest.model.MessageModel;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringIntegrationMqttTestApplicationTest {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private MqttOutGateway mqttOutGateway;
	
	@Autowired
	private ObjectMapper mqttObjectMapper;
	
	@Test
	public void test() throws Exception{
		this.logger.info("test start....");
		
		//发送消息
		MessageModel messageModel = new MessageModel("1", "zy");
		this.mqttOutGateway.sendToMqtt(mqttObjectMapper.writeValueAsString(messageModel));
//		this.logger.info("已收到响应:{}", reply);
		
		this.logger.info("test end....");
	}

}
