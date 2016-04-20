package com.mobilecomputing.group3.mcproject;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
    }

    void onSearchBtnClick(View view) {
        FilterFragment filterFrag = new FilterFragment();
        filterFrag.searchBtnClicked();
    }
}
