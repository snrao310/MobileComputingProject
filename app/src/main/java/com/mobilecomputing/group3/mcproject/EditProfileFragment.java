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
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by snrao on 4/21/16.
 */
public class EditProfileFragment extends Fragment implements View.OnClickListener {

    View view;
    Address addr;
    String ip=new IP().getIP();


    String name, userName, passWord, selectedLocation, eMail, pHone, confirmPassword, aoi, skillset;
    double lat, lon;
    // TextView content;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.editprofile_fragment, container, false);
        Button setLoc = (Button) view.findViewById(R.id.new_locSelect);
        setLoc.setOnClickListener(this);

        Button signup = (Button) view.findViewById(R.id.new_submit);
        signup.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.new_locSelect) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            GMapFragment mapFragment = new GMapFragment();
            fragmentTransaction.replace(R.id.editp_content_frame, mapFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else if (v.getId() == R.id.new_submit) {
            EditText passWord_field = (EditText) view.findViewById(R.id.new_passwdbox);
            EditText confirmPassword_field = (EditText) view.findViewById(R.id.new_confpasswdbox);
            EditText selectedLocation_field = (EditText) view.findViewById(R.id.new_locationbox);
            EditText email_field = (EditText) view.findViewById(R.id.new_emailbox);
            EditText phone_field = (EditText) view.findViewById(R.id.new_Phonebox);
            EditText aoi_field = (EditText) view.findViewById(R.id.new_aoibox);
            EditText skillset_field = (EditText) view.findViewById(R.id.new_skillsetbox);
            EditText name_field = (EditText) view.findViewById(R.id.new_namebox);

            userName = getActivity().getIntent().getExtras().get("username").toString();
            passWord = passWord_field.getText().toString();
            name = name_field.getText().toString();
            confirmPassword = confirmPassword_field.getText().toString();
            selectedLocation = selectedLocation_field.getText().toString();
            if(!selectedLocation.equals("")) {
                lat = addr.getLatitude();
                lon = addr.getLongitude();
            }
            eMail = email_field.getText().toString();
            pHone = phone_field.getText().toString();
            aoi = aoi_field.getText().toString();
            skillset = skillset_field.getText().toString();

            if (!confirmPassword.equals(passWord)) {
                Toast.makeText(getActivity(), " Password did not match . Check the Password",
                        Toast.LENGTH_SHORT).show();
            } else {
                SendInfo t = new SendInfo();
                t.execute();
            }
        }
    }


    class SendInfo extends AsyncTask<String, Void, String> {


        public void sendRegInfo() throws UnsupportedEncodingException {
            // Create data variable for sent values to server
            String data = URLEncoder.encode("username", "UTF-8")
                        + "=" + URLEncoder.encode(userName, "UTF-8");

            if (!passWord.equals(""))
                data += "&" + URLEncoder.encode("password", "UTF-8") + "="
                        + URLEncoder.encode(passWord, "UTF-8");

            if (!name.equals(""))
                data += "&" + URLEncoder.encode("name", "UTF-8")
                        + "=" + URLEncoder.encode(name, "UTF-8");

            if (!selectedLocation.equals(""))
                data += "&" + URLEncoder.encode("location", "UTF-8")
                        + "=" + URLEncoder.encode(selectedLocation, "UTF-8");

            if (!eMail.equals(""))
                data += "&" + URLEncoder.encode("mail", "UTF-8")
                        + "=" + URLEncoder.encode(eMail, "UTF-8");

            if (!pHone.equals(""))
                data += "&" + URLEncoder.encode("phone", "UTF-8")
                        + "=" + URLEncoder.encode(pHone, "UTF-8");

            if (!aoi.equals(""))
                data += "&" + URLEncoder.encode("aoi", "UTF-8")
                        + "=" + URLEncoder.encode(aoi, "UTF-8");

            if (!skillset.equals(""))
                data += "&" + URLEncoder.encode("skillset", "UTF-8")
                        + "=" + URLEncoder.encode(skillset, "UTF-8");

            if (!selectedLocation.equals(""))
                data += "&" + URLEncoder.encode("latitude", "UTF-8")
                        + "=" + URLEncoder.encode(String.valueOf(lat), "UTF-8");

            if (!selectedLocation.equals(""))
                data += "&" + URLEncoder.encode("longitude", "UTF-8")
                        + "=" + URLEncoder.encode(String.valueOf(lon), "UTF-8");

            String text = "";
            BufferedReader reader = null;

            // Send data
            try {
                // Defined URL  where to send data
                URL url = new URL("http://" + ip + ":3000/update");

                // Send POST data request
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
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
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                // CALL GetText method to make post method call
                sendRegInfo();
            } catch (Exception ex) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            getActivity().finish();
        }
    }


    public void update(Address curraddr) {
        addr = curraddr;
        EditText t = (EditText) view.findViewById(R.id.new_locationbox);
        String addrlines = new String();
        addrlines += curraddr.getAddressLine(0);
        if (curraddr.getAddressLine(1) != null)
            addrlines += ", " + curraddr.getAddressLine(1);
        if (curraddr.getAddressLine(2) != null)
            addrlines += ", " + curraddr.getAddressLine(2);
        if (curraddr.getAddressLine(3) != null)
            addrlines += ", " + curraddr.getAddressLine(3);
        t.setText(addrlines);
    }
}
