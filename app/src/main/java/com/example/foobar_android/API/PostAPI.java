package com.example.foobar_android.API;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.foobar_android.ApiService;
import com.example.foobar_android.Daos.PostDao;
import com.example.foobar_android.Foobar_Android;
import com.example.foobar_android.MyUser;
import com.example.foobar_android.ReqRes.AddLike;
import com.example.foobar_android.ReqRes.PostUpdateRequest;
import com.example.foobar_android.activities.HomePageActivity;
import com.example.foobar_android.adapter.PostListAdapter;
import com.example.foobar_android.entities.Post;
import com.example.foobar_android.ReqRes.PublishPostReq;
import com.example.foobar_android.entities.User;

import org.json.JSONObject;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class PostAPI {
    private MutableLiveData<List<Post>> postListData;
    private Retrofit retrofit;
    private ApiService webServiceAPI;

    public PostAPI(MutableLiveData<List<Post>> postListData) {
        this.postListData = postListData;

        retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:12345/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        webServiceAPI = retrofit.create(ApiService.class);
    }

    public void getPosts(MutableLiveData <List<Post>> listPost, PostDao postDao) {
        String token = getTokenFromLocalStorage();
        String authorization = "Bearer " + "{\"token\":\"" + token + "\"}";
        Call<List<Post>> call = webServiceAPI.fetchPosts(authorization);
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (response.isSuccessful()) {
                    listPost.setValue(response.body());
                    new Thread(() -> {
                        postDao.clear();
                        postDao.insert(listPost.getValue().toArray(new Post[0]));
                    }).start();
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Log.e("PostAPI", "API Call Failed", t);
            }
        });
    }

    public void getMyPosts(MutableLiveData <List<Post>> listPost, PostDao postDao, String username) {
        String token = getTokenFromLocalStorage();
        String authorization = "Bearer " + "{\"token\":\"" + token + "\"}";
        Call<List<Post>> call = webServiceAPI.getMyPosts(username, authorization);
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (response.isSuccessful()) {
                    listPost.setValue(response.body());
                    new Thread(() -> {
                        postDao.clear();
                        postDao.insert(listPost.getValue().toArray(new Post[0]));
                    }).start();
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Log.e("PostAPI", "API Call Failed", t);
            }
        });
    }

    public void addPosts( Post post, PostDao postDao) {
        String token = getTokenFromLocalStorage();
        String authorization = "Bearer " + "{\"token\":\"" + token + "\"}";
        String username = getUserFromLocalStorage();
        Call<Void> call = webServiceAPI.savePost(username, post, authorization);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    new Thread(() -> {
                        postDao.insert(post);
                    }).start();
                }
                else{
                    String errorMessage = "Failed to post. Please try again.";
                    if (response.code() == 403) {
                        try {
                            String responseError = response.errorBody().string();
                            errorMessage = new JSONObject(responseError).getJSONArray("errors").getString(0);
                        } catch (Exception e) {
                            Log.e("PostAPI", "Error parsing error response", e);
                        }
                    }
                    Toast.makeText(Foobar_Android.context, errorMessage, Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("PostAPI", "API Call Failed", t);
            }
        });
    }
    private String getTokenFromLocalStorage() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Foobar_Android.context);
        return sharedPreferences.getString("token", "");
    }

    private String getUserFromLocalStorage() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Foobar_Android.context);
        return sharedPreferences.getString("username", "");
    }

    public void editPost( Post post, PostDao postDao) {
        String token = getTokenFromLocalStorage();
        String authorization = "Bearer " + "{\"token\":\"" + token + "\"}";
        String username = MyUser.getUser().getUsername();
        String pid = post.getId();

        String content = post.getPostContent();
        String postImg = post.getPicture();
        PostUpdateRequest postUpdateRequest = new PostUpdateRequest(content, postImg);


        Call<Void> call = webServiceAPI.editPost(username, pid, authorization, postUpdateRequest);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    new Thread(() -> {
                        postDao.insert(post);
                    }).start();
                }
                else{
                    String errorMessage = "Failed to post. Please try again.";
                    if (response.code() == 400) {
                        try {
                            String responseError = response.errorBody().string();
                            errorMessage = new JSONObject(responseError).getJSONArray("errors").getString(0);
                        } catch (Exception e) {
                            Log.e("PostAPI", "Error parsing error response", e);
                        }
                    }
                    Toast.makeText(Foobar_Android.context, errorMessage, Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("PostAPI", "API Call Failed", t);
            }
        });
    }

    public void deletePost(String postId, PostDao postDao) {
        String token = getTokenFromLocalStorage();
        String authorization = "Bearer " + "{\"token\":\"" + token + "\"}";
        Call<Void> call = webServiceAPI.deletePost(MyUser.getUser().getUsername(), postId, authorization);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {

                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("PostAPI", "API Call Failed", t);
            }
        });
    }

    public void getFriendPosts(MutableLiveData <List<Post>> listPost, String friendUsername){
        String token = getTokenFromLocalStorage();
        String authorization = "Bearer " + "{\"token\":\"" + token + "\"}";
        Call<List<Post>> call = webServiceAPI.getFriendPosts(MyUser.getUser().getUsername(), friendUsername, authorization);
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (response.isSuccessful()) {
                    listPost.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Log.e("PostAPI", "API Call Failed", t);
            }
        });

    }

    public void likePost(Post post, int shouldAddLike){
        String token = getTokenFromLocalStorage();
        String authorization = "Bearer " + "{\"token\":\"" + token + "\"}";
        AddLike addLike = new AddLike(shouldAddLike);
        Call<Post> call = webServiceAPI.likePost(MyUser.getUser().getUsername(), post.getId(), authorization, addLike);
        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if (response.isSuccessful()) {
                    Post updatedpost = response.body();
                }
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                Log.e("PostAPI", "API Call Failed", t);
            }
        });
    }
}


