package com.simarjot.bookwala;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.simarjot.bookwala.databinding.ActivityHomeBinding;
import com.simarjot.bookwala.ui.AllChatsFragment;
import com.simarjot.bookwala.ui.DiscoverFragment;
import com.simarjot.bookwala.ui.SearchFragment;
import com.simarjot.bookwala.ui.SellFragment;
import com.simarjot.bookwala.ui.SettingsFragment;

public class HomeActivity extends AppCompatActivity {
    private NavController mNavController;
    ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);

        if (isNoUserLoggedIn()) {
           Intent loginIntent = new Intent(this, LoginActivity.class);
           startActivity(loginIntent);
        }

        mNavController = Navigation.findNavController(this, R.id.nav_host);
        NavigationUI.setupWithNavController(binding.bottomNavigation, mNavController);
    }

    private boolean isNoUserLoggedIn() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return user == null;
    }
}
