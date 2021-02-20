package co.il.scanner.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StatusOrders {

    @SerializedName("orders")
    private List<Orders> ordersList;

    @SerializedName("status")
    private String status;

    public List<Orders> getOrdersList() {
        return ordersList;
    }

    public String getStatus() {
        return status;
    }
}
