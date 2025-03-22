package com.example.foobar_android.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foobar_android.LoginActivity;
import com.example.foobar_android.MyUser;
import com.example.foobar_android.R;
import com.example.foobar_android.ViewModel.PostViewModel;
import com.example.foobar_android.ViewModel.UserViewModel;
import com.example.foobar_android.adapter.PostListAdapter;

public class MyProfileActivity extends AppCompatActivity {
    private TextView displayUsername;
    private ImageView displayProfilePic;
    private PostListAdapter adapter;
    private RecyclerView lstPosts;
    private PostViewModel postViewModel;
    private static final int EDIT_POST_REQUEST = 1;
    private Button editbtn;
    private Button deletebtn;
    private UserViewModel userViewModel;
    private ImageView showFriends;
    private ImageView homepage;
    private ImageView friendBottomMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        userViewModel = new UserViewModel();

        //display name and username
        displayUsername = findViewById(R.id.tvProfileName);
        displayUsername.setText(MyUser.getUser().getDisplayName());
        displayProfilePic = findViewById(R.id.user_pic);
        Bitmap imageBitmap = base64ToBitmap(MyUser.getUser().getProfilePic());
        displayProfilePic.setImageBitmap(imageBitmap);
        editbtn = findViewById(R.id.btnEditProfile);
        deletebtn = findViewById(R.id.btnDeleteProfile);
        showFriends = findViewById(R.id.friends);
        homepage = findViewById(R.id.icon_home);
        friendBottomMenu = findViewById(R.id.icon_people);

        //display posts
        lstPosts = findViewById(R.id.mylstPosts);
        adapter = new PostListAdapter(this, EDIT_POST_REQUEST);
        lstPosts.setAdapter(adapter);
        lstPosts.setLayoutManager(new LinearLayoutManager(this));
        postViewModel = new ViewModelProvider(this).get(PostViewModel.class);
        postViewModel.getMyPosts(MyUser.getUser().getUsername()).observe(this, posts-> {
            if (posts != null && !posts.isEmpty()){
                adapter.setPosts(posts);
            }
        });

        editbtn.setOnClickListener(view -> {
            Intent editIntent = new Intent(MyProfileActivity.this, EditProfileActivity.class);
            startActivity(editIntent);
            postViewModel.getMyPosts(MyUser.getUser().getUsername()).observe(this, posts-> {
                if (posts != null && !posts.isEmpty()){
                    adapter.setPosts(posts);
                }
            });

        });

        deletebtn.setOnClickListener(view -> {
            userViewModel.delete();
            Intent loginIntent = new Intent(MyProfileActivity.this, LoginActivity.class);
            startActivity(loginIntent);


        });

        showFriends.setOnClickListener(view -> {
            Intent friendIntent = new Intent(MyProfileActivity.this, FriendActivity.class);
            startActivity(friendIntent);
        });

        friendBottomMenu.setOnClickListener(view -> {
            Intent friendIntent = new Intent(MyProfileActivity.this, FriendActivity.class);
            startActivity(friendIntent);
        });

        homepage.setOnClickListener(view -> {
            Intent home = new Intent(MyProfileActivity.this, HomePageActivity.class);
            startActivity(home);
        });
    }


    public Bitmap base64ToBitmap(String base64Value) {
        // Decode Base64 back to a byte array
        String[] parts = base64Value.split(",");
        if (parts.length > 1) {
            // Use only the second part (after the comma)
            base64Value = parts[1];
        }
        byte[] decodedBytes = Base64.decode(base64Value, Base64.DEFAULT);

        // Use BitmapFactory to create a Bitmap
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }
}
