package com.example.foobar_android.adapter;



import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.foobar_android.Foobar_Android;
import com.example.foobar_android.MyUser;
import com.example.foobar_android.ViewModel.PostViewModel;
import com.example.foobar_android.activities.CommentActivity;
import com.example.foobar_android.activities.EditPostActivity;
import com.example.foobar_android.R;
import com.example.foobar_android.activities.FriendProfileActivity;
import com.example.foobar_android.activities.MyProfileActivity;
import com.example.foobar_android.activities.ShareActivity;
import com.example.foobar_android.entities.Post;
import com.example.foobar_android.repositories.PostRepository;

import org.w3c.dom.Document;

import java.util.List;

public class PostListAdapter extends RecyclerView.Adapter<PostListAdapter.PostViewHolder> {
    class PostViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvPostAuthor;
        private final TextView tvPostContent;
        private final ImageView ivPicture;
        private final TextView text_comments_action;
        private final TextView text_share_action;
        private final TextView text_like_action;
        private final ImageView btnDeletePost;
        private final ImageView btnEditPost;
        private final ImageView user_pic;
        private String id;
        private PostViewModel postViewModel;


        private PostViewHolder (View itemView) {
            super(itemView);
            tvPostAuthor = itemView.findViewById(R.id.tvPostAuthor);
            tvPostContent = itemView.findViewById(R.id.tvPostContent);
            ivPicture = itemView.findViewById(R.id.ivPicture);
            text_comments_action = itemView.findViewById(R.id.text_comment_action);
            text_share_action = itemView.findViewById(R.id.text_share_action);
            text_like_action = itemView.findViewById(R.id.text_like_action);
            user_pic = itemView.findViewById(R.id.user_pic);
            postViewModel = new PostViewModel();
            btnDeletePost = itemView.findViewById(R.id.btnDeletePost);
            btnDeletePost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    // Call a method in the adapter to delete the post
                    if (position != RecyclerView.NO_POSITION) {
                        if (!posts.get(position).getUsername().equals(MyUser.getUser().getUsername())) {
                            Toast.makeText(Foobar_Android.context, "You can only delete your own posts.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        // Get the ID of the post at this position
                        String postId = posts.get(position).getId();
                        postViewModel = new PostViewModel();
                        postViewModel.delete(postId);
                        posts.remove(position);
                        Intent intent = new Intent(v.getContext(), MyProfileActivity.class);
                        v.getContext().startActivity(intent);

                    }
                }
            });

            btnEditPost = itemView.findViewById(R.id.btnEditPost);

            btnEditPost.setOnClickListener (new View.OnClickListener() {
                public void onClick(View v) {
                    // Get the position of the adapter item
                    int position = getAdapterPosition();

                    // Check if the item still exists
                    if (position != RecyclerView.NO_POSITION) {
                        if (!posts.get(position).getUsername().equals(MyUser.getUser().getUsername())) {
                            Toast.makeText(Foobar_Android.context, "You can only edit your own posts.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // Get the ID of the post at this position
                        String postId = posts.get(position).getId();

                        // Create an intent and put the postId as an extra
                        Intent intent = new Intent(v.getContext(), EditPostActivity.class);
                        intent.putExtra("postId", postId);
                        intent.putExtra("content", posts.get(position).getPostContent());
                        MyUser.setPostPic(posts.get(position).getPicture());
                        // Start the activity
                        v.getContext().startActivity(intent);
                    }
                    }
            });

            user_pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Post post = posts.get(position);
                        Intent intent = new Intent(context, FriendProfileActivity.class);
                        intent.putExtra("username", post.getUsername());
                        intent.putExtra("nickname", post.getPostAuthor());
                        MyUser.setFriendProfilePic(post.getProfilePic());
                        context.startActivity(intent);
                    }
                }
            });
        }
    }
    private final LayoutInflater mInfater;
    private List<Post> posts;
    private Context context;
    private int editRequestCode;
    private PostViewModel postViewModel;

    public PostListAdapter (Context context, int editRequestCode) {
        mInfater = LayoutInflater.from(context);
        this.context = context;
        this.editRequestCode = editRequestCode;
        this.postViewModel = new PostViewModel();
    }
    public void deletePost(int position) {
        if (posts != null && position < posts.size()) {
            posts.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, posts.size());
        }
    }
    private void editPost(String currentContext, int position) {
        Intent intent = new Intent(context, EditPostActivity.class);
        intent.putExtra("postId", posts.get(position).getId());
        intent.putExtra("post_content", currentContext);
        ((Activity) context).startActivityForResult(intent, editRequestCode);
    }
    @Override
    public PostViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        View itemView = mInfater.inflate(R.layout.post_item, parent, false);
        return new PostViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder (PostViewHolder holder, int position) {
        int index = position;
        if(posts != null) {
            final Post current = posts.get(position);
            holder.tvPostAuthor.setText(current.getPostAuthor());
            holder.tvPostContent.setText(current.getPostContent());

            if (current.getProfilePic() != null) {
                String base64ImageString = current.getProfilePic();
                int startIndex = base64ImageString.indexOf(",") + 1;
                if (startIndex > 0) {
                    byte[] decodedBytes = Base64.decode(base64ImageString.substring(startIndex), Base64.DEFAULT);
                    Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
                    holder.user_pic.setImageBitmap(decodedBitmap);
                }
            }
            if (current.getPicture() != null) {
                int startIndex = current.getPicture().indexOf(",") + 1;
                if (startIndex > 0) {
                    byte[] decodedBytes = Base64.decode(current.getPicture().substring(startIndex), Base64.DEFAULT);
                    Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
                    holder.ivPicture.setImageBitmap(decodedBitmap);
                }
            } else {
                holder.ivPicture.setImageResource(R.drawable.beach);
            }



        }

        holder.text_like_action.setTag(false);

        holder.text_like_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView textView = (TextView) view;
                boolean isBlue = (boolean) textView.getTag();
                if (isBlue) {
                    textView.setTextColor(textView.getContext().getResources().getColor(R.color.black));
                    textView.setTag(false);
                    postViewModel.likePost(posts.get(index), 0);
                } else {
                    textView.setTextColor(textView.getContext().getResources().getColor(R.color.blue_primary));
                    textView.setTag(true);
                    postViewModel.likePost(posts.get(index), 1);
                }
            }
        });

        holder.text_comments_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String postId = posts.get(holder.getAdapterPosition()).getId();
                Intent intent = new Intent(context, CommentActivity.class);
                intent.putExtra("postId", postId);
                context.startActivity(intent);
            }
        });

        holder.text_share_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ShareActivity.class);
                context.startActivity(intent);
            }
        });
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        if(posts != null) {
            return posts.size();
        }
        else{
            return 0;
        }
    }
}