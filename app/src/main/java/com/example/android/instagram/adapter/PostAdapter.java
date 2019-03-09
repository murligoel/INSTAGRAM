package com.example.android.instagram.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.instagram.Interface.APIService;
import com.example.android.instagram.R;
import com.example.android.instagram.activity.MessageActivity;
import com.example.android.instagram.activity.PostActivity;
import com.example.android.instagram.activity.UserProfileActivity;
import com.example.android.instagram.activity.ViewProfileActivity;
import com.example.android.instagram.fragments.LoginFragment;
import com.example.android.instagram.httpservice.HttpClientService;
import com.example.android.instagram.model.Like;
import com.example.android.instagram.model.Post;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder>{
    private ArrayList<Post>  mPost;
    private Context mContext;
    public static String liked,count;


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
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        Post currentPost = mPost.get(position);
        String imageUrl = currentPost.getPicture();
        final String caption = currentPost.getCaption();
        final String userName = currentPost.getName();
        final String like = currentPost.getId();

//        like = currentPost.getId();


        holder.captionText.setText(caption);
        holder.nameText.setText(userName);
        Picasso.with(mContext)
                .load(imageUrl)
                .fit()
                .centerCrop()
                .into(holder.imageView);
        holder.likeCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                APIService service = HttpClientService.getClient().create(APIService.class);
                Call<Like> call = service.createLike(like,"JWT " + LoginFragment.get_Token());

                call.enqueue(new Callback<Like>() {
                    @Override
                    public void onResponse(Call<Like> call, Response<Like> response) {

                        if (response.isSuccessful()){
                            Toast.makeText(mContext, response.body().getDetail(), Toast.LENGTH_SHORT).show();
//                            holder.likeCountText.setText(response.body().getDetail());
                            String user_detail = response.body().getDetail();
//                            SharedPreferences countSettings = mContext.getSharedPreferences("count",0);
//                             count = countSettings.getString("counts",""+user_detail);
//                            final SharedPreferences.Editor edit = countSettings.edit();
//                            edit.putString("counts",count);
//                            edit.commit();
                            holder.likeCountText.setText(user_detail);
                        }
                        else {
                            Toast.makeText(mContext, "error1", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Like> call, Throwable t) {
                        Toast.makeText(mContext, "error", Toast.LENGTH_SHORT).show();

                    }
                });
                
            }
        });




        holder.messageComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent messageactivity = new Intent(mContext.getApplicationContext(), MessageActivity.class);
                mContext.getApplicationContext().startActivity(messageactivity);
                liked = like;
                Toast.makeText(mContext,liked, Toast.LENGTH_LONG).show();

            }
        });

//        holder.likeCountText.setText(count);
    }

    public static String get_Like() {
        return liked;
    }





    @Override
    public int getItemCount() {
        return mPost.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView captionText,nameText,likeCountText;
        public Button likeCount,messageComment;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image_view);
            captionText = itemView.findViewById(R.id.caption_view);
            nameText = itemView.findViewById(R.id.name_view);
            likeCount = itemView.findViewById(R.id.post_like);
            likeCountText = itemView.findViewById(R.id.like_count);
            messageComment = itemView.findViewById(R.id.message_comment);
        }
    }
}
