package com.mobilecomputing.group3.mcproject;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by snrao on 4/18/16.
 */
public class ProfileFragment extends Fragment{


    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.profile_fragment_layout, container, false);
        TextView email= (TextView) view.findViewById(R.id.pemail);
        TextView name=(TextView) view.findViewById(R.id.pname);
        TextView phone=(TextView) view.findViewById(R.id.phone);
        TextView loc=(TextView) view.findViewById(R.id.ploc);
        TextView aoi=(TextView) view.findViewById(R.id.paoi);
        TextView skillset=(TextView) view.findViewById(R.id.pskills);



        return view;
    }


    class FileUploader extends AsyncTask<String, Void, String> {


        public void getContent() throws UnsupportedEncodingException {

        }

        @Override
        protected String doInBackground(String... params) {
            try {
                // CALL GetText method to make post method call
                ;
            } catch (Exception ex) {}
            return null;
        }
    }
}
