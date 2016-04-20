package com.mobilecomputing.group3.mcproject;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.HashSet;

public class FilterFragment extends Fragment implements View.OnClickListener {

    ListView listview;
    String[] users;
    String[] areas_of_interest;
    String[] skills;
    HashSet<String> areaSet, skillSet;
    View view;

    boolean isChecked[];
    SearchAdapter adapter;

    @Nullable
    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.filter_fragment_layout, container, false);

        Button btnArea = (Button)view.findViewById(R.id.button);
        Button btnSkills = (Button)view.findViewById(R.id.button2);
        btnArea.setOnClickListener(this);
        btnSkills.setOnClickListener(this);
//
        users = new String[10];
//
//        if ( adapter == null ) {
//            adapter = new SearchAdapter(getActivity(),R.layout.userlist);
//        }
//
//        for(int i = 1; i<= 10; i++ )
//        {
//            SearchClass obj = new SearchClass("User #" + i);
//            listview = (ListView) view.findViewById(R.id.listview);
//            adapter.add(obj);
//            users[i-1] = new String("User #" + i);
//        }
//
//        if ( listview == null ) {
//            listview.setAdapter(adapter);
//        }

//        listview.setMinimumHeight(10 * 50);
//        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) listview.getLayoutParams();
//        lp.height = 10 * 50;
//        listview.setLayoutParams(lp);
//        listview.height
        return view;
    }

    public void onFilterAOI(View view) {
        isChecked = new boolean[5];
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());

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
                Toast.makeText(getActivity(),
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
                Toast.makeText(getActivity(),
                        "Action cancelled", Toast.LENGTH_SHORT).show();
            }
        });

        try {
            AlertDialog dialogBox = dialogBuilder.create();
            dialogBox.show();
        } catch (Exception ex) {
            Toast.makeText(getActivity(),
                    ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void onFilterSkill(View view) {

    }

    public void searchBtnClicked() {
//        Button btnArea = (Button)view.findViewById(R.id.button);
//        Button btnSkills = (Button)view.findViewById(R.id.button2);
//        btnArea.setOnClickListener(this);
//        btnSkills.setOnClickListener(this);

//        users = new String[10];

        if ( adapter == null ) {
            adapter = new SearchAdapter(getActivity(),R.layout.userlist);
        }

        for(int i = 1; i<= 10; i++ )
        {
            SearchClass obj = new SearchClass("User #" + i);
            adapter.add(obj);
            users[i-1] = new String("User #" + i);
        }

        if ( listview == null ) {
            listview = (ListView) view.findViewById(R.id.listview);
            listview.setAdapter(adapter);
        }

        view.invalidate();
    }

    /*
        Override functions for Fragment to work
     */

    @Override
    public void onClick(View v) {
        if ( v.getId() == R.id.searchPartnerBut ) {

        }
    }

//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        view= inflater.inflate(R.layout.filter_fragment_layout, container, false);
//
//        // Initialize buttons "Area of Interest" and "Skillset" here and add
//        // event listeners
//        Button btnArea = (Button)view.findViewById(R.id.button);
//        Button btnSkills = (Button)view.findViewById(R.id.button2);
//        btnArea.setOnClickListener(this);
//        btnSkills.setOnClickListener(this);
//
//        return view;
//    }
}