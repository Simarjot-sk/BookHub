package com.simarjot.bookwala;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.makeramen.roundedimageview.RoundedImageView;
import com.simarjot.bookwala.helpers.GetPictureActivity;
import com.simarjot.bookwala.helpers.Helper;

public class RegistrationActivity extends AppCompatActivity {
    private static final int GET_PICTURE_CODE = 123;
    private static final String TAG = "nerd";
    private Uri mImageUri;

    //widgets
    private LabeledEditText mNameLET;
    private LabeledEditText mEmailLET;
    private RoundedImageView mProfileIV;
    private RoundedImageView mAddIcon;
    private ImageView mBackBtn;
    private ImageButton mSubmitButton;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        initViews();
        mProgressBar.setVisibility(View.GONE);
        mProfileIV.setOnClickListener(v -> {
            Intent getPictureIntent = new Intent(this, GetPictureActivity.class);
            getPictureIntent.putExtra(GetPictureActivity.CIRCULAR_DIMMED_LAYER, true);
            startActivityForResult(getPictureIntent, GET_PICTURE_CODE);
        });
        mAddIcon.setOnClickListener(v -> {

        });
        mSubmitButton.setOnClickListener(v -> updateUserDetails());
    }

    private void initViews() {
        mNameLET = findViewById(R.id.name_let);
        mEmailLET = findViewById(R.id.email_let);
        mSubmitButton = findViewById(R.id.submit_btn);
        mProfileIV = findViewById(R.id.profile_image);
        mAddIcon = findViewById(R.id.plus_icon);
        mProgressBar = findViewById(R.id.progressBar);
    }

    private void updateUserDetails() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "User is not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        StorageReference fs = FirebaseStorage.getInstance().getReference("images/users");
        StorageReference userProfileRef = fs.child(user.getUid());
        mProgressBar.setVisibility(View.VISIBLE);
        mSubmitButton.setClickable(false);
        userProfileRef.putFile(mImageUri).addOnFailureListener(e -> Log.d(Helper.TAG, "failed to upload file to server", e))
                .addOnCompleteListener(task -> userProfileRef.getDownloadUrl().addOnSuccessListener(uri -> updateUser(uri)));
    }

    private void updateUser(Uri downloadUri) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(mNameLET.getText())
                .setPhotoUri(downloadUri)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(task -> {
                    Toast.makeText(RegistrationActivity.this, "profile updated successfully", Toast.LENGTH_SHORT).show();
                    mProgressBar.setVisibility(View.GONE);
                    mSubmitButton.setClickable(true);
                    setResult(RESULT_OK);
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(RegistrationActivity.this, "failed to update profile", Toast.LENGTH_SHORT).show());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_PICTURE_CODE && resultCode == RESULT_OK) {
            mImageUri = data.getData();
            mProfileIV.setImageURI(mImageUri);
        }
    }
}
