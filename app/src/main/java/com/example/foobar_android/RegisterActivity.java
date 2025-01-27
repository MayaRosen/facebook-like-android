package com.example.foobar_android;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import android.os.Parcelable;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import android.os.Environment;
import android.util.Log;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.Manifest;

import com.google.android.material.snackbar.Snackbar;

public class RegisterActivity extends AppCompatActivity {


    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private EditText displayNameEditText;
    private ImageView profilePictureImageView;
    private Button uploadProfilePictureButton;
    private Button registerButton;
    private Uri profilePictureUri;
    private String profilePictureBase64;
    private static final int REQUEST_CODE_PICK_IMAGE = 1;
    private String currentPhotoPath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize views
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        confirmPasswordEditText = findViewById(R.id.confirm_password);
        displayNameEditText = findViewById(R.id.display_name);
        profilePictureImageView = findViewById(R.id.profile_picture);
        uploadProfilePictureButton = findViewById(R.id.upload_profile_picture);
        registerButton = findViewById(R.id.register_button);
        TextView welcome = findViewById(R.id.welcome);
        ImageView passwordHelpIcon = findViewById(R.id.password_help_icon);
        TextView passwordHelpText = findViewById(R.id.password_help_text);

        // Set click listener on password help icon
        passwordHelpIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPasswordHelpDialog();
            }
        });



        uploadProfilePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickProfilePicture();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        TextView tvLogin = findViewById(R.id.login_link);
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the RegisterActivity
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
    }


    private void pickProfilePicture() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            try {
                File photoFile = createImageFile();
                if (photoFile != null) {
                    Uri photoUri = FileProvider.getUriForFile(this, "com.example.foobar_android.fileprovider", photoFile);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        Intent chooserIntent = Intent.createChooser(galleryIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Parcelable[]{cameraIntent});
        startActivityForResult(chooserIntent, REQUEST_CODE_PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_PICK_IMAGE) {
                if (data != null) {
                    // Image selected from gallery
                    Uri selectedImageUri = data.getData();
                    if (selectedImageUri != null) {
                        profilePictureUri = selectedImageUri;
                        profilePictureImageView.setImageURI(profilePictureUri);
                        profilePictureBase64 = convertProfilePictureToBase64();
                        return;
                    }
                } if (currentPhotoPath != null) {
                    // Image captured from the camera
                    File photoFile = new File(currentPhotoPath);
                    if (photoFile.exists()) {
                        profilePictureUri = Uri.fromFile(photoFile);
                        profilePictureImageView.setImageURI(profilePictureUri);
                    }
                }
                // Convert profile picture to Base64
                profilePictureBase64 = convertProfilePictureToBase64();
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        // Create the storage directory if it does not exist
        if (!storageDir.exists() && !storageDir.mkdirs()) {
            Log.e("RegisterActivity", "Failed to create directory");
            return null;
        }
        // Create the image file
        File imageFile = File.createTempFile(imageFileName, ".jpg", storageDir);
        currentPhotoPath = imageFile.getAbsolutePath();
        return imageFile;
    }


    private void showPasswordHelpDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Password Requirements");
        builder.setMessage("Password must be at least 8 characters long and contain at least one letter and one number.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private String convertProfilePictureToBase64() {
        try {
            if (profilePictureUri == null) {
                // Set default image from drawable
                profilePictureImageView.setImageResource(R.drawable.default_profile_picture);

                // Convert default profile picture to Base64
                BitmapDrawable drawable = (BitmapDrawable) profilePictureImageView.getDrawable();
                Bitmap defaultProfileBitmap = drawable.getBitmap();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                defaultProfileBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] defaultProfileBytes = byteArrayOutputStream.toByteArray();
                profilePictureBase64 = Base64.encodeToString(defaultProfileBytes, Base64.DEFAULT);
                return profilePictureBase64;
            }
            InputStream inputStream = getContentResolver().openInputStream(profilePictureUri);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }

            byte[] imageBytes = byteArrayOutputStream.toByteArray();
            String base64Image = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            return base64Image;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void registerUser() {
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();
        String displayName = displayNameEditText.getText().toString();
        View rootView = getWindow().getDecorView().findViewById(android.R.id.content);
        Snackbar.make(rootView, "Invalid username or password. Please try again.", Snackbar.LENGTH_SHORT).show();

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || displayName.isEmpty()) {
            Snackbar.make(rootView, "Please fill in all fields", Snackbar.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Snackbar.make(rootView, "Passwords do not match", Snackbar.LENGTH_SHORT).show();
            return;
        }

        // Regular expression to match passwords containing at least one letter and one number
        String passwordPattern = "^(?=.*[a-zA-Z])(?=.*\\d).+$";
        if (!password.matches(passwordPattern)) {
            Snackbar.make(rootView, "Password must contain both letters and numbers", Snackbar.LENGTH_SHORT).show();
            return;
        }

        // Check if the username already exists
        if (isUsernameExists(username)) {
            Snackbar.make(rootView, "Username already exists, please choose a different one", Snackbar.LENGTH_SHORT).show();
            return;
        }

        // Validate profile picture upload
        if (profilePictureBase64 == null) {
            Snackbar.make(rootView, "Please upload a profile picture", Snackbar.LENGTH_SHORT).show();
            return;
        }

        String profilePic = profilePictureBase64;

        SharedPreferences preferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("username", username);
        editor.putString("password", password);
        editor.putString("displayName", displayName);
        editor.putString("profilePic", profilePic);
        editor.apply();



        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean isUsernameExists(String username) {
        SharedPreferences preferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        return preferences.contains("user_" + username + "_username");
    }
}
