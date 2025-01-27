package com.example.foobar_android.adapter;



import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.foobar_android.CommentActivity;
import com.example.foobar_android.EditPostActivity;
import com.example.foobar_android.HomePageActivity;
import com.example.foobar_android.R;
import com.example.foobar_android.ShareActivity;
import com.example.foobar_android.entities.Post;

import java.io.IOException;
import java.io.InputStream;
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


        private PostViewHolder (View itemView) {
            super(itemView);
            tvPostAuthor = itemView.findViewById(R.id.tvPostAuthor);
            tvPostContent = itemView.findViewById(R.id.tvPostContent);
            ivPicture = itemView.findViewById(R.id.ivPicture);
            text_comments_action = itemView.findViewById(R.id.text_comment_action);
            text_share_action = itemView.findViewById(R.id.text_share_action);
            text_like_action = itemView.findViewById(R.id.text_like_action);
            user_pic = itemView.findViewById(R.id.user_pic);


            btnDeletePost = itemView.findViewById(R.id.btnDeletePost);
            btnDeletePost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Call a method in the adapter to delete the post
                    if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                        deletePost(getAdapterPosition());
                    }
                }
            });

            btnEditPost = itemView.findViewById(R.id.btnEditPost);
            btnEditPost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                        editPost(tvPostContent.getText().toString(),getAdapterPosition());
                    }
                }
            });

        }
    }
    private final LayoutInflater mInfater;
    private List<Post> posts;
    private Context context;
    private int editRequestCode;

    public PostListAdapter (Context context, int editRequestCode) {
        mInfater = LayoutInflater.from(context);
        this.context = context;
        this.editRequestCode = editRequestCode;
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
        if(posts != null) {
            final Post current = posts.get(position);
            holder.tvPostAuthor.setText(current.getPostAuthor());
            holder.tvPostContent.setText(current.getPostContent());
            if (current.getProfilePic() != 0) {
                holder.user_pic.setImageResource(current.getProfilePic());
            }
            else if (current.getBitmapProfile() !=null) {
                Bitmap imageBit = current.getBitmapProfile();
                holder.user_pic.setImageBitmap(imageBit);
            }

            if (current.hasImageUri()) {
                // Load the image from the URI
                Uri imageUri = Uri.parse(current.getUriImg());
                holder.ivPicture.setImageURI(imageUri);
            } else if (current.hasImageResId()) {
                holder.ivPicture.setImageResource(current.getPicture());
            } else {
                holder.ivPicture.setVisibility(View.GONE);
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
                } else {
                    textView.setTextColor(textView.getContext().getResources().getColor(R.color.blue_primary));
                    textView.setTag(true);
                }
            }
        });

        holder.text_comments_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int postId = posts.get(holder.getAdapterPosition()).getId();
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