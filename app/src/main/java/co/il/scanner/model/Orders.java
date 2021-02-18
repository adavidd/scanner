package co.il.scanner.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Orders{

	@SerializedName("foreign_id")
	private Object foreignId;

	@SerializedName("status_id")
	private int statusId;

	@SerializedName("comments")
	private String comments;

	@SerializedName("deleted")
	private boolean deleted;

	@SerializedName("id")
	private int id;

	@SerializedName("customer_id")
	private int customerId;

	@SerializedName("order_items")
	private List<OrderItemsItem> orderItems;

	@SerializedName("customer")
	private Customer customer;

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

	public void setComments(String comments){
		this.comments = comments;
	}

	public String getComments(){
		return comments;
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

	public void setCustomerId(int customerId){
		this.customerId = customerId;
	}

	public int getCustomerId(){
		return customerId;
	}

	public void setOrderItems(List<OrderItemsItem> orderItems){
		this.orderItems = orderItems;
	}

	public List<OrderItemsItem> getOrderItems(){
		return orderItems;
	}

	public void setCustomer(Customer customer){
		this.customer = customer;
	}

	public Customer getCustomer(){
		return customer;
	}
}