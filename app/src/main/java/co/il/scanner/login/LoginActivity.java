package co.il.scanner.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

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



    }


    private void getUsersFromServer() {

        mProgressBar.setVisibility(View.VISIBLE);

        RequestManager.getUsersLoginList().subscribe(new Observer<List<LoginUser>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull List<LoginUser> loginUsers) {

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

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(LOGIN_USER, loginUser);
        startActivity(intent);
        finish();
    }
}