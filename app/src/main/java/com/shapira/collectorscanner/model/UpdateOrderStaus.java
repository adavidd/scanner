package com.shapira.collectorscanner.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import retrofit2.http.Body;

public class UpdateOrderStaus implements Serializable {
    @SerializedName("order_id")
    public int order_id;

    @SerializedName("status_id")
    public int status_id;
    @SerializedName("user_id")
    public int user_id;


}
