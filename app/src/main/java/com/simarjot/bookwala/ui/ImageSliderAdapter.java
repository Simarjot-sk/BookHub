package com.simarjot.bookwala.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.simarjot.bookwala.R;

import java.util.List;

public class ImageSliderAdapter extends PagerAdapter {
    List<String> mImageUris;
    LayoutInflater mInflater;
    Context mContext;

    public ImageSliderAdapter(Context context, List<String> imageUris){
        mImageUris = imageUris;
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View imageLayout = mInflater.inflate(R.layout.slider_item, container, false);
        ImageView bookIV = imageLayout.findViewById(R.id.book_image);
        Glide.with(mContext).load(mImageUris.get(position)).into(bookIV);
        container.addView(imageLayout);
        return imageLayout;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return mImageUris.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }
}
