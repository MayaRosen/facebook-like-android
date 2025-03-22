package com.example.foobar_android.repositories;

import com.example.foobar_android.API.FriendAPI;
import com.example.foobar_android.API.UserAPI;
import com.example.foobar_android.ReqRes.EditUserReq;
import com.example.foobar_android.entities.User;

public class UserRepository {
    private UserAPI api;
    private User user;

    public UserRepository(){
        api = new UserAPI(user);
    }

    public void editUser(EditUserReq editUserReq){
        api.editUser(editUserReq);
    }
    public void deleteUser(){
        api.deleteUser();
    }

}
