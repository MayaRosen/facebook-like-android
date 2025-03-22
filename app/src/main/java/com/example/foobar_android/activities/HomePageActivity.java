package com.example.foobar_android.activities;

import java.io.ByteArrayOutputStream;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.foobar_android.ApiService;
import com.example.foobar_android.Daos.UserPostsDao;
import com.example.foobar_android.Foobar_Android;
import com.example.foobar_android.LoginActivity;
import com.example.foobar_android.MyUser;
import com.example.foobar_android.R;
import com.example.foobar_android.ViewModel.PostViewModel;
import com.example.foobar_android.adapter.PostListAdapter;
import com.example.foobar_android.entities.Comment;
import com.example.foobar_android.entities.Post;
import com.example.foobar_android.entities.User;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class HomePageActivity extends AppCompatActivity {
    private String userName;
    private TextView greeting_text;
    private ImageView profile_picture;
    private static final int EDIT_POST_REQUEST = 1;
    private PostListAdapter adapter;
    private RecyclerView lstPosts;
    private boolean isNightMode = false;
    private PostViewModel postViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Intent intent = getIntent();
        this.userName = intent.getStringExtra("username");
        setOnClickViews();
        lstPosts = findViewById(R.id.lstPosts);
        adapter = new PostListAdapter(this, EDIT_POST_REQUEST);
        lstPosts.setAdapter(adapter);
        lstPosts.setLayoutManager(new LinearLayoutManager(this));
        postViewModel = new ViewModelProvider(this).get(PostViewModel.class);
        postViewModel.reload();
        postViewModel.get().observe(this, posts-> {
            if (posts != null && !posts.isEmpty()){
                adapter.setPosts(posts);
            }
        });
        profile_picture = findViewById(R.id.profile_picture);
        greeting_text = findViewById(R.id.greeting_text);
        loadUserDetails();
    }

    private void loadposts(){
        postViewModel.get().observe(this, posts-> {
            if (posts != null && !posts.isEmpty()){
                adapter.setPosts(posts);
            }
        });
    }

    private void loadUserDetails(){
        String nickname = MyUser.getUser().getDisplayName();
        String stringProfilePic = MyUser.getUser().getProfilePic();
        Bitmap imageBitmap = base64ToBitmap(stringProfilePic);
        profile_picture.setImageBitmap(imageBitmap);
        greeting_text.setText(nickname);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    public Bitmap base64ToBitmap(String base64Value) {
        String[] parts = base64Value.split(",");
        if (parts.length > 1) {
            base64Value = parts[1];
        }
        byte[] decodedBytes = Base64.decode(base64Value, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    private void applyTheme(boolean isNightMode) {
        int themeMode = isNightMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO;
        AppCompatDelegate.setDefaultNightMode(themeMode);
        getDelegate().applyDayNight();
    }

    private void logoutUser() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }


    private void setOnClickViews(){
        ImageView addPostButton = findViewById(R.id.add);
        addPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePageActivity.this, AddPostActivity.class);
                intent.putExtra("username", userName);
                startActivity(intent);
            }
        });


        ImageView iconPeople = findViewById(R.id.icon_people);
        iconPeople.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click event
                Intent intent = new Intent(HomePageActivity.this, FriendActivity.class);
                intent.putExtra("username", userName);
                startActivity(intent);
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

        ImageView myProfile = findViewById(R.id.icon_person);
        myProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePageActivity.this, MyProfileActivity.class);
                startActivity(intent);
                loadposts();
            }
        });

        Button logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });
    }
}

