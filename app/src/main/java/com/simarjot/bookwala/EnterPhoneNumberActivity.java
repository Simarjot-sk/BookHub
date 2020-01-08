package com.simarjot.bookwala;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.hbb20.CountryCodePicker;

public class EnterPhoneNumberActivity extends AppCompatActivity {
    public static final int OTP_ACTIVITY = 451;
    public static String MOBILE_EXTRA = "mobile_extra";
    public static String MOBILE_EXTRA_FORMATTED = "mobile_formatted";
    public static String TAG = "nerd";
    private boolean isValid = false;

    //Widgets
    private CountryCodePicker ccp;
    private ImageView tickView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_phone_number);

        ImageButton nextButton = findViewById(R.id.button2);
        EditText mobileEditText = findViewById(R.id.mobile_edit_text);
        ccp = findViewById(R.id.ccp);
        tickView = findViewById(R.id.tick_mark);
        tickView.setVisibility(View.GONE);

        ccp.registerCarrierNumberEditText(mobileEditText);

        ccp.setPhoneNumberValidityChangeListener(isValidNumber -> {
            isValid = isValidNumber;
            if (isValid) {
                tickView.setVisibility(View.VISIBLE);
            } else {
                tickView.setVisibility(View.GONE);
            }
        });

        nextButton.setOnClickListener(v -> {
            if (isValid) {
                String mobileNo = ccp.getFullNumberWithPlus().trim();
                Intent otpIntent = new Intent(EnterPhoneNumberActivity.this, OtpActivity.class);
                otpIntent.putExtra(MOBILE_EXTRA, mobileNo);
                otpIntent.putExtra(MOBILE_EXTRA_FORMATTED, ccp.getFormattedFullNumber().trim());
                startActivityForResult(otpIntent, OTP_ACTIVITY);
            } else {
                Toast.makeText(this, "Please Enter a Valid Mobile Number", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OTP_ACTIVITY) {
            if (resultCode == RESULT_OK) {
                setResult(RESULT_OK);
                finish();
            }
        }
    }
}
