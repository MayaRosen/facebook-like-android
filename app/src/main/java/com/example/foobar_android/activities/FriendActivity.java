package com.example.foobar_android.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foobar_android.MyUser;
import com.example.foobar_android.R;
import com.example.foobar_android.ViewModel.FriendViewModel;
import com.example.foobar_android.adapter.FriendListAdapter;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;

public class FriendActivity extends AppCompatActivity {

    private RecyclerView lstFriends, lstFriendRequests;
    private FriendListAdapter friendsAdapter, friendRequestsAdapter;
    private FriendViewModel friendViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_lists);

        lstFriends = findViewById(R.id.lstFriends);
        lstFriendRequests = findViewById(R.id.lstFriendRequests);

        friendsAdapter = new FriendListAdapter(this, false, true);
        friendRequestsAdapter = new FriendListAdapter(this, true, true);

        lstFriends.setAdapter(friendsAdapter);
        lstFriends.setLayoutManager(new LinearLayoutManager(this));

        lstFriendRequests.setAdapter(friendRequestsAdapter);
        lstFriendRequests.setLayoutManager(new LinearLayoutManager(this));

        friendViewModel = new ViewModelProvider(this).get(FriendViewModel.class);

        friendViewModel.get(MyUser.getUser().getUsername()).observe(this, users -> {
            if (users != null && !users.isEmpty()) {
                friendsAdapter.setUsers(users);
            }
        });

        friendViewModel.getReq().observe(this, usersReq -> {
            if (usersReq != null && !usersReq.isEmpty()) {
                friendRequestsAdapter.setUsers(usersReq);
            }
        });
    }
}

