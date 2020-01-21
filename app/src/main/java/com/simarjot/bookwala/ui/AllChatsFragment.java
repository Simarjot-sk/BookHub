package com.simarjot.bookwala.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.simarjot.bookwala.MessagingActivity;
import com.simarjot.bookwala.R;
import com.simarjot.bookwala.model.chat.AllChatsAdapter;
import com.simarjot.bookwala.model.chat.Chat;

public class AllChatsFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chat, null);
        if (getArguments() != null) {
            Intent messagingIntent = new Intent(getContext(), MessagingActivity.class);
            messagingIntent.putExtra(
                    MessagingActivity.OTHER_USER_UID,
                    getArguments().getString(MessagingActivity.OTHER_USER_UID));
            Log.d("user uid", getArguments().getString(MessagingActivity.OTHER_USER_UID));
            getActivity().startActivity(messagingIntent);
        } else {
            Query query = FirebaseFirestore.getInstance()
                    .collection("chats")
                    .whereArrayContains("participants", FirebaseAuth.getInstance().getCurrentUser().getUid());

            FirestoreRecyclerOptions<Chat> options = new FirestoreRecyclerOptions.Builder<Chat>()
                    .setQuery(query, Chat.class)
                    .setLifecycleOwner(this)
                    .build();

            RecyclerView allChatsList = rootView.findViewById(R.id.all_chats);
            allChatsList.setLayoutManager(new LinearLayoutManager(getActivity()));
            allChatsList.setAdapter(new AllChatsAdapter(options, getActivity()));
        }
        return rootView;
    }
}
