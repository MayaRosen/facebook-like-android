package com.example.foobar_android.repositories;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.foobar_android.API.PostAPI;
import com.example.foobar_android.Daos.PostDao;
import com.example.foobar_android.Foobar_Android;
import com.example.foobar_android.LocalDatabase;
import com.example.foobar_android.ReqRes.PublishPostReq;
import com.example.foobar_android.entities.Post;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class PostRepository {
    private PostDao dao;
    private PostListData postListData;
    private myPostListData myPostListData;
    private PostAPI api;
    private PostAPI myAPI;
    private Post post;

    public PostRepository() {
        LocalDatabase db = LocalDatabase.getInstance();
        dao = db.postDao();
        postListData = new PostListData();
        myPostListData = new myPostListData();
        api = new PostAPI(postListData);
        myAPI = new PostAPI(myPostListData);
        Post[] postsArray = postListData.getValue().toArray(new Post[0]);
    }

    class PostListData extends MutableLiveData<List<Post>> {
        public PostListData() {
            super();
            setValue(new LinkedList<Post>());

        }

        @Override
        protected void onActive() {
            super.onActive();
            new Thread(() ->
            {
                postListData.postValue(dao.index());
            }).start();
        }
    }

    class myPostListData extends MutableLiveData<List<Post>> {
        public myPostListData() {
            super();
            setValue(new LinkedList<Post>());
        }

        @Override
        protected void onActive() {
            super.onActive();
            new Thread(() ->
            {
                myPostListData.postValue(myPostListData.getValue());
            }).start();
        }
    }

    public LiveData<List<Post>> getAll() {
        api.getPosts(postListData, dao);
        return postListData;
    }
    public LiveData<List<Post>> getMyPosts(String username)
    {
        myAPI.getMyPosts(myPostListData, dao, username);
        return myPostListData;
    }

    public LiveData<List<Post>> getFriendPosts(String friendUsername){
        api.getFriendPosts(postListData, friendUsername);
        return postListData;
    }

    public void likePost(Post post, int shouldAdd) {
       api.likePost(post, shouldAdd);
    }

    public void addPost(Post post){
        api.addPosts(post, dao);
    }
    public void editPost(Post post){
        api.editPost(post, dao);
    }
    public void deletePost(String postId){
        api.deletePost(postId, dao);
    }
    public void reload() {
        api.getPosts(new MutableLiveData<>(), dao);
    }
}



