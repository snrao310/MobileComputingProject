package com.mobilecomputing.group3.mcproject;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SignupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        FragmentManager fragmentManager= getFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        SignupFragment signupFragment=new SignupFragment();
        fragmentTransaction.add(R.id.frameCont, signupFragment);
        fragmentTransaction.commit();

        Button setLoc=(Button) findViewById(R.id.locSelect);
        setLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.activity_signup);
                FragmentManager fragmentManager= getFragmentManager();
                FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                GMapFragment mapFragment=new GMapFragment();
                fragmentTransaction.replace(R.id.frameCont,mapFragment);
                fragmentTransaction.commit();
            }
        });
    }

    public void onSetLoc(View view)
    {
        //Intent intent = new Intent(this, MapsActivity.class);
        //startActivity(intent);
    }
}
