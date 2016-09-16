package com.andres.wherearemyuser.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.andres.wherearemyuser.R;
import com.andres.wherearemyuser.dataobjects.User;
import com.andres.wherearemyuser.webservices.GetUser;
import com.andres.wherearemyuser.webservices.ServiceCreator;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapActivity extends AppCompatActivity {

    private  final String LOG_TAG = MapActivity.class.toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        getUsersFromServer();
    }

    public void getUsersFromServer() {
        GetUser userService = ServiceCreator.createService(GetUser.class);
        Call<List<User>> call = userService.getUsers();
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful()) {
                    Log.d(LOG_TAG,"isSuccessful Response"+response.body().get(0).getName());
                } else {
                    Log.e(LOG_TAG,"onUsersFail");
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                onNetworkError();
            }
        });
    }
    public void onNetworkError() {
        Toast.makeText(this,"No internet connection,Please Check you Internet Conecction",Toast.LENGTH_SHORT).show();
    }
}
