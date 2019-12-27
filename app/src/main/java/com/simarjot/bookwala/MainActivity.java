package com.simarjot.bookwala;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.simarjot.bookwala.helpers.EmailHelper;

public class MainActivity extends AppCompatActivity {
    public static String EMAIL_EXTRA="email_extra";
    public static String MOBILE_EXTRA = "mobile_extra";
    private static String TAG = "nerd";

    private TextView errorMessage;
    private ImageButton nextButton;
    private EditText emailEditText;
    private boolean isEmail = false;
    private boolean isMobile = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nextButton = findViewById(R.id.button2);
        errorMessage = findViewById(R.id.errorMessage);
        emailEditText = findViewById(R.id.editText);
        addTextWatcherToEmailEditText();
    }

    //on click listener for next button
    public void onClickNext(View view){
        String userIdentifier = emailEditText.getText().toString().trim();
        Log.d("nerd", userIdentifier);
        if(isMobile){
            Intent otpIntent = new Intent(MainActivity.this, OtpActivity.class);
            otpIntent.putExtra(MOBILE_EXTRA, userIdentifier);
            startActivity(otpIntent);
        }else if(isEmail){
            Intent emailSigninIntent = new Intent(MainActivity.this, EmailSigninActivity.class);
            emailSigninIntent.putExtra( EMAIL_EXTRA, userIdentifier);
            startActivity(emailSigninIntent);
        }
    }

    //on click listener for forgot password textView
    public void onClickForgotPassword(View view){
        startActivity(new Intent(MainActivity.this, ForgotPasswordActivity.class));
    }

    private void addTextWatcherToEmailEditText(){
        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String currentText = s.toString();
                if(EmailHelper.seemsNumber(currentText)){
                    //seems a phone no
                    if(EmailHelper.hasTenDigits(currentText)){
                        setNoErrors("Signing in with Mobile Number");
                        isMobile=true;
                    }else{
                        setError("Enter valid Mobile Number with 10 digits");
                        isMobile=false;
                    }
                }else {
                    //seems an email
                    if(EmailHelper.isValidEmail(currentText)){
                        setNoErrors("Signing in with Email id");
                        isEmail=true;
                    }else{
                        setError("Enter valid Email in format aaa@bb.cc");
                        isEmail=false;
                    }
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void setError(String msg){
        errorMessage.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.red));
        errorMessage.setText(msg);
        nextButton.setClickable(false);
        nextButton.setEnabled(false);
    }

    private void setNoErrors(String msg){
        errorMessage.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimary));
        errorMessage.setText(msg);
        nextButton.setClickable(true);
        nextButton.setEnabled(true);
    }
}
