package com.zy.springintegrationredistest;

import java.math.BigDecimal;
import java.util.concurrent.locks.Lock;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.integration.redis.util.RedisLockRegistry;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.zy.springintegrationredistest.model.PubsubMessage;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringIntegrationRedisTestApplicationTest {
	
	@Autowired
	private MessageChannel redisPubsubChannel;
	
	@Autowired
	private ObjectMapper redisObjectMapper;
	
	@Autowired
	private RedisLockRegistry redisLockRegistry;
	
	/**
	 * 测试广播消息
	 * @throws Exception
	 */
	@Test
	public void test() throws Exception{
		PubsubMessage pubsubMessage = new PubsubMessage("1", "zy", new BigDecimal("100"));
		this.redisPubsubChannel.send(MessageBuilder.withPayload(this.redisObjectMapper.writeValueAsString(pubsubMessage)).build());
		System.out.println("");
	}
	
	/**
	 * 测试基于Redis的分布式锁
	 */
	@Test
	public void testLock(){
		for (int i = 0; i < 10; i++) {
			//获取锁
			Lock lock = this.redisLockRegistry.obtain("zy");
			//加锁
			lock.lock();
			try {
				System.out.println(this.redisLockRegistry.listLocks().size());
			} finally {
				//锁释放
				lock.unlock();
			}
		}
		System.out.println(this.redisLockRegistry.listLocks().size());
	}

}
