package com.simarjot.bookwala.model;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class BookSharedPrefs {

    public static final String SHARED_PREFS = "SHARED_PREFS";
    public static final String TITLE = "TITLE";
    public static final String LEVEL = "LEVEL";
    public static final String SUBJECT = "SUBJECT";
    public static final String AUTHOR = "AUTHOR";
    public static final String CATEGORY = "CAT";
    public static final String DESCRIPTION = "DESC";
    public static final String PRICE = "PRICE";
    public static final String CURRENCY = "CURRENCY";
    public static final String IMAGE_URIS = "IMAGE_URIS";
    public static final String COVER_IMAGE_URI = "COVER";

    public static Book getBook(Context context){
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        Category category = Category.valueOf(prefs.getString(CATEGORY, null));
        String title = prefs.getString(TITLE, null);
        String subject = prefs.getString(SUBJECT, null);
        String author = prefs.getString(AUTHOR, null);
        int level = prefs.getInt(LEVEL, -1);
        String price = prefs.getString(PRICE, null);
        String desc = prefs.getString(DESCRIPTION, null);
        String currency = prefs.getString(CURRENCY, null);
        return new Book(title, author, subject, desc, price, currency, category, level);
    }

    public static List<String> getImageUris(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        return new ArrayList<>(prefs.getStringSet(IMAGE_URIS, null));
    }

    public static String getCoverImageUri(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        return prefs.getString(COVER_IMAGE_URI, null);
    }
}
