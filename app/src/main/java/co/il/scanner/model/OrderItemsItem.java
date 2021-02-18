package co.il.scanner.model;

import com.google.gson.annotations.SerializedName;

public class OrderItemsItem{

	@SerializedName("foreign_id")
	private Object foreignId;

	@SerializedName("item")
	private String item;

	@SerializedName("comments")
	private Object comments;

	@SerializedName("deleted")
	private boolean deleted;

	@SerializedName("item_id")
	private int itemId;

	@SerializedName("order_quantity")
	private int orderQuantity;

	@SerializedName("id")
	private int id;

	@SerializedName("order_id")
	private int orderId;

	@SerializedName("collected_quantity")
	private int collectedQuantity;

	public void setForeignId(Object foreignId){
		this.foreignId = foreignId;
	}

	public Object getForeignId(){
		return foreignId;
	}

	public void setItem(String item){
		this.item = item;
	}

	public String getItem(){
		return item;
	}

	public void setComments(Object comments){
		this.comments = comments;
	}

	public Object getComments(){
		return comments;
	}

	public void setDeleted(boolean deleted){
		this.deleted = deleted;
	}

	public boolean isDeleted(){
		return deleted;
	}

	public void setItemId(int itemId){
		this.itemId = itemId;
	}

	public int getItemId(){
		return itemId;
	}

	public void setOrderQuantity(int orderQuantity){
		this.orderQuantity = orderQuantity;
	}

	public int getOrderQuantity(){
		return orderQuantity;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setOrderId(int orderId){
		this.orderId = orderId;
	}

	public int getOrderId(){
		return orderId;
	}

	public void setCollectedQuantity(int collectedQuantity){
		this.collectedQuantity = collectedQuantity;
	}

	public int getCollectedQuantity(){
		return collectedQuantity;
	}
}