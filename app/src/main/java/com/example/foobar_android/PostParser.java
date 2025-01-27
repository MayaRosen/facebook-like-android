package com.example.foobar_android;

import android.content.Context;

import com.example.foobar_android.entities.Comment;
import com.example.foobar_android.entities.Post;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class PostParser {

    public static List<Post> parsePostsFromJSON(Context context, String fileName) {
        List<Post> posts = new ArrayList<>();

        try {
            String jsonString = loadJSONFromAsset(context, fileName);
            if (jsonString != null) {
                JSONArray jsonArray = new JSONArray(jsonString);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    int id = jsonObject.getInt("id");
                    String profilePicName = jsonObject.getString("profilePic");
                    int profilePic = getResourceId(context, profilePicName, "drawable");
                    String title = jsonObject.getString("title");
                    String content = jsonObject.getString("postText");
                    String imgName = jsonObject.getString("img"); // Get the image name
                    int imgResId = getResourceId(context, imgName, "drawable");
                    JSONArray commentsJsonArray = jsonObject.getJSONArray("comments");
                    List<Comment> comments = new ArrayList<>();

                    for (int j = 0; j < commentsJsonArray.length(); j++) {
                        JSONObject commentJsonObject = commentsJsonArray.getJSONObject(j);
                        int commentId = commentJsonObject.getInt("commentId");
                        String name = commentJsonObject.getString("name");
                        String commentText = commentJsonObject.getString("comment");
                        Comment comment = new Comment(commentId, name, commentText);
                        comments.add(comment);

                    }
                    Post post = new Post(id, profilePic, title, content, imgResId, comments);
                    posts.add(post);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return posts;
    }

    private static String loadJSONFromAsset(Context context, String fileName) {
        String json = null;
        try {
            InputStream is = context.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    private static int getResourceId(Context context, String resourceName, String resourceType) {
        return context.getResources().getIdentifier(resourceName, resourceType, context.getPackageName());
    }

}
