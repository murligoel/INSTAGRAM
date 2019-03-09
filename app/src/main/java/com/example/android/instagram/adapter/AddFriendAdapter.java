package com.example.android.instagram.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import com.example.android.instagram.activity.AddFriendActivity;
import com.example.android.instagram.activity.OtherUserProfileActivity;
import com.example.android.instagram.fragments.LoginFragment;
import com.example.android.instagram.httpservice.HttpClientService;
import com.example.android.instagram.model.AddFriendModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddFriendAdapter extends RecyclerView.Adapter<AddFriendAdapter.MyViewHolder>{

    private ArrayList<AddFriendModel> mFriend;
    private Context mContext;
    public static String user_id;

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
        final String userId = currentFriend.getId();

        holder.nameText.setText(name);
        Picasso.with(mContext)
                .load(imageUrl)
                .placeholder(R.drawable.message)
                .fit()
                .centerCrop()
                .into(holder.imageView);

        holder.addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                APIService service = HttpClientService.getClient().create(APIService.class);


                Call<ResponseBody> call = service.addSearchedFriend(userId,name,"JWT " + LoginFragment.get_Token());

                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                        if (response.isSuccessful()){
                            Toast.makeText(mContext, "friend added", Toast.LENGTH_LONG).show();


                        }
                        else {
                            Toast.makeText(mContext, "error1", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                        Toast.makeText(mContext, "error", Toast.LENGTH_LONG).show();

                    }
                });

            }
        });

        holder.viewFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent otherUserProfile = new Intent(mContext.getApplicationContext(), OtherUserProfileActivity.class);
                mContext.getApplicationContext().startActivity(otherUserProfile);
//                liked = like;
                user_id = userId;
                Toast.makeText(mContext,user_id, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mFriend.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public CircleImageView imageView;
        public TextView nameText;
        public Button addFriend,viewFriend;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.friend_image);
            nameText = itemView.findViewById(R.id.friend_name);
            addFriend = itemView.findViewById(R.id.add_friend_button);
            viewFriend = itemView.findViewById(R.id.view_other_users_profile);
        }
    }
}
