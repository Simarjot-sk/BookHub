package com.simarjot.bookwala;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseUserMetadata;

import java.util.Optional;

public class EmailSigninActivity extends AppCompatActivity {
    private static final String TAG = "nerd";
    public static final String SAVED_EMAIL="saved_email";
    private String email;
    private SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_signin);

        sharedpreferences = getSharedPreferences("email", Context.MODE_PRIVATE);
        if (sharedpreferences.contains(SAVED_EMAIL)) {
            email = sharedpreferences.getString(SAVED_EMAIL, "");
        }else{
            Log.d(TAG, "shared prefereces does not contain email");
        }
        verify();
    }

    public void verify(){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        Intent intent = getIntent();
        String emailLink = intent.getData().toString();

        // Confirm the link is a sign-in with email link.
        if (auth.isSignInWithEmailLink(emailLink)) {
            // Retrieve this from wherever you stored it

            // The client SDK will parse the code from the link for you.
            auth.signInWithEmailLink(email, emailLink)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "Successfully signed in with email link!");
                                AuthResult result = task.getResult();

                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                editor.clear();
                                editor.apply();
                                Log.d(TAG, "shared preferences cleared");

                                if(result!=null){
                                    boolean isNewUser = result.getAdditionalUserInfo().isNewUser();
                                    if(isNewUser){
                                        Intent registrationIntent = new Intent(EmailSigninActivity.this, RegistrationActivity.class);
                                        registrationIntent.putExtra(MainActivity.EMAIL_EXTRA, email);
                                        startActivity(registrationIntent);
                                    }else{
                                        Intent welcomeIntent = new Intent(EmailSigninActivity.this, Login.class);
                                        startActivity(welcomeIntent);
                                        Toast.makeText(EmailSigninActivity.this, "Welcome Back!", Toast.LENGTH_LONG).show();
                                    }
                                }
                            } else {
                                Log.e(TAG, "Error signing in with email link", task.getException());
                                Toast.makeText(EmailSigninActivity.this, "Error signing in with email link", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    });
        }
    }
}
