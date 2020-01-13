package com.simarjot.bookwala;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.simarjot.bookwala.model.Book;

public class SeeDetailsActivity extends AppCompatActivity {
    private Book book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_details);

    }
}
