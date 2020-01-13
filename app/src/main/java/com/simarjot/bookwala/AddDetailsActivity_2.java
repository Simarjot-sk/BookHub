package com.simarjot.bookwala;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.simarjot.bookwala.model.Book;
import com.simarjot.bookwala.model.BookSharedPrefs;
import com.simarjot.bookwala.model.Server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Currency;
import java.util.List;
import java.util.Set;

public class AddDetailsActivity_2 extends AppCompatActivity {
    private Context mContext;
    //widgets
    private Spinner mCurrencySpinner;
    private EditText mDescET;
    private EditText mPriceET;
    private Button mDoneBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_details_2);
        mContext = AddDetailsActivity_2.this;
        mCurrencySpinner = findViewById(R.id.currency_selector_spinner);
        mDescET = findViewById(R.id.description_et);
        mPriceET = findViewById(R.id.price_et);
        mDoneBTN = findViewById(R.id.done_btn);

        Set<Currency> currencySet = Currency.getAvailableCurrencies();
        List<String> currencySymbols = new ArrayList<>();


        for(Currency currency : currencySet){
            currencySymbols.add(currency.getSymbol());
        }

        Collections.sort(currencySymbols, String::compareTo);
        Collections.swap(currencySymbols, 0, 295);
        Collections.swap(currencySymbols, 1, 246);
        Collections.swap(currencySymbols, 2, 195);
        Collections.swap(currencySymbols, 3, 26);
        Collections.swap(currencySymbols, 4, 185);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, currencySymbols);
        mCurrencySpinner.setAdapter(spinnerAdapter);

        mDoneBTN.setOnClickListener(v->{
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
                server.uploadBookWithImages(BookSharedPrefs.getImageUris(mContext),
                        BookSharedPrefs.getCoverImageUri(mContext), 
                        book.getBookUUID());
            }else{
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
