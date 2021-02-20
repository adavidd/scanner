package co.il.scanner.server;

import java.util.List;

import co.il.scanner.model.CompleteOrder;
import co.il.scanner.model.LoginUser;
import co.il.scanner.model.NextOrder;
import co.il.scanner.model.Orders;
import co.il.scanner.model.ProcessPaymentObject;
import co.il.scanner.model.Status;
import co.il.scanner.model.StatusMessage;
import co.il.scanner.model.StatusOrders;
import co.il.scanner.model.UpdateItem;
import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GetDataService {

    @GET("/api/users")
    Observable<List<LoginUser>> getUsersLogin();

    @GET("/api/orders/{order_number}")
    Observable<Orders> getOrders(@Path("order_number") int orderNumber);

    @GET("/api/orders/getNextOrder")
    Observable<NextOrder> getNextOrder(@Query("user_id") int userId, @Query("status_id") int statusId);

    @POST("/api/orders/updateItemCollection")
    Observable<Status> updateItemCollection(@Body UpdateItem updateItem);

    @POST("/api/orders/updateCompleteOrderCollection")
    Observable<Status> updateCompleteOrderCollection(@Body CompleteOrder completeOrder);

    @POST("/api/processPayment")
    Observable<StatusMessage> processPayment(@Body ProcessPaymentObject processPaymentObject);

    @GET("/api/orders")
    Observable<StatusOrders> getOrdersList(@Query("user_id") int userId);
}
