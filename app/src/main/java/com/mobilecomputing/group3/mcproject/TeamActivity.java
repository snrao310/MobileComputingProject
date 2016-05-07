package com.mobilecomputing.group3.mcproject;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;

public class TeamActivity extends AppCompatActivity {

    String ip = new IP().getIP();
    TentativeMineAdapter tentativeMineAdapter;
    TeamAdapter teamAdapter, tentOthersAdapter;
    ListView listview, listview2, listview3;
    JSONObject jsonObject, placesObject;
    JSONArray teamList, tenOthersList, tenMineList;
    JSONArray userList, placesList;
    HashSet<String> userSet;
    String currentUser;
    String[] teamConstraint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);

        currentUser = getIntent().getExtras().getString("username");

        GetInfo info = new GetInfo();
        info.execute();
    }

    public void onBack(View view) {
        finish();
    }


    public void onSuggest(View v) {
        GetPlaces g = new GetPlaces(getApplicationContext());
        g.execute();


    }


    private class GetPlaces extends AsyncTask<String, Void, String> {

        Context context;
        public GetPlaces(Context c){
            this.context=c;
        }


        public void getUsers() {
            try {
                String myurl = "http://" + ip + ":3000/users";

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
                userList = jsonObject.getJSONArray("users");

            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }

        public void getplaces() {
            try {
                String myurl = "http://" + ip + ":3000/places";

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

                placesObject = new JSONObject(total.toString());
                placesList = placesObject.getJSONArray("places");

            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }


        @Override
        protected void onPostExecute(String s) {
            double[] location = (new LocList()).getLocation();
            String[] details = new String[4];
            double latitude_cur = location[0];
            double longitude_cur = location[1];
            double minlat = 1000;
            double minlong = 1000;
            double maxlat = -1000;
            double maxlong = -1000;

            if (latitude_cur < minlat)
                minlat = latitude_cur;
            if (latitude_cur > maxlat)
                maxlat = latitude_cur;

            if (longitude_cur < minlong)
                minlong = longitude_cur;
            if (longitude_cur > maxlong)
                maxlong = longitude_cur;

            try {
                for (int i = 0; i < userList.length(); i++) {
                    JSONObject temp = userList.getJSONObject(i);

                    if (!temp.getString("username").equals(currentUser) && Arrays.asList(teamConstraint).contains(temp.getString("username"))) {
                        // This guy is a team member

                        double lat = Double.parseDouble(temp.getString("current_latitude"));
                        double longi = Double.parseDouble(temp.getString("current_longitude"));
                        if (lat < minlat)
                            minlat = lat;
                        if (lat > maxlat)
                            maxlat = lat;

                        if (longi < minlong)
                            minlong = longi;
                        if (longi > maxlong)
                            maxlong = longi;
                    }
                }
            } catch (JSONException ex) {
                Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
            }

            double startlat = minlat - (maxlat - minlat) / 2;
            double endlat = maxlat + (maxlat - minlat) / 2;
            double startlon = minlong - (maxlong - minlong) / 2;
            double endlon = maxlong + (maxlong - minlong) / 2;

            try {
                for (int i = 0; i < placesList.length(); i++) {
                    JSONObject temp = placesList.getJSONObject(i);

                    double lat = Double.parseDouble(temp.getString("latitude"));
                    double longi = Double.parseDouble(temp.getString("longitude"));

                    if (startlat < lat && lat < endlat && startlon < longi && longi < endlon) {
                        placesList.remove(i);
                    }
                }
            } catch (JSONException ex) {
                Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
            }
            try {
                Bundle b = new Bundle();
                b.putString("Places", placesList.toString());
                Intent intent = new Intent(TeamActivity.this, MapsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtras(b);
                startActivity(intent);
            }catch (Exception ex){
                ex.getMessage();}
        }


        @Override
        protected String doInBackground(String... params) {
            getUsers();
            getplaces();
            return null;
        }
    }


    public void onMeet(View view) {
        // For each user, do
        String sourceUsername = currentUser;
        String targetUsername = "";

        try {
            for (int i = 0; i < userList.length(); i++) {
                JSONObject temp = userList.getJSONObject(i);

                if (!temp.getString("username").equals(currentUser) && Arrays.asList(teamConstraint).contains(temp.getString("username"))) {
                    // This guy is a team member
                    targetUsername = temp.getString("username");

                    // Send request
                    double[] location = (new LocList()).getLocation();
                    String[] details = new String[4];
                    double latitude_cur = location[0];
                    double longitude_cur = location[1];

                    details[0] = sourceUsername;
                    details[1] = targetUsername;
                    details[2] = String.valueOf(latitude_cur);
                    details[3] = String.valueOf(longitude_cur);

                    GetMeetRequestsTask makeMeetRequest = new GetMeetRequestsTask();
                    makeMeetRequest.execute(details);
                }
            }
        } catch (JSONException ex) {
            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
        }

        Button b=(Button)findViewById(R.id.btnSuggest);
        b.setVisibility(View.VISIBLE);
    }

    /* Get meet requests task, copied from search adapter.java file */
    class GetMeetRequestsTask extends AsyncTask<String, Void, String> {
        public String meetRequester = "";

        @Override
        protected String doInBackground(String... details) {
            try {
                URL url = new URL("http://" + new IP().getIP() + ":3000/makemeetrequest");

                String data = URLEncoder.encode("fromUsername", "UTF-8") + "=" + URLEncoder.encode(details[0], "UTF-8") + "&";
                data += URLEncoder.encode("toUsername", "UTF-8") + "=" + URLEncoder.encode(details[1], "UTF-8") + "&";
                data += URLEncoder.encode("latitude", "UTF-8") + "=" + URLEncoder.encode(details[2], "UTF-8") + "&";
                data += URLEncoder.encode("longitude", "UTF-8") + "=" + URLEncoder.encode(details[3], "UTF-8");

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                wr.write(data);
                wr.flush();

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = reader.readLine();

                meetRequester = line;

                return line;
            } catch (Exception ex) {
                return null; // Error processing request/response
            }
        }

        @Override
        protected void onPostExecute(String s) {
        }
    }



    /* ------------------------------ Start Get location ----------------------------- */

    class LocList implements LocationListener {
        private LocationManager locMgr;
        private double latitude, longitude;
        private Location location;

        public LocList() {
            try {
                locMgr = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
                locMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, this);
                Location location_g = locMgr.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                Location location_n = locMgr.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (location_g != null) {
                    this.location = location_g;
                }
                if (location_g == null && location_n != null) {
                    this.location = location_n;
                }
            } catch (SecurityException ex) {
                Log.i("EXCEPTION:", "Permission denied");
            }
        }

        public LocationManager getLocationManager() {
            return locMgr;
        }

        @Override
        public void onLocationChanged(Location location) {
            this.latitude = location.getLatitude();
            this.longitude = location.getLongitude();
            this.location = location;
        }

        @Override
        public void onProviderDisabled(String provider) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            getApplicationContext().startActivity(intent);
            Toast.makeText(getApplicationContext(), "Gps is turned off!! ",
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderEnabled(String provider) {
            Toast.makeText(getApplicationContext(), "Gps is turned on!! ",
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        public double getLatitude() {
            return this.latitude;
        }

        public double getLongitude() {
            return this.longitude;
        }

        public double[] getLocation() {
            double[] loc_details = new double[2];
            loc_details[0] = location.getLatitude();
            loc_details[1] = location.getLongitude();
            return loc_details;
        }
    }


    /* ------------------------------ End Get location ----------------------------- */


    /* ------------ Start Updating Server with location info ----------------------- */

    /* ------------ End Updating Server with location info ----------------------- */


    private class GetInfo extends AsyncTask<String, Void, String> {

        String[] tentativeOthers, tentativeMine;
        HashSet<JSONObject> resultSet;

        public void populate() {
            JSONObject temp;
            for (int i = 0; i < userList.length(); i++) {
                // Check if they have the skills and aoi
                try {
                    temp = userList.getJSONObject(i);
                    if (Arrays.asList(teamConstraint).contains(temp.getString("username"))) {
                        teamAdapter.add(new SearchClass(temp.getString("name"), temp.getString("username")));
                    } else if (Arrays.asList(tentativeMine).contains(temp.getString("username"))) {
                        tentativeMineAdapter.add(new SearchClass(temp.getString("name"), temp.getString("username")));
                    } else if (Arrays.asList(tentativeOthers).contains(temp.getString("username"))) {
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

        public void adjustList(ListView listView) {
            ViewGroup.LayoutParams lp = listview.getLayoutParams();

            int totalHeight = 0,
                    desiredWidth = View.MeasureSpec.makeMeasureSpec(listview.getWidth(), View.MeasureSpec.UNSPECIFIED);
            View v = null;

            for (int i = 0; i < teamAdapter.getCount(); i++) {
                v = teamAdapter.getView(i, v, listview);
                if (i == 0) {
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
                String username = getIntent().getExtras().get("username").toString();
                String myurl = "http://" + ip + ":3000/user/" + username;

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
                teamList = jsonObject.getJSONArray("teamwith");
                tenOthersList = jsonObject.getJSONArray("tentative_others");
                tenMineList = jsonObject.getJSONArray("tentative_mine");
                teamConstraint = new String[teamList.length() + 1];
                tentativeOthers = new String[tenOthersList.length()];
                tentativeMine = new String[tenMineList.length()];
                int i;
                for (i = 0; i < teamList.length(); i++) {
                    teamConstraint[i] = teamList.getString(i);
                }
                teamConstraint[i] = username;

                for (i = 0; i < tenOthersList.length(); i++) {
                    tentativeOthers[i] = tenOthersList.getString(i);
                }

                for (i = 0; i < tenMineList.length(); i++) {
                    tentativeMine[i] = tenMineList.getString(i);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }


        public void getAllUsers() {
            try {
                String myurl = "http://" + ip + ":3000/users";

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


                if (teamAdapter == null) {
                    String userName1 = getIntent().getExtras().get("username").toString();
                    teamAdapter = new TeamAdapter(TeamActivity.this, R.layout.team_row_layout, userName1);
                }

                if (tentativeMineAdapter == null) {
                    String userName1 = getIntent().getExtras().get("username").toString();
                    tentativeMineAdapter = new TentativeMineAdapter(TeamActivity.this, R.layout.tentminerow, userName1);
                }

                if (tentOthersAdapter == null) {
                    String userName1 = getIntent().getExtras().get("username").toString();
                    tentOthersAdapter = new TeamAdapter(TeamActivity.this, R.layout.team_row_layout, userName1);
                }
                populate();
            } catch (Exception e) {
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