package com.mobilecomputing.group3.mcproject;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;

public class TeamActivity extends AppCompatActivity {

    String ip = new IP().getIP();
    TentativeMineAdapter tentativeMineAdapter;
    TeamAdapter teamAdapter, tentOthersAdapter;
    ListView listview,listview2,listview3;
    JSONObject jsonObject;
    JSONArray teamList,tenOthersList,tenMineList;
    JSONArray userList;
    HashSet<String> userSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);

        GetInfo info = new GetInfo();
        info.execute();

//        String[] sample={"a","b"};
//        ListAdapter teamAdapt=new TeamAdapter(this, sample);
//        ListView listView=(ListView) findViewById(R.id.teamlist);
//        listView.setAdapter(teamAdapt);
    }

    public void onBack(View view){
        finish();
    }



    private class GetInfo extends AsyncTask<String, Void, String> {

        String[] teamConstraint,tentativeOthers,tentativeMine;
        HashSet<JSONObject> resultSet;

        public void populate() {
            JSONObject temp;
            for (int i = 0; i < userList.length(); i++) {
                // Check if they have the skills and aoi
                try {
                    temp = userList.getJSONObject(i);
                    if (Arrays.asList(teamConstraint).contains(temp.getString("username"))) {
                        teamAdapter.add(new SearchClass(temp.getString("name"), temp.getString("username")));
                    }

                    else if(Arrays.asList(tentativeMine).contains(temp.getString("username"))) {
                        tentativeMineAdapter.add(new SearchClass(temp.getString("name"), temp.getString("username")));
                    }

                    else if(Arrays.asList(tentativeOthers).contains(temp.getString("username"))) {
                        tentOthersAdapter.add(new SearchClass(temp.getString("name"), temp.getString("username")));
                    }
                } catch (Exception ex) {
                    Toast.makeText(TeamActivity.this,
                            ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            listview = (ListView) findViewById(R.id.teamlist);
            listview.setAdapter(teamAdapter);

            listview2 = (ListView) findViewById(R.id.tentMineList);
            listview2.setAdapter(tentativeMineAdapter);

            listview3 = (ListView) findViewById(R.id.tentOthersList);
            listview3.setAdapter(tentOthersAdapter);

            adjustList(listview);
            adjustList(listview2);
            adjustList(listview3);
        }

        public void adjustList(ListView listView){
            ViewGroup.LayoutParams lp = listview.getLayoutParams();

            int totalHeight = 0,
                    desiredWidth = View.MeasureSpec.makeMeasureSpec(listview.getWidth(), View.MeasureSpec.UNSPECIFIED);
            View v = null;

            for ( int i = 0 ; i < teamAdapter.getCount(); i++ ) {
                v = teamAdapter.getView(i, v, listview);
                if ( i == 0 ) {
                    v.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));
                }
                v.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                totalHeight += v.getMeasuredHeight();
            }

            lp.height = (listview.getDividerHeight() * (teamAdapter.getCount() - 1)) + totalHeight + 10;
            listview.setLayoutParams(lp);
        }


        public void getTeams() {
            try {
                String username=getIntent().getExtras().get("username").toString();
                String myurl = "http://" + ip + ":3000/user/"+username;

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
                teamList=jsonObject.getJSONArray("teamwith");
                tenOthersList=jsonObject.getJSONArray("tentative_others");
                tenMineList=jsonObject.getJSONArray("tentative_mine");
                teamConstraint=new String[teamList.length()+1];
                tentativeOthers=new String[tenOthersList.length()];
                tentativeMine=new String[tenMineList.length()];
                int i;
                for(i=0;i<teamList.length();i++){
                    teamConstraint[i]=teamList.getString(i);
                }
                teamConstraint[i]=username;

                for(i=0;i<tenOthersList.length();i++){
                    tentativeOthers[i]=tenOthersList.getString(i);
                }

                for(i=0;i<tenMineList.length();i++){
                    tentativeMine[i]=tenMineList.getString(i);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }



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


                if ( teamAdapter == null ) {
                    String userName1=getIntent().getExtras().get("username").toString();
                    teamAdapter = new TeamAdapter(TeamActivity.this,R.layout.team_row_layout,userName1);
                }

                if ( tentativeMineAdapter == null ) {
                    String userName1=getIntent().getExtras().get("username").toString();
                    tentativeMineAdapter = new TentativeMineAdapter(TeamActivity.this,R.layout.tentminerow,userName1);
                }

                if ( tentOthersAdapter == null ) {
                    String userName1=getIntent().getExtras().get("username").toString();
                    tentOthersAdapter = new TeamAdapter(TeamActivity.this,R.layout.team_row_layout,userName1);
                }
                populate();
            }catch(Exception e){
                e.printStackTrace();
            }
        }


        @Override
        protected String doInBackground(String... arg0) {
            getTeams();
            //getTentativeOthers();
            //getTentativeMine();
            getAllUsers();
            return null;
        }
    }

}