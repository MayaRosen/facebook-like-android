package com.example.foobar_android.entities;

import android.graphics.Bitmap;
import android.net.Uri;

import androidx.room.PrimaryKey;

import com.example.foobar_android.R;

import java.util.List;

import com.google.gson.annotations.SerializedName;



public class Post {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int profilePic = 0;
    private Bitmap bitmapProfile = null;
    @SerializedName("title")

    private String postAuthor;
    @SerializedName("postText")

    private String postContent;
    @SerializedName("img")

    private int picture;

    private String uriImg;

    private List<Comment> comments;

    public Bitmap getBitmapProfile() {
        return bitmapProfile;
    }

    public void setBitmapProfile(Bitmap bitmapProfile) {
        this.bitmapProfile = bitmapProfile;
    }

    public Post(int id, int profilePic, String postAuthor, String postContent, int picture, List<Comment> comments) {
        this.id = id;
        this.profilePic = profilePic;
        this.postAuthor = postAuthor;
        this.postContent = postContent;
        this.picture = picture;
        this.comments = comments;

    }

    public Post(int id, Bitmap bitmapProfile, String postAuthor, String postContent,String uriImg, List<Comment> comments) {
        this.id = id;
        this.bitmapProfile = bitmapProfile;
        this.postAuthor = postAuthor;
        this.postContent = postContent;
        this.uriImg = uriImg;
        this.comments = comments;

    }
    public String getUriImg() {
        return uriImg;
    }

    public int getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(int profilePic) {
        this.profilePic = profilePic;
    }

    public void setUriImg(String uriImg) {
        this.uriImg = uriImg;
    }


    public List<Comment> getComments() {
        return comments;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public int getPicture() {
        return picture;
    }

    public void setPicture(int picture) {
        this.picture = picture;
    }

    public boolean hasImageUri() {
        return uriImg != null && !uriImg.isEmpty();
    }

    // Method to check if the post has a drawable image resource ID
    public boolean hasImageResId() {
        return picture != -1;
    }
}
