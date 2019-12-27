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

public class EmailSigninActivity extends AppCompatActivity {
    private static final String TAG = "nerd";
    private static final String SAVED_EMAIL="saved_email";
    private String email;
    private SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_signin);

        email = getIntent().getStringExtra(MainActivity.EMAIL_EXTRA);

        sharedpreferences = getSharedPreferences("email", Context.MODE_PRIVATE);
        if (sharedpreferences.contains(SAVED_EMAIL)) {
            email = sharedpreferences.getString(SAVED_EMAIL, "");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "on stop");

        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(SAVED_EMAIL, email);
        editor.commit();
    }

    public void sendEmail(View view){
        Log.d(TAG, "sending email...");
        ActionCodeSettings actionCodeSettings =
                ActionCodeSettings.newBuilder()
                        // URL you want to redirect back to. The domain (www.example.com) for this
                        // URL must be whitelisted in the Firebase Console.
                        .setUrl("https://bookwala.page.link/signIn")
                        // This must be true
                        .setHandleCodeInApp(true)
                        .setIOSBundleId("com.example.ios")
                        .setAndroidPackageName(
                                "com.simarjot.bookwala",
                                true, /* installIfNotAvailable */
                                "12" /* minimumVersion */)
                        .build();


        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.sendSignInLinkToEmail(email, actionCodeSettings)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Email sent to " + email);
                            Toast.makeText(EmailSigninActivity.this, "Email sent to " + email, Toast.LENGTH_SHORT).show();
                        }else if(task.isCanceled()){
                            Log.d(TAG, "cancelled");
                            Toast.makeText(EmailSigninActivity.this, "cancelled", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "email sending failed", e);
                    }

                });
    }

    public void verify(View view){
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
                                Toast.makeText(EmailSigninActivity.this, "Successfully signed in with email link!", Toast.LENGTH_SHORT).show();
                                // You can access the new user via result.getUser()
                                // Additional user info profile *not* available via:
                                // result.getAdditionalUserInfo().getProfile() == null
                                // You can check if the user is new or existing:
                                // result.getAdditionalUserInfo().isNewUser()
                            } else {
                                Log.e(TAG, "Error signing in with email link", task.getException());
                                Toast.makeText(EmailSigninActivity.this, "Error signing in with email link", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}
