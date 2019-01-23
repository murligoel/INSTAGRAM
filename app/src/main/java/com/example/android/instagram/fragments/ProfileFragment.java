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

import com.example.android.instagram.R;


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


    }



}
