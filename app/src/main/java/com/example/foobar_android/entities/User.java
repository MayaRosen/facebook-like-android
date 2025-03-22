package com.example.foobar_android.entities;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class User implements Serializable {
    @SerializedName("username")
    private String username;
    @SerializedName("nickname")
    private String displayName;
    @SerializedName("profilePic")
    private String profilePic;

    public User(String username, String displayName, String profilePic) {
        this.username = username;
        this.displayName = displayName;
        this.profilePic = profilePic;
    }

    // Getters and setters for the user properties

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }
}
