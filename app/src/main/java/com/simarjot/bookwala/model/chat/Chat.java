package com.simarjot.bookwala.model.chat;

import android.util.Log;

import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.simarjot.bookwala.helpers.Helper;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Chat {
    private String mCurrentUser;
    private String mOtherUser;
    private String mChatId;
    private DocumentReference mCurrentChatRef;

    public Chat() {
    }

    public Chat(String currentUser, String otherUser) {
        mCurrentUser = currentUser;
        mOtherUser = otherUser;
        mChatId = mCurrentUser + "-" + mOtherUser;
        mCurrentChatRef = FirebaseFirestore.getInstance().collection("chats").document(mChatId);
        mCurrentChatRef.get().addOnSuccessListener(documentSnapshot -> {
            if (!documentSnapshot.exists()) {
                mCurrentChatRef = FirebaseFirestore.getInstance().collection("chats").document(mOtherUser + "-" + mCurrentUser);
                mCurrentChatRef.get().addOnSuccessListener(documentSnapshotRev -> {
                    if (!documentSnapshotRev.exists()) {
                        Map<String, Object> chatObject = new HashMap<>();
                        chatObject.put("participants", Arrays.asList(mCurrentUser, mOtherUser));
                        mCurrentChatRef.set(chatObject);
                    } else {
                        mChatId = mOtherUser + "-" + mCurrentUser;
                    }
                });
            }
        });
    }

    public void sendMessage(Message message, OnSuccessListener<DocumentReference> listener) {
        Log.d(Helper.TAG, mCurrentChatRef.toString());
        mCurrentChatRef.collection("messages").add(message)
                .addOnFailureListener(command -> Log.d(Helper.TAG, command.getMessage()))
                .addOnSuccessListener(listener);
    }

    public List<String> getParticipants() {
        return Arrays.asList(mCurrentUser, mOtherUser);
    }

    //only called by firestore while crating Chat Object from data snapshot
    public void setParticipants(List<String> participants) {
        if (participants.get(0).equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            mCurrentUser = participants.get(0);
            mOtherUser = participants.get(1);
        } else {
            mCurrentUser = participants.get(1);
            mOtherUser = participants.get(0);
        }
    }

}
