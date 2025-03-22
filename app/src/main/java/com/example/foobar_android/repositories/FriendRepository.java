package com.example.foobar_android.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.foobar_android.API.FriendAPI;
import com.example.foobar_android.API.PostAPI;
import com.example.foobar_android.Daos.PostDao;
import com.example.foobar_android.LocalDatabase;
import com.example.foobar_android.entities.Post;
import com.example.foobar_android.entities.User;

import java.util.LinkedList;
import java.util.List;

public class FriendRepository {
    private FriendListData friendListData;
    private FriendListData reqFriendListData;
    private FriendAPI api;

    public FriendRepository() {
        friendListData = new FriendListData();
        reqFriendListData = new FriendListData();
        api = new FriendAPI(friendListData, reqFriendListData);
    }

    class FriendListData extends MutableLiveData<List<User>> {
        public FriendListData() {
            super();
            setValue(new LinkedList<User>());
        }
    }
    public LiveData<List<User>> getFriends(String username) {
        api.getFriends(friendListData, username);
        return friendListData;
    }

    public LiveData<List<User>> getFriendsReq() {
        api.getFriendsReq(reqFriendListData);
        return reqFriendListData;
    }

    public void approveFriend(String friendUsername){
        api.approveFriend(friendUsername);
    }
    public void sendFriendReq(String friendUsername){
        api.sendFriendReq(friendUsername);
    }

    public void deleteFriend(String friendUsername){
        api.deleteFriend(friendUsername);
    }

}
