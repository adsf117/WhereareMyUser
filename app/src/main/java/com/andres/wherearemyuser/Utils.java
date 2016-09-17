package com.andres.wherearemyuser;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import java.util.Random;

/**
 * Created by andresdavid on 16/09/16.
 */
public class Utils {

    private  final String MY_LOCATION = "mylocation.sharedpreferences";
    private int index=0;
    private int lastindex=0;

    public  int generateRamdomNumber( int arraySize){
        Random randomGenerator = new Random();
        while (index == lastindex)
        {
            index= randomGenerator.nextInt(arraySize);
        }
        lastindex=index;
        return index;
    }
    public static float getDistanceBetweenLocations(LatLng locationOne, LatLng locationTwo) {
        float [] results = new float[1];
        Location.distanceBetween(locationOne.latitude,locationOne.longitude,locationTwo.latitude,locationTwo.longitude,results);
        return results[0];
    }
    public void saveMylastlocation(Activity activity,Location location)
    {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(MY_LOCATION, new Gson().toJson(location));
        editor.commit();

    }
    public  Location getMylastlocation(Activity activity)
    {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        String location = sharedPref.getString(MY_LOCATION, "");
        return new Gson().fromJson(location, Location.class);

    }
}
