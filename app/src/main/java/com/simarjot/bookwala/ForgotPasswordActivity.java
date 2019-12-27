package com.simarjot.bookwala;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.simarjot.bookwala.helpers.EmailHelper;


public class ForgotPasswordActivity extends AppCompatActivity {
    private EditText emailEditText;
    private ImageButton nextButton;
    private ImageView backView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        emailEditText = findViewById(R.id.email_edit_text);
        nextButton = findViewById(R.id.button2);
        backView = findViewById(R.id.back_button);

        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    
    
    public void onClickNext(View view){
        String emailString = emailEditText.getText().toString();
        if(EmailHelper.isValidEmail(emailString)){
            Toast.makeText(this, "Password change link has been sent to your email address", Toast.LENGTH_SHORT).show();
        }else{
            emailEditText.setError("Enter a valid Email Address");
        }
    }
}
