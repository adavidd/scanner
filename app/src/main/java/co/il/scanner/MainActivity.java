package co.il.scanner;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.budiyev.android.codescanner.ScanMode;
import com.dinuscxj.progressbar.CircleProgressBar;
import com.google.zxing.Result;

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
    private static final int REQUEST_CODE_FOR_CAMERA = 2347;
    private TextView mStartButton;
    private RecyclerView mRecyclerView;
    private OrderAdapter mOrderAdapter;
    private LoginUser mLoginUser;
    String barcodeStr = "";
    private LottieAnimationView mScanning;
    boolean isScanning = false;
    private ProgressBar mProgressBar;
    private LinearLayout mOrderLinear;
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
    private CircleProgressBar mOrderCircleProgress;
    private CodeScanner mCodeScanner;
    private LinearLayout mCameraScannerLL;
    private CodeScannerView scannerView;
    private Ringtone successBeep;
    private Ringtone wrongBeep;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initViews();
        initListeners();
        initScannerReceiver();
        initCameraScanner();
        initBeeps();

    }


    private void initViews() {

        mLoginUser = (LoginUser) getIntent().getSerializableExtra(LOGIN_USER);
        mStartButton = findViewById(R.id.MA_start_TV);
        mScanning = findViewById(R.id.MA_animationView_LAV);
        mRecyclerView = findViewById(R.id.MA_recycler_RV);
        mProgressBar = findViewById(R.id.MA_progress_bar_PB);
        mOrderLinear = findViewById(R.id.MA_recycler_linear_LL);
        mClientText = findViewById(R.id.MA_client_text_TV);
        mClientboxRL = findViewById(R.id.AM_client_box_RL);
        mHandScanLL = findViewById(R.id.MA_hand_scann_LL);
        mFinishdOrderRL = findViewById(R.id.AM_save_RL);
        mFinishedTextTV = findViewById(R.id.MA_save_text_TV);
        mFinishedProgressBar = findViewById(R.id.MA_progress_bar_order_PB);
        mMyOrdersTV = findViewById(R.id.MA_my_orders_TV);
        mMyOrdersListRV = findViewById(R.id.my_orders_list_RV);
        mOrderCircleProgress = findViewById(R.id.MA_progress_bar);
        mCameraScannerLL = findViewById(R.id.MA_camera_scann_LL);


    }


    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();

    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }

    private void initBeeps(){
        try {
            String uri = "android.resource://" + getPackageName() + "/" + R.raw.beep;
            Uri notification = Uri.parse(uri);
            successBeep = RingtoneManager.getRingtone(getApplicationContext(), notification);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            String uri = "android.resource://" + getPackageName() + "/" + R.raw.wrongbeep;
            Uri notification = Uri.parse(uri);
            wrongBeep = RingtoneManager.getRingtone(getApplicationContext(), notification);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void initCameraScanner() {

        scannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setAutoFocusEnabled(true);
        mCodeScanner.setScanMode(ScanMode.SINGLE);
        mCodeScanner.setTouchFocusEnabled(true);
//        mCodeScanner.autoFocusMode  =true;
//        mCodeScanner.scanMode = ScanMode.CONTINUOUS;
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        scannerView.setVisibility(View.GONE);
                        mCodeScanner.stopPreview();
                        int delay = 500;
                        if(checkIfBarcodeExist(result.getText())){
                            successBeep.play();
                            delay = 2000;
                        }else{
                            wrongBeep.play();
                        }

                        new Handler().postDelayed(() -> {
                            scannerView.setVisibility(View.VISIBLE);
                            mCodeScanner.startPreview();
                        }, delay);


                        //scannerView.setVisibility(View.GONE); //commented to continiue scanning
                        Toast.makeText(MainActivity.this, result.getText(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }


    private void initRecyclerView(Orders orders) {

        mOrderLinear.setVisibility(View.VISIBLE);


        if (orders.getCustomer() != null) {

            String clientNumber = "הזמנה: " + orders.getId();
            mClientText.setText(clientNumber);
        }

        mOrdersList = orders.getOrderItems();
        setCircleProgressBar();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mOrderAdapter = new OrderAdapter(this, this, mOrdersList);
        mRecyclerView.setAdapter(mOrderAdapter);

        if (orders.getStatusId() == 3) {

            mFinishedTextTV.setText("ביצוע תשלום");

        } else if (orders.getStatusId() == 4) {

            mFinishedTextTV.setText("ההזמנה הושלמה");

        } else if (orders.getStatusId() == 2 || orders.getStatusId() == 1) {

            mFinishedTextTV.setText("סיים הזמנה");

        }

    }


    private void setCircleProgressBar() {

        int ordersCompleted = 0;

        for (int i = 0; i < mOrdersList.size(); i++) {

            if (mOrdersList.get(i).getCollectedQuantity() == mOrdersList.get(i).getOrderQuantity()) {
                ordersCompleted++;
            }
        }

        if (ordersCompleted > 0) {
            mOrderCircleProgress.setProgress((int) ((float) ordersCompleted / (float) mOrdersList.size() * 100));
        } else {
            mOrderCircleProgress.setProgress(0);
        }
        if( ordersCompleted>=mOrdersList.size()){

            if(scannerView.getVisibility() == View.VISIBLE){
                mCodeScanner.stopPreview();
                scannerView.setVisibility(View.GONE);
            }
        }

    }


    @Override
    public void onBackPressed() {

        if (mOrderLinear.getVisibility() == View.VISIBLE) {

            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this, R.style.AlertDialogCustom).create();
            alertDialog.setTitle("לצאת מההזמנה?");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "כן",
                    (dialog, which) -> {
                        dialog.dismiss();
                        mOrderLinear.setVisibility(View.GONE);
                        scannerView.setVisibility(View.GONE);
                        mStartButton.setVisibility(View.VISIBLE);
                        mMyOrdersTV.setVisibility(View.VISIBLE);
                    });

            alertDialog.show();


        } else if (mMyOrdersListRV.getVisibility() == View.VISIBLE) {

            mMyOrdersListRV.setVisibility(View.GONE);
            mStartButton.setVisibility(View.VISIBLE);
            mMyOrdersTV.setVisibility(View.VISIBLE);

        } else {

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
            if(scannerView.getVisibility() == View.VISIBLE){
                mCodeScanner.stopPreview();
                scannerView.setVisibility(View.GONE);
            }
            HandScanDialog handScanDialog = new HandScanDialog();
            handScanDialog.showDialog(MainActivity.this, MainActivity.this);
        });


        mFinishdOrderRL.setOnClickListener(view -> {

            if (mOrders.getStatusId() == 3) {

                finishOrder();

            } else if (mOrders.getStatusId() == 2) {


                if (finishedAllCollection()) {

                    finishOrder();

                } else {

                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this, R.style.AlertDialogCustom).create();
                    alertDialog.setTitle("לסיים הזמנה?");
                    alertDialog.setMessage("לא השלמת את כל הליקוט. בטוח שברצונך לסיים?");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "כן",
                            (dialog, which) -> {
                                dialog.dismiss();
                                mFinishdOrderRL.setBackground(ContextCompat.getDrawable(this, R.color.teal_700));
                                finishOrder();

                            });

                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "לא",
                            (dialog, which) -> {
                                dialog.dismiss();

                            });

                    alertDialog.show();

                }
            }


        });

        mMyOrdersTV.setOnClickListener(view -> {


            setMyOrdersListRecycler();

        });


        mCameraScannerLL.setOnClickListener(view -> {

            if (checkIfAlreadyHavePermission(this, Manifest.permission.CAMERA)) {
                    if(scannerView.getVisibility() == View.VISIBLE){
                        mCodeScanner.stopPreview();
                        scannerView.setVisibility(View.GONE);
                    }else {
                        mCodeScanner.startPreview();
                        scannerView.setVisibility(View.VISIBLE);
                    }
            } else {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                        REQUEST_CODE_FOR_CAMERA);
            }

        });

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @androidx.annotation.NonNull String[] permissions, @androidx.annotation.NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_FOR_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mCodeScanner.startPreview();
                scannerView.setVisibility(View.VISIBLE);
            }
        }

    }

    public static boolean checkIfAlreadyHavePermission(Context context, String... permissions) {
        for (String permission : permissions) {
            int result = ContextCompat.checkSelfPermission(context, permission);
            if (result != PackageManager.PERMISSION_GRANTED)
                return false;
        }

        return true;
    }


    private void setMyOrdersListRecycler() {

        RequestManager.getOrdersList(mLoginUser.getId()).subscribe(new Observer<StatusOrders>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull StatusOrders statusOrders) {

                if (statusOrders.getOrdersList().size() > 0) {

                    mMyOrdersListRV.setVisibility(View.VISIBLE);
                    mMyOrdersListRV.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                    mMyOrdersAdapter = new MyOrdersAdapter(MainActivity.this, MainActivity.this, statusOrders.getOrdersList());
                    mMyOrdersListRV.setAdapter(mMyOrdersAdapter);
                } else {
                    Toast.makeText(MainActivity.this, "אין היסטוריית הזמנות", Toast.LENGTH_SHORT).show();
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

    boolean paymentOrderInProgress = false;
    private void finishOrder() {

        if (mOrders.getStatusId() == 2) {

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


        } else if (mOrders.getStatusId() == 3) {


            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this, R.style.AlertDialogCustom).create();
            alertDialog.setTitle("ביצוע תשלום");
            alertDialog.setMessage("האם בטוח שברצונך לחייב תשלום על הזמנה זו?");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "כן",
                    (dialog, which) -> {
                        dialog.dismiss();

                        mFinishedProgressBar.setVisibility(View.VISIBLE);

                        ProcessPaymentObject processPaymentObject = new ProcessPaymentObject();
                        processPaymentObject.setOrderId(mOrders.getId());
                        processPaymentObject.setUserId(mLoginUser.getId());
                        if(paymentOrderInProgress){
                            Toast.makeText(MainActivity.this, "ההזמנה הושלמה", Toast.LENGTH_LONG).show();
                            return;
                        }
                        paymentOrderInProgress =true;

                        RequestManager.processPayment(processPaymentObject).subscribe(new Observer<StatusMessage>() {
                            @Override
                            public void onSubscribe(@NonNull Disposable d) {

                            }

                            @Override
                            public void onNext(@NonNull StatusMessage statusMessage) {
                                paymentOrderInProgress =false;
                                if(statusMessage.getStatus() =="ok") {
                                    Toast.makeText(MainActivity.this, "ההזמנה הושלמה", Toast.LENGTH_LONG).show();

                                    new Handler().postDelayed(() -> {

                                        mOrderLinear.setVisibility(View.GONE);
                                        mStartButton.setVisibility(View.VISIBLE);
                                        mMyOrdersTV.setVisibility(View.VISIBLE);
                                        mFinishedProgressBar.setVisibility(View.GONE);

                                    }, 1000);
                                }else{
                                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this, R.style.AlertDialogCustom).create();
                                    alertDialog.setTitle("התשלום נכשל");
                                    alertDialog.setMessage("אנא העבר את ההזמנה לטיפול מנהל");
                                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "אישור",
                                            (dialog, which) -> {
                                                dialog.dismiss();
                                                mFinishedProgressBar.setVisibility(View.GONE);
                                                mOrderLinear.setVisibility(View.GONE);
                                                mStartButton.setVisibility(View.VISIBLE);
                                                mMyOrdersTV.setVisibility(View.VISIBLE);

                                            });

                                    alertDialog.show();
                                }

                            }

                            @Override
                            public void onError(@NonNull Throwable e) {

                                paymentOrderInProgress =false;
                                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this, R.style.AlertDialogCustom).create();
                                alertDialog.setTitle("התשלום נכשל");
                                alertDialog.setMessage("אנא העבר את ההזמנה לטיפול מנהל");
                                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "אישור",
                                        (dialog, which) -> {
                                            dialog.dismiss();
                                            mFinishedProgressBar.setVisibility(View.GONE);
                                            mOrderLinear.setVisibility(View.GONE);
                                            mStartButton.setVisibility(View.VISIBLE);
                                            mMyOrdersTV.setVisibility(View.VISIBLE);

                                        });

                                alertDialog.show();


                            }

                            @Override
                            public void onComplete() {
                                paymentOrderInProgress =false;
                            }
                        });


                    });

            alertDialog.show();


        }

    }


    private boolean finishedAllCollection() {

        boolean finishedAllCollections = true;

        for (int i = 0; i < mOrdersList.size(); i++) {

            if (mOrdersList.get(i).getCollectedQuantity() != mOrdersList.get(i).getOrderQuantity()) {

                finishedAllCollections = false;
            }

        }

        return finishedAllCollections;


    }


    private void getOrdersFromServer() {


        RequestManager.getNextOrder(mLoginUser.getId(), START_COLLECT).subscribe(new Observer<NextOrder>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull NextOrder nextOrder) {

                if (nextOrder.getStatus().equals("ok")) {

                    getOrder(nextOrder.getOrder().getId());
                } else {

                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this, R.style.AlertDialogCustom).create();
                    alertDialog.setMessage(nextOrder.getMessage());
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "אוקי",
                            (dialog, which) -> {
                                dialog.dismiss();
                                mProgressBar.setVisibility(View.GONE);
                                mStartButton.setVisibility(View.VISIBLE);
                                mMyOrdersTV.setVisibility(View.VISIBLE);
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

    private void showScannerDialog() {

        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this, R.style.AlertDialogCustom).create();
        alertDialog.setMessage("אפשר לסרוק רק כשהזמנה פתוחה");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "אוקי",
                (dialog, which) -> {
                    dialog.dismiss();
                });

        alertDialog.show();

    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent e) {

        if (e.getAction() == KeyEvent.ACTION_DOWN) {


            if (e.getKeyCode() == 301 || e.getKeyCode() == 302 || e.getKeyCode() == 303) {

                if (inCollectionMode()) {

                    Log.i("chaim", "ACTION_DOWN: ");
                    mScanning.setVisibility(View.VISIBLE);
                    if (!isScanning) {
                        mScanning.playAnimation();
                        isScanning = true;
                    }
                } else {

                    showScannerDialog();
                    vibrate(500);

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


    public boolean inCollectionMode() {

        return mOrderLinear.getVisibility() == View.VISIBLE && mOrders.getStatusId() == 2;


    }


    private void initScannerReceiver() {


        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                if (inCollectionMode()) {


                    byte[] barocode = intent.getByteArrayExtra("barocode");
                    int barocodelen = intent.getIntExtra("length", 0);
                    byte temp = intent.getByteExtra("barcodeType", (byte) 0);
                    android.util.Log.i("debug", "----codetype--" + temp);
                    barcodeStr = new String(barocode, 0, barocodelen);
                    mScanning.cancelAnimation();
                    checkIfBarcodeExist(barcodeStr);

                    Toast.makeText(context, barcodeStr, Toast.LENGTH_SHORT).show();

                }
            }
        };


        BroadcastReceiver broadcastReceiver1 = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                if (inCollectionMode()) {

                    barcodeStr = intent.getExtras().getString("code");
                    mScanning.cancelAnimation();
                    checkIfBarcodeExist(barcodeStr);

                }
            }
        };

        IntentFilter filter = new IntentFilter();
        filter.addAction(SCAN_ACTION_RCV);
        registerReceiver(broadcastReceiver, filter);

        IntentFilter filter1 = new IntentFilter();
        filter1.addAction(SCAN_ACTION_ZKC);
        registerReceiver(broadcastReceiver1, filter1);

    }


    private boolean checkIfBarcodeExist(String barcodeStr) {
        boolean itemMatched = false;
        if (mOrdersList != null) {

            for (int i = 0; i < mOrdersList.size(); i++) {

                if (mOrdersList.get(i).getItem() != null) {

                    String barcode1 = mOrdersList.get(i).getItem().getBarcode1();
                    String barcode2 = mOrdersList.get(i).getItem().getBarcode2();

                    if (barcode1 != null && barcode1.equals(barcodeStr) || barcode2 != null && barcode2.equals(barcodeStr)) {
                        itemMatched = true;
                        if (mOrdersList.get(i).getOrderQuantity() <= mOrdersList.get(i).getCollectedQuantity()) {
                            blink(Color.RED);
                            vibrate(2000);

                        } else {
                            mOrdersList.get(i).setCollectedQuantity(mOrdersList.get(i).getCollectedQuantity() + 1);
                            mOrderAdapter.notifyDataSetChanged();

                            if (i == 0) {
                                mRecyclerView.smoothScrollToPosition(0);
                            } else if (i > 0 && i < mOrdersList.size() - 1) {
                                mRecyclerView.smoothScrollToPosition(i + 1);
                            } else {
                                mRecyclerView.smoothScrollToPosition(i);

                            }
                            setCircleProgressBar();
                            UpdateServer(mOrdersList.get(i));
                            blink(Color.GREEN);

                        }
                        break;
                    }
                }


            }
            if (!itemMatched) {

                vibrate(500);
            }
        }
        return itemMatched;


    }


    private void UpdateServer(OrderItemsItem orderItemsItem) {

        UpdateItem updateItem = new UpdateItem();
        updateItem.setItemId(orderItemsItem.getItemId());
        updateItem.setOrderId(orderItemsItem.getOrderId());
        updateItem.setUserId(mLoginUser.getId());
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


    private void vibrate(int ms) {
        Vibrator v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(ms);
        blink(Color.RED);
    }


    private void blink(int c) {
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

        orderItemsItem.setCollectedQuantity(0);
        mOrderAdapter.notifyDataSetChanged();
        setCircleProgressBar();
        UpdateServer(orderItemsItem);


    }


    @Override
    public void onMinusClicked(OrderItemsItem orderItemsItem) {

        orderItemsItem.setCollectedQuantity(orderItemsItem.getCollectedQuantity() - 1);
        mOrderAdapter.notifyDataSetChanged();
        setCircleProgressBar();
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
