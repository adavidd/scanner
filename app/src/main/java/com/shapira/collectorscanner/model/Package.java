package com.shapira.collectorscanner.model;

import com.google.gson.annotations.SerializedName;

public class Package {
    @SerializedName("id")
    private int id;
    @SerializedName("order_id")
    private int order_id;
    @SerializedName("pallet_id")
    private int pallet_id;
    @SerializedName("number")
    private int number;
    @SerializedName("order")
    private Order order;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public int getPallet_id() {
        return pallet_id;
    }

    public void setPallet_id(int pallet_id) {
        this.pallet_id = pallet_id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
