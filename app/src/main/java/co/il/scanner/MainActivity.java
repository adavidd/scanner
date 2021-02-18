package co.il.scanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

import java.io.Serializable;
import java.util.List;

import co.il.scanner.login.LoginListAdapter;
import co.il.scanner.model.LoginUser;
import co.il.scanner.model.OrderItemsItem;
import co.il.scanner.model.Orders;
import co.il.scanner.server.RequestManager;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

import static co.il.scanner.Constants.LOGIN_USER;

public class MainActivity extends AppCompatActivity implements OrderAdapter.OrderAdapterListener {

    private TextView mStartButton;
    private RecyclerView mRecyclerView;
    private OrderAdapter mOrderAdapter;
    private LoginUser mLoginUser;
    private final static String SCAN_ACTION = "scan.rcv.message";
    String barcodeStr = "";
    private LottieAnimationView mScanning;
    boolean isScanning = false;
    private ProgressBar mProgressBar;
    private LinearLayout mRecyclerLinear;
    private TextView mClientText;
    private List<OrderItemsItem> mOrdersList;

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


        Toast.makeText(this, "שלום " + mLoginUser.getFirstname(), Toast.LENGTH_SHORT).show();
    }


    private void initRecyclerView(Orders orders) {

        mRecyclerLinear.setVisibility(View.VISIBLE);

        String clientName = orders.getCustomer().getFirstname1() + " " + orders.getCustomer().getLastname();
        mClientText.setText(clientName);

        mOrdersList = orders.getOrderItems();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mOrderAdapter = new OrderAdapter(this, this, mOrdersList);
        mRecyclerView.setAdapter(mOrderAdapter);


    }


    @Override
    public void onBackPressed() {

        if (mRecyclerLinear.getVisibility() == View.VISIBLE) {
            mRecyclerLinear.setVisibility(View.GONE);
            mStartButton.setVisibility(View.VISIBLE);

        } else {

            super.onBackPressed();

        }

    }

    private void initListeners() {

        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mProgressBar.setVisibility(View.VISIBLE);
                mStartButton.setVisibility(View.GONE);

                getOrdersFromServer();


            }
        });


    }


    private void getOrdersFromServer() {

        RequestManager.getOrders().subscribe(new Observer<Orders>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull Orders orders) {

                initRecyclerView(orders);
                mProgressBar.setVisibility(View.GONE);

            }

            @Override
            public void onError(@NonNull Throwable e) {
                mProgressBar.setVisibility(View.GONE);
                mStartButton.setVisibility(View.VISIBLE);

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


        IntentFilter filter = new IntentFilter();
        filter.addAction(SCAN_ACTION);
        registerReceiver(broadcastReceiver, filter);


    }


    private void checkIfBarcodeExist(String barcodeStr) {

        if (mOrdersList != null){

            for (int i = 0; i < mOrdersList.size(); i++) {

                if (mOrdersList.get(i).getItem().getBarcode1().equals(barcodeStr)) {

                    mOrdersList.get(i).setCollectedQuantity(mOrdersList.get(i).getCollectedQuantity() + 1);
                    mOrderAdapter.notifyDataSetChanged();
                    break;
                }

            }
        }


    }


}
