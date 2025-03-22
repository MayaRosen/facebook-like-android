package com.example.foobar_android.ReqRes;

import com.google.gson.annotations.SerializedName;

public class PendingFrindsReq {
    @SerializedName("username")
    private String username;
    public PendingFrindsReq(String username){
        this.username = username;
    }

}
