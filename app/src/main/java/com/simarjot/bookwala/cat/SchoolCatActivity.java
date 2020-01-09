package com.simarjot.bookwala.cat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.simarjot.bookwala.R;
import com.simarjot.bookwala.helpers.Helper;
import com.warkiz.widget.IndicatorSeekBar;
import com.warkiz.widget.OnSeekChangeListener;
import com.warkiz.widget.SeekParams;

public class SchoolCatActivity extends AppCompatActivity {
    //widgets
    private IndicatorSeekBar mClassSeekBar;
    private TextView mClassTV;
    private RoundedImageView mCoverImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_cat);
        mCoverImage = findViewById(R.id.cover_image);
        mClassTV = findViewById(R.id.text);
        mClassSeekBar = findViewById(R.id.class_seek_bar);

        mClassSeekBar.setOnSeekChangeListener(new OnSeekChangeListener() {
            @Override
            public void onSeeking(SeekParams seekParams) {

            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {
                int progress = seekBar.getProgress();
                if(progress==1){
                    mClassTV.setText(progress + "st");
                }else if(progress==2){
                    mClassTV.setText(progress + "nd");
                }else if(progress==3){
                    mClassTV.setText(progress + "rd");
                }else if(progress>3 && progress<13){
                    mClassTV.setText(progress + "th");
                }
            }
        });
        SharedPreferences prefs = getSharedPreferences(Helper.BOOK_IMAGE_PREFS, MODE_PRIVATE);
        String coverImageUrl = prefs.getString(Helper.COVER_IMAGE, null);
        mCoverImage.setImageURI(Uri.parse(coverImageUrl));
    }
}
