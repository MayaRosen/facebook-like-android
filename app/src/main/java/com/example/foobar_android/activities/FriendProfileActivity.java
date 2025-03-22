package com.example.foobar_android.activities;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foobar_android.MyUser;
import com.example.foobar_android.R;
import com.example.foobar_android.ViewModel.FriendViewModel;
import com.example.foobar_android.ViewModel.PostViewModel;
import com.example.foobar_android.adapter.FriendListAdapter;
import com.example.foobar_android.adapter.PostListAdapter;
import com.example.foobar_android.entities.Post;
import com.example.foobar_android.entities.User;

import java.util.ArrayList;
import java.util.List;

public class FriendProfileActivity extends AppCompatActivity {
    private ImageView imgUserProfilePic;
    private TextView tvUserName;
    private Button btnFollowUnfollow;
    private RecyclerView rvUserPosts;
    private LinearLayout layoutPrivate;
    private static final int EDIT_POST_REQUEST = 1;
    private PostListAdapter postListAdapter;
    private FriendListAdapter friendListAdapter;

    private User userProfile;
    private FriendViewModel friendViewModel;
    private List<User> friendList = new ArrayList<>();
    private boolean isFriend = false;
    private String friendUsername;
    private String friendNickname;
    private String friendProfilepic;
    private PostViewModel postViewModel;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_profile_page);
        imgUserProfilePic = findViewById(R.id.imgUserProfilePic);
        tvUserName = findViewById(R.id.tvUserName);
        btnFollowUnfollow = findViewById(R.id.btnFollowUnfollow);
        rvUserPosts = findViewById(R.id.rvUserPosts);
        layoutPrivate = findViewById(R.id.layoutPrivate);
        postListAdapter = new PostListAdapter(this, EDIT_POST_REQUEST);
        rvUserPosts.setAdapter(postListAdapter);
        rvUserPosts.setLayoutManager(new LinearLayoutManager(this));
        friendViewModel = new FriendViewModel();
        friendUsername = getIntent().getStringExtra("username");
        friendNickname = getIntent().getStringExtra("nickname");
        friendProfilepic = MyUser.getFriendProfilePic();
        postViewModel = new PostViewModel();
        loadUserProfile();
        updateUIBasedOnFriendshipStatus();

        btnFollowUnfollow.setOnClickListener(view -> {
            if (isFriend){
                friendViewModel.delete(friendUsername);
            }
            else{
                friendViewModel.sendReq(friendUsername);
            }
            toggleFollowStatus();
        });
    }

    private void loadUserProfile() {
        friendViewModel.get(MyUser.getUser().getUsername()).observe(this, users -> {
            if (users != null && !users.isEmpty()) {
                friendList = users;
                for (User user : friendList) {
                    if (user.getUsername().equals(friendUsername)) {
                        isFriend = true;
                        break;
                    }
                }
                updateUIBasedOnFriendshipStatus();
            }
        });
        tvUserName.setText(friendNickname);
        Bitmap imageBitmap = base64ToBitmap(friendProfilepic);
        imgUserProfilePic.setImageBitmap(imageBitmap);

    }

    private void updateUIBasedOnFriendshipStatus() {
        rvUserPosts.setVisibility(isFriend ? View.VISIBLE : View.GONE);
        layoutPrivate.setVisibility(isFriend ? View.GONE : View.VISIBLE);
        btnFollowUnfollow.setText(isFriend ? "Unfollow" : "Follow");

        if (isFriend) {
            loadUserPosts();
        }

    }

    private void toggleFollowStatus() {
        isFriend = !isFriend;
        updateUIBasedOnFriendshipStatus();
    }

    private void showFriendsList() {
        friendViewModel.get(friendUsername).observe(this, users -> {
            if (users != null && !users.isEmpty()) {
                friendListAdapter.setUsers(users);
            }
        });
    }

    private void loadUserPosts() {
        postViewModel.getFriendPosts(friendUsername).observe(this, posts-> {
            if (posts != null && !posts.isEmpty()){
                postListAdapter.setPosts(posts);
            }
        });
    }

    public Bitmap base64ToBitmap(String base64Value) {
        String[] parts = base64Value.split(",");
        if (parts.length > 1) {
            base64Value = parts[1];
        }
        byte[] decodedBytes = Base64.decode(base64Value, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }
}

