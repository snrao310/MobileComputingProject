package com.mobilecomputing.group3.mcproject;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ProfileActivity extends AppCompatActivity {
    boolean b = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
    }

    public void onSearchBtnClick(View view) {
        FragmentManager fragmentManager = getFragmentManager();
        FilterFragment ff = (FilterFragment) fragmentManager.findFragmentById(R.id.filter_fragment);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if ( b ) {
            fragmentTransaction.show(ff).commit();
            b = false;
        } else {
            fragmentTransaction.hide(ff).commit();
            b = true;
        }
    }
}
