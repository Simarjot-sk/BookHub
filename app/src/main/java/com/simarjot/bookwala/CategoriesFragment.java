package com.simarjot.bookwala;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.simarjot.bookwala.databinding.FragmentCategoriesBinding;
import com.simarjot.bookwala.model.BookSharedPrefs;
import com.simarjot.bookwala.model.Category;

public class CategoriesFragment extends Fragment {
    private Category category;
    FragmentCategoriesBinding mBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_categories, container, false);

        removeBorders();

        mBinding.schoolIv.setOnClickListener(v -> {
            removeBorders();
            category = Category.SCHOOL;
            mBinding.schoolBg.setVisibility(View.VISIBLE);
        });

        mBinding.collegeIv.setOnClickListener(v -> {
            removeBorders();
            category = Category.COLLEGE;
            mBinding.collegeBg.setVisibility(View.VISIBLE);
        });

        mBinding.competitiveIv.setOnClickListener(v -> {
            removeBorders();
            category = Category.COMPETITIVE;
            mBinding.competitiveBg.setVisibility(View.VISIBLE);
        });

        mBinding.othersIv.setOnClickListener(v -> {
            removeBorders();
            category = Category.OTHERS;
            mBinding.othersBg.setVisibility(View.VISIBLE);
        });

        mBinding.doneBtn.setOnClickListener(v -> {
            Log.d("nerd", "category: " + category.name());
            if (category != null) {
                SharedPreferences.Editor editor = getActivity().getSharedPreferences(BookSharedPrefs.SHARED_PREFS, Context.MODE_PRIVATE).edit();
                editor.putString(BookSharedPrefs.CATEGORY, category.name());
                editor.apply();
                Navigation.findNavController(v).navigate(CategoriesFragmentDirections.actionCategoriesFragmentToDetailsFragment());
            } else {
                Toast.makeText(getContext(), "Please select a category", Toast.LENGTH_SHORT).show();
            }
        });
        return mBinding.getRoot();
    }

    private void removeBorders() {
        mBinding.schoolBg.setVisibility(View.INVISIBLE);
        mBinding.collegeBg.setVisibility(View.INVISIBLE);
        mBinding.collegeBg.setVisibility(View.INVISIBLE);
        mBinding.othersBg.setVisibility(View.INVISIBLE);
    }
}
