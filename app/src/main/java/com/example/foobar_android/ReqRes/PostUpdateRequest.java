package com.example.foobar_android.ReqRes;

public class PostUpdateRequest {
    private String postText;
    private String img;

    public PostUpdateRequest(String postText, String img) {
        this.postText = postText;
        this.img = img;
    }

    // Getters
    public String getPostText() {
        return postText;
    }

    public String getImg() {
        return img;
    }

    // Setters
    public void setPostText(String postText) {
        this.postText = postText;
    }

    public void setImg(String img) {
        this.img = img;
    }
}