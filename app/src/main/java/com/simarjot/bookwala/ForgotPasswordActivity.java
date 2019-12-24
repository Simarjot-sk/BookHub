package com.simarjot.bookwala;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import static com.simarjot.bookwala.MainActivity.isValidEmail;

public class ForgotPasswordActivity extends AppCompatActivity {
    private EditText emailEditText;
    private ImageButton nextButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        emailEditText = findViewById(R.id.email_edit_text);
        nextButton = findViewById(R.id.button);
    }
    
    
    public void onClickNext(View view){
        String emailString = emailEditText.getText().toString();
        if(isValidEmail(emailString)){
            Toast.makeText(this, "Password change link has been sent to your email address", Toast.LENGTH_SHORT).show();
        }else{
            emailEditText.setError("Enter a valid Email Address");
        }
    }

    private void addTextWatcherToEmailEditText(){
        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String currentText = s.toString();
                if(isValidEmail(currentText)){
                    nextButton.setVisibility(View.VISIBLE);
                }else {
                    nextButton.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
}
