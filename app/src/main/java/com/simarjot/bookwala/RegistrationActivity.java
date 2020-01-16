package com.simarjot.bookwala;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.makeramen.roundedimageview.RoundedImageView;
import com.simarjot.bookwala.helpers.GetPictureActivity;
import com.simarjot.bookwala.helpers.Helper;
import com.yalantis.ucrop.UCrop;

import java.io.File;

public class RegistrationActivity extends AppCompatActivity {
    private static final int GET_PICTURE_CODE = 123;
    private static final String TAG = "nerd";
    private LabeledEditText mNameLET;
    private LabeledEditText mEmailLET;
    private RoundedImageView mProfileIV;
    private RoundedImageView mAddIcon;
    private ImageView mBackBtn;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        initViews();

        mProfileIV.setOnClickListener(v -> {
            Intent getPictureIntent = new Intent(this, GetPictureActivity.class);
            startActivityForResult(getPictureIntent, GET_PICTURE_CODE);
        });
        mAddIcon.setOnClickListener(v -> {

        });
    }

    private void initViews(){
        mNameLET = findViewById(R.id.name_let);
        mEmailLET = findViewById(R.id.email_let);
        mProfileIV = findViewById(R.id.profile_image);
        mAddIcon = findViewById(R.id.plus_icon);
    }

    public void onClickNext(View view) {
        if (!mNameLET.hasErrors() && !mEmailLET.hasErrors()) {
            updateUserDetails();
        }
    }

    private void updateUserDetails() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "User is not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        String name = mNameLET.getText();
        String email = mEmailLET.getText();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .setPhotoUri(imageUri)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(task -> Toast.makeText(RegistrationActivity.this, "profile updated successfully", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(RegistrationActivity.this, "failed to update profile", Toast.LENGTH_SHORT).show());


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GET_PICTURE_CODE && resultCode == RESULT_OK){
            mProfileIV.setImageURI(data.getData());
        }
    }

    //
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode) {
//            case 2:
//                Log.d(TAG, "External storage2");
//                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    Log.v(TAG, "Permission: " + permissions[0] + "was " + grantResults[0]);
//                    //resume tasks needing this permission
//                    Helper.pickImageFromGallery(RegistrationActivity.this, GALLERY_REQUEST_CODE);
//                } else {
//                    Toast.makeText(this, "permission not granted", Toast.LENGTH_SHORT).show();
//                }
//                break;
//
//            case 3:
//                Log.d(TAG, "External storage1");
//                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    Log.v(TAG, "Permission: " + permissions[0] + "was " + grantResults[0]);
//                    //resume tasks needing this permission
//                    Helper.pickImageFromGallery(RegistrationActivity.this, GALLERY_REQUEST_CODE);
//                } else {
//                    Toast.makeText(this, "permission not granted", Toast.LENGTH_SHORT).show();
//                }
//                break;
//        }
//    }
//
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == Activity.RESULT_OK) {
//            switch (requestCode) {
//                case GALLERY_REQUEST_CODE:
//                    Uri selectedImage = data.getData();
//                    Log.d("nerd", "user selected this image" + selectedImage.toString());
//                    File file = Helper.getImageFile(); // 2
//                    Uri destinationUri = Uri.fromFile(file);
//
//                    UCrop.Options options = new UCrop.Options();
//                    options.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
//                    options.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary));
//
//                    options.setToolbarWidgetColor(ContextCompat.getColor(this, R.color.white));
//                    options.setRootViewBackgroundColor(ContextCompat.getColor(this, R.color.dark_grey));
//                    options.setCropFrameColor(ContextCompat.getColor(this, R.color.dark_grey));
//                    options.setActiveWidgetColor(ContextCompat.getColor(this, R.color.colorPrimary));
//                    options.setRootViewBackgroundColor(ContextCompat.getColor(this, R.color.black));
//
//                    options.setCircleDimmedLayer(true);
//                    options.setCompressionQuality(100);
//
//                    UCrop.of(selectedImage, destinationUri)
//                            .withMaxResultSize(100, 100)
//                            .withOptions(options)
//                            .start(RegistrationActivity.this);
//                    break;
//                case UCrop.RESULT_ERROR:
//                    final Throwable cropError = UCrop.getError(data);
//                    Log.d("nerd", "UCrop error occured\n", cropError);
//                    break;
//                case UCrop.REQUEST_CROP:
//                    imageUri = UCrop.getOutput(data);
//                    Log.d("nerd", "recieved cropped image: " + imageUri.toString());
//                    mProfileIV.setImageURI(imageUri);
//                    break;
//                default:
//                    throw new IllegalStateException("Unexpected value: " + requestCode);
//            }
//        } else if (resultCode == Activity.RESULT_CANCELED) {
//            Log.d("nerd", "result cancelled");
//        }
//
//        if (resultCode == 96) {
//            final Throwable cropError = UCrop.getError(data);
//            Log.d("nerd", "UCrop error occured\n", cropError);
//        }
//    }
}