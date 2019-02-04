package com.example.android.instagram.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.support.v7.widget.Toolbar;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.android.instagram.Interface.APIService;
import com.example.android.instagram.Interface.EditImageFragmentListener;
import com.example.android.instagram.Interface.FiltersListFragmentListener;
import com.example.android.instagram.R;
import com.example.android.instagram.Utils.BitmapUtils;
import com.example.android.instagram.fragments.EditImageFragment;
import com.example.android.instagram.fragments.FilterListFragment;
import com.example.android.instagram.fragments.LoginFragment;
import com.example.android.instagram.httpservice.HttpClientService;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.imageprocessors.subfilters.BrightnessSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.ContrastSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.SaturationSubfilter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FilterActivity extends AppCompatActivity implements FiltersListFragmentListener,EditImageFragmentListener {

    public static final String pictureName = "flash.jpg";
    public static final int PERMISSION_PICK_IMAGE = 1000;
    public static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    Uri image_uri;

    ImageView img_preview;
    TabLayout tabLAyout;
    ViewPager viewPager;
    CoordinatorLayout coordinatorLayout;

    Bitmap originalBitmap,filteredBitmap,finalBitmap;

    FilterListFragment filterListFragment;
    EditImageFragment editImageFragment;

    int brightnessFinal = 0;
    float saturationFinal = 1.0f;
    float constrantFinal = 1.0f;

    private boolean clickable = true;

    // load native image filters library

    static {
     System.loadLibrary("NativeImageProcessor");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Filter");
        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //view

        img_preview = (ImageView) findViewById(R.id.image_preview);
        tabLAyout = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        coordinatorLayout= (CoordinatorLayout) findViewById(R.id.coordinator);

        loadImage();

        setupViewPager(viewPager);
        tabLAyout.setupWithViewPager(viewPager);


    }

    private void loadImage() {
        originalBitmap = BitmapUtils.getBitmapFromAssets(this,pictureName,100,100);
        assert originalBitmap != null;
        filteredBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888,true);
        finalBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888,true);
        img_preview.setImageBitmap(originalBitmap);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        filterListFragment = new FilterListFragment();
        filterListFragment.setListener(this);

        editImageFragment = new EditImageFragment();
        editImageFragment.setListener(this);

        adapter.addFragment(filterListFragment,"FILTERS");
        adapter.addFragment(editImageFragment,"EDIT");

        viewPager.setAdapter(adapter);
    }

    @Override
    public void onBrightnessChanged(int brightness) {
        brightnessFinal = brightness;
        Filter myFilter = new Filter();
        myFilter.addSubFilter(new BrightnessSubFilter(brightness));
        img_preview.setImageBitmap(myFilter.processFilter(finalBitmap.copy(Bitmap.Config.ARGB_8888,true)));
    }

    @Override
    public void onSaturationChanged(float saturation) {
        saturationFinal = saturation;
        Filter myFilter = new Filter();
        myFilter.addSubFilter(new SaturationSubfilter(saturation));
        img_preview.setImageBitmap(myFilter.processFilter(finalBitmap.copy(Bitmap.Config.ARGB_8888,true)));

    }

    @Override
    public void onConstraintChanged(float constraint) {
        constrantFinal = constraint;
        Filter myFilter = new Filter();
        myFilter.addSubFilter(new ContrastSubFilter(constraint));
        img_preview.setImageBitmap(myFilter.processFilter(finalBitmap.copy(Bitmap.Config.ARGB_8888,true)));
    }

    @Override
    public void onEditStarted() {
    }

    @Override
    public void onEditCompleted() {
        Bitmap bitmap = filteredBitmap.copy(Bitmap.Config.ARGB_8888,true);


        Filter myFilter = new Filter();
        myFilter.addSubFilter(new BrightnessSubFilter(brightnessFinal));
        myFilter.addSubFilter(new SaturationSubfilter(saturationFinal));
        myFilter.addSubFilter(new ContrastSubFilter(constrantFinal));

        finalBitmap = myFilter.processFilter(bitmap);

    }

    @Override
    public void onFilterSelected(Filter filter) {

        resetControl();
        filteredBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888,true);
        img_preview.setImageBitmap(filter.processFilter(filteredBitmap));
        finalBitmap = filteredBitmap.copy(Bitmap.Config.ARGB_8888,true);

    }

    private void resetControl() {

      if(editImageFragment != null)
          editImageFragment.resetControls();
      brightnessFinal=0;
      saturationFinal=1.0f;
      constrantFinal=1.0f;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id  = item.getItemId();

        if(id == R.id.action_open){
            openImageFromGallery();
            return true;
        }

        if(id == R.id.action_save){
//            saveImageToGallery();
            if(clickable) {
                clickable = false;
                post();
            }
//            startActivity(new Intent(FilterActivity.this,PostActivity.class));
            return true;
        }

        if(id == R.id.action_camera){
            openCamera();
            return true;
        }
        return super.onOptionsItemSelected(item);
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

//        String post_caption = caption.getText().toString().trim();


        APIService service = HttpClientService.getClient().create(APIService.class);

        if(image_uri != null) {
            File file = new File(String.valueOf(getRealPathFromURI(image_uri)));
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), file);

            // MultipartBody.Part is used to send also the actual file name
            MultipartBody.Part body =
                    MultipartBody.Part.createFormData("picture", file.getName(), requestFile);

