package com.shapira.collectorscanner.model;

import com.google.gson.annotations.SerializedName;

public class ProcessPaymentObject{

	@SerializedName("user_id")
	private int userId;

	@SerializedName("order_id")
	private int orderId;
	@SerializedName("printer_id")
	private int printerId;
	public void setPrinterId(int printerId){
		this.printerId = printerId;
	}
	public int getPrinterId(){
		return printerId;
	}
	public void setUserId(int userId){
		this.userId = userId;
	}

	public int getUserId(){
		return userId;
	}

	public void setOrderId(int orderId){
		this.orderId = orderId;
	}

	public int getOrderId(){
		return orderId;
	}
}