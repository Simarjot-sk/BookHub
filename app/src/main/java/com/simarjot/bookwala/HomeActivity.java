package com.simarjot.bookwala;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.simarjot.bookwala.ui.ChatFragment;
import com.simarjot.bookwala.ui.DiscoverFragment;
import com.simarjot.bookwala.ui.SearchFragment;
import com.simarjot.bookwala.ui.SellFragment;
import com.simarjot.bookwala.ui.SettingsFragment;
import com.yalantis.ucrop.UCrop;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        loadFragment(new SellFragment());
        BottomNavigationView navigationView = findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        Fragment fragment = null;

        switch (menuItem.getItemId()){
            case R.id.discover:
                fragment = new DiscoverFragment();
                break;
            case R.id.search:
                fragment = new SearchFragment();
                break;
            case R.id.settings:
                fragment = new SettingsFragment();
                break;
            case R.id.chat:
                fragment = new ChatFragment();
                break;
            case R.id.sell:
                fragment = new SellFragment();
                break;
        }
        return loadFragment(fragment);
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}
