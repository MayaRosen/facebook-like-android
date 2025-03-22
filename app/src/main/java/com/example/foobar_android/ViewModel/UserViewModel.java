package com.example.foobar_android.ViewModel;

import androidx.lifecycle.ViewModel;

import com.example.foobar_android.ReqRes.EditUserReq;
import com.example.foobar_android.repositories.FriendRepository;
import com.example.foobar_android.repositories.UserRepository;

public class UserViewModel extends ViewModel {
    private UserRepository repository;

    public UserViewModel () {
        repository = new UserRepository();
    }
    public void edit(EditUserReq editUserReq){
        repository.editUser(editUserReq);
    }

    public void delete(){
        repository.deleteUser();
    }

}
