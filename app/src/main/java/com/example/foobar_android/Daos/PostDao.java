package com.example.foobar_android.Daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.foobar_android.ReqRes.PublishPostReq;
import com.example.foobar_android.entities.Post;

import java.util.List;
@Dao
public interface PostDao {
    @Query("select * FROM post")
    List<Post> index();
    @Query("SELECT * FROM post WHERE id = :id")
    Post get(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert (Post... posts);
    @Update
    void update(Post... posts);

    @Delete
    void delete(Post... posts);

    @Query("DELETE FROM post")
    void clear();
}

