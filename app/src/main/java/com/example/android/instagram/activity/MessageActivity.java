package com.example.android.instagram.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.instagram.Interface.APIService;
import com.example.android.instagram.R;
import com.example.android.instagram.adapter.CommentAdapter;
import com.example.android.instagram.adapter.PostAdapter;
import com.example.android.instagram.fragments.LoginFragment;
import com.example.android.instagram.httpservice.HttpClientService;
import com.example.android.instagram.model.Comment;
import com.example.android.instagram.model.CommentList;

import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageActivity extends AppCompatActivity {
    private EditText commenting;
    private Button postComment;
    private ArrayList<Comment> mComment = new ArrayList<>() ;
    private RecyclerView recyclerView;
    private CommentAdapter eAdapter;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_message);
        //to set the action bar
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Comment");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implemented by activity
                onBackPressed();
            }
        });

        commenting = (EditText) findViewById(R.id.comment_text);
        postComment = (Button) findViewById(R.id.post_text);
        postComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentPost();
            }

        });

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_comment);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MessageActivity.this);
        recyclerView.setLayoutManager(layoutManager);

        createComment();
    }

    public void createComment() {

        pDialog = new ProgressDialog(MessageActivity.this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        pDialog.show();

        APIService service = HttpClientService.getClient().create(APIService.class);
        Call<CommentList> call = service.getComments(PostAdapter.liked,"JWT " +LoginFragment.get_Token());

        call.enqueue(new Callback<CommentList>() {
            @Override
            public void onResponse(Call<CommentList> call, Response<CommentList> response) {
                pDialog.dismiss();

                if (response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "success1", Toast.LENGTH_LONG).show();

                    mComment = response.body().getComment();

                   String[] comments = new String[mComment.size()];

                    eAdapter = new CommentAdapter(mComment);
                    recyclerView.setAdapter(eAdapter);

                }
                else {
                    Toast.makeText(getApplicationContext(), "error1", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<CommentList> call, Throwable t) {
                pDialog.dismiss();
                Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG).show();

            }
        });

    }

    private void commentPost() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Commenting....");
        progressDialog.show();

        String post_caption = commenting.getText().toString().trim();


        APIService service = HttpClientService.getClient().create(APIService.class);

        RequestBody post_comment = RequestBody.create(MediaType.parse("text/plain"), post_caption);


        Call<ResponseBody> call = service.commentPost(PostAdapter.liked,"JWT " + LoginFragment.get_Token(), post_comment);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> userResponse) {
                progressDialog.dismiss();
                if (userResponse.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "successful", Toast.LENGTH_LONG).show();
                    // SharedPreference.getInstance(getApplicationContext()).userLogin(user);
                    // startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    createComment();
                } else {
                    Toast.makeText(getApplicationContext(), "error1", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(MessageActivity.this,UserProfileActivity.class));
        finish();

    }
}
