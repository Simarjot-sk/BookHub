package com.simarjot.bookwala;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.simarjot.bookwala.ui.ChatFragment;
import com.simarjot.bookwala.ui.DiscoverFragment;
import com.simarjot.bookwala.ui.SearchFragment;
import com.simarjot.bookwala.ui.SellFragment;
import com.simarjot.bookwala.ui.SettingsFragment;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    public static final int ENTER_PHONE_FOR_LOGIN = 112;
    private DiscoverFragment discoverFragment;
    private SellFragment sellFragment;
    private SettingsFragment settingsFragment;
    private ChatFragment chatFragment;
    private SearchFragment searchFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if (isNoUserLoggedIn()) {
            Intent loginIntent = new Intent(HomeActivity.this, EnterPhoneNumberActivity.class);
            startActivityForResult(loginIntent, ENTER_PHONE_FOR_LOGIN);
        }

        discoverFragment = new DiscoverFragment();
        sellFragment = new SellFragment();
        searchFragment = new SearchFragment();
        settingsFragment = new SettingsFragment();
        chatFragment = new ChatFragment();

        loadFragment(sellFragment);
        BottomNavigationView navigationView = findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(this);
    }

    private boolean isNoUserLoggedIn() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return user == null;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        Fragment fragment = null;

        switch (menuItem.getItemId()) {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ENTER_PHONE_FOR_LOGIN) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Logged in successfully", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
