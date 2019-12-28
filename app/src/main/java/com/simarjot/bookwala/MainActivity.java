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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.FirebaseAuth;
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
            sendEmail(userIdentifier);
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


    private void sendEmail(final String email){
        Log.d(TAG, "sending email...");
        ActionCodeSettings actionCodeSettings =
                ActionCodeSettings.newBuilder()
                        .setUrl("https://bookwala.page.link/signIn")
                        .setHandleCodeInApp(true)
                        .setIOSBundleId("com.example.ios")
                        .setAndroidPackageName(
                                "com.simarjot.bookwala",
                                true,
                                "12")
                        .build();

        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.sendSignInLinkToEmail(email, actionCodeSettings)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Email sent to " + email);

                            //saving the email in shared pref so that it is persisted even if the app is destroyed or stopped
                            SharedPreferences preferences = getSharedPreferences("email", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString(EmailSigninActivity.SAVED_EMAIL, email);
                            editor.apply();

                            Toast.makeText(MainActivity.this, "Email sent to " + email, Toast.LENGTH_SHORT).show();

                        }else if(task.isCanceled()){
                            Log.d(TAG, "cancelled");
                            Toast.makeText(MainActivity.this, "Failed to Send Email", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Failed to Send Email", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "email sending failed", e);
                    }
                });
    }
}
