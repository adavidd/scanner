package co.il.scanner.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Orders{

	@SerializedName("foreign_id")
	private Object foreignId;

	@SerializedName("order_status")
	private OrderStatus orderStatus;

	@SerializedName("status_id")
	private int statusId;

	@SerializedName("comments")
	private String comments;

	@SerializedName("deleted")
	private boolean deleted;

	@SerializedName("ordered_from")
	private String orderedFrom;

	@SerializedName("id")
	private int id;

	@SerializedName("order_time")
	private Object orderTime;

	@SerializedName("customer_id")
	private int customerId;

	@SerializedName("order_items")
	private List<OrderItemsItem> orderItems;

	@SerializedName("customer")
	private Customer customer;

	public Object getForeignId(){
		return foreignId;
	}

	public OrderStatus getOrderStatus(){
		return orderStatus;
	}

	public int getStatusId(){
		return statusId;
	}

	public String getComments(){
		return comments;
	}

	public boolean isDeleted(){
		return deleted;
	}

	public String getOrderedFrom(){
		return orderedFrom;
	}

	public int getId(){
		return id;
	}

	public Object getOrderTime(){
		return orderTime;
	}

	public int getCustomerId(){
		return customerId;
	}

	public void setForeignId(Object foreignId) {
		this.foreignId = foreignId;
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public void setOrderedFrom(String orderedFrom) {
		this.orderedFrom = orderedFrom;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setOrderTime(Object orderTime) {
		this.orderTime = orderTime;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public void setOrderItems(List<OrderItemsItem> orderItems) {
		this.orderItems = orderItems;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public List<OrderItemsItem> getOrderItems(){
		return orderItems;
	}

	public Customer getCustomer(){
		return customer;
	}
}