package com.zy.springintegrationkafkatest.config;

import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.expression.common.LiteralExpression;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.kafka.inbound.KafkaMessageDrivenChannelAdapter;
import org.springframework.integration.kafka.outbound.KafkaProducerMessageHandler;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.config.ContainerProperties;
import org.springframework.kafka.support.TopicPartitionInitialOffset;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.PollableChannel;

@Configuration
@EnableConfigurationProperties(IntegrationKafkaProperties.class)
public class IntegrationKafkaConfig {
	
	@Autowired
	private IntegrationKafkaProperties integrationKafkaProperties;
	
	//*********************************Producer(outbound)************************************
	
	@Bean
	public ProducerFactory<?, ?> kafkaProducerFactory(KafkaProperties properties) {
		Map<String, Object> producerProperties = properties.buildProducerProperties();
		producerProperties.put(ProducerConfig.LINGER_MS_CONFIG, 1);
		@SuppressWarnings({ "rawtypes", "unchecked" })
		DefaultKafkaProducerFactory kafkaProducerFactory = new DefaultKafkaProducerFactory(producerProperties);
		return kafkaProducerFactory;
	}
	
	@Bean
	@ServiceActivator(inputChannel = "toKafkaChannel")
	public MessageHandler handler(KafkaTemplate<String, String> kafkaTemplate) {
		@SuppressWarnings({ "rawtypes", "unchecked" })
		KafkaProducerMessageHandler<String, String> handler =
				new KafkaProducerMessageHandler(kafkaTemplate);
		handler.setMessageKeyExpression(new LiteralExpression(this.integrationKafkaProperties.getMessageKey()));
//		handler.setAsync(true);
//		handler.setRequiresReply(true);
		return handler;
	}
	
	//outBoundGateWay
//	@MessagingGateway
//	public interface kafkaOutGateway {
//		
//		@Gateway(requestChannel = "toKafkaChannel", replyTimeout = 30 * 1000L)
//		String send(@Payload String message);
//		
//	}
	
	
	//*******************************Consumer(inbound)***********************************
	
	@Bean
	public ConsumerFactory<?, ?> kafkaConsumerFactory(KafkaProperties properties) {
		Map<String, Object> consumerProperties = properties
				.buildConsumerProperties();
		consumerProperties.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 15000);
		@SuppressWarnings({ "rawtypes", "unchecked" })
		ConsumerFactory kafkaConsumerFactory = new DefaultKafkaConsumerFactory(consumerProperties);
		return kafkaConsumerFactory;
	}
	
	@Bean
	public KafkaMessageListenerContainer<String, String> container(
			ConsumerFactory<String, String> kafkaConsumerFactory) {
		
		@SuppressWarnings({ "rawtypes", "unchecked" })
		KafkaMessageListenerContainer container = new KafkaMessageListenerContainer(kafkaConsumerFactory,
				new ContainerProperties(new TopicPartitionInitialOffset(this.integrationKafkaProperties.getTopic(), 0)));
		
		return container;
	}
	
	@Bean
	public KafkaMessageDrivenChannelAdapter<String, String>
				adapter(KafkaMessageListenerContainer<String, String> container) {
		KafkaMessageDrivenChannelAdapter<String, String> kafkaMessageDrivenChannelAdapter =
				new KafkaMessageDrivenChannelAdapter(container);
		kafkaMessageDrivenChannelAdapter.setOutputChannel(fromKafkaChannel());
		return kafkaMessageDrivenChannelAdapter;
	}
	
	//InboundGateway
//	@Bean
//	public KafkaInboundGateway kafkaInboundGateway(KafkaMessageListenerContainer<String, String> container,KafkaTemplate<String, String> kafkaTemplate){
//		KafkaInboundGateway kafkaInboundGateway = new KafkaInboundGateway(container, kafkaTemplate);
//		kafkaInboundGateway.setRequestChannel(requestChannel);
//		return kafkaInboundGateway;
//	}
	
	@Bean
	public PollableChannel fromKafkaChannel() {
		return new QueueChannel();
	}

}
