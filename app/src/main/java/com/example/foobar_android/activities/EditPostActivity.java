package com.example.foobar_android.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.foobar_android.LoginActivity;
import com.example.foobar_android.MyUser;
import com.example.foobar_android.R;
import com.example.foobar_android.entities.Post;
import com.example.foobar_android.repositories.PostRepository;

import java.io.ByteArrayOutputStream;

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
        if (currentContent != null) {
            editPostContent.setText(currentContent);
        }


        buttonChooseImage.setOnClickListener(v -> {
            Intent intent1 = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent1, 100);
        });



        buttonSavePost.setOnClickListener(v -> {
            String imgFull;
            if (editPostImage.getDrawable() != null) {
                Bitmap imgBitmap = ((BitmapDrawable)editPostImage.getDrawable()).getBitmap();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                imgBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                String img = Base64.encodeToString(byteArray, Base64.DEFAULT);
                imgFull = "data:image/jpeg;base64," + img;
            }
            else{
                imgFull = MyUser.getPostPic();
            }

            Post newpost = new Post(MyUser.getUser().getUsername(), MyUser.getUser().getProfilePic(), MyUser.getUser().getDisplayName(), editPostContent.getText().toString(), imgFull);
            newpost.setId(intent.getStringExtra("postId"));
            if(editPostContent.getText().toString().equals("")){
                newpost.setPostContent(intent.getStringExtra("content"));
            }
            PostRepository postRepository = new PostRepository();
            postRepository.editPost(newpost);
            postRepository.reload();
            Intent resultIntent = new Intent();
            setResult(RESULT_OK, resultIntent);
            Intent goToProfile = new Intent(EditPostActivity.this, MyProfileActivity.class);
            startActivity(goToProfile);
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
