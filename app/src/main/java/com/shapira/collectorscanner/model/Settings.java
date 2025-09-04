package com.shapira.collectorscanner.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Settings implements Serializable {

	@SerializedName("order_complete_password")
	public String order_complete_password;
//	public String get_order_complete_password(){
//		return order_complete_password;
//	}
}