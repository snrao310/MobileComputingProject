package com.mobilecomputing.group3.mcproject;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.location.Address;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;


/**
 * Created by snrao on 12/4/16.
 */
public class SignupFragment extends Fragment implements View.OnClickListener{

    View view;


    String userName,passWord,selectedLocation ,eMail ,pHone ;
   // TextView content;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view= inflater.inflate(R.layout.signup_fragment,container,false);
        Button setLoc=(Button) view.findViewById(R.id.locSelect);
        setLoc.setOnClickListener(this);

        Button signup=(Button) view.findViewById(R.id.submit);
        signup.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.locSelect) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            GMapFragment mapFragment = new GMapFragment();
            fragmentTransaction.replace(R.id.content_frame, mapFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
        else if(v.getId()==R.id.submit){

            EditText userName_field = (EditText) view.findViewById(R.id.usernamebox);
            EditText passWord_field=(EditText) view.findViewById(R.id.passwdbox);
            EditText selectedLocation_field=(EditText) view.findViewById(R.id.locationbox);
            EditText email_field=(EditText) view.findViewById(R.id.emailbox);
            EditText phone_field=(EditText) view.findViewById(R.id.Phonebox);



             userName = userName_field.getText().toString();
             passWord = passWord_field.getText().toString();
             selectedLocation = selectedLocation_field.getText().toString();
             eMail = email_field.getText().toString();
             pHone = phone_field.getText().toString();

            /*

            try{

                // CALL GetText method to make post method call
                GetText();
            }
            catch(Exception ex)
            {
               // content.setText(" url exeption! " );
            }


        */







        }
    }

    class FileUploader extends AsyncTask<String, Void, String> {
    public  void  GetText()  throws UnsupportedEncodingException {

        // Create data variable for sent values to server

        String data = URLEncoder.encode("username", "UTF-8")
                + "=" + URLEncoder.encode(userName, "UTF-8");

        data += "&" + URLEncoder.encode("email", "UTF-8") + "="
                + URLEncoder.encode(passWord, "UTF-8");

        data += "&" + URLEncoder.encode("latitude", "UTF-8")
                + "=" + URLEncoder.encode(selectedLocation, "UTF-8");

        data += "&" + URLEncoder.encode("longitude", "UTF-8")
                + "=" + URLEncoder.encode(eMail, "UTF-8");

        data += "&" + URLEncoder.encode("mail", "UTF-8")
                + "=" + URLEncoder.encode(eMail, "UTF-8");

        data += "&" + URLEncoder.encode("phone", "UTF-8")
                + "=" + URLEncoder.encode(pHone, "UTF-8");

        String text = "";
        BufferedReader reader = null;


        // Send data
        try {
            // Defined URL  where to send data
            URL url = new URL("http://10.143.128.43:3000/friends");

            // Send POST data request

            URLConnection conn = url.openConnection();
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
                sb.append(line + "\n");
            }


            text = sb.toString();
        } catch (Exception ex) {

        } finally {
            try {

                reader.close();
            } catch (Exception ex) {
            }
        }

        // Show response on activity
        //  content.setText( text  );


    }
        @Override
        protected String doInBackground(String... params) {

            try{

                // CALL GetText method to make post method call
                GetText();
            }
            catch(Exception ex)
            {
                // content.setText(" url exeption! " );
            }
            return null;
        }

}

    public void update(Address curraddr){
        EditText t=(EditText) view.findViewById(R.id.locationbox);
        t.setText(curraddr.getAddressLine(0)+", "+curraddr.getAddressLine(1)+" "+curraddr.getAddressLine(2)+" "+curraddr.getAddressLine(3));
    }
}




