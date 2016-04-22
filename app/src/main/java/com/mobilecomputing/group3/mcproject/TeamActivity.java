package com.mobilecomputing.group3.mcproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

public class TeamActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);
        String[] sample={"a","b"};
        ListAdapter teamAdapt=new TeamAdapter(this, sample);
        ListView listView=(ListView) findViewById(R.id.teamlist);
        listView.setAdapter(teamAdapt);
    }

    public void onBack(View view){
        finish();
    }
}