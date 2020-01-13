package com.simarjot.bookwala.ui;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;

public class SellFragment extends Fragment {
    private static final int GALLERY_REQUEST_CODE = 420;
    private static final int CAMERA_REQUEST_CODE = 421;
    private static final int WRITE_PERMISSION_CODE = 11;
    private static final int CAMERA_PERMISSION_CODE = 12;
    private ImageAdderUtility imageUtil;
    private SharedPreferences.Editor editor;
    private Context mContext;
    private String mImageFilePath;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sell_new, null);
        mContext = getContext();
        imageUtil = new ImageAdderUtility(getContext(), view);
        SharedPreferences pref = getActivity().getSharedPreferences(BookSharedPrefs.SHARED_PREFS, Context.MODE_PRIVATE);
        editor = pref.edit();

        Button doneButton = view.findViewById(R.id.done_btn);
        ImageButton addBtn = view.findViewById(R.id.add_images_btn);

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
                dispatchCameraIntent();
            } else if (items[which].equals("Choose from Gallery")) {
                dispatchGalleryIntent();
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
                    startCropper(Uri.fromFile(new File(mImageFilePath)));
                    break;
            }
        } else {
            if (data != null) {
                Throwable th = UCrop.getError(data);
                Log.d(EnterPhoneNumberActivity.TAG, "error occurred while starting activity for result " + requestCode, th);
            }
            Log.d("nerd", "result code not ok");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Toast.makeText(mContext, "on request permissions called", Toast.LENGTH_SHORT).show();
        for (int i = 0; i < permissions.length; i++) {
            String permissionName = permissions[i];
            int grantCode = grantResults[i];
            if (permissionName.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                if (grantCode == PackageManager.PERMISSION_GRANTED) {
                    dispatchGalleryIntent();
                } else if (grantCode == PackageManager.PERMISSION_DENIED) {
                    Toast.makeText(mContext, "Cannot perform operation without required permissions", Toast.LENGTH_LONG).show();
                }
            } else if (permissionName.equals(Manifest.permission.CAMERA)) {
                if (grantCode == PackageManager.PERMISSION_GRANTED) {
                    dispatchCameraIntent();
                } else if (grantCode == PackageManager.PERMISSION_DENIED) {
                    Toast.makeText(mContext, "Cannot perform operation without required permissions", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void dispatchGalleryIntent() {
        if (getWritePermission()) {
            Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
            galleryIntent.setType("image/*");
            String[] mimeTypes = {"image/jpeg", "image/png"};
            galleryIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            galleryIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
        }
    }

    private boolean getWritePermission() {
        if (Build.VERSION.SDK_INT >= 23) {//permission is automatically granted on sdk<23 upon installation
            if (mContext.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        WRITE_PERMISSION_CODE);
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    private void dispatchCameraIntent() {
        if (getCameraPermission()) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(mContext.getPackageManager()) != null) {
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    Log.e(Helper.TAG, "could not create file");
                }

                String packageName = mContext.getApplicationContext().getPackageName();
                if (photoFile != null) {
                    Uri photoUri = FileProvider.getUriForFile(mContext,
                            packageName + ".provider",
                            photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                    startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
                }
            }
        }
    }

    private boolean getCameraPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (mContext.checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        storageDir.mkdirs();
        File file = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        mImageFilePath = file.getAbsolutePath();
        return file;
    }

    private void startCropper(Uri selectedImage) {
        try {
            Uri destinationUri = Uri.fromFile(createImageFile());
            UCrop.Options options = new UCrop.Options();
            options.setStatusBarColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
            options.setToolbarColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
            options.setFreeStyleCropEnabled(true);
            options.setToolbarWidgetColor(ContextCompat.getColor(mContext, R.color.white));
            options.setRootViewBackgroundColor(ContextCompat.getColor(mContext, R.color.dark_grey));
            options.setCropFrameColor(ContextCompat.getColor(mContext, R.color.dark_grey));
            options.setActiveWidgetColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
            options.setRootViewBackgroundColor(ContextCompat.getColor(mContext, R.color.black));
            options.setMaxBitmapSize(1000);
            options.setCompressionQuality(100);

            UCrop.of(selectedImage, destinationUri)
                    .withMaxResultSize(1000, 1000)
                    .withOptions(options)
                    .start(mContext, getFragmentManager().findFragmentById(R.id.fragment_container));
        } catch (IOException ex) {
            Log.e(Helper.TAG, "error while creating destination uri", ex);
        }
    }
}
