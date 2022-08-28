package com.shapira.collectorscanner.model;

import com.google.gson.annotations.SerializedName;

public class OrderStatus{

	@SerializedName("createdAt")
	private String createdAt;

	@SerializedName("color")
	private String color;

	@SerializedName("name")
	private String name;

	@SerializedName("id")
	private int id;

	@SerializedName("updatedAt")
	private String updatedAt;

	public String getCreatedAt(){
		return createdAt;
	}

	public String getColor(){
		return color;
	}

	public String getName(){
		return name;
	}

	public int getId(){
		return id;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}
}