package com.shapira.collectorscanner.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LoginUser implements Serializable {

	@SerializedName("createdAt")
	private String createdAt;

	@SerializedName("password")
	private String password;



	@SerializedName("pincode")
	private String pincode;

	@SerializedName("firstname")
	private String firstname;

	@SerializedName("role")
	private int role;

	@SerializedName("deleted")
	private boolean deleted;

	@SerializedName("id")
	private int id;

	@SerializedName("username")
	private String username;

	@SerializedName("lastname")
	private String lastname;

	@SerializedName("updatedAt")
	private String updatedAt;

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setPassword(String password){
		this.password = password;
	}

	public String getPassword(){
		return password;
	}

	public void setPincode(String pincode){
		this.pincode = pincode;
	}
	public String getPincode(){
		return pincode;
	}

	public void setFirstname(String firstname){
		this.firstname = firstname;
	}

	public String getFirstname(){
		return firstname;
	}

	public void setRole(int role){
		this.role = role;
	}

	public int getRole(){
		return role;
	}

	public void setDeleted(boolean deleted){
		this.deleted = deleted;
	}

	public boolean isDeleted(){
		return deleted;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setUsername(String username){
		this.username = username;
	}

	public String getUsername(){
		return username;
	}

	public void setLastname(String lastname){
		this.lastname = lastname;
	}

	public String getLastname(){
		return lastname;
	}

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}
}