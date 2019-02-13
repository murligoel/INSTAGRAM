package com.example.android.instagram.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.instagram.adapter.PostAdapter;
import com.example.android.instagram.Interface.APIService;
import com.example.android.instagram.R;
import com.example.android.instagram.fragments.LoginFragment;
import com.example.android.instagram.httpservice.HttpClientService;
import com.example.android.instagram.model.Post;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserProfileActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int IMAGE_GALLERY_REQUEST  = 20;
    public static Context contextOfApplication;
    private Button openGallery,postOnClick;
    private FloatingActionButton imageFromGallery;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private ArrayList<Post> mPost = new ArrayList<>() ;
//    private Post mPost;
    private RecyclerView recyclerView;
    private PostAdapter eAdapter;
    private ProgressDialog pDialog;
    private EditText caption;
    private CircleImageView postImage;
    Uri image_uri;
    private int count = 0;
    private boolean clickable = true;


    public static Context getContextOfApplication()
    {
        return contextOfApplication;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(UserProfileActivity.this);
        recyclerView.setLayoutManager(layoutManager);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(UserProfileActivity.this, mDrawerLayout, R.string.Open, R.string.Close);
        mDrawerLayout.setDrawerListener(drawerToggle);

        caption = (EditText) findViewById(R.id.caption_for_post);
        postImage = (CircleImageView) findViewById(R.id.post_getting_from_gallery);
        openGallery = (Button) findViewById(R.id.opening_gallery_for_post);
        postOnClick = (Button) findViewById(R.id.post_on_click);

        openGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gallery();
            }
        });

        postOnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clickable) {
                    clickable = false;
                    post();
                }
            }
        });

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        int id = menuItem.getItemId();

                        switch (id){
                            case R.id.nav_profile:
//                                imageFromGallery.setVisibility(GONE);
//                                openGallery.setVisibility(GONE);
//                                postOnClick.setVisibility(GONE);
//                                FragmentManager fm = getSupportFragmentManager();
//                                ViewProfileFragment fr = new ViewProfileFragment();
//                                fm.beginTransaction().replace(R.id.drawer_layout,fr).commit();
//                                mDrawerLayout.closeDrawers();
                                startActivity(new Intent(UserProfileActivity.this,ViewProfileActivity.class));
                                return true;
                            case R.id.log_out:
                                userLogOut();
                                return true;
                        }

                        return true;
                    }
                });


        imageFromGallery = (FloatingActionButton) findViewById(R.id.images_from_gallery);
        contextOfApplication = getApplicationContext();

        imageFromGallery.setOnClickListener(this);
        if( count == 0){
            create_Post();
            count++;
        }
    }

    private void userLogOut() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Logging Out....");
        progressDialog.show();

        APIService service = HttpClientService.getClient().create(APIService.class);

        Call<ResponseBody> call = service.logOut("JWT " +LoginFragment.get_Token());

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> userResponse) {
                progressDialog.dismiss();
                if(userResponse.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Logged Out", Toast.LENGTH_LONG).show();
                    // SharedPreference.getInstance(getApplicationContext()).userLogin(user);
                    // startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    Intent intent = new Intent(UserProfileActivity.this, FrontPageActivity.class);
                    startActivity(intent);
                }
                else{
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

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    private void post() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading Post....");
        progressDialog.show();

        String post_caption = caption.getText().toString().trim();


        APIService service = HttpClientService.getClient().create(APIService.class);

        if(image_uri != null) {
            File file = new File(String.valueOf(getRealPathFromURI(image_uri)));
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), file);

            // MultipartBody.Part is used to send also the actual file name
            MultipartBody.Part body =
                    MultipartBody.Part.createFormData("picture", file.getName(), requestFile);

            RequestBody caption_post = RequestBody.create(MediaType.parse("text/plain"), post_caption);

            Call<ResponseBody> call = service.createPost("JWT " + LoginFragment.get_Token(), caption_post, body);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> userResponse) {
                    progressDialog.dismiss();
                    if (userResponse.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "successful", Toast.LENGTH_LONG).show();
                        create_Post();
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
        else {
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), "image required", Toast.LENGTH_SHORT).show();
        }
    }

    public void gallery(){

        // invoke the image gallery using an implicit intent
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);

        // where do we want to find the data?
        File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String pictureDirectoryPath = pictureDirectory.getPath();




        // finally, get a URI representation
        Uri data = Uri.parse(pictureDirectoryPath);


        // set the data and type. Get all image types.
        photoPickerIntent.setDataAndType(data, "image/*");

        // we will invoke this activity and get something back from it
        startActivityForResult(photoPickerIntent, IMAGE_GALLERY_REQUEST);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode == RESULT_OK){
            // if we are here everything processed successfully.
            if(requestCode == IMAGE_GALLERY_REQUEST){
                // if we are here we are hearing back from the image gallery.

                // the address of the image from sd card
                Uri imageUri = data.getData();
                image_uri = imageUri;
                clickable = true;
                // declare a stream to read the image data from the SD card.
                InputStream inputStream;

                // we are getting an input stream, based on the URI of the image
                try {
                    Context applicationContext = UserProfileActivity.getContextOfApplication();
                    inputStream =applicationContext.getContentResolver().openInputStream(imageUri);

                    // get a bitmap from the stream.
                    Bitmap img = BitmapFactory.decodeStream(inputStream);
                    postImage.setImageBitmap(img);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
//                    Toast.makeText(this, "Unable to open image", Toast.LENGTH_LONG).show();
                    Toast.makeText(getApplicationContext(), "Unable to open image", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void create_Post() {
        pDialog = new ProgressDialog(UserProfileActivity.this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        pDialog.show();

        APIService service = HttpClientService.getClient().create(APIService.class);
        Call<ArrayList<Post>> call = service.viewPost("JWT " +LoginFragment.get_Token());

        call.enqueue(new Callback<ArrayList<Post>>() {
            @Override
            public void onResponse(Call<ArrayList<Post>> call, Response<ArrayList<Post>> response) {
                pDialog.dismiss();

                if (response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "success1", Toast.LENGTH_LONG).show();

                    mPost = response.body();

                    String[] posts = new String[mPost.size()];

                    eAdapter = new PostAdapter(mPost);
                    recyclerView.setAdapter(eAdapter);

                }
                else {
                    Toast.makeText(getApplicationContext(), "error1", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Post>> call, Throwable t) {
                pDialog.dismiss();
                Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG).show();

            }
        });

    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            //return true the click event will be consumed
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        if(v == imageFromGallery){
            Intent intent = new Intent(UserProfileActivity.this, FilterActivity.class);
            startActivity(intent);
        }

    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(UserProfileActivity.this,UserProfileActivity.class));
        finish();

    }
}
