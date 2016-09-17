package com.andres.wherearemyuser.ui;

import android.Manifest;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.andres.wherearemyuser.LocationManager;
import com.andres.wherearemyuser.R;
import com.andres.wherearemyuser.Utils;
import com.andres.wherearemyuser.dataobjects.User;
import com.andres.wherearemyuser.webservices.GetUser;
import com.andres.wherearemyuser.webservices.ServiceCreator;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import io.github.yavski.fabspeeddial.FabSpeedDial;
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private final String LOG_TAG = MapActivity.class.toString();
    GoogleMap m_map;
    boolean mapReady = false;
    private List<User> mUserList = null;
    private static final String[] PERMISSIONS = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    private int ACCESS_FINE_LOCATION = 23;
    private  int DEFAUL_MAP_TILT=45;
    private  int DEFAUL_MAP_ZOOM=3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        retrieveLocation();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, R.string.showing_a_random_user, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                ramdomUser();
            }
        });
        FabSpeedDial fabSpeedDial = (FabSpeedDial) findViewById(R.id.fab_speed_dial);
        fabSpeedDial.setMenuListener(new SimpleMenuListenerAdapter() {
            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                //TODO: Start some activity
                User resulUser = null;
                if (menuItem.getTitle().equals(getString(R.string.the_closest_user))) {

                    resulUser = getClosestUser();
                } else if (menuItem.getTitle().equals(getString(R.string.the_furthest_user))) {

                    resulUser = getFurthestUser();
                }

                if (resulUser != null)
                    createMarker(resulUser);
                return false;
            }
        });
    }

    public User getFurthestUser() {

        Location myLocation = new Utils().getMylastlocation(this);
        LatLng myLocationLatLng = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
        User farthestUser = null;
        float distance = 0;

        for (User user : mUserList) {
            LatLng latLng = new LatLng(user.getAddress().getGeo().getLat(), user.getAddress().getGeo().getLng());
            float userDistance = Utils.getDistanceBetweenLocations(myLocationLatLng, latLng);
            if (userDistance > distance) {
                distance = userDistance;
                farthestUser = user;
            }
        }

        return farthestUser;
    }

    public User getClosestUser() {

        Location myLocation = new Utils().getMylastlocation(this);
        LatLng myLocationLatLng = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
        User nearestUser = null;
        float distance = Float.MAX_VALUE;
        ;
        for (User user : mUserList) {
            LatLng latLng = new LatLng(user.getAddress().getGeo().getLat(), user.getAddress().getGeo().getLng());
            float userDistance = Utils.getDistanceBetweenLocations(myLocationLatLng, latLng);
            if (userDistance < distance) {
                distance = userDistance;
                nearestUser = user;
            }
        }

        return nearestUser;
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
                } else {
                    Log.e(LOG_TAG, "onUsersFail" + response.toString());
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                onNetworkError();
            }
        });
    }

    public void onNetworkError() {
        Toast.makeText(this, "No internet connection. Please check you internet conecction", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapReady = true;
        m_map = googleMap;
        getUsersFromServer();
    }

    private void ramdomUser() {
        createMarker(mUserList.get(new Utils().generateRamdomNumber(mUserList.size())));
    }

    protected void createMarker(User user) {

        m_map.clear();
        LatLng latLng = new LatLng(user.getAddress().getGeo().getLng(), user.getAddress().getGeo().getLng());
        m_map.addMarker(new MarkerOptions()
                .position(latLng)
                .snippet(user.getUsername())
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location))
                .title(user.getName()));

        CameraPosition cameraPosition = CameraPosition.builder()
                .target(latLng)
                .zoom(DEFAUL_MAP_ZOOM)
                .bearing(0)
                .tilt(DEFAUL_MAP_TILT)
                .build();
        m_map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 3000, null);

    }

    private void retrieveLocation() {

        if (EasyPermissions.hasPermissions(this, PERMISSIONS)) {
            LocationManager.getInstance(this).init();

        } else {
            EasyPermissions.requestPermissions(this, "Permisos requeridos", ACCESS_FINE_LOCATION, PERMISSIONS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

}
