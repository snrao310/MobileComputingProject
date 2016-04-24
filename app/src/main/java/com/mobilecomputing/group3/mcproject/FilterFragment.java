package com.mobilecomputing.group3.mcproject;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class FilterFragment extends Fragment implements View.OnClickListener{

    ListView listview;
    String[] users;
    String[] areas_of_interest;
    String[] skills;
    JSONObject jsonObject;
    JSONArray userList;

    HashSet<String> areaSet, skillSet, userSet;
    HashSet<JSONObject> resultSet;
    View view;
    String ip=new IP().getIP();

    boolean isChecked[];
    SearchAdapter adapter;

    String searchURL = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate view
        view= inflater.inflate(R.layout.filter_fragment_layout, container, false);

        // Set onclick listeners
        Button btnArea = (Button)view.findViewById(R.id.button);
        Button btnSkills = (Button)view.findViewById(R.id.button2);
        btnArea.setOnClickListener(this);
        btnSkills.setOnClickListener(this);

        populateAllUsers();

        return view;
    }

    @Override
    public void onClick(View v) {
        if ( v.getId() == R.id.button) {
            populateAOI();
            onFilterAOI(v); // Show Area of interest filter dialog
        }

        else if ( v.getId() == R.id.button2 ) {
            populateSkills();
            onFilterSkill(v); // Show Skills filter dialog
        }

//        else if ( v.getId() == R.id.searchPartnerBut ) {
//            showSearchResults(v); // Show the search results
//        }
//
//        else {
//            // Anything else I need to handle here?
//        }

    }

    public void populateAOI() {
        int k = 0;
        isChecked = new boolean[areaSet.size()];
        areas_of_interest = new String[areaSet.size()];

        Iterator i = areaSet.iterator();
        while ( i.hasNext() ) {
            areas_of_interest[k++] = i.next().toString();
        }
    }

    public void onFilterAOI(View view) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());

        dialogBuilder.setTitle("Area of Interest");

        dialogBuilder.setMultiChoiceItems(areas_of_interest, isChecked, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isCheck) {
                isChecked[which] = isCheck;
            }
        });

        dialogBuilder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                StringBuilder sb = new StringBuilder();
                sb.append("You have selected\n");

                List<String> search_skills = new ArrayList<String>();
                List<String> search_aoi = new ArrayList<String>();

                for (int i = 0; i < areas_of_interest.length; i++) {
                    if (isChecked[i]) {
                        sb.append(areas_of_interest[i] + ",");
                        search_aoi.add(areas_of_interest[i]);
                    }
                }

                Iterator it = skillSet.iterator();
                while ( it.hasNext() ) {
                    search_skills.add(it.next().toString());
                }

                Toast.makeText(getActivity(),
                        sb.toString().substring(0,sb.length()-1), Toast.LENGTH_SHORT).show();

                searchAndRefresh(search_skills, search_aoi, 0);
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

    public void searchAndRefresh(List skills, List aoi, int which) {
        JSONObject temp;
        resultSet = new HashSet<JSONObject>();
        for ( int i = 0; i < userList.length(); i++ ) {
            // Check if they have the skills and aoi
            try {
                boolean userAdded = false;
                temp = userList.getJSONObject(i);
                String t_skills = temp.get("skillset").toString();
                String t_aoi = temp.get("aoi").toString();
                if ( which == 0 ) {
                    for ( int k = 0; k < aoi.size(); k++ ) {
                        if ( t_aoi.contains(aoi.get(k).toString()) ) {
                            resultSet.add(temp);
                            break;
                        }
                    }
                } else {
                    for ( int k = 0; k < skills.size(); k++ ) {
                        if ( t_skills.contains(skills.get(k).toString()) ) {
                            resultSet.add(temp);
                            break;
                        }
                    }
                }

            } catch (Exception ex) {
                Toast.makeText(getActivity(),
                        ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        // Now, add all elements in the result set to the adapter
        adapter.clearAll();

        Iterator res = resultSet.iterator();
        while ( res.hasNext() ) {
            JSONObject j=(JSONObject) res.next();
            try {
                adapter.add(new SearchClass(j.getString("name"), j.getString("username")));
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
        ((BaseAdapter)listview.getAdapter()).notifyDataSetChanged();
    }

    public void populateSkills() {
        int k = 0;
        isChecked = new boolean[skillSet.size()];
        skills = new String[skillSet.size()];

        Iterator i = skillSet.iterator();
        while (i.hasNext() ) {
            skills[k++] = i.next().toString();
        }
    }

    public void onFilterSkill(View view) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());

        dialogBuilder.setTitle("Skills");

        dialogBuilder.setMultiChoiceItems(skills, isChecked, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isCheck) {
                isChecked[which] = isCheck;
            }
        });

        dialogBuilder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                StringBuilder sb = new StringBuilder();
                sb.append("You have selected\n");

                List<String> search_skills = new ArrayList<String>();
                List<String> search_aoi = new ArrayList<String>();

                for (int i = 0; i < skills.length; i++) {
                    if (isChecked[i]) {
                        sb.append(skills[i] + ",");
                        search_skills.add(skills[i]);
                    }
                }

                Iterator it = areaSet.iterator();
                while ( it.hasNext() ) {
                    search_aoi.add(it.next().toString());
                }

                Toast.makeText(getActivity(),
                        sb.toString().substring(0,sb.length()-1), Toast.LENGTH_SHORT).show();

                searchAndRefresh(search_skills, search_aoi, 1);
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

    public void populateAllUsers() {
//        users = new String[10];

        GetInfo info = new GetInfo();
        info.execute();

    }

    public void getSearchResults() {
        if ( listview == null ) {
            listview = (ListView) view.findViewById(R.id.listview);
            listview.setAdapter(adapter);
        } else {
            listview.setAdapter(adapter);
        }

        ViewGroup.LayoutParams lp = listview.getLayoutParams();

        int totalHeight = 0,
                desiredWidth = View.MeasureSpec.makeMeasureSpec(listview.getWidth(), View.MeasureSpec.UNSPECIFIED);
        View v = null;

        for ( int i = 0 ; i < adapter.getCount(); i++ ) {
            v = adapter.getView(i, v, listview);
            if ( i == 0 ) {
                v.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));
            }
            v.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += v.getMeasuredHeight();
        }

        lp.height = (listview.getDividerHeight() * (adapter.getCount() - 1)) + totalHeight + 10;
        listview.setLayoutParams(lp);
    }

    public void showSearchResults(View v) {
    }

    public void searchBtnClicked(View view) {

    }

    private class GetInfo extends AsyncTask<String, Void, String> {

        public void getAllUsers() {
            try {
                String myurl = "http://"+ip +":3000/users";

                URL url = new URL(myurl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();
                InputStream inputStream = connection.getInputStream();

                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    Log.i("ERROR: ", "Server returned HTTP " + connection.getResponseCode()
                            + " " + connection.getResponseMessage());
                }

                BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder total = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    total.append(line);
                }

                jsonObject = new JSONObject(total.toString());

            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }


        @Override
        protected void onPostExecute(String s) {
            try {
                userList = jsonObject.getJSONArray("users");
                userSet = new HashSet(userList.length());
                areaSet = new HashSet<>();
                skillSet = new HashSet<>();


                if ( adapter == null ) {
                    String userName1=getActivity().getIntent().getExtras().get("username").toString();
                    adapter = new SearchAdapter(getActivity(),R.layout.userlist,userName1);
                }

                for ( int i = 0; i < userList.length(); i++ ) {
                    // Add all available users, areas of interest and skills here

                    // Variables: areaSet, skillSet, userSet
                    JSONObject temp = userList.getJSONObject(i);

                    JSONArray arr_area = temp.getJSONArray("aoi");
                    JSONArray arr_skills = temp.getJSONArray("skillset");

                    userSet.add(temp.get("name").toString());
                    SearchClass obj = new SearchClass(temp.get("name").toString(),temp.get("username").toString());
                    adapter.add(obj);

                    for ( int j = 0; j < arr_area.length(); j++ ) {
                        areaSet.add(arr_area.get(j).toString().trim());
                    }

                    for ( int j = 0; j < arr_skills.length(); j++ ) {
                        skillSet.add(arr_skills.get(j).toString().trim());
                    }
                }

                Log.i("HERE:", "HERE");
                getSearchResults();
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... arg0) {
            getAllUsers();
            return null;
        }
    }

}