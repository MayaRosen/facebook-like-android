package com.example.foobar_android.ReqRes;

import com.google.gson.Gson;

public class PublishPostReq {
    private String username;
    private String nickname;
    private String profilePic;
    private String postText;
    private String img;

    public PublishPostReq(String username, String nickname, String profilePic, String postText, String img) {
        this.username = username;
        this.nickname = nickname;
        this.profilePic = profilePic;
        this.postText = postText;
        this.img = img;
    }

}
