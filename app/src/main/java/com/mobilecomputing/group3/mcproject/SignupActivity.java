package com.mobilecomputing.group3.mcproject;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SignupActivity extends AppCompatActivity {
    GMapFragment mapFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        FragmentManager fragmentManager= getFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        SignupFragment signupFragment=new SignupFragment();
        fragmentTransaction.add(R.id.content_frame, signupFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        Button setLoc=(Button) findViewById(R.id.locSelect);
        setLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setContentView(R.layout.activity_signup);
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                mapFragment = new GMapFragment();
                fragmentTransaction.replace(R.id.content_frame, mapFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        Button sig=(Button) findViewById(R.id.submit);
        sig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if( SignupActivity.this.getFragmentManager().getBackStackEntryCount() != 0 ){
                    SignupActivity.this.getFragmentManager().popBackStack();}
            }
        });

    }

}
