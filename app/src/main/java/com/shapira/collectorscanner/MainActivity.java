package com.shapira.collectorscanner;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.budiyev.android.codescanner.ScanMode;
import com.dinuscxj.progressbar.CircleProgressBar;
import com.google.zxing.Result;
import com.labters.lottiealertdialoglibrary.ClickListener;
import com.labters.lottiealertdialoglibrary.DialogTypes;
import com.labters.lottiealertdialoglibrary.LottieAlertDialog;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.shapira.collectorscanner.databinding.ActivityMainBinding;
import com.shapira.collectorscanner.login.LoginActivity;
import com.shapira.collectorscanner.model.CompleteOrder;
import com.shapira.collectorscanner.model.LoginUser;
import com.shapira.collectorscanner.model.NextOrder;
import com.shapira.collectorscanner.model.Order;
import com.shapira.collectorscanner.model.OrderItemsItem;
import com.shapira.collectorscanner.model.Package;
import com.shapira.collectorscanner.model.PackageScan;
import com.shapira.collectorscanner.model.Pallet;
import com.shapira.collectorscanner.model.ProcessPaymentObject;
import com.shapira.collectorscanner.model.Status;
import com.shapira.collectorscanner.model.StatusMessage;
import com.shapira.collectorscanner.model.StatusOrders;
import com.shapira.collectorscanner.model.UpdateItem;
import com.shapira.collectorscanner.server.RequestManager;
import com.shapira.collectorscanner.utility.Device;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

import static com.shapira.collectorscanner.Constants.*;
//import static co.il.scanner.Constants.LOGIN_USER;
//import static co.il.scanner.Constants.ORDERS_LIST_STATE;
//import static co.il.scanner.Constants.ORDER_VIEW_STATE;
//import static co.il.scanner.Constants.PALLET_SCAN_STATE;

