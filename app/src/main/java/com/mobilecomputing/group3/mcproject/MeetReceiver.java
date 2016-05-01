package com.mobilecomputing.group3.mcproject;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Debug;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

interface OnTaskCompleted{
    public void onTaskCompleted();
}

public class MeetReceiver extends BroadcastReceiver {
    String username = "";
    JSONObject meetReqs;
    Intent i;

    public MeetReceiver() {
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        i = intent;
        username = intent.getExtras().getString("username");
        Toast.makeText(context, "HELLO WORLD", Toast.LENGTH_LONG).show();

        OnTaskCompleted listener = new OnTaskCompleted() {

            @Override
            public void onTaskCompleted() {
                try {
                    Toast.makeText(context, "HELLO WORLD", Toast.LENGTH_LONG).show();
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
                            .setContentTitle("Friend request from " + username)
                            .setContentText("Tap to share location and open maps")
                            .setContentIntent(pendingIntent)
                            .setDefaults(Notification.DEFAULT_SOUND)
                            .setAutoCancel(true);

                    NotificationManager notificationmanager = (NotificationManager) context
                            .getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationmanager.notify(0, mBuilder.build());

                    // After notification, clear the database using

                } catch(Exception ex) {
                    Toast.makeText(context, meetReqs.toString(), Toast.LENGTH_LONG).show();
                }

                meetReqs = null;
            }
        };
        GetMeetRequestsTask getReqs = new GetMeetRequestsTask(listener);
        getReqs.execute();
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
}
