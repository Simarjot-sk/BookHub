package com.simarjot.bookwala;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.flexbox.FlexboxLayout;
import com.makeramen.roundedimageview.RoundedImageView;
import com.simarjot.bookwala.helpers.Helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CoverImageSelectorActivity extends AppCompatActivity {
    private int selectedImage = -1;
    private List<ConstraintLayout> images;
    private ArrayList<String> imageUris;
    //widgets
    private Button doneButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cover_image_selector);
        doneButton = findViewById(R.id.done_btn);
        images = new ArrayList<>();

        Set<String> imageUrisSet =  getSharedPreferences(Helper.BOOK_IMAGE_PREFS, MODE_PRIVATE).getStringSet(Helper.SELECTED_IMAGES, null);
        if(imageUrisSet==null){
            Log.d(Helper.TAG, "imageUris not found in shared prefs");
        }else{
            imageUris = new ArrayList<>(imageUrisSet);
            for(String uri: imageUris){
                Log.d(Helper.TAG, uri);
            }
        }

        FlexboxLayout layout = findViewById(R.id.flex_box_layout);

        for(int i=0; i < imageUris.size(); i++){
            LayoutInflater inflater = LayoutInflater.from(this);
            ConstraintLayout constraintLayout = (ConstraintLayout) inflater.inflate(R.layout.image_selection_layout, null, false);
            RoundedImageView bookImage = constraintLayout.findViewById(R.id.book_image);
            bookImage.setTag(i);
            bookImage.setImageURI( Uri.parse( imageUris.get(i) ) );
            images.add(constraintLayout);
            layout.addView(constraintLayout);

            bookImage.setOnClickListener(v -> {
                selectedImage = (int) v.getTag();
                ConstraintLayout cl = images.get(selectedImage);
                RoundedImageView riv = cl.findViewById(R.id.overlay_img);
                riv.setVisibility(View.VISIBLE);
                makeOverlaysGone(selectedImage);
            });
        }

        makeOverlaysGone(-1);

        doneButton.setOnClickListener(v -> {
            if(selectedImage<0){
                Toast.makeText(this, "Please select the title image", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent categoriesIntent = new Intent(CoverImageSelectorActivity.this, Categories.class);
            SharedPreferences.Editor editor= getSharedPreferences(Helper.BOOK_IMAGE_PREFS, MODE_PRIVATE).edit();
            editor.putString(Helper.COVER_IMAGE, imageUris.get(selectedImage));
            editor.apply();
            startActivity(categoriesIntent);
        });
    }

    private void makeOverlaysGone(int except){
        for(int i=0; i < images.size(); i++){
            if(i!=except){
                RoundedImageView overlay = images.get(i).findViewById(R.id.overlay_img);
                overlay.setVisibility(View.GONE);
            }
        }
    }
}
