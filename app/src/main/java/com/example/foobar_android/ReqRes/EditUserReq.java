package com.example.foobar_android.ReqRes;

public class EditUserReq {
    private String password;
    private String nickname;
    private String profilePic;

    public EditUserReq(String password, String nickname, String profilePic) {
        this.password = password;
        this.nickname = nickname;
        this.profilePic = profilePic;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }
}
