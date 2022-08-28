package com.shapira.collectorscanner.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class CompleteOrder{

	@SerializedName("stickers_quantity")
	private String stickersQuantity;

	@SerializedName("user_id")
	private int userId;

	@SerializedName("print_receipt")
	private String printReceipt;

	@SerializedName("printer_id")
	private String printerId;

	@SerializedName("order_id")
	private int orderId;

	@SerializedName("order_items")
	private List<OrderItemsItem> orderItems;

	public void setStickersQuantity(String stickersQuantity){
		this.stickersQuantity = stickersQuantity;
	}

	public String getStickersQuantity(){
		return stickersQuantity;
	}

	public void setUserId(int userId){
		this.userId = userId;
	}

	public int getUserId(){
		return userId;
	}

	public void setPrintReceipt(String printReceipt){
		this.printReceipt = printReceipt;
	}

	public String getPrintReceipt(){
		return printReceipt;
	}

	public void setPrinterId(String printerId){
		this.printerId = printerId;
	}

	public String getPrinterId(){
		return printerId;
	}

	public void setOrderId(int orderId){
		this.orderId = orderId;
	}

	public int getOrderId(){
		return orderId;
	}

	public void setOrderItems(List<OrderItemsItem> orderItems){
		this.orderItems = orderItems;
	}

	public List<OrderItemsItem> getOrderItems(){
		return orderItems;
	}
}