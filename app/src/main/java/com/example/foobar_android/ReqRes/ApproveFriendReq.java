package com.example.foobar_android.ReqRes;

import com.google.gson.annotations.SerializedName;

public class ApproveFriendReq {
    @SerializedName("username")
    private String id;
    private String fid;
    public ApproveFriendReq(String username, String friendUN){
        this.id = username;
        this.fid = friendUN;
    }
}
