package com.example.foobar_android.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.foobar_android.MyUser;
import com.example.foobar_android.R;
import com.example.foobar_android.entities.Post;
import com.example.foobar_android.repositories.PostRepository;

import java.io.ByteArrayOutputStream;
import android.util.Base64;

public class AddPostActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    private EditText postContent;
    private TextView addPicture;
    private String userName;
    private String profilePic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_post);
        Intent intent = getIntent();
        userName = intent.getStringExtra("username");
        profilePic = intent.getStringExtra("profilePic");

        postContent = findViewById(R.id.post_content);
        addPicture = findViewById(R.id.add_picture);
        ImageView selectedImage = findViewById(R.id.selected_image);

        addPicture.setOnClickListener(view -> openFileChooser());

        Button postButton = findViewById(R.id.add_button);
        postButton.setOnClickListener(view -> {
            String content = postContent.getText().toString();
            Bitmap imgBitmap = ((BitmapDrawable)selectedImage.getDrawable()).getBitmap();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            imgBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            String img = Base64.encodeToString(byteArray, Base64.DEFAULT);
            String imgFull = "data:image/jpeg;base64," + img;

            Post newpost = new Post(MyUser.getUser().getUsername(), MyUser.getUser().getProfilePic(), MyUser.getUser().getDisplayName(), content, imgFull);
            new PostRepository().addPost(newpost);
            Intent resultIntent = new Intent();
            resultIntent.putExtra("post_content", content);
            if (imageUri != null) {
                resultIntent.putExtra("post_image", imageUri.toString());
            }
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            ImageView selectedImage = findViewById(R.id.selected_image);
            selectedImage.setImageURI(imageUri);
        }
    }
}
