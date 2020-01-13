package com.simarjot.bookwala;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.simarjot.bookwala.helpers.Helper;
import com.simarjot.bookwala.model.Book;
import com.simarjot.bookwala.model.BookSharedPrefs;
import com.simarjot.bookwala.model.Server;

import java.util.Arrays;
import java.util.List;

public class AddDetailsActivity_2 extends AppCompatActivity {
    private Context mContext;
    //widgets
    private Spinner mCurrencySpinner;
    private EditText mDescET;
    private EditText mPriceET;
    private Button mDoneBTN;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_details_2);
        mContext = AddDetailsActivity_2.this;
        mProgressBar = findViewById(R.id.progress_bar);
        mCurrencySpinner = findViewById(R.id.currency_selector_spinner);
        mDescET = findViewById(R.id.description_et);
        mPriceET = findViewById(R.id.price_et);
        mDoneBTN = findViewById(R.id.done_btn);

        mProgressBar.setVisibility(View.GONE);

        List<String> currencySymbols = Arrays.asList("\u20B9", "US$", "PKR", "AU$", "CA$", "BDT", "NPR");

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, currencySymbols);
        mCurrencySpinner.setAdapter(spinnerAdapter);

        mDoneBTN.setOnClickListener(v->{
            mDoneBTN.setClickable(false);
            Log.d(Helper.TAG, "done button clicked");
            String desc = mDescET.getText().toString();
            String price = mPriceET.getText().toString();
            String currency = (String) mCurrencySpinner.getSelectedItem();

            if(!desc.isEmpty() && !price.isEmpty() && currency!=null ){
                SharedPreferences.Editor editor= getSharedPreferences(BookSharedPrefs.SHARED_PREFS, MODE_PRIVATE).edit();
                editor.putString(BookSharedPrefs.DESCRIPTION, desc);
                editor.putString(BookSharedPrefs.PRICE, price);
                editor.putString(BookSharedPrefs.CURRENCY, currency);
                editor.apply();

                Book book = BookSharedPrefs.getBook(mContext);
                Server server = new Server(book);
                mProgressBar.setVisibility(View.VISIBLE);

                server.uploadBookWithImages(BookSharedPrefs.getImageUris(mContext),
                        BookSharedPrefs.getCoverImageUri(mContext), 
                        book.getBookUUID())
                        .setUploadFinishedListener(new Server.OnUploadFinishedListener() {
                            @Override
                            public void onUploadFinish() {
                                mProgressBar.setVisibility(View.GONE);
                                mDoneBTN.setClickable(true);
                                Intent intent = new Intent(AddDetailsActivity_2.this, HomeActivity.class);
                                startActivity(intent);
                                finish();
                            }

                            @Override
                            public void onUploadFailed() {
                                mProgressBar.setVisibility(View.GONE);
                                mDoneBTN.setClickable(true);
                            }
                        });
            }else{
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
