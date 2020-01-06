package com.simarjot.bookwala.helpers;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.android.flexbox.FlexboxLayout;
import com.makeramen.roundedimageview.RoundedImageView;
import com.simarjot.bookwala.R;

import java.util.ArrayList;
import java.util.List;


public class ImageAdderUtility {
    private static final String TAG = "imageAdder";
    private int currentIndex = 0;
    private Context context;
    private List<String> imageUris;

    //Widgets
    private FlexboxLayout layout;

    public ImageAdderUtility(Context context, View view) {
        layout = view.findViewById(R.id.flex_box_layout);
        imageUris = new ArrayList<>();
        this.context = context;
    }

    public void addImage(Uri imageUri){
        imageUris.add(imageUri.toString());
        LayoutInflater inflater = LayoutInflater.from(context);
        ConstraintLayout constraintLayout = (ConstraintLayout) inflater.inflate(R.layout.image_minus_layout, null, false);
        RoundedImageView minus = constraintLayout.findViewById(R.id.minus);
        minus.setTag(currentIndex);
        minus.setOnClickListener( v->{
            Log.d("nerd", "clicked " + v.getTag());
            remove((int)v.getTag());
        });
        RoundedImageView newImage = constraintLayout.findViewById(R.id.roundedImageView);
        newImage.setImageURI(imageUri);
        layout.addView(constraintLayout, currentIndex);
        currentIndex++;
    }

    private void remove(int index){
        imageUris.remove(index);
        layout.removeViewAt(index);
        currentIndex--;
        updateTags();
    }

    private void updateTags(){
        int childCount = layout.getChildCount();
        for(int i=0;i<childCount;i++){
            View view = layout.getChildAt(i);
            RoundedImageView minus = view.findViewById(R.id.minus);
            //in case of the plus imageButton there is no minus imageView.
            if(minus!=null){
                minus.setTag(i);
            }
        }
    }

    public List<String> getImageUris(){
        return imageUris;
    }
}
