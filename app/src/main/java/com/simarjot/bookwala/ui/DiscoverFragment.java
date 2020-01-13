package com.simarjot.bookwala.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.simarjot.bookwala.R;
import com.simarjot.bookwala.model.Book;

public class DiscoverFragment extends Fragment {
    FirebaseFirestore db;
    BookRecyclerAdapter adapter;
    private RecyclerView mBookRecyclerView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_discover, null);
        mBookRecyclerView = rootView.findViewById(R.id.book_recycler_view);
        db = FirebaseFirestore.getInstance();
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        mBookRecyclerView.setLayoutManager(layoutManager);

        Query query = db.collection("books");

        FirestoreRecyclerOptions<Book> response = new FirestoreRecyclerOptions.Builder<Book>()
                .setQuery(query, Book.class)
                .build();

        adapter = new BookRecyclerAdapter(response);
        mBookRecyclerView.setAdapter(adapter);
        return rootView;
    }


    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
