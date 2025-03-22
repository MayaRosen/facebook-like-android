package com.example.foobar_android.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.foobar_android.R;
import com.example.foobar_android.entities.Comment;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class CommentListAdapter extends RecyclerView.Adapter<com.example.foobar_android.adapter.CommentListAdapter.CommentViewHolder> {
    class CommentViewHolder extends RecyclerView.ViewHolder {
        private final TextView comment_author;
        private final TextView comment_content;
        private final ImageView comment_delete_button;
        private final ImageView comment_edit_button;
        private final ImageView comment_profile_pic;


        private CommentViewHolder(View itemView) {
            super(itemView);
            comment_author = itemView.findViewById(R.id.comment_author);
            comment_content = itemView.findViewById(R.id.comment_content);
            comment_profile_pic = itemView.findViewById(R.id.comment_profile_pic);
            comment_delete_button = itemView.findViewById(R.id.comment_delete_button);
            comment_delete_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Call a method in the adapter to delete the comment
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION) {
                        deleteComment(position);
                    }
                }
            });
            comment_edit_button = itemView.findViewById(R.id.comment_edit_button);

            comment_edit_button.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    editComment(position);
                }
            });

        }
    }

    private final LayoutInflater mInfater;
    private List<Comment> comments;
    private Context context;

    public CommentListAdapter(Context context) {
        mInfater = LayoutInflater.from(context);
        this.context = context;
    }

    public void deleteComment(int position) {
        if (comments != null && position < comments.size()) {
            comments.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, comments.size());
        }
    }
    private void editComment(int position) {
        Comment commentToEdit = comments.get(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Edit Comment");

        final EditText input = new EditText(context);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setText(commentToEdit.getCommentText());
        builder.setView(input);

        builder.setPositiveButton("Save", (dialog, which) -> {
            String newText = input.getText().toString();
            // Update your comment with the new text
            commentToEdit.setCommentText(newText);
            notifyItemChanged(position);
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInfater.inflate(R.layout.comment_item, parent, false);
        return new CommentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CommentViewHolder holder, int position) {
        if (comments != null) {
            final Comment current = comments.get(position);
            holder.comment_author.setText(current.getAuthor());
            holder.comment_content.setText(current.getCommentText());
            holder.comment_profile_pic.setImageBitmap(current.getProfile_image());
        }

    }
    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        if(comments != null) {
            return comments.size();
        }
        else{
            return 0;
        }
    }
}
