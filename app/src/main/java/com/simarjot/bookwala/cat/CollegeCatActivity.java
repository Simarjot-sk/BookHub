package com.simarjot.bookwala.cat;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import com.simarjot.bookwala.R;
import com.simarjot.bookwala.helpers.Helper;
import com.warkiz.widget.IndicatorSeekBar;
import com.warkiz.widget.OnSeekChangeListener;
import com.warkiz.widget.SeekParams;

public class CollegeCatActivity extends AppCompatActivity {
    //widgets

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_college_cat);
        ImageView coverIV = findViewById(R.id.cover_image);
        String coverImageUrl = getSharedPreferences(Helper.BOOK_IMAGE_PREFS, MODE_PRIVATE).getString(Helper.COVER_IMAGE, null);
        coverIV.setImageURI(Uri.parse(coverImageUrl));
        IndicatorSeekBar semesterSeekBar = findViewById(R.id.semester_seek_bar);
        semesterSeekBar.setOnSeekChangeListener(new OnSeekChangeListener() {
            @Override
            public void onSeeking(SeekParams seekParams) {

            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {

            }
        });
    }
}
