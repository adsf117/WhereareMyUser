package com.andres.wherearemyuser.ui;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.andres.wherearemyuser.R;
import com.andres.wherearemyuser.Utils;
import com.andres.wherearemyuser.dataobjects.User;
import com.andres.wherearemyuser.webservices.GetUser;
import com.andres.wherearemyuser.webservices.ServiceCreator;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private  final String LOG_TAG = MapActivity.class.toString();
    GoogleMap m_map;
    boolean mapReady=false;
    private List<User> mUserList=null;
    static final CameraPosition SEATTLE = CameraPosition.builder()
            .target(new LatLng(47.6204,-122.2491))
            .zoom(10)
            .bearing(0)
            .tilt(45)
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Generate Ramdom User", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                ramdomUser();
            }
        });
    }

    public void getUsersFromServer() {
        GetUser userService = ServiceCreator.createService(GetUser.class);
        Call<List<User>> call = userService.getUsers();
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful()) {
                    mUserList = response.body();
                    ramdomUser();
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapReady=true;
        m_map = googleMap;
        getUsersFromServer();
    }

    private void ramdomUser()
    {
        User ramdomUser = mUserList.get(new Utils().generateRamdomNumber(mUserList.size()));
        createMarker (ramdomUser.getAddress().getGeo().getLng(),ramdomUser.getAddress().getGeo().getLat(),ramdomUser.getName());

    }
    protected void  createMarker(double latitude, double longitude, String title) {

        m_map.clear();
        LatLng latLng = new LatLng(latitude, longitude);
         m_map.addMarker(new MarkerOptions()
                .position(latLng)
                .title(title));
        CameraPosition cameraPosition = CameraPosition.builder()
                .target(latLng)
                .bearing(0)
                .tilt(45)
                .build();
        m_map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 3000, null);

    }
}
