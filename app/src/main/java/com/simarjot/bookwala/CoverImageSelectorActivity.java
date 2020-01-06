package com.simarjot.bookwala;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.flexbox.FlexboxLayout;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

public class CoverImageSelectorActivity extends AppCompatActivity {
    private int selectedImage;
    private List<ConstraintLayout> images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cover_image_selector);
        images = new ArrayList<>();
        ArrayList<String> imageUris = getIntent().getStringArrayListExtra("imageUris");

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
