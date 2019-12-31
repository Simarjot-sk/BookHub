package com.simarjot.bookwala;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.FirebaseAuth;
import com.hbb20.CountryCodePicker;
import com.simarjot.bookwala.helpers.EmailHelper;

public class MainActivity extends AppCompatActivity {
    public static String MOBILE_EXTRA = "mobile_extra";
    public static String MOBILE_EXTRA_FORMATTED = "mobile_formatted";
    private static String TAG = "nerd";

    private CountryCodePicker ccp;
    private EditText mobileEditText;
    private ImageButton nextButton;
    private ImageView tickView;
    private boolean isValid = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        //checking if the user is already logged in
        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            Intent homeIntent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(homeIntent);
        }

        setContentView(R.layout.activity_main);
        nextButton = findViewById(R.id.button2);
        mobileEditText = findViewById(R.id.mobile_edit_text);
        ccp = findViewById(R.id.ccp);
        tickView= findViewById(R.id.tick_mark);
        tickView.setVisibility(View.GONE);

        ccp.registerCarrierNumberEditText(mobileEditText);

        ccp.setPhoneNumberValidityChangeListener(new CountryCodePicker.PhoneNumberValidityChangeListener() {
            @Override
            public void onValidityChanged(boolean isValidNumber) {
                isValid = isValidNumber;
                if(isValid){
                    tickView.setVisibility(View.VISIBLE);
                }else{
                    tickView.setVisibility(View.GONE);
                }
            }
        });
    }

    //on click listener for next button
    public void onClickNext(View view){
        if(isValid){
            String mobileNo = ccp.getFullNumberWithPlus().trim();
            Intent otpIntent = new Intent(MainActivity.this, OtpActivity.class);
            otpIntent.putExtra(MOBILE_EXTRA, mobileNo);
            otpIntent.putExtra(MOBILE_EXTRA_FORMATTED, ccp.getFormattedFullNumber().trim());
            startActivity(otpIntent);
        } else{
            Toast.makeText(this, "Please Enter a Valid Mobile Number", Toast.LENGTH_SHORT).show();
        }
    }
}
