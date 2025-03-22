package com.example.foobar_android.API;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.foobar_android.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.example.foobar_android.Foobar_Android;
import com.example.foobar_android.MyUser;
import com.example.foobar_android.ReqRes.EditUserReq;
import com.example.foobar_android.ReqRes.PostUpdateRequest;
import com.example.foobar_android.entities.User;

import java.util.List;


public class UserAPI {
    private Retrofit retrofit;
    private ApiService webServiceAPI;
    private User user;

    public UserAPI(User user) {
        this.user = user;
        retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:12345/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        webServiceAPI = retrofit.create(ApiService.class);
    }


    public void editUser(EditUserReq editUserReq) {
        String token = getTokenFromLocalStorage();
        String authorization = "Bearer " + "{\"token\":\"" + token + "\"}";
        String username = MyUser.getUser().getUsername();

        Call<Void> call = webServiceAPI.editUser(username, authorization, editUserReq);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("UserAPI", "API Call Failed", t);
            }
        });
    }

    public void deleteUser() {
        String token = getTokenFromLocalStorage();
        String authorization = "Bearer " + "{\"token\":\"" + token + "\"}";
        String username = MyUser.getUser().getUsername();

        Call<Void> call = webServiceAPI.deleteUser(username, authorization);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("UserAPI", "API Call Failed", t);
            }
        });
    }

    private String getTokenFromLocalStorage() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Foobar_Android.context);
        return sharedPreferences.getString("token", "");
    }
}
