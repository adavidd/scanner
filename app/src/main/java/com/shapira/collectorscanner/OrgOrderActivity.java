package com.shapira.collectorscanner;

import static com.shapira.collectorscanner.Constants.LOGIN_USER;

import static com.shapira.collectorscanner.Constants.ORDER_VIEW_STATE;
import static com.shapira.collectorscanner.Constants.ORG_ORDER;
import static com.shapira.collectorscanner.Constants.PACKAGES_SCAN_STATE;
import static com.shapira.collectorscanner.Constants.PALLET_SCAN_STATE;
import static com.shapira.collectorscanner.MainActivity.checkIfAlreadyHavePermission;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.DecodeCallback;
import com.budiyev.android.codescanner.ScanMode;
import com.google.zxing.Result;
import com.labters.lottiealertdialoglibrary.DialogTypes;
import com.labters.lottiealertdialoglibrary.LottieAlertDialog;
import com.shapira.collectorscanner.adapters.OrgOrderItemListAdapter;
import com.shapira.collectorscanner.adapters.OrgOrdersListAdapter;
import com.shapira.collectorscanner.databinding.ActivityOrgOrderBinding;
import com.shapira.collectorscanner.databinding.ActivityOrgOrdersListBinding;
import com.shapira.collectorscanner.model.LoginUser;
import com.shapira.collectorscanner.model.OrderItemsItem;
import com.shapira.collectorscanner.model.Status;
import com.shapira.collectorscanner.model.UpdateItem;
import com.shapira.collectorscanner.model.orgorder.OrgOrder;
import com.shapira.collectorscanner.model.orgorder.OrgOrderItemsItem;
import com.shapira.collectorscanner.server.RequestManager;
import com.shapira.collectorscanner.utility.BarcodeString;
import com.shapira.collectorscanner.utility.Device;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

