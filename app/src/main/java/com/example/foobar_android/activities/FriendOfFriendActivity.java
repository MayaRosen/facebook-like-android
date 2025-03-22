package com.example.foobar_android.activities;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foobar_android.MyUser;
import com.example.foobar_android.R;
import com.example.foobar_android.ViewModel.FriendViewModel;
import com.example.foobar_android.adapter.FriendListAdapter;
import com.example.foobar_android.entities.User;

import java.util.ArrayList;
import java.util.List;

public class FriendOfFriendActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FriendListAdapter friendListAdapter;
    private List<User> friendsOfFriendList = new ArrayList<>();
    private FriendViewModel friendViewModel;
    private String friendUsername;
    private String friendNickname;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_of_friends);

        recyclerView = findViewById(R.id.rvFriendList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        friendListAdapter = new FriendListAdapter(this, false, false);
        recyclerView.setAdapter(friendListAdapter);
        friendViewModel = new FriendViewModel();
        friendUsername = getIntent().getStringExtra("username");
        friendNickname = getIntent().getStringExtra("nickname");
        loadFriendsOfFriendData();
    }

    private void loadFriendsOfFriendData() {
        friendViewModel.get(friendUsername).observe(this, users -> {
            if (users != null && !users.isEmpty()) {
                friendListAdapter.setUsers(users);
            }
        });
    }
}

