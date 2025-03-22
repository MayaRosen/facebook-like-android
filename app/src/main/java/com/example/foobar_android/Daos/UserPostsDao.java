package com.example.foobar_android.Daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.foobar_android.entities.Post;

import java.util.List;

@Dao
public interface UserPostsDao {

    @Query("SELECT * FROM post WHERE username = :userName")
    List<Post> get(String userName);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Post... posts);
}
