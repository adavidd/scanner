package co.il.scanner.model;

import com.google.gson.annotations.SerializedName;

public class OrderStatus{

	@SerializedName("createdAt")
	private String createdAt;

	@SerializedName("name")
	private String name;

	@SerializedName("id")
	private int id;

	@SerializedName("updatedAt")
	private String updatedAt;

	public String getCreatedAt(){
		return createdAt;
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