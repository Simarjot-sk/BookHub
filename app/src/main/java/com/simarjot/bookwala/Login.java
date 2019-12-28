package com.simarjot.bookwala;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mikhaellopez.circularimageview.CircularImageView;

public class Login extends AppCompatActivity {
    private CircularImageView profileImageView;
    private TextView nameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        profileImageView = findViewById(R.id.profile_image);
        nameView = findViewById(R.id.name);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser!=null){
            profileImageView.setImageURI(currentUser.getPhotoUrl());
            nameView.setText(currentUser.getDisplayName());
        }
    }
}
