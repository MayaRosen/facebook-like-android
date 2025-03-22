package com.example.foobar_android.entities;

import android.graphics.Bitmap;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.foobar_android.R;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;


@Entity
public class Post {
    @PrimaryKey
    @NonNull
    @SerializedName("_id")
    private String id = "";
    private String profilePic;
    private String username;
    @SerializedName("nickname")
    private String postAuthor;
    @SerializedName("postText")
    private String postContent;
    @SerializedName("img")
    private String picture;



    public Post(String username, String profilePic, String postAuthor, String postContent,String picture) {
        this.username = username;
        this.profilePic = profilePic;
        this.postAuthor = postAuthor;
        this.postContent = postContent;
        this.picture = picture;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPostAuthor() {
        return postAuthor;
    }

    public void setPostAuthor(String postAuthor) {
        this.postAuthor = postAuthor;
    }

    public String getPostContent() {
        return postContent;
    }

    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }


    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public byte[] toJson() {
        Gson gson = new Gson();
        String json = gson.toJson(this);
        return json.getBytes();
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }
    public Post getPost(){
        return this;
    }
}
