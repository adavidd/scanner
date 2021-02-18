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

	public void setBranchId(Object branchId){
		this.branchId = branchId;
	}

	public Object getBranchId(){
		return branchId;
	}

	public void setFirstname2(String firstname2){
		this.firstname2 = firstname2;
	}

	public String getFirstname2(){
		return firstname2;
	}

	public void setFirstname1(String firstname1){
		this.firstname1 = firstname1;
	}

	public String getFirstname1(){
		return firstname1;
	}

	public void setAddress(Object address){
		this.address = address;
	}

	public Object getAddress(){
		return address;
	}

	public void setComments(Object comments){
		this.comments = comments;
	}

	public Object getComments(){
		return comments;
	}

	public void setCity(Object city){
		this.city = city;
	}

	public Object getCity(){
		return city;
	}

	public void setPhone2(Object phone2){
		this.phone2 = phone2;
	}

	public Object getPhone2(){
		return phone2;
	}

	public void setTz1(String tz1){
		this.tz1 = tz1;
	}

	public String getTz1(){
		return tz1;
	}

	public void setTz2(String tz2){
		this.tz2 = tz2;
	}

	public String getTz2(){
		return tz2;
	}

	public void setLastname(String lastname){
		this.lastname = lastname;
	}

	public String getLastname(){
		return lastname;
	}

	public void setPhone1(Object phone1){
		this.phone1 = phone1;
	}

	public Object getPhone1(){
		return phone1;
	}

	public void setForeignId(Object foreignId){
		this.foreignId = foreignId;
	}

	public Object getForeignId(){
		return foreignId;
	}

	public void setDeleted(boolean deleted){
		this.deleted = deleted;
	}

	public boolean isDeleted(){
		return deleted;
	}

	public void setBarnchId(Object barnchId){
		this.barnchId = barnchId;
	}

	public Object getBarnchId(){
		return barnchId;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}
}