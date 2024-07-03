package com.shapira.collectorscanner.model.orgorder;

import com.google.gson.annotations.SerializedName;
import com.shapira.collectorscanner.model.Item;

import java.io.Serializable;
import java.util.Date;

public class OrgOrderItemsItem implements Serializable {

	@SerializedName("item")
	private Item item;
	@SerializedName("item_id")
	private int itemId;
	@SerializedName("unit_price")
	private String unitPrice;
	@SerializedName("units_in_package")
	public int units_in_package;
	@SerializedName("collected_quantity")
	private int collectedQuantity;



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

	public int getUnitsInPackage(){
		return units_in_package;
	}

	public int getItemId(){
		return itemId;
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

}