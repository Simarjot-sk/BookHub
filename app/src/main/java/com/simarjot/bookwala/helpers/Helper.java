package com.simarjot.bookwala.helpers;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.util.Patterns;

import com.google.firebase.firestore.GeoPoint;

public class Helper {
    public static final String TAG = "nerd";

    public static boolean isValidEmail(CharSequence target) {
        return (Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }


    public static GeoPoint getCurrentLocation(Context context){
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(bestProvider);
        return new GeoPoint(location.getLatitude(), location.getLongitude());
    }



}
