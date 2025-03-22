package com.example.foobar_android.entities;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity
public class Comment {
    @PrimaryKey
    @SerializedName("commentId")
    private int id;
    @SerializedName("name")
    private String author;
    @SerializedName("comment")
    private String commentText;
    @SerializedName("usr_img")
    private Bitmap profile_image;

    @Ignore
    public Comment(int id, String author, String commentText){
        this.id = id;
        this.author = author;
        this.commentText = commentText;
    }

    public Comment(int id, String author, String commentText, Bitmap profile_image){
        this.id = id;
        this.author = author;
        this.commentText = commentText;
        this.profile_image = profile_image;
    }

    public String getAuthor(){
        return this.author;
    }
    public String getCommentText(){
        return this.commentText;
    }
    public int getId(){
        return this.id;
    }
    public  Bitmap getProfile_image(){
        return this.profile_image;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setProfile_image(Bitmap profile_image) {
        this.profile_image = profile_image;
    }
}
