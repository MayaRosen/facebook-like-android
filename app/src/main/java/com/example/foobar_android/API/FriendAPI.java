package com.example.foobar_android.API;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.foobar_android.ApiService;
import com.example.foobar_android.Daos.PostDao;
import com.example.foobar_android.Foobar_Android;
import com.example.foobar_android.MyUser;
import com.example.foobar_android.ReqRes.AddFriendReq;
import com.example.foobar_android.ReqRes.ApproveFriendReq;
import com.example.foobar_android.entities.Post;
import com.example.foobar_android.entities.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FriendAPI {
    private MutableLiveData<List<User>> friendListData;
    private MutableLiveData<List<User>> reqFriendListData;
    private Retrofit retrofit;
    private ApiService webServiceAPI;

    public FriendAPI(MutableLiveData<List<User>> friendListData, MutableLiveData<List<User>> reqFriendListData) {
        this.friendListData = friendListData;
        this.reqFriendListData = reqFriendListData;

        retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:12345/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        webServiceAPI = retrofit.create(ApiService.class);
    }

    public void getFriends(MutableLiveData <List<User>> listfriends, String username) {
        String token = getTokenFromLocalStorage();
        String authorization = "Bearer " + "{\"token\":\"" + token + "\"}";
        Call<List<User>> call = webServiceAPI.getUserFriends(username, authorization);
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful()) {
                    listfriends.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.e("FriendAPI", "API Call Failed", t);
            }
        });
    }

    public void getFriendsReq(MutableLiveData <List<User>> reqlistfriends) {
        String token = getTokenFromLocalStorage();
        String authorization = "Bearer " + "{\"token\":\"" + token + "\"}";
        Call<List<User>> call = webServiceAPI.getUserPendingFriends(MyUser.getUser().getUsername(), authorization);
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful()) {
                    reqlistfriends.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.e("FriendAPI", "API Call Failed", t);
            }
        });
    }

    public void approveFriend( String friendUsername) {
        String token = getTokenFromLocalStorage();
        String authorization = "Bearer " + "{\"token\":\"" + token + "\"}";
        Call<Void> call = webServiceAPI.approveFriend(MyUser.getUser().getUsername(), friendUsername, authorization);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("FriendAPI", "API Call Failed", t);
            }
        });
    }

    public void sendFriendReq( String friendUsername) {
        String token = getTokenFromLocalStorage();
        String authorization = "Bearer " + "{\"token\":\"" + token + "\"}";
        AddFriendReq addFriendReq = new AddFriendReq(friendUsername);
        Call<Void> call = webServiceAPI.sendFriendReq(MyUser.getUser().getUsername(), addFriendReq, authorization);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("FriendAPI", "API Call Failed", t);
            }
        });
    }

    public void deleteFriend( String friendUsername) {
        String token = getTokenFromLocalStorage();
        String authorization = "Bearer " + "{\"token\":\"" + token + "\"}";
        Call<Void> call = webServiceAPI.deleteFriend(MyUser.getUser().getUsername(), friendUsername, authorization);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("FriendAPI", "API Call Failed", t);
            }
        });
    }
    private String getTokenFromLocalStorage() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Foobar_Android.context);
        return sharedPreferences.getString("token", "");
    }
}
