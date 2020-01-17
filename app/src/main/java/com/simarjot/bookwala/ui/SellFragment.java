package com.simarjot.bookwala.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.simarjot.bookwala.CategorySelectorActivity;
import com.simarjot.bookwala.R;
import com.simarjot.bookwala.helpers.GetPictureActivity;
import com.simarjot.bookwala.helpers.ImageAdderUtility;
import com.simarjot.bookwala.model.BookSharedPrefs;

import java.util.ArrayList;
import java.util.HashSet;

public class SellFragment extends Fragment {
    private ImageAdderUtility imageUtil;
    private SharedPreferences.Editor editor;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sell_new, null);
        imageUtil = new ImageAdderUtility(getContext(), view, 5);
        SharedPreferences pref = getActivity().getSharedPreferences(BookSharedPrefs.SHARED_PREFS, Context.MODE_PRIVATE);
        editor = pref.edit();

        Button doneButton = view.findViewById(R.id.done_btn);
        ImageButton addBtn = view.findViewById(R.id.add_images_btn);

        doneButton.setOnClickListener(v -> {
            ArrayList<String> imageUris = (ArrayList<String>) imageUtil.getImageUris();
            int coverImageIndex = imageUtil.getCoverImageIndex();

            if (imageUris.size() < 1) {
                Toast.makeText(getContext(), "Please Add Images of the book", Toast.LENGTH_SHORT).show();
                return;
            }
            editor.putStringSet(BookSharedPrefs.IMAGE_URIS, new HashSet<>(imageUris));
            editor.putString(BookSharedPrefs.COVER_IMAGE_URI, imageUris.get(coverImageIndex));
            editor.apply();
            Intent intent = new Intent(getContext(), CategorySelectorActivity.class);
            getActivity().startActivity(intent);
        });

        addBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), GetPictureActivity.class);
            startActivityForResult(intent, 22);
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 22) {
            imageUtil.addImage(data.getData());
        } else {
            Log.d("nerd", "result code not ok");
        }
    }
}
