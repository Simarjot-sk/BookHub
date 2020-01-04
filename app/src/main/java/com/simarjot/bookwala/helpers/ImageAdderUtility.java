package com.simarjot.bookwala.helpers;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Shader;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.flexbox.AlignSelf;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.makeramen.roundedimageview.RoundedImageView;
import com.simarjot.bookwala.R;

import java.util.ArrayList;
import java.util.List;

public class ImageAdderUtility {
    private static final String TAG = "imageAdder";
    private int currentIndex = 0;
    private Context context;

    //Widgets
    private FlexboxLayout layout;

    public ImageAdderUtility(Context context, View view) {
        layout = view.findViewById(R.id.flex_box_layout);
        this.context = context;
    }

    public void addImage(Uri imageUri){

        RoundedImageView newImage = new RoundedImageView(context);
        newImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        newImage.setCornerRadius((float) 10);
        newImage.setBorderWidth((float) 2);
        newImage.setBorderColor(Color.DKGRAY);
        newImage.mutateBackground(true);
        newImage.setImageURI(imageUri);
        newImage.setOval(false);

        layout.addView(newImage, currentIndex);

        FlexboxLayout.LayoutParams newImageParams = new FlexboxLayout.LayoutParams(180, 180);
        newImageParams.setMargins(0,0,10,10);

        newImage.setLayoutParams(newImageParams);
        log();
        currentIndex++;

    }

//    private void updatePlus(){
//        if(currentIndex < (bookViews.size() - 1)){
//            int plusIndex = currentIndex+1;
//            bookViews.get(plusIndex).setImageResource(R.drawable.chnage_img);
//            bookViews.get(plusIndex).setScaleType(ImageView.ScaleType.CENTER);
//
//            //setting all the images unclickable
//            for(ImageView iv: bookViews){
//                iv.setClickable(false);
//            }
//            //setting just the image with plus clickable
//            bookViews.get(plusIndex).setClickable(true);
//        }else {
//            for(ImageView iv: bookViews){
//                iv.setClickable(false);
//            }
//        }
//    }


    private void log(){
        if(currentIndex==-1){
            Log.d(TAG, "no images added");
        }else if(currentIndex==0){
            Log.d(TAG, "1 image added");
        }else {
            Log.d(TAG, (currentIndex + 1) + " images added");
        }
    }

    public int getCurrentIndex(){
        return currentIndex;
    }
//    public List<Uri> getBookImageUris(){
//        return bookImageUris;
//    }
}