public class MainActivity extends AppCompatActivity implements OrderAdapter.OrderAdapterListener,
        HandScanDialog.HandScanDialogListener,
        MyOrdersAdapter.MyOrdersAdapterListener,
        PackagesAdapter.PackageAdapterListener
{

    private final static String SCAN_ACTION_RCV = "scan.rcv.message";
    private final static String SCAN_ACTION_ZKC = "com.zkc.scancode";
    private static final int START_COLLECT = 2;
    private static final int REQUEST_CODE_FOR_CAMERA = 2347;
    //    private TextView mStartButton;
    private RecyclerView mRecyclerView;
    private OrderAdapter mOrderAdapter;
    private LoginUser mLoginUser;
    String barcodeStr = "";
    private LottieAnimationView mScanning;
    boolean isScanning = false;
    private ProgressBar mProgressBar;
    //    private LinearLayout mOrderLinear;
    private TextView mClientText;
    private List<OrderItemsItem> mOrdersList;
    private Dialog clientDetailsDialog;
    private Order mOrder;
    private Pallet mPallet;
    private RelativeLayout mClientboxRL;
    private LinearLayout mHandScanLL;
    private RelativeLayout mFinishdOrderRL;
    private TextView mFinishedTextTV;
    private ProgressBar mFinishedProgressBar;
    //    private TextView mMyOrdersTV;
//    private TextView mUserNameTV;
    private RecyclerView mMyOrdersListRV;
    private MyOrdersAdapter mMyOrdersAdapter;
    private PackagesAdapter mPackageAdapter;
    private CircleProgressBar mOrderCircleProgress;
    private CodeScanner mCodeScanner;
    private LinearLayout mCameraScannerLL;
    private CodeScannerView scannerView;
    private Ringtone successBeep;
    private Ringtone wrongBeep;
    private Ringtone buzzerBeep;
    //    int view_State = 0;
    ActivityMainBinding binding;
    int IDLE_DELAY_SECONDS =30;
    Date lastUserInteraction;
    Date lastItemCollection;
    String versionName;
    BroadcastReceiver palletBroadCastReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setViewState(INITIAL_USER_STATE);

        Log.d("chaim", "model" + new Device().getDevicemModel());
        if (new Device().getDevicemModel() == "IPDA035") {
            binding.setShowCameraScanBtn(false);
        } else {
            binding.setShowCameraScanBtn(true);
        }
        versionName = BuildConfig.VERSION_NAME;
        binding.setVersionName(versionName);
        initViews();
        initListeners();
        initScannerReceiver();
        initCameraScanner();
        initBeeps();

    }


    private void initViews() {
//        mUserNameTV = findViewById(R.id.UserNameTv);
        mLoginUser = (LoginUser) getIntent().getSerializableExtra(LOGIN_USER);
        binding.setMLoginUser(mLoginUser);
//        mStartButton = findViewById(R.id.MA_start_TV);
        mScanning = findViewById(R.id.MA_animationView_LAV);
        mRecyclerView = findViewById(R.id.MA_recycler_RV);
        mProgressBar = findViewById(R.id.MA_progress_bar_PB);
//        mOrderLinear = findViewById(R.id.MA_recycler_linear_LL);
        mClientText = findViewById(R.id.MA_client_text_TV);
        mClientboxRL = findViewById(R.id.AM_client_box_RL);
        mHandScanLL = findViewById(R.id.MA_hand_scann_LL);
        mFinishdOrderRL = findViewById(R.id.AM_save_RL);
        mFinishedTextTV = findViewById(R.id.MA_save_text_TV);
        mFinishedProgressBar = findViewById(R.id.MA_progress_bar_order_PB);
//        mMyOrdersTV = findViewById(R.id.MA_my_orders_TV);
        mMyOrdersListRV = findViewById(R.id.my_orders_list_RV);
        mOrderCircleProgress = findViewById(R.id.MA_progress_bar);
        mCameraScannerLL = findViewById(R.id.MA_camera_scann_LL);

//        binding.UserNameTv.setText(mLoginUser.getFirstname() + " " + mLoginUser.getLastname());

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

    private void initBeeps() {
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
        try {
            String uri = "android.resource://" + getPackageName() + "/" + R.raw.buzzer;
            Uri notification = Uri.parse(uri);
            buzzerBeep = RingtoneManager.getRingtone(getApplicationContext(), notification);
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
                        if (checkIfBarcodeExist(result.getText())) {
                            successBeep.play();
                            delay = 2000;
                        } else {
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

    private void getPalletBarcode(){
        binding.setViewState(PALLET_SCAN_STATE);
        LottieAlertDialog.Builder alert = new LottieAlertDialog.Builder(MainActivity.this, DialogTypes.TYPE_CUSTOM, "carry-trolley.json");
        alert.setDescription("על מנת לבצע פעולות במשטח , עליך לסרוק את הברקוד של המשטח");
        alert.setTitle("סרוק משטח");
        alert.setNegativeText("ביטול");
        alert.setNegativeTextColor(-1);
        alert.setNegativeButtonColor(Color.parseColor("#9e035d"));
        alert.setNegativeListener((lottieAlertDialog -> {
            lottieAlertDialog.dismiss();
            binding.setViewState(INITIAL_USER_STATE);

        }));

        LottieAlertDialog palletScanAlert = alert.build();
        palletScanAlert.setCancelable(false);
        palletScanAlert.setCanceledOnTouchOutside(false);
        palletScanAlert.show();
        Context activityContext = this;
         palletBroadCastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {


                String palletBarcodeStr;
                byte[] barocode = intent.getByteArrayExtra("barocode");
                int barocodelen = intent.getIntExtra("length", 0);
                byte temp = intent.getByteExtra("barcodeType", (byte) 0);
//                android.util.Log.i("debug", "----codetype--" + temp);
                palletBarcodeStr = new String(barocode, 0, barocodelen);
//                    Log.d("chaim","cartBarcode "  +cartbarcodeStr);
                if (palletBarcodeStr.endsWith("PALLET") && palletBarcodeStr.startsWith("MALBUSHEY")) {
                    try {
                        palletBarcodeStr = palletBarcodeStr.substring(0, palletBarcodeStr.length() - 6).substring(9);
                        RequestManager.getPallet(Integer.parseInt(palletBarcodeStr),mLoginUser.getId()).subscribe(new Observer<Pallet>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }
                            @Override
                            public void onNext(Pallet pallet) {
                                setCurrentPallet(pallet);
                                binding.setViewState(PACKAGES_SCAN_STATE);
                            }

                            @Override
                            public void onError(Throwable e) {

                            }
                            @Override
                            public void onComplete() {

                            }
                        });
                        palletScanAlert.dismiss();
                        binding.PalletTitle.setText("משטח " + Integer.parseInt(palletBarcodeStr));
                        activityContext.unregisterReceiver(this);

                    } catch (NumberFormatException ex) {
                        Toast.makeText(getApplicationContext(), "הברקוד " +palletBarcodeStr  +" שנסרק אינו ברקוד משטח תקין", Toast.LENGTH_LONG).show();
                    }
                } else {
//                        cartScanAlert.
                    Toast.makeText(getApplicationContext(),  " הברקוד" + palletBarcodeStr +  " שנסרק אינו ברקוד משטח תקין", Toast.LENGTH_LONG).show();
                }


            }
        };

        IntentFilter filter = new IntentFilter();
        filter.addAction(SCAN_ACTION_RCV);
        activityContext.registerReceiver(palletBroadCastReceiver, filter);

    }
    private void getCartId(Order order) {
        Log.d("chaim", "order " + order.toString());
        initOrderRecyclerView(order);
        if (order.getStatusId() > 2) {
//            initOrderRecyclerView(order);
            return;
        }
        LottieAlertDialog.Builder alert = new LottieAlertDialog.Builder(MainActivity.this, DialogTypes.TYPE_CUSTOM, "cart_1.json");
        alert.setDescription("על מנת להתחיל בליקוט, עליך לסרוק את הברקוד של העגלה");
        alert.setTitle("סרוק עגלה");
        alert.setNegativeText("ביטול ביצוע ליקוט");
        alert.setNegativeTextColor(-1);
        alert.setNegativeButtonColor(Color.parseColor("#9e035d"));
        alert.setNegativeListener((lottieAlertDialog -> {
            Log.d("chaim"," current mOrder StatusId " + mOrder.getStatusId());
            if(mOrder.getStatusId() ==2  ||mOrder.getStatusId() ==1){

                if(!(mOrder.getOrderItems().stream().anyMatch(orderItemsItem -> orderItemsItem.getCollectedQuantity()>0)) && mOrder.getCollectingUserId() == mLoginUser.getId()) {
                    mOrder.setStatusId(1);
                    mOrder.setCollectingUserId(null);
                    postOrderToSever(mOrder);
                }
            }

            lottieAlertDialog.dismiss();
            binding.setViewState(INITIAL_USER_STATE);

        }));

        LottieAlertDialog cartScanAlert = alert.build();
        cartScanAlert.setCancelable(false);
        cartScanAlert.setCanceledOnTouchOutside(false);
        cartScanAlert.show();
        Context activityContext = this;
        BroadcastReceiver cartBroadCastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {


                String cartbarcodeStr;
                byte[] barocode = intent.getByteArrayExtra("barocode");
                int barocodelen = intent.getIntExtra("length", 0);
                byte temp = intent.getByteExtra("barcodeType", (byte) 0);
//                android.util.Log.i("debug", "----codetype--" + temp);
                cartbarcodeStr = new String(barocode, 0, barocodelen);
//                    Log.d("chaim","cartBarcode "  +cartbarcodeStr);
                if (cartbarcodeStr.endsWith("CART") && cartbarcodeStr.startsWith("MALBUSHEY")) {
                    try {
                        cartbarcodeStr = cartbarcodeStr.substring(0, cartbarcodeStr.length() - 4);
                        cartbarcodeStr = cartbarcodeStr.substring(9);
                        int cart_id = Integer.parseInt(cartbarcodeStr);
                        mOrder.setCartId(cart_id);
                        order.setCartId(cart_id);
                        initOrderRecyclerView(order);
                        cartScanAlert.dismiss();
//                        LocalBroadcastManager.getInstance(context).unregisterReceiver(this);
                        activityContext.unregisterReceiver(this);


                    } catch (NumberFormatException ex) {
                        Toast.makeText(getApplicationContext(), "הברקוד שנסרק אינו ברקוד עגלה תקין", Toast.LENGTH_LONG).show();
                    }
                } else {
//                        cartScanAlert.
                    Toast.makeText(getApplicationContext(), "הברקוד שנסרק אינו ברקוד עגלה תקין", Toast.LENGTH_LONG).show();
                }


            }
        };

        IntentFilter filter = new IntentFilter();
        filter.addAction(SCAN_ACTION_RCV);
        activityContext.registerReceiver(cartBroadCastReceiver, filter);


    }
    private void  postOrderToSever(Order order){
        RequestManager.updateOrder(order).subscribe(new Observer<Status>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull Status status) {
                if(status.toString() == "ok"){
                    Toast.makeText(MainActivity.this, "ההזמנה עודכנה", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
            }

            @Override
            public void onComplete() {

            }
        });
    };
    private void initOrderRecyclerView(Order order) {
//        mOrderLinear.setVisibility(View.VISIBLE);
        binding.setViewState(ORDER_VIEW_STATE);
        if (order.getCustomer() != null) {
            String clientNumber = "הזמנה: " + order.getId();
            mClientText.setText(clientNumber);
            binding.cartNumberTV.setText(order.getCartId() + "");
        }
        mOrdersList = order.getOrderItems();
        setCircleProgressBar();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mOrderAdapter = new OrderAdapter(this, this, mOrdersList);

        mOrderAdapter.setHasStableIds(true);
        mRecyclerView.setAdapter(mOrderAdapter);


        if (order.getStatusId() == 3) {

            mFinishedTextTV.setText("לחץ לחיוב הזמנה");

        } else if (order.getStatusId() == 4) {

            mFinishedTextTV.setText("ההזמנה שולמה");

        } else if (order.getStatusId() == 2 || order.getStatusId() == 1) {

            mFinishedTextTV.setText("סיום הזמנה");

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
        if (ordersCompleted >= mOrdersList.size()) {

            if (scannerView.getVisibility() == View.VISIBLE) {
                mCodeScanner.stopPreview();
                scannerView.setVisibility(View.GONE);
            }
        }

    }


    @Override
    public void onBackPressed() {

        if (binding.getViewState() == ORDER_VIEW_STATE) {

            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this, R.style.AlertDialogCustom).create();
            alertDialog.setTitle("לצאת מההזמנה?");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "כן",
                    (dialog, which) -> {
                        dialog.dismiss();
//                        mOrderLinear.setVisibility(View.GONE);
                        scannerView.setVisibility(View.GONE);
//                        mStartButton.setVisibility(View.VISIBLE);
//                        mUserNameTV.setVisibility(View.VISIBLE);
                        binding.setViewState(INITIAL_USER_STATE);
//                        mMyOrdersTV.setVisibility(View.VISIBLE);
                    });

            alertDialog.show();
        }else if(binding.getViewState() == PACKAGES_SCAN_STATE){
            binding.setViewState(INITIAL_USER_STATE);
            scannerView.setVisibility(View.GONE);
        }else if(binding.getViewState() == PALLET_SCAN_STATE){
            binding.setViewState(INITIAL_USER_STATE);
            scannerView.setVisibility(View.GONE);
            this.unregisterReceiver(palletBroadCastReceiver);
        } else if (mMyOrdersListRV.getVisibility() == View.VISIBLE) {

            mMyOrdersListRV.setVisibility(View.GONE);
//            mStartButton.setVisibility(View.VISIBLE);
//            mMyOrdersTV.setVisibility(View.VISIBLE);
//            mUserNameTV.setVisibility(View.VISIBLE);
            binding.setViewState(INITIAL_USER_STATE);

        } else {

            super.onBackPressed();

        }

    }

    private void initListeners() {
        binding.scanPallet.setOnClickListener(view->{
            getPalletBarcode();
        });
        binding.MAStartTV.setOnClickListener(view -> {
//            mUserNameTV.setVisibility(View.GONE);
//            view_State = ORDER_VIEW_STATE;


            mProgressBar.setVisibility(View.VISIBLE);
//            mStartButton.setVisibility(View.GONE);
//            mMyOrdersTV.setVisibility(View.GONE);

            getOrdersFromServer();


        });
        binding.OrgOrdersListTV.setOnClickListener(view -> {
//            startActivity(new Intent(this, OrgOrdersList.class));
            Intent intent = new Intent(this, OrgOrdersListActivity.class);
            intent.putExtra(LOGIN_USER, mLoginUser);
            startActivity(intent);
        });

        mClientboxRL.setOnClickListener(view -> {


            ClientDialog clientDialog = new ClientDialog();
            clientDialog.showDialog(this, mOrder);

        });
        binding.materialIconLogout.setOnClickListener(view -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finishAffinity();
        });

        mHandScanLL.setOnClickListener(view -> {
            if (scannerView.getVisibility() == View.VISIBLE) {
                mCodeScanner.stopPreview();
                scannerView.setVisibility(View.GONE);
            }
            HandScanDialog handScanDialog = new HandScanDialog();


            handScanDialog.showDialog(MainActivity.this, MainActivity.this);

        });


        mFinishdOrderRL.setOnClickListener(view -> {

            if (mOrder.getStatusId() == 3) {

                finishOrder();

            } else if (mOrder.getStatusId() == 2) {


                if (finishedAllCollection()) {

                    finishOrder();

                } else {

                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this, R.style.AlertDialogCustom).create();
                    alertDialog.setTitle("לסיים הזמנה?");
                    alertDialog.setMessage("לא השלמת את כל הליקוט. בטוח שברצונך לסיים?");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "כן",
                            (dialog, which) -> {
                                dialog.dismiss();
                                final EditText input = new EditText(this);
                                input.setSingleLine(true);
                                input.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

                                new AlertDialog.Builder(MainActivity.this)
                                        .setTitle("אישור סיום הזמנה")
                                        .setMessage("הזן קוד:")
                                        .setView(input)

                                        .setPositiveButton("אישור", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int whichButton) {


                                                if(input.getText().toString().equals("1212")){
                                                    //openUser(loginUser);
                                                    mFinishdOrderRL.setBackground(ContextCompat.getDrawable(MainActivity.this, R.color.teal_700));

                                                    finishOrder();
                                                }else{
                                                    Toast.makeText(MainActivity.this,  "הקוד שגוי", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }).setNegativeButton("ביטול", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int whichButton) {
                                                // Do nothing.
                                            }
                                        }).show();


                            });

                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "לא",
                            (dialog, which) -> {
                                dialog.dismiss();

                            });

                    alertDialog.show();

                }
            }


        });

        binding.myOrdersLL.setOnClickListener(view -> {
            setMyOrdersListRecycler();
        });


        mCameraScannerLL.setOnClickListener(view -> {

            if (checkIfAlreadyHavePermission(this, Manifest.permission.CAMERA)) {
                if (scannerView.getVisibility() == View.VISIBLE) {
                    mCodeScanner.stopPreview();
                    scannerView.setVisibility(View.GONE);
                } else {
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
        LottieAlertDialog.Builder alert = new LottieAlertDialog.Builder(MainActivity.this, DialogTypes.TYPE_CUSTOM, "loading.json");
        alert.setDescription("המערכת מקבלת רשימת הזמנות קיימות מהשרת");
        alert.setTitle("שליפת רשימות הזמנות");


        LottieAlertDialog getOrdersListProgressDialog = alert.build();

        getOrdersListProgressDialog.show();

        RequestManager.getOrdersList(mLoginUser.getId()).subscribe(new Observer<StatusOrders>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull StatusOrders statusOrders) {
                getOrdersListProgressDialog.dismiss();
                if (statusOrders.getOrdersList()!=null && statusOrders.getOrdersList().size() > 0) {
//                    view_State = ORDERS_LIST_STATE;
                    binding.setViewState(ORDERS_LIST_STATE);
                    mMyOrdersListRV.setVisibility(View.VISIBLE);
                    mMyOrdersListRV.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                    mMyOrdersAdapter = new MyOrdersAdapter(MainActivity.this, MainActivity.this, statusOrders.getOrdersList());

                    mMyOrdersAdapter.setHasStableIds(true);

                    mMyOrdersListRV.setAdapter(mMyOrdersAdapter);

                } else {
                    Toast.makeText(MainActivity.this, "אין היסטוריית הזמנות", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                getOrdersListProgressDialog.dismiss();
                Toast.makeText(MainActivity.this, "תקלה בשליפת הזמנות", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {
                getOrdersListProgressDialog.dismiss();
            }
        });


    }

    boolean paymentOrderInProgress = false;

    private void finishOrder() {
        Context activityContext = this;
        if (mOrder.getStatusId() == 2) {

            CompleteOrder completeOrder = new CompleteOrder();
            completeOrder.setOrderId(mOrder.getId());
            completeOrder.setUserId(mLoginUser.getId());
            completeOrder.setOrderItems(mOrdersList);

            mFinishedProgressBar.setVisibility(View.VISIBLE);

            RequestManager.updateCompleteOrderCollection(completeOrder).subscribe(new Observer<Status>() {
                @Override
                public void onSubscribe(@NonNull Disposable d) {

                }

                @Override
                public void onNext(@NonNull Status status) {

                    mFinishedTextTV.setText("בצע חיוב אשראי על ההזמנה");
                    mOrder.setStatusId(3);
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


        } else if (mOrder.getStatusId() == 3) {


            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this, R.style.AlertDialogCustom).create();
            alertDialog.setTitle("ביצוע תשלום");
            alertDialog.setMessage("האם בטוח שברצונך לחייב תשלום על הזמנה זו?");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "כן",
                    (dialog, which) -> {
                        dialog.dismiss();

                        mFinishedProgressBar.setVisibility(View.VISIBLE);
                        LottieAlertDialog.Builder alert = new LottieAlertDialog.Builder(MainActivity.this, DialogTypes.TYPE_CUSTOM, "barcode-scanner-file.json");
                        alert.setDescription("על מנת לבצע חיוב על ההזמנה, עליך לגשת לעמדת ההדפסה ולסרוק את הברקוד שעל העמדה");
                        alert.setTitle("סרוק ברקוד עמדת הדפסה");
                        alert.setNegativeText("ביטול ביצוע תשלום");
                        alert.setNegativeTextColor(-1);
                        alert.setNegativeButtonColor(Color.parseColor("#9e035d"));
                        alert.setNegativeListener((lottieAlertDialog -> {
                            lottieAlertDialog.dismiss();
                            mFinishedProgressBar.setVisibility(View.GONE);
                            paymentOrderInProgress =false;
//                            binding.setViewState(INITIAL_USER_STATE);
                        }));

                        LottieAlertDialog printScanAlert = alert.build();
                        printScanAlert.setCancelable(false);
                        printScanAlert.setCanceledOnTouchOutside(false);
                        printScanAlert.show();

                        BroadcastReceiver printerBroadCastReceiver = new BroadcastReceiver() {
                            @Override
                            public void onReceive(Context context, Intent intent) {
                                String cartbarcodeStr;
                                byte[] barocode = intent.getByteArrayExtra("barocode");
                                int barocodelen = intent.getIntExtra("length", 0);
                                byte temp = intent.getByteExtra("barcodeType", (byte) 0);
                                cartbarcodeStr = new String(barocode, 0, barocodelen);
                                if (cartbarcodeStr.endsWith("PRINT") && cartbarcodeStr.startsWith("MALBUSHEY")) {
                                    try {
                                        cartbarcodeStr = cartbarcodeStr.substring(0, cartbarcodeStr.length() - 5);

                                        cartbarcodeStr = cartbarcodeStr.substring(9);

                                        int printer_id = Integer.parseInt(cartbarcodeStr);

                                        printScanAlert.dismiss();

                                        activityContext.unregisterReceiver(this);
                                        ProcessPayment(printer_id);
                                    } catch (NumberFormatException ex) {
                                        Toast.makeText(getApplicationContext(), "הברקוד שנסרק אינו ברקוד עמדת הדפסה תקין", Toast.LENGTH_LONG).show();
                                    }
                                } else {
//                        cartScanAlert.
                                    Toast.makeText(getApplicationContext(), "הברקוד שנסרק אינו ברקוד עגלה תקין", Toast.LENGTH_LONG).show();
                                }


                            }
                        };

                        IntentFilter filter = new IntentFilter();
                        filter.addAction(SCAN_ACTION_RCV);
                        activityContext.registerReceiver(printerBroadCastReceiver, filter);





                    });
            alertDialog.show();


        }

    }

    private void ProcessPayment(int printer_id) {

        ProcessPaymentObject processPaymentObject = new ProcessPaymentObject();
        processPaymentObject.setPrinterId(printer_id);
        processPaymentObject.setOrderId(mOrder.getId());
        processPaymentObject.setUserId(mLoginUser.getId());
        if (paymentOrderInProgress) {
            Toast.makeText(MainActivity.this, "אנא המתן באמצע לבצע תשלום", Toast.LENGTH_LONG).show();
            return;
        }
        paymentOrderInProgress = true;
        LottieAlertDialog.Builder alert = new LottieAlertDialog.Builder(MainActivity.this, DialogTypes.TYPE_CUSTOM, "online-payment.json");
        alert.setDescription("נא המתינו בזמן שהמערכת מבצעת את החיוב");
        alert.setTitle("חיוב אשראי");
//        alert.setNegativeText("ביטול ביצוע תשלום");
//        alert.setNegativeTextColor(-1);
//        alert.setNegativeButtonColor(Color.parseColor("#9e035d"));
//        alert.setNegativeListener((lottieAlertDialog -> {
//            lottieAlertDialog.dismiss();
////                            binding.setViewState(INITIAL_USER_STATE);
//        }));

        LottieAlertDialog paymentProcessingAlert = alert.build();
        paymentProcessingAlert.setCancelable(false);
        paymentProcessingAlert.setCanceledOnTouchOutside(false);
        paymentProcessingAlert.show();
        RequestManager.processPayment(processPaymentObject).subscribe(new Observer<StatusMessage>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull StatusMessage statusMessage) {
                paymentOrderInProgress = false;
                paymentProcessingAlert.dismiss();
                if (statusMessage.getStatus() == "ok") {

                    LottieAlertDialog.Builder alert = new LottieAlertDialog.Builder(MainActivity.this, DialogTypes.TYPE_CUSTOM, "credit-card-success.json");
                    alert.setDescription(" סיבה:" + statusMessage.getMessage());
                    alert.setTitle( "התשלום עבר בהצלחה, ההזמנה הושלמה!");
//                    alert.setNegativeButtonColor(Color.parseColor("#9e035d"));
                    alert.setPositiveListener((lottieAlertDialog -> {
                        lottieAlertDialog.dismiss();
                        binding.setViewState(INITIAL_USER_STATE);
                        mFinishedProgressBar.setVisibility(View.GONE);

                    }));

                    LottieAlertDialog PaymentSuccessdAlert = alert.build();
                    PaymentSuccessdAlert.setCancelable(false);
                    PaymentSuccessdAlert.setCanceledOnTouchOutside(false);
                    PaymentSuccessdAlert.show();


//                    Toast.makeText(MainActivity.this, "התשלום עבר בהצלחה, ההזמנה הושלמה ", Toast.LENGTH_LONG).show();
//
//                    new Handler().postDelayed(() -> {
//
////                                        mOrderLinear.setVisibility(View.GONE);
////                                        mStartButton.setVisibility(View.VISIBLE);
////                                        mMyOrdersTV.setVisibility(View.VISIBLE);
////                                        view_State = INITIAL_USER_STATE;
//                        binding.setViewState(INITIAL_USER_STATE);
//                        mFinishedProgressBar.setVisibility(View.GONE);
//
//                    }, 1000);
                } else {

                    LottieAlertDialog.Builder alert = new LottieAlertDialog.Builder(MainActivity.this, DialogTypes.TYPE_CUSTOM, "credit-card-failed.json");
                    alert.setDescription(" סיבה:" + statusMessage.getMessage());
                    alert.setTitle( "התשלום נכשל אנא העבר את ההזמנה לטיפול מנהל");

                    alert.setPositiveText("אישור");
                    alert.setPositiveListener((lottieAlertDialog -> {
                        lottieAlertDialog.dismiss();
                        mFinishedProgressBar.setVisibility(View.GONE);

                        binding.setViewState(INITIAL_USER_STATE);
                    }));

                    LottieAlertDialog PaymentFailedAlert = alert.build();
                    PaymentFailedAlert.setCancelable(false);
                    PaymentFailedAlert.setCanceledOnTouchOutside(false);
                    PaymentFailedAlert.show();


//                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this, R.style.AlertDialogCustom).create();
//                    alertDialog.setTitle("התשלום נכשל");
//                    alertDialog.setMessage("אנא העבר את ההזמנה לטיפול מנהל");
//                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "אישור",
//                            (dialog, which) -> {
//                                dialog.dismiss();
//                                mFinishedProgressBar.setVisibility(View.GONE);
//
//                                binding.setViewState(INITIAL_USER_STATE);
//                            });
//
//                    alertDialog.show();
                }

            }

            @Override
            public void onError(@NonNull Throwable e) {

                paymentProcessingAlert.dismiss();

                paymentOrderInProgress = false;
                LottieAlertDialog.Builder alert = new LottieAlertDialog.Builder(MainActivity.this, DialogTypes.TYPE_CUSTOM, "credit-card-failed.json");
                alert.setDescription(" סיבה: תקלת תקשורת");
                alert.setTitle( "התשלום נכשל אנא העבר את ההזמנה לטיפול מנהל");
                alert.setPositiveText("אישור");
                alert.setPositiveListener((lottieAlertDialog -> {
                    lottieAlertDialog.dismiss();
                    mFinishedProgressBar.setVisibility(View.GONE);

                    binding.setViewState(INITIAL_USER_STATE);
                }));

                LottieAlertDialog PaymentFailedAlert = alert.build();
                PaymentFailedAlert.setCancelable(false);
                PaymentFailedAlert.setCanceledOnTouchOutside(false);
                PaymentFailedAlert.show();






            }

            @Override
            public void onComplete() {
                paymentProcessingAlert.dismiss();
                paymentOrderInProgress = false;
            }
        });


    }


    private boolean finishedAllCollection() {

        boolean finishedAllCollections = true;

        for (int i = 0; i < mOrdersList.size(); i++) {

            if (mOrdersList.get(i).getCollectedQuantity() != mOrdersList.get(i).getOrderQuantity() && !mOrdersList.get(i).getItem().isHide_in_app() ) {

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
//                    Log.d("chaim","got next  order id" +  nextOrder.getOrder().getId());
                    if(nextOrder.getOrder()!=null) {
                        getOrder(nextOrder.getOrder().getId());
                    }
                } else {
                    LottieAlertDialog.Builder alert = new LottieAlertDialog.Builder(MainActivity.this, DialogTypes.TYPE_CUSTOM, "sad-face.json");
                    alert.setDescription(nextOrder.getMessage());
                    alert.setTitle("");
                    alert.setPositiveText("אוקי");
                    alert.setPositiveListener(new ClickListener() {
                        @Override
                        public void onClick(@androidx.annotation.NonNull LottieAlertDialog lottieAlertDialog) {
                            mProgressBar.setVisibility(View.GONE);
//                            mStartButton.setVisibility(View.VISIBLE);
//                            mMyOrdersTV.setVisibility(View.VISIBLE);
//                            view_State = INITIAL_USER_STATE;
                            binding.setViewState(INITIAL_USER_STATE);
                            lottieAlertDialog.dismiss();
                        }
                    });

                    alert.build().show();
//                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this, R.style.AlertDialogCustom).create();
//                    alertDialog.setMessage(nextOrder.getMessage());
//                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "אוקי",
//                            (dialog, which) -> {
//                                dialog.dismiss();
//                                mProgressBar.setVisibility(View.GONE);
//                                mStartButton.setVisibility(View.VISIBLE);
//                                mMyOrdersTV.setVisibility(View.VISIBLE);
//                            });
//
//                    alertDialog.show();
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
        Log.d("chaim","getOrder " + nextOrderId);
//        Toast.makeText(getApplicationContext(),"getOrder Order #" + nextOrderId,Toast.LENGTH_SHORT).show();
        RequestManager.getOrder(nextOrderId,mLoginUser.getId()).subscribe(new Observer<Order>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull Order order) {


                List<OrderItemsItem> filteredList =  order.getOrderItems().stream().filter(c->c.getItem() != null && !c.getItem().isHide_in_app()).collect(Collectors.toList());
                order.setOrderItems(filteredList);
                mOrder = order;

//                view_State = ORDER_VIEW_STATE;


                mProgressBar.setVisibility(View.GONE);

                getCartId(order);
//                initOrderRecyclerView(order);

            }

            @Override
            public void onError(@NonNull Throwable e) {
                mProgressBar.setVisibility(View.GONE);
//                mStartButton.setVisibility(View.VISIBLE);
//                mMyOrdersTV.setVisibility(View.VISIBLE);
//                mUserNameTV.setVisibility(View.VISIBLE);
//                view_State =INITIAL_USER_STATE;
                binding.setViewState(INITIAL_USER_STATE);
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

                if (inCollectionMode() || inPalletScanMode()|| inPackagesScanMode()) {

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

        return binding.getViewState() == ORDER_VIEW_STATE && mOrder.getStatusId() == 2;


    }

    public boolean inPalletScanMode() {

        return binding.getViewState() == PALLET_SCAN_STATE;


    }
    public boolean inPackagesScanMode() {

        return binding.getViewState() == PACKAGES_SCAN_STATE;


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
                    if (inCollectionMode()) {
                        checkIfBarcodeExist(barcodeStr);
                    }
                    if(inPackagesScanMode()){
                        checkPackageBracodeAndAssign(barcodeStr);
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

        //unregisterReceiver(broadcastReceiver);
        IntentFilter filter = new IntentFilter();
        filter.addAction(SCAN_ACTION_RCV);
        registerReceiver(broadcastReceiver, filter);
        //unregisterReceiver(broadcastReceiver1);
        IntentFilter filter1 = new IntentFilter();
        filter1.addAction(SCAN_ACTION_ZKC);
        registerReceiver(broadcastReceiver1, filter1);

    }

    private int checkPackageBracodeAndAssign(String barcodeStr){
            int result =0;
            if(barcodeStr.startsWith("MALBUSHEY") && barcodeStr.endsWith("PACKAGE")){

                int package_id= Integer.parseInt(barcodeStr.substring(0, barcodeStr.length() - 7).substring(9));
                Toast.makeText(this, " מס חבילה" + package_id,Toast.LENGTH_LONG).show();
                mProgressBar.setVisibility(View.VISIBLE);
                RequestManager.checkPackageAndAssign(new PackageScan(package_id,mPallet.getId(),mLoginUser.getId())).subscribe(new Observer<Status>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Status status) {
                        if(status.status !=null  && status.status.equals("ok")){
                            successBeep.play();
                            blink(Color.GREEN);
                            getPalletPackages();
                        }else{
                            vibrate(2*1000);
                            blink(Color.RED);
                            buzzerBeep.play();
                            Toast.makeText(MainActivity.this,status.message,Toast.LENGTH_LONG).show();
                        }
                        mProgressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mProgressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
            }else{
                Toast.makeText(this,"הברקוד שנסרק אינו ברקוד של חבילה",Toast.LENGTH_LONG).show();
                result =0;
            }
            return result;
    }
    void getPalletPackages(){
        RequestManager.getPallet(mPallet.getId(),mLoginUser.getId()).subscribe(new Observer<Pallet>() {

            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Pallet pallet) {
                setCurrentPallet(pallet);
            }



            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }
    void setCurrentPallet(Pallet pallet){
        mPallet =pallet;
        binding.setMPallet(pallet);
        binding.PalletPackagesRV.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        mPackageAdapter = new PackagesAdapter(MainActivity.this, MainActivity.this,mPallet.getPackages());
        binding.PalletPackagesRV.setAdapter(mPackageAdapter);
        binding.packageCounterText.setText(String.valueOf(mPallet.getPackages().size()));
    }
    private boolean checkIfBarcodeExist(String barcodeStr) {
        //ast.makeText(this,barcodeStr,Toast.LENGTH_SHORT);
        String origBarcodeStr = barcodeStr;
        barcodeStr = barcodeStr.replaceAll("\\s+","").replace("*","").replace("*","").replace("/J","").replace("/j","");;
       boolean itemMatched = false;
        if (mOrdersList != null) {

            for (int i = 0; i < mOrdersList.size(); i++) {

                if (mOrdersList.get(i).getItem() != null) {

                    String barcode1 = mOrdersList.get(i).getItem().getBarcode1();
                    String barcode2 = mOrdersList.get(i).getItem().getBarcode2();

                    if (barcode1 != null && barcode1.replaceAll("\\s+","").replace("*","").replace("*","").equals(barcodeStr) || barcode2 != null && barcode2.replaceAll("\\s+","").replace("*","").replace("*","").equals(barcodeStr)) {
                        itemMatched = true;
                        if (mOrdersList.get(i).getOrderQuantity() <= mOrdersList.get(i).getCollectedQuantity()) {
                            blink(Color.RED);
                            vibrate(2000);

                        } else {
                            mOrdersList.get(i).setCollectedQuantity(mOrdersList.get(i).getCollectedQuantity() + 1);
                            mOrderAdapter.notifyDataSetChanged();
                            if (mOrdersList.size() < 100) {
                                if (i == 0) {

                                    mRecyclerView.smoothScrollToPosition(0);

                                } else if (i > 0 && i < mOrdersList.size() - 1) {
                                    mRecyclerView.smoothScrollToPosition(i + 1);
                                } else {
                                    mRecyclerView.smoothScrollToPosition(i);

                                }
                            }
                            setCircleProgressBar();
                            UpdateServer(mOrdersList.get(i));
                            blink(Color.GREEN);

                        }
                        break;
                    }else{


                    }
                }


            }
            if (!itemMatched) {
                //Toast.makeText(this,barcodeStr,Toast.LENGTH_SHORT);
                Toast.makeText(this,origBarcodeStr,Toast.LENGTH_SHORT);
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
        updateItem.setCartId(mOrder.getCartId());

        RequestManager.updateItemCollection(updateItem).subscribe(new Observer<Status>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull Status status) {
                if(status.status.equals("err")){
                    LottieAlertDialog.Builder alert = new LottieAlertDialog.Builder(MainActivity.this, DialogTypes.TYPE_CUSTOM, "warning.json");
                    alert.setDescription(status.message);
                    alert.setTitle("אירעה שגיאה בליקוט פריט " + orderItemsItem.getItem().getName());
                    alert.setNegativeText("סגור");
                    alert.setNegativeTextColor(-1);
                    alert.setNegativeButtonColor(Color.parseColor("#9e035d"));
                    alert.setNegativeListener((lottieAlertDialog -> {
                        lottieAlertDialog.dismiss();
                    }));

                    LottieAlertDialog cartScanAlert = alert.build();
                    cartScanAlert.setCancelable(false);
                    cartScanAlert.setCanceledOnTouchOutside(false);
                    cartScanAlert.show();
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


    @SuppressLint("MissingPermission")
    private void vibrate(int ms) {
        Vibrator v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(ms);
        blink(Color.RED);
    }


    private void blink(int c) {
        if(binding.getViewState() ==ORDER_VIEW_STATE ) {
            final LinearLayout layout = (LinearLayout) findViewById(R.id.MA_recycler_linear_LL);
//        final LinearLayout layout2 = (LinearLayout) findViewById(R.id.PalletPackagesRV);
            final AnimationDrawable drawable = new AnimationDrawable();
            final Handler handler = new Handler();


            drawable.addFrame(new ColorDrawable(c), 400);
            drawable.addFrame(new ColorDrawable(Color.WHITE), 400);
            drawable.setOneShot(true);

            layout.setBackgroundDrawable(drawable);
//        layout2.setBackgroundDrawable(drawable);
            handler.postDelayed(() -> drawable.start(), 100);
        } else if(binding.getViewState() == PACKAGES_SCAN_STATE || binding.getViewState() == PALLET_SCAN_STATE){
            final LinearLayout layout = (LinearLayout) findViewById(R.id.LL_Pallet);
//        final LinearLayout layout2 = (LinearLayout) findViewById(R.id.PalletPackagesRV);
            final AnimationDrawable drawable = new AnimationDrawable();
            final Handler handler = new Handler();


            drawable.addFrame(new ColorDrawable(c), 400);
            drawable.addFrame(new ColorDrawable(Color.WHITE), 400);
            drawable.setOneShot(true);

            layout.setBackgroundDrawable(drawable);
//        layout2.setBackgroundDrawable(drawable);
            handler.postDelayed(() -> drawable.start(), 100);
        }
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
        if(orderItemsItem.getCollectedQuantity() > 0) {
            orderItemsItem.setCollectedQuantity(orderItemsItem.getCollectedQuantity() - 1);
            mOrderAdapter.notifyDataSetChanged();
            setCircleProgressBar();
            UpdateServer(orderItemsItem);
        }
    }


    @Override
    public void onMyOrdersItemListClicked() {

        mMyOrdersListRV.setVisibility(View.GONE);
        //view_State = ORDER_VIEW_STATE ;
        binding.setViewState(ORDER_VIEW_STATE);

    }


    @Override
    public void onMyListItemClicked(Order order) {

        mMyOrdersListRV.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
//        mStartButton.setVisibility(View.GONE);
//        mUserNameTV.setVisibility(View.GONE);
//        view_State = ORDER_VIEW_STATE ;
//        mMyOrdersTV.setVisibility(View.GONE);
        binding.setViewState(ORDER_VIEW_STATE);
        getOrder(order.getId());

    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        if(lastUserInteraction != null) {
            long diffInMillies = new Date().getTime()-lastUserInteraction.getTime() ;
            Log.d("chaim","user inactive for: " + diffInMillies);
            int inActiveMinutes = (int) (diffInMillies/1000/60);

            if (inActiveMinutes>15 ) {
                Toast.makeText(getApplicationContext(), "לא היית פעיל " + (int) inActiveMinutes + " דקות הינך מועבר למסך בחירת משתמשים", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finishAffinity();
            }
        }
        lastUserInteraction = new Date();
    }

    @Override
    public void onPackageItemClicked() {

    }

    @Override
    public void onPackageItemClicked(Package pckg) {


    }

    @Override
    public void onPackageDeleteClicked(Package pckg) {
        LottieAlertDialog.Builder alert = new LottieAlertDialog.Builder(MainActivity.this, DialogTypes.TYPE_CUSTOM, "question.json");
        alert.setDescription("האם אתה בטוח שברצונך למחוק את החבילה מהמשטח");
        alert.setTitle("מחיקת חבילה ממשטח");
        alert.setNegativeText("ביטול");
        alert.setPositiveText("מחק");
        alert.setPositiveTextColor(-1);
        alert.setPositiveButtonColor(ContextCompat.getColor(this, R.color.purple_200));
        alert.setNegativeTextColor(-1);
        alert.setNegativeButtonColor(ContextCompat.getColor(this, R.color.teal_700));
        alert.setNegativeListener(lottieAlertDialog -> {
            lottieAlertDialog.dismiss();

        });
        alert.setPositiveListener(lottieAlertDialog -> {
            lottieAlertDialog.dismiss();
            RequestManager.deletePackageFromPallet(new PackageScan(pckg.getId(),mPallet.getId(),mLoginUser.getId())).subscribe(new Observer<Status>() {

                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(Status status) {
                        getPalletPackages();
                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onComplete() {

                }
            });
        });
        LottieAlertDialog deletePckgAlert = alert.build();
        deletePckgAlert.setCancelable(true);
        deletePckgAlert.show();

    }

//    Handler _idleHandler = new Handler();
//    Runnable _idleRunnable = new Runnable() {
//        @Override
//        public void run() {
//            Toast.makeText(getApplicationContext(),"לא היית פעיל יותר מ" + IDLE_DELAY_SECONDS + " שניות",Toast.LENGTH_LONG).show();
//            //handle your IDLE state
//        }
//    };
//
//    private void delayedIdle(int delaySeconds) {
//        _idleHandler.removeCallbacks(_idleRunnable);
//        _idleHandler.postDelayed(_idleRunnable, ( 1000 * delaySeconds));
//    }

}
