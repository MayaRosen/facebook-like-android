package com.example.foobar_android;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class EditPostActivity extends AppCompatActivity {

    private EditText editPostContent;
    private ImageView editPostImage;
    private Button buttonChooseImage, buttonSavePost;
    private Uri imageUri;
    private int postId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_post);

        editPostContent = findViewById(R.id.edit_post_content);
        editPostImage = findViewById(R.id.edit_post_image);
        buttonChooseImage = findViewById(R.id.button_choose_image);
        buttonSavePost = findViewById(R.id.button_save_post);

        // Retrieve and display the post's current data
        Intent intent = getIntent();
        postId = intent.getIntExtra("postId", -1);
        String currentContent = intent.getStringExtra("post_content");
        String currentImageUri = intent.getStringExtra("post_image");
        if (currentContent != null) {
            editPostContent.setText(currentContent);
        }
        if (currentImageUri != null) {
            imageUri = Uri.parse(currentImageUri);
            editPostImage.setImageURI(imageUri);
        }

        buttonChooseImage.setOnClickListener(v -> {
            // Intent to pick an image
            Intent intent1 = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent1, 100);
        });

        buttonSavePost.setOnClickListener(v -> {
            // Return the edited content and image Uri to HomePageActivity
            Intent resultIntent = new Intent();
            resultIntent.putExtra("postId", postId);
            if(!editPostContent.getText().toString().equals("")){
                resultIntent.putExtra("post_content", editPostContent.getText().toString());
            }
            else{
                editPostContent.setText(currentContent);
            }
            if (imageUri != null) {
                resultIntent.putExtra("post_image", imageUri.toString());
            }
            setResult(RESULT_OK, resultIntent);
            finish(); // Close the activity
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            editPostImage.setImageURI(imageUri);
        }
    }
}
