package com.mobilecomputing.group3.mcproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by snrao on 4/22/16.
 */
class TeamAdapter extends ArrayAdapter<String>{

    public TeamAdapter(Context context, String[] values) {
        super(context, R.layout.team_row_layout, values);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater theInflater = LayoutInflater.from(getContext());
        View view=theInflater.inflate(R.layout.team_row_layout, parent, false);
        String member=getItem(position);
        TextView textView=(TextView) view.findViewById(R.id.team_member);
        textView.setText(member);
        return  view;
    }
}
