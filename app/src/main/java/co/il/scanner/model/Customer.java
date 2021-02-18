package co.il.scanner.model;

import com.google.gson.annotations.SerializedName;

public class Customer{

	@SerializedName("branchId")
	private Object branchId;

	@SerializedName("firstname2")
	private String firstname2;

	@SerializedName("firstname1")
	private String firstname1;

	@SerializedName("address")
	private Object address;

	@SerializedName("comments")
	private Object comments;

	@SerializedName("city")
	private Object city;

	@SerializedName("phone2")
	private Object phone2;

	@SerializedName("tz1")
	private String tz1;

	@SerializedName("tz2")
	private String tz2;

	@SerializedName("lastname")
	private String lastname;

	@SerializedName("phone1")
	private Object phone1;

	@SerializedName("foreign_id")
	private Object foreignId;

	@SerializedName("deleted")
	private boolean deleted;

	@SerializedName("barnch_id")
	private Object barnchId;

	@SerializedName("id")
	private int id;

	public Object getBranchId(){
		return branchId;
	}

	public String getFirstname2(){
		return firstname2;
	}

	public String getFirstname1(){
		return firstname1;
	}

	public Object getAddress(){
		return address;
	}

	public Object getComments(){
		return comments;
	}

	public Object getCity(){
		return city;
	}

	public Object getPhone2(){
		return phone2;
	}

	public String getTz1(){
		return tz1;
	}

	public String getTz2(){
		return tz2;
	}

	public String getLastname(){
		return lastname;
	}

	public Object getPhone1(){
		return phone1;
	}

	public Object getForeignId(){
		return foreignId;
	}

	public boolean isDeleted(){
		return deleted;
	}

	public Object getBarnchId(){
		return barnchId;
	}

	public int getId(){
		return id;
	}
}