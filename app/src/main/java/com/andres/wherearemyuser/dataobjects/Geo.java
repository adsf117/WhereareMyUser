package com.andres.wherearemyuser.dataobjects;

import io.realm.RealmObject;

/**
 * Created by andresdavid on 16/09/16.
 */
public class Geo extends RealmObject {

    private double lat;
    private double lng;

    public Geo() {
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
