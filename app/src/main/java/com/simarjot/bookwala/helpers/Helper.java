package com.simarjot.bookwala.helpers;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.util.Log;
import android.util.Patterns;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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

    public static void getUserFromUid(Context context, String uid, Response.Listener<String> listener) {
        Uri uri = Uri.parse("https://us-central1-bookwala-86dc9.cloudfunctions.net");
        String uriString = uri.buildUpon()
                .appendPath("user")
                .appendQueryParameter("uid", uid).build().toString();
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, uriString, listener,
                error -> Log.d(Helper.TAG, error.getMessage(), error));

        queue.add(stringRequest);
    }
}
