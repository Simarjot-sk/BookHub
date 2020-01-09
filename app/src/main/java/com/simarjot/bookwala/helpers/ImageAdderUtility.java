package com.simarjot.bookwala.helpers;

import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import com.makeramen.roundedimageview.RoundedImageView;
import com.simarjot.bookwala.R;

import java.util.ArrayList;
import java.util.List;

public class ImageAdderUtility {
    private static final String TAG = "imageAdder";
    private int lastImageIndex = -1;
    private int coverImageIndex = -1;
    private Context context;
    private List<String> imageUris;
    private List<ConstraintLayout> imageContainers;

    //Widgets
    private LinearLayout layout;

    public ImageAdderUtility(Context context, View view) {
        layout = view.findViewById(R.id.flex_box_layout);
        imageUris = new ArrayList<>();
        imageContainers = new ArrayList<>();
        this.context = context;
    }

    public void addImage(Uri imageUri){
        lastImageIndex++;
        imageUris.add(imageUri.toString());
        LayoutInflater inflater = LayoutInflater.from(context);
        ConstraintLayout constraintLayout = (ConstraintLayout) inflater.inflate(R.layout.image_minus_layout, null, false);
        imageContainers.add(constraintLayout);

        //making the coverImageMsg visible on the first image added
        makeFirstImageCover();

        RoundedImageView minus = constraintLayout.findViewById(R.id.minus);
        minus.setTag(lastImageIndex);
        minus.setOnClickListener( v->{
            Log.d("nerd", "clicked " + v.getTag());
            remove((int)v.getTag());
        });

        RoundedImageView newImage = constraintLayout.findViewById(R.id.roundedImageView);
        newImage.setImageURI(imageUri);
        newImage.setTag(lastImageIndex);

        newImage.setOnClickListener( v ->{
            coverImageIndex =(int) v.getTag();
            Log.d("nerd", "coverImageIndex: " + coverImageIndex);
            removeCoverImageMsg();
            imageContainers.get(coverImageIndex).findViewById(R.id.cover_image_msg).setVisibility(View.VISIBLE);
        });
        layout.addView(constraintLayout, lastImageIndex);
    }

    private void remove(int removeIndex){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setItems(new CharSequence[]{"Remove Image", "Cancel"}, (dialog, which) -> {
            if(which==0){//Remove Image Selected
                imageUris.remove(removeIndex);
                imageContainers.remove(removeIndex);
                layout.removeViewAt(removeIndex);
                Log.d("nerd", "removeIndex: " + removeIndex);
                lastImageIndex--;
                if(removeIndex == coverImageIndex){
                    makeFirstImageCover();
                }
                updateTags();
            }
        });
        alertBuilder.show();
    }

    private void updateTags(){
        int childCount = layout.getChildCount();
        for(int i=0;i<childCount;i++){
            View view = layout.getChildAt(i);
            RoundedImageView minus = view.findViewById(R.id.minus);
            RoundedImageView newImage = view.findViewById(R.id.roundedImageView);
            //in case of the plus imageButton there is no minus imageView.
            if(minus!=null){
                minus.setTag(i);
                newImage.setTag(i);
            }
        }
    }

    private void removeCoverImageMsg(){
        for(ConstraintLayout layout : imageContainers){
            TextView coverImageMsg = layout.findViewById(R.id.cover_image_msg);
            coverImageMsg.setVisibility(View.INVISIBLE);
        }
    }
    private void makeFirstImageCover(){
        if(imageContainers.size()>=1){
            coverImageIndex = 0;
            imageContainers.get(coverImageIndex).findViewById(R.id.cover_image_msg).setVisibility(View.VISIBLE);
        }
    }
    public List<String> getImageUris(){
        return imageUris;
    }

    public int getCoverImageIndex() {
        return coverImageIndex;
    }
}
