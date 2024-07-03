package com.shapira.collectorscanner;

import static com.shapira.collectorscanner.Constants.LOGIN_USER;

import static com.shapira.collectorscanner.Constants.ORG_ORDER;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.awesomedialog.AwesomeDialog;
import com.labters.lottiealertdialoglibrary.DialogTypes;
import com.labters.lottiealertdialoglibrary.LottieAlertDialog;
import com.shapira.collectorscanner.adapters.OrgOrdersListAdapter;
import com.shapira.collectorscanner.databinding.ActivityOrgOrdersListBinding;
import com.shapira.collectorscanner.model.LoginUser;
import com.shapira.collectorscanner.model.Status;
import com.shapira.collectorscanner.model.orgorder.OrgOrder;
import com.shapira.collectorscanner.server.RequestManager;

import java.io.Serializable;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

public class OrgOrdersListActivity extends AppCompatActivity {
    private LoginUser mLoginUser;
    ActivityOrgOrdersListBinding    binding;

    @Override
    protected void onResume() {
        super.onResume();
        initOrdersRecyclerView();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_org_orders_list);
        binding = ActivityOrgOrdersListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mLoginUser = (LoginUser) getIntent().getSerializableExtra(LOGIN_USER);
        binding.materialIconReturn.setOnClickListener(v -> finish());
        initOrdersRecyclerView();


    }
    private void initOrdersRecyclerView() {
        LottieAlertDialog.Builder alert = new LottieAlertDialog.Builder(OrgOrdersListActivity.this, DialogTypes.TYPE_CUSTOM, "loading.json");
        alert.setDescription("המערכת מקבלת רשימת הזמנות קיימות מהשרת");
        alert.setTitle("שליפת רשימות הזמנות");
        LottieAlertDialog getOrdersListProgressDialog = alert.build();

        getOrdersListProgressDialog.show();

        RequestManager.getOrgOrdersList(mLoginUser.getId()).subscribe(new Observer<List<OrgOrder>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull List<OrgOrder> orgOrders) {
                getOrdersListProgressDialog.dismiss();
                if (orgOrders.size() == 0) {
                    Toast.makeText(OrgOrdersListActivity.this, "אין הזמנות פתוחות", Toast.LENGTH_SHORT).show();
                } else {
                    binding.OrgOrdersListRv.setLayoutManager(new LinearLayoutManager(OrgOrdersListActivity.this));
                    binding.OrgOrdersListRv.setAdapter(new OrgOrdersListAdapter(orgOrders, new OrgOrdersListAdapter.OrgOrdersAdapterListener() {
                        @Override
                        public void onOrgOrderClicked(OrgOrder orgOrder) {
                            //Toast.makeText(OrgOrdersListActivity.this, "הזמנה מספר " + orgOrder.getId(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(OrgOrdersListActivity.this, OrgOrderActivity.class);
                            intent.putExtra(LOGIN_USER, mLoginUser);
                            intent.putExtra(ORG_ORDER,  orgOrder);
                            startActivity(intent);
                        }

                        @Override
                        public void onOrgOrderPrintClicked(OrgOrder orgOrder) {
                            RequestManager.printOrgOrder(orgOrder.getId()).subscribe(new Observer<Status>() {
                                @Override
                                public void onSubscribe(@NonNull Disposable d) {

                                }

                                @Override
                                public void onNext(@NonNull Status s) {
                                    if (s.status.equals("ok")){
                                        Toast.makeText(OrgOrdersListActivity.this, "ההזמנה נשלחה להדפסה", Toast.LENGTH_SHORT).show();



                                    } else {
                                        Toast.makeText(OrgOrdersListActivity.this, "שגיאה בשליחת ההזמנה להדפסה", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onError(@NonNull Throwable e) {
                                    Toast.makeText(OrgOrdersListActivity.this, "שגיאה בשליחת ההזמנה להדפסה", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onComplete() {

                                }
                            });
                        }
                    }, OrgOrdersListActivity.this));
//                    binding.ordersRecyclerView.setLayoutManager(new LinearLayoutManager(OrgOrdersList.this));
//                    binding.ordersRecyclerView.setAdapter(new OrgOrdersListAdapter(orgOrders, OrgOrdersList.this));
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                getOrdersListProgressDialog.dismiss();
                Toast.makeText(OrgOrdersListActivity.this, "שגיאה בשליפת רשימת הזמנות", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {

            }
        });


    }
}