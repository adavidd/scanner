package com.shapira.collectorscanner.model;

import com.google.gson.annotations.SerializedName;

public class Customer{

	@SerializedName("city")
	private String city;

	@SerializedName("cc_lastdigits")
	private String ccLastdigits;

	@SerializedName("phone2")
	private String phone2;

	@SerializedName("cc_token")
	private String ccToken;

	@SerializedName("cc_cvv")
	private String ccCvv;

	@SerializedName("cc_lastdigtis")
	private Object ccLastdigtis;

	@SerializedName("branch")
	private Branch branch;

	@SerializedName("phone1")
	private String phone1;

	@SerializedName("branch_id")
	private int branchId;

	@SerializedName("neighbourhood")
	private Object neighbourhood;

	@SerializedName("id")
	private int id;

	@SerializedName("cc_number")
	private Object ccNumber;

	@SerializedName("member_id")
	private int memberId;

	@SerializedName("firstname2")
	private String firstname2;

	@SerializedName("firstname1")
	private String firstname1;

	@SerializedName("address")
	private String address;

	@SerializedName("comments")
	private Object comments;

	@SerializedName("tz1")
	private String tz1;

	@SerializedName("tz2")
	private String tz2;

	@SerializedName("lastname")
	private String lastname;

	@SerializedName("foreign_id")
	private int foreignId;

	@SerializedName("branch_comments")
	private Object branchComments;

	@SerializedName("cc_exp")
	private String ccExp;

	@SerializedName("deleted")
	private boolean deleted;

	@SerializedName("phone_id")
	private int phoneId;

	public String getCity(){
		return city;
	}

	public String getCcLastdigits(){
		return ccLastdigits;
	}

	public String getPhone2(){
		return phone2;
	}

	public String getCcToken(){
		return ccToken;
	}

	public String getCcCvv(){
		return ccCvv;
	}

	public Object getCcLastdigtis(){
		return ccLastdigtis;
	}

	public Branch getBranch(){
		return branch;
	}

	public String getPhone1(){
		return phone1;
	}

	public int getBranchId(){
		return branchId;
	}

	public Object getNeighbourhood(){
		return neighbourhood;
	}

	public int getId(){
		return id;
	}

	public Object getCcNumber(){
		return ccNumber;
	}

	public int getMemberId(){
		return memberId;
	}

	public String getFirstname2(){
		return firstname2;
	}

	public String getFirstname1(){
		return firstname1;
	}

	public String getAddress(){
		return address;
	}

	public Object getComments(){
		return comments;
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

	public int getForeignId(){
		return foreignId;
	}

	public Object getBranchComments(){
		return branchComments;
	}

	public String getCcExp(){
		return ccExp;
	}

	public boolean isDeleted(){
		return deleted;
	}

	public int getPhoneId(){
		return phoneId;
	}
}