package com.mobilecomputing.group3.mcproject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DoSomething extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle meetReqs = getIntent().getExtras();

        // First update the server
        GetInfo info = new GetInfo();
        String[] details = new String[6];
        details[0] = meetReqs.getString("src_latitude");
        details[1] = meetReqs.getString("src_longitude");
        details[2] = meetReqs.getString("latitude");
        details[3] = meetReqs.getString("longitude");
        details[4] = meetReqs.getString("fromUser");
        details[5] = meetReqs.getString("toUser");
        info.execute(details);
    }

    private class GetInfo extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... arg0) {
            try {
                String[] details = arg0;

                String myurl =
                        "http://"+new IP().getIP() +
                        ":3000/accept/" +
                        details[4]
                        +"/" + details[5]
                        +"/" + details[0]
                        +"/" + details[1];

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

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            // Then open Google maps
            Bundle meetReqs = getIntent().getExtras();

            double latitude = Double.parseDouble(meetReqs.getString("src_latitude"));
            double longitude = Double.parseDouble(meetReqs.getString("src_longitude"));
            double src_latitude = Double.parseDouble(meetReqs.getString("latitude"));
            double src_longitude = Double.parseDouble(meetReqs.getString("longitude"));

            Intent intent2 = new Intent(
                    android.content.Intent.ACTION_VIEW,
                    Uri.parse("http://maps.google.com/maps?saddr=" +
                            String.valueOf(src_latitude) + "," +
                            String.valueOf(src_longitude) +
                            "&daddr=" + String.valueOf(latitude) + "," +
                            String.valueOf(longitude)));
            intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent2.setPackage("com.google.android.apps.maps");
            startActivity(intent2);
        }
    }
}