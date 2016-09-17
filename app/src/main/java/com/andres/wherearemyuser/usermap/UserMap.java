package com.andres.wherearemyuser.usermap;

import android.app.Activity;
import android.location.Location;

import com.andres.wherearemyuser.Utils;
import com.andres.wherearemyuser.dataobjects.User;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by andresdavid on 17/09/16.
 */
public class UserMap {

    public  static  User getClosestUser(List<User> userList, Activity activity) {

        Location myLocation = new Utils().getMylastlocation(activity);
        LatLng myLocationLatLng = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
        User nearestUser = null;
        float distance = Float.MAX_VALUE;
        ;
        for (User user : userList) {
            LatLng latLng = new LatLng(user.getAddress().getGeo().getLat(), user.getAddress().getGeo().getLng());
            float userDistance = getDistanceBetweenLocations(myLocationLatLng, latLng);
            if (userDistance < distance) {
                distance = userDistance;
                nearestUser = user;
            }
        }

        return nearestUser;
    }
    public static User getFurthestUser(List<User> userList,Activity activity) {

        Location myLocation = new Utils().getMylastlocation(activity);
        LatLng myLocationLatLng = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
        User farthestUser = null;
        float distance = 0;

        for (User user : userList) {
            LatLng latLng = new LatLng(user.getAddress().getGeo().getLat(), user.getAddress().getGeo().getLng());
            float userDistance = getDistanceBetweenLocations(myLocationLatLng, latLng);
            if (userDistance > distance) {
                distance = userDistance;
                farthestUser = user;
            }
        }

        return farthestUser;
    }
    private static float getDistanceBetweenLocations(LatLng locationOne, LatLng locationTwo) {
        float [] results = new float[1];
        Location.distanceBetween(locationOne.latitude,locationOne.longitude,locationTwo.latitude,locationTwo.longitude,results);
        return results[0];
    }
}
