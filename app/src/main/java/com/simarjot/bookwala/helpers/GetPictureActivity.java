package com.simarjot.bookwala.helpers;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.simarjot.bookwala.R;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class GetPictureActivity extends AppCompatActivity {
    private static final int GALLERY_REQUEST_CODE = 420;
    private static final int CAMERA_REQUEST_CODE = 421;
    private static final int WRITE_PERMISSION_CODE = 11;
    private static final int CAMERA_PERMISSION_CODE = 12;
    public static final String CIRCULAR_DIMMED_LAYER = "ASLDKFJ";
    private boolean circularDimmedLayer;
    private String mImageFilePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_picture);
        circularDimmedLayer = getIntent().getBooleanExtra(CIRCULAR_DIMMED_LAYER, false);
        getImageFromCameraOrGallery();
    }

    private void getImageFromCameraOrGallery() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final CharSequence[] items = {"Take Photo From Camera", "Choose from Gallery", "Cancel"};
        builder.setItems(items, (dialog, which) -> {
            if (items[which].equals("Take Photo From Camera")) {
                dispatchCameraIntent();
            } else if (items[which].equals("Choose from Gallery")) {
                dispatchGalleryIntent();
            }
        }).setOnCancelListener(dialog -> {
            setResult(RESULT_CANCELED);
            finish();
        });
        builder.show();
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
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
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
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    Log.e(Helper.TAG, "could not create file");
                }

                String packageName = getApplicationContext().getPackageName();
                if (photoFile != null) {
                    Uri photoUri = FileProvider.getUriForFile(this,
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
            if (this.checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
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
        File storageDir = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
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
            options.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
            options.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary));
            options.setFreeStyleCropEnabled(true);
            options.setToolbarWidgetColor(ContextCompat.getColor(this, R.color.white));
            options.setRootViewBackgroundColor(ContextCompat.getColor(this, R.color.dark_grey));
            options.setCropFrameColor(ContextCompat.getColor(this, R.color.dark_grey));
            options.setActiveWidgetColor(ContextCompat.getColor(this, R.color.colorPrimary));
            options.setRootViewBackgroundColor(ContextCompat.getColor(this, R.color.black));
            options.setMaxBitmapSize(1000);
            options.withAspectRatio(2, 3);
            options.setCompressionQuality(100);
            options.setCircleDimmedLayer(circularDimmedLayer);

            UCrop.of(selectedImage, destinationUri)
                    .withMaxResultSize(1000, 1000)
                    .withOptions(options)
                    .start(this);
        } catch (IOException ex) {
            Log.e(Helper.TAG, "error while creating destination uri", ex);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(Helper.TAG, "get picture on result called" + requestCode + ", " + resultCode);
        Toast.makeText(this, "get picture on result called", Toast.LENGTH_SHORT).show();
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case GALLERY_REQUEST_CODE:
                    Uri selectedImage = data.getData();
                    startCropper(selectedImage);
                    break;
                case CAMERA_REQUEST_CODE:
                    startCropper(Uri.fromFile(new File(mImageFilePath)));
                    break;
                case UCrop.REQUEST_CROP:
                    Uri croppedImageUri = UCrop.getOutput(data);
                    setResult(RESULT_OK, new Intent().setData(croppedImageUri));
                    finish();
                    break;
            }
        } else {
            if (data != null) {
                Throwable th = UCrop.getError(data);
                Log.d(Helper.TAG, "error occurred while starting activity for result " + requestCode, th);
            }
            Log.d("nerd", "result code not ok");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Toast.makeText(this, "on request permissions called", Toast.LENGTH_SHORT).show();
        for (int i = 0; i < permissions.length; i++) {
            String permissionName = permissions[i];
            int grantCode = grantResults[i];
            if (permissionName.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                if (grantCode == PackageManager.PERMISSION_GRANTED) {
                    dispatchGalleryIntent();
                } else if (grantCode == PackageManager.PERMISSION_DENIED) {
                    Toast.makeText(this, "Cannot perform operation without required permissions", Toast.LENGTH_LONG).show();
                }
            } else if (permissionName.equals(Manifest.permission.CAMERA)) {
                if (grantCode == PackageManager.PERMISSION_GRANTED) {
                    dispatchCameraIntent();
                } else if (grantCode == PackageManager.PERMISSION_DENIED) {
                    Toast.makeText(this, "Cannot perform operation without required permissions", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

}
