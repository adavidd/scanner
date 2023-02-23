package com.shapira.collectorscanner.server;

import android.util.Log;

import java.util.List;
import java.util.concurrent.TimeUnit;

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
    public static Observable<Pallet> getPallet(int palletId, int userId) {

        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        return service.getPallet(palletId,userId)
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
    public static Observable<Status> updateUserAppLogin(LoginUser loginUser) {

        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        return service.updateUserAppLogin(loginUser)
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
    public static Observable<Status> checkPackageAndAssign(PackageScan  package_scan) {

        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        return service.checkPackageAndAssign(package_scan)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(17, TimeUnit.SECONDS);
    }
    public static Observable<Status> deletePackageFromPallet(PackageScan  package_scan) {

        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        return service.deletePackageFromPallet(package_scan)
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
    public static Observable<Pallet> getPalletPackages(int palletid) {

        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        return service.getPalletPackages(palletid)
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
