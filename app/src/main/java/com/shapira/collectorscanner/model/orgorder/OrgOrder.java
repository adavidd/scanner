package com.shapira.collectorscanner.model.orgorder;

import com.google.gson.annotations.SerializedName;
import com.shapira.collectorscanner.model.OrderStatus;
import com.shapira.collectorscanner.model.Organization;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class OrgOrder implements Serializable {

	@SerializedName("id")
	private int id;

	@SerializedName("org_id")
	private int orgId;
	@SerializedName("lines_count")
	public int linesCount;
	@SerializedName("items_sum")
	public int itemsSum;
	@SerializedName("status_id")
	private int statusId;
	@SerializedName("timestamp")
	private Date orderTime;

	@SerializedName("organization")
	private Organization organization;
	@SerializedName("order_status")
	private OrderStatus orderStatus;
	@SerializedName("order_items")
	private List<OrgOrderItemsItem> orderItems;
	public OrderStatus getOrderStatus(){
		return orderStatus;
	}

	public int getStatusId(){
		return statusId;
	}


	public int getId(){
		return id;
	}
	public Date getOrderTime(){
		return orderTime;
	}
	public Organization getOrganization(){
		return organization;
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}

	public List<OrgOrderItemsItem> getOrderItems(){
		return orderItems;
	}


}