package com.simarjot.bookwala;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.makeramen.roundedimageview.RoundedImageView;
import com.simarjot.bookwala.model.BookSharedPrefs;
import com.simarjot.bookwala.model.Category;

public class CategorySelectorActivity extends AppCompatActivity {
    private RoundedImageView mSchoolBG;
    private RoundedImageView mCollegeBG;
    private RoundedImageView mCompetitiveBG;
    private RoundedImageView mOthersBG;
    private Category category;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories_selector);
        //widgets
        mSchoolBG = findViewById(R.id.school_bg);
        mCollegeBG = findViewById(R.id.college_bg);
        mCompetitiveBG = findViewById(R.id.competitive_bg);
        mOthersBG = findViewById(R.id.others_bg);
        Button doneButton = findViewById(R.id.done_btn);
        Button schoolIV = findViewById(R.id.school_iv);
        Button collegeIV = findViewById(R.id.college_iv);
        Button competitiveIV = findViewById(R.id.competitive_iv);
        Button othersIV = findViewById(R.id.others_iv);
        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish());
        removeBorders();

        schoolIV.setOnClickListener(v->{
            removeBorders();
            category = Category.SCHOOL;
            mSchoolBG.setVisibility(View.VISIBLE);
        });

        collegeIV.setOnClickListener(v->{
            removeBorders();
            category = Category.COLLEGE;
            mCollegeBG.setVisibility(View.VISIBLE);
        });

        competitiveIV.setOnClickListener(v->{
            removeBorders();
            category = Category.COMPETITIVE;
            mCompetitiveBG.setVisibility(View.VISIBLE);
        });

        othersIV.setOnClickListener(v->{
            removeBorders();
            category = Category.OTHERS;
            mOthersBG.setVisibility(View.VISIBLE);
        });

        doneButton.setOnClickListener( v->{
            Log.d("nerd", "category: " + category.name());
            if(category!=null){
                SharedPreferences.Editor editor = getSharedPreferences(BookSharedPrefs.SHARED_PREFS,MODE_PRIVATE).edit();
                editor.putString(BookSharedPrefs.CATEGORY, category.name());
                editor.apply();
                Intent addDetailsIntent = new Intent(this, AddDetailsActivity.class);
                startActivity(addDetailsIntent);
            }else {
                Toast.makeText(this, "Please select a category", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void removeBorders(){
        mSchoolBG.setVisibility(View.INVISIBLE);
        mCollegeBG.setVisibility(View.INVISIBLE);
        mCompetitiveBG.setVisibility(View.INVISIBLE);
        mOthersBG.setVisibility(View.INVISIBLE);
    }

    public Category getCategory(){
        return category;
    }
}
