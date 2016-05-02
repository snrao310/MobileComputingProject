package com.mobilecomputing.group3.mcproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

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

            double[] location = (new LocList()).getLocation();

            double latitude = location[0];
            double longitude = location[1];
//            double src_latitude = Double.parseDouble(meetReqs.getString("latitude"));
//            double src_longitude = Double.parseDouble(meetReqs.getString("longitude"));

            double src_latitude = location[0];
            double src_longitude = location[1];
            double mr_latitude = Double.parseDouble(meetReqs.getString("latitude"));
            double mr_longitude = Double.parseDouble(meetReqs.getString("longitude"));

            Intent intent2 = new Intent(
                    android.content.Intent.ACTION_VIEW,
                    Uri.parse("http://maps.google.com/maps?saddr=" +
                            String.valueOf(src_latitude) + "," +
                            String.valueOf(src_longitude) +
                            "&daddr=" + String.valueOf(mr_latitude) + "," +
                            String.valueOf(mr_longitude)));
            intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent2.setPackage("com.google.android.apps.maps");
            startActivity(intent2);
        }


        class LocList implements LocationListener {
            private LocationManager locMgr;
            private double latitude, longitude;
            private Location location;

            public LocList() {
                try {
                    locMgr = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
//                    locMgr.requestLocationUpdates( LocationManager.GPS_PROVIDER, 2000, 10, this);
                    String mBestProvider = locMgr.getBestProvider(new Criteria(), true);
                    locMgr.requestLocationUpdates(mBestProvider, 0, 0, this);
                    Location location_g = locMgr.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    Location location_n = locMgr.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if ( location_g != null ) {
                        this.location = location_g;
                    }
                    if ( location_g == null && location_n != null ) {
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
                Toast.makeText(getApplication(), "Gps is turned off!! ",
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
    }
}