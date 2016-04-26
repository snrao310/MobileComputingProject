
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
 * Created by snrao on 4/23/16.
 */
public class TentativeMineAdapter extends  ArrayAdapter{

    private List list= new ArrayList();
    private final int MAX_ENTRIES = 5;
    String userName2,userName1;
    String ip=new IP().getIP();
    Button globalSelectedButton, globalOtherButton1,globalOtherButton2;

    public TentativeMineAdapter(Context context, int resource, String userName1) {
        super(context, R.layout.tentminerow);
        this.userName1=userName1;
    }


    static class SearchHolder
    {
        TextView NAME;
        Button accept;
        Button reject;
    }


    public void add(SearchClass object) {
        list.add(object);
        super.add(object);
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
            row = inflater.inflate(R.layout.tentminerow,parent,false);
            holder = new SearchHolder();
            holder.NAME = (TextView) row.findViewById(R.id.tenttxtName);
            row.setTag(holder);
        }
        else
        {
            holder = (SearchHolder) row.getTag();
        }

        SearchClass FR = (SearchClass) getItem(position);
        holder.NAME.setText(FR.getName());


        holder.accept=(Button) row.findViewById(R.id.tentacceptBtn);
        holder.reject=(Button) row.findViewById(R.id.tentrejectBtn);
        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    globalSelectedButton=holder.accept;
                    globalOtherButton1=holder.reject;
//                      Toast.makeText(getContext(), userList.getJSONObject(position).get("username").toString(), Toast.LENGTH_LONG).show();
                    SearchClass FR=(SearchClass) getItem(position);
                    userName2=FR.getUsername();
                    SendInfo s=new SendInfo();
                    s.execute("accept");
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        });

        holder.reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    globalSelectedButton=holder.reject;
                    globalOtherButton1=holder.accept;
//                      Toast.makeText(getContext(), userList.getJSONObject(position).get("username").toString(), Toast.LENGTH_LONG).show();
                    SearchClass FR=(SearchClass) getItem(position);
                    userName2=FR.getUsername();
                    SendInfo s=new SendInfo();
                    s.execute("reject");
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        });

        return row;
    }



















    class SendInfo extends AsyncTask<String, Void, String> {

        String text="";

        public void sendAccInfo() throws UnsupportedEncodingException {
            // Create data variable for sent values to server
            String data = URLEncoder.encode("fromUsername", "UTF-8")
                    + "=" + URLEncoder.encode(userName2, "UTF-8");

            data += "&" + URLEncoder.encode("toUsername", "UTF-8") + "="
                    + URLEncoder.encode(userName1, "UTF-8");

            BufferedReader reader = null;

            // Send data
            try {
                // Defined URL  where to send data
                URL url = new URL("http://"+ip+":3000/accept");

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


        public void sendRejInfo() throws UnsupportedEncodingException {
            // Create data variable for sent values to server
            String data = URLEncoder.encode("fromUsername", "UTF-8")
                    + "=" + URLEncoder.encode(userName2, "UTF-8");

            data += "&" + URLEncoder.encode("toUsername", "UTF-8") + "="
                    + URLEncoder.encode(userName1, "UTF-8");

            BufferedReader reader = null;

            // Send data
            try {
                // Defined URL  where to send data
                URL url = new URL("http://"+ip+":3000/reject");

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



        public void sendTenInfo() throws UnsupportedEncodingException {
            // Create data variable for sent values to server
            String data = URLEncoder.encode("fromUsername", "UTF-8")
                    + "=" + URLEncoder.encode(userName2, "UTF-8");

            data += "&" + URLEncoder.encode("toUsername", "UTF-8") + "="
                    + URLEncoder.encode(userName1, "UTF-8");

            BufferedReader reader = null;

            // Send data
            try {
                // Defined URL  where to send data
                URL url = new URL("http://"+ip+":3000/tentative");

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
                if(params[0].equals("accept"))
                    sendAccInfo();
                else if(params[0].equals("reject"))
                    sendRejInfo();
            } catch (Exception ex) {}
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if(text.equals("rejected")){
                Log.i("ABC", text);
                Toast.makeText(getContext(), "Rejected", Toast.LENGTH_LONG).show();
                globalSelectedButton.setText("Rejected");
                globalSelectedButton.setClickable(false);
                globalSelectedButton.setBackgroundColor(Color.LTGRAY);
                globalOtherButton1.setVisibility(View.GONE);
            }
            else if(text.equals("accepted")) {
                Log.i("ABC", text);
                Toast.makeText(getContext(), "Accepted", Toast.LENGTH_LONG).show();
                globalSelectedButton.setText("Accepted");
                globalSelectedButton.setClickable(false);
                globalSelectedButton.setBackgroundColor(Color.LTGRAY);
                globalOtherButton1.setVisibility(View.GONE);
            }
        }
    }
}