//            RequestBody caption_post = RequestBody.create(MediaType.parse("text/plain"));

            Call<ResponseBody> call = service.createPost1("JWT " + LoginFragment.get_Token(),  body);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> userResponse) {
                    progressDialog.dismiss();
                    if (userResponse.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "successful", Toast.LENGTH_LONG).show();
//                        create_Post();
                        // SharedPreference.getInstance(getApplicationContext()).userLogin(user);
                        // startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        startActivity(new Intent(FilterActivity.this,UserProfileActivity.class));
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


    private void openCamera() {

            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }



    private void saveImageToGallery() {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if(report.areAllPermissionsGranted()){
                            try {
                                final String path = BitmapUtils.insertImage(getContentResolver(),
                                        finalBitmap,
                                        System.currentTimeMillis()+"profile.jpg"
                                        ,null);

                                if(!TextUtils.isEmpty(path))
                                {
                                    Snackbar snackbar = Snackbar.make(coordinatorLayout,
                                            "Image Saved to Gallery",
                                            Snackbar.LENGTH_LONG)
                                            .setAction("OPEN", new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    openImage(path);
                                                }
                                            });
                                    snackbar.show();
                                }
                                else {
                                    Snackbar snackbar = Snackbar.make(coordinatorLayout,
                                            "Unable to save images",
                                            Snackbar.LENGTH_LONG);
                                    snackbar.show();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        else
                        {
                            Toast.makeText(FilterActivity.this, "permission denied", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                         token.continuePermissionRequest();
                    }
                })
        .check();
    }

    private void openImage(String path) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(path),"image/*");
        startActivity(intent);
    }

    private void openImageFromGallery() {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if(report.areAllPermissionsGranted()){
                            Intent intent = new Intent(Intent.ACTION_PICK);
                            intent.setType("image/*");
                            startActivityForResult(intent,PERMISSION_PICK_IMAGE);
                        }
                        else{
                            Toast.makeText(FilterActivity.this, "PERMISSION DENIED !", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                      token.continuePermissionRequest();
                    }
                })
        .check();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK  && requestCode == PERMISSION_PICK_IMAGE)
        {
            Uri imageUri = data.getData();
            Bitmap bitmap = BitmapUtils.getBitmapFromGallery(this,imageUri,100,100);
            image_uri = imageUri;
            clickable = true;

            // clear bitmap memory
            originalBitmap.recycle();
            finalBitmap.recycle();
            filteredBitmap.recycle();

            originalBitmap = bitmap.copy(Bitmap.Config.ARGB_8888,true);
            finalBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888,true);
            filteredBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888,true);
            img_preview.setImageBitmap(originalBitmap);
            bitmap.recycle();

            filterListFragment.displayThumbnail(originalBitmap);

        }else if(requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {

            Bitmap photo = (Bitmap) data.getExtras().get("data");

            originalBitmap.recycle();
            finalBitmap.recycle();
            filteredBitmap.recycle();
            Uri tempUri = getImageUri(getApplicationContext(), photo);
            image_uri = tempUri;
            clickable = true;


            originalBitmap = photo.copy(Bitmap.Config.ARGB_8888,true);
            finalBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888,true);
            filteredBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888,true);
            img_preview.setImageBitmap(originalBitmap);
//            img_preview.buildDrawingCache();
//            Bitmap bitmap = img_preview.getDrawingCache();
//
//            Intent intent = new Intent(this, PostActivity.class);
//            intent.putExtra("BitmapImage", bitmap);
//            startActivity(intent);


            filterListFragment.displayThumbnail(originalBitmap);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(FilterActivity.this,UserProfileActivity.class));
        finish();

    }
}
