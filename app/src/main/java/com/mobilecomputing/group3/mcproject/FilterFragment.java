package com.mobilecomputing.group3.mcproject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class FilterFragment extends AppCompatActivity {

    ListView listview;
    String[] users;
    String[] areas_of_interest;
    String[] skills;

    boolean isChecked[];
    SearchAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter_fragment_layout);

        users = new String[10];

        if ( adapter == null ) {
            adapter = new SearchAdapter(getApplicationContext(),R.layout.userlist);
        }

        for(int i = 1; i<= 10; i++ )
        {
            SearchClass obj = new SearchClass("User #" + i);
            adapter.add(obj);
            users[i-1] = new String("User #" + i);
        }

        if ( listview == null ) {
            listview = (ListView) findViewById(R.id.listview);
            listview.setAdapter(adapter);
        }
    }

    public void onFilterAOI(View view) {
        isChecked = new boolean[5];
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(FilterFragment.this);

        isChecked[1] = true;
        isChecked[3] = true;

        areas_of_interest = new String[5];
        for ( int i = 0; i < 5; i++ ) {
            areas_of_interest[i] = "Area #" + (i+1);
        }

        dialogBuilder.setTitle("Area of Interest");

        // Happens when users click on multiple items
        dialogBuilder.setMultiChoiceItems(areas_of_interest, isChecked, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isCheck) {
                // isChecked should point which indices were selected
                isChecked[which] = isCheck;
            }
        });

        dialogBuilder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                StringBuilder sb = new StringBuilder();
                sb.append("You have selected\n");
                for (int i = 0; i < 5; i++) {
                    if (isChecked[i]) {
                        sb.append(users[i] + ",");
                    }
                }
                Toast.makeText(getApplicationContext(),
                        sb.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        dialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do something when click the negative button
            }
        });

        dialogBuilder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(),
                        "Action cancelled", Toast.LENGTH_SHORT).show();
            }
        });

        try {
            AlertDialog dialogBox = dialogBuilder.create();
            dialogBox.show();
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(),
                    ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void onFilterSkill(View view) {

    }
}