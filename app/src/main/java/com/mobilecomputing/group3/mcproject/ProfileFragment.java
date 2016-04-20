package com.mobilecomputing.group3.mcproject;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by snrao on 4/18/16.
 */
public class ProfileFragment extends Fragment {


    View view;
    JSONObject jobj;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.profile_fragment_layout, container, false);
        TextView email = (TextView) view.findViewById(R.id.pemail);
        TextView name = (TextView) view.findViewById(R.id.pname);
        TextView phone = (TextView) view.findViewById(R.id.phone);
        TextView loc = (TextView) view.findViewById(R.id.ploc);
        TextView aoi = (TextView) view.findViewById(R.id.paoi);
        TextView skillset = (TextView) view.findViewById(R.id.pskills);

        GetInfo f=new GetInfo();
        f.execute();
        return view;
    }


    private class GetInfo extends AsyncTask<String, Void, String> {

        public void getuserinfo() {
            try {
                String username="abc";
                String myurl = "http://10.143.2.185:3000/user/"+username;

                URL url = new URL(myurl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
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
                JSONObject jsonObject = new JSONObject(total.toString());
                Log.i("SEE",line);
                /*JSONArray array = new JSONArray(jsonObject.getString("aoi"));

                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = new JSONObject(array.getString(i));
                    User user = new User(obj.getString("name"), obj.getString("firstname"));
                    users.add(user);
                }*/
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }

        @Override
        protected String doInBackground(String... arg0) {
            getuserinfo();
            return null;
        }
    }

}
