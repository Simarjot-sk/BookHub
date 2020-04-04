package com.simarjot.bookwala.ui;

import android.os.Bundle;
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
import com.simarjot.bookwala.R;
import com.simarjot.bookwala.model.chat.AllChatsAdapter;
import com.simarjot.bookwala.model.chat.Chat;

public class AllChatsFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chat, container, false);

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

        return rootView;
    }
}
