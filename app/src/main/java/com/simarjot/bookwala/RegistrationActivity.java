package com.simarjot.bookwala;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.simarjot.bookwala.helpers.EmailHelper;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;

public class RegistrationActivity extends AppCompatActivity {
    private static final int GALLERY_REQUEST_CODE = 123;
    private static final String TAG = "nerd";
    private EditText name;
    private EditText email;
    private EditText password;
    private EditText confirmPassword;
    private CircularImageView profileView;
    private ImageView backView;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirm_password);
        profileView = findViewById(R.id.profile_image);
        backView = findViewById(R.id.back_button);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            name.setText(user.getDisplayName());
            profileView.setImageURI(user.getPhotoUrl());
        }


        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        profileView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isReadStoragePermissionGranted()&&isWriteStoragePermissionGranted()){
                    pickImage();
                }
            }
        });
        CircularImageView plusIcon = findViewById(R.id.plus_icon);
        plusIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isReadStoragePermissionGranted()&&isWriteStoragePermissionGranted()){
                    pickImage();
                }
            }
        });
        email.setText(getIntent().getStringExtra(MainActivity.EMAIL_EXTRA));
    }

    public void onClickNext(View view){
        String nameString = name.getText().toString();
        String emailString = email.getText().toString();
        String passwordString = password.getText().toString();
        String confirmString = confirmPassword.getText().toString();
        
        boolean hasErrors=false;
        if(!EmailHelper.isValidEmail(emailString)){
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
            updateUserDetails(nameString);
        }
    }

    private void updateUserDetails(String name){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user==null){
            Toast.makeText(this, "User is not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .setPhotoUri(imageUri)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(RegistrationActivity.this, "profile updated successfully", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "User profile updated.");
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                             Log.d(TAG, "failed to update profile", e);
                        Toast.makeText(RegistrationActivity.this, "failed to update profile", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 2:
                Log.d(TAG, "External storage2");
                if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    Log.v(TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);
                    //resume tasks needing this permission
                    pickImage();
                }else{
                    Toast.makeText(this, "permission not granted", Toast.LENGTH_SHORT).show();
                }
                break;

            case 3:
                Log.d(TAG, "External storage1");
                if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    Log.v(TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);
                    //resume tasks needing this permission
                    pickImage();
                }else{
                    Toast.makeText(this, "permission not granted", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void pickImage(){
        Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
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
        if (resultCode == Activity.RESULT_OK){
            switch (requestCode){
                case GALLERY_REQUEST_CODE:
                    Uri selectedImage = data.getData();
                    Log.d("nerd","user selected this image" + selectedImage.toString());
                    File file = getImageFile(); // 2
                    Uri destinationUri = Uri.fromFile(file);

                    UCrop.Options options = new UCrop.Options();
                    options.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
                    options.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary));

                    options.setToolbarWidgetColor(ContextCompat.getColor(this, R.color.white));
                    options.setRootViewBackgroundColor(ContextCompat.getColor(this, R.color.dark_grey));
                    options.setCropFrameColor(ContextCompat.getColor(this, R.color.dark_grey));
                    options.setActiveWidgetColor(ContextCompat.getColor(this, R.color.colorPrimary));
                    options.setRootViewBackgroundColor(ContextCompat.getColor(this, R.color.black));

                    options.setCircleDimmedLayer(true);
                    options.setCompressionQuality(100);

                    UCrop.of(selectedImage,destinationUri)
                            .withMaxResultSize(100, 100)
                            .withOptions(options)
                            .start(RegistrationActivity.this);
                    break;
                case UCrop.RESULT_ERROR:
                    final Throwable cropError = UCrop.getError(data);
                    Log.d("nerd", "UCrop error occured\n", cropError);
                    break;
                case UCrop.REQUEST_CROP:
                    imageUri = UCrop.getOutput(data);
                    Log.d("nerd", "recieved cropped image: " + imageUri.toString());
                    profileView.setImageURI(imageUri);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + requestCode);
            }
        } else if (resultCode == Activity.RESULT_CANCELED){
            Log.d("nerd", "result cancelled");
        }

        if(resultCode==96){
            final Throwable cropError = UCrop.getError(data);
            Log.d("nerd", "UCrop error occured\n", cropError);
        }
    }

    private File getImageFile() {
        String imageFileName = "JPEG_" + System.currentTimeMillis() + "_";
        File storageDir = new File(
                Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DCIM
                ), "Camera"
        );
        File file = null;
        try {
             file = File.createTempFile(
                    imageFileName, ".jpg", storageDir
            );
        }catch (IOException ex){
            Log.d("nerd", "io exception", ex);
        }

        return file;
    }


    public  boolean isReadStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted1");
                return true;
            } else {

                Log.v(TAG,"Permission is revoked1");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted1");
            return true;
        }
    }

    public  boolean isWriteStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted2");
                return true;
            } else {

                Log.v(TAG,"Permission is revoked2");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted2");
            return true;
        }
    }
}
