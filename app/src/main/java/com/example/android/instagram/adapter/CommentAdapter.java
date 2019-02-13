package com.example.android.instagram.adapter;

import android.content.Context;
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
        final String commentId = currentPostComment.getId();

        holder.commentText.setText(textView);
        holder.deleteComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                APIService service = HttpClientService.getClient().create(APIService.class);
                Call<ResponseBody> call = service.commentDelete(commentId,"JWT " + LoginFragment.get_Token());

                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                        if (response.isSuccessful()){
                            Toast.makeText(mContext, "delete success", Toast.LENGTH_SHORT).show();
////                            holder.likeCountText.setText(response.body().getDetail());
//                            String user_detail = response.body().getDetail();
//                            holder.likeCountText.setText(user_detail);
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
        public TextView commentText;
        public Button deleteComment;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            commentText = itemView.findViewById(R.id.text_comment);
            deleteComment = itemView.findViewById(R.id.delete_comment);
        }
    }
}
