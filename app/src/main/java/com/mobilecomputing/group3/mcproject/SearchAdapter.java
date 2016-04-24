package com.mobilecomputing.group3.mcproject;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sureshgururajan on 4/19/16.
 */
public class SearchAdapter extends ArrayAdapter {
    private List list= new ArrayList();
    private final int MAX_ENTRIES = 5;
    JSONArray userList;
    String userName2,userName1;
    String ip=new IP().getIP();
    Button globalButton;

    public SearchAdapter(Context context, int resource,String userName1) {
        super(context, resource);
        this.userList=userList;
        this.userName1=userName1;
    }

    public void add(SearchClass object) {
        list.add(object);
        super.add(object);
    }

    static class SearchHolder
    {
        TextView NAME;
        Button meet;
        Button add;
    }

    public void clearAll() {
        super.clear();
        list.clear();
    }

    @Override
    public int getCount() {
//        return Math.min(MAX_ENTRIES, this.list.size());
        return this.list.size();
    }

    @Override
    public Object getItem(int position) {
        return this.list.get(position);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row;
        row = convertView;
        final SearchHolder holder;

        if(convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.userlist,parent,false);
            holder = new SearchHolder();
            holder.NAME = (TextView) row.findViewById(R.id.txtUsername);
            row.setTag(holder);
        }
        else
        {
            holder = (SearchHolder) row.getTag();
        }

        SearchClass FR = (SearchClass) getItem(position);
        holder.NAME.setText(FR.getName());


        holder.add=(Button) row.findViewById(R.id.reqButton);
        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    globalButton=holder.add;
                    //Toast.makeText(getContext(), userList.getJSONObject(position).get("username").toString(), Toast.LENGTH_LONG).show();
                    SearchClass FR=(SearchClass)getItem(position);
                    userName2=FR.getUsername();
                    SendInfo s=new SendInfo();
                    s.execute();
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        });
        return row;
    }



















    class SendInfo extends AsyncTask<String, Void, String> {

        String text="";

        public void sendReqInfo() throws UnsupportedEncodingException {
            // Create data variable for sent values to server
            String data = URLEncoder.encode("fromUsername", "UTF-8")
                    + "=" + URLEncoder.encode(userName1, "UTF-8");

            data += "&" + URLEncoder.encode("toUsername", "UTF-8") + "="
                    + URLEncoder.encode(userName2, "UTF-8");

            BufferedReader reader = null;

            // Send data
            try {
                // Defined URL  where to send data
                URL url = new URL("http://"+ip+":3000/request");

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
                    sb.append(line);
                }
                text = sb.toString();
            } catch (Exception ex) {}
            finally {
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
                sendReqInfo();
            } catch (Exception ex) {}
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if(text.equals("0")){
                Toast.makeText(getContext(),"Error Request couldn't be sent",Toast.LENGTH_LONG).show();
            }
            else {
                Log.i("ABC", text);
                Toast.makeText(getContext(), "Request Sent", Toast.LENGTH_LONG).show();
                globalButton.setText("Request Sent");
                globalButton.setClickable(false);
                globalButton.setBackgroundColor(Color.LTGRAY);
            }
        }
    }
}
