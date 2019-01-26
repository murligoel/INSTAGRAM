package com.example.android.instagram.activity;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


import com.example.android.instagram.R;
import com.example.android.instagram.fragments.EditImageFragment;
import com.example.android.instagram.fragments.FilterListFragment;
import com.example.android.instagram.fragments.LoginFragment;
import com.example.android.instagram.fragments.SignUpFragment;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;

public class FrontPageActivity extends AppCompatActivity {

//    private Button buttonSignUp, buttonLogIn;

    TabLayout tabLayout;
    ViewPager viewPager;

    SignUpFragment signupFragment;
    LoginFragment loginFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front_page);

        tabLayout = (TabLayout) findViewById(R.id.tabs_front_page);
        viewPager = (ViewPager) findViewById(R.id.view_pager_front_page);

//        buttonSignUp = (Button) findViewById(R.id.buttonSignUp);
//        buttonLogIn = (Button) findViewById(R.id.buttonLogin);

//        buttonSignUp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                buttonLogIn.setVisibility(GONE);
//                buttonSignUp.setVisibility(GONE);
//                FragmentManager fm = getSupportFragmentManager();
//                SignUpFragment fr = new SignUpFragment();
//                fm.beginTransaction().replace(R.id.container,fr).commit();
//            }
//        });
//        buttonLogIn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                buttonLogIn.setVisibility(GONE);
//                buttonSignUp.setVisibility(GONE);
//                FragmentManager fm = getSupportFragmentManager();
//                LoginFragment fr = new LoginFragment();
//                fm.beginTransaction().replace(R.id.container,fr).commit();
//            }
//        });
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

    }

    private void setupViewPager(ViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        loginFragment = new LoginFragment();

        signupFragment = new SignUpFragment();

        adapter.addFragment(signupFragment,"SIGN UP");
        adapter.addFragment(loginFragment,"LOGIN");

        viewPager.setAdapter(adapter);
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
}


