package com.mobilecomputing.group3.mcproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //is it working?
    }

    public void onSignup(View view){
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }
}
