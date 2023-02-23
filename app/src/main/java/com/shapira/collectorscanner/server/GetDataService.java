package com.shapira.collectorscanner.server;

import java.util.List;

import com.shapira.collectorscanner.model.CompleteOrder;
import com.shapira.collectorscanner.model.LoginUser;
import com.shapira.collectorscanner.model.NextOrder;
import com.shapira.collectorscanner.model.Order;
import com.shapira.collectorscanner.model.PackageScan;
import com.shapira.collectorscanner.model.Pallet;
import com.shapira.collectorscanner.model.ProcessPaymentObject;
import com.shapira.collectorscanner.model.Status;
import com.shapira.collectorscanner.model.StatusMessage;
import com.shapira.collectorscanner.model.StatusOrders;
import com.shapira.collectorscanner.model.UpdateItem;
import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GetDataService {

    @GET("/api/users/app")
    Observable<List<LoginUser>> getUsersLogin();

    @GET("/api/orders/{order_number}")
    Observable<Order> getOrders(@Path("order_number") int orderNumber,@Query("user_id") int userId);

        @GET("/api/pallets/{pallet_id}")
    Observable<Pallet> getPallet(@Path("pallet_id") int pallet_id, @Query("user_id") int userId);

    @GET("/api/packages/getByPallet")
    Observable<Pallet> getPalletPackages(@Query("pallet_id") int pallet_id);


    @POST("/api/packages/assign")
    Observable<Status> checkPackageAndAssign(@Body PackageScan data);

    @POST("/api/packages/deleteFromPallet")
    Observable<Status> deletePackageFromPallet(@Body PackageScan data);

    @GET("/api/orders/getNextOrder")
    Observable<NextOrder> getNextOrder(@Query("user_id") int userId, @Query("status_id") int statusId);

    @POST("/api/orders/updateItemCollection")
    Observable<Status> updateItemCollection(@Body UpdateItem updateItem);
    @POST("/api/users/updateUserAppLogin")
    Observable<Status> updateUserAppLogin(@Body LoginUser loginUser);

    @POST("/api/orders/updateOrder")
    Observable<Status> updateOrder(@Body Order order);

    @POST("/api/orders/updateCompleteOrderCollection")
    Observable<Status> updateCompleteOrderCollection(@Body CompleteOrder completeOrder);

    @POST("/api/orders/processPayment")
    Observable<StatusMessage> processPayment(@Body ProcessPaymentObject processPaymentObject);

    @GET("/api/orders")
    Observable<StatusOrders> getOrdersList(@Query("user_id") int userId);
}
