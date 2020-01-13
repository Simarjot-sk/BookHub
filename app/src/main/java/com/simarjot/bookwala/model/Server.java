package com.simarjot.bookwala.model;

import android.net.Uri;
import android.os.Handler;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.simarjot.bookwala.helpers.Helper;

import java.util.ArrayList;
import java.util.List;

public class Server {
    private Book book;
    private OnUploadFinishedListener uploadFinishedListener;

    public Server(Book book) {
        this.book = book;
    }

    public void setUploadFinishedListener(OnUploadFinishedListener uploadFinishedListener) {
        this.uploadFinishedListener = uploadFinishedListener;
    }

    public Server uploadBookWithImages(List<String> imageUris, String coverImageUri, String bookId) {
        List<Uri> imageDownloadUris = new ArrayList<>();
        List<Uri> coverDownloadUri = new ArrayList<>();

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference booksRef = storageRef.child("images/books/" + bookId);

        imageUris.remove(coverImageUri);

        int i = 0;
        for (String uri : imageUris) {
            StorageReference imageBunchRef = booksRef.child(bookId + "__" + (++i) + ".jpg");
            imageBunchRef.putFile(Uri.parse(uri))
                    .addOnCanceledListener(() -> Log.d(Helper.TAG, "image upload Cancelled"))
                    .addOnFailureListener(e -> Log.d(Helper.TAG, "image upload failed", e))
                    .addOnSuccessListener(taskSnapshot -> {
                        imageBunchRef.getDownloadUrl().addOnSuccessListener(uri1 -> {
                            Log.d(Helper.TAG, "image uploaded successfully-> " + uri1.toString());
                            imageDownloadUris.add(uri1);
                        });
                    });
        }

        StorageReference coverStorageRef = booksRef.child("cover.jpg");
        coverStorageRef.putFile(Uri.parse(coverImageUri))
                .addOnCanceledListener(() -> Log.d(Helper.TAG, "cover upload Cancelled"))
                .addOnFailureListener(e -> Log.d(Helper.TAG, "cover upload Failed"))
                .addOnSuccessListener(taskSnapshot -> {
                    coverStorageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        Log.d(Helper.TAG, "cover uploaded successfully-> " + uri.toString());
                        coverDownloadUri.add(uri);
                    });
                });
        Handler handler = new Handler();
        handler.post(new Runnable() {
                         @Override
                         public void run() {
                             if (imageDownloadUris.size() == imageUris.size() && coverDownloadUri.size() == 1) {
                                 Log.d(Helper.TAG, "all images uploaded");
                                 for (Uri u : imageDownloadUris) {
                                     Log.d(Helper.TAG, u.toString());
                                 }
                                 Log.d(Helper.TAG, coverDownloadUri.get(0).toString());
                                 uploadBook(coverDownloadUri.get(0), imageDownloadUris);
                                 return;
                             }
                             handler.postDelayed(this, 100);
                         }
                     }
        );
        return this;
    }

    private void uploadBook(Uri coverDownloadUri, List<Uri> imageDownloadUris) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        book.setCoverDownloadUri(coverDownloadUri.toString());
        List<String> imageUriString = new ArrayList<>();
        for (Uri u : imageDownloadUris) {
            imageUriString.add(u.toString());
        }
        book.setImageDownloadUris(imageUriString);
        firestore.collection("books").add(book)
                .addOnFailureListener(command -> uploadFinishedListener.onUploadFailed())
                .addOnSuccessListener(command -> uploadFinishedListener.onUploadFinish());
    }

    public interface OnUploadFinishedListener {
        void onUploadFinish();
        void onUploadFailed();
    }
}
