package com.shapira.collectorscanner.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class OrderItemsItem{

	@SerializedName("item")
	private Item item;

	@SerializedName("comments")
	private String comments;

	@SerializedName("item_id")
	private int itemId;

	@SerializedName("ordered_at")
	private Date orderedAt;

	@SerializedName("unit_price")
	private String unitPrice;

	@SerializedName("collected_quantity")
	private int collectedQuantity;

	@SerializedName("line_total")
	private double lineTotal;

	@SerializedName("collected_at")
	private Date collectedAt;

	@SerializedName("foreign_id")
	private int foreignId;

	@SerializedName("deleted")
	private boolean deleted;

	@SerializedName("hide_in_app")
	private boolean hide_in_app;

	@SerializedName("supplied_quantity")
	private int suppliedQuantity;

	@SerializedName("order_quantity")
	private int orderQuantity;

	@SerializedName("id")
	private int id;

	@SerializedName("collected_by_user_id")
	private int collectedByUserId;

	@SerializedName("order_id")
	private int orderId;

	@SerializedName("collected_by_cart_id")
	private int collectedByCartId;

	@SerializedName("phone_id")
	private int phoneId;

	public Item getItem(){
		return item;
	}

	public String getComments(){
		return comments;
	}

	public int getItemId(){
		return itemId;
	}

	public Date getOrderedAt(){
		return orderedAt;
	}

	public String getUnitPrice(){
		return unitPrice;
	}

	public int getCollectedQuantity(){
		return collectedQuantity;
	}

	public void setCollectedQuantity(int collectedQuantity) {
		this.collectedQuantity = collectedQuantity;
	}
	public Double getLineTotal(){
		return lineTotal;
	}

	public Date getCollectedAt(){
		return collectedAt;
	}

	public int getForeignId(){
		return foreignId;
	}

	public boolean isDeleted(){
		return deleted;
	}
	public boolean isHide_in_app(){
		return hide_in_app;
	}

	public int getSuppliedQuantity(){
		return suppliedQuantity;
	}

	public int getOrderQuantity(){
		return orderQuantity;
	}

	public int getId(){
		return id;
	}

	public int getCollectedByUserId(){
		return collectedByUserId;
	}

	public int getOrderId(){
		return orderId;
	}

	public int getCollectedByCartId(){
		return collectedByCartId;
	}

	public int getPhoneId(){
		return phoneId;
	}
}