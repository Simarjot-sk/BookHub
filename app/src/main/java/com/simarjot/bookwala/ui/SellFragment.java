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
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
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
import androidx.fragment.app.Fragment;

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
import java.util.Arrays;
import java.util.UUID;

public class SellFragment extends Fragment {
    private static final int CAMERA_PERMISSION_CODE=121;
    private static final int GALLERY_REQUEST_CODE=420;
    private static final int CAMERA_REQUEST_CODE=421;
    private Button uploadBtn;
    private ImageAdderUtility imageUtil;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sell, null);
        imageUtil = new ImageAdderUtility(Arrays.asList(
                (ImageView) view.findViewById(R.id.book_image_1),
                (ImageView) view.findViewById(R.id.book_image_2),
                (ImageView) view.findViewById(R.id.book_image_3),
                (ImageView) view.findViewById(R.id.book_image_4))
        );

        uploadBtn = view.findViewById(R.id.image_upload_button);
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), android.R.style.Animation_Dialog));
                final CharSequence[] items = {"Take Photo", "Choose from Gallery", "Cancel"};
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if(items[which].equals("Take Photo")){
                            if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                                requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
                            }else{
                                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
                            }

                        }else if(items[which].equals("Choose from Gallery")){

                            if(Helper.isReadStoragePermissionGranted(getActivity()) &&
                                    Helper.isReadStoragePermissionGranted(getActivity())){
                                Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
                                intent.setType("image/*");
                                String[] mimeTypes = {"image/jpeg", "image/png"};
                                intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
                                startActivityForResult(intent,GALLERY_REQUEST_CODE);
                            }
                        }
                    }
                });
                builder.show();
            }
        });

        return view;
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
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                   // bookImage.setImageBitmap(imageBitmap);
                    break;
            }
        }else{
            Log.d(MainActivity.TAG, "error occured while statiring activity for result");
        }
    }

    public void uploadImageToFirebase(){
        String postId = UUID.randomUUID().toString();

        FirebaseStorage storage = FirebaseStorage.getInstance();

        StorageReference storageRef = storage.getReference();

        StorageReference booksRef = storageRef.child("images/books");

        StorageReference recent = booksRef.child(postId + ".jpg");

      //  Bitmap bitmap = ((BitmapDrawable) bookImage.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        StorageMetadata metadata = new StorageMetadata.Builder()
                .setCustomMetadata("post_id", postId)
                .setContentType("image/jpg").build();

        UploadTask uploadTask = recent.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d("nerd", "TaSk FaileD SuCceSsfuLly");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Log.d("nerd", "uploaded successfully");
                Toast.makeText(getActivity(), "file uploaded successfully", Toast.LENGTH_LONG).show();
            }
        });
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
}
