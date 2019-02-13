package com.example.android.instagram.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.instagram.R;
import com.example.android.instagram.model.Comment;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHolder>{
    private ArrayList<Comment> mComment;
    private Context mContext;

    public CommentAdapter(ArrayList<Comment> comment){
        mComment = comment;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        mContext = parent.getContext();
        View v = LayoutInflater.from(mContext).inflate(R.layout.comment_model,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Comment currentPostComment = mComment.get(position);

        final String textView = currentPostComment.getText();

        holder.commentText.setText(textView);

    }

    @Override
    public int getItemCount() {
        return mComment.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView commentText;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            commentText = itemView.findViewById(R.id.text_comment);
        }
    }
}
