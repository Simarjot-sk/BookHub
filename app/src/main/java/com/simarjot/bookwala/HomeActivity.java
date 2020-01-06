package com.simarjot.bookwala;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.simarjot.bookwala.ui.ChatFragment;
import com.simarjot.bookwala.ui.DiscoverFragment;
import com.simarjot.bookwala.ui.SearchFragment;
import com.simarjot.bookwala.ui.SellFragment;
import com.simarjot.bookwala.ui.SettingsFragment;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private DiscoverFragment discoverFragment;
    private SellFragment sellFragment;
    private SettingsFragment settingsFragment;
    private ChatFragment chatFragment;
    private SearchFragment searchFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        discoverFragment = new DiscoverFragment();
        sellFragment = new SellFragment();
        searchFragment = new SearchFragment();
        settingsFragment = new SettingsFragment();
        chatFragment = new ChatFragment();

        loadFragment(sellFragment);
        BottomNavigationView navigationView = findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        Fragment fragment = null;

        switch (menuItem.getItemId()){
            case R.id.discover:
                fragment = discoverFragment;
                break;
            case R.id.search:
                fragment = searchFragment;
                break;
            case R.id.settings:
                fragment = settingsFragment;
                break;
            case R.id.chat:
                fragment = chatFragment;
                break;
            case R.id.sell:
                fragment = sellFragment;
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
