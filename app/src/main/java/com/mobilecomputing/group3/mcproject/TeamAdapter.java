package com.mobilecomputing.group3.mcproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by snrao on 4/22/16.
 */
class TeamAdapter extends ArrayAdapter {

    private List list = new ArrayList();
    private final int MAX_ENTRIES = 5;
    String userName2, userName1;
    String ip = new IP().getIP();
    Button globalSelectedButton, globalOtherButton1, globalOtherButton2;

    public TeamAdapter(Context context, int resource, String userName1) {
        super(context, R.layout.team_row_layout);
        this.userName1 = userName1;
    }


    static class SearchHolder {
        TextView NAME;
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

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.team_row_layout, parent, false);
            holder = new SearchHolder();
            holder.NAME = (TextView) row.findViewById(R.id.team_member);
            row.setTag(holder);
        } else {
            holder = (SearchHolder) row.getTag();
        }

        SearchClass FR = (SearchClass) getItem(position);
        holder.NAME.setText(FR.getName());
        return row;
    }
}
