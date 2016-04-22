package com.mobilecomputing.group3.mcproject;

import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by snrao on 4/18/16.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener {


    View view;
    JSONObject jobj;
    TextView email,name,phone,loc,aoi,skillset;
    JSONObject jsonObject;
    String ip="192.168.0.12";
    String username;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.profile_fragment_layout, container, false);
        email = (TextView) view.findViewById(R.id.pemail);
         name = (TextView) view.findViewById(R.id.pname);
         phone = (TextView) view.findViewById(R.id.pphone);
         loc = (TextView) view.findViewById(R.id.ploc);
         aoi = (TextView) view.findViewById(R.id.paoi);
         skillset = (TextView) view.findViewById(R.id.pskills);

        username=getActivity().getIntent().getExtras().get("username").toString();

        Button editprof=(Button) view.findViewById(R.id.editprof);
        editprof.setOnClickListener(this);

        GetInfo f=new GetInfo();
        f.execute(username);
        return view;
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.editprof){
            Intent intent=new Intent(getActivity(),EditProfileActivity.class);
            Bundle b=getActivity().getIntent().getExtras();
            intent.putExtras(b);
            int reqcode=1;
            startActivityForResult(intent,reqcode);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        GetInfo f=new GetInfo();
        f.execute(username);
    }


    private class GetInfo extends AsyncTask<String, Void, String> {

        public void getuserinfo(String usern) {
            try {
                String myurl = "http://"+ip +":3000/user/"+usern;

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
                Log.i("SEE",jsonObject.getString("aoi"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }


        @Override
        protected void onPostExecute(String s) {
            try {
                name.setText(jsonObject.getString("name"));
                email.setText("Emailyy: " + jsonObject.getString("mail"));
                phone.setText("Phone: " + jsonObject.getString("phone"));
                loc.setText("Location: " + jsonObject.getString("location"));
                aoi.setText("Areas of Interest: " + jsonObject.getString("aoi"));
                skillset.setText("Skillset: "+jsonObject.getString("skillset"));
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... arg0) {
            getuserinfo(arg0[0]);
            return null;
        }
    }

}
