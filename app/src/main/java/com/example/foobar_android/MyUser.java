package com.example.foobar_android;

import com.example.foobar_android.entities.User;

public class MyUser {
    private static User user;
    private static String password;
    private static String friendProfilePic;
    private static String postPic;

    public static void setUser(User user, String password){
        MyUser.user = user;
        MyUser.password = password;
    }

    public static User getUser(){
        return user;
    }
    public static String getPassword(){
        return password;
    }
    public static void updateUserDisplayName(String newDisplayName) {
        if (user != null) {
            user.setDisplayName(newDisplayName);
        }
    }
    public static void updateUserProfilePic(String newProfilePic) {
        if (user != null) {
            user.setProfilePic(newProfilePic);
        }
    }

    public static void setPassword(String newPassword) {
        MyUser.password = newPassword;
    }

    public static void setFriendProfilePic(String profile){
        MyUser.friendProfilePic = profile;
    }

    public static String getFriendProfilePic(){
        return MyUser.friendProfilePic;
    }
    public static void setPostPic(String postPic){
        MyUser.postPic = postPic;
    }

    public static String getPostPic(){
        return MyUser.postPic;
    }
}
