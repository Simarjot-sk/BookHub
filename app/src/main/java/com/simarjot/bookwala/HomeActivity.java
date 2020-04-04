package com.simarjot.bookwala;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.simarjot.bookwala.databinding.ActivityHomeBinding;

public class HomeActivity extends AppCompatActivity {
    private AppBarConfiguration mAppBarConfiguration;
    private NavController mNavController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //used for stopping showing splash screen
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        ActivityHomeBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_home);

        mNavController = Navigation.findNavController(this, R.id.nav_host);
        mAppBarConfiguration = new AppBarConfiguration.Builder(mNavController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, mNavController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(binding.bottomNavigation, mNavController);

        mNavController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            switch (destination.getId()) {
                case R.id.enterPhoneNumberFragment:
                case R.id.otpFragment:
                case R.id.registrationFragment:
                    binding.bottomNavigation.setVisibility(View.GONE);
                    break;
                default:
                    binding.bottomNavigation.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        //if back is pressed from the last destination in back-stack then finish the activity
        if (!mNavController.popBackStack()) {
            finish();
        }
        return NavigationUI.navigateUp(mNavController, mAppBarConfiguration);
    }
}
