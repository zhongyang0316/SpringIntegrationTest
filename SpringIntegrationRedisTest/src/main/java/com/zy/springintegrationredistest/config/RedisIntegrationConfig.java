package com.zy.springintegrationredistest.config;

import java.math.BigDecimal;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.integration.redis.channel.SubscribableRedisChannel;
import org.springframework.integration.redis.util.RedisLockRegistry;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.integration.support.converter.SimpleMessageConverter;
import org.springframework.integration.util.ErrorHandlingTaskExecutor;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.zy.springintegrationredistest.model.PubsubMessage;

@Configuration
public class RedisIntegrationConfig {
	
	@Bean(name = "redisPubsubChannel")
	public MessageChannel redisPubsubChannel(RedisConnectionFactory connectionFactory){
		SubscribableRedisChannel redisPubsubChannel = 
				new SubscribableRedisChannel(connectionFactory, "zy.test.channel");
		redisPubsubChannel.setBeanName("redisPubsubChannel");
//		redisPubsubChannel.setMessageConverter(redisMessageConverter());
//		redisPubsubChannel.setSerializer(redisSerializer());
		return redisPubsubChannel;
	}
	
	@Bean
	public ObjectMapper redisObjectMapper(){
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, true);
		objectMapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true);
		return objectMapper;
	}
	
	//Redis分布式锁注册
	@Bean
	public RedisLockRegistry redisLockRegistry(RedisConnectionFactory connectionFactory){
		RedisLockRegistry redisLockRegistry = new RedisLockRegistry(connectionFactory, "testLock");
		return redisLockRegistry;
	}
	
	public MessageConverter redisMessageConverter(){
		MappingJackson2MessageConverter redisMessageConverter = new MappingJackson2MessageConverter();
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, true);
		objectMapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true);
		redisMessageConverter.setObjectMapper(objectMapper);
		return redisMessageConverter;
	}
	
	public RedisSerializer redisSerializer(){
		Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
		ObjectMapper om = new ObjectMapper();
		om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
		om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
		jackson2JsonRedisSerializer.setObjectMapper(om);
		return jackson2JsonRedisSerializer;
	}
	
//	public static void main(String[] args) throws Exception {
//		MappingJackson2MessageConverter redisMessageConverter = new MappingJackson2MessageConverter();
//		ObjectMapper objectMapper = new ObjectMapper();
//		objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, true);
//		objectMapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true);
//		redisMessageConverter.setObjectMapper(objectMapper);
//		PubsubMessage pubsubMessage = new PubsubMessage("1", "zy", new BigDecimal("100"));
//		PubsubMessage aa = (PubsubMessage) redisMessageConverter.fromMessage(MessageBuilder.withPayload(objectMapper.writeValueAsString(pubsubMessage)).build(), PubsubMessage.class);
//		System.out.println(aa);
//	}
	
//	public RedisMessageListenerContainer redisMessageListenerContainer(){
//		RedisMessageListenerContainer redisMessageListenerContainer = new RedisMessageListenerContainer();
//		redisMessageListenerContainer.
//		return redisMessageListenerContainer;
//	}
	
//	@Bean
//    public RedisTemplate redisTemplate(RedisConnectionFactory connectionFactory) {
//        RedisTemplate redisTemplate = new RedisTemplate();
//        redisTemplate.setConnectionFactory(connectionFactory);
//        initDomainRedisTemplate(redisTemplate);
//        redisTemplate.afterPropertiesSet();
//        return redisTemplate;
//    }
	
	/**

	 * 设置数据存入 redis 的序列化方式

	 * </br>redisTemplate 序列化默认使用的jdkSerializeable, 存储二进制字节码, 所以自定义序列化类

	 *

	 * @param redisTemplate

	 */
//	@SuppressWarnings({ "rawtypes", "unchecked" })
//	private void initDomainRedisTemplate(RedisTemplate redisTemplate) {
//		//key序列化方式;（不然会出现乱码;）,但是如果方法上有Long等非String类型的话，会报类型转换错误；
//		//所以在没有自己定义key生成策略的时候，以下这个代码建议不要这么写，可以不配置或者自己实现ObjectRedisSerializer
//		//或者JdkSerializationRedisSerializer序列化方式;
//		// 使用Jackson2JsonRedisSerialize 替换默认序列化
//		Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
//		ObjectMapper om = new ObjectMapper();
//		om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//		om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
//		jackson2JsonRedisSerializer.setObjectMapper(om);
//		// string结构的数据，设置value的序列化规则和 key的序列化规则
//		//StringRedisSerializer解决key中午乱码问题。//Long类型不可以会出现异常信息;
////		redisTemplate.setKeySerializer(new StringRedisSerializer());
//		//value乱码问题：Jackson2JsonRedisSerializer
//		redisTemplate.setDefaultSerializer(jackson2JsonRedisSerializer);
//		redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
//		//设置Hash结构的key和value的序列化方式
//		//redisTemplate.setHashKeySerializer(jackson2JsonRedisSerializer);
//		//redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
//	}
	
}
