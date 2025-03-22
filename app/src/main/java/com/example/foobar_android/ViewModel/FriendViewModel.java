package com.example.foobar_android.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.foobar_android.entities.Post;
import com.example.foobar_android.entities.User;
import com.example.foobar_android.repositories.FriendRepository;
import com.example.foobar_android.repositories.PostRepository;

import java.util.List;

public class FriendViewModel extends ViewModel {
    private LiveData<List<User>> friends;
    private LiveData<List<User>> friendsReq;


    private FriendRepository repository;

    public FriendViewModel () {
        repository = new FriendRepository();
    }

    public LiveData<List<User>> get(String username) {
        friends = repository.getFriends(username);
        return friends;
    }

    public LiveData<List<User>> getReq() {
        friendsReq = repository.getFriendsReq();
        return friendsReq;
    }
    public void approve(String friendUsername){
        repository.approveFriend(friendUsername);
    }
    public void sendReq(String friendUsername){
        repository.sendFriendReq(friendUsername);
    }

    public void delete(String friendUsername){
        repository.deleteFriend(friendUsername);
    }




}
