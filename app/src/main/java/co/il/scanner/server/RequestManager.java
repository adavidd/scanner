package co.il.scanner.server;

import java.util.List;
import java.util.concurrent.TimeUnit;

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
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RequestManager {


    public static Observable<List<LoginUser>> getUsersLoginList() {

        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        return service.getUsersLogin()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(17, TimeUnit.SECONDS);
    }

    public static Observable<Orders> getOrders(int orderNumber) {

        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        return service.getOrders(orderNumber)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(17, TimeUnit.SECONDS);
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
}
