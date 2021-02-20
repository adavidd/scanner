package co.il.scanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

import java.util.List;


import co.il.scanner.model.CompleteOrder;
import co.il.scanner.model.LoginUser;
import co.il.scanner.model.NextOrder;
import co.il.scanner.model.OrderItemsItem;
import co.il.scanner.model.Orders;
import co.il.scanner.model.ProcessPaymentObject;
import co.il.scanner.model.Status;
import co.il.scanner.model.StatusMessage;
import co.il.scanner.model.StatusOrders;
import co.il.scanner.model.UpdateItem;
import co.il.scanner.server.RequestManager;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

import static co.il.scanner.Constants.LOGIN_USER;

public class MainActivity extends AppCompatActivity implements OrderAdapter.OrderAdapterListener,
                                            HandScanDialog.HandScanDialogListener,
                                            MyOrdersAdapter.MyOrdersAdapterListener {

    private final static String SCAN_ACTION_RCV = "scan.rcv.message";
    private final static String SCAN_ACTION_ZKC = "com.zkc.scancode";
    private static final int START_COLLECT = 2;
    private TextView mStartButton;
    private RecyclerView mRecyclerView;
    private OrderAdapter mOrderAdapter;
    private LoginUser mLoginUser;
    String barcodeStr = "";
    private LottieAnimationView mScanning;
    boolean isScanning = false;
    private ProgressBar mProgressBar;
    private LinearLayout mRecyclerLinear;
    private TextView mClientText;
    private List<OrderItemsItem> mOrdersList;
    private Dialog clientDetailsDialog;
    private Orders mOrders;
    private RelativeLayout mClientboxRL;
    private LinearLayout mHandScanLL;
    private RelativeLayout mFinishdOrderRL;
    private TextView mFinishedTextTV;
    private ProgressBar mFinishedProgressBar;
    private TextView mMyOrdersTV;
    private RecyclerView mMyOrdersListRV;
    private MyOrdersAdapter mMyOrdersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initViews();
        initListeners();
        initScannerReceiver();

    }


    private void initViews() {

        mLoginUser = (LoginUser) getIntent().getSerializableExtra(LOGIN_USER);
        mStartButton = findViewById(R.id.MA_start_TV);
        mScanning = findViewById(R.id.MA_animationView_LAV);
        mRecyclerView = findViewById(R.id.MA_recycler_RV);
        mProgressBar = findViewById(R.id.MA_progress_bar_PB);
        mRecyclerLinear = findViewById(R.id.MA_recycler_linear_LL);
        mClientText = findViewById(R.id.MA_client_text_TV);
        mClientboxRL = findViewById(R.id.AM_client_box_RL);
        mHandScanLL = findViewById(R.id.MA_hand_scann_LL);
        mFinishdOrderRL = findViewById(R.id.AM_save_RL);
        mFinishedTextTV = findViewById(R.id.MA_save_text_TV);
        mFinishedProgressBar = findViewById(R.id.MA_progress_bar_order_PB);
        mMyOrdersTV = findViewById(R.id.MA_my_orders_TV);
        mMyOrdersListRV = findViewById(R.id.my_orders_list_RV);


        Toast.makeText(this, "שלום " + mLoginUser.getFirstname(), Toast.LENGTH_SHORT).show();
    }


    private void initRecyclerView(Orders orders) {

        mRecyclerLinear.setVisibility(View.VISIBLE);

        String clientNumber = orders.getCustomer().getTz1();
        mClientText.setText(clientNumber);

        mOrdersList = orders.getOrderItems();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mOrderAdapter = new OrderAdapter(this, this, mOrdersList);
        mRecyclerView.setAdapter(mOrderAdapter);

        if (orders.getStatusId() == 3){

            mFinishedTextTV.setText("סיים תשלום");

        }else if (orders.getStatusId() == 4){

            mFinishedTextTV.setText("ההזמנה הושלמה");

        }else if (orders.getStatusId() == 2 || orders.getStatusId() == 1){

            mFinishedTextTV.setText("סיים הזמנה");

        }

    }


    @Override
    public void onBackPressed() {

        if (mRecyclerLinear.getVisibility() == View.VISIBLE) {

            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this, R.style.AlertDialogCustom).create();
            alertDialog.setTitle("לצאת מההזמנה?");
            alertDialog.setMessage("כל הנתונים יאבדו");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "כן",
                    (dialog, which) -> {
                        dialog.dismiss();
                        mRecyclerLinear.setVisibility(View.GONE);
                        mStartButton.setVisibility(View.VISIBLE);
                        mMyOrdersTV.setVisibility(View.VISIBLE);
                    });

            alertDialog.show();



        }

        else if (mMyOrdersListRV.getVisibility() == View.VISIBLE){

            mMyOrdersListRV.setVisibility(View.GONE);
            mStartButton.setVisibility(View.VISIBLE);
            mMyOrdersTV.setVisibility(View.VISIBLE);

        }
        else {

            super.onBackPressed();

        }

    }

    private void initListeners() {

        mStartButton.setOnClickListener(view -> {

            mProgressBar.setVisibility(View.VISIBLE);
            mStartButton.setVisibility(View.GONE);
            mMyOrdersTV.setVisibility(View.GONE);

            getOrdersFromServer();


        });


        mClientboxRL.setOnClickListener(view -> {


            ClientDialog clientDialog = new ClientDialog();
            clientDialog.showDialog(this, mOrders);

        });


        mHandScanLL.setOnClickListener(view -> {

            HandScanDialog handScanDialog = new HandScanDialog();
            handScanDialog.showDialog(MainActivity.this, MainActivity.this);
        });



        mFinishdOrderRL.setOnClickListener(view -> {

            finishOrder();


        });

        mMyOrdersTV.setOnClickListener(view -> {


            setMyOrdersListRecycler();

        });


    }





    private void setMyOrdersListRecycler() {

        RequestManager.getOrdersList(mLoginUser.getId()).subscribe(new Observer<StatusOrders>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull StatusOrders statusOrders) {

                    mMyOrdersListRV.setVisibility(View.VISIBLE);
                    mMyOrdersListRV.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                    mMyOrdersAdapter = new MyOrdersAdapter(MainActivity.this, MainActivity.this, statusOrders.getOrdersList());
                    mMyOrdersListRV.setAdapter(mMyOrdersAdapter);
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });




    }



    private void finishOrder() {

        if (mOrders.getStatusId() == 2){

            CompleteOrder completeOrder = new CompleteOrder();
            completeOrder.setOrderId(mOrders.getId());
            completeOrder.setUserId(mLoginUser.getId());
            completeOrder.setOrderItems(mOrdersList);

            mFinishedProgressBar.setVisibility(View.VISIBLE);

            RequestManager.updateCompleteOrderCollection(completeOrder).subscribe(new Observer<Status>() {
                @Override
                public void onSubscribe(@NonNull Disposable d) {

                }

                @Override
                public void onNext(@NonNull Status status) {

                    mFinishedTextTV.setText("סיים תשלום");
                    mOrders.setStatusId(3);
                    mFinishedProgressBar.setVisibility(View.GONE);

                }

                @Override
                public void onError(@NonNull Throwable e) {

                    Toast.makeText(MainActivity.this, "בעיה בהשלמת ההזמנה", Toast.LENGTH_SHORT).show();
                    mFinishedProgressBar.setVisibility(View.GONE);

                }

                @Override
                public void onComplete() {

                }
            });

        }

        else if (mOrders.getStatusId() == 3){

            mFinishedProgressBar.setVisibility(View.VISIBLE);

            ProcessPaymentObject processPaymentObject = new ProcessPaymentObject();
            processPaymentObject.setOrderId(mOrders.getId());
            processPaymentObject.setUserId(mLoginUser.getId());

            RequestManager.processPayment(processPaymentObject).subscribe(new Observer<StatusMessage>() {
                @Override
                public void onSubscribe(@NonNull Disposable d) {

                }

                @Override
                public void onNext(@NonNull StatusMessage statusMessage) {

                    Toast.makeText(MainActivity.this, "ההזמנה הושלמה", Toast.LENGTH_SHORT).show();

                    new Handler().postDelayed(() -> {

                        mRecyclerLinear.setVisibility(View.GONE);
                        mStartButton.setVisibility(View.VISIBLE);
                        mMyOrdersTV.setVisibility(View.VISIBLE);
                        mFinishedProgressBar.setVisibility(View.GONE);

                    }, 1000);


                }

                @Override
                public void onError(@NonNull Throwable e) {

                    mFinishedProgressBar.setVisibility(View.GONE);

                }

                @Override
                public void onComplete() {

                }
            });


        }

    }


    private void getOrdersFromServer() {


        RequestManager.getNextOrder(mLoginUser.getId(), START_COLLECT).subscribe(new Observer<NextOrder>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull NextOrder nextOrder) {

                if (nextOrder.getStatus().equals("ok")){

                    getOrder(nextOrder.getOrder().getId());
                }else {

                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this,  R.style.AlertDialogCustom).create();
                    alertDialog.setMessage(nextOrder.getMessage());
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "אוקי",
                            (dialog, which) -> {
                                dialog.dismiss();
                            });

                    alertDialog.show();
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });





    }




    private void getOrder(int nextOrderId) {


        RequestManager.getOrders(nextOrderId).subscribe(new Observer<Orders>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull Orders orders) {

                mOrders = orders;
                initRecyclerView(orders);
                mProgressBar.setVisibility(View.GONE);

            }

            @Override
            public void onError(@NonNull Throwable e) {
                mProgressBar.setVisibility(View.GONE);
                mStartButton.setVisibility(View.VISIBLE);
                mMyOrdersTV.setVisibility(View.VISIBLE);


            }

            @Override
            public void onComplete() {

            }
        });



    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent e) {

        if (e.getAction() == KeyEvent.ACTION_DOWN) {

            if (e.getKeyCode() == 301 || e.getKeyCode() == 302 || e.getKeyCode() == 303) {
                Log.i("chaim", "ACTION_DOWN: ");
                mScanning.setVisibility(View.VISIBLE);
                if (!isScanning) {
                    mScanning.playAnimation();
                    isScanning = true;
                }
            }
        }
        if (e.getAction() == KeyEvent.ACTION_UP) {

            if (e.getKeyCode() == 301 || e.getKeyCode() == 302 || e.getKeyCode() == 303) {
                Log.i("chaim", "ACTION_UP");
                mScanning.setVisibility(View.GONE);
                mScanning.cancelAnimation();
                isScanning = false;

            }
        }

        return super.

                dispatchKeyEvent(e);

    }

    private void initScannerReceiver() {


        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {


                byte[] barocode = intent.getByteArrayExtra("barocode");
                int barocodelen = intent.getIntExtra("length", 0);
                byte temp = intent.getByteExtra("barcodeType", (byte) 0);
                android.util.Log.i("debug", "----codetype--" + temp);
                barcodeStr = new String(barocode, 0, barocodelen);
                mScanning.cancelAnimation();
                checkIfBarcodeExist(barcodeStr);

                Toast.makeText(context, barcodeStr, Toast.LENGTH_SHORT).show();
            }
        };
        

        BroadcastReceiver broadcastReceiver1 = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {


                barcodeStr = intent.getExtras().getString("code");
                mScanning.cancelAnimation();
                checkIfBarcodeExist(barcodeStr);
                Toast.makeText(context, barcodeStr, Toast.LENGTH_SHORT).show();
            }
        };

        IntentFilter filter = new IntentFilter();
        filter.addAction(SCAN_ACTION_RCV);
        registerReceiver(broadcastReceiver, filter);

        IntentFilter filter1 = new IntentFilter();
        filter1.addAction(SCAN_ACTION_ZKC);
        registerReceiver(broadcastReceiver1, filter1);

    }



    private void checkIfBarcodeExist(String barcodeStr) {
        boolean itemMatched = false;
        if (mOrdersList != null){

            for (int i = 0; i < mOrdersList.size(); i++) {

                if (mOrdersList.get(i).getItem() != null){

                    String barcode1 = mOrdersList.get(i).getItem().getBarcode1();
                    String barcode2 = mOrdersList.get(i).getItem().getBarcode2();

                    if (barcode1 != null && barcode1.equals(barcodeStr) || barcode2 != null && barcode2.equals(barcodeStr)) {
                        itemMatched = true;
                        if(mOrdersList.get(i).getOrderQuantity() <= mOrdersList.get(i).getCollectedQuantity()){
                            blink(Color.RED);
                            vibrate(2000);
                        }else {
                            mOrdersList.get(i).setCollectedQuantity(mOrdersList.get(i).getCollectedQuantity() + 1);
                            mOrderAdapter.notifyDataSetChanged();
                            UpdateServer(mOrdersList.get(i));
                            blink(Color.GREEN);

                        }
                        break;
                    }
                }



            }
            if(!itemMatched){
                vibrate(500);
            }
        }



    }





    private void UpdateServer(OrderItemsItem orderItemsItem) {

        UpdateItem updateItem = new UpdateItem();
        updateItem.setItemId(orderItemsItem.getItemId());
        updateItem.setOrderId(orderItemsItem.getOrderId());
        updateItem.setUserId(mOrders.getId());
        updateItem.setQuantity(orderItemsItem.getCollectedQuantity());


        RequestManager.updateItemCollection(updateItem).subscribe(new Observer<Status>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull Status status) {

            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });



    }




    private  void vibrate(int ms){
        Vibrator v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(ms);
        blink(Color.RED);
    }


    private void blink(int c){
        final LinearLayout layout = (LinearLayout) findViewById(R.id.MA_recycler_linear_LL);
        final AnimationDrawable drawable = new AnimationDrawable();
        final Handler handler = new Handler();


        drawable.addFrame(new ColorDrawable(c), 400);
        drawable.addFrame(new ColorDrawable(Color.WHITE), 400);
        drawable.setOneShot(true);

        layout.setBackgroundDrawable(drawable);
        handler.postDelayed(() -> drawable.start(), 100);
    }




    @Override
    public void onHandScanClicked(String handScan) {

        checkIfBarcodeExist(handScan);


    }




    @Override
    public void onDeleteClicked(OrderItemsItem orderItemsItem) {

        orderItemsItem.setCollectedQuantity(orderItemsItem.getCollectedQuantity() - 1);
        mOrderAdapter.notifyDataSetChanged();
        UpdateServer(orderItemsItem);


    }


    @Override
    public void onMyOrdersItemListClicked() {

        mMyOrdersListRV.setVisibility(View.GONE);


    }



    @Override
    public void onMyListItemClicked(Orders orders) {

        mMyOrdersListRV.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
        mStartButton.setVisibility(View.GONE);
        mMyOrdersTV.setVisibility(View.GONE);
        getOrder(orders.getId());

    }


}
