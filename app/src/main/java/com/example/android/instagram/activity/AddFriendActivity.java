package com.example.android.instagram.activity;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Toast;

import com.example.android.instagram.Interface.APIService;
import com.example.android.instagram.R;
import com.example.android.instagram.adapter.AddFriendAdapter;
import com.example.android.instagram.fragments.LoginFragment;
import com.example.android.instagram.httpservice.HttpClientService;
import com.example.android.instagram.model.AddFriendModel;
import com.example.android.instagram.model.Post;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddFriendActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProgressDialog pDialog;
    private AddFriendAdapter eAdapter;
    private ArrayList<AddFriendModel> mFriend = new ArrayList<>();

    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        Toolbar mTopToolbar = (Toolbar) findViewById(R.id.toolbar_add_friend);
        setSupportActionBar(mTopToolbar);
        mTopToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        mTopToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                onBackPressed();
            }
        });

        sharedPref  = getSharedPreferences("userInfo", Context.MODE_PRIVATE);


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_add_friend);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(AddFriendActivity.this);
        recyclerView.setLayoutManager(layoutManager);

        addFriend();
    }

    private void addFriend() {
        pDialog = new ProgressDialog(AddFriendActivity.this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        pDialog.show();

        APIService service = HttpClientService.getClient().create(APIService.class);

        String token = sharedPref.getString("usertoken","");

        Call<ArrayList<AddFriendModel>> call = service.addFriend("","JWT " + token);

        call.enqueue(new Callback<ArrayList<AddFriendModel>>() {
            @Override
            public void onResponse(Call<ArrayList<AddFriendModel>> call, Response<ArrayList<AddFriendModel>> response) {
                pDialog.dismiss();

                if (response.isSuccessful()){
//                    Toast.makeText(getApplicationContext(), "success1", Toast.LENGTH_LONG).show();

                    mFriend = response.body();

                    eAdapter = new AddFriendAdapter(mFriend);
                    recyclerView.setAdapter(eAdapter);

                }
                else {
                    Toast.makeText(getApplicationContext(), "error1", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<AddFriendModel>> call, Throwable t) {
                pDialog.dismiss();
                Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG).show();

            }
        });

    }

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu,menu);

        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final android.support.v7.widget.SearchView searchView =
                (android.support.v7.widget.SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        //initiating the search operation an listening to the callbacks
        searchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.i("query", query);
                searchView.clearFocus();
                searchView.setQuery("", false);
                AddFriendActivity.this.setTitle(query);
//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/results?search_query=recipe "+query));
//                startActivity(intent);

                pDialog = new ProgressDialog(AddFriendActivity.this);
                pDialog.setMessage("Please wait...");
                pDialog.setCancelable(false);
                pDialog.show();

                APIService service = HttpClientService.getClient().create(APIService.class);

                String token = sharedPref.getString("usertoken","");

                Call<ArrayList<AddFriendModel>> call = service.addFriend(query,"JWT " + token);

                call.enqueue(new Callback<ArrayList<AddFriendModel>>() {
                    @Override
                    public void onResponse(Call<ArrayList<AddFriendModel>> call, Response<ArrayList<AddFriendModel>> response) {
                        pDialog.dismiss();

                        if (response.isSuccessful()){
//                            Toast.makeText(getApplicationContext(), "success1", Toast.LENGTH_LONG).show();

                            mFriend = response.body();

                            String[] posts = new String[mFriend.size()];

                            eAdapter = new AddFriendAdapter(mFriend);
                            recyclerView.setAdapter(eAdapter);

                        }
                        else {
                            Toast.makeText(getApplicationContext(), "error1", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<AddFriendModel>> call, Throwable t) {
                        pDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG).show();

                    }
                });


                return true;
            }
            //method called everytime the query text changed by user
            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }

        });
        return true;

    }
}
