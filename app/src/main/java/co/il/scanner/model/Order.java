package co.il.scanner.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class Order{

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

	@SerializedName("open")
	private boolean open;

	@SerializedName("ordered_from")
	private String orderedFrom;

	@SerializedName("id")
	private int id;

	@SerializedName("order_time")
	private Object orderTime;

	@SerializedName("updatedAt")
	private Date updatedAt;

	@SerializedName("customer_id")
	private int customerId;
	@SerializedName("collecting_user_id")
	private Integer  collectingUserId;
	@SerializedName("cart_id")
	private Integer  cartId;
	@SerializedName("order_items")
	private List<OrderItemsItem> orderItems;

	@SerializedName("member")
	private Member member;

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
	public Integer getCartId(){
		return cartId;
	}
	public Integer getCollectingUserId(){
		return collectingUserId;
	}
	public Date getUpdatedAt(){
		return updatedAt;
	}
	public Member getMember(){
		return member;
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
	public void setCollectingUserId(Integer userId) {
		this.collectingUserId = userId;
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
	public void setCartId(int cartId) {
		this.cartId = cartId;
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