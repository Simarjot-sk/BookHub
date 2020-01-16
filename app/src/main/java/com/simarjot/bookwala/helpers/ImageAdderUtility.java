package com.simarjot.bookwala.helpers;

import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.flexbox.FlexboxLayout;
import com.makeramen.roundedimageview.RoundedImageView;
import com.simarjot.bookwala.R;

import java.util.ArrayList;
import java.util.List;

public class ImageAdderUtility {
    private int lastImageIndex = -1;
    private int coverImageIndex = -1;
    private int mMaxImages;
    private Context mContext;
    private List<String> mImageUris;
    private List<ConstraintLayout> mImageContainers;
    //Widgets
    private FlexboxLayout layout;
    private ImageButton mAddImageBTN;

    public ImageAdderUtility(Context context, View view, int maxImages) {
        mContext = context;
        layout = view.findViewById(R.id.flex_box_layout);
        mAddImageBTN = view.findViewById(R.id.add_images_btn);
        mImageUris = new ArrayList<>();
        mImageContainers = new ArrayList<>();
        mMaxImages = maxImages;
    }

    public void addImage(Uri imageUri) {
        lastImageIndex++;
        mImageUris.add(imageUri.toString());

        LayoutInflater inflater = LayoutInflater.from(mContext);
        ConstraintLayout constraintLayout = (ConstraintLayout) inflater.inflate(R.layout.image_minus_layout, null, false);
        mImageContainers.add(constraintLayout);

        //making the coverImageMsg visible on the first image added
        makeFirstImageCover();
        updateAddButton();

        RoundedImageView minus = constraintLayout.findViewById(R.id.minus);
        minus.setTag(lastImageIndex);
        minus.setOnClickListener(v -> {
            Log.d("nerd", "clicked " + v.getTag());
            remove((int) v.getTag());
        });

        RoundedImageView newImage = constraintLayout.findViewById(R.id.roundedImageView);
        newImage.setImageURI(imageUri);
        newImage.setTag(lastImageIndex);

        newImage.setOnClickListener(v -> {
            coverImageIndex = (int) v.getTag();
            Log.d("nerd", "coverImageIndex: " + coverImageIndex);
            removeCoverImageMsg();
            mImageContainers.get(coverImageIndex).findViewById(R.id.cover_image_msg).setVisibility(View.VISIBLE);
        });
        layout.addView(constraintLayout, lastImageIndex);
        int layoutWidth = layout.getWidth();
        int padding = 25;
        int childWidth = (layoutWidth - padding) / 3;
        constraintLayout.getLayoutParams().width = childWidth;
    }

    private void remove(int removeIndex) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mContext);
        alertBuilder.setTitle("Remove Image");
        alertBuilder.setPositiveButton("Ok", (dialog, which) -> {
            mImageUris.remove(removeIndex);
            mImageContainers.remove(removeIndex);
            layout.removeViewAt(removeIndex);
            Log.d("nerd", "removeIndex: " + removeIndex);
            lastImageIndex--;
            if (removeIndex == coverImageIndex) {
                makeFirstImageCover();
            }
            updateTags();
            updateAddButton();
        });
        alertBuilder.setNegativeButton("Cancel", (dialog, which) -> {
        });
        alertBuilder.show();
    }

    private void updateTags() {
        int childCount = layout.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = layout.getChildAt(i);
            RoundedImageView minus = view.findViewById(R.id.minus);
            RoundedImageView newImage = view.findViewById(R.id.roundedImageView);
            //in case of the plus imageButton there is no minus imageView.
            if (minus != null) {
                minus.setTag(i);
                newImage.setTag(i);
            }
        }
    }

    private void updateAddButton() {
        if (mImageUris.size() >= mMaxImages) {
            mAddImageBTN.setVisibility(View.GONE);
        } else {
            mAddImageBTN.setVisibility(View.VISIBLE);
        }
    }

    private void removeCoverImageMsg() {
        for (ConstraintLayout layout : mImageContainers) {
            TextView coverImageMsg = layout.findViewById(R.id.cover_image_msg);
            coverImageMsg.setVisibility(View.INVISIBLE);
        }
    }

    private void makeFirstImageCover() {
        if (mImageContainers.size() >= 1) {
            coverImageIndex = 0;
            mImageContainers.get(coverImageIndex).findViewById(R.id.cover_image_msg).setVisibility(View.VISIBLE);
        }
    }

    public List<String> getImageUris() {
        return mImageUris;
    }

    public int getCoverImageIndex() {
        return coverImageIndex;
    }
}
