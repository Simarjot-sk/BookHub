package com.simarjot.bookwala.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.simarjot.bookwala.R;
import com.simarjot.bookwala.databinding.FragmentDiscoverBinding;
import com.simarjot.bookwala.model.Book;

public class DiscoverFragment extends Fragment {
    private BookRecyclerAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentDiscoverBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_discover, container, false);
        if(!isLoggedIn()){
            Activity homeActivity = getActivity();
            assert homeActivity != null;
            NavController controller = Navigation.findNavController(getActivity(), R.id.nav_host);
            controller.navigate(DiscoverFragmentDirections.actionDiscoverMenuToEnterPhoneNumberFragment());
        }
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        binding.bookRecyclerView.setLayoutManager(gridLayoutManager);

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

        adapter = new BookRecyclerAdapter(options, (AppCompatActivity) getActivity());
        binding.bookRecyclerView.setAdapter(adapter);
        return binding.getRoot();
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

    private boolean isLoggedIn(){
        return FirebaseAuth.getInstance().getCurrentUser()!=null;
    }
}
