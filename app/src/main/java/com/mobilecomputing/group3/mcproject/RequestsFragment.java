package com.mobilecomputing.group3.mcproject;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
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

/**
 * Created by snrao on 4/18/16.
 */
public class RequestsFragment extends Fragment {

    ListView listview;
    JSONObject jsonObject;
    JSONArray reqList;
    JSONArray userList;
    HashSet<String> userSet;

    View view;
    String ip = new IP().getIP();

    RequestAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate view
        view = inflater.inflate(R.layout.requests_fragment_layout, container, false);
        GetInfo info = new GetInfo();
        info.execute();
        return view;
    }

    public void refresh(){
        adapter.clearAll();
        GetInfo info = new GetInfo();
        info.execute();
    }


    private class GetInfo extends AsyncTask<String, Void, String> {

        String[] reqConstraint;
        HashSet<JSONObject> resultSet;

        public void populate() {
            JSONObject temp;
            for (int i = 0; i < userList.length(); i++) {
                // Check if they have the skills and aoi
                try {
                    boolean userAdded = false;
                    temp = userList.getJSONObject(i);
                    if (Arrays.asList(reqConstraint).contains(temp.getString("username"))) {
                        adapter.add(new SearchClass(temp.getString("name"), temp.getString("username")));
                    }
                } catch (Exception ex) {
                    Toast.makeText(getActivity(),
                            ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            listview = (ListView) view.findViewById(R.id.listview);
            listview.setAdapter(adapter);

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


        public void getReqList() {
            try {
                String username=getActivity().getIntent().getExtras().get("username").toString();
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
                reqList=jsonObject.getJSONArray("requests");
                reqConstraint=new String[reqList.length()];
                for(int i=0;i<reqList.length();i++){
                    reqConstraint[i]=reqList.getString(i);
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


                if ( adapter == null ) {
                    String userName1=getActivity().getIntent().getExtras().get("username").toString();
                    adapter = new RequestAdapter(getActivity(),R.layout.requestlist,userName1);
                }
                populate();
            }catch(Exception e){
                e.printStackTrace();
            }
        }


        @Override
        protected String doInBackground(String... arg0) {
            getReqList();
            getAllUsers();
            return null;
        }
    }

}
