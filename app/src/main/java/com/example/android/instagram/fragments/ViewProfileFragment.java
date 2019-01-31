package com.example.android.instagram.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.instagram.Interface.APIService;
import com.example.android.instagram.R;
import com.example.android.instagram.httpservice.HttpClientService;
import com.example.android.instagram.model.Profile;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ViewProfileFragment extends Fragment {

    private CircleImageView circularProfileImage;
    private TextView userProfileName;
    private Button editYourProfile;
    private Context mContext;


    public ViewProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_view_profile, container, false);
        circularProfileImage = (CircleImageView) v.findViewById(R.id.profile_image);
        userProfileName = (TextView) v.findViewById(R.id.profile_bio);
        editYourProfile = (Button) v.findViewById(R.id.edit_your_profile);
        editYourProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                EditProfileFragment fr = new EditProfileFragment();
                fm.beginTransaction().replace(R.id.drawer_layout,fr).commit();
            }
        });

        viewProfile();
        return v;
    }

    private void viewProfile() {
        final ProgressDialog progressDialog = ProgressDialog.show(getActivity(),
                "Profile",
                "Please wait...",
                true);


        APIService service = HttpClientService.getClient().create(APIService.class);
        Call<Profile> call = service.viewProfile(LoginFragment.get_user_id(),"JWT " +LoginFragment.get_Token());

        mContext = getActivity().getApplicationContext();

        call.enqueue(new Callback<Profile>() {
            @Override
            public void onResponse( Call<Profile> call, Response<Profile> userResponse) {
                progressDialog.dismiss();

                if(userResponse.isSuccessful()) {
                    Toast.makeText(getActivity(), "success1", Toast.LENGTH_LONG).show();
                    //    SharedPreference.getInstance(getApplicationContext()).;
                    // startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                   String user_bio = userResponse.body().getBio();
                   userProfileName.setText(user_bio);
                   String user_image = userResponse.body().getImage();
                    Picasso.with(mContext)
                            .load(user_image)
//                           .placeholder(R.drawable.progress_animation)
                            .fit()
                            .centerCrop()
                            .into(circularProfileImage);

                }
                else{
                    Toast.makeText(getActivity(), "profile not created", Toast.LENGTH_LONG).show();
                }

            }


            @Override
            public void onFailure( Call<Profile> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), "error", Toast.LENGTH_LONG).show();
            }
        });





    }


}
