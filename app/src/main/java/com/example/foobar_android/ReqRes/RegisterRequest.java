package com.example.foobar_android.ReqRes;

public class RegisterRequest {
    private String username;
    private String password;
    private String nickname;
    private String profilePic;

    public RegisterRequest(String username, String password, String displayName, String profilePicture) {
        this.username = username;
        this.password = password;
        this.nickname = displayName;
        this.profilePic = profilePicture;
    }

}