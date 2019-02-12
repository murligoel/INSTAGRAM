package com.example.android.instagram.activity;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.instagram.Interface.APIService;
import com.example.android.instagram.R;
import com.example.android.instagram.adapter.PostAdapter;
import com.example.android.instagram.fragments.LoginFragment;
import com.example.android.instagram.httpservice.HttpClientService;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageActivity extends AppCompatActivity {
    private EditText commenting;
    private Button postComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_message);
        //to set the action bar
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Comment");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Implemented by activity
//                onBackPressed();
//            }
//        });

        commenting = (EditText) findViewById(R.id.comment_text);
        postComment = (Button) findViewById(R.id.post_text);
        postComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentPost();
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

        Call<ResponseBody> call = service.commentPost(PostAdapter.get_Like(),"JWT " + LoginFragment.get_Token(), post_comment);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> userResponse) {
                progressDialog.dismiss();
                if (userResponse.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "successful", Toast.LENGTH_LONG).show();
                    // SharedPreference.getInstance(getApplicationContext()).userLogin(user);
                    // startActivity(new Intent(getApplicationContext(), HomeActivity.class));
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
}