public class OrgOrderActivity extends AppCompatActivity implements HandScanDialog.HandScanDialogListener {
    private LoginUser mLoginUser;
    private OrgOrder mOrgOrder;
    private OrgOrderItemListAdapter mOrgOrderItemListAdapter ;
    ActivityOrgOrderBinding binding;
    private Ringtone successBeep;
    private Ringtone wrongBeep;
    private Ringtone buzzerBeep;
    BarcodeString barcodeString = new BarcodeString();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrgOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mLoginUser = (LoginUser) getIntent().getSerializableExtra(LOGIN_USER);
        mOrgOrder = (OrgOrder) getIntent().getSerializableExtra(ORG_ORDER);
        initViews();
        if (new Device().getDevicemModel() == "IPDA035") {
            binding.setShowCameraScanBtn(false);
        } else {
            binding.setShowCameraScanBtn(true);
        }

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
    private static final int REQUEST_CODE_FOR_CAMERA = 2347;
    void initViews(){
        binding.materialIconReturn.setOnClickListener(v -> finish());
        binding.OrderTitleTV.setText("הזמנה מס' " + mOrgOrder.getId());
        binding.OrgNameTv.setText(mOrgOrder.getOrganization().getName());
        binding.MAHandScannLL.setOnClickListener(v -> {
            if (binding.scannerView.getVisibility() == View.VISIBLE) {
                mCodeScanner.stopPreview();
                binding.scannerView.setVisibility(View.GONE);
            }
            HandScanDialog handScanDialog = new HandScanDialog();


            handScanDialog.showDialog(OrgOrderActivity.this, OrgOrderActivity.this);
        });
        binding.MACameraScannLL.setOnClickListener(view -> {

            if (checkIfAlreadyHavePermission(this, Manifest.permission.CAMERA)) {
                if (binding.scannerView.getVisibility() == View.VISIBLE) {
                    mCodeScanner.stopPreview();
                    binding.scannerView.setVisibility(View.GONE);
                } else {
                    mCodeScanner.startPreview();
                    binding.scannerView.setVisibility(View.VISIBLE);
                }
            } else {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                        REQUEST_CODE_FOR_CAMERA);
            }

        });
        binding.finishOrderRL.setOnClickListener(view -> {
            LottieAlertDialog.Builder alert = new LottieAlertDialog.Builder(OrgOrderActivity.this, DialogTypes.TYPE_CUSTOM, "question.json");
            alert.setDescription("האם אתה בטוח שברצונך לסיים את ההזמנה?");
            alert.setTitle("סיום הזמנה");

            alert.setPositiveText("סיום");
            alert.setNegativeText("ביטול");
            alert.setNegativeButtonColor(R.color.white);
            alert.setNegativeTextColor(R.color.black);
            alert.setPositiveButtonColor(R.color.teal_200);
            alert.setPositiveTextColor(R.color.white);



            alert.setNegativeListener(lottieAlertDialog -> {
                lottieAlertDialog.dismiss();

            });
            alert.setPositiveListener(lottieAlertDialog -> {
                finishOrder();
                lottieAlertDialog.dismiss();

            });
            LottieAlertDialog getOrdersListProgressDialog = alert.build();
            getOrdersListProgressDialog.show();

        });
        binding.MAProgressBar.setOnProgressChangeListener(progress -> {
            binding.MAProgressBarTextTV.setText(((int) Math.round(progress)) + "%");
            return null;
        });
        initOrderItemsRecyclerView();
        initCameraScanner();
        initScannerReceiver();
        initBeeps();
    }
    void finishOrder(){
        RequestManager.updateOrgOrderStatus(mOrgOrder.getId(),3, mLoginUser.getId()).subscribe(new Observer<Status>() {

            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Status status) {
                if(status.status.equals("ok")){
                    LottieAlertDialog.Builder alert = new LottieAlertDialog.Builder(OrgOrderActivity.this, DialogTypes.TYPE_SUCCESS, "success.json");
                    alert.setDescription("ההזמנה הסתיימה בהצלחה");
                    alert.setTitle("סיום");
                    alert.setPositiveText("סגור");

                    alert.setPositiveTextColor(-1);
                    alert.setPositiveButtonColor(Color.parseColor("#9e035d"));
                    alert.setPositiveListener((lottieAlertDialog -> {
                        lottieAlertDialog.dismiss();
                        finish();
                    }));
                    alert.setNegativeListener((lottieAlertDialog -> {
                        lottieAlertDialog.dismiss();
                        finish();
                    }));

                    LottieAlertDialog cartScanAlert = alert.build();
                    cartScanAlert.setCancelable(false);
                    cartScanAlert.setCanceledOnTouchOutside(false);
                    cartScanAlert.show();


            }else{
                    LottieAlertDialog.Builder alert = new LottieAlertDialog.Builder(OrgOrderActivity.this, DialogTypes.TYPE_CUSTOM, "warning.json");
                    alert.setDescription(status.message);
                    alert.setTitle("אירעה שגיאה בסיום הזמנה");
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
            public void onError(Throwable e) {
                Toast.makeText(OrgOrderActivity.this, "שגיאה בסיום הזמנה", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {

            }
        });

    }
    private CodeScanner mCodeScanner;
    private void initCameraScanner() {

        //scannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this, binding.scannerView);
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
                        binding.scannerView.setVisibility(View.GONE);
                        mCodeScanner.stopPreview();
                        int delay = 500;
                        if (checkIfBarcodeExist(result.getText())) {
                            successBeep.play();
                            delay = 2000;
                        } else {
                            wrongBeep.play();
                        }

                        new Handler().postDelayed(() -> {
                            binding.scannerView.setVisibility(View.VISIBLE);
                            mCodeScanner.startPreview();
                        }, delay);


                        //scannerView.setVisibility(View.GONE); //commented to continiue scanning
                        Toast.makeText(OrgOrderActivity.this, result.getText(), Toast.LENGTH_SHORT).show();
                    }
                });
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

            //final LinearLayout layout = (LinearLayout) findViewById(R.id.MA_recycler_linear_LL);
//        final LinearLayout layout2 = (LinearLayout) findViewById(R.id.PalletPackagesRV);
            final AnimationDrawable drawable = new AnimationDrawable();
            final Handler handler = new Handler();
            drawable.addFrame(new ColorDrawable(c), 400);
            drawable.addFrame(new ColorDrawable(Color.WHITE), 400);
            drawable.setOneShot(true);
            binding.OrgOrderLL.setBackgroundDrawable(drawable);
            handler.postDelayed(() -> drawable.start(), 100);

    }
    private final static String SCAN_ACTION_RCV = "scan.rcv.message";
    private final static String SCAN_ACTION_ZKC = "com.zkc.scancode";
    String barcodeStr = "";
    private void initScannerReceiver() {
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @SuppressLint("SuspiciousIndentation")
            @Override
            public void onReceive(Context context, Intent intent) {

                String barcode = barcodeString.getBarcodeFromIntent(context,intent);


                checkIfBarcodeExist(barcode);

            }
        };
        BroadcastReceiver broadcastReceiver1 = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                String barcode = barcodeString.getBarcodeFromIntent(context,intent);
                    checkIfBarcodeExist(barcode);

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
    private boolean checkIfBarcodeExist(String barcodeStr) {
        //ast.makeText(this,barcodeStr,Toast.LENGTH_SHORT);
        String origBarcodeStr = barcodeStr;
        barcodeStr = barcodeStr.replaceAll("\\s+","").replace("*","").replace("*","").replace("/J","").replace("/j","");;
        boolean itemMatched = false;
        if (mOrgOrder!=null && mOrgOrder.getOrderItems() !=null && mOrgOrder.getOrderItems().size()>0) {

            for (int i = 0; i < mOrgOrder.getOrderItems().size(); i++) {

                if (mOrgOrder.getOrderItems().get(i).getItem() != null) {

                    String barcode1 = mOrgOrder.getOrderItems().get(i).getItem().getBarcode1();
                    String barcode2 = mOrgOrder.getOrderItems().get(i).getItem().getBarcode2();

                    if (barcode1 != null && barcode1.replaceAll("\\s+","").replace("*","").replace("*","").equals(barcodeStr) || barcode2 != null && barcode2.replaceAll("\\s+","").replace("*","").replace("*","").equals(barcodeStr)) {
                        itemMatched = true;
                        if (mOrgOrder.getOrderItems().get(i).getOrderQuantity() <= mOrgOrder.getOrderItems().get(i).getCollectedQuantity()) {
                            blink(Color.RED);
                            vibrate(2000);
                            wrongBeep.play();
                        } else {
                            mOrgOrder.getOrderItems().get(i).setCollectedQuantity(mOrgOrder.getOrderItems().get(i).getCollectedQuantity() + 1);
                            mOrgOrderItemListAdapter.notifyDataSetChanged();
                            if (mOrgOrder.getOrderItems().size() < 100) {
                                if (i == 0) {

                                    binding.OrgOrderItemsListRv.smoothScrollToPosition(0);

                                } else if (i > 0 && i < mOrgOrder.getOrderItems().size() - 1) {
                                    binding.OrgOrderItemsListRv.smoothScrollToPosition(i + 1);
                                } else {
                                    binding.OrgOrderItemsListRv.smoothScrollToPosition(i);

                                }
                            }
                            setCircleProgressBar();
                            UpdateServer(mOrgOrder.getOrderItems().get(i));
                            blink(Color.GREEN);
                            successBeep.play();
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
    private void initOrderItemsRecyclerView() {
        LottieAlertDialog.Builder alert = new LottieAlertDialog.Builder(OrgOrderActivity.this, DialogTypes.TYPE_CUSTOM, "loading.json");
        alert.setDescription("המערכת מקבלת רשימת הזמנות קיימות מהשרת");
        alert.setTitle("שליפת רשימות הזמנות");
        LottieAlertDialog getOrdersListProgressDialog = alert.build();

        getOrdersListProgressDialog.show();

        RequestManager.getOrgOrder(mOrgOrder.getId(),2, mLoginUser.getId()).subscribe(new Observer<OrgOrder>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull OrgOrder orgOrder) {
                getOrdersListProgressDialog.dismiss();
                if (orgOrder.getOrderItems().size() == 0) {
                    Toast.makeText(OrgOrderActivity.this, "לא נמצאו פריטים להזמנה", Toast.LENGTH_SHORT).show();
                } else {
                    mOrgOrder = orgOrder;
                    binding.OrgOrderItemsListRv.setLayoutManager(new LinearLayoutManager(OrgOrderActivity.this));
                    mOrgOrderItemListAdapter =new OrgOrderItemListAdapter(orgOrder.getOrderItems(), new OrgOrderItemListAdapter.OrgOrderItemAdapterListener() {


                        @Override
                        public void onDeleteClicked(OrgOrderItemsItem orderItemsItem) {
                            deleteClicked(orderItemsItem);
                        }

                        @Override
                        public void onMinusClicked(OrgOrderItemsItem orderItemsItem) {
                            minusClicked(orderItemsItem);
                        }
                    }, OrgOrderActivity.this);
                    binding.OrgOrderItemsListRv.setAdapter(mOrgOrderItemListAdapter);
                    setCircleProgressBar();
//                    binding.ordersRecyclerView.setLayoutManager(new LinearLayoutManager(OrgOrdersList.this));
//                    binding.ordersRecyclerView.setAdapter(new OrgOrdersListAdapter(orgOrders, OrgOrdersList.this));
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                getOrdersListProgressDialog.dismiss();
                Toast.makeText(OrgOrderActivity.this, "שגיאה בשליפת פרטי הזמנה", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {

            }
        });


    }

    public void deleteClicked(OrgOrderItemsItem orgOrderItem) {

        orgOrderItem.setCollectedQuantity(0);
        mOrgOrderItemListAdapter.notifyDataSetChanged();
        setCircleProgressBar();
        UpdateServer(orgOrderItem);


    }



    public void minusClicked(OrgOrderItemsItem orgOrderItem) {
        if(orgOrderItem.getCollectedQuantity() > 0) {
            orgOrderItem.setCollectedQuantity(orgOrderItem.getCollectedQuantity() - 1);
            mOrgOrderItemListAdapter.notifyDataSetChanged();
            setCircleProgressBar();

            UpdateServer(orgOrderItem);
        }
    }
    private void setCircleProgressBar() {

        int ordersCompleted = 0;

        for (int i = 0; i < mOrgOrder.getOrderItems().size(); i++) {

            if (mOrgOrder.getOrderItems().get(i).getCollectedQuantity() == mOrgOrder.getOrderItems().get(i).getOrderQuantity()) {
                ordersCompleted++;
            }
        }

        if (ordersCompleted > 0) {
            binding.MAProgressBar.setProgress((int) ((float) ordersCompleted / (float) mOrgOrder.getOrderItems().size() * 100));
        } else {
            binding.MAProgressBar.setProgress(0);
        }
        if (ordersCompleted >= mOrgOrder.getOrderItems().size()) {

            if (binding.scannerView.getVisibility() == View.VISIBLE) {
                mCodeScanner.stopPreview();
                binding.scannerView.setVisibility(View.GONE);
            }
        }

    }
    private void UpdateServer(OrgOrderItemsItem orgOrderItemsItem) {

        UpdateItem updateItem = new UpdateItem();
        updateItem.setItemId(orgOrderItemsItem.getItemId());
        updateItem.setOrderId(mOrgOrder.getId());
        updateItem.setUserId(mLoginUser.getId());
        updateItem.setQuantity(orgOrderItemsItem.getCollectedQuantity());
//        updateItem.setCartId(mOrder.getCartId());


        RequestManager.updateOrgOrderItemCollection(updateItem).subscribe(new Observer<Status>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull Status status) {
                if(status.status.equals("err")){
                    LottieAlertDialog.Builder alert = new LottieAlertDialog.Builder(OrgOrderActivity.this, DialogTypes.TYPE_CUSTOM, "warning.json");
                    alert.setDescription(status.message);
                    alert.setTitle("אירעה שגיאה בליקוט פריט " + orgOrderItemsItem.getItem().getName());
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
                Toast.makeText(OrgOrderActivity.this, "שגיאה בעדכון פרטי הזמנה", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onComplete() {

            }
        });


    }

    @Override
    public void onHandScanClicked(String handScan) {
        checkIfBarcodeExist(handScan);
    }
}