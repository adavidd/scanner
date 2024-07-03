package com.shapira.collectorscanner.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Item implements Serializable {

	@SerializedName("code")
	private int code;

	@SerializedName("comments")
	private Object comments;

	@SerializedName("color")
	private String color;

	@SerializedName("image_url")
	private Object imageUrl;

	@SerializedName("description")
	private Object description;

	@SerializedName("weight")
	private int weight;

	@SerializedName("warhouse_description")
	private String warhouseDescription;

	@SerializedName("type")
	private Object type;

	@SerializedName("barcode2")
	private String barcode2;

	@SerializedName("barcode1")
	private String barcode1;

	@SerializedName("foreign_id")
	private int foreignId;

	@SerializedName("item_location")
	private Object itemLocation;

	@SerializedName("createdAt")
	private String createdAt;

	@SerializedName("deleted")
	private boolean deleted;

	@SerializedName("hide_in_app")
	private boolean hide_in_app;

	@SerializedName("size")
	private String size;

	@SerializedName("category_id")
	private Object categoryId;

	@SerializedName("catalog_number")
	private int catalogNumber;

	@SerializedName("price")
	private String price;

	@SerializedName("name")
	private String name;

	@SerializedName("id")
	private int id;

	@SerializedName("phone_id")
	private int phoneId;

	@SerializedName("updatedAt")
	private String updatedAt;

	public int getCode(){
		return code;
	}

	public Object getComments(){
		return comments;
	}

	public String getColor(){
		return color;
	}

	public Object getImageUrl(){
		return imageUrl;
	}

	public Object getDescription(){
		return description;
	}

	public int getWeight(){
		return weight;
	}

	public String getWarhouseDescription(){
		return warhouseDescription;
	}

	public Object getType(){
		return type;
	}

	public String getBarcode2(){
		return barcode2;
	}

	public String getBarcode1(){
		return barcode1;
	}

	public int getForeignId(){
		return foreignId;
	}

	public Object getItemLocation(){
		return itemLocation;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public boolean isDeleted(){
		return deleted;
	}

	public boolean isHide_in_app(){
		return hide_in_app;
	}

	public String getSize(){
		return size;
	}

	public Object getCategoryId(){
		return categoryId;
	}

	public int getCatalogNumber(){
		return catalogNumber;
	}

	public String getPrice(){
		return price;
	}

	public String getName(){
		return name;
	}

	public int getId(){
		return id;
	}

	public int getPhoneId(){
		return phoneId;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}
}