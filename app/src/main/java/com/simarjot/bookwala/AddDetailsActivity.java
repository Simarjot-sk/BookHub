package com.simarjot.bookwala;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.makeramen.roundedimageview.RoundedImageView;
import com.simarjot.bookwala.model.BookSharedPrefs;
import com.simarjot.bookwala.model.Category;
import com.warkiz.widget.IndicatorSeekBar;
import com.warkiz.widget.OnSeekChangeListener;
import com.warkiz.widget.SeekParams;

public class AddDetailsActivity extends AppCompatActivity {
    private SharedPreferences mSharedPrefs;
    private Category mCategory;
    private int mLevel = 1;

    //widgets
    private IndicatorSeekBar mLevelSeekBar;
    private TextView mLevelTV;
    private TextView mLevelTypeTV;
    private RoundedImageView mCoverImage;
    private LabeledEditText mSubjectTV;
    private LabeledEditText mTitleTV;
    private LabeledEditText mAuthorTV;
    private Button mDoneButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_details);
        mSharedPrefs = getSharedPreferences(BookSharedPrefs.SHARED_PREFS, MODE_PRIVATE);
        mCategory = Category.valueOf( mSharedPrefs.getString(BookSharedPrefs.CATEGORY,null) );
        mSubjectTV = findViewById(R.id.subject_et);
        mTitleTV = findViewById(R.id.title_et);
        mAuthorTV = findViewById(R.id.author_et);
        mCoverImage = findViewById(R.id.cover_image);
        mLevelTV = findViewById(R.id.subject_tv);
        mLevelTypeTV = findViewById(R.id.level_type_tv);
        mLevelSeekBar = findViewById(R.id.level_seek_bar);
        mDoneButton = findViewById(R.id.done_btn);
        TextView categoryTV = findViewById(R.id.category_tv);
        switch(mCategory){
            case SCHOOL:
                mLevelTypeTV.setText(R.string.class_msg);
                mLevelSeekBar.setMax(12);
                mLevelSeekBar.setTickCount(12);
                categoryTV.setText(R.string.school);
                break;
            case COLLEGE:
                mLevelTypeTV.setText(R.string.semester_msg);
                mLevelSeekBar.setMax(8);
                mLevelSeekBar.setTickCount(8);
                categoryTV.setText(R.string.college);
                break;
            case COMPETITIVE:
                mLevelSeekBar.setVisibility(View.GONE);
                mLevelTypeTV.setVisibility(View.GONE);
                mLevelTV.setVisibility(View.GONE);
                mSubjectTV.setHint("Exam Name");
                categoryTV.setText(R.string.competitive);
                break;
            case OTHERS:
                mLevelSeekBar.setVisibility(View.GONE);
                mLevelTypeTV.setVisibility(View.GONE);
                mLevelTV.setVisibility(View.GONE);
                mSubjectTV.setHint("Genre");
                categoryTV.setText(R.string.others);
                break;
        }

        mLevelSeekBar.setOnSeekChangeListener(new OnSeekChangeListener() {
            @Override
            public void onSeeking(SeekParams seekParams) {

            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {
               mLevel = seekBar.getProgress();

                if(mLevel==1){
                    mLevelTV.setText(mLevel + "st");
                }else if(mLevel==2){
                    mLevelTV.setText(mLevel + "nd");
                }else if(mLevel==3){
                    mLevelTV.setText(mLevel + "rd");
                }else if(mLevel>3 && mLevel<13){
                    mLevelTV.setText(mLevel + "th");
                }
            }
        });

        mDoneButton.setOnClickListener(v ->{
            String author = mAuthorTV.getText();
            String subject = mSubjectTV.getText();
            String title = mTitleTV.getText();
            if(author!=null && subject!=null && title!=null ){
                SharedPreferences.Editor editor = getSharedPreferences(BookSharedPrefs.SHARED_PREFS, MODE_PRIVATE).edit();
                editor.putString(BookSharedPrefs.SUBJECT, subject);
                editor.putInt(BookSharedPrefs.LEVEL, mLevel);
                editor.putString(BookSharedPrefs.AUTHOR, author);
                editor.putString(BookSharedPrefs.TITLE, title);
                editor.apply();
                Intent intent = new Intent(this, AddDetailsActivity_2.class);
                startActivity(intent);
            }else {
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            }
        });

        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish());
        SharedPreferences prefs = getSharedPreferences(BookSharedPrefs.SHARED_PREFS, MODE_PRIVATE);
        String coverImageUrl = prefs.getString(BookSharedPrefs.COVER_IMAGE_URI, null);
        mCoverImage.setImageURI(Uri.parse(coverImageUrl));
    }
}
