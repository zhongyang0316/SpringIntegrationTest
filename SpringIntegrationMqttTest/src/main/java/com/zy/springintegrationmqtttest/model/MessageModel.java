package com.zy.springintegrationmqtttest.model;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@SuppressWarnings("serial")
public class MessageModel implements Serializable {
	
	private String id;
	
	private String name;
	
	public MessageModel(String id, String name){
		this.id = id;
		this.name = name;
	}

}
