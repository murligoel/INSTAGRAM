package com.example.android.instagram.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.instagram.R;
import com.example.android.instagram.activity.AddFriendActivity;
import com.example.android.instagram.model.AddFriendModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddFriendAdapter extends RecyclerView.Adapter<AddFriendAdapter.MyViewHolder>{

    private ArrayList<AddFriendModel> mFriend;
    private Context mContext;

    public AddFriendAdapter(ArrayList<AddFriendModel> friend){
        mFriend = friend;
    }

    @Override
    public AddFriendAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        mContext = parent.getContext();
        View v = LayoutInflater.from(mContext).inflate(R.layout.add_friend_model,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AddFriendAdapter.MyViewHolder holder, int position) {

        AddFriendModel currentFriend = mFriend.get(position);

        String imageUrl = currentFriend.getImage();
        final String name = currentFriend.getName();

        holder.nameText.setText(name);
        Picasso.with(mContext)
                .load(imageUrl)
                .placeholder(R.drawable.message)
                .fit()
                .centerCrop()
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mFriend.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public CircleImageView imageView;
        public TextView nameText;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.friend_image);
            nameText = itemView.findViewById(R.id.friend_name);
        }
    }
}
