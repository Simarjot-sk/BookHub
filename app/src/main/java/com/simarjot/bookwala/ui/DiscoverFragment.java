package com.simarjot.bookwala.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.paging.FirestorePagingOptions;
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

        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPrefetchDistance(5)
                .setPageSize(10)
                .build();

        FirestorePagingOptions<Book> options = new FirestorePagingOptions.Builder<Book>()
                .setLifecycleOwner(this)
                .setQuery(query, config, Book.class)
                .build();

        adapter = new BookRecyclerAdapter(options);
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
