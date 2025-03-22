package com.example.foobar_android;

import com.example.foobar_android.ReqRes.AddFriendReq;
import com.example.foobar_android.ReqRes.AddLike;
import com.example.foobar_android.ReqRes.ApproveFriendReq;
import com.example.foobar_android.ReqRes.EditUserReq;
import com.example.foobar_android.ReqRes.LoginRequest;
import com.example.foobar_android.ReqRes.LoginResponse;
import com.example.foobar_android.ReqRes.PendingFrindsReq;
import com.example.foobar_android.ReqRes.PostUpdateRequest;
import com.example.foobar_android.ReqRes.PublishPostReq;
import com.example.foobar_android.ReqRes.RegisterRequest;
import com.example.foobar_android.entities.Post;
import com.example.foobar_android.entities.User;

import java.util.List;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    @POST("api/Tokens")
    Call<LoginResponse> loginUser(@Body LoginRequest loginRequest);


    @Headers({
            "Content-Type: application/json",
            "accept: application/json"
    })
    @POST("api/Users")
    Call<Void> registerUser(@Body RegisterRequest registerRequest);

    @GET("api/Posts")
    Call<List<Post>> fetchPosts(@Header("Authorization") String authorization);

    @GET("api/Users/{id}")
    Call<User> getUserByName(@Path("id") String username, @Header("Authorization") String authorization);

    @POST("api/Users/{id}/Posts")
    Call<Void> savePost(@Path("id") String username, @Body Post post, @Header("Authorization") String authorization);

    @PATCH("api/Users/{id}/Posts/{pid}")
    Call<Void> editPost(@Path("id") String username, @Path("pid") String pid, @Header("Authorization") String authorization, @Body PostUpdateRequest postUpdateRequest);

    @DELETE("api/Users/{id}/Posts/{pid}")
    Call<Void> deletePost(@Path("id") String username, @Path("pid") String pid, @Header("Authorization") String authorization);

    @GET("api/Users/{id}/friends")
    Call<List<User>> getUserFriends(@Path("id") String username, @Header("Authorization") String authorization);

    @GET("api/Users/{id}/Posts")
    Call<List<Post>> getMyPosts(@Path("id") String username, @Header("Authorization") String authorization);

    @GET("api/Users/{id}/pendingFriends")
    Call<List<User>> getUserPendingFriends(@Path("id") String username, @Header("Authorization") String authorization);

    @POST("api/Users/{id}/friends")
    Call<Void> sendFriendReq(@Path("id") String username, @Body AddFriendReq addFriendReq, @Header("Authorization") String authorization);

    @PATCH("api/Users/{id}/friends/{fid}")
    Call<Void> approveFriend(@Path("id") String username, @Path("fid") String fid, @Header("Authorization") String authorization);

    @DELETE("api/Users/{id}/friends/{fid}")
    Call<Void> deleteFriend(@Path("id") String username, @Path("fid") String fid, @Header("Authorization") String authorization);

    @PATCH("api/Users/{id}")
    Call<Void> editUser(@Path("id") String username, @Header("Authorization") String authorization, @Body EditUserReq editUserReq);

    @DELETE("api/Users/{id}")
    Call<Void> deleteUser(@Path("id") String username, @Header("Authorization") String authorization);

    @GET("api/Users/{id}/friends/{fid}/posts")
    Call<List<Post>> getFriendPosts(@Path("id") String username, @Path("fid") String friendUsername, @Header("Authorization") String authorization);

    @PATCH("api/Users/{id}/posts/{pid}/likes")
    Call<Post> likePost(@Path("id") String username, @Path("pid") String pid, @Header("Authorization") String authorization, @Body AddLike addLike);
}