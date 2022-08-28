package com.shapira.collectorscanner.model;

import com.google.gson.annotations.SerializedName;

public class NextOrder{

	@SerializedName("message")
	private String message;

	@SerializedName("status")
	private String status;

	@SerializedName("order")
	private Order order;

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}

	public void setOrder(Order order){
		this.order = order;
	}

	public Order getOrder(){
		return order;
	}
}