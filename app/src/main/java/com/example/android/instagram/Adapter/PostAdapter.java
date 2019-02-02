package com.example.android.instagram.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.instagram.R;
import com.example.android.instagram.model.Post;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder>{
    private ArrayList<Post>  mPost;
    private Context mContext;

    public PostAdapter(ArrayList<Post> post){
        mPost = post;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View v = LayoutInflater.from(mContext).inflate(R.layout.post_model,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Post currentPost = mPost.get(position);
        String imageUrl = currentPost.getPicture();
        final String dateView = currentPost.getDate_created();
        final String caption = currentPost.getCaption();

        holder.dateText.setText(dateView);
        holder.captionText.setText(caption);
        Picasso.with(mContext)
                .load(imageUrl)
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
        public TextView dateText;
        public TextView captionText;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image_view);
            dateText = itemView.findViewById(R.id.date_view);
            captionText = itemView.findViewById(R.id.caption_view);
        }
    }
}
