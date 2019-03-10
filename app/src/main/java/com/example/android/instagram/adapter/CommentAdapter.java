package com.example.android.instagram.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.instagram.Interface.APIService;
import com.example.android.instagram.R;
import com.example.android.instagram.activity.MessageActivity;
import com.example.android.instagram.fragments.LoginFragment;
import com.example.android.instagram.httpservice.HttpClientService;
import com.example.android.instagram.model.Comment;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHolder>{
    private ArrayList<Comment> mComment;
    private Context mContext;
    SharedPreferences sharedPref;

    public CommentAdapter(ArrayList<Comment> comment){
        mComment = comment;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        mContext = parent.getContext();
        View v = LayoutInflater.from(mContext).inflate(R.layout.comment_model,parent,false);
        sharedPref = mContext.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Comment currentPostComment = mComment.get(position);

        final String textView = currentPostComment.getText();
        final String textName = currentPostComment.getName();
        final String commentId = currentPostComment.getId();

        holder.commentText.setText(textView);
        holder.commentName.setText(textName);
        holder.deleteComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String token = sharedPref.getString("usertoken","");

                APIService service = HttpClientService.getClient().create(APIService.class);
                Call<ResponseBody> call = service.commentDelete(commentId,"JWT " + token);

                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                        if (response.isSuccessful()){
                            Toast.makeText(mContext, "delete success", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(mContext, "error1", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(mContext, "error", Toast.LENGTH_SHORT).show();

                    }
                });

            }
        });

    }

    @Override
    public int getItemCount() {
        return mComment.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView commentText,commentName;
        public Button deleteComment;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            commentText = itemView.findViewById(R.id.text_comment);
            commentName = itemView.findViewById(R.id.post_comment_name);
            deleteComment = itemView.findViewById(R.id.delete_comment);
        }
    }
}
