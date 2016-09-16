package com.andres.wherearemyuser.dataobjects;

/**
 * Created by andresdavid on 16/09/16.
 *  * "geo": {
 "lat": "-37.3159",
 "lng": "81.1496"
 */
public class Geo {

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
