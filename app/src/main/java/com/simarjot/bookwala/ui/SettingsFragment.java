package com.simarjot.bookwala.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.simarjot.bookwala.PhoneFragment;
import com.simarjot.bookwala.R;

public class SettingsFragment extends Fragment {

    private Button logoutBtn;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_settings, null);
        logoutBtn = view.findViewById(R.id.logout_btn);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(getActivity(), "logged out successfully", Toast.LENGTH_SHORT).show();
                Intent mainActivityIntent = new Intent(getActivity(), PhoneFragment.class);
                startActivity(mainActivityIntent);
            }
        });

        return view;
    }
}
