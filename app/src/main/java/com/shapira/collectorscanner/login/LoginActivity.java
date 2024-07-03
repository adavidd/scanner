package com.shapira.collectorscanner.login;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.shapira.collectorscanner.BuildConfig;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
import com.shapira.collectorscanner.utility.UpdateApp;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

import static com.shapira.collectorscanner.Constants.LOGIN_USER;

public class LoginActivity extends AppCompatActivity implements LoginListAdapter.LoginListAdapterListener {

    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private Button updateBtn;
    private LoginListAdapter mLoginListadapter;
    private List<LoginUser> mUsersList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        initViews();
        getUsersFromServer();
        boolean hasPermission = (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
        if (!hasPermission) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_WRITE_STORAGE);
        }

    }
    private static final int REQUEST_WRITE_STORAGE = 112;
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_WRITE_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission granted!", Toast.LENGTH_SHORT).show();
                    // Continue your work here
                } else {
                    Toast.makeText(this, "Permission denied!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public static boolean isGrantedPermissionWRITE_EXTERNAL_STORAGE(Activity activity) {
        int version = Build.VERSION.SDK_INT;
        if( version <= 32 ) {
            boolean isAllowPermissionApi28 = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED ;
            Log.i("general_em","isGrantedPermissionWRITE_EXTERNAL_STORAGE() - isAllowPermissionApi28: " + isAllowPermissionApi28);
            return  isAllowPermissionApi28;
        } else {
            boolean isAllowPermissionApi33 = Environment.isExternalStorageManager();
            Log.i("general_em","isGrantedPermissionWRITE_EXTERNAL_STORAGE() - isAllowPermissionApi33: " + isAllowPermissionApi33);
            return isAllowPermissionApi33;
        }
    }
    private void initViews() {

        mRecyclerView = findViewById(R.id.LA_recycler_RV);

        mProgressBar = findViewById(R.id.LA_progress_bar_PB);
        updateBtn = findViewById(R.id.updateBtn);
        TextView versionTv = findViewById(R.id.versionTV);
        versionTv.setText("גרסה " + BuildConfig.VERSION_NAME);
    updateBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            updateBtn.setEnabled(false);
            UpdateApp atualizaApp = new UpdateApp();
            atualizaApp.setContext(LoginActivity.this, getApplicationContext(), new UpdateApp.DownloadUpdateInterface() {
                @Override
                public void onDownloadProgress(int buffer) {
//                    binding.downloadProgressBar.setIndeterminate(true);
                }

                @Override
                public void onDownloadComplete() {

//                    binding.downloadProgressBar.setIndeterminate(false);
                }
                @Override
                public void onUpdateFailed() {
                    updateBtn.setEnabled(true);
                    Toast.makeText(LoginActivity.this, "העדכון נכשל", Toast.LENGTH_LONG).show();
                }
            });
            atualizaApp.execute(BuildConfig.BASE_URL  + "/app-debug.apk");
        }
    });
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