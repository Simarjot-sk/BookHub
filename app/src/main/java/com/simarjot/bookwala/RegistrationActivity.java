package com.simarjot.bookwala;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class RegistrationActivity extends AppCompatActivity {
    private static final int GALLERY_REQUEST_CODE = 123;
    private EditText name;
    private EditText email;
    private EditText password;
    private EditText confirmPassword;
    private ImageView profileView;
    private ImageButton nextbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        nextbutton = findViewById(R.id.button);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirm_password);
        profileView = findViewById(R.id.profile_image);
    }
    
    public void onClickNext(View view){
        String nameString = name.getText().toString();
        String emailString = email.getText().toString();
        String passwordString = password.getText().toString();
        String confirmString = confirmPassword.getText().toString();
        
        boolean hasErrors=false;
        if(!MainActivity.isValidEmail(emailString)){
            email.setError("Enter a valid Email Address");  
            hasErrors = true;
        }
        if(nameString.length()<2){
            name.setError("Name should have atleast 2 characters");
            hasErrors = true;
        }
        if(passwordString.length()<8){
            password.setError("Password should contain atleast 8 digits");
            hasErrors = true;
        }
        if(!passwordString.equals(confirmString)){
            confirmPassword.setError("Passwords do not match");
            hasErrors = true;
        }
        
        if(!hasErrors){
            Toast.makeText(this, "User registered successfully", Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickProfileImage(View view){
        Intent intent=new Intent(Intent.ACTION_PICK);
        // Sets the type as image/*. This ensures only components of type image are selected
        intent.setType("image/*");
        //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
        // Launching the Intent
        startActivityForResult(intent,GALLERY_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK)
            switch (requestCode){
                case GALLERY_REQUEST_CODE:
                    Uri selectedImage = data.getData();
                    profileView.setImageURI(selectedImage);
                    break;
            }
    }
}
