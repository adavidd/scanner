package co.il.scanner.model;

import com.google.gson.annotations.SerializedName;

public class ProcessPaymentObject{

	@SerializedName("user_id")
	private int userId;

	@SerializedName("order_id")
	private int orderId;

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