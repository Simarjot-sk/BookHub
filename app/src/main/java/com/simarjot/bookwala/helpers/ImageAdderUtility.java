package com.simarjot.bookwala.helpers;

import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;
import com.simarjot.bookwala.R;

import java.util.List;

public class ImageAdderUtility {
    private static final String TAG = "imageAdder";
    private List<ImageView> bookViews;
    private int currentIndex = -1;

    public ImageAdderUtility(List<ImageView> imageViews) {
        bookViews = imageViews;
    }

    public void addImage(Uri imageUri){
        currentIndex++;
        updatePlus();
        bookViews.get(currentIndex).setImageURI(imageUri);
        bookViews.get(currentIndex).setScaleType(ImageView.ScaleType.CENTER_CROP);
        log();
    }

    private void updatePlus(){
        if(currentIndex < (bookViews.size() - 1)){
            bookViews.get(currentIndex+1).setImageResource(R.drawable.chnage_img);
            bookViews.get(currentIndex+1).setScaleType(ImageView.ScaleType.CENTER);
        }
    }

    private void log(){
        if(currentIndex==-1){
            Log.d(TAG, "no images added");
        }else if(currentIndex==0){
            Log.d(TAG, "1 image added");
        }else {
            Log.d(TAG, (currentIndex + 1) + " images added");
        }
    }
}
