package com.simarjot.bookwala;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.simarjot.bookwala.databinding.FragmentRegistrationBinding;
import com.simarjot.bookwala.helpers.GetPictureActivity;
import com.simarjot.bookwala.helpers.Helper;

import static android.app.Activity.RESULT_OK;

public class RegistrationFragment extends Fragment {
    private static final int GET_PICTURE_CODE = 123;
    private Uri mImageUri;
    private FragmentRegistrationBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_registration, container, false);
        binding.progressBar.setVisibility(View.GONE);
        binding.profileImage.setOnClickListener(v -> {
            Intent getPictureIntent = new Intent(getActivity(), GetPictureActivity.class);
            getPictureIntent.putExtra(GetPictureActivity.CIRCULAR_DIMMED_LAYER, true);
            startActivityForResult(getPictureIntent, GET_PICTURE_CODE);
        });
        binding.plusIcon.setOnClickListener(v -> {

        });
        binding.submitBtn.setOnClickListener(v -> updateUserDetails());
        return binding.getRoot();
    }


    private void updateUserDetails() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Toast.makeText(getContext(), "User is not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        StorageReference fs = FirebaseStorage.getInstance().getReference("images/users");
        StorageReference userProfileRef = fs.child(user.getUid());
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.submitBtn.setClickable(false);
        userProfileRef.putFile(mImageUri).addOnFailureListener(e -> Log.d(Helper.TAG, "failed to upload file to server", e))
                .addOnCompleteListener(task -> userProfileRef.getDownloadUrl().addOnSuccessListener(this::updateUser));
    }

    private void updateUser(Uri downloadUri) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(binding.nameLet.getText())
                .setPhotoUri(downloadUri)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(task -> {
                    Toast.makeText(getContext(), "profile updated successfully", Toast.LENGTH_SHORT).show();
                    binding.progressBar.setVisibility(View.GONE);
                    binding.submitBtn.setClickable(true);
                    Navigation.findNavController(getActivity(), R.id.nav_host).navigate(RegistrationFragmentDirections.actionRegistrationFragmentToDiscoverMenu());
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "failed to update profile", Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_PICTURE_CODE && resultCode == RESULT_OK) {
            mImageUri = data.getData();
            binding.profileImage.setImageURI(mImageUri);
        }
    }
}
