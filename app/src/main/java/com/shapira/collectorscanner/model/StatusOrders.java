package com.shapira.collectorscanner.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StatusOrders {

    @SerializedName("orders")
    private List<Order> ordersList;


    @SerializedName("status")
    private String status;

    public List<Order> getOrdersList() {
        return ordersList;
    }

    public String getStatus() {
        return status;
    }
}
