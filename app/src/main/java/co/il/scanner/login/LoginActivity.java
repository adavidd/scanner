package co.il.scanner.login;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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

import co.il.scanner.MainActivity;
import co.il.scanner.R;
import co.il.scanner.model.LoginUser;
import co.il.scanner.server.RequestManager;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

import static co.il.scanner.Constants.LOGIN_USER;

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

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mLoginListadapter = new LoginListAdapter(this, this, mUsersList);
        mRecyclerView.setAdapter(mLoginListadapter);
        if(mUsersList.size()>0){
            int lastUserId = this.getPreferences(Context.MODE_PRIVATE).getInt("lastUserId",0);
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

            new AlertDialog.Builder(LoginActivity.this)
                    .setTitle("כניסת משתמש")
                    .setMessage("הזן סיסמה:")
                    .setView(input)
                    .setPositiveButton("אישור", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {


                            if(input.getText().toString().equals(loginUser.getPincode())){
                                openUser(loginUser);
                            }else{
                                Toast.makeText(LoginActivity.this,  "הסיסמא שגויה", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
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
        editor.putInt("lastUserId", loginUser.getId());
        editor.commit();
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(LOGIN_USER, loginUser);
        startActivity(intent);
        finish();
    }
}