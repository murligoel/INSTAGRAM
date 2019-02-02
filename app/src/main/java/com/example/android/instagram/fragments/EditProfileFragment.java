package com.example.android.instagram.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.instagram.Interface.APIService;
import com.example.android.instagram.R;
import com.example.android.instagram.activity.UserProfileActivity;
import com.example.android.instagram.httpservice.HttpClientService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;


public class EditProfileFragment extends Fragment {

    public static final int IMAGE_GALLERY_REQUEST  = 20;
    private Button buttonProfile,pickProfileImage;
    private EditText bio, phoneNumber;
    private CircleImageView image;
    Uri image_uri;


    public EditProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_edit_profile, container, false);

        buttonProfile = (Button) v.findViewById(R.id.profile_button);
        image = (CircleImageView) v.findViewById(R.id.profile_image);
        pickProfileImage = (Button) v.findViewById(R.id.pick_profile_image_from_gallery);


        bio = (EditText) v.findViewById(R.id.profile_username);

        phoneNumber = (EditText) v.findViewById(R.id.profile_phone_number);
        buttonProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });
        pickProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        return v;
    }

        public void openGallery(){

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

                // declare a stream to read the image data from the SD card.
                InputStream inputStream;

                // we are getting an input stream, based on the URI of the image
                try {
                    Context applicationContext = UserProfileActivity.getContextOfApplication();
                    inputStream =applicationContext.getContentResolver().openInputStream(imageUri);

                    // get a bitmap from the stream.
                    Bitmap img = BitmapFactory.decodeStream(inputStream);
                    image.setImageBitmap(img);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
//                    Toast.makeText(this, "Unable to open image", Toast.LENGTH_LONG).show();
                    Toast.makeText(getActivity(), "Unable to open image", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getActivity().getContentResolver().query(contentURI, null, null, null, null);
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

    private void updateProfile(){
        final ProgressDialog progressDialog = ProgressDialog.show(getActivity(),
                "Updating",
                "Please wait...",
                true);


        String user_name = bio.getText().toString().trim();
        String phone_number = phoneNumber.getText().toString().trim();

        APIService service = HttpClientService.getClient().create(APIService.class);


        File file = new File(String.valueOf(getRealPathFromURI(image_uri)));
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("image", file.getName(), requestFile);


        RequestBody name =RequestBody.create(MediaType.parse("text/plain"),user_name);
        RequestBody phone = RequestBody.create(MediaType.parse("text/plain"),phone_number);
        Call<ResponseBody> call = service.putPost(LoginFragment.get_user_id(),name,phone,"JWT " +LoginFragment.get_Token(),body);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> userResponse) {
                progressDialog.dismiss();
                if(userResponse.isSuccessful()) {
                    Toast.makeText(getActivity(), "successful", Toast.LENGTH_LONG).show();
                    // SharedPreference.getInstance(getApplicationContext()).userLogin(user);
                    // startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                }
                else{
                    Toast.makeText(getActivity(), "error1", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), "error", Toast.LENGTH_LONG).show();
            }
        });
    }




}
