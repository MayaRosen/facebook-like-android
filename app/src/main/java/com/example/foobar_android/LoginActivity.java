package com.example.foobar_android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.foobar_android.ReqRes.LoginRequest;
import com.example.foobar_android.ReqRes.LoginResponse;
import com.example.foobar_android.activities.HomePageActivity;
import com.example.foobar_android.entities.User;
import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:12345/") // Replace with your server URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService apiService = retrofit.create(ApiService.class);

        LoginRequest loginRequest = new LoginRequest(username, password); // Create the LoginRequest object

        Call<LoginResponse> call = apiService.loginUser(loginRequest); // Pass the LoginRequest object as the argument
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    LoginResponse loginResponse = response.body();
                    String token = loginResponse.getToken();
                    String authorization = "Bearer " + "{\"token\":\"" + token + "\"}";

                    apiService.getUserByName(username, authorization).enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            saveTokenLocally(username, password, token, response.body().getDisplayName(), response.body().getProfilePic());
                            navigateToChatActivity(username);
                            Toast.makeText(LoginActivity.this, "Success", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {

                        }
                    });
                } else {
                    switch (response.code()) {
                        case 404:
                            Toast.makeText(LoginActivity.this, "Invalid credentials, please try again.", Toast.LENGTH_SHORT).show();
                            break;
                        case 500:
                            Toast.makeText(LoginActivity.this, "An error occurred on the server.", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(LoginActivity.this, "An unexpected error occurred. Please try again later.", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.e("LoginActivity", "onFailure: An error occurred", t);
                if (t != null) {
                    Log.e("LoginActivity", "Error message: " + t.getMessage());
                }
                Toast.makeText(LoginActivity.this, "An error occurred. Please try again later.", Toast.LENGTH_SHORT).show();
            }
        });

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

    private void saveTokenLocally(String username, String password, String token, String nickname, String profilePic) {
        MyUser.setUser(new User(username, nickname, profilePic), password);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", username);
        editor.putString("password", password);
        editor.putString("token", token);
        editor.putString("nickname", nickname);
        editor.putString("profilePic", profilePic);
        // Store timestamp
        long timestamp = System.currentTimeMillis();
        editor.putLong("timestamp", timestamp);
        editor.apply();
    }

    private void navigateToChatActivity(String username) {
        Intent intent = new Intent(LoginActivity.this, HomePageActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
        finish();
    }
}
