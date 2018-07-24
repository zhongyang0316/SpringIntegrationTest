package com.zy.springintegrationmqtttest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@Configuration
public class IntegrationMqttConfig {
	
	//客户端配置
	@Bean
	public MqttPahoClientFactory mqttPahoClientFactory(){
		DefaultMqttPahoClientFactory defaultMqttPahoClientFactory = new DefaultMqttPahoClientFactory();
		defaultMqttPahoClientFactory.setUserName("guest");
		defaultMqttPahoClientFactory.setPassword("guest");
		defaultMqttPahoClientFactory.setServerURIs("tcp://localhost:1883");//url-消息代理地址
		return defaultMqttPahoClientFactory;
	}
	
	//Inbound(消息驱动)通道适配器
	@Bean
	public MqttPahoMessageDrivenChannelAdapter mqttPahoMessageDrivenChannelAdapter(MqttPahoClientFactory mqttPahoClientFactory){
		MqttPahoMessageDrivenChannelAdapter channelAdapter = 
				new MqttPahoMessageDrivenChannelAdapter(
				"zyInmqtt", //clientId
				mqttPahoClientFactory, 
				"zytest"); //topics-一个用逗号分隔的主题列表，从这个列表中，这个适配器将接收消息
		channelAdapter.setQos(1); //Qos-消息服务质量值
		channelAdapter.setSendTimeout(30000);
		channelAdapter.setConverter(new DefaultPahoMessageConverter());
		channelAdapter.setOutputChannelName("mqttInputChannel");
		return channelAdapter;
	}
	
	//inbound 处理channel
	@Bean
	public MessageChannel mqttInputChannel(){
		return new DirectChannel();
	}
	
	//序列化问题   应自行实现接口MqttMessageConverter
//	public MqttMessageConverter mqttMessageConverter(){
//		DefaultPahoMessageConverter defaultPahoMessageConverter = new DefaultPahoMessageConverter(
//		return defaultPahoMessageConverter;
//	}
	@Bean
	public ObjectMapper mqttObjectMapper(){
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, true);
		objectMapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true);
		return objectMapper;
	}
	
	//Outbound通道适配器
	@Bean
	@ServiceActivator(inputChannel = "mqttOutboundChannel")
	public MqttPahoMessageHandler mqttPahoMessageHandler(MqttPahoClientFactory mqttPahoClientFactory){
		MqttPahoMessageHandler mqttPahoMessageHandler = new MqttPahoMessageHandler(
				"zyOutmqtt", 
				mqttPahoClientFactory);
//		mqttPahoMessageHandler.setConverter(new DefaultPahoMessageConverter());
		mqttPahoMessageHandler.setDefaultQos(1); //默认的服务质量（如果没有找到mqttqos头，或者qos-表达式返回null。如果提供自定义转换器，则不使用
		mqttPahoMessageHandler.setDefaultTopic("zytest"); //消息将被发送的默认主题（如果没有找到mqtttopic头，则使用该主题）。
		mqttPahoMessageHandler.setAsync(false); //消息确认处理方式
		//当异步和异步事件都为真时，就会发出MqttMessageSentEvent，包含消息、主题、由客户端库生成的messageId、clientId和clientInstance（每次客户机连接时递增）。
		//当配送被客户端库确认时，会释放MqttMessageDeliveredEvent，内含messageId、clientId和clientInstance，从而使交付与发送相关联。
		//这些事件可以由任何应用程序监听器接收，也可以由事件入站通道适配器接收。请注意，MqttMessageDeliveredEvent可能在MqttMessageSentEvent之前收到。默认值:假
		mqttPahoMessageHandler.setAsyncEvents(false); //消息确认事件  
		return mqttPahoMessageHandler;
	}
	
	//outBound 消息发送通道
	@Bean
    public MessageChannel mqttOutboundChannel() {
        return new DirectChannel();
    }
	
	@MessagingGateway
    public interface MqttOutGateway {

		@Gateway(requestChannel = "mqttOutboundChannel")
        void sendToMqtt(String data);

    }

}
