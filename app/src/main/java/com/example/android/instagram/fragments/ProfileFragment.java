package com.example.android.instagram.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.android.instagram.Interface.APIService;
import com.example.android.instagram.R;
import com.example.android.instagram.httpservice.HttpClientService;
import com.example.android.instagram.model.Profile;
import com.example.android.instagram.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProfileFragment extends Fragment {

    private Button buttonProfile;
    private EditText userName, email, firstName, lastName, bio, phoneNumber, birthDate;
    private ImageView image;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_profile, container, false);

        buttonProfile = (Button) v.findViewById(R.id.profile_button);

        userName = (EditText) v.findViewById(R.id.profile_username);
        email = (EditText) v.findViewById(R.id.profile_email);
        firstName = (EditText) v.findViewById(R.id.profile_first_name);
        lastName = (EditText) v.findViewById(R.id.profile_last_name);
        bio = (EditText) v.findViewById(R.id.profile_bio);
        phoneNumber = (EditText) v.findViewById(R.id.profile_phone_number);
        birthDate = (EditText) v.findViewById(R.id.profile_date_of_birth);
        buttonProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });

        return v;
    }

    private void updateProfile(){
        final ProgressDialog progressDialog = ProgressDialog.show(getActivity(),
                "Updating",
                "Please wait...",
                true);


        String user_name = userName.getText().toString().trim();
        String email_id = email.getText().toString().trim();
        String first_name = firstName.getText().toString().trim();
        String last_name = lastName.getText().toString().trim();
        String about_bio = bio.getText().toString().trim();
        String phone_number = phoneNumber.getText().toString().trim();
        String birth_date = birthDate.getText().toString().trim();

        APIService service = HttpClientService.getClient().create(APIService.class);

        Profile profile = new Profile();

        profile.setUsername(user_name);
        profile.setEmail(email_id);
        profile.setFirst_name(first_name);
        profile.setLast_name(last_name);
        profile.setBio(about_bio);
        profile.setPhone_number(phone_number);
        profile.setBirth_date(birth_date);

        Call<Profile> call = service.putPost(LoginFragment.get_user_id(),profile,"JWT " +LoginFragment.get_Token());

        call.enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, Response<Profile> userResponse) {
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
            public void onFailure(Call<Profile> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), "error", Toast.LENGTH_LONG).show();
            }
        });
    }




}
