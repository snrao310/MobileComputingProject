package com.mobilecomputing.group3.mcproject;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.Buffer;

public class MainActivity extends AppCompatActivity {

    String username,passwd;
    String ip="192.168.0.34";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onSignup(View view){
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }

    public void onLogin(View view){
        //check username and password match or not.
        EditText u=(EditText)findViewById(R.id.username1);
        username=u.getText().toString();
        EditText p=(EditText)findViewById(R.id.pass1);
        passwd=p.getText().toString();
        Checkinfo c=new Checkinfo();
        c.execute(username,passwd);
    }



    private class Checkinfo extends AsyncTask<String, Void, String> {

        String text="";

        public void verify(String u,String p) throws UnsupportedEncodingException {

            BufferedReader reader=null;

            u = "suresh";
            p = "s";


            String data = URLEncoder.encode("username", "UTF-8")
                    + "=" + URLEncoder.encode(u, "UTF-8");
            data += "&" + URLEncoder.encode("password", "UTF-8")
                    + "=" + URLEncoder.encode(p, "UTF-8");

            try {
                // Defined URL  where to send data
                URL url = new URL("http://"+ip+":3000/check");

                // Send POST data request
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
                //conn.connect();
                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                wr.write(data);
                wr.flush();

                // Get the server response
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = null;

                // Read Server Response
                while ((line = reader.readLine()) != null) {
                    // Append server response in string
                    sb.append(line );
                }
                text = sb.toString();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            finally {
                try {
                    reader.close();
                } catch (Exception ex) {
                }
            }
            return;
        }


        @Override
        protected void onPostExecute(String s) {
            if(text.equals("0"))
                Toast.makeText(MainActivity.this,"Username and Password don't match", Toast.LENGTH_LONG).show();
            else{
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                Bundle b=new Bundle();
                b.putString("username",username);
                intent.putExtras(b);
                startActivity(intent);
            }
        }

        @Override
        protected String doInBackground(String... arg0) {
            try {
                verify(arg0[0],arg0[1]);
            } catch (Exception ex) {}
            return null;
        }
    }
}

//need to check if anyone with same username exists while creating user
//need to include team members in schema