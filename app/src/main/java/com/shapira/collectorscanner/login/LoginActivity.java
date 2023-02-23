package com.shapira.collectorscanner.login;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import com.labters.lottiealertdialoglibrary.DialogTypes;
import com.labters.lottiealertdialoglibrary.LottieAlertDialog;
import com.shapira.collectorscanner.MainActivity;
import com.shapira.collectorscanner.R;
import com.shapira.collectorscanner.model.LoginUser;
import com.shapira.collectorscanner.model.Status;
import com.shapira.collectorscanner.server.RequestManager;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

import static com.shapira.collectorscanner.Constants.LOGIN_USER;

public class LoginActivity extends AppCompatActivity implements LoginListAdapter.LoginListAdapterListener {

    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private LoginListAdapter mLoginListadapter;
    private List<LoginUser> mUsersList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        initViews();
        getUsersFromServer();

    }


    private void initViews() {

        mRecyclerView = findViewById(R.id.LA_recycler_RV);
        mProgressBar = findViewById(R.id.LA_progress_bar_PB);


    }



    private void initRecyclerView() {
        int lastUserId = this.getPreferences(Context.MODE_PRIVATE).getInt("lastUserId",0);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mLoginListadapter = new LoginListAdapter(this, this, mUsersList,lastUserId);
        mLoginListadapter.setHasStableIds(true);
        mRecyclerView.setAdapter(mLoginListadapter);

        if(mUsersList.size()>0){

            if(lastUserId>0){
                for (int i = 0; i < mUsersList.size(); i++) {
                    if (mUsersList.get(i).getId() >0) {
                        if(mUsersList.get(i).getId() == lastUserId){
                                mRecyclerView.smoothScrollToPosition(Math.min(i+6, mUsersList.size()));
                        }
                    }
                }
            }

        }


    }


    private void getUsersFromServer() {

        mProgressBar.setVisibility(View.VISIBLE);

        RequestManager.getUsersLoginList().subscribe(new Observer<List<LoginUser>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull List<LoginUser> loginUsers) {
            Log.d("chaim","got " +  loginUsers.size() + " users");
                mProgressBar.setVisibility(View.GONE);
                mUsersList = loginUsers;

                initRecyclerView();


            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });


    }

    @Override
    public void onItemListClicked(LoginUser loginUser) {
//        if(loginUser.getPincode()==null){
//            loginUser.setPincode("1234");
//        }

        if(loginUser.getPincode()!=null && loginUser.getPincode().length()>0){
            //Toast.makeText(LoginActivity.this,   loginUser.getPincode(), Toast.LENGTH_SHORT).show();
            final EditText input = new EditText(this);
            input.setSingleLine(true);
            input.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);


            new AlertDialog.Builder(LoginActivity.this)
                    .setTitle("כניסת משתמש")
                    .setMessage("הזן קוד:")
                    .setView(input)

                    .setPositiveButton("אישור", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {


                            if(input.getText().toString().equals(loginUser.getPincode())){
                                openUser(loginUser);
                            }else{
                                Toast.makeText(LoginActivity.this,  "הקוד שגוי", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).setNegativeButton("ביטול", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // Do nothing.
                }
            }).show();

        }else {

            openUser(loginUser);
        }
    }
    void openUser(LoginUser loginUser){
        SharedPreferences.Editor editor = this.getPreferences(Context.MODE_PRIVATE).edit();
        RequestManager.updateUserAppLogin(loginUser).subscribe(new Observer<Status>() {
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
        editor.putInt("lastUserId", loginUser.getId());
        editor.commit();
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(LOGIN_USER, loginUser);
        startActivity(intent);
        finish();
    }
}