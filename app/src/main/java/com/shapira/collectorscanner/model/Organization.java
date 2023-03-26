package com.shapira.collectorscanner.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Organization {




	@SerializedName("name")
	private String name;

	@SerializedName("id")
	private int id;


	@SerializedName("phone_id")
	private int phoneId;







	public String getName(){
		return name;
	}

	public int getId(){
		return id;
	}



	public int getPhoneId(){
		return phoneId;
	}

}