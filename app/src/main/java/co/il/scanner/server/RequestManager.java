package co.il.scanner.server;

import android.util.Log;

import java.util.List;
import java.util.concurrent.TimeUnit;

import co.il.scanner.model.CompleteOrder;
import co.il.scanner.model.LoginUser;
import co.il.scanner.model.NextOrder;
import co.il.scanner.model.Order;
import co.il.scanner.model.ProcessPaymentObject;
import co.il.scanner.model.Status;
import co.il.scanner.model.StatusMessage;
import co.il.scanner.model.StatusOrders;
import co.il.scanner.model.UpdateItem;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RequestManager {


    public static Observable<List<LoginUser>> getUsersLoginList() {
        Log.d("chaim","get users");
        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        return service.getUsersLogin()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(20, TimeUnit.SECONDS);
    }

    public static Observable<Order> getOrder(int orderNumber,int userId) {

        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        return service.getOrders(orderNumber,userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(20, TimeUnit.SECONDS);
    }

    public static Observable<NextOrder> getNextOrder(int userId, int statusId) {

        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        return service.getNextOrder(userId, statusId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(17, TimeUnit.SECONDS);
    }

    public static Observable<Status> updateItemCollection(UpdateItem updateItem) {

        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        return service.updateItemCollection(updateItem)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(17, TimeUnit.SECONDS);
    }

    public static Observable<Status> updateCompleteOrderCollection(CompleteOrder completeOrder) {

        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        return service.updateCompleteOrderCollection(completeOrder)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(17, TimeUnit.SECONDS);
    }

    public static Observable<StatusMessage> processPayment(ProcessPaymentObject processPaymentObject) {

        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        return service.processPayment(processPaymentObject)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(17, TimeUnit.SECONDS);
    }

    public static Observable<StatusOrders> getOrdersList(int userId) {

        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        return service.getOrdersList(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(17, TimeUnit.SECONDS);
    }
    public static Observable<Status> updateOrder(Order order) {

        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        return service.updateOrder(order)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(17, TimeUnit.SECONDS);
    }
}
