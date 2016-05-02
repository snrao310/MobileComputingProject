package com.mobilecomputing.group3.mcproject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

interface OnTaskCompleted{
    public void onTaskCompleted();
}

interface OnTaskCompleted2{
    public void onTaskCompleted();
}

public class MeetReceiver extends BroadcastReceiver {
    String username = "";
    JSONObject meetReqs;
    JSONObject meetACReqs;
    Intent i;

    public MeetReceiver() {
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        i = intent;
        username = intent.getExtras().getString("username");

        OnTaskCompleted listener = new OnTaskCompleted() {

            class LocList implements LocationListener {
                private LocationManager locMgr;
                private double latitude, longitude;
                private Location location;

                public LocList() {
                    try {
                        locMgr = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                        locMgr.requestLocationUpdates( LocationManager.GPS_PROVIDER, 2000, 10, this);
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
                    context.startActivity(intent);
                    Toast.makeText(context, "Gps is turned off!! ",
                            Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onProviderEnabled(String provider) {
                    Toast.makeText(context, "Gps is turned on!! ",
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

            @Override
            public void onTaskCompleted() {
                try {
                    double latitude = Double.parseDouble(meetReqs.getString("mr_latitude"));
                    double longitude = Double.parseDouble(meetReqs.getString("mr_longitude"));

                    Log.i("MR_LAT: ", String.valueOf(latitude));
                    Log.i("MR_LON: ", String.valueOf(longitude));
                    Toast.makeText(context, "MR_LAT: " + String.valueOf(latitude), Toast.LENGTH_LONG).show();
                    Toast.makeText(context, "MR_LON: " + String.valueOf(longitude), Toast.LENGTH_LONG).show();

                    double[] location = (new LocList()).getLocation();

                    double src_latitude = location[0];
                    double src_longitude = location[1];

                    Log.i("LAT: ", String.valueOf(src_latitude));
                    Log.i("LON: ", String.valueOf(src_longitude));

                    Intent intent2 = new Intent(context, DoSomething.class);
                    Bundle b = new Bundle();

                    b.putString("fromUser", meetReqs.getString("mr_user"));
                    b.putString("toUser", username);
                    b.putString("latitude", String.valueOf(latitude));
                    b.putString("longitude", String.valueOf(longitude));
                    b.putString("src_latitude", String.valueOf(src_latitude));
                    b.putString("src_longitude", String.valueOf(src_longitude));

                    intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent2.putExtras(b);

                    /*
                    Send a notification to the user stating that there's a meeting request
                    */
                    PendingIntent pendingIntent = PendingIntent.getActivity(
                            context,
                            0,
                            intent2,
                            0
                    );

                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                            context)
                            .setSmallIcon(R.drawable.mr_ic_cast_dark)
                            .setContentTitle("Meeting request from " + meetReqs.getString("mr_user"))
                            .setContentText("Tap to accept and open maps")
                            .setContentIntent(pendingIntent)
                            .setDefaults(Notification.DEFAULT_SOUND)
                            .setAutoCancel(true);

                    NotificationManager notificationmanager = (NotificationManager) context
                            .getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationmanager.notify(0, mBuilder.build());

                } catch(Exception ex) {
                    Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
                }

                meetReqs = null;
            }
        };

        OnTaskCompleted2 listener2 = new OnTaskCompleted2() {

            @Override
            public void onTaskCompleted() {
                try {
//                    double latitude = Double.parseDouble(meetACReqs.getString("ac_latitude"));
//                    double longitude = Double.parseDouble(meetACReqs.getString("ac_longitude"));
//                    double src_latitude = Double.parseDouble(meetACReqs.getString("latitude"));
//                    double src_longitude = Double.parseDouble(meetACReqs.getString("longitude"));

//                    Intent intent2 = new Intent(context, DoSomething.class);
//                    Bundle b = new Bundle();
//
//                    b.putString("fromUser", meetACReqs.getString("ac_user"));
//                    b.putString("toUser", username);
//                    b.putString("latitude", String.valueOf(latitude));
//                    b.putString("longitude", String.valueOf(longitude));
//                    b.putString("src_latitude", String.valueOf(src_latitude));
//                    b.putString("src_longitude", String.valueOf(src_longitude));
//
//                    intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    intent2.putExtras(b);
//
//                    /*
//                    Send a notification to the user stating that there's a meeting request
//                    */
//                    PendingIntent pendingIntent = PendingIntent.getActivity(
//                            context,
//                            0,
//                            intent2,
//                            0
//                    );

                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                            context)
                            .setSmallIcon(R.drawable.mr_ic_cast_dark)
                            .setContentTitle(meetACReqs.getString("ac_user") + " has accepted your meet request")
                            .setContentText("Tap to accept and open maps")
                            .setDefaults(Notification.DEFAULT_SOUND)
                            .setAutoCancel(true);

                    NotificationManager notificationmanager = (NotificationManager) context
                            .getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationmanager.notify(0, mBuilder.build());

                } catch(Exception ex) {
                    Toast.makeText(context, meetACReqs.toString(), Toast.LENGTH_LONG).show();
                }

                meetACReqs = null;
            }
        };

        GetMeetRequestsTask getReqs = new GetMeetRequestsTask(listener);
        getReqs.execute();
        GetACMeetRequestsTask getACReqs = new GetACMeetRequestsTask(listener2);
        getACReqs.execute();
    }

    class GetMeetRequestsTask extends AsyncTask<String, Void, String> {
        public JSONObject meetRequester;
        private OnTaskCompleted listener;

        public GetMeetRequestsTask(OnTaskCompleted listener) {
            this.listener = listener;
        }

        @Override
        protected String doInBackground(String ... details) {
            try {
                meetRequester = null;

                URL url = new URL("http://" + new IP().getIP() + ":3000/getmeetrequests/" + username);
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();

                conn.setRequestMethod("GET");
                conn.connect();
                InputStream inputStream = conn.getInputStream();

                if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    Log.i("ERROR: ", "Server returned HTTP " + conn.getResponseCode()
                            + " " + conn.getResponseMessage());
                }

                BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder total = new StringBuilder();
                String line;

                while ((line = r.readLine()) != null) {
                    total.append(line);
                }

                if ( !total.toString().equals("No")) {
                    meetRequester = new JSONObject(total.toString());
                }
            } catch(Exception ex) {
                return null;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (meetRequester != null) {
                meetReqs = meetRequester;
                listener.onTaskCompleted();
            }
        }
    }


    class GetACMeetRequestsTask extends AsyncTask<String, Void, String> {
        public JSONObject meetRequester;
        private OnTaskCompleted2 listener;

        public GetACMeetRequestsTask(OnTaskCompleted2 listener) {
            this.listener = listener;
        }

        @Override
        protected String doInBackground(String ... details) {
            try {
                meetRequester = null;

                URL url = new URL("http://" + new IP().getIP() + ":3000/getaccepted/" + username);
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();

                conn.setRequestMethod("GET");
                conn.connect();
                InputStream inputStream = conn.getInputStream();

                if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    Log.i("ERROR: ", "Server returned HTTP " + conn.getResponseCode()
                            + " " + conn.getResponseMessage());
                }

                BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder total = new StringBuilder();
                String line;

                while ((line = r.readLine()) != null) {
                    total.append(line);
                }

                if ( !total.toString().equals("No")) {
                    meetRequester = new JSONObject(total.toString());
                }
            } catch(Exception ex) {
                return null;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (meetRequester != null) {
                meetACReqs = meetRequester;
                listener.onTaskCompleted();
            }
        }
    }
}
