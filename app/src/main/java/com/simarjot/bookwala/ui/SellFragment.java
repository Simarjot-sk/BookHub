package com.simarjot.bookwala.ui;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.simarjot.bookwala.MainActivity;
import com.simarjot.bookwala.R;
import com.simarjot.bookwala.helpers.Helper;
import com.simarjot.bookwala.helpers.ImageAdderUtility;
import com.yalantis.ucrop.UCrop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class SellFragment extends Fragment {
    private static final int CAMERA_PERMISSION_CODE=121;
    private static final int GALLERY_REQUEST_CODE=420;
    private static final int CAMERA_REQUEST_CODE=421;
    private ImageAdderUtility imageUtil;

    //widgets
    private Button uploadButton;
    private ImageView minus1;
    private ImageView minus2;
    private ImageView minus3;
    private ImageView minus4;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sell, null);
        uploadButton = view.findViewById(R.id.image_upload_button);

        minus1 = view.findViewById(R.id.minus_1);
        minus2 = view.findViewById(R.id.minus_2);
        minus3 = view.findViewById(R.id.minus_3);
        minus4 = view.findViewById(R.id.minus_4);
        minus1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Remove Image", Toast.LENGTH_SHORT).show();
            }
        });

        minus2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Remove Image", Toast.LENGTH_SHORT).show();
            }
        });

        minus3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Remove Image", Toast.LENGTH_SHORT).show();
            }
        });

        minus4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Remove Image", Toast.LENGTH_SHORT).show();
            }
        });
        ImageView bookView1 = view.findViewById(R.id.book_image_1);
        ImageView bookView2 = view.findViewById(R.id.book_image_2);
        ImageView bookView3 = view.findViewById(R.id.book_image_3);
        ImageView bookView4 = view.findViewById(R.id.book_image_4);

        imageUtil = new ImageAdderUtility(Arrays.asList(bookView1, bookView2, bookView3, bookView4), Arrays.asList(minus1, minus2, minus3, minus4));
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImageToFirebase();
            }
        });

        bookView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImageFromCameraOrGallery();
            }
        });
        bookView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImageFromCameraOrGallery();
            }
        });
        bookView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImageFromCameraOrGallery();
            }
        });
        bookView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImageFromCameraOrGallery();
            }
        });

        return view;
    }

    private void getImageFromCameraOrGallery(){
            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), android.R.style.Animation_Dialog));
            final CharSequence[] items = {"Take Photo From Camera", "Choose from Gallery", "Cancel"};
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    if(items[which].equals("Take Photo From Camera")){
                        if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                            requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
                        }else{
                            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(getContext(),
                                    getContext().getApplicationContext().getPackageName() + ".provider", createImageFile()));
                            startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
                        }

                    }else if(items[which].equals("Choose from Gallery")){
                        if(Helper.isReadStoragePermissionGranted(getActivity()) &&
                                Helper.isReadStoragePermissionGranted(getActivity())){
                            Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
                            intent.setType("image/*");
                            String[] mimeTypes = {"image/jpeg", "image/png"};
                            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
                            startActivityForResult(intent,GALLERY_REQUEST_CODE);
                        }
                    }
                }
            });
            builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            switch (requestCode){
                case GALLERY_REQUEST_CODE:
                    Uri selectedImage = data.getData();
                    startCropper(selectedImage);
                    break;
                case UCrop.REQUEST_CROP:
                    Uri croppedImageUri = UCrop.getOutput(data);
                    imageUtil.addImage(croppedImageUri);
                   // uploadImageToFirebase();
                    break;
                case CAMERA_REQUEST_CODE:
                    if(currentPhotoPath==null){
                        Log.d("nerd" , "null hai baba");
                    }
                    startCropper(Uri.fromFile(theFile));
                    break;
            }
        }else{
            Throwable th = UCrop.getError(data);
            Log.d(MainActivity.TAG, "error occured while statiring activity for result " + requestCode, th);
        }
    }

    private void uploadImageToFirebase(){
        
        if(imageUtil.getCurrentIndex()<3) return;

        String postId = UUID.randomUUID().toString();

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference booksRef = storageRef.child("images/books/" + postId);

        int i=0;
        List<Uri> bookImageUris = imageUtil.getBookImageUris();
        for(Uri uri:bookImageUris){
            StorageReference imageBunch = booksRef.child(postId + "__" + (++i) +".jpg");
            StorageMetadata metadata = new StorageMetadata.Builder()
                    .setCustomMetadata("createdAt", new Date().toString()).build();
            imageBunch.updateMetadata(metadata);
            imageBunch.putFile(uri).addOnCanceledListener(new OnCanceledListener() {
                @Override
                public void onCanceled() {
                    Toast.makeText(getContext(), "Image upload Cancelled", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "Image upload Failed", Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getActivity(), "image uploaded successfully", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void startCropper(Uri selectedImage){
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
        options.setMaxBitmapSize(10000);
        options.setCompressionQuality(100);

        UCrop.of(selectedImage,destinationUri)
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
        }catch (IOException ex){
            Log.d("nerd", "io exception", ex);
        }
    currentPhotoPath = file.getAbsolutePath();
        theFile = file;
        return file;
    }
}
