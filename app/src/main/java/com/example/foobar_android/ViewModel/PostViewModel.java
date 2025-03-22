package com.example.foobar_android.ViewModel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.foobar_android.entities.Post;
import com.example.foobar_android.repositories.PostRepository;

import java.util.List;

public class PostViewModel extends ViewModel {
     private LiveData<List<Post>> posts;
    private LiveData<List<Post>> myPosts;
    private Post post;


    private PostRepository repository;

    public PostViewModel () {
        repository = new PostRepository();
     }

    public LiveData<List<Post>> get() {
        posts = repository.getAll();
        return posts;
    }
    public LiveData<List<Post>> getMyPosts(String username) {
        myPosts = repository.getMyPosts(username);
        return myPosts;
    }

    public void add(Post post){
        repository.addPost(post);
    }
    public void edit(Post post){
        repository.editPost(post);
    }
    public void delete(String postId){
        repository.deletePost(postId);
    }
    public void reload(){ repository.reload();}
    public LiveData<List<Post>> getFriendPosts(String friendUsername){
        myPosts = repository.getFriendPosts(friendUsername);
        return myPosts;
    }

    public void likePost(Post getPost, int shouldAddLike){
        repository.likePost(getPost, shouldAddLike);
    }


}
