package com.example.foobar_android.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foobar_android.R;
import com.example.foobar_android.adapter.CommentListAdapter;
import com.example.foobar_android.adapter.PostListAdapter;
import com.example.foobar_android.entities.Comment;
import com.example.foobar_android.entities.Post;
import com.google.gson.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;

import java.util.ArrayList;
import java.util.List;

public class CommentActivity extends AppCompatActivity {
    private List<Comment> comments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comments);



        RecyclerView lstComments = findViewById(R.id.commentLst);
        final CommentListAdapter adapter = new CommentListAdapter(this);
        lstComments.setAdapter(adapter);
        lstComments.setLayoutManager(new LinearLayoutManager(this));



        EditText commentInput = findViewById(R.id.add_comment);
        Button submitComment = findViewById(R.id.publish_comment);

        submitComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                String displayName = sharedPreferences.getString("displayName", "");
                String commentText = commentInput.getText().toString();
                String profilePic = sharedPreferences.getString("profilePic","");
                Bitmap imageBitmap = base64ToBitmap(profilePic);

                if (!commentText.isEmpty()) {
                    Comment newComment = new Comment(comments.size() + 1, displayName, commentText, imageBitmap);
                    comments.add(newComment);
                    adapter.setComments(comments);
                    adapter.notifyDataSetChanged();
                    commentInput.setText("");

                }
            }
        });
        }

    public Bitmap base64ToBitmap(String base64Value) {
        try {
            byte[] decodedBytes = Base64.decode(base64Value, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Comment> getComments() {
        return comments;
    }


}
