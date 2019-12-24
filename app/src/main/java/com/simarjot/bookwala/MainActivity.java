package com.simarjot.bookwala;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static String TAG = "nerd";
    private ImageButton button;
    private EditText emailEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.button);
        button.setVisibility(View.GONE);

        emailEditText = findViewById(R.id.editText);
        addTextWatcherToEmailEditText();
    }

    //on click listener for next button
    public void onClickNext(View view){
        String email = emailEditText.getText().toString().trim();
        Log.d("nerd", email);
        if(!isValidEmail(email)){
            emailEditText.setError("Enter a valid Email Address");
        }else{
            startActivity(new Intent(MainActivity.this, RegistrationActivity.class));
        }
    }

    //on click listener for forgot password textView
    public void onClickForgotPassword(View view){
        startActivity(new Intent(MainActivity.this, ForgotPasswordActivity.class));
    }

    private void addTextWatcherToEmailEditText(){
        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String currentText = s.toString();
                if(isValidEmail(currentText)){
                    button.setVisibility(View.VISIBLE);
                }else {
                    button.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    public static boolean isValidEmail(CharSequence target) {
        return (Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}
