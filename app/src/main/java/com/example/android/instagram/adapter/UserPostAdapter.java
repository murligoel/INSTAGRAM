package com.example.android.instagram.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.instagram.R;
import com.example.android.instagram.model.Post;
import com.example.android.instagram.model.UserPost;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UserPostAdapter extends RecyclerView.Adapter<UserPostAdapter.MyViewHolder> {
    private ArrayList<UserPost> mPost;
    private Context mContext;

    public UserPostAdapter(ArrayList<UserPost> post){
        mPost = post;
    }

    @Override
    public UserPostAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View v = LayoutInflater.from(mContext).inflate(R.layout.user_post_model,parent,false);
        return new UserPostAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final UserPostAdapter.MyViewHolder holder, int position) {
        UserPost currentPost = mPost.get(position);
        String imageUrl = currentPost.getPicture();
        Picasso.with(mContext)
                .load(imageUrl)
                .placeholder(R.drawable.message)
                .fit()
                .centerCrop()
                .into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return mPost.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.user_image_view);
        }
    }
}
