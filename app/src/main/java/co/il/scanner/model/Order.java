package co.il.scanner.model;

import com.google.gson.annotations.SerializedName;

public class Order{

	@SerializedName("foreign_id")
	private Object foreignId;

	@SerializedName("status_id")
	private int statusId;

	@SerializedName("comments")
	private Object comments;

	@SerializedName("deleted")
	private boolean deleted;

	@SerializedName("ordered_from")
	private Object orderedFrom;

	@SerializedName("order_fee")
	private String orderFee;

	@SerializedName("id")
	private int id;

	@SerializedName("order_time")
	private Object orderTime;

	@SerializedName("priority")
	private String priority;

	@SerializedName("customer_id")
	private int customerId;

	@SerializedName("collecting_user_id")
	private Object collectingUserId;

	public void setForeignId(Object foreignId){
		this.foreignId = foreignId;
	}

	public Object getForeignId(){
		return foreignId;
	}

	public void setStatusId(int statusId){
		this.statusId = statusId;
	}

	public int getStatusId(){
		return statusId;
	}

	public void setComments(Object comments){
		this.comments = comments;
	}

	public Object getComments(){
		return comments;
	}

	public void setDeleted(boolean deleted){
		this.deleted = deleted;
	}

	public boolean isDeleted(){
		return deleted;
	}

	public void setOrderedFrom(Object orderedFrom){
		this.orderedFrom = orderedFrom;
	}

	public Object getOrderedFrom(){
		return orderedFrom;
	}

	public void setOrderFee(String orderFee){
		this.orderFee = orderFee;
	}

	public String getOrderFee(){
		return orderFee;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setOrderTime(Object orderTime){
		this.orderTime = orderTime;
	}

	public Object getOrderTime(){
		return orderTime;
	}

	public void setPriority(String priority){
		this.priority = priority;
	}

	public String getPriority(){
		return priority;
	}

	public void setCustomerId(int customerId){
		this.customerId = customerId;
	}

	public int getCustomerId(){
		return customerId;
	}

	public void setCollectingUserId(Object collectingUserId){
		this.collectingUserId = collectingUserId;
	}

	public Object getCollectingUserId(){
		return collectingUserId;
	}
}