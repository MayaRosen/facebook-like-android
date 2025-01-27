package com.example.foobar_android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Base64;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.foobar_android.adapter.PostListAdapter;
import com.example.foobar_android.entities.Comment;
import com.example.foobar_android.entities.Post;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;




public class HomePageActivity extends AppCompatActivity {

    ImageView addPostButton;
    private TextView greetingText;
    private ImageView profile_picture;
    private static final int ADD_POST_REQUEST = 2;
    private static final int EDIT_POST_REQUEST = 1;

    private PostListAdapter adapter;
    private RecyclerView lstPosts;
    private List<Post> posts;
    private boolean isNightMode = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);


        posts = PostParser.parsePostsFromJSON(getApplicationContext(), "post.json");
        lstPosts = findViewById(R.id.lstPosts);
        adapter = new PostListAdapter(this, EDIT_POST_REQUEST);
        lstPosts.setAdapter(adapter);
        lstPosts.setLayoutManager(new LinearLayoutManager(this));

        adapter.setPosts(posts);

        // Find your ImageView using its ID
        addPostButton = findViewById(R.id.add);

        // Set an OnClickListener to that ImageView
        addPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePageActivity.this, AddPostActivity.class);
                startActivityForResult(intent, ADD_POST_REQUEST);
            }
        });
        ImageView shiftButton = findViewById(R.id.shiftButton);
        shiftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toggle between dark and light themes
                isNightMode = !isNightMode;
                applyTheme(isNightMode);
            }
        });

        greetingText = findViewById(R.id.greeting_text);
        profile_picture = findViewById(R.id.profile_picture);
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String displayName = sharedPreferences.getString("displayName", "");
        String profilePic = sharedPreferences.getString("profilePic","");
        Bitmap imageBitmap = base64ToBitmap(profilePic);
        profile_picture.setImageBitmap(imageBitmap);

        // Set the greeting text including the username
        greetingText.setText("Hello, " + displayName + "! How are you?");

        Button logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the logout logic here
                logoutUser();
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_POST_REQUEST && resultCode == RESULT_OK) {
            String postContent = data.getStringExtra("post_content");
            String imageUriString = data.getStringExtra("post_image");
            List<Comment> comments = new ArrayList<>();
            // Create a new Post object
            SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            String displayName = sharedPreferences.getString("displayName", "");
            String profilePic = sharedPreferences.getString("profilePic","");
            Bitmap imageBitmap = base64ToBitmap(profilePic);
            Post newPost = new Post(posts.size() + 1,imageBitmap, displayName, postContent, imageUriString, comments);

            // Add the new post to your list and update the UI
            posts.add(0, newPost);
            adapter.notifyItemInserted(0);
            lstPosts.scrollToPosition(0);
        }

        if (requestCode == EDIT_POST_REQUEST && resultCode == RESULT_OK) {
            // Handle the updated post data
            int postId = data.getIntExtra("postId", -1);
            String updatedText = data.getStringExtra("post_content");
            String imageUriString = data.getStringExtra("post_image");

            // Update the post in your posts list and notify the adapter
            for (int i = 0; i < posts.size(); i++) {
                if (posts.get(i).getId() == postId) {
                    posts.get(i).setPostContent(updatedText);
                    posts.get(i).setUriImg(imageUriString);
                    // Update image if necessary
                    adapter.notifyItemChanged(i);
                    break;
                }
            }
        }
    }
    public Bitmap base64ToBitmap(String base64Value) {
        // Decode Base64 back to a byte array
        byte[] decodedBytes = Base64.decode(base64Value, Base64.DEFAULT);

        // Use BitmapFactory to create a Bitmap
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    private void applyTheme(boolean isNightMode) {
        int themeMode = isNightMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO;
        AppCompatDelegate.setDefaultNightMode(themeMode);


        // Use the default night mode settings (follow system) for your activity
        getDelegate().applyDayNight();
    }

    private void logoutUser() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear(); // Clear all stored data
        editor.apply();

        // Optionally, redirect the user to the login activity
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear the activity stack
        startActivity(intent);
        finish(); // Ensure this activity is closed
    }


}

