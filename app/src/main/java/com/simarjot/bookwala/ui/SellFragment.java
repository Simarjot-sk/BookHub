package com.simarjot.bookwala.ui;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.simarjot.bookwala.CategorySelectorActivity;
import com.simarjot.bookwala.EnterPhoneNumberActivity;
import com.simarjot.bookwala.R;
import com.simarjot.bookwala.helpers.Helper;
import com.simarjot.bookwala.helpers.ImageAdderUtility;
import com.simarjot.bookwala.model.BookSharedPrefs;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class SellFragment extends Fragment {
    private static final int CAMERA_PERMISSION_CODE = 121;
    private static final int GALLERY_REQUEST_CODE = 420;
    private static final int CAMERA_REQUEST_CODE = 421;
    private ImageAdderUtility imageUtil;
    private SharedPreferences.Editor editor;

    //widgets
    private Button doneButton;
    private ImageButton addBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sell_new, null);
        imageUtil = new ImageAdderUtility(getContext(), view);
        SharedPreferences pref = getActivity().getSharedPreferences(BookSharedPrefs.SHARED_PREFS, Context.MODE_PRIVATE);
        editor = pref.edit();

        doneButton = view.findViewById(R.id.done_btn);
        addBtn = view.findViewById(R.id.add_images_btn);

        doneButton.setOnClickListener(v -> {
            ArrayList<String> imageUris = (ArrayList<String>) imageUtil.getImageUris();
            int coverImageIndex = imageUtil.getCoverImageIndex();

            if (imageUris.size() < 1) {
                Toast.makeText(getContext(), "Please Add Images of the book", Toast.LENGTH_SHORT).show();
                return;
            }
            editor.putStringSet(BookSharedPrefs.IMAGE_URIS, new HashSet<>(imageUris));
            editor.putString(BookSharedPrefs.COVER_IMAGE_URI, imageUris.get(coverImageIndex));
            editor.apply();
            Intent intent = new Intent(getContext(), CategorySelectorActivity.class);
            getActivity().startActivity(intent);
        });
        addBtn.setOnClickListener(v -> getImageFromCameraOrGallery());
        return view;
    }

    private void getImageFromCameraOrGallery() {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), android.R.style.Animation_Dialog));
        final CharSequence[] items = {"Take Photo From Camera", "Choose from Gallery", "Cancel"};
        builder.setItems(items, (dialog, which) -> {

            if (items[which].equals("Take Photo From Camera")) {
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
                } else {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(getContext(),
                            getContext().getApplicationContext().getPackageName() + ".provider", createImageFile()));
                    startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
                }

            } else if (items[which].equals("Choose from Gallery")) {
                if (Helper.isReadStoragePermissionGranted(getActivity()) &&
                        Helper.isReadStoragePermissionGranted(getActivity())) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    String[] mimeTypes = {"image/jpeg", "image/png"};
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
                    startActivityForResult(intent, GALLERY_REQUEST_CODE);
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case GALLERY_REQUEST_CODE:
                    Uri selectedImage = data.getData();
                    startCropper(selectedImage);
                    break;
                case UCrop.REQUEST_CROP:
                    Uri croppedImageUri = UCrop.getOutput(data);
                    imageUtil.addImage(croppedImageUri);
                    break;
                case CAMERA_REQUEST_CODE:
                    if (currentPhotoPath == null) {
                        Log.d("nerd", "null hai baba");
                    }
                    startCropper(Uri.fromFile(theFile));
                    break;
            }
        } else {
            if (data != null) {
                Throwable th = UCrop.getError(data);
                Log.d(EnterPhoneNumberActivity.TAG, "error occured while statiring activity for result " + requestCode, th);
            }
            Log.d("nerd", "result code not ok");
        }
    }

    private void startCropper(Uri selectedImage) {
        File file = Helper.getImageFile();
        Uri destinationUri = Uri.fromFile(file);

        UCrop.Options options = new UCrop.Options();
        options.setStatusBarColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
        options.setToolbarColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
        options.setFreeStyleCropEnabled(true);
        options.setToolbarWidgetColor(ContextCompat.getColor(getActivity(), R.color.white));
        options.setRootViewBackgroundColor(ContextCompat.getColor(getActivity(), R.color.dark_grey));
        options.setCropFrameColor(ContextCompat.getColor(getActivity(), R.color.dark_grey));
        options.setActiveWidgetColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
        options.setRootViewBackgroundColor(ContextCompat.getColor(getActivity(), R.color.black));
        options.setMaxBitmapSize(1000);
        options.setCompressionQuality(100);

        UCrop.of(selectedImage, destinationUri)
                .withMaxResultSize(1000, 1000)
                .withOptions(options)
                .start(getActivity().getApplicationContext(), getFragmentManager().findFragmentById(R.id.fragment_container));
    }

    private String currentPhotoPath;
    private File theFile;

    private File createImageFile() {
        // Create an image file name
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
        } catch (IOException ex) {
            Log.d("nerd", "io exception", ex);
        }
        currentPhotoPath = file.getAbsolutePath();
        theFile = file;
        return file;
    }
}
