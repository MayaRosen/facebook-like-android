package com.example.foobar_android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

public class LoginActivity extends AppCompatActivity {
    private EditText etUsername;
    private EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etUsername = findViewById(R.id.etusername);
        etPassword = findViewById(R.id.etpassword);

        Button btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                // Call the login API method here
                loginUser(username, password);
            }
        });

        TextView tvRegister = findViewById(R.id.tvRegister);
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the RegisterActivity
               startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

    private void loginUser(String username, String password) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        // Retrieve saved user data from SharedPreferences
        String savedUsername = sharedPreferences.getString("username", "");
        String savedPassword = sharedPreferences.getString("password", "");

        // Check if the entered username and password match the saved values
        if (!TextUtils.isEmpty(username) && username.equals(savedUsername) && password.equals(savedPassword)) {
            // Authentication successful, navigate to the chat activity or perform necessary actions
            navigateToChatActivity(username);
        } else {
            View rootView = getWindow().getDecorView().findViewById(android.R.id.content);
            Snackbar.make(rootView, "Invalid username or password. Please try again.", Snackbar.LENGTH_SHORT).show();

        }
    }

    private void navigateToChatActivity(String username) {
        Intent intent = new Intent(LoginActivity.this, HomePageActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
        finish();
    }
}
