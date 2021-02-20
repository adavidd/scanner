package co.il.scanner.model;

import com.google.gson.annotations.SerializedName;

public class UpdateItem{

	@SerializedName("quantity")
	private int quantity;

	@SerializedName("item_id")
	private int itemId;

	@SerializedName("user_id")
	private int userId;

	@SerializedName("order_id")
	private int orderId;

	public void setQuantity(int quantity){
		this.quantity = quantity;
	}

	public int getQuantity(){
		return quantity;
	}

	public void setItemId(int itemId){
		this.itemId = itemId;
	}

	public int getItemId(){
		return itemId;
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