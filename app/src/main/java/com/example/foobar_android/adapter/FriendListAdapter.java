package com.example.foobar_android.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foobar_android.R;
import com.example.foobar_android.ViewModel.FriendViewModel;
import com.example.foobar_android.activities.FriendActivity;
import com.example.foobar_android.activities.MyProfileActivity;
import com.example.foobar_android.entities.User;

import java.util.ArrayList;
import java.util.List;

public class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.ViewHolder> {

    private List<User> users = new ArrayList<>();
    private boolean showAcceptButton;
    private Context context;
    private FriendViewModel friendViewModel;
    private boolean isMe;

    public FriendListAdapter(Context context, boolean showAcceptButton, boolean isMe) {
        this.context = context;
        this.showAcceptButton = showAcceptButton;
        this.friendViewModel = new FriendViewModel();
        this.isMe = isMe;
    }

    public void setUsers(List<User> users) {
        this.users = users;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_requests, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = users.get(position);
        holder.tvFriendName.setText(user.getDisplayName());

        // Decode and set profile picture
        String base64ImageString = user.getProfilePic();
        if (base64ImageString != null && base64ImageString.startsWith("data:image")) {
            String[] parts = base64ImageString.split(",");
            if (parts.length > 1) {
                byte[] decodedBytes = Base64.decode(parts[1], Base64.DEFAULT);
                Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
                holder.imgProfilePic.setImageBitmap(decodedBitmap);
            }
        }

        holder.btnAccept.setVisibility(showAcceptButton ? View.VISIBLE : View.GONE);
        holder.btnDelete.setVisibility(isMe ? View.VISIBLE : View.GONE);

        holder.btnAccept.setOnClickListener(view -> {
            friendViewModel.approve(user.getUsername());
            Intent intent = new Intent(view.getContext(), FriendActivity.class);
            view.getContext().startActivity(intent);
        });

        holder.btnDelete.setOnClickListener(view -> {
            friendViewModel.delete(user.getUsername());
            Intent intent = new Intent(view.getContext(), FriendActivity.class);
            view.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvFriendName;
        Button btnAccept, btnDelete;
        ImageView imgProfilePic;

        public ViewHolder(View itemView) {
            super(itemView);
            tvFriendName = itemView.findViewById(R.id.tvFriendName);
            btnAccept = itemView.findViewById(R.id.btnAccept);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            imgProfilePic = itemView.findViewById(R.id.imgProfilePic);
        }
    }
}
