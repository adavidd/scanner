package com.shapira.collectorscanner.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Branch{

	@SerializedName("foreign_id")
	private int foreignId;

	@SerializedName("catalog_id")
	private int catalogId;

	@SerializedName("createdAt")
	private Date createdAt;

	@SerializedName("comments")
	private String comments;


	@SerializedName("name")
	private String name;

	@SerializedName("id")
	private int id;

	@SerializedName("priority")
	private int priority;

	@SerializedName("phone_id")
	private int phoneId;

	@SerializedName("updatedAt")
	private Date updatedAt;

	public int getForeignId(){
		return foreignId;
	}

	public int getCatalogId(){
		return catalogId;
	}



	public Date getCreatedAt(){
		return createdAt;
	}

	public String getComments(){
		return comments;
	}



	public String getName(){
		return name;
	}

	public int getId(){
		return id;
	}

	public Object getPriority(){
		return priority;
	}

	public int getPhoneId(){
		return phoneId;
	}

	public Object getUpdatedAt(){
		return updatedAt;
	}
}