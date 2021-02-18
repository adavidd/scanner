package co.il.scanner.server;

import java.util.List;

import co.il.scanner.model.LoginUser;
import co.il.scanner.model.Orders;
import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface GetDataService {

    @GET("/api/users")
    Observable<List<LoginUser>> getUsersLogin();

    @GET("/api/orders/1")
    Observable<Orders> getOrders();
}
