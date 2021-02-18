package co.il.scanner.model;

import com.google.gson.annotations.SerializedName;

public class OrderItemsItem{

	@SerializedName("foreign_id")
	private Object foreignId;

	@SerializedName("item")
	private Item item;

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

	@SerializedName("collected_at")
	private Object collectedAt;

	public Object getForeignId(){
		return foreignId;
	}

	public Item getItem(){
		return item;
	}

	public Object getComments(){
		return comments;
	}

	public boolean isDeleted(){
		return deleted;
	}

	public int getItemId(){
		return itemId;
	}

	public int getOrderQuantity(){
		return orderQuantity;
	}

	public void setForeignId(Object foreignId) {
		this.foreignId = foreignId;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public void setComments(Object comments) {
		this.comments = comments;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public void setOrderQuantity(int orderQuantity) {
		this.orderQuantity = orderQuantity;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public void setCollectedQuantity(int collectedQuantity) {
		this.collectedQuantity = collectedQuantity;
	}

	public void setCollectedAt(Object collectedAt) {
		this.collectedAt = collectedAt;
	}

	public int getId(){
		return id;
	}

	public int getOrderId(){
		return orderId;
	}

	public int getCollectedQuantity(){
		return collectedQuantity;
	}

	public Object getCollectedAt(){
		return collectedAt;
	}
}