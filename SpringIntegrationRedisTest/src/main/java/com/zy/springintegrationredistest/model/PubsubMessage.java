package com.zy.springintegrationredistest.model;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;

@Data
@SuppressWarnings("serial")
public class PubsubMessage implements Serializable {
	
	private String id;
	
	private String name;
	
	private BigDecimal amt;
	
	public PubsubMessage(){
		
	}
	
	public PubsubMessage(String id, String name, BigDecimal amt){
		this.id = id;
		this.name = name;
		this.amt = amt;
	}

}
