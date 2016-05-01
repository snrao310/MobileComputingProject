package com.mobilecomputing.group3.mcproject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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

            @Override
            public void onTaskCompleted() {
                try {
                    double latitude = Double.parseDouble(meetReqs.getString("mr_latitude"));
                    double longitude = Double.parseDouble(meetReqs.getString("mr_longitude"));
                    double src_latitude = Double.parseDouble(meetReqs.getString("latitude"));
                    double src_longitude = Double.parseDouble(meetReqs.getString("longitude"));

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
                    Toast.makeText(context, meetReqs.toString(), Toast.LENGTH_LONG).show();
                }

                meetReqs = null;
            }
        };

        OnTaskCompleted2 listener2 = new OnTaskCompleted2() {

            @Override
            public void onTaskCompleted() {
                try {
                    double latitude = Double.parseDouble(meetACReqs.getString("ac_latitude"));
                    double longitude = Double.parseDouble(meetACReqs.getString("ac_longitude"));
                    double src_latitude = Double.parseDouble(meetACReqs.getString("latitude"));
                    double src_longitude = Double.parseDouble(meetACReqs.getString("longitude"));

                    Intent intent2 = new Intent(context, DoSomething.class);
                    Bundle b = new Bundle();

                    b.putString("fromUser", meetACReqs.getString("ac_user"));
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
                            .setContentTitle(meetACReqs.getString("ac_user") + " has accepted your meet request")
                            .setContentText("Tap to accept and open maps")
                            .setContentIntent(pendingIntent)
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
