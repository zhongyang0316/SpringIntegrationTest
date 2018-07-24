package com.zy.springintegrationkafkatest;

import java.util.Collections;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.PollableChannel;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.context.junit4.SpringRunner;

import com.zy.springintegrationkafkatest.config.IntegrationKafkaProperties;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringIntegrationKafkaTestApplicationTests {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private IntegrationKafkaProperties integrationKafkaProperties;
	
	@Autowired
	private MessageChannel toKafkaChannel;
	
	@Autowired
	private PollableChannel fromKafkaChannel;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void contextLoads() {
		this.logger.info("只是测试。。。。");
		Map<String, String> headers = Collections.singletonMap(KafkaHeaders.TOPIC, this.integrationKafkaProperties.getTopic());
		for (int i = 0; i < 10; i++) {
			toKafkaChannel.send(new GenericMessage("foo" + i, headers));
		}
		
		Message<?> received = fromKafkaChannel.receive(10000);
		int count = 0;
		while (received != null) {
			this.logger.info(received.toString());
			received = fromKafkaChannel.receive(++count < 11 ? 10000 : 1000);
		}
		this.logger.info("测试完毕。。。。");
	}

}
